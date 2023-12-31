//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.02 at 04:07:28 PM CET 
//


package it.csi.smranag.smrgaa.dto.jaxb.fascicolo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Ute"/>
 *         &lt;element ref="{}CodiceAsl"/>
 *         &lt;element ref="{}Specie"/>
 *         &lt;element ref="{}TipoProduzione"/>
 *         &lt;element ref="{}OrientamentoProduttivo"/>
 *         &lt;element ref="{}TipologiaAssicurativa"/>
 *         &lt;element ref="{}ElencoCategorie"/>
 *         &lt;element ref="{}RigaTotaliAllev"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ute",
    "codiceAsl",
    "specie",
    "tipoProduzione",
    "orientamentoProduttivo",
    "tipologiaAssicurativa",
    "elencoCategorie",
    "rigaTotaliAllev"
})
@XmlRootElement(name = "Allevamento")
public class Allevamento {

    @XmlElement(name = "Ute", required = true)
    protected String ute;
    @XmlElement(name = "CodiceAsl", required = true)
    protected String codiceAsl;
    @XmlElement(name = "Specie", required = true)
    protected String specie;
    @XmlElement(name = "TipoProduzione", required = true)
    protected String tipoProduzione;
    @XmlElement(name = "OrientamentoProduttivo", required = true)
    protected String orientamentoProduttivo;
    @XmlElement(name = "TipologiaAssicurativa", required = true)
    protected String tipologiaAssicurativa;
    @XmlElement(name = "ElencoCategorie", required = true)
    protected ElencoCategorie elencoCategorie;
    @XmlElement(name = "RigaTotaliAllev", required = true)
    protected RigaTotaliAllev rigaTotaliAllev;

    /**
     * Gets the value of the ute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUte() {
        return ute;
    }

    /**
     * Sets the value of the ute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUte(String value) {
        this.ute = value;
    }

    /**
     * Gets the value of the codiceAsl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceAsl() {
        return codiceAsl;
    }

    /**
     * Sets the value of the codiceAsl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceAsl(String value) {
        this.codiceAsl = value;
    }

    /**
     * Gets the value of the specie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecie() {
        return specie;
    }

    /**
     * Sets the value of the specie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecie(String value) {
        this.specie = value;
    }

    /**
     * Gets the value of the tipoProduzione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProduzione() {
        return tipoProduzione;
    }

    /**
     * Sets the value of the tipoProduzione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProduzione(String value) {
        this.tipoProduzione = value;
    }

    /**
     * Gets the value of the orientamentoProduttivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrientamentoProduttivo() {
        return orientamentoProduttivo;
    }

    /**
     * Sets the value of the orientamentoProduttivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrientamentoProduttivo(String value) {
        this.orientamentoProduttivo = value;
    }

    /**
     * Gets the value of the tipologiaAssicurativa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipologiaAssicurativa() {
        return tipologiaAssicurativa;
    }

    /**
     * Sets the value of the tipologiaAssicurativa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipologiaAssicurativa(String value) {
        this.tipologiaAssicurativa = value;
    }

    /**
     * Gets the value of the elencoCategorie property.
     * 
     * @return
     *     possible object is
     *     {@link ElencoCategorie }
     *     
     */
    public ElencoCategorie getElencoCategorie() {
        return elencoCategorie;
    }

    /**
     * Sets the value of the elencoCategorie property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElencoCategorie }
     *     
     */
    public void setElencoCategorie(ElencoCategorie value) {
        this.elencoCategorie = value;
    }

    /**
     * Gets the value of the rigaTotaliAllev property.
     * 
     * @return
     *     possible object is
     *     {@link RigaTotaliAllev }
     *     
     */
    public RigaTotaliAllev getRigaTotaliAllev() {
        return rigaTotaliAllev;
    }

    /**
     * Sets the value of the rigaTotaliAllev property.
     * 
     * @param value
     *     allowed object is
     *     {@link RigaTotaliAllev }
     *     
     */
    public void setRigaTotaliAllev(RigaTotaliAllev value) {
        this.rigaTotaliAllev = value;
    }

}
