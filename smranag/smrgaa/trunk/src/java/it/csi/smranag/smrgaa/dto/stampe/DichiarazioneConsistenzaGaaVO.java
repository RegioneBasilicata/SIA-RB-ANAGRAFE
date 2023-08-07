package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;
import java.util.Date;

public class DichiarazioneConsistenzaGaaVO implements Serializable
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 752072680616953436L;
  
  
  
  
  private Date dataInserimentoDichiarazione;
  private String numeroProtocollo;
  private Date dataProtocollo;
  private Date data;
  private String motivo;
  private String tipoMotivo;
  private Long codiceFotografia;
  private String responsabile;
  private Long idUtente;
  private Long annoCampagna;
  private Long anno;
  private String flagInvioMail;
  
  
  
  public Long getIdUtente()
  {
    return idUtente;
  }
  public void setIdUtente(Long idUtente)
  {
    this.idUtente = idUtente;
  }
  public Long getCodiceFotografia()
  {
    return codiceFotografia;
  }
  public void setCodiceFotografia(Long codiceFotografia)
  {
    this.codiceFotografia = codiceFotografia;
  }
  public String getMotivo()
  {
    return motivo;
  }
  public void setMotivo(String motivo)
  {
    this.motivo = motivo;
  }
  public Date getDataInserimentoDichiarazione()
  {
    return dataInserimentoDichiarazione;
  }
  public void setDataInserimentoDichiarazione(Date dataInserimentoDichiarazione)
  {
    this.dataInserimentoDichiarazione = dataInserimentoDichiarazione;
  }
  public String getNumeroProtocollo()
  {
    return numeroProtocollo;
  }
  public void setNumeroProtocollo(String numeroProtocollo)
  {
    this.numeroProtocollo = numeroProtocollo;
  }
  public Date getDataProtocollo()
  {
    return dataProtocollo;
  }
  public void setDataProtocollo(Date dataProtocollo)
  {
    this.dataProtocollo = dataProtocollo;
  }
  public Date getData()
  {
    return data;
  }
  public void setData(Date data)
  {
    this.data = data;
  }
  public String getResponsabile()
  {
    return responsabile;
  }
  public void setResponsabile(String responsabile)
  {
    this.responsabile = responsabile;
  }
  public Long getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(Long annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
  public Long getAnno()
  {
    return anno;
  }
  public void setAnno(Long anno)
  {
    this.anno = anno;
  }
  public String getTipoMotivo()
  {
    return tipoMotivo;
  }
  public void setTipoMotivo(String tipoMotivo)
  {
    this.tipoMotivo = tipoMotivo;
  }
  public String getFlagInvioMail()
  {
    return flagInvioMail;
  }
  public void setFlagInvioMail(String flagInvioMail)
  {
    this.flagInvioMail = flagInvioMail;
  }
  
  
  
  
  

  
}
