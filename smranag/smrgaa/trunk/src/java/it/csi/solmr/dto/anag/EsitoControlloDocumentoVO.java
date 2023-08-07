package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.*;
import java.io.*;

public class EsitoControlloDocumentoVO implements Serializable {

	private static final long serialVersionUID = 6637764422593439934L;
	
	private Long idEsitoControlloDocumento = null;
	private Long idDocumento = null;
	private Long idControllo = null;
	private CodeDescription controllo = null;
	private String bloccante = null;
	private String descrizione = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public EsitoControlloDocumentoVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idEsitoControlloDocumento
	 * @param idDocumento
	 * @param idControllo
	 * @param controllo
	 * @param bloccante
	 * @param descrizione
	 */
	public EsitoControlloDocumentoVO(Long idEsitoControlloDocumento, Long idDocumento, Long idControllo, CodeDescription controllo, String bloccante, String descrizione) {
		super();
		this.idEsitoControlloDocumento = idEsitoControlloDocumento;
		this.idDocumento = idDocumento;
		this.idControllo = idControllo;
		this.controllo = controllo;
		this.bloccante = bloccante;
		this.descrizione = descrizione;
	}

	/**
	 * @return the bloccante
	 */
	public String getBloccante() {
		return bloccante;
	}

	/**
	 * @param bloccante the bloccante to set
	 */
	public void setBloccante(String bloccante) {
		this.bloccante = bloccante;
	}

	/**
	 * @return the controllo
	 */
	public CodeDescription getControllo() {
		return controllo;
	}

	/**
	 * @param controllo the controllo to set
	 */
	public void setControllo(CodeDescription controllo) {
		this.controllo = controllo;
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
	 * @return the idControllo
	 */
	public Long getIdControllo() {
		return idControllo;
	}

	/**
	 * @param idControllo the idControllo to set
	 */
	public void setIdControllo(Long idControllo) {
		this.idControllo = idControllo;
	}

	/**
	 * @return the idDocumento
	 */
	public Long getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idDocumento the idDocumento to set
	 */
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @return the idEsitoControlloDocumento
	 */
	public Long getIdEsitoControlloDocumento() {
		return idEsitoControlloDocumento;
	}

	/**
	 * @param idEsitoControlloDocumento the idEsitoControlloDocumento to set
	 */
	public void setIdEsitoControlloDocumento(Long idEsitoControlloDocumento) {
		this.idEsitoControlloDocumento = idEsitoControlloDocumento;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((bloccante == null) ? 0 : bloccante.hashCode());
		result = PRIME * result + ((controllo == null) ? 0 : controllo.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idControllo == null) ? 0 : idControllo.hashCode());
		result = PRIME * result + ((idDocumento == null) ? 0 : idDocumento.hashCode());
		result = PRIME * result + ((idEsitoControlloDocumento == null) ? 0 : idEsitoControlloDocumento.hashCode());
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
		final EsitoControlloDocumentoVO other = (EsitoControlloDocumentoVO) obj;
		if (bloccante == null) {
			if (other.bloccante != null)
				return false;
		} else if (!bloccante.equals(other.bloccante))
			return false;
		if (controllo == null) {
			if (other.controllo != null)
				return false;
		} else if (!controllo.equals(other.controllo))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idControllo == null) {
			if (other.idControllo != null)
				return false;
		} else if (!idControllo.equals(other.idControllo))
			return false;
		if (idDocumento == null) {
			if (other.idDocumento != null)
				return false;
		} else if (!idDocumento.equals(other.idDocumento))
			return false;
		if (idEsitoControlloDocumento == null) {
			if (other.idEsitoControlloDocumento != null)
				return false;
		} else if (!idEsitoControlloDocumento.equals(other.idEsitoControlloDocumento))
			return false;
		return true;
	}
	
}
