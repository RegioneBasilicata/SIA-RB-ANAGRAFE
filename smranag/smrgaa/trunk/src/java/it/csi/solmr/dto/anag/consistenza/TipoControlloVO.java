package it.csi.solmr.dto.anag.consistenza;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_CONTROLLO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoControlloVO implements Serializable {
	
	private static final long serialVersionUID = 170007793859775996L;
	
	private Long idControllo = null;
	private Long idGruppoControllo = null;
	private String descrizione = null;
	private String obbligatorio = null;
	private String note = null;
	private String codiceControllo = null;
	private String ordinamento = null;
	
	/**
	 * Costruttore di default 
	 */
	public TipoControlloVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idControllo
	 * @param idGruppoControllo
	 * @param descrizione
	 * @param obbligatorio
	 * @param note
	 * @param codiceControllo
	 * @param ordinamento
	 */
	public TipoControlloVO(Long idControllo, Long idGruppoControllo, String descrizione, String obbligatorio, String note, String codiceControllo, String ordinamento) {
		super();
		this.idControllo = idControllo;
		this.idGruppoControllo = idGruppoControllo;
		this.descrizione = descrizione;
		this.obbligatorio = obbligatorio;
		this.note = note;
		this.codiceControllo = codiceControllo;
		this.ordinamento = ordinamento;
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
	 * @return the idGruppoControllo
	 */
	public Long getIdGruppoControllo() {
		return idGruppoControllo;
	}

	/**
	 * @param idGruppoControllo the idGruppoControllo to set
	 */
	public void setIdGruppoControllo(Long idGruppoControllo) {
		this.idGruppoControllo = idGruppoControllo;
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
	 * @return the obbligatorio
	 */
	public String getObbligatorio() {
		return obbligatorio;
	}

	/**
	 * @param obbligatorio the obbligatorio to set
	 */
	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the codiceControllo
	 */
	public String getCodiceControllo() {
		return codiceControllo;
	}

	/**
	 * @param codiceControllo the codiceControllo to set
	 */
	public void setCodiceControllo(String codiceControllo) {
		this.codiceControllo = codiceControllo;
	}

	/**
	 * @return the ordinamento
	 */
	public String getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @param ordinamento the ordinamento to set
	 */
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * Metodo hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codiceControllo == null) ? 0 : codiceControllo.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((idControllo == null) ? 0 : idControllo.hashCode());
		result = prime * result + ((idGruppoControllo == null) ? 0 : idGruppoControllo.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((obbligatorio == null) ? 0 : obbligatorio.hashCode());
		result = prime * result + ((ordinamento == null) ? 0 : ordinamento.hashCode());
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
		final TipoControlloVO other = (TipoControlloVO) obj;
		if (codiceControllo == null) {
			if (other.codiceControllo != null)
				return false;
		} else if (!codiceControllo.equals(other.codiceControllo))
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
		if (idGruppoControllo == null) {
			if (other.idGruppoControllo != null)
				return false;
		} else if (!idGruppoControllo.equals(other.idGruppoControllo))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (obbligatorio == null) {
			if (other.obbligatorio != null)
				return false;
		} else if (!obbligatorio.equals(other.obbligatorio))
			return false;
		if (ordinamento == null) {
			if (other.ordinamento != null)
				return false;
		} else if (!ordinamento.equals(other.ordinamento))
			return false;
		return true;
	}
}