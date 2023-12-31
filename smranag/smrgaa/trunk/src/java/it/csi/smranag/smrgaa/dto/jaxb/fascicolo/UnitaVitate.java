//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.02 at 04:07:28 PM CET 
//


package it.csi.smranag.smrgaa.dto.jaxb.fascicolo;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}Visibility"/>
 *         &lt;element ref="{}SezioneVuota"/>
 *         &lt;element ref="{}RigaTotaliUv"/>
 *         &lt;element ref="{}RigaUv" maxOccurs="unbounded"/>
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
    "visibility",
    "sezioneVuota",
    "rigaTotaliUv",
    "rigaUv"
})
@XmlRootElement(name = "UnitaVitate")
public class UnitaVitate {

    @XmlElement(name = "Visibility")
    protected boolean visibility;
    @XmlElement(name = "SezioneVuota")
    protected boolean sezioneVuota;
    @XmlElement(name = "RigaTotaliUv", required = true)
    protected RigaTotaliUv rigaTotaliUv;
    @XmlElement(name = "RigaUv", required = true)
    protected List<RigaUv> rigaUv;

    /**
     * Gets the value of the visibility property.
     * 
     */
    public boolean isVisibility() {
        return visibility;
    }

    /**
     * Sets the value of the visibility property.
     * 
     */
    public void setVisibility(boolean value) {
        this.visibility = value;
    }

    /**
     * Gets the value of the sezioneVuota property.
     * 
     */
    public boolean isSezioneVuota() {
        return sezioneVuota;
    }

    /**
     * Sets the value of the sezioneVuota property.
     * 
     */
    public void setSezioneVuota(boolean value) {
        this.sezioneVuota = value;
    }

    /**
     * Gets the value of the rigaTotaliUv property.
     * 
     * @return
     *     possible object is
     *     {@link RigaTotaliUv }
     *     
     */
    public RigaTotaliUv getRigaTotaliUv() {
        return rigaTotaliUv;
    }

    /**
     * Sets the value of the rigaTotaliUv property.
     * 
     * @param value
     *     allowed object is
     *     {@link RigaTotaliUv }
     *     
     */
    public void setRigaTotaliUv(RigaTotaliUv value) {
        this.rigaTotaliUv = value;
    }

    /**
     * Gets the value of the rigaUv property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rigaUv property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRigaUv().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RigaUv }
     * 
     * 
     */
    public List<RigaUv> getRigaUv() {
        if (rigaUv == null) {
            rigaUv = new ArrayList<RigaUv>();
        }
        return this.rigaUv;
    }

}
