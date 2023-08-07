package it.csi.solmr.dto.anag;

import java.io.*;

public class DettaglioTerreniVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -1202883055662283881L;

  private String descTipoConduzione = null;
  private String descComune = null;
  private Long idTipoConduzione = null;
  private String descProvincia = null;
  private Double SAU = null;
  private Double supTotale = null;
  private String idComune = null;
  private String descUtilizzo = null;
  private Long idTipoUtilizzo = null;

  public void setSupTotale(Double supTotale) {
    this.supTotale = supTotale;
  }
  public Double getSupTotale() {
    return supTotale;
  }
  public void setSAU(Double SAU) {
    this.SAU = SAU;
  }
  public Double getSAU() {
    return SAU;
  }
  public void setDescTipoConduzione(String descTipoConduzione) {
    this.descTipoConduzione = descTipoConduzione;
  }
  public String getDescTipoConduzione() {
    return descTipoConduzione;
  }
  public void setDescComune(String descComune) {
    this.descComune = descComune;
  }
  public String getDescComune() {
    return descComune;
  }
  public void setIdTipoConduzione(Long idTipoConduzione) {
    this.idTipoConduzione = idTipoConduzione;
  }
  public Long getIdTipoConduzione() {
    return idTipoConduzione;
  }
  public void setIdComune(String idComune) {
    this.idComune = idComune;
  }
  public String getIdComune() {
    return idComune;
  }
  public void setDescProvincia(String descProvincia) {
    this.descProvincia = descProvincia;
  }
  public String getDescProvincia() {
    return descProvincia;
  }
  public void setDescUtilizzo(String descUtilizzo) {
    this.descUtilizzo = descUtilizzo;
  }
  public String getDescUtilizzo() {
    return descUtilizzo;
  }
  public void setIdTipoUtilizzo(Long idTipoUtilizzo) {
    this.idTipoUtilizzo = idTipoUtilizzo;
  }
  public Long getIdTipoUtilizzo() {
    return idTipoUtilizzo;
  }
}