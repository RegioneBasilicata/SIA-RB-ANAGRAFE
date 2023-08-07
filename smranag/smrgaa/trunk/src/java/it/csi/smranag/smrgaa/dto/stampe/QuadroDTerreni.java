package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale Questo VO viene utilizzato
 * esclusivamente dalle stampe
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
public class QuadroDTerreni implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 6170299140276537744L;

  private String            comune;
  private String            siglaProv;
  private String            foglio;
  private BigDecimal        supAssVul;
  private BigDecimal        supCondVul;
  private BigDecimal        supAssNonVul;
  private BigDecimal        supCondNonVul;
  private String            comuneUte;
  private String            siglaProvUte;
  private String            indirizzoUte;

  public String getComune()
  {
    return comune;
  }

  public void setComune(String comune)
  {
    this.comune = comune;
  }

  public String getSiglaProv()
  {
    return siglaProv;
  }

  public void setSiglaProv(String siglaProv)
  {
    this.siglaProv = siglaProv;
  }

  public BigDecimal getSupAssVul()
  {
    return supAssVul;
  }

  public void setSupAssVul(BigDecimal supAssVul)
  {
    this.supAssVul = supAssVul;
  }

  public BigDecimal getSupCondVul()
  {
    return supCondVul;
  }

  public void setSupCondVul(BigDecimal supCondVul)
  {
    this.supCondVul = supCondVul;
  }

  public BigDecimal getSupAssNonVul()
  {
    return supAssNonVul;
  }

  public void setSupAssNonVul(BigDecimal supAssNonVul)
  {
    this.supAssNonVul = supAssNonVul;
  }

  public BigDecimal getSupCondNonVul()
  {
    return supCondNonVul;
  }

  public void setSupCondNonVul(BigDecimal supCondNonVul)
  {
    this.supCondNonVul = supCondNonVul;
  }

  public String getFoglio()
  {
    return foglio;
  }

  public void setFoglio(String foglio)
  {
    this.foglio = foglio;
  }

  public String getComuneUte()
  {
    return comuneUte;
  }

  public void setComuneUte(String comuneUte)
  {
    this.comuneUte = comuneUte;
  }

  public String getSiglaProvUte()
  {
    return siglaProvUte;
  }

  public void setSiglaProvUte(String siglaProvUte)
  {
    this.siglaProvUte = siglaProvUte;
  }

  public String getIndirizzoUte()
  {
    return indirizzoUte;
  }

  public void setIndirizzoUte(String indirizzoUte)
  {
    this.indirizzoUte = indirizzoUte;
  }
  
  

}
