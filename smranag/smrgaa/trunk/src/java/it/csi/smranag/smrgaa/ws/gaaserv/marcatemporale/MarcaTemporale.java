
package it.csi.smranag.smrgaa.ws.gaaserv.marcatemporale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per marcaTemporale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="marcaTemporale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="arg0" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "marcaTemporale", propOrder = {
    "arg0"
})
public class MarcaTemporale {

    protected byte[] arg0;

    /**
     * Recupera il valore della proprietÓ arg0.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getArg0() {
        return arg0;
    }

    /**
     * Imposta il valore della proprietÓ arg0.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setArg0(byte[] value) {
        this.arg0 = value;
    }

}
