package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe coi i campi per la visualizzazione 
 * delle uv per l'excel compensazione
 * 
 * @author TOBECONFIG
 *
 */
public class DirittoCompensazioneVO implements Serializable 
{
	
    
  /**
   * 
   */
  private static final long serialVersionUID = -5625751381913590673L;
  
  
  
  private String descVarieta;
  private Long idVarieta;
  private BigDecimal supDirittiComp;
  private String descTipologiaVino;
  private Long idTipologiaVino;
  private BigDecimal supDirittiIpo;
  private BigDecimal supVitiEstirpoVar;
  private BigDecimal supVitiEstirpoVarblocAz;
  private String flagVitignoParticolare;
  private BigDecimal supVitiEstirpoAz; 
  private BigDecimal supUvDuplicate;
  private BigDecimal supDirittiCondivisi;
  private String dirittiCondivTempo;
  private BigDecimal supPostCons;
  private BigDecimal supFinaleAssegnata;
  
  
  
  public String getDescVarieta()
  {
    return descVarieta;
  }
  public void setDescVarieta(String descVarieta)
  {
    this.descVarieta = descVarieta;
  }
  public Long getIdVarieta()
  {
    return idVarieta;
  }
  public void setIdVarieta(Long idVarieta)
  {
    this.idVarieta = idVarieta;
  }
  public String getDescTipologiaVino()
  {
    return descTipologiaVino;
  }
  public void setDescTipologiaVino(String descTipologiaVino)
  {
    this.descTipologiaVino = descTipologiaVino;
  }
  public Long getIdTipologiaVino()
  {
    return idTipologiaVino;
  }
  public void setIdTipologiaVino(Long idTipologiaVino)
  {
    this.idTipologiaVino = idTipologiaVino;
  }
  public String getFlagVitignoParticolare()
  {
    return flagVitignoParticolare;
  }
  public void setFlagVitignoParticolare(String flagVitignoParticolare)
  {
    this.flagVitignoParticolare = flagVitignoParticolare;
  }
  public BigDecimal getSupUvDuplicate()
  {
    return supUvDuplicate;
  }
  public void setSupUvDuplicate(BigDecimal supUvDuplicate)
  {
    this.supUvDuplicate = supUvDuplicate;
  }
  public BigDecimal getSupDirittiCondivisi()
  {
    return supDirittiCondivisi;
  }
  public void setSupDirittiCondivisi(BigDecimal supDirittiCondivisi)
  {
    this.supDirittiCondivisi = supDirittiCondivisi;
  }
  public String getDirittiCondivTempo()
  {
    return dirittiCondivTempo;
  }
  public void setDirittiCondivTempo(String dirittiCondivTempo)
  {
    this.dirittiCondivTempo = dirittiCondivTempo;
  }
  public BigDecimal getSupPostCons()
  {
    return supPostCons;
  }
  public void setSupPostCons(BigDecimal supPostCons)
  {
    this.supPostCons = supPostCons;
  }
  public BigDecimal getSupFinaleAssegnata()
  {
    return supFinaleAssegnata;
  }
  public void setSupFinaleAssegnata(BigDecimal supFinaleAssegnata)
  {
    this.supFinaleAssegnata = supFinaleAssegnata;
  }
  public BigDecimal getSupDirittiIpo()
  {
    return supDirittiIpo;
  }
  public void setSupDirittiIpo(BigDecimal supDirittiIpo)
  {
    this.supDirittiIpo = supDirittiIpo;
  }
  public BigDecimal getSupDirittiComp()
  {
    return supDirittiComp;
  }
  public void setSupDirittiComp(BigDecimal supDirittiComp)
  {
    this.supDirittiComp = supDirittiComp;
  }
  public BigDecimal getSupVitiEstirpoVar()
  {
    return supVitiEstirpoVar;
  }
  public void setSupVitiEstirpoVar(BigDecimal supVitiEstirpoVar)
  {
    this.supVitiEstirpoVar = supVitiEstirpoVar;
  }
  public BigDecimal getSupVitiEstirpoVarblocAz()
  {
    return supVitiEstirpoVarblocAz;
  }
  public void setSupVitiEstirpoVarblocAz(BigDecimal supVitiEstirpoVarblocAz)
  {
    this.supVitiEstirpoVarblocAz = supVitiEstirpoVarblocAz;
  }
  public BigDecimal getSupVitiEstirpoAz()
  {
    return supVitiEstirpoAz;
  }
  public void setSupVitiEstirpoAz(BigDecimal supVitiEstirpoAz)
  {
    this.supVitiEstirpoAz = supVitiEstirpoAz;
  }
  
  
  
  
}
