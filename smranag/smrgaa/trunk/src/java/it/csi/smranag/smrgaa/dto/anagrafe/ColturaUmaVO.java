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

public class ColturaUmaVO implements Serializable
{  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5251357431784527109L;
  
  
  private String colturaUma;
  private BigDecimal superficieUma;
  
  
  public String getColturaUma()
  {
    return colturaUma;
  }
  public void setColturaUma(String colturaUma)
  {
    this.colturaUma = colturaUma;
  }
  public BigDecimal getSuperficieUma()
  {
    return superficieUma;
  }
  public void setSuperficieUma(BigDecimal superficieUma)
  {
    this.superficieUma = superficieUma;
  }
  
  
  
  
  
  
}
