package it.csi.smranag.smrgaa.dto.stampe.documento;

import java.util.ArrayList;
import java.util.List;

public class DocSottoscritto {

	private String denominazione;
	private String dataNascita;
	private String luogoNascita;
	private String indirizzo;
	private String comune;
	private String prov;
	private String cap;
	private String codiceFiscale;
	private String partitaIva;
	public List<String> dichiarazioni;

	public void addDichiarazione(String dichiarazione)
	{
		if(dichiarazioni == null)
		{
			dichiarazioni = new ArrayList<>();
		}
		dichiarazioni.add(dichiarazione);
	}
	
	public List<String> getDichiarazioni() {
		return dichiarazioni;
	}

	public void setDichiarazioni(List<String> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}
	
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
	
	
	
}
