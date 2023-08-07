package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_AZIENDA_DESTINAZIONE
 * @author Mauro Vocale
 *
 */
public class AziendaDestinazioneVO implements Serializable {

	private static final long serialVersionUID = -877798672114663707L;
	
	private Long idAziendaDestinazione = null;
	private Long idAzienda = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private Long idAziendaDiDestinazione = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public AziendaDestinazioneVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idAziendaDestinazione
	 * @param idAzienda
	 * @param dataAggiornamento
	 * @param idUtenteAggiornamento
	 * @param idAziendaDiDestinazione
	 */
	public AziendaDestinazioneVO(Long idAziendaDestinazione, Long idAzienda, Date dataAggiornamento, Long idUtenteAggiornamento, Long idAziendaDiDestinazione) {
		super();
		this.idAziendaDestinazione = idAziendaDestinazione;
		this.idAzienda = idAzienda;
		this.dataAggiornamento = dataAggiornamento;
		this.idUtenteAggiornamento = idUtenteAggiornamento;
		this.idAziendaDiDestinazione = idAziendaDiDestinazione;
	}

	/**
	 * @return the dataAggiornamento
	 */
	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	/**
	 * @param dataAggiornamento the dataAggiornamento to set
	 */
	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	/**
	 * @return the idAzienda
	 */
	public Long getIdAzienda() {
		return idAzienda;
	}

	/**
	 * @param idAzienda the idAzienda to set
	 */
	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	/**
	 * @return the idAziendaDestinazione
	 */
	public Long getIdAziendaDestinazione() {
		return idAziendaDestinazione;
	}

	/**
	 * @param idAziendaDestinazione the idAziendaDestinazione to set
	 */
	public void setIdAziendaDestinazione(Long idAziendaDestinazione) {
		this.idAziendaDestinazione = idAziendaDestinazione;
	}

	/**
	 * @return the idAziendaDiDestinazione
	 */
	public Long getIdAziendaDiDestinazione() {
		return idAziendaDiDestinazione;
	}

	/**
	 * @param idAziendaDiDestinazione the idAziendaDiDestinazione to set
	 */
	public void setIdAziendaDiDestinazione(Long idAziendaDiDestinazione) {
		this.idAziendaDiDestinazione = idAziendaDiDestinazione;
	}

	/**
	 * @return the idUtenteAggiornamento
	 */
	public Long getIdUtenteAggiornamento() {
		return idUtenteAggiornamento;
	}

	/**
	 * @param idUtenteAggiornamento the idUtenteAggiornamento to set
	 */
	public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
		this.idUtenteAggiornamento = idUtenteAggiornamento;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataAggiornamento == null) ? 0 : dataAggiornamento.hashCode());
		result = PRIME * result + ((idAzienda == null) ? 0 : idAzienda.hashCode());
		result = PRIME * result + ((idAziendaDestinazione == null) ? 0 : idAziendaDestinazione.hashCode());
		result = PRIME * result + ((idAziendaDiDestinazione == null) ? 0 : idAziendaDiDestinazione.hashCode());
		result = PRIME * result + ((idUtenteAggiornamento == null) ? 0 : idUtenteAggiornamento.hashCode());
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
		final AziendaDestinazioneVO other = (AziendaDestinazioneVO) obj;
		if (dataAggiornamento == null) {
			if (other.dataAggiornamento != null)
				return false;
		} else if (!dataAggiornamento.equals(other.dataAggiornamento))
			return false;
		if (idAzienda == null) {
			if (other.idAzienda != null)
				return false;
		} else if (!idAzienda.equals(other.idAzienda))
			return false;
		if (idAziendaDestinazione == null) {
			if (other.idAziendaDestinazione != null)
				return false;
		} else if (!idAziendaDestinazione.equals(other.idAziendaDestinazione))
			return false;
		if (idAziendaDiDestinazione == null) {
			if (other.idAziendaDiDestinazione != null)
				return false;
		} else if (!idAziendaDiDestinazione.equals(other.idAziendaDiDestinazione))
			return false;
		if (idUtenteAggiornamento == null) {
			if (other.idUtenteAggiornamento != null)
				return false;
		} else if (!idUtenteAggiornamento.equals(other.idUtenteAggiornamento))
			return false;
		return true;
	}
}