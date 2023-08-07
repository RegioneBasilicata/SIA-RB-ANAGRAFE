package it.csi.smranag.smrgaa.dto.stampe.protocollo;

import it.csi.smranag.smrgaa.dto.stampe.documento.DocAltroParticelle;
import it.csi.smranag.smrgaa.dto.stampe.documento.DocDichiarazioni;
import it.csi.smranag.smrgaa.dto.stampe.documento.DocFirma;
import it.csi.smranag.smrgaa.dto.stampe.documento.DocSottoscritto;
import it.csi.smranag.smrgaa.dto.stampe.documento.HeaderStampaDocumento;

public class Protocollo {
	protected HeaderDocumento headerDocumento;
	protected HeaderStampaDocumento headerStampaDocumento;
	protected DocAnagrafico docAnagrafico;
	protected DocContoCorrente docContoCorrente;
	protected DocCorrettivaTerreni docCorrettivaTerreni;
	protected DocIdentificazione docIdentificazione;
	protected DocProprietari docProprietari;
	protected DocTerreni docTerreni;
	protected DocZootecnico docZootecnico;
	protected DocAltroParticelle docAltroParticelle;
	protected DocDichiarazioni docDichiarazioni;
	protected DocFirma docFirma;
	protected DocSottoscritto docSottoscritto;
	
	public DocDichiarazioni getDocDichiarazioni() {
		return docDichiarazioni;
	}
	public void setDocDichiarazioni(DocDichiarazioni docDichiarazioni) {
		this.docDichiarazioni = docDichiarazioni;
	}
	public HeaderDocumento getHeaderDocumento() {
		return headerDocumento;
	}
	public void setHeaderDocumento(HeaderDocumento headerDocumento) {
		this.headerDocumento = headerDocumento;
	}
	public DocAnagrafico getDocAnagrafico() {
		return docAnagrafico;
	}
	public void setDocAnagrafico(DocAnagrafico docAnagrafico) {
		this.docAnagrafico = docAnagrafico;
	}
	public DocContoCorrente getDocContoCorrente() {
		return docContoCorrente;
	}
	public void setDocContoCorrente(DocContoCorrente docContoCorrente) {
		this.docContoCorrente = docContoCorrente;
	}
	public DocCorrettivaTerreni getDocCorrettivaTerreni() {
		return docCorrettivaTerreni;
	}
	public void setDocCorrettivaTerreni(DocCorrettivaTerreni docCorrettivaTerreni) {
		this.docCorrettivaTerreni = docCorrettivaTerreni;
	}
	public DocIdentificazione getDocIdentificazione() {
		return docIdentificazione;
	}
	public void setDocIdentificazione(DocIdentificazione docIdentificazione) {
		this.docIdentificazione = docIdentificazione;
	}
	public DocProprietari getDocProprietari() {
		return docProprietari;
	}
	public void setDocProprietari(DocProprietari docProprietari) {
		this.docProprietari = docProprietari;
	}
	public DocTerreni getDocTerreni() {
		return docTerreni;
	}
	public void setDocTerreni(DocTerreni docTerreni) {
		this.docTerreni = docTerreni;
	}
	public DocZootecnico getDocZootecnico() {
		return docZootecnico;
	}
	public void setDocZootecnico(DocZootecnico docZootecnico) {
		this.docZootecnico = docZootecnico;
	}
	public HeaderStampaDocumento getHeaderStampaDocumento() {
		return headerStampaDocumento;
	}
	public void setHeaderStampaDocumento(HeaderStampaDocumento headerStampaDocumento) {
		this.headerStampaDocumento = headerStampaDocumento;
	}
	public DocAltroParticelle getDocAltroParticelle() {
		return docAltroParticelle;
	}
	public void setDocAltroParticelle(DocAltroParticelle docAltroParticelle) {
		this.docAltroParticelle = docAltroParticelle;
	}
	public DocFirma getDocFirma() {
		return docFirma;
	}
	public void setDocFirma(DocFirma docFirma) {
		this.docFirma = docFirma;
	}
	public DocSottoscritto getDocSottoscritto() {
		return docSottoscritto;
	}
	public void setDocSottoscritto(DocSottoscritto docSottoscritto) {
		this.docSottoscritto = docSottoscritto;
	}
	
}
