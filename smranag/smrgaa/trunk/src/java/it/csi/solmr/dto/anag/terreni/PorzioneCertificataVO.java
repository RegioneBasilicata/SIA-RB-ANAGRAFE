package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_PORZIONE_CERTIFICATA
 * @author Mauro Vocale
 *
 */
public class PorzioneCertificataVO implements Serializable {
	
	private static final long serialVersionUID = 7397523799816176775L;
	private Long idPorzioneCertificata = null;
	private Long idParticellaCertificata = null;
	private String comune = null;
	private String stadio = null;
	private String sezione = null;
	private Long idQualita = null;
	private String supCatastale = null;
	private String identificativoPorzione = null;	

	/**
	 * Costruttore di default
	 *
	 */
	public PorzioneCertificataVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idPorzioneCertificata
	 * @param idParticellaCertificata
	 * @param comune
	 * @param stadio
	 * @param sezione
	 * @param idQualita
	 * @param supCatastale
	 * @param identificativoPorzione
	 */
	public PorzioneCertificataVO(Long idPorzioneCertificata, Long idParticellaCertificata, String comune, String stadio, String sezione, Long idQualita, String supCatastale, String identificativoPorzione) {
		super();
		this.idPorzioneCertificata = idPorzioneCertificata;
		this.idParticellaCertificata = idParticellaCertificata;
		this.comune = comune;
		this.stadio = stadio;
		this.sezione = sezione;
		this.idQualita = idQualita;
		this.supCatastale = supCatastale;
		this.identificativoPorzione = identificativoPorzione;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((comune == null) ? 0 : comune.hashCode());
		result = PRIME * result + ((idParticellaCertificata == null) ? 0 : idParticellaCertificata.hashCode());
		result = PRIME * result + ((idPorzioneCertificata == null) ? 0 : idPorzioneCertificata.hashCode());
		result = PRIME * result + ((idQualita == null) ? 0 : idQualita.hashCode());
		result = PRIME * result + ((identificativoPorzione == null) ? 0 : identificativoPorzione.hashCode());
		result = PRIME * result + ((sezione == null) ? 0 : sezione.hashCode());
		result = PRIME * result + ((stadio == null) ? 0 : stadio.hashCode());
		result = PRIME * result + ((supCatastale == null) ? 0 : supCatastale.hashCode());
		return result;
	}

	/**
	 * Metodo equals()
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PorzioneCertificataVO other = (PorzioneCertificataVO) obj;
		if (comune == null) {
			if (other.comune != null)
				return false;
		} else if (!comune.equals(other.comune))
			return false;
		if (idParticellaCertificata == null) {
			if (other.idParticellaCertificata != null)
				return false;
		} else if (!idParticellaCertificata.equals(other.idParticellaCertificata))
			return false;
		if (idPorzioneCertificata == null) {
			if (other.idPorzioneCertificata != null)
				return false;
		} else if (!idPorzioneCertificata.equals(other.idPorzioneCertificata))
			return false;
		if (idQualita == null) {
			if (other.idQualita != null)
				return false;
		} else if (!idQualita.equals(other.idQualita))
			return false;
		if (identificativoPorzione == null) {
			if (other.identificativoPorzione != null)
				return false;
		} else if (!identificativoPorzione.equals(other.identificativoPorzione))
			return false;
		if (sezione == null) {
			if (other.sezione != null)
				return false;
		} else if (!sezione.equals(other.sezione))
			return false;
		if (stadio == null) {
			if (other.stadio != null)
				return false;
		} else if (!stadio.equals(other.stadio))
			return false;
		if (supCatastale == null) {
			if (other.supCatastale != null)
				return false;
		} else if (!supCatastale.equals(other.supCatastale))
			return false;
		return true;
	}

	/**
	 * @return the comune
	 */
	public String getComune() {
		return comune;
	}

	/**
	 * @param comune the comune to set
	 */
	public void setComune(String comune) {
		this.comune = comune;
	}

	/**
	 * @return the identificativoPorzione
	 */
	public String getIdentificativoPorzione() {
		return identificativoPorzione;
	}

	/**
	 * @param identificativoPorzione the identificativoPorzione to set
	 */
	public void setIdentificativoPorzione(String identificativoPorzione) {
		this.identificativoPorzione = identificativoPorzione;
	}

	/**
	 * @return the idParticellaCertificata
	 */
	public Long getIdParticellaCertificata() {
		return idParticellaCertificata;
	}

	/**
	 * @param idParticellaCertificata the idParticellaCertificata to set
	 */
	public void setIdParticellaCertificata(Long idParticellaCertificata) {
		this.idParticellaCertificata = idParticellaCertificata;
	}

	/**
	 * @return the idPorzioneCertificata
	 */
	public Long getIdPorzioneCertificata() {
		return idPorzioneCertificata;
	}

	/**
	 * @param idPorzioneCertificata the idPorzioneCertificata to set
	 */
	public void setIdPorzioneCertificata(Long idPorzioneCertificata) {
		this.idPorzioneCertificata = idPorzioneCertificata;
	}

	/**
	 * @return the idQualita
	 */
	public Long getIdQualita() {
		return idQualita;
	}

	/**
	 * @param idQualita the idQualita to set
	 */
	public void setIdQualita(Long idQualita) {
		this.idQualita = idQualita;
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
	 * @return the stadio
	 */
	public String getStadio() {
		return stadio;
	}

	/**
	 * @param stadio the stadio to set
	 */
	public void setStadio(String stadio) {
		this.stadio = stadio;
	}

	/**
	 * @return the supCatastale
	 */
	public String getSupCatastale() {
		return supCatastale;
	}

	/**
	 * @param supCatastale the supCatastale to set
	 */
	public void setSupCatastale(String supCatastale) {
		this.supCatastale = supCatastale;
	}
}