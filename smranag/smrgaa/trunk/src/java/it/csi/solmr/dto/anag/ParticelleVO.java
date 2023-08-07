package it.csi.solmr.dto.anag;

import java.io.*;

public class ParticelleVO extends DettaglioTerreniVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 4867733616344356993L;

  private String sezione = null;
  private String subalterno = null;
  private String utilizzo = null;
  private Long foglio = null;
  private Long particella = null;
  private String flagBiologico = null;
  private String denominazioneProprietario = null;
  private String codFiscaleProprietario = null;
  private String zonaAltimetrica = null;
  private String tipoArea = null;
  private String casiParticolari = null;
  private Long idParticella = null;
  private Double supCatastale = null;
  private Double supUtilizzata = null;
  private Long idParticellaImport = null;
  private String numeroTotRecordForParticella = null;

  public String getSezione() {
    return sezione;
  }
  public void setSezione(String sezione) {
    this.sezione = sezione;
  }
  public void setSubalterno(String subalterno) {
    this.subalterno = subalterno;
  }
  public String getSubalterno() {
    return subalterno;
  }
  public void setUtilizzo(String utilizzo) {
    this.utilizzo = utilizzo;
  }
  public String getUtilizzo() {
    return utilizzo;
  }
  public void setFoglio(Long foglio) {
    this.foglio = foglio;
  }
  public Long getFoglio() {
    return foglio;
  }
  public void setParticella(Long particella) {
    this.particella = particella;
  }
  public Long getParticella() {
    return particella;
  }
  public void setFlagBiologico(String flagBiologico) {
    this.flagBiologico = flagBiologico;
  }
  public String getFlagBiologico() {
    return flagBiologico;
  }
  public void setDenominazioneProprietario(String denominazioneProprietario) {
    this.denominazioneProprietario = denominazioneProprietario;
  }
  public String getDenominazioneProprietario() {
    return denominazioneProprietario;
  }
  public void setCodFiscaleProprietario(String codFiscaleProprietario) {
    this.codFiscaleProprietario = codFiscaleProprietario;
  }
  public String getCodFiscaleProprietario() {
    return codFiscaleProprietario;
  }
  public void setZonaAltimetrica(String zonaAltimetrica) {
    this.zonaAltimetrica = zonaAltimetrica;
  }
  public String getZonaAltimetrica() {
    return zonaAltimetrica;
  }
  public void setTipoArea(String tipoArea) {
    this.tipoArea = tipoArea;
  }
  public String getTipoArea() {
    return tipoArea;
  }
  public void setCasiParticolari(String casiParticolari) {
    this.casiParticolari = casiParticolari;
  }
  public String getCasiParticolari() {
    return casiParticolari;
  }
  public void setIdParticella(Long idParticella) {
    this.idParticella = idParticella;
  }
  public Long getIdParticella() {
    return idParticella;
  }
  public void setSupCatastale(Double supCatastale) {
    this.supCatastale = supCatastale;
  }
  public Double getSupCatastale() {
    return supCatastale;
  }
  public void setSupUtilizzata(Double supUtilizzata) {
    this.supUtilizzata = supUtilizzata;
  }
  public Double getSupUtilizzata() {
    return supUtilizzata;
  }
  public void setIdParticellaImport(Long idParticellaImport) {
    this.idParticellaImport = idParticellaImport;
  }
  public Long getIdParticellaImport() {
    return idParticellaImport;
  }
  public String getNumeroTotRecordForParticella() {
    return numeroTotRecordForParticella;
  }
  public void setNumeroTotRecordForParticella(String numeroTotRecordForParticella) {
    this.numeroTotRecordForParticella = numeroTotRecordForParticella;
  }

}