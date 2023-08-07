package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Vector;

public class TerreniVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -3301940347365144793L;

  private it.csi.solmr.dto.CodeDescription unitaProduttiva = null;
  private Long annoRilevamento = null;
  @SuppressWarnings("unchecked")
  private Vector elencoTerreni = null;
  private Double sommaSAU;
  private Double sommaSuperficieTotale;

  public Double getSommaSuperficieTotale() {
    return sommaSuperficieTotale;
  }
  public void setSommaSuperficieTotale(Double sommaSuperficieTotale) {
    this.sommaSuperficieTotale = sommaSuperficieTotale;
  }
  public void setSommaSAU(Double sommaSAU) {
    this.sommaSAU = sommaSAU;
  }
  public Double getSommaSAU() {
    return sommaSAU;
  }
  public void setUnitaProduttiva(it.csi.solmr.dto.CodeDescription unitaProduttiva) {
    this.unitaProduttiva = unitaProduttiva;
  }
  public it.csi.solmr.dto.CodeDescription getUnitaProduttiva() {
    return unitaProduttiva;
  }
  public void setAnnoRilevamento(Long annoRilevamento) {
    this.annoRilevamento = annoRilevamento;
  }
  public Long getAnnoRilevamento() {
    return annoRilevamento;
  }
  @SuppressWarnings("unchecked")
  public void setElencoTerreni(Vector elencoTerreni) {
    this.elencoTerreni = elencoTerreni;
  }
  @SuppressWarnings("unchecked")
  public Vector getElencoTerreni() {
    return elencoTerreni;
  }
}