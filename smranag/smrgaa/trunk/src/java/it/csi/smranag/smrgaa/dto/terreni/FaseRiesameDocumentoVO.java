package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.util.Date;


public class FaseRiesameDocumentoVO implements Serializable 
{
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6700303803439796683L;
  
  
  
  private Long idFaseRiesameTpDocumento;
  private Integer idFaseIstanzaRiesame;
  private Long idDocumento;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private String extraSistema;
  
  
  public Long getIdFaseRiesameTpDocumento()
  {
    return idFaseRiesameTpDocumento;
  }
  public void setIdFaseRiesameTpDocumento(Long idFaseRiesameTpDocumento)
  {
    this.idFaseRiesameTpDocumento = idFaseRiesameTpDocumento;
  }
  public Integer getIdFaseIstanzaRiesame()
  {
    return idFaseIstanzaRiesame;
  }
  public void setIdFaseIstanzaRiesame(Integer idFaseIstanzaRiesame)
  {
    this.idFaseIstanzaRiesame = idFaseIstanzaRiesame;
  }
  public Long getIdDocumento()
  {
    return idDocumento;
  }
  public void setIdDocumento(Long idDocumento)
  {
    this.idDocumento = idDocumento;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  public String getExtraSistema()
  {
    return extraSistema;
  }
  public void setExtraSistema(String extraSistema)
  {
    this.extraSistema = extraSistema;
  }
  
  
	
}
