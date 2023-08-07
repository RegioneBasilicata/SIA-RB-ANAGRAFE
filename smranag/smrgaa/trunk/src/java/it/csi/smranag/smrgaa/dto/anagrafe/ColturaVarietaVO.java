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

public class ColturaVarietaVO implements Serializable
{  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -3866042707355263620L;
  
  
  private String descUtilizzo;
  private String descVarieta;
  private BigDecimal superficieUtilizzo;
  
  
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
  public BigDecimal getSuperficieUtilizzo()
  {
    return superficieUtilizzo;
  }
  public void setSuperficieUtilizzo(BigDecimal superficieUtilizzo)
  {
    this.superficieUtilizzo = superficieUtilizzo;
  }
  
  
  
  
  
  
  
  
  
}
