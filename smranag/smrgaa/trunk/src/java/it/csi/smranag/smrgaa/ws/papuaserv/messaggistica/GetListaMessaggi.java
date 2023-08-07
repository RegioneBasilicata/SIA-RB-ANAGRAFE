
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getListaMessaggi complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getListaMessaggi">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idProcedimento" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codiceRuolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoMessaggio" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="letto" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="obbligatorio" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="visibile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getListaMessaggi", propOrder = {
    "idProcedimento",
    "codiceRuolo",
    "codiceFiscale",
    "tipoMessaggio",
    "letto",
    "obbligatorio",
    "visibile"
})
public class GetListaMessaggi {

    protected int idProcedimento;
    protected String codiceRuolo;
    protected String codiceFiscale;
    protected int tipoMessaggio;
    protected Boolean letto;
    protected Boolean obbligatorio;
    protected Boolean visibile;

    /**
     * Gets the value of the idProcedimento property.
     * 
     */
    public int getIdProcedimento() {
        return idProcedimento;
    }

    /**
     * Sets the value of the idProcedimento property.
     * 
     */
    public void setIdProcedimento(int value) {
        this.idProcedimento = value;
    }

    /**
     * Gets the value of the codiceRuolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceRuolo() {
        return codiceRuolo;
    }

    /**
     * Sets the value of the codiceRuolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceRuolo(String value) {
        this.codiceRuolo = value;
    }

    /**
     * Gets the value of the codiceFiscale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Sets the value of the codiceFiscale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Gets the value of the tipoMessaggio property.
     * 
     */
    public int getTipoMessaggio() {
        return tipoMessaggio;
    }

    /**
     * Sets the value of the tipoMessaggio property.
     * 
     */
    public void setTipoMessaggio(int value) {
        this.tipoMessaggio = value;
    }

    /**
     * Gets the value of the letto property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLetto() {
        return letto;
    }

    /**
     * Sets the value of the letto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLetto(Boolean value) {
        this.letto = value;
    }

    /**
     * Gets the value of the obbligatorio property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isObbligatorio() {
        return obbligatorio;
    }

    /**
     * Sets the value of the obbligatorio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setObbligatorio(Boolean value) {
        this.obbligatorio = value;
    }

    /**
     * Gets the value of the visibile property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVisibile() {
        return visibile;
    }

    /**
     * Sets the value of the visibile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVisibile(Boolean value) {
        this.visibile = value;
    }

}
