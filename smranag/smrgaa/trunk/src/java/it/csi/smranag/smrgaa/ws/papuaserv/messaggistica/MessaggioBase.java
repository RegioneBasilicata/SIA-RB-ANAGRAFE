
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for messaggioBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="messaggioBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataInizioValidita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idElencoMessaggi" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idTipoMessaggio" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="letto" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="letturaObbligatoria" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="titolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messaggioBase", propOrder = {
    "dataInizioValidita",
    "idElencoMessaggi",
    "idTipoMessaggio",
    "letto",
    "letturaObbligatoria",
    "titolo"
})
@XmlSeeAlso({
    DettagliMessaggio.class,
    Messaggio.class
})
public abstract class MessaggioBase {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInizioValidita;
    protected long idElencoMessaggi;
    protected long idTipoMessaggio;
    protected boolean letto;
    protected boolean letturaObbligatoria;
    protected String titolo;

    /**
     * Gets the value of the dataInizioValidita property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizioValidita() {
        return dataInizioValidita;
    }

    /**
     * Sets the value of the dataInizioValidita property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizioValidita(XMLGregorianCalendar value) {
        this.dataInizioValidita = value;
    }

    /**
     * Gets the value of the idElencoMessaggi property.
     * 
     */
    public long getIdElencoMessaggi() {
        return idElencoMessaggi;
    }

    /**
     * Sets the value of the idElencoMessaggi property.
     * 
     */
    public void setIdElencoMessaggi(long value) {
        this.idElencoMessaggi = value;
    }

    /**
     * Gets the value of the idTipoMessaggio property.
     * 
     */
    public long getIdTipoMessaggio() {
        return idTipoMessaggio;
    }

    /**
     * Sets the value of the idTipoMessaggio property.
     * 
     */
    public void setIdTipoMessaggio(long value) {
        this.idTipoMessaggio = value;
    }

    /**
     * Gets the value of the letto property.
     * 
     */
    public boolean isLetto() {
        return letto;
    }

    /**
     * Sets the value of the letto property.
     * 
     */
    public void setLetto(boolean value) {
        this.letto = value;
    }

    /**
     * Gets the value of the letturaObbligatoria property.
     * 
     */
    public boolean isLetturaObbligatoria() {
        return letturaObbligatoria;
    }

    /**
     * Sets the value of the letturaObbligatoria property.
     * 
     */
    public void setLetturaObbligatoria(boolean value) {
        this.letturaObbligatoria = value;
    }

    /**
     * Gets the value of the titolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Sets the value of the titolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitolo(String value) {
        this.titolo = value;
    }

}
