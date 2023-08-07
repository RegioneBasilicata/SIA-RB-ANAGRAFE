package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

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

public class ParticellaAziendaNuovaVO implements Serializable
{  
 
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -619091755075779708L;
  
  
  
  private Long idUtilizzoAziendaNuova;
  private Long idUteAziendaNuova;
  private String denominazUte;
  private String istatComune;
  private String desCom;
  private String istatProvincia;
  private String sglProv;
  private String sezione;
  private Long foglio;
  private String strFoglio;
  private Long particella;
  private String strParticella;
  private BigDecimal superficie;
  private String strSuperficie;
  private Long idIndirizzoUtilizzo;
  private String descIndirizzoUtilizzo;
  private Long idUtilizzo;
  private String descTipoUtilizzo;
  private Long idVarieta;
  private String descTipoVarieta;
  private Integer idTitoloPossesso;
  private String descTitoloPossesso;
  private BigDecimal percentualePossesso;
  private String strPercentualePossesso;
  private Integer idUnitaMisura;
  private String descUnitaMisura;
  
  
  public Long getIdUtilizzoAziendaNuova()
  {
    return idUtilizzoAziendaNuova;
  }
  public void setIdUtilizzoAziendaNuova(Long idUtilizzoAziendaNuova)
  {
    this.idUtilizzoAziendaNuova = idUtilizzoAziendaNuova;
  }
  public Long getIdUteAziendaNuova()
  {
    return idUteAziendaNuova;
  }
  public void setIdUteAziendaNuova(Long idUteAziendaNuova)
  {
    this.idUteAziendaNuova = idUteAziendaNuova;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getDesCom()
  {
    return desCom;
  }
  public void setDesCom(String desCom)
  {
    this.desCom = desCom;
  }
  public String getIstatProvincia()
  {
    return istatProvincia;
  }
  public void setIstatProvincia(String istatProvincia)
  {
    this.istatProvincia = istatProvincia;
  }
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public Long getFoglio()
  {
    return foglio;
  }
  public void setFoglio(Long foglio)
  {
    this.foglio = foglio;
  }
  public String getStrFoglio()
  {
    return strFoglio;
  }
  public void setStrFoglio(String strFoglio)
  {
    this.strFoglio = strFoglio;
  }
  public Long getParticella()
  {
    return particella;
  }
  public void setParticella(Long particella)
  {
    this.particella = particella;
  }
  public String getStrParticella()
  {
    return strParticella;
  }
  public void setStrParticella(String strParticella)
  {
    this.strParticella = strParticella;
  }
  public BigDecimal getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }
  public String getStrSuperficie()
  {
    return strSuperficie;
  }
  public void setStrSuperficie(String strSuperficie)
  {
    this.strSuperficie = strSuperficie;
  }
  public Long getIdUtilizzo()
  {
    return idUtilizzo;
  }
  public void setIdUtilizzo(Long idUtilizzo)
  {
    this.idUtilizzo = idUtilizzo;
  }
  public String getDescTipoUtilizzo()
  {
    return descTipoUtilizzo;
  }
  public void setDescTipoUtilizzo(String descTipoUtilizzo)
  {
    this.descTipoUtilizzo = descTipoUtilizzo;
  }
  public Long getIdVarieta()
  {
    return idVarieta;
  }
  public void setIdVarieta(Long idVarieta)
  {
    this.idVarieta = idVarieta;
  }
  public String getDescTipoVarieta()
  {
    return descTipoVarieta;
  }
  public void setDescTipoVarieta(String descTipoVarieta)
  {
    this.descTipoVarieta = descTipoVarieta;
  }
  public Integer getIdTitoloPossesso()
  {
    return idTitoloPossesso;
  }
  public void setIdTitoloPossesso(Integer idTitoloPossesso)
  {
    this.idTitoloPossesso = idTitoloPossesso;
  }
  public String getDescTitoloPossesso()
  {
    return descTitoloPossesso;
  }
  public void setDescTitoloPossesso(String descTitoloPossesso)
  {
    this.descTitoloPossesso = descTitoloPossesso;
  }
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }
  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
  public String getStrPercentualePossesso()
  {
    return strPercentualePossesso;
  }
  public void setStrPercentualePossesso(String strPercentualePossesso)
  {
    this.strPercentualePossesso = strPercentualePossesso;
  }
  public Integer getIdUnitaMisura()
  {
    return idUnitaMisura;
  }
  public void setIdUnitaMisura(Integer idUnitaMisura)
  {
    this.idUnitaMisura = idUnitaMisura;
  }
  public String getDescUnitaMisura()
  {
    return descUnitaMisura;
  }
  public void setDescUnitaMisura(String descUnitaMisura)
  {
    this.descUnitaMisura = descUnitaMisura;
  }
  public String getDenominazUte()
  {
    return denominazUte;
  }
  public void setDenominazUte(String denominazUte)
  {
    this.denominazUte = denominazUte;
  }
  public Long getIdIndirizzoUtilizzo()
  {
    return idIndirizzoUtilizzo;
  }
  public void setIdIndirizzoUtilizzo(Long idIndirizzoUtilizzo)
  {
    this.idIndirizzoUtilizzo = idIndirizzoUtilizzo;
  }
  public String getDescIndirizzoUtilizzo()
  {
    return descIndirizzoUtilizzo;
  }
  public void setDescIndirizzoUtilizzo(String descIndirizzoUtilizzo)
  {
    this.descIndirizzoUtilizzo = descIndirizzoUtilizzo;
  }
  
  
  
  
  
  
}
