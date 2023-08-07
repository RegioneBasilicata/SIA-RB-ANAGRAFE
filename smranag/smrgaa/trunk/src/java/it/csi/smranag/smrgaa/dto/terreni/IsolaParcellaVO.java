package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati isola parcella
 * 
 * @author TOBECONFIG
 *
 */
public class IsolaParcellaVO implements Serializable 
{
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -2006957513373487130L;
  
  
  
  
  
  private Long idIsolaParcella;
  private Long idIlo;
  private BigDecimal supVitata;
  private BigDecimal supParcella;
  private Integer numeroUV;
  private Integer tolleranza;
  private Long idParticella;
  private Long idDichiarazioneConsistenza;
  private Long idUnarParcella;
  private Date dataAggiornamntoUV;
  private String parcellaSelezionata;
  
  
  
  public String getParcellaSelezionata()
  {
    return parcellaSelezionata;
  }
  public void setParcellaSelezionata(String parcellaSelezionata)
  {
    this.parcellaSelezionata = parcellaSelezionata;
  }
  public Date getDataAggiornamntoUV()
  {
    return dataAggiornamntoUV;
  }
  public void setDataAggiornamntoUV(Date dataAggiornamntoUV)
  {
    this.dataAggiornamntoUV = dataAggiornamntoUV;
  }
  public Long getIdUnarParcella()
  {
    return idUnarParcella;
  }
  public void setIdUnarParcella(Long idUnarParcella)
  {
    this.idUnarParcella = idUnarParcella;
  }
  public Long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }
  public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }
  public Long getIdParticella()
  {
    return idParticella;
  }
  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }
  public Long getIdIsolaParcella()
  {
    return idIsolaParcella;
  }
  public void setIdIsolaParcella(Long idIsolaParcella)
  {
    this.idIsolaParcella = idIsolaParcella;
  }  
  public Long getIdIlo()
  {
    return idIlo;
  }
  public void setIdIlo(Long idIlo)
  {
    this.idIlo = idIlo;
  }
  public BigDecimal getSupVitata()
  {
    return supVitata;
  }
  public void setSupVitata(BigDecimal supVitata)
  {
    this.supVitata = supVitata;
  }
  public BigDecimal getSupParcella()
  {
    return supParcella;
  }
  public void setSupParcella(BigDecimal supParcella)
  {
    this.supParcella = supParcella;
  }
  public Integer getNumeroUV()
  {
    return numeroUV;
  }
  public void setNumeroUV(Integer numeroUV)
  {
    this.numeroUV = numeroUV;
  }
  public Integer getTolleranza()
  {
    return tolleranza;
  }
  public void setTolleranza(Integer tolleranza)
  {
    this.tolleranza = tolleranza;
  }
  
  
	
}
