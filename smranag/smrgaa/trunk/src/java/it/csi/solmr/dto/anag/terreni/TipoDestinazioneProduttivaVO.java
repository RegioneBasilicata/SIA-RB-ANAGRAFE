package it.csi.solmr.dto.anag.terreni;

import java.io.*;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_DESTINAZIONE_PRODUTTIV
 * 
 * @author Mauro Vocale
 *
 */
public class TipoDestinazioneProduttivaVO implements Serializable {
	
	private static final long serialVersionUID = -299105378387797980L;
	
	private Long idDestinazioneProduttiva = null;
	private Long idTipologiaUnar = null;
	private String codice = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	
	/**
	 * Costruttore di default
	 */
	public TipoDestinazioneProduttivaVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idDestinazioneProduttiva
	 * @param idTipologiaUnar
	 * @param codice
	 * @param descrizione
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoDestinazioneProduttivaVO(Long idDestinazioneProduttiva, Long idTipologiaUnar, String codice, String descrizione, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idDestinazioneProduttiva = idDestinazioneProduttiva;
		this.idTipologiaUnar = idTipologiaUnar;
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
	 * @return the idDestinazioneProduttiva
	 */
	public Long getIdDestinazioneProduttiva() {
		return idDestinazioneProduttiva;
	}

	/**
	 * @param idDestinazioneProduttiva the idDestinazioneProduttiva to set
	 */
	public void setIdDestinazioneProduttiva(Long idDestinazioneProduttiva) {
		this.idDestinazioneProduttiva = idDestinazioneProduttiva;
	}

	/**
	 * @return the idTipologiaUnar
	 */
	public Long getIdTipologiaUnar() {
		return idTipologiaUnar;
	}

	/**
	 * @param idTipologiaUnar the idTipologiaUnar to set
	 */
	public void setIdTipologiaUnar(Long idTipologiaUnar) {
		this.idTipologiaUnar = idTipologiaUnar;
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
		result = PRIME * result + ((idDestinazioneProduttiva == null) ? 0 : idDestinazioneProduttiva.hashCode());
		result = PRIME * result + ((idTipologiaUnar == null) ? 0 : idTipologiaUnar.hashCode());
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
		final TipoDestinazioneProduttivaVO other = (TipoDestinazioneProduttivaVO) obj;
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
		if (idDestinazioneProduttiva == null) {
			if (other.idDestinazioneProduttiva != null)
				return false;
		} else if (!idDestinazioneProduttiva.equals(other.idDestinazioneProduttiva))
			return false;
		if (idTipologiaUnar == null) {
			if (other.idTipologiaUnar != null)
				return false;
		} else if (!idTipologiaUnar.equals(other.idTipologiaUnar))
			return false;
		return true;
	}

}
