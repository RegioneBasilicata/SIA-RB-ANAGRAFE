package it.csi.solmr.dto.anag.terreni;

import java.io.*;
import java.util.*;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_CESSAZIONE_UNAR
 * 
 * @author Mauro Vocale
 *
 */
public class TipoCessazioneUnarVO implements Serializable {
	
	private static final long serialVersionUID = 1173788924917427399L;
	
	private Long idCessazioneUnar = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	
	/**
	 * Costruttore di default
	 */
	public TipoCessazioneUnarVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idCessazioneUnar
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoCessazioneUnarVO(Long idCessazioneUnar, String descrizione, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idCessazioneUnar = idCessazioneUnar;
		this.descrizione = descrizione;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
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
	 * @return the idCessazioneUnar
	 */
	public Long getIdCessazioneUnar() {
		return idCessazioneUnar;
	}

	/**
	 * @param idCessazioneUnar the idCessazioneUnar to set
	 */
	public void setIdCessazioneUnar(Long idCessazioneUnar) {
		this.idCessazioneUnar = idCessazioneUnar;
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
		result = PRIME * result + ((idCessazioneUnar == null) ? 0 : idCessazioneUnar.hashCode());
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
		final TipoCessazioneUnarVO other = (TipoCessazioneUnarVO) obj;
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
		if (idCessazioneUnar == null) {
			if (other.idCessazioneUnar != null)
				return false;
		} else if (!idCessazioneUnar.equals(other.idCessazioneUnar))
			return false;
		return true;
	}
}
