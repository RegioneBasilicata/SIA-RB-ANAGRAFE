package it.csi.solmr.dto.anag;

import java.io.*;

/**
 * Value Object utilizzato per la costruzione del foglio excel relativo ai documenti
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
public class DocumentoExcelVO implements Serializable {

	private static final long serialVersionUID = 1028841957678613249L;
	
	private String descTipoTipologiaDocumento = null;
	private String descTipoCategoriaDocumento = null;
	private String descTipoDocumento = null;
	private String dataInizioDocumento = null;
	private String dataFineDocumento = null;
	private String numeroProtocollo = null;
	private String dataProtocollo = null;
	private String descStatoDocumento = null;
	private DocumentoFiltroExcelVO documentoFiltroExcelVO = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public DocumentoExcelVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param descTipoTipologiaDocumento
	 * @param descTipoCategoriaDocumento
	 * @param descTipoDocumento
	 * @param dataInizioDocumento
	 * @param dataFineDocumento
	 * @param numeroProtocollo
	 * @param dataProtocollo
	 * @param descStatoDocumento
	 * @param documentoFiltroExcelVO
	 */
	public DocumentoExcelVO(String descTipoTipologiaDocumento, String descTipoCategoriaDocumento, String descTipoDocumento, String dataInizioDocumento, String dataFineDocumento, String numeroProtocollo, String dataProtocollo, String descStatoDocumento, DocumentoFiltroExcelVO documentoFiltroExcelVO) {
		super();
		this.descTipoTipologiaDocumento = descTipoTipologiaDocumento;
		this.descTipoCategoriaDocumento = descTipoCategoriaDocumento;
		this.descTipoDocumento = descTipoDocumento;
		this.dataInizioDocumento = dataInizioDocumento;
		this.dataFineDocumento = dataFineDocumento;
		this.numeroProtocollo = numeroProtocollo;
		this.dataProtocollo = dataProtocollo;
		this.descStatoDocumento = descStatoDocumento;
		this.documentoFiltroExcelVO = documentoFiltroExcelVO;
	}

	/**
	 * @return the dataFineDocumento
	 */
	public String getDataFineDocumento() {
		return dataFineDocumento;
	}

	/**
	 * @param dataFineDocumento the dataFineDocumento to set
	 */
	public void setDataFineDocumento(String dataFineDocumento) {
		this.dataFineDocumento = dataFineDocumento;
	}

	/**
	 * @return the dataInizioDocumento
	 */
	public String getDataInizioDocumento() {
		return dataInizioDocumento;
	}

	/**
	 * @param dataInizioDocumento the dataInizioDocumento to set
	 */
	public void setDataInizioDocumento(String dataInizioDocumento) {
		this.dataInizioDocumento = dataInizioDocumento;
	}

	/**
	 * @return the dataProtocollo
	 */
	public String getDataProtocollo() {
		return dataProtocollo;
	}

	/**
	 * @param dataProtocollo the dataProtocollo to set
	 */
	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
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
	 * @return the documentoFiltroExcelVO
	 */
	public DocumentoFiltroExcelVO getDocumentoFiltroExcelVO() {
		return documentoFiltroExcelVO;
	}

	/**
	 * @param documentoFiltroExcelVO the documentoFiltroExcelVO to set
	 */
	public void setDocumentoFiltroExcelVO(
			DocumentoFiltroExcelVO documentoFiltroExcelVO) {
		this.documentoFiltroExcelVO = documentoFiltroExcelVO;
	}

	/**
	 * @return the numeroProtocollo
	 */
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	/**
	 * @param numeroProtocollo the numeroProtocollo to set
	 */
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataFineDocumento == null) ? 0 : dataFineDocumento.hashCode());
		result = PRIME * result + ((dataInizioDocumento == null) ? 0 : dataInizioDocumento.hashCode());
		result = PRIME * result + ((dataProtocollo == null) ? 0 : dataProtocollo.hashCode());
		result = PRIME * result + ((descStatoDocumento == null) ? 0 : descStatoDocumento.hashCode());
		result = PRIME * result + ((descTipoCategoriaDocumento == null) ? 0 : descTipoCategoriaDocumento.hashCode());
		result = PRIME * result + ((descTipoDocumento == null) ? 0 : descTipoDocumento.hashCode());
		result = PRIME * result + ((descTipoTipologiaDocumento == null) ? 0 : descTipoTipologiaDocumento.hashCode());
		result = PRIME * result + ((documentoFiltroExcelVO == null) ? 0 : documentoFiltroExcelVO.hashCode());
		result = PRIME * result + ((numeroProtocollo == null) ? 0 : numeroProtocollo.hashCode());
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
		final DocumentoExcelVO other = (DocumentoExcelVO) obj;
		if (dataFineDocumento == null) {
			if (other.dataFineDocumento != null)
				return false;
		} else if (!dataFineDocumento.equals(other.dataFineDocumento))
			return false;
		if (dataInizioDocumento == null) {
			if (other.dataInizioDocumento != null)
				return false;
		} else if (!dataInizioDocumento.equals(other.dataInizioDocumento))
			return false;
		if (dataProtocollo == null) {
			if (other.dataProtocollo != null)
				return false;
		} else if (!dataProtocollo.equals(other.dataProtocollo))
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
		if (documentoFiltroExcelVO == null) {
			if (other.documentoFiltroExcelVO != null)
				return false;
		} else if (!documentoFiltroExcelVO.equals(other.documentoFiltroExcelVO))
			return false;
		if (numeroProtocollo == null) {
			if (other.numeroProtocollo != null)
				return false;
		} else if (!numeroProtocollo.equals(other.numeroProtocollo))
			return false;
		return true;
	}
}
