package it.csi.solmr.dto.anag.fabbricati;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_COLTURA_SERRA
 * 
 * @author Mauro Vocale
 *
 */
public class TipoColturaSerraVO implements Serializable {
	
	private static final long serialVersionUID = -6323754564188479293L;
	
	private Long idColturaSerra = null;
	private Long idTipologiaFabbricato = null;
	private String descrizione = null;
	private String mesiRiscaldamento = null;
	
	/**
	 * Costruttore di default
	 */
	public TipoColturaSerraVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idColturaSerra
	 * @param idTipologiaFabbricato
	 * @param descrizione
	 * @param mesiRiscaldamento
	 */
	public TipoColturaSerraVO(Long idColturaSerra, Long idTipologiaFabbricato, String descrizione, String mesiRiscaldamento) {
		super();
		this.idColturaSerra = idColturaSerra;
		this.idTipologiaFabbricato = idTipologiaFabbricato;
		this.descrizione = descrizione;
		this.mesiRiscaldamento = mesiRiscaldamento;
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
	 * @return the idColturaSerra
	 */
	public Long getIdColturaSerra() {
		return idColturaSerra;
	}

	/**
	 * @param idColturaSerra the idColturaSerra to set
	 */
	public void setIdColturaSerra(Long idColturaSerra) {
		this.idColturaSerra = idColturaSerra;
	}

	/**
	 * @return the idTipologiaFabbricato
	 */
	public Long getIdTipologiaFabbricato() {
		return idTipologiaFabbricato;
	}

	/**
	 * @param idTipologiaFabbricato the idTipologiaFabbricato to set
	 */
	public void setIdTipologiaFabbricato(Long idTipologiaFabbricato) {
		this.idTipologiaFabbricato = idTipologiaFabbricato;
	}

	/**
	 * @return the mesiRiscaldamento
	 */
	public String getMesiRiscaldamento() {
		return mesiRiscaldamento;
	}

	/**
	 * @param mesiRiscaldamento the mesiRiscaldamento to set
	 */
	public void setMesiRiscaldamento(String mesiRiscaldamento) {
		this.mesiRiscaldamento = mesiRiscaldamento;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idColturaSerra == null) ? 0 : idColturaSerra.hashCode());
		result = PRIME * result + ((idTipologiaFabbricato == null) ? 0 : idTipologiaFabbricato.hashCode());
		result = PRIME * result + ((mesiRiscaldamento == null) ? 0 : mesiRiscaldamento.hashCode());
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
		final TipoColturaSerraVO other = (TipoColturaSerraVO) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idColturaSerra == null) {
			if (other.idColturaSerra != null)
				return false;
		} else if (!idColturaSerra.equals(other.idColturaSerra))
			return false;
		if (idTipologiaFabbricato == null) {
			if (other.idTipologiaFabbricato != null)
				return false;
		} else if (!idTipologiaFabbricato.equals(other.idTipologiaFabbricato))
			return false;
		if (mesiRiscaldamento == null) {
			if (other.mesiRiscaldamento != null)
				return false;
		} else if (!mesiRiscaldamento.equals(other.mesiRiscaldamento))
			return false;
		return true;
	}

}
