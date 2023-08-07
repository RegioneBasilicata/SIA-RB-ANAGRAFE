package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ParticellaDettaglioValidazioniVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -2027760648270574752L;
  private long              idParticella;
  private long              idDichiarazioneConsistenza;
  private String            cuaa;
  private String            denominazione;
  private String            validazione;
  private long              idTitoloPossesso;
  private String            codiceMacroUso;
  private String            descrizioneMacroUso;
  private BigDecimal        superficieUtilizzata;
  private String            codiceUtilizzo;
  private String            descrizioneUtilizzo;
  private String            descrizioneTitoloPossesso;
  private String            esitoControllo;
  private BigDecimal        superficieCondotta;
  private long              idConduzioneDichiarata;
  private long              idAzienda;
  private BigDecimal        supCatastale;
  private BigDecimal        percentualePossesso;
  
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }

  public BigDecimal getSupCatastale()
  {
    return supCatastale;
  }

  public void setSupCatastale(BigDecimal supCatastale)
  {
    this.supCatastale = supCatastale;
  }

  public long getIdConduzioneDichiarata()
  {
    return idConduzioneDichiarata;
  }

  public void setIdConduzioneDichiarata(long idConduzioneDichiarata)
  {
    this.idConduzioneDichiarata = idConduzioneDichiarata;
  }

  public long getIdParticella()
  {
    return idParticella;
  }

  public void setIdParticella(long idParticella)
  {
    this.idParticella = idParticella;
  }

  public long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }

  public void setIdDichiarazioneConsistenza(long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public String getValidazione()
  {
    return validazione;
  }

  public void setValidazione(String validazione)
  {
    this.validazione = validazione;
  }

  public long getIdTitoloPossesso()
  {
    return idTitoloPossesso;
  }

  public void setIdTitoloPossesso(long idTitoloPossesso)
  {
    this.idTitoloPossesso = idTitoloPossesso;
  }

  public String getCodiceMacroUso()
  {
    return codiceMacroUso;
  }

  public void setCodiceMacroUso(String codiceMacroUso)
  {
    this.codiceMacroUso = codiceMacroUso;
  }

  public String getDescrizioneMacroUso()
  {
    return descrizioneMacroUso;
  }

  public void setDescrizioneMacroUso(String descrizioneMacroUso)
  {
    this.descrizioneMacroUso = descrizioneMacroUso;
  }

  public BigDecimal getSuperficieUtilizzata()
  {
    return superficieUtilizzata;
  }

  public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
  {
    this.superficieUtilizzata = superficieUtilizzata;
  }

  public String getCodiceUtilizzo()
  {
    return codiceUtilizzo;
  }

  public void setCodiceUtilizzo(String codiceUtilizzo)
  {
    this.codiceUtilizzo = codiceUtilizzo;
  }

  public String getDescrizioneUtilizzo()
  {
    return descrizioneUtilizzo;
  }

  public void setDescrizioneUtilizzo(String descrizioneUtilizzo)
  {
    this.descrizioneUtilizzo = descrizioneUtilizzo;
  }

  public String getDescrizioneTitoloPossesso()
  {
    return descrizioneTitoloPossesso;
  }

  public void setDescrizioneTitoloPossesso(String descrizioneTitoloPossesso)
  {
    this.descrizioneTitoloPossesso = descrizioneTitoloPossesso;
  }

  public String getEsitoControllo()
  {
    return esitoControllo;
  }

  public void setEsitoControllo(String esitoControllo)
  {
    this.esitoControllo = esitoControllo;
  }

  public BigDecimal getSuperficieCondotta()
  {
    return superficieCondotta;
  }

  public void setSuperficieCondotta(BigDecimal superficieCondotta)
  {
    this.superficieCondotta = superficieCondotta;
  }

  public long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
}
