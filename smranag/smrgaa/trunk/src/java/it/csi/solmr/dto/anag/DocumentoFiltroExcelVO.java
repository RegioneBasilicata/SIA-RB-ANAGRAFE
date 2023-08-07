package it.csi.solmr.dto.anag;

import java.io.*;

/**
 * Value object utilizzato per la costruzione del file excel relativo ai documenti: contiene i filtri di ricerca utilizzati dall'utente
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
public class DocumentoFiltroExcelVO implements Serializable {
	
	private static final long serialVersionUID = -4357785007188733127L;
	
	private String cuaa = null;
	private String descTipoTipologiaDocumento = null;
	private String descTipoCategoriaDocumento = null;
	private String descTipoDocumento = null;
	private String descStatoDocumento = null;
	private String protocollazione = null;
	private String dataScadenza = null;
	private String intestazione = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public DocumentoFiltroExcelVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param cuaa
	 * @param descTipoTipologiaDocumento
	 * @param descTipoCategoriaDocumento
	 * @param descTipoDocumento
	 * @param descStatoDocumento
	 * @param protocollazione
	 * @param dataScadenza
	 * @param intestazione
	 */
	public DocumentoFiltroExcelVO(String cuaa, String descTipoTipologiaDocumento, String descTipoCategoriaDocumento, String descTipoDocumento, String descStatoDocumento, String protocollazione, String dataScadenza, String intestazione) {
		super();
		this.cuaa = cuaa;
		this.descTipoTipologiaDocumento = descTipoTipologiaDocumento;
		this.descTipoCategoriaDocumento = descTipoCategoriaDocumento;
		this.descTipoDocumento = descTipoDocumento;
		this.descStatoDocumento = descStatoDocumento;
		this.protocollazione = protocollazione;
		this.dataScadenza = dataScadenza;
		this.intestazione = intestazione;
	}

	/**
	 * @return the cuaa
	 */
	public String getCuaa() {
		return cuaa;
	}

	/**
	 * @param cuaa the cuaa to set
	 */
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	/**
	 * @return the dataScadenza
	 */
	public String getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @param dataScadenza the dataScadenza to set
	 */
	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @return the descStatoDocumento
	 */
	public String getDescStatoDocumento() {
		return descStatoDocumento;
	}

	/**
	 * @param descStatoDocumento the descStatoDocumento to set
	 */
	public void setDescStatoDocumento(String descStatoDocumento) {
		this.descStatoDocumento = descStatoDocumento;
	}

	/**
	 * @return the descTipoCategoriaDocumento
	 */
	public String getDescTipoCategoriaDocumento() {
		return descTipoCategoriaDocumento;
	}

	/**
	 * @param descTipoCategoriaDocumento the descTipoCategoriaDocumento to set
	 */
	public void setDescTipoCategoriaDocumento(String descTipoCategoriaDocumento) {
		this.descTipoCategoriaDocumento = descTipoCategoriaDocumento;
	}

	/**
	 * @return the descTipoDocumento
	 */
	public String getDescTipoDocumento() {
		return descTipoDocumento;
	}

	/**
	 * @param descTipoDocumento the descTipoDocumento to set
	 */
	public void setDescTipoDocumento(String descTipoDocumento) {
		this.descTipoDocumento = descTipoDocumento;
	}

	/**
	 * @return the descTipoTipologiaDocumento
	 */
	public String getDescTipoTipologiaDocumento() {
		return descTipoTipologiaDocumento;
	}

	/**
	 * @param descTipoTipologiaDocumento the descTipoTipologiaDocumento to set
	 */
	public void setDescTipoTipologiaDocumento(String descTipoTipologiaDocumento) {
		this.descTipoTipologiaDocumento = descTipoTipologiaDocumento;
	}

	/**
	 * @return the intestazione
	 */
	public String getIntestazione() {
		return intestazione;
	}

	/**
	 * @param intestazione the intestazione to set
	 */
	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}

	/**
	 * @return the protocollazione
	 */
	public String getProtocollazione() {
		return protocollazione;
	}

	/**
	 * @param protocollazione the protocollazione to set
	 */
	public void setProtocollazione(String protocollazione) {
		this.protocollazione = protocollazione;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((cuaa == null) ? 0 : cuaa.hashCode());
		result = PRIME * result + ((dataScadenza == null) ? 0 : dataScadenza.hashCode());
		result = PRIME * result + ((descStatoDocumento == null) ? 0 : descStatoDocumento.hashCode());
		result = PRIME * result + ((descTipoCategoriaDocumento == null) ? 0 : descTipoCategoriaDocumento.hashCode());
		result = PRIME * result + ((descTipoDocumento == null) ? 0 : descTipoDocumento.hashCode());
		result = PRIME * result + ((descTipoTipologiaDocumento == null) ? 0 : descTipoTipologiaDocumento.hashCode());
		result = PRIME * result + ((intestazione == null) ? 0 : intestazione.hashCode());
		result = PRIME * result + ((protocollazione == null) ? 0 : protocollazione.hashCode());
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
		final DocumentoFiltroExcelVO other = (DocumentoFiltroExcelVO) obj;
		if (cuaa == null) {
			if (other.cuaa != null)
				return false;
		} else if (!cuaa.equals(other.cuaa))
			return false;
		if (dataScadenza == null) {
			if (other.dataScadenza != null)
				return false;
		} else if (!dataScadenza.equals(other.dataScadenza))
			return false;
		if (descStatoDocumento == null) {
			if (other.descStatoDocumento != null)
				return false;
		} else if (!descStatoDocumento.equals(other.descStatoDocumento))
			return false;
		if (descTipoCategoriaDocumento == null) {
			if (other.descTipoCategoriaDocumento != null)
				return false;
		} else if (!descTipoCategoriaDocumento.equals(other.descTipoCategoriaDocumento))
			return false;
		if (descTipoDocumento == null) {
			if (other.descTipoDocumento != null)
				return false;
		} else if (!descTipoDocumento.equals(other.descTipoDocumento))
			return false;
		if (descTipoTipologiaDocumento == null) {
			if (other.descTipoTipologiaDocumento != null)
				return false;
		} else if (!descTipoTipologiaDocumento.equals(other.descTipoTipologiaDocumento))
			return false;
		if (intestazione == null) {
			if (other.intestazione != null)
				return false;
		} else if (!intestazione.equals(other.intestazione))
			return false;
		if (protocollazione == null) {
			if (other.protocollazione != null)
				return false;
		} else if (!protocollazione.equals(other.protocollazione))
			return false;
		return true;
	}
}
