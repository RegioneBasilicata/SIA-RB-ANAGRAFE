package it.csi.smranag.smrgaa.dto.stampe.documento;

import java.util.ArrayList;
import java.util.List;

import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;

public class DocAltroParticelle {

	private String denominazione;
	private String dataNascita;
	private String luogoNascita;
	private String indirizzo;
	private String comune;
	private String prov;
	private String cap;
	private String codiceFiscale;
	private String partitaIva;
	private String dataInizioValidita;
	private String dataFineValidita;
	private List<String> attestazioni;
	private List<StoricoParticellaVO> particella;
	
	public void addParticella(StoricoParticellaVO part)
	{
		if(particella == null){
			particella = new ArrayList<>();
		}
		
		particella.add(part);
	}
	public List<StoricoParticellaVO> getParticella() {
		return particella;
	}

	public void setParticella(List<StoricoParticellaVO> particella) {
		this.particella = particella;
	}

	public void addAttestazione(String descrizione)
	{
		if(attestazioni == null){
			attestazioni = new ArrayList<>();
		}
		
		attestazioni.add(descrizione);
	}
	
	public List<String> getAttestazioni() {
		return attestazioni;
	}
	public void setAttestazioni(List<String> attestazioni) {
		this.attestazioni = attestazioni;
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
	public String getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(String dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public String getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(String dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	
	
}
