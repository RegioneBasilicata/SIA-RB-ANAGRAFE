package it.csi.solmr.dto.anag;

import java.io.*;

/**
 * Value Object che mappa la tabella DB_TIPO_CATEGORIA_DOCUMENTO
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
public class TipoCategoriaDocumentoVO implements Serializable {

	private static final long serialVersionUID = -5903096480673261991L;
	
	private Long idCategoriaDocumento = null;
	private Long idTipologiaDocumento = null;
	private String descrizione = null;
	private String identificativo = null;
	private String tipoIdentificativo = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public TipoCategoriaDocumentoVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idCategoriaDocumento
	 * @param idTipologiaDocumento
	 * @param descrizione
	 * @param identificativo
	 * @param tipoIdentificativo
	 */
	public TipoCategoriaDocumentoVO(Long idCategoriaDocumento, Long idTipologiaDocumento, String descrizione, String identificativo, String tipoIdentificativo) {
		super();
		this.idCategoriaDocumento = idCategoriaDocumento;
		this.idTipologiaDocumento = idTipologiaDocumento;
		this.descrizione = descrizione;
		this.identificativo = identificativo;
		this.tipoIdentificativo = tipoIdentificativo;
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
	 * @return the idCategoriaDocumento
	 */
	public Long getIdCategoriaDocumento() {
		return idCategoriaDocumento;
	}

	/**
	 * @param idCategoriaDocumento the idCategoriaDocumento to set
	 */
	public void setIdCategoriaDocumento(Long idCategoriaDocumento) {
		this.idCategoriaDocumento = idCategoriaDocumento;
	}

	/**
	 * @return the identificativo
	 */
	public String getIdentificativo() {
		return identificativo;
	}

	/**
	 * @param identificativo the identificativo to set
	 */
	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	/**
	 * @return the idTipologiaDocumento
	 */
	public Long getIdTipologiaDocumento() {
		return idTipologiaDocumento;
	}

	/**
	 * @param idTipologiaDocumento the idTipologiaDocumento to set
	 */
	public void setIdTipologiaDocumento(Long idTipologiaDocumento) {
		this.idTipologiaDocumento = idTipologiaDocumento;
	}

	/**
	 * @return the tipoIdentificativo
	 */
	public String getTipoIdentificativo() {
		return tipoIdentificativo;
	}

	/**
	 * @param tipoIdentificativo the tipoIdentificativo to set
	 */
	public void setTipoIdentificativo(String tipoIdentificativo) {
		this.tipoIdentificativo = tipoIdentificativo;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idCategoriaDocumento == null) ? 0 : idCategoriaDocumento.hashCode());
		result = PRIME * result + ((idTipologiaDocumento == null) ? 0 : idTipologiaDocumento.hashCode());
		result = PRIME * result + ((identificativo == null) ? 0 : identificativo.hashCode());
		result = PRIME * result + ((tipoIdentificativo == null) ? 0 : tipoIdentificativo.hashCode());
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
		final TipoCategoriaDocumentoVO other = (TipoCategoriaDocumentoVO) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idCategoriaDocumento == null) {
			if (other.idCategoriaDocumento != null)
				return false;
		} else if (!idCategoriaDocumento.equals(other.idCategoriaDocumento))
			return false;
		if (idTipologiaDocumento == null) {
			if (other.idTipologiaDocumento != null)
				return false;
		} else if (!idTipologiaDocumento.equals(other.idTipologiaDocumento))
			return false;
		if (identificativo == null) {
			if (other.identificativo != null)
				return false;
		} else if (!identificativo.equals(other.identificativo))
			return false;
		if (tipoIdentificativo == null) {
			if (other.tipoIdentificativo != null)
				return false;
		} else if (!tipoIdentificativo.equals(other.tipoIdentificativo))
			return false;
		return true;
	}

}
