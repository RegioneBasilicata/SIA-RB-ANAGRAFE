package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_IMPIANTO
 * @author Mauro Vocale
 *
 */
public class TipoImpiantoVO implements Serializable {

	private static final long serialVersionUID = -4767530697472222023L;
	
	private Long idImpianto = null;
	private String descrizione = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	private String flagPianteConsociate = null;
	
	/**
	 * Costruttore
	 *
	 */
	public TipoImpiantoVO() {
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idImpianto
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoImpiantoVO(Long idImpianto, String descrizione, Date dataInizioValidita, Date dataFineValidita, String flagPianteConsociate) {
		super();
		this.idImpianto = idImpianto;
		this.descrizione = descrizione;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
		this.flagPianteConsociate = flagPianteConsociate;
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
	 * @return the idImpianto
	 */
	public Long getIdImpianto() {
		return idImpianto;
	}

	/**
	 * @param idImpianto the idImpianto to set
	 */
	public void setIdImpianto(Long idImpianto) {
		this.idImpianto = idImpianto;
	}

	/**
	 * @return the flagPianteConsociate
	 */
	public String getFlagPianteConsociate() {
		return flagPianteConsociate;
	}

	/**
	 * @param flagPianteConsociate the flagPianteConsociate to set
	 */
	public void setFlagPianteConsociate(String flagPianteConsociate) {
		this.flagPianteConsociate = flagPianteConsociate;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idImpianto == null) ? 0 : idImpianto.hashCode());
		result = PRIME * result + ((flagPianteConsociate == null) ? 0 : flagPianteConsociate.hashCode());
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
		final TipoImpiantoVO other = (TipoImpiantoVO) obj;
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
		if (idImpianto == null) {
			if (other.idImpianto != null)
				return false;
		} else if (!idImpianto.equals(other.idImpianto))
			return false;
		if (flagPianteConsociate == null) {
			if (other.flagPianteConsociate != null)
				return false;
		} else if (!flagPianteConsociate.equals(other.flagPianteConsociate))
			return false;
		return true;
	}
}