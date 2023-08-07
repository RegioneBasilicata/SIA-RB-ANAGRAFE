package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_IRRIGAZIONE
 * @author Mauro Vocale
 *
 */
public class TipoIrrigazioneVO implements Serializable {

	private static final long serialVersionUID = -3337169482373581391L;
	
	private Long idIrrigazione = null;
	private String descrizione = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public TipoIrrigazioneVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idIrrigazione
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoIrrigazioneVO(Long idIrrigazione, String descrizione, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idIrrigazione = idIrrigazione;
		this.descrizione = descrizione;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the dataFineValidita
	 */
	public java.util.Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(java.util.Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the dataInizioValidita
	 */
	public java.util.Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(java.util.Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
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
	 * @return the idIrrigazione
	 */
	public Long getIdIrrigazione() {
		return idIrrigazione;
	}

	/**
	 * @param idIrrigazione the idIrrigazione to set
	 */
	public void setIdIrrigazione(Long idIrrigazione) {
		this.idIrrigazione = idIrrigazione;
	}

	/** 
	 * Metodo equals()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idIrrigazione == null) ? 0 : idIrrigazione.hashCode());
		return result;
	}

	/**
	 * Metodo hashCode();
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TipoIrrigazioneVO other = (TipoIrrigazioneVO) obj;
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
		if (idIrrigazione == null) {
			if (other.idIrrigazione != null)
				return false;
		} else if (!idIrrigazione.equals(other.idIrrigazione))
			return false;
		return true;
	}
	
	
}