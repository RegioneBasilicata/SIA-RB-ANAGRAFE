package it.csi.solmr.dto.anag;

import java.io.*;

public class ElencoAziendeParticellaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -4748997192492913144L;

  private String denominazione = null;
  private String cuaa = null;
  private Long idTitoloPossesso = null;
  private String descrizioneTitoloPossesso = null;
  private String superficieCondotta = null;

  public ElencoAziendeParticellaVO() {
  }



  public int hashCode() {
    return (denominazione == null ? 0 : denominazione.hashCode())+
            (cuaa == null ? 0 : cuaa.hashCode())+
            (descrizioneTitoloPossesso == null ? 0 : descrizioneTitoloPossesso.hashCode())+
            (superficieCondotta == null ? 0 : superficieCondotta.hashCode())+
            (idTitoloPossesso == null ? 0 : idTitoloPossesso.hashCode());
  }

  public boolean equals(Object o) {
    if (o instanceof ElencoAziendeParticellaVO) {
      ElencoAziendeParticellaVO other = (ElencoAziendeParticellaVO)o;
      return (this.denominazione == null && other.denominazione == null || this.denominazione.equals(other.denominazione) &&
              this.cuaa == null && other.cuaa == null || this.cuaa.equals(other.cuaa) &&
              this.descrizioneTitoloPossesso == null && other.descrizioneTitoloPossesso == null || this.descrizioneTitoloPossesso.equals(other.descrizioneTitoloPossesso) &&
              this.superficieCondotta == null && other.superficieCondotta == null || this.superficieCondotta.equals(other.superficieCondotta) &&
              this.idTitoloPossesso == null && other.idTitoloPossesso == null || this.idTitoloPossesso.equals(other.idTitoloPossesso));

    } else
      return false;
  }

  public String getCuaa() {
    return cuaa;
  }
  public void setCuaa(String cuaa) {
    this.cuaa = cuaa;
  }
  public String getDenominazione() {
    return denominazione;
  }
  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }
  public String getDescrizioneTitoloPossesso() {
    return descrizioneTitoloPossesso;
  }
  public void setDescrizioneTitoloPossesso(String descrizioneTitoloPossesso) {
    this.descrizioneTitoloPossesso = descrizioneTitoloPossesso;
  }
  public String getSuperficieCondotta() {
    return superficieCondotta;
  }
  public void setSuperficieCondotta(String superficieCondotta) {
    this.superficieCondotta = superficieCondotta;
  }
  public Long getIdTitoloPossesso() {
    return idTitoloPossesso;
  }
  public void setIdTitoloPossesso(Long idTitoloPossesso) {
    this.idTitoloPossesso = idTitoloPossesso;
  }
}