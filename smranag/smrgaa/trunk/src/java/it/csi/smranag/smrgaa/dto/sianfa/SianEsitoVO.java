
package it.csi.smranag.smrgaa.dto.sianfa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;




public class SianEsitoVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    protected List<SianEsitoAggFascicoloVO> esitiAggFascicolo;
    protected String esito;

    
    public List<SianEsitoAggFascicoloVO> getEsitiAggFascicolo() {
        if (esitiAggFascicolo == null) {
            esitiAggFascicolo = new ArrayList<SianEsitoAggFascicoloVO>();
        }
        return this.esitiAggFascicolo;
    }

    
    public String getEsito() {
        return esito;
    }

    
    public void setEsito(String value) {
        this.esito = value;
    }


	public void setEsitiAggFascicolo(List<SianEsitoAggFascicoloVO> esitiAggFascicolo) {
		this.esitiAggFascicolo = esitiAggFascicolo;
	}

}
