package it.csi.solmr.dto.anag.terreni;

import java.io.*;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_TIPOLOGIA_VIGNETO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoTipologiaVignetoVO implements Serializable {
	
	private static final long serialVersionUID = -601780391102818232L;
	
	private Long idTipologiaVigneto = null;
	private String codice = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	
	/**
	 * Costruttore di default
	 */
	public TipoTipologiaVignetoVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idTipologiaVigneto
	 * @param codice
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoTipologiaVignetoVO(Long idTipologiaVigneto, String codice, String descrizione, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idTipologiaVigneto = idTipologiaVigneto;
		this.codice = codice;
		this.descrizione = descrizione;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
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
	 * @return the idTipologiaVigneto
	 */
	public Long getIdTipologiaVigneto() {
		return idTipologiaVigneto;
	}

	/**
	 * @param idTipologiaVigneto the idTipologiaVigneto to set
	 */
	public void setIdTipologiaVigneto(Long idTipologiaVigneto) {
		this.idTipologiaVigneto = idTipologiaVigneto;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((codice == null) ? 0 : codice.hashCode());
		result = PRIME * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idTipologiaVigneto == null) ? 0 : idTipologiaVigneto.hashCode());
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
		final TipoTipologiaVignetoVO other = (TipoTipologiaVignetoVO) obj;
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
		if (idTipologiaVigneto == null) {
			if (other.idTipologiaVigneto != null)
				return false;
		} else if (!idTipologiaVigneto.equals(other.idTipologiaVigneto))
			return false;
		return true;
	}

}
