package it.csi.solmr.dto.anag.consistenza;

import java.io.Serializable;
import java.util.*;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_MOTIVO_DICHIARAZIONE
 * 
 * @author Mauro Vocale
 *
 */
public class TipoMotivoDichiarazioneVO implements Serializable {
	
	private static final long serialVersionUID = 5081974639119867792L;
	
	private Long idMotivoDichiarazione = null;
	private String descrizione = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	private String annoCampagna = null;
	private Long idFase = null;
	private String gtfo = null;
	private String tipoDichiarazione = null;
	
	/**
	 * Costruttore di default 
	 */
	public TipoMotivoDichiarazioneVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idMotivoDichiarazione
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 * @param annoCampagna
	 * @param idFase
	 * @param gtfo
	 * @param tipoDichiarazione
	 */
	public TipoMotivoDichiarazioneVO(Long idMotivoDichiarazione, String descrizione, Date dataInizioValidita, Date dataFineValidita, String annoCampagna, Long idFase, String gtfo, String tipoDichiarazione) {
		super();
		this.idMotivoDichiarazione = idMotivoDichiarazione;
		this.descrizione = descrizione;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
		this.annoCampagna = annoCampagna;
		this.idFase = idFase;
		this.gtfo = gtfo;
		this.tipoDichiarazione = tipoDichiarazione;
	}

	/**
	 * @return the annoCampagna
	 */
	public String getAnnoCampagna() {
		return annoCampagna;
	}

	/**
	 * @param annoCampagna the annoCampagna to set
	 */
	public void setAnnoCampagna(String annoCampagna) {
		this.annoCampagna = annoCampagna;
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
	 * @return the gtfo
	 */
	public String getGtfo() {
		return gtfo;
	}

	/**
	 * @param gtfo the gtfo to set
	 */
	public void setGtfo(String gtfo) {
		this.gtfo = gtfo;
	}

	/**
	 * @return the idFase
	 */
	public Long getIdFase() {
		return idFase;
	}

	/**
	 * @param idFase the idFase to set
	 */
	public void setIdFase(Long idFase) {
		this.idFase = idFase;
	}

	/**
	 * @return the idMotivoDichiarazione
	 */
	public Long getIdMotivoDichiarazione() {
		return idMotivoDichiarazione;
	}

	/**
	 * @param idMotivoDichiarazione the idMotivoDichiarazione to set
	 */
	public void setIdMotivoDichiarazione(Long idMotivoDichiarazione) {
		this.idMotivoDichiarazione = idMotivoDichiarazione;
	}

	/**
	 * @return the tipoDichiarazione
	 */
	public String getTipoDichiarazione() {
		return tipoDichiarazione;
	}

	/**
	 * @param tipoDichiarazione the tipoDichiarazione to set
	 */
	public void setTipoDichiarazione(String tipoDichiarazione) {
		this.tipoDichiarazione = tipoDichiarazione;
	}

	/**
	 * Metodo hashCode();
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((annoCampagna == null) ? 0 : annoCampagna.hashCode());
		result = PRIME * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((gtfo == null) ? 0 : gtfo.hashCode());
		result = PRIME * result + ((idFase == null) ? 0 : idFase.hashCode());
		result = PRIME * result + ((idMotivoDichiarazione == null) ? 0 : idMotivoDichiarazione.hashCode());
		result = PRIME * result + ((tipoDichiarazione == null) ? 0 : tipoDichiarazione.hashCode());
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
		final TipoMotivoDichiarazioneVO other = (TipoMotivoDichiarazioneVO) obj;
		if (annoCampagna == null) {
			if (other.annoCampagna != null)
				return false;
		} else if (!annoCampagna.equals(other.annoCampagna))
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
		if (gtfo == null) {
			if (other.gtfo != null)
				return false;
		} else if (!gtfo.equals(other.gtfo))
			return false;
		if (idFase == null) {
			if (other.idFase != null)
				return false;
		} else if (!idFase.equals(other.idFase))
			return false;
		if (idMotivoDichiarazione == null) {
			if (other.idMotivoDichiarazione != null)
				return false;
		} else if (!idMotivoDichiarazione.equals(other.idMotivoDichiarazione))
			return false;
		if (tipoDichiarazione == null) {
			if (other.tipoDichiarazione != null)
				return false;
		} else if (!tipoDichiarazione.equals(other.tipoDichiarazione))
			return false;
		return true;
	}
			
}