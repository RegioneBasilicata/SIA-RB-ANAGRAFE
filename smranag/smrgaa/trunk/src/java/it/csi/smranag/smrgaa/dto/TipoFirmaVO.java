package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;



public class TipoFirmaVO  implements Serializable
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -5696421304595417894L;
  
  
  
  private long idTipoFirma;
  private String codiceTipoFirma;
  private String descrizioneTipoFirma;
  private String stileFirma;
  private String flagFirmaDoquiAgri;
  
  
  
  
  public long getIdTipoFirma()
  {
    return idTipoFirma;
  }
  public void setIdTipoFirma(long idTipoFirma)
  {
    this.idTipoFirma = idTipoFirma;
  }
  public String getCodiceTipoFirma()
  {
    return codiceTipoFirma;
  }
  public void setCodiceTipoFirma(String codiceTipoFirma)
  {
    this.codiceTipoFirma = codiceTipoFirma;
  }
  public String getDescrizioneTipoFirma()
  {
    return descrizioneTipoFirma;
  }
  public void setDescrizioneTipoFirma(String descrizioneTipoFirma)
  {
    this.descrizioneTipoFirma = descrizioneTipoFirma;
  }
  public String getStileFirma()
  {
    return stileFirma;
  }
  public void setStileFirma(String stileFirma)
  {
    this.stileFirma = stileFirma;
  }
  public String getFlagFirmaDoquiAgri()
  {
    return flagFirmaDoquiAgri;
  }
  public void setFlagFirmaDoquiAgri(String flagFirmaDoquiAgri)
  {
    this.flagFirmaDoquiAgri = flagFirmaDoquiAgri;
  }
  
  
  
  
  

}