package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;

public class TipoFirmatarioVO implements Serializable
{
  
   
  /**
   * 
   */
  private static final long serialVersionUID = 2812397319577275499L;
  
  
  private int               idTipoFirmatario;
  private String            codice;
  private String            descrizione;
  private String            flagCaa;
  
  
  
  
  public int getIdTipoFirmatario()
  {
    return idTipoFirmatario;
  }
  public void setIdTipoFirmatario(int idTipoFirmatario)
  {
    this.idTipoFirmatario = idTipoFirmatario;
  }
  public String getCodice()
  {
    return codice;
  }
  public void setCodice(String codice)
  {
    this.codice = codice;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagCaa()
  {
    return flagCaa;
  }
  public void setFlagCaa(String flagCaa)
  {
    this.flagCaa = flagCaa;
  }
 
  
  
  
  
 
}
