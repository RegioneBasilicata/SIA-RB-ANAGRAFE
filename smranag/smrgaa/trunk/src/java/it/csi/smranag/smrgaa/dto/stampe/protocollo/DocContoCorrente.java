package it.csi.smranag.smrgaa.dto.stampe.protocollo;

import it.csi.smranag.smrgaa.presentation.client.stampe.StampeGaaServlet;
import it.csi.solmr.util.DateUtils;

public class DocContoCorrente {

	private String cin; // DB_CONTO_CORRENTE.CIN
	private String bban; // DB_CONTO_CORRENTE.BBAN
	private String iban; // DB_CONTO_CORRENTE.IBAN
	private String cifraCtrl;// DB_CONTO_CORRENTE.CIFRA_CONTROLLO
	private String flagValidato;// DB:CONTO_CORRENTE_FLAG_VALIDATO
	private String bic;
	private String codPaese;// DB_TIPO_SPORTELLO.CODICE_PAESE
	private String intestazione; // DB_CONTO_CORRENTE.INTESTAZIONE
	private String denominazioneBanca; // DB_TIPO_BANCA.DENOMINAZIONE
	private String cab; // DB_TIPO_SPORTELLO.CAB
	private String indirizzoSportello; // DB_TIPO_SPORTELLO.INDIRIZZO
	private String capSportello; // DB_TIPO_SPORTELLO.CAP
	private String istatComuneSportello; // DB_TIPO_SPORTELLO.COMUNE
	private String denominazioneSportello; // DB_TIPO_SPORTELLO.DENOMINAZIONE
	private String descrizioneComuneSportello; // COMUNE.DESCOM da
												// DB_TIPO_SPORTELLO.COMUNE
	private String flagSportelloGf; // DB_TIPO_SPORTELLO.FLAG_SPORTELLO_GF
	private java.util.Date dataAggiornamento; // DB_CONTO_CORRENTE.DATA_AGGIORNAMENTO
	private java.util.Date dataEstinzione; // DB_CONTO_CORRENTE.DATA_ESTINZIONE
	private java.util.Date dataInizioValiditaContoCorrente; // DB_CONTO_CORRENTE.DATA_INIZIO_VALIDITA
	private java.util.Date dataFineValiditaContoCorrente; // DB_CONTO_CORRENTE.DATA_FINE_VALIDITA

	public String getDenominazioneBanca() {
		return denominazioneBanca;
	}

	public void setDenominazioneBanca(String denominazioneBanca) {
		this.denominazioneBanca = denominazioneBanca;
	}

	public String getCab() {
		return cab;
	}

	public void setCab(String cab) {
		this.cab = cab;
	}

	public String getIndirizzoSportello() {
		return indirizzoSportello;
	}

	public void setIndirizzoSportello(String indirizzoSportello) {
		this.indirizzoSportello = indirizzoSportello;
	}

	public String getCapSportello() {
		return capSportello;
	}

	public void setCapSportello(String capSportello) {
		this.capSportello = capSportello;
	}

	public String getIstatComuneSportello() {
		return istatComuneSportello;
	}

	public void setIstatComuneSportello(String istatComuneSportello) {
		this.istatComuneSportello = istatComuneSportello;
	}

	public String getDenominazioneSportello() {
		return denominazioneSportello;
	}

	public void setDenominazioneSportello(String denominazioneSportello) {
		this.denominazioneSportello = denominazioneSportello;
	}

	public String getDescrizioneComuneSportello() {
		return descrizioneComuneSportello;
	}

	public void setDescrizioneComuneSportello(String descrizioneComuneSportello) {
		this.descrizioneComuneSportello = descrizioneComuneSportello;
	}

	public String getFlagSportelloGf() {
		return flagSportelloGf;
	}

	public void setFlagSportelloGf(String flagSportelloGf) {
		this.flagSportelloGf = flagSportelloGf;
	}

	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public java.util.Date getDataEstinzione() {
		return dataEstinzione;
	}

	public void setDataEstinzione(java.util.Date dataEstinzione) {
		this.dataEstinzione = dataEstinzione;
	}

	public java.util.Date getDataInizioValiditaContoCorrente() {
		return dataInizioValiditaContoCorrente;
	}

	public void setDataInizioValiditaContoCorrente(java.util.Date dataInizioValiditaContoCorrente) {
		this.dataInizioValiditaContoCorrente = dataInizioValiditaContoCorrente;
	}

	public java.util.Date getDataFineValiditaContoCorrente() {
		return dataFineValiditaContoCorrente;
	}
	
	public String getDataInizioValiditaContoCorrenteFmt() {
		return StampeGaaServlet.checkNull(
		          DateUtils.formatDateNotNull(dataInizioValiditaContoCorrente));
	}
	
	public String getDataFineValiditaContoCorrenteFmt() {
		return StampeGaaServlet.checkNull(
		          DateUtils.formatDateNotNull(dataFineValiditaContoCorrente));
	}

	public void setDataFineValiditaContoCorrente(java.util.Date dataFineValiditaContoCorrente) {
		this.dataFineValiditaContoCorrente = dataFineValiditaContoCorrente;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getBban() {
		return bban;
	}

	public void setBban(String bban) {
		this.bban = bban;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getCifraCtrl() {
		return cifraCtrl;
	}

	public void setCifraCtrl(String cifraCtrl) {
		this.cifraCtrl = cifraCtrl;
	}

	public String getFlagValidato() {
		return flagValidato;
	}

	public void setFlagValidato(String flagValidato) {
		this.flagValidato = flagValidato;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getCodPaese() {
		return codPaese;
	}

	public void setCodPaese(String codPaese) {
		this.codPaese = codPaese;
	}

	public String getIntestazione() {
		return intestazione;
	}

	public void setIntestazione(String intestazione) {
		this.intestazione = intestazione;
	}

}
