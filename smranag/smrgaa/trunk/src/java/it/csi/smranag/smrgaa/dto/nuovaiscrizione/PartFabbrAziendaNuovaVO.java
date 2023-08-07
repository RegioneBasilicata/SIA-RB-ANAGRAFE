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

public class PartFabbrAziendaNuovaVO implements Serializable
{  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -3250339691915831396L;
  
  
  
  private Long idPartFabbrAziendaNuova;
  private Long idFabbricatoAziendaNuova;
  private String istatComune;
  private String desCom;
  private String istatProvincia;
  private String sglProv;
  private String sezione;
  private Long foglio;
  private String strFoglio;
  private Long particella;
  private String strParticella;
  private String subalterno;
  private BigDecimal superficie;
  private String strSuperficie;
  private Long idUtilizzo;
  private String descTipoUtilizzo;
  private Long idVarieta;
  private String descTipoVarieta;
  private Integer idTitoloPossesso;
  private String descTitoloPossesso;
  
  
  
  
  public Long getIdPartFabbrAziendaNuova()
  {
    return idPartFabbrAziendaNuova;
  }
  public void setIdPartFabbrAziendaNuova(Long idPartFabbrAziendaNuova)
  {
    this.idPartFabbrAziendaNuova = idPartFabbrAziendaNuova;
  }
  public Long getIdFabbricatoAziendaNuova()
  {
    return idFabbricatoAziendaNuova;
  }
  public void setIdFabbricatoAziendaNuova(Long idFabbricatoAziendaNuova)
  {
    this.idFabbricatoAziendaNuova = idFabbricatoAziendaNuova;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
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
  public Long getParticella()
  {
    return particella;
  }
  public void setParticella(Long particella)
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
  public BigDecimal getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }
  public Long getIdUtilizzo()
  {
    return idUtilizzo;
  }
  public void setIdUtilizzo(Long idUtilizzo)
  {
    this.idUtilizzo = idUtilizzo;
  }
  public Long getIdVarieta()
  {
    return idVarieta;
  }
  public void setIdVarieta(Long idVarieta)
  {
    this.idVarieta = idVarieta;
  }
  public Integer getIdTitoloPossesso()
  {
    return idTitoloPossesso;
  }
  public void setIdTitoloPossesso(Integer idTitoloPossesso)
  {
    this.idTitoloPossesso = idTitoloPossesso;
  }
  public String getDesCom()
  {
    return desCom;
  }
  public void setDesCom(String desCom)
  {
    this.desCom = desCom;
  }
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  public String getDescTipoUtilizzo()
  {
    return descTipoUtilizzo;
  }
  public void setDescTipoUtilizzo(String descTipoUtilizzo)
  {
    this.descTipoUtilizzo = descTipoUtilizzo;
  }
  public String getDescTipoVarieta()
  {
    return descTipoVarieta;
  }
  public void setDescTipoVarieta(String descTipoVarieta)
  {
    this.descTipoVarieta = descTipoVarieta;
  }
  public String getDescTitoloPossesso()
  {
    return descTitoloPossesso;
  }
  public void setDescTitoloPossesso(String descTitoloPossesso)
  {
    this.descTitoloPossesso = descTitoloPossesso;
  }
  public String getIstatProvincia()
  {
    return istatProvincia;
  }
  public void setIstatProvincia(String istatProvincia)
  {
    this.istatProvincia = istatProvincia;
  }
  public String getStrFoglio()
  {
    return strFoglio;
  }
  public void setStrFoglio(String strFoglio)
  {
    this.strFoglio = strFoglio;
  }
  public String getStrParticella()
  {
    return strParticella;
  }
  public void setStrParticella(String strParticella)
  {
    this.strParticella = strParticella;
  }
  public String getStrSuperficie()
  {
    return strSuperficie;
  }
  public void setStrSuperficie(String strSuperficie)
  {
    this.strSuperficie = strSuperficie;
  }
  
  
  
  
  
  
}
