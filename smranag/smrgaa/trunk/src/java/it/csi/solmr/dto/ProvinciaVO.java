package it.csi.solmr.dto;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

import java.io.Serializable;

public class ProvinciaVO implements Serializable {
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -6848038163954902036L;

	private String istatProvincia=null;
	private String siglaProvincia=null;
	private String idRegione=null;
	private String descrizione=null;
	public ProvinciaVO() {
	}
	public String getIstatProvincia() {
		return istatProvincia;
	}
	public void setIstatProvincia(String istatProvincia) {
		this.istatProvincia = istatProvincia;
	}
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}
	public String getSiglaProvincia() {
		return siglaProvincia;
	}
	public void setIdRegione(String idRegione) {
		this.idRegione = idRegione;
	}
	public String getIdRegione() {
		return idRegione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getDescrizione() {
		return descrizione;
	}
	
	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idRegione == null) ? 0 : idRegione.hashCode());
		result = PRIME * result + ((istatProvincia == null) ? 0 : istatProvincia.hashCode());
		result = PRIME * result + ((siglaProvincia == null) ? 0 : siglaProvincia.hashCode());
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
		final ProvinciaVO other = (ProvinciaVO) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idRegione == null) {
			if (other.idRegione != null)
				return false;
		} else if (!idRegione.equals(other.idRegione))
			return false;
		if (istatProvincia == null) {
			if (other.istatProvincia != null)
				return false;
		} else if (!istatProvincia.equals(other.istatProvincia))
			return false;
		if (siglaProvincia == null) {
			if (other.siglaProvincia != null)
				return false;
		} else if (!siglaProvincia.equals(other.siglaProvincia))
			return false;
		return true;
	}
	
}