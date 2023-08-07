package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_MACRO_USO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoMacroUsoVO implements Serializable {
	
	private static final long serialVersionUID = 8384555557532186455L;
	
	private Long idMacroUso = null;
	private String codice = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	
	/**
	 * Costruttore di default 
	 */
	public TipoMacroUsoVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idMacroUso
	 * @param codice
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoMacroUsoVO(Long idMacroUso, String codice, String descrizione, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idMacroUso = idMacroUso;
		this.codice = codice;
		this.descrizione = descrizione;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the idMacroUso
	 */
	public Long getIdMacroUso() {
		return idMacroUso;
	}

	/**
	 * @param idMacroUso the idMacroUso to set
	 */
	public void setIdMacroUso(Long idMacroUso) {
		this.idMacroUso = idMacroUso;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
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
	 * @return the dataInizioValidita
	 */
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the dataFineValidita
	 */
	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime
				* result
				+ ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = prime
				* result
				+ ((dataInizioValidita == null) ? 0 : dataInizioValidita
						.hashCode());
		result = prime * result
				+ ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result
				+ ((idMacroUso == null) ? 0 : idMacroUso.hashCode());
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
		final TipoMacroUsoVO other = (TipoMacroUsoVO) obj;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (dataFineValidita == null) {
			if (other.dataFineValidita != null)
				return false;
		} else if (!dataFineValidita.equals(other.dataFineValidita))
			return false;
		if (dataInizioValidita == null) {
			if (other.dataInizioValidita != null)
				return false;
		} else if (!dataInizioValidita.equals(other.dataInizioValidita))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idMacroUso == null) {
			if (other.idMacroUso != null)
				return false;
		} else if (!idMacroUso.equals(other.idMacroUso))
			return false;
		return true;
	}
	
}