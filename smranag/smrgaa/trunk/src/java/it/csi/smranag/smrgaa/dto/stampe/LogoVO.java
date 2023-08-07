package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class LogoVO implements Serializable
{
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5248662095377675720L;
  
  
  
  
  private ImageIcon       logoProvincia;
  private ImageIcon       logoRegione;
  
  
  
  public ImageIcon getLogoProvincia()
  {
    return logoProvincia;
  }
  public void setLogoProvincia(ImageIcon logoProvincia)
  {
    this.logoProvincia = logoProvincia;
  }
  public ImageIcon getLogoRegione()
  {
    return logoRegione;
  }
  public void setLogoRegione(ImageIcon logoRegione)
  {
    this.logoRegione = logoRegione;
  }
  
  
  
  
  
  
  
  

  
}
