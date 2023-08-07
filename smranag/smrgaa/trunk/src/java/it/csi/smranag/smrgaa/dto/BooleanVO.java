package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;

/**
 * Value Object per contenere coppie di id/descrizione Oct 22, 2008
 * 
 * @author TOBECONFIG
 */
public class BooleanVO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -5497139146216751674L;
  
  
  private boolean valore;


  public boolean isValore()
  {
    return valore;
  }


  public void setValore(boolean valore)
  {
    this.valore = valore;
  }
  
  
  
}
