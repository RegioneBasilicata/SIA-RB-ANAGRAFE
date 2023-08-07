package it.csi.solmr.dto.anag.terreni;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_SEZIONE
 * @author Mauro Vocale
 *
 */
public class SezioneVO implements Serializable {

	
	private static final long serialVersionUID = 7129503312987374677L;
	
	private String istatComune = null;
	private String sezione = null;
	private String descrizione = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public SezioneVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param istatComune
	 * @param sezione
	 * @param descrizione
	 */
	public SezioneVO(String istatComune, String sezione, String descrizione) {
		super();
		this.istatComune = istatComune;
		this.sezione = sezione;
		this.descrizione = descrizione;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the istatComune
	 */
	public String getIstatComune() {
		return istatComune;
	}

	/**
	 * @param istatComune the istatComune to set
	 */
	public void setIstatComune(String istatComune) {
		this.istatComune = istatComune;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @param sezione the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((istatComune == null) ? 0 : istatComune.hashCode());
		result = PRIME * result + ((sezione == null) ? 0 : sezione.hashCode());
		return result;
	}

	/**
	 * Metodo equals
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SezioneVO other = (SezioneVO) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (istatComune == null) {
			if (other.istatComune != null)
				return false;
		} else if (!istatComune.equals(other.istatComune))
			return false;
		if (sezione == null) {
			if (other.sezione != null)
				return false;
		} else if (!sezione.equals(other.sezione))
			return false;
		return true;
	}		
}