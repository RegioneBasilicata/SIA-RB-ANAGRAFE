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
 *         &lt;element ref="{}Header"/>
 *         &lt;element ref="{}IntroRichVar"/>
 *         &lt;element ref="{}HeaderAllegati"/>
 *         &lt;element ref="{}QuadroA"/>
 *         &lt;element ref="{}QuadroUte"/>
 *         &lt;element ref="{}QuadroManodopera"/>
 *         &lt;element ref="{}QuadroAllevamenti"/>
 *         &lt;element ref="{}QuadroTerreni"/>
 *         &lt;element ref="{}QuadroUnitaVitate"/>
 *         &lt;element ref="{}QuadroFabbricati"/>
 *         &lt;element ref="{}QuadroMotori"/>
 *         &lt;element ref="{}QuadroDocumenti"/>
 *         &lt;element ref="{}QuadroAssociazioni"/>
 *         &lt;element ref="{}QuadroSoggetti"/>
 *         &lt;element ref="{}QuadroVariazioneMotori"/>
 *         &lt;element ref="{}Dichiarazioni"/>
 *         &lt;element ref="{}Allegati"/>
 *         &lt;element ref="{}HeaderLandscape"/>
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
    "header",
    "introRichVar",
    "headerAllegati",
    "quadroA",
    "quadroUte",
    "quadroManodopera",
    "quadroAllevamenti",
    "quadroTerreni",
    "quadroUnitaVitate",
    "quadroFabbricati",
    "quadroMotori",
    "quadroDocumenti",
    "quadroAssociazioni",
    "quadroSoggetti",
    "quadroVariazioneMotori",
    "dichiarazioni",
    "allegati",
    "headerLandscape"
})
@XmlRootElement(name = "Fascicolo")
public class Fascicolo {

    @XmlElement(name = "Header", required = true)
    protected Header header;
    @XmlElement(name = "IntroRichVar", required = true)
    protected IntroRichVar introRichVar;
    @XmlElement(name = "HeaderAllegati", required = true)
    protected HeaderAllegati headerAllegati;
    @XmlElement(name = "QuadroA", required = true)
    protected QuadroA quadroA;
    @XmlElement(name = "QuadroUte", required = true)
    protected QuadroUte quadroUte;
    @XmlElement(name = "QuadroManodopera", required = true)
    protected QuadroManodopera quadroManodopera;
    @XmlElement(name = "QuadroAllevamenti", required = true)
    protected QuadroAllevamenti quadroAllevamenti;
    @XmlElement(name = "QuadroTerreni", required = true)
    protected QuadroTerreni quadroTerreni;
    @XmlElement(name = "QuadroUnitaVitate", required = true)
    protected QuadroUnitaVitate quadroUnitaVitate;
    @XmlElement(name = "QuadroFabbricati", required = true)
    protected QuadroFabbricati quadroFabbricati;
    @XmlElement(name = "QuadroMotori", required = true)
    protected QuadroMotori quadroMotori;
    @XmlElement(name = "QuadroDocumenti", required = true)
    protected QuadroDocumenti quadroDocumenti;
    @XmlElement(name = "QuadroAssociazioni", required = true)
    protected QuadroAssociazioni quadroAssociazioni;
    @XmlElement(name = "QuadroSoggetti", required = true)
    protected QuadroSoggetti quadroSoggetti;
    @XmlElement(name = "QuadroVariazioneMotori", required = true)
    protected QuadroVariazioneMotori quadroVariazioneMotori;
    @XmlElement(name = "Dichiarazioni", required = true)
    protected Dichiarazioni dichiarazioni;
    @XmlElement(name = "Allegati", required = true)
    protected Allegati allegati;
    @XmlElement(name = "HeaderLandscape", required = true)
    protected HeaderLandscape headerLandscape;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the introRichVar property.
     * 
     * @return
     *     possible object is
     *     {@link IntroRichVar }
     *     
     */
    public IntroRichVar getIntroRichVar() {
        return introRichVar;
    }

    /**
     * Sets the value of the introRichVar property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntroRichVar }
     *     
     */
    public void setIntroRichVar(IntroRichVar value) {
        this.introRichVar = value;
    }

    /**
     * Gets the value of the headerAllegati property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderAllegati }
     *     
     */
    public HeaderAllegati getHeaderAllegati() {
        return headerAllegati;
    }

    /**
     * Sets the value of the headerAllegati property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderAllegati }
     *     
     */
    public void setHeaderAllegati(HeaderAllegati value) {
        this.headerAllegati = value;
    }

    /**
     * Gets the value of the quadroA property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroA }
     *     
     */
    public QuadroA getQuadroA() {
        return quadroA;
    }

    /**
     * Sets the value of the quadroA property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroA }
     *     
     */
    public void setQuadroA(QuadroA value) {
        this.quadroA = value;
    }

    /**
     * Gets the value of the quadroUte property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroUte }
     *     
     */
    public QuadroUte getQuadroUte() {
        return quadroUte;
    }

    /**
     * Sets the value of the quadroUte property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroUte }
     *     
     */
    public void setQuadroUte(QuadroUte value) {
        this.quadroUte = value;
    }

    /**
     * Gets the value of the quadroManodopera property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroManodopera }
     *     
     */
    public QuadroManodopera getQuadroManodopera() {
        return quadroManodopera;
    }

    /**
     * Sets the value of the quadroManodopera property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroManodopera }
     *     
     */
    public void setQuadroManodopera(QuadroManodopera value) {
        this.quadroManodopera = value;
    }

    /**
     * Gets the value of the quadroAllevamenti property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroAllevamenti }
     *     
     */
    public QuadroAllevamenti getQuadroAllevamenti() {
        return quadroAllevamenti;
    }

    /**
     * Sets the value of the quadroAllevamenti property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroAllevamenti }
     *     
     */
    public void setQuadroAllevamenti(QuadroAllevamenti value) {
        this.quadroAllevamenti = value;
    }

    /**
     * Gets the value of the quadroTerreni property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroTerreni }
     *     
     */
    public QuadroTerreni getQuadroTerreni() {
        return quadroTerreni;
    }

    /**
     * Sets the value of the quadroTerreni property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroTerreni }
     *     
     */
    public void setQuadroTerreni(QuadroTerreni value) {
        this.quadroTerreni = value;
    }

    /**
     * Gets the value of the quadroUnitaVitate property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroUnitaVitate }
     *     
     */
    public QuadroUnitaVitate getQuadroUnitaVitate() {
        return quadroUnitaVitate;
    }

    /**
     * Sets the value of the quadroUnitaVitate property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroUnitaVitate }
     *     
     */
    public void setQuadroUnitaVitate(QuadroUnitaVitate value) {
        this.quadroUnitaVitate = value;
    }

    /**
     * Gets the value of the quadroFabbricati property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroFabbricati }
     *     
     */
    public QuadroFabbricati getQuadroFabbricati() {
        return quadroFabbricati;
    }

    /**
     * Sets the value of the quadroFabbricati property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroFabbricati }
     *     
     */
    public void setQuadroFabbricati(QuadroFabbricati value) {
        this.quadroFabbricati = value;
    }

    /**
     * Gets the value of the quadroMotori property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroMotori }
     *     
     */
    public QuadroMotori getQuadroMotori() {
        return quadroMotori;
    }

    /**
     * Sets the value of the quadroMotori property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroMotori }
     *     
     */
    public void setQuadroMotori(QuadroMotori value) {
        this.quadroMotori = value;
    }

    /**
     * Gets the value of the quadroDocumenti property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroDocumenti }
     *     
     */
    public QuadroDocumenti getQuadroDocumenti() {
        return quadroDocumenti;
    }

    /**
     * Sets the value of the quadroDocumenti property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroDocumenti }
     *     
     */
    public void setQuadroDocumenti(QuadroDocumenti value) {
        this.quadroDocumenti = value;
    }

    /**
     * Gets the value of the quadroAssociazioni property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroAssociazioni }
     *     
     */
    public QuadroAssociazioni getQuadroAssociazioni() {
        return quadroAssociazioni;
    }

    /**
     * Sets the value of the quadroAssociazioni property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroAssociazioni }
     *     
     */
    public void setQuadroAssociazioni(QuadroAssociazioni value) {
        this.quadroAssociazioni = value;
    }

    /**
     * Gets the value of the quadroSoggetti property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroSoggetti }
     *     
     */
    public QuadroSoggetti getQuadroSoggetti() {
        return quadroSoggetti;
    }

    /**
     * Sets the value of the quadroSoggetti property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroSoggetti }
     *     
     */
    public void setQuadroSoggetti(QuadroSoggetti value) {
        this.quadroSoggetti = value;
    }

    /**
     * Gets the value of the quadroVariazioneMotori property.
     * 
     * @return
     *     possible object is
     *     {@link QuadroVariazioneMotori }
     *     
     */
    public QuadroVariazioneMotori getQuadroVariazioneMotori() {
        return quadroVariazioneMotori;
    }

    /**
     * Sets the value of the quadroVariazioneMotori property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuadroVariazioneMotori }
     *     
     */
    public void setQuadroVariazioneMotori(QuadroVariazioneMotori value) {
        this.quadroVariazioneMotori = value;
    }

    /**
     * Gets the value of the dichiarazioni property.
     * 
     * @return
     *     possible object is
     *     {@link Dichiarazioni }
     *     
     */
    public Dichiarazioni getDichiarazioni() {
        return dichiarazioni;
    }

    /**
     * Sets the value of the dichiarazioni property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dichiarazioni }
     *     
     */
    public void setDichiarazioni(Dichiarazioni value) {
        this.dichiarazioni = value;
    }

    /**
     * Gets the value of the allegati property.
     * 
     * @return
     *     possible object is
     *     {@link Allegati }
     *     
     */
    public Allegati getAllegati() {
        return allegati;
    }

    /**
     * Sets the value of the allegati property.
     * 
     * @param value
     *     allowed object is
     *     {@link Allegati }
     *     
     */
    public void setAllegati(Allegati value) {
        this.allegati = value;
    }

    /**
     * Gets the value of the headerLandscape property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderLandscape }
     *     
     */
    public HeaderLandscape getHeaderLandscape() {
        return headerLandscape;
    }

    /**
     * Sets the value of the headerLandscape property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderLandscape }
     *     
     */
    public void setHeaderLandscape(HeaderLandscape value) {
        this.headerLandscape = value;
    }

}