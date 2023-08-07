package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe coi i campi per la visualizzazione 
 * dei riepiloghi della compensazione
 * 
 * @author TOBECONFIG
 *
 */
public class RiepilogoCompensazioneVO implements Serializable 
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -214221758861175132L;
  
  
  
  private BigDecimal supVitLavorazione;
  private BigDecimal supPastAllinea; 
  private String codDestProd;
  private String descDestProd;
  private String codVitigno;
  private String descVitigno;
  private String idoneita;
  private BigDecimal supVitata;
  private int vitignoParticolare;
  private Long idStoricoUnitaArborea;
  private Long idUnitaArborea;
  private BigDecimal supAreaDichiarata; 
  private BigDecimal sumDelta; 
  
  
  
  public BigDecimal getSupVitLavorazione()
  {
    return supVitLavorazione;
  }
  public void setSupVitLavorazione(BigDecimal supVitLavorazione)
  {
    this.supVitLavorazione = supVitLavorazione;
  }
  public BigDecimal getSupPastAllinea()
  {
    return supPastAllinea;
  }
  public void setSupPastAllinea(BigDecimal supPastAllinea)
  {
    this.supPastAllinea = supPastAllinea;
  }
  public String getCodDestProd()
  {
    return codDestProd;
  }
  public void setCodDestProd(String codDestProd)
  {
    this.codDestProd = codDestProd;
  }
  public String getDescDestProd()
  {
    return descDestProd;
  }
  public void setDescDestProd(String descDestProd)
  {
    this.descDestProd = descDestProd;
  }
  public String getCodVitigno()
  {
    return codVitigno;
  }
  public void setCodVitigno(String codVitigno)
  {
    this.codVitigno = codVitigno;
  }
  public String getDescVitigno()
  {
    return descVitigno;
  }
  public void setDescVitigno(String descVitigno)
  {
    this.descVitigno = descVitigno;
  }
  public String getIdoneita()
  {
    return idoneita;
  }
  public void setIdoneita(String idoneita)
  {
    this.idoneita = idoneita;
  }
  public BigDecimal getSupVitata()
  {
    return supVitata;
  }
  public void setSupVitata(BigDecimal supVitata)
  {
    this.supVitata = supVitata;
  }
  public int getVitignoParticolare()
  {
    return vitignoParticolare;
  }
  public void setVitignoParticolare(int vitignoParticolare)
  {
    this.vitignoParticolare = vitignoParticolare;
  }
  public Long getIdStoricoUnitaArborea()
  {
    return idStoricoUnitaArborea;
  }
  public void setIdStoricoUnitaArborea(Long idStoricoUnitaArborea)
  {
    this.idStoricoUnitaArborea = idStoricoUnitaArborea;
  }
  public BigDecimal getSupAreaDichiarata()
  {
    return supAreaDichiarata;
  }
  public void setSupAreaDichiarata(BigDecimal supAreaDichiarata)
  {
    this.supAreaDichiarata = supAreaDichiarata;
  }
  public BigDecimal getSumDelta()
  {
    return sumDelta;
  }
  public void setSumDelta(BigDecimal sumDelta)
  {
    this.sumDelta = sumDelta;
  }
  public Long getIdUnitaArborea()
  {
    return idUnitaArborea;
  }
  public void setIdUnitaArborea(Long idUnitaArborea)
  {
    this.idUnitaArborea = idUnitaArborea;
  }
  
    
  
	
}
