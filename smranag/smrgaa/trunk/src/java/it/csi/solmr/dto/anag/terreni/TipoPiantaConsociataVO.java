package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_PIANTE_CONSOCIATE
 * 
 * @author Mauro Vocale
 *
 */

public class TipoPiantaConsociataVO implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1418644497576197160L;
	
	private Long idPianteConsociate = null;
	private String descrizione = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	
	public TipoPiantaConsociataVO() {
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
	 * @return the idPianteConsociate
	 */
	public Long getIdPianteConsociate() {
		return idPianteConsociate;
	}

	/**
	 * @param idPianteConsociate the idPianteConsociate to set
	 */
	public void setIdPianteConsociate(Long idPianteConsociate) {
		this.idPianteConsociate = idPianteConsociate;
	}

	/** 
	 * 
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idPianteConsociate == null) ? 0 : idPianteConsociate.hashCode());
		return result;
	}

	/**
	 * 
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TipoPiantaConsociataVO other = (TipoPiantaConsociataVO) obj;
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
		if (idPianteConsociate == null) {
			if (other.idPianteConsociate != null)
				return false;
		} else if (!idPianteConsociate.equals(other.idPianteConsociate))
			return false;
		return true;
	}
				
}