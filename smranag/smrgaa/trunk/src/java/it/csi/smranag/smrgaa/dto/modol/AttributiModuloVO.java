package it.csi.smranag.smrgaa.dto.modol;

public class AttributiModuloVO
{
  private String rifAdobe; 
  private String codiceModello; 
  private String codiceModulo; 
  private String codiceApplicazione; 
  private String descrizioneApplicazione;
   
  public AttributiModuloVO( String codiceApplicazione,
                          String descrizioneApplicazione,
                          String codiceModulo,
                          String codiceModello,
                          String rifAdobe)
  {
    this.rifAdobe = rifAdobe; 
    this.codiceModello = codiceModello; 
    this.codiceModulo = codiceModulo; 
    this.codiceApplicazione = codiceApplicazione; 
    this.descrizioneApplicazione = descrizioneApplicazione; 
  }
  
  public String getRifAdobe()
  {
    return rifAdobe; 
  }  
  public void setRifAdobe(String rifAdobe)
  {
    this.rifAdobe = rifAdobe; 
  }  
  
  public String getCodiceModello()
  {
    return codiceModello; 
  }  
  public void setCodiceModello(String codiceModello)
  {
    this.codiceModello = codiceModello; 
  }  
  
  public String getCodiceModulo()
  {
    return codiceModulo; 
  }  
  public void setCodiceModulo(String codiceModulo)
  {
    this.codiceModulo = codiceModulo; 
  }  
  
  public String getCodiceApplicazione()
  {
    return codiceApplicazione; 
  }  
  public void setCodiceApplicazione(String codiceApplicazione)
  {
    this.codiceApplicazione = codiceApplicazione; 
  }  
  
  public String getDescrizioneApplicazione()
  {
    return descrizioneApplicazione; 
  }  
  public void setDescrizioneApplicazione(String descrizioneApplicazione)
  {
    this.descrizioneApplicazione = descrizioneApplicazione; 
  }  
  
}
