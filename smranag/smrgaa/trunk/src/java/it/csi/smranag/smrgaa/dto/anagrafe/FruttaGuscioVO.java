package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */

public class FruttaGuscioVO implements Serializable
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 6246713031130587154L;
  
  private String sezione;
  private String descComune;
  private String sglProvincia;
  private String foglio;
  private String particella;
  private String subalterno;
  private String codUtilizzo;
  private String descUtilizzo;
  private String descVarieta;
  private Integer annImpianto;
  private Long sestoTraFile;
  private Long sestoSuFile;
  private Long nPiante;
  private BigDecimal supGrafica;
  private BigDecimal supCondotta;
  private BigDecimal supUtilizzo;
  
  private Long idParticella;
  private Long idConduzione;
  
  
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSglProvincia()
  {
    return sglProvincia;
  }
  public void setSglProvincia(String sglProvincia)
  {
    this.sglProvincia = sglProvincia;
  }
  public String getFoglio()
  {
    return foglio;
  }
  public void setFoglio(String foglio)
  {
    this.foglio = foglio;
  }
  public String getParticella()
  {
    return particella;
  }
  public void setParticella(String particella)
  {
    this.particella = particella;
  }
  public String getSubalterno()
  {
    return subalterno;
  }
  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }
  public String getCodUtilizzo()
  {
    return codUtilizzo;
  }
  public void setCodUtilizzo(String codUtilizzo)
  {
    this.codUtilizzo = codUtilizzo;
  }
  public String getDescUtilizzo()
  {
    return descUtilizzo;
  }
  public void setDescUtilizzo(String descUtilizzo)
  {
    this.descUtilizzo = descUtilizzo;
  }
  public String getDescVarieta()
  {
    return descVarieta;
  }
  public void setDescVarieta(String descVarieta)
  {
    this.descVarieta = descVarieta;
  }
  public Integer getAnnImpianto()
  {
    return annImpianto;
  }
  public void setAnnImpianto(Integer annImpianto)
  {
    this.annImpianto = annImpianto;
  }
  public Long getSestoTraFile()
  {
    return sestoTraFile;
  }
  public void setSestoTraFile(Long sestoTraFile)
  {
    this.sestoTraFile = sestoTraFile;
  }
  public Long getSestoSuFile()
  {
    return sestoSuFile;
  }
  public void setSestoSuFile(Long sestoSuFile)
  {
    this.sestoSuFile = sestoSuFile;
  }
  public Long getnPiante()
  {
    return nPiante;
  }
  public void setnPiante(Long nPiante)
  {
    this.nPiante = nPiante;
  }
  public BigDecimal getSupGrafica()
  {
    return supGrafica;
  }
  public void setSupGrafica(BigDecimal supGrafica)
  {
    this.supGrafica = supGrafica;
  }
  public BigDecimal getSupCondotta()
  {
    return supCondotta;
  }
  public void setSupCondotta(BigDecimal supCondotta)
  {
    this.supCondotta = supCondotta;
  }
  public BigDecimal getSupUtilizzo()
  {
    return supUtilizzo;
  }
  public void setSupUtilizzo(BigDecimal supUtilizzo)
  {
    this.supUtilizzo = supUtilizzo;
  }
  public Long getIdParticella()
  {
    return idParticella;
  }
  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }
  public Long getIdConduzione()
  {
    return idConduzione;
  }
  public void setIdConduzione(Long idConduzione)
  {
    this.idConduzione = idConduzione;
  }
  
  
  
  
}
