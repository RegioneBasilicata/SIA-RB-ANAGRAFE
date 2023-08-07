package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;


/**
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
public class DocumentoProprietarioVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6309653918726198010L;
	private Long idDocumentoProprietario = null;
	private Long idDocumento = null;
	private String cuaa = null;
	private String denominazione = null;
	private Date dataUltimoAggiornamento = null;
	private Long utenteUltimoAggiornamento = null;
	private Date dataInserimento = null;
	private String flagValidato = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public DocumentoProprietarioVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * @param idDocumentoProprietario
	 * @param idDocumento
	 * @param cuaa
	 * @param denominazione
	 * @param dataUltimoAggiornamento
	 * @param utenteUltimoAggiornamento
	 * @param dataInserimento
	 * @param flagValidato
	 */
	public DocumentoProprietarioVO(Long idDocumentoProprietario, Long idDocumento, String cuaa, String denominazione, Date dataUltimoAggiornamento, Long utenteUltimoAggiornamento, Date dataInserimento, String flagValidato) {
		super();
		this.idDocumentoProprietario = idDocumentoProprietario;
		this.idDocumento = idDocumento;
		this.cuaa = cuaa;
		this.denominazione = denominazione;
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
		this.utenteUltimoAggiornamento = utenteUltimoAggiornamento;
		this.dataInserimento = dataInserimento;
		this.flagValidato = flagValidato;
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
	 * @return the dataInserimento
	 */
	public Date getDataInserimento() {
		return dataInserimento;
	}

	/**
	 * @param dataInserimento the dataInserimento to set
	 */
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	/**
	 * @return the dataUltimoAggiornamento
	 */
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	/**
	 * @param dataUltimoAggiornamento the dataUltimoAggiornamento to set
	 */
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @param denominazione the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @return the flagValidato
	 */
	public String getFlagValidato() {
		return flagValidato;
	}

	/**
	 * @param flagValidato the flagValidato to set
	 */
	public void setFlagValidato(String flagValidato) {
		this.flagValidato = flagValidato;
	}

	/**
	 * @return the idDocumento
	 */
	public Long getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idDocumento the idDocumento to set
	 */
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @return the idDocumentoProprietario
	 */
	public Long getIdDocumentoProprietario() {
		return idDocumentoProprietario;
	}

	/**
	 * @param idDocumentoProprietario the idDocumentoProprietario to set
	 */
	public void setIdDocumentoProprietario(Long idDocumentoProprietario) {
		this.idDocumentoProprietario = idDocumentoProprietario;
	}

	/**
	 * @return the utenteUltimoAggiornamento
	 */
	public Long getUtenteUltimoAggiornamento() {
		return utenteUltimoAggiornamento;
	}

	/**
	 * @param utenteUltimoAggiornamento the utenteUltimoAggiornamento to set
	 */
	public void setUtenteUltimoAggiornamento(Long utenteUltimoAggiornamento) {
		this.utenteUltimoAggiornamento = utenteUltimoAggiornamento;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((cuaa == null) ? 0 : cuaa.hashCode());
		result = PRIME * result + ((dataInserimento == null) ? 0 : dataInserimento.hashCode());
		result = PRIME * result + ((dataUltimoAggiornamento == null) ? 0 : dataUltimoAggiornamento.hashCode());
		result = PRIME * result + ((denominazione == null) ? 0 : denominazione.hashCode());
		result = PRIME * result + ((flagValidato == null) ? 0 : flagValidato.hashCode());
		result = PRIME * result + ((idDocumento == null) ? 0 : idDocumento.hashCode());
		result = PRIME * result + ((idDocumentoProprietario == null) ? 0 : idDocumentoProprietario.hashCode());
		result = PRIME * result + ((utenteUltimoAggiornamento == null) ? 0 : utenteUltimoAggiornamento.hashCode());
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
		final DocumentoProprietarioVO other = (DocumentoProprietarioVO) obj;
		if (cuaa == null) {
			if (other.cuaa != null)
				return false;
		} else if (!cuaa.equals(other.cuaa))
			return false;
		if (dataInserimento == null) {
			if (other.dataInserimento != null)
				return false;
		} else if (!dataInserimento.equals(other.dataInserimento))
			return false;
		if (dataUltimoAggiornamento == null) {
			if (other.dataUltimoAggiornamento != null)
				return false;
		} else if (!dataUltimoAggiornamento.equals(other.dataUltimoAggiornamento))
			return false;
		if (denominazione == null) {
			if (other.denominazione != null)
				return false;
		} else if (!denominazione.equals(other.denominazione))
			return false;
		if (flagValidato == null) {
			if (other.flagValidato != null)
				return false;
		} else if (!flagValidato.equals(other.flagValidato))
			return false;
		if (idDocumento == null) {
			if (other.idDocumento != null)
				return false;
		} else if (!idDocumento.equals(other.idDocumento))
			return false;
		if (idDocumentoProprietario == null) {
			if (other.idDocumentoProprietario != null)
				return false;
		} else if (!idDocumentoProprietario.equals(other.idDocumentoProprietario))
			return false;
		if (utenteUltimoAggiornamento == null) {
			if (other.utenteUltimoAggiornamento != null)
				return false;
		} else if (!utenteUltimoAggiornamento.equals(other.utenteUltimoAggiornamento))
			return false;
		return true;
	}
}
