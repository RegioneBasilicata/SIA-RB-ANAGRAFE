package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * istanza riesame
 * 
 * @author TOBECONFIG
 *
 */
public class IstanzaRiesameAziendaVO implements Serializable 
{
  
  
  
 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4432455274180093364L;
  
  
  
  private Long idIstanzaRiesameAzienda;
  private String protocolloIstanza;
  private Long idAzienda;
  private Integer annoIstanza;
  
  
  public Long getIdIstanzaRiesameAzienda()
  {
    return idIstanzaRiesameAzienda;
  }
  public void setIdIstanzaRiesameAzienda(Long idIstanzaRiesameAzienda)
  {
    this.idIstanzaRiesameAzienda = idIstanzaRiesameAzienda;
  }
  public String getProtocolloIstanza()
  {
    return protocolloIstanza;
  }
  public void setProtocolloIstanza(String protocolloIstanza)
  {
    this.protocolloIstanza = protocolloIstanza;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public Integer getAnnoIstanza()
  {
    return annoIstanza;
  }
  public void setAnnoIstanza(Integer annoIstanza)
  {
    this.annoIstanza = annoIstanza;
  }
  
  
  
  
	
}
