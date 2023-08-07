package it.csi.solmr.dto.anag;

import java.io.Serializable;


public class AnagraficaAzVO implements Serializable{
  
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5738715429176946432L;
	
	private String CUAA;
	private String partitaIVA;
	private String denominazione;
	private String sedelegIndirizzo;
	private String sedelegComune;
	private String descComune;
	private String sedelegProv;
	private String sedelegCAP;
	private String sedelegEstero;
	private String sedelegCittaEstero;
	private String intestazionePartitaIva;
	
	public String getCUAA() {
		return CUAA;
	}
	public void setCUAA(String cUAA) {
		CUAA = cUAA;
	}
	public String getPartitaIVA() {
		return partitaIVA;
	}
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getSedelegIndirizzo() {
		return sedelegIndirizzo;
	}
	public void setSedelegIndirizzo(String sedelegIndirizzo) {
		this.sedelegIndirizzo = sedelegIndirizzo;
	}
	public String getSedelegComune() {
		return sedelegComune;
	}
	public void setSedelegComune(String sedelegComune) {
		this.sedelegComune = sedelegComune;
	}
	public String getDescComune() {
		return descComune;
	}
	public void setDescComune(String descComune) {
		this.descComune = descComune;
	}
	public String getSedelegProv() {
		return sedelegProv;
	}
	public void setSedelegProv(String sedelegProv) {
		this.sedelegProv = sedelegProv;
	}
	public String getSedelegCAP() {
		return sedelegCAP;
	}
	public void setSedelegCAP(String sedelegCAP) {
		this.sedelegCAP = sedelegCAP;
	}
	public String getSedelegEstero() {
		return sedelegEstero;
	}
	public void setSedelegEstero(String sedelegEstero) {
		this.sedelegEstero = sedelegEstero;
	}
	public String getSedelegCittaEstero() {
		return sedelegCittaEstero;
	}
	public void setSedelegCittaEstero(String sedelegCittaEstero) {
		this.sedelegCittaEstero = sedelegCittaEstero;
	}
	public String getIntestazionePartitaIva() {
		return intestazionePartitaIva;
	}
	public void setIntestazionePartitaIva(String intestazionePartitaIva) {
		this.intestazionePartitaIva = intestazionePartitaIva;
	}
	
	
	
  
}
