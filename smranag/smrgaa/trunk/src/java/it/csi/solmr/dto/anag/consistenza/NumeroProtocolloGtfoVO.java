package it.csi.solmr.dto.anag.consistenza;

import java.io.Serializable;
import java.util.*;

/**
 * Classe che si occupa di mappare la tabella DB_NUMERO_PROTOCOLLO_GTFO
 * 
 * @author Mauro Vocale
 *
 */
public class NumeroProtocolloGtfoVO implements Serializable {
	
	private static final long serialVersionUID = -4408471349870895515L;
	
	private Long idNumeroProtocolloGtfo = null; 
	private String codiceEnte = null;  
	private String anno = null; 
	private String progressivo = null; 
	private String flagIntermediario = null; 
	private String progrInizio = null; 
	private String progrFine = null;
	private Date dataProtocollo = null;
	private String numeroProtocollo = null;
	
	/**
	 * Costruttore di default 
	 */
	public NumeroProtocolloGtfoVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idNumeroProtocolloGtfo
	 * @param codiceEnte
	 * @param anno
	 * @param progressivo
	 * @param flagIntermediario
	 * @param progrInizio
	 * @param progrFine
	 * @param dataProtocollo
	 * @param numeroProtocollo
	 */
	public NumeroProtocolloGtfoVO(Long idNumeroProtocolloGtfo, String codiceEnte, String anno, String progressivo, String flagIntermediario, String progrInizio, String progrFine, Date dataProtocollo, String numeroProtocollo) {
		super();
		this.idNumeroProtocolloGtfo = idNumeroProtocolloGtfo;
		this.codiceEnte = codiceEnte;
		this.anno = anno;
		this.progressivo = progressivo;
		this.flagIntermediario = flagIntermediario;
		this.progrInizio = progrInizio;
		this.progrFine = progrFine;
		this.dataProtocollo = dataProtocollo;
		this.numeroProtocollo = numeroProtocollo;
	}

	/**
	 * @return the idNumeroProtocolloGtfo
	 */
	public Long getIdNumeroProtocolloGtfo() {
		return idNumeroProtocolloGtfo;
	}

	/**
	 * @param idNumeroProtocolloGtfo the idNumeroProtocolloGtfo to set
	 */
	public void setIdNumeroProtocolloGtfo(Long idNumeroProtocolloGtfo) {
		this.idNumeroProtocolloGtfo = idNumeroProtocolloGtfo;
	}

	/**
	 * @return the codiceEnte
	 */
	public String getCodiceEnte() {
		return codiceEnte;
	}

	/**
	 * @param codiceEnte the codiceEnte to set
	 */
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}

	/**
	 * @return the anno
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(String anno) {
		this.anno = anno;
	}

	/**
	 * @return the progressivo
	 */
	public String getProgressivo() {
		return progressivo;
	}

	/**
	 * @param progressivo the progressivo to set
	 */
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	/**
	 * @return the flagIntermediario
	 */
	public String getFlagIntermediario() {
		return flagIntermediario;
	}

	/**
	 * @param flagIntermediario the flagIntermediario to set
	 */
	public void setFlagIntermediario(String flagIntermediario) {
		this.flagIntermediario = flagIntermediario;
	}

	/**
	 * @return the progrInizio
	 */
	public String getProgrInizio() {
		return progrInizio;
	}

	/**
	 * @param progrInizio the progrInizio to set
	 */
	public void setProgrInizio(String progrInizio) {
		this.progrInizio = progrInizio;
	}

	/**
	 * @return the progrFine
	 */
	public String getProgrFine() {
		return progrFine;
	}

	/**
	 * @param progrFine the progrFine to set
	 */
	public void setProgrFine(String progrFine) {
		this.progrFine = progrFine;
	}

	/**
	 * @return the dataProtocollo
	 */
	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	/**
	 * @param dataProtocollo the dataProtocollo to set
	 */
	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anno == null) ? 0 : anno.hashCode());
		result = prime * result + ((codiceEnte == null) ? 0 : codiceEnte.hashCode());
		result = prime * result + ((dataProtocollo == null) ? 0 : dataProtocollo.hashCode());
		result = prime * result + ((flagIntermediario == null) ? 0 : flagIntermediario.hashCode());
		result = prime * result + ((idNumeroProtocolloGtfo == null) ? 0 : idNumeroProtocolloGtfo.hashCode());
		result = prime * result + ((numeroProtocollo == null) ? 0 : numeroProtocollo.hashCode());
		result = prime * result + ((progrFine == null) ? 0 : progrFine.hashCode());
		result = prime * result + ((progrInizio == null) ? 0 : progrInizio.hashCode());
		result = prime * result + ((progressivo == null) ? 0 : progressivo.hashCode());
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
		final NumeroProtocolloGtfoVO other = (NumeroProtocolloGtfoVO) obj;
		if (anno == null) {
			if (other.anno != null)
				return false;
		} else if (!anno.equals(other.anno))
			return false;
		if (codiceEnte == null) {
			if (other.codiceEnte != null)
				return false;
		} else if (!codiceEnte.equals(other.codiceEnte))
			return false;
		if (dataProtocollo == null) {
			if (other.dataProtocollo != null)
				return false;
		} else if (!dataProtocollo.equals(other.dataProtocollo))
			return false;
		if (flagIntermediario == null) {
			if (other.flagIntermediario != null)
				return false;
		} else if (!flagIntermediario.equals(other.flagIntermediario))
			return false;
		if (idNumeroProtocolloGtfo == null) {
			if (other.idNumeroProtocolloGtfo != null)
				return false;
		} else if (!idNumeroProtocolloGtfo.equals(other.idNumeroProtocolloGtfo))
			return false;
		if (numeroProtocollo == null) {
			if (other.numeroProtocollo != null)
				return false;
		} else if (!numeroProtocollo.equals(other.numeroProtocollo))
			return false;
		if (progrFine == null) {
			if (other.progrFine != null)
				return false;
		} else if (!progrFine.equals(other.progrFine))
			return false;
		if (progrInizio == null) {
			if (other.progrInizio != null)
				return false;
		} else if (!progrInizio.equals(other.progrInizio))
			return false;
		if (progressivo == null) {
			if (other.progressivo != null)
				return false;
		} else if (!progressivo.equals(other.progressivo))
			return false;
		return true;
	}
			
}