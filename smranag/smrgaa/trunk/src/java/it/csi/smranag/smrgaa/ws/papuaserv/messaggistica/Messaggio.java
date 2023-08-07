
package it.csi.smranag.smrgaa.ws.papuaserv.messaggistica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for messaggio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="messaggio">
 *   &lt;complexContent>
 *     &lt;extension base="{http://papuaserv.webservice.business.papuaserv.papua.csi.it/}messaggioBase">
 *       &lt;sequence>
 *         &lt;element name="conAllegati" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messaggio", propOrder = {
    "conAllegati"
})
public class Messaggio
    extends MessaggioBase
{

    protected boolean conAllegati;

    /**
     * Gets the value of the conAllegati property.
     * 
     */
    public boolean isConAllegati() {
        return conAllegati;
    }

    /**
     * Sets the value of the conAllegati property.
     * 
     */
    public void setConAllegati(boolean value) {
        this.conAllegati = value;
    }

}
