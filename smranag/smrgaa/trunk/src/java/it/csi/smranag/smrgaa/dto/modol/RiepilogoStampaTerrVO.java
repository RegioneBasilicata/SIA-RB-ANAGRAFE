package it.csi.smranag.smrgaa.dto.modol;

import java.io.Serializable;
import java.math.BigDecimal;

public class RiepilogoStampaTerrVO implements Serializable
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4262471332568529985L;
  
  
  
  private String descrizione; 
  private BigDecimal valore1;
  private BigDecimal valore2;
  
  
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public BigDecimal getValore1()
  {
    return valore1;
  }
  public void setValore1(BigDecimal valore1)
  {
    this.valore1 = valore1;
  }
  public BigDecimal getValore2()
  {
    return valore2;
  }
  public void setValore2(BigDecimal valore2)
  {
    this.valore2 = valore2;
  }
  
  
  
}
