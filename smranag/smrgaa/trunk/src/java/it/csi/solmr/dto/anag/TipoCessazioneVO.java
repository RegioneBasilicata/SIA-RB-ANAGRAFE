package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_CESSAZIONE
 * @author Mauro Vocale
 *
 */
public class TipoCessazioneVO implements Serializable {
	
	private static final long serialVersionUID = 5116052003782942365L;
	
	private Long idCessazione = null;
	private String descrizione = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public TipoCessazioneVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idCessazione
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoCessazioneVO(Long idCessazione, String descrizione, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idCessazione = idCessazione;
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
	 * @return the idCessazione
	 */
	public Long getIdCessazione() {
		return idCessazione;
	}

	/**
	 * @param idCessazione the idIrrigazione to set
	 */
	public void setIdCessazione(Long idCessazione) {
		this.idCessazione = idCessazione;
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
		result = PRIME * result + ((idCessazione == null) ? 0 : idCessazione.hashCode());
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
		final TipoCessazioneVO other = (TipoCessazioneVO) obj;
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
		if (idCessazione == null) {
			if (other.idCessazione != null)
				return false;
		} else if (!idCessazione.equals(other.idCessazione))
			return false;
		return true;
	}
	
	
}