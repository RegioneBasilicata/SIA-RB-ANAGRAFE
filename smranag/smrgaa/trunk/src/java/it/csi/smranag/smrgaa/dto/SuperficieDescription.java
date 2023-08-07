package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object per contenere coppie di superficie/descrizione May 2, 2011
 * 
 * @author TOBECONFIG (Matr. 71646)
 */
public class SuperficieDescription implements Serializable 
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7773586313498403457L;
  
  
  
  
  private BigDecimal superficie;  
  private String descrizione;
  
  
  
  
  
  public BigDecimal getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  
}
