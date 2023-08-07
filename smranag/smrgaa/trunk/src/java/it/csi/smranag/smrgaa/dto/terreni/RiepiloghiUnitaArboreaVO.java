package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe coi i campi per la visualizzazione 
 * dei riepiloghi delle unità vitate
 * 
 * @author TOBECONFIG
 *
 */
public class RiepiloghiUnitaArboreaVO implements Serializable 
{
	
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 1133295747412564680L;
  
  
  
  
  
  private String tipoTipolgiaVino;
  private Long idTipolgiaVino;
  private String istatComune;
  private String descComune;
  private String istatProv;
  private String descProv;
  private String siglaProv;
  private BigDecimal uvaDaVino;
  private BigDecimal uvaDaMensa;
  private BigDecimal altreDestinazioniProduttive;
  private BigDecimal supIscritta;
  private BigDecimal supVitata;
  private BigDecimal supIscrittaDOP;
  private BigDecimal supVitataDOP;
  private BigDecimal supIscrittaVinoTavola;
  private BigDecimal supVitataVinoTavola;
  private BigDecimal prodResa100;
  private BigDecimal prodResa70;
  private BigDecimal prodResa0;
  private BigDecimal resa;
  private BigDecimal supVitata100;
  private BigDecimal supVitata70;
  private BigDecimal supVitata0;
  private String descVarieta;
  private Integer numElementi;
  
  
  //****** Campi solo per l'uso del riepilogo nei soci
  private String denominazioneAzienda;
  private String cuaaAzienda;
  
  
  
  
  public String getTipoTipolgiaVino()
  {
    return tipoTipolgiaVino;
  }
  public void setTipoTipolgiaVino(String tipoTipolgiaVino)
  {
    this.tipoTipolgiaVino = tipoTipolgiaVino;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getIstatProv()
  {
    return istatProv;
  }
  public void setIstatProv(String istatProv)
  {
    this.istatProv = istatProv;
  }
  public String getDescProv()
  {
    return descProv;
  }
  public void setDescProv(String descProv)
  {
    this.descProv = descProv;
  }
  public String getSiglaProv()
  {
    return siglaProv;
  }
  public void setSiglaProv(String siglaProv)
  {
    this.siglaProv = siglaProv;
  }
  public BigDecimal getUvaDaVino()
  {
    return uvaDaVino;
  }
  public void setUvaDaVino(BigDecimal uvaDaVino)
  {
    this.uvaDaVino = uvaDaVino;
  }
  public BigDecimal getUvaDaMensa()
  {
    return uvaDaMensa;
  }
  public void setUvaDaMensa(BigDecimal uvaDaMensa)
  {
    this.uvaDaMensa = uvaDaMensa;
  }
  public BigDecimal getAltreDestinazioniProduttive()
  {
    return altreDestinazioniProduttive;
  }
  public void setAltreDestinazioniProduttive(
      BigDecimal altreDestinazioniProduttive)
  {
    this.altreDestinazioniProduttive = altreDestinazioniProduttive;
  }
  public BigDecimal getSupIscritta()
  {
    return supIscritta;
  }
  public void setSupIscritta(BigDecimal supIscritta)
  {
    this.supIscritta = supIscritta;
  }
  public BigDecimal getSupVitata()
  {
    return supVitata;
  }
  public void setSupVitata(BigDecimal supVitata)
  {
    this.supVitata = supVitata;
  }
  public BigDecimal getSupIscrittaDOP()
  {
    return supIscrittaDOP;
  }
  public void setSupIscrittaDOP(BigDecimal supIscrittaDOP)
  {
    this.supIscrittaDOP = supIscrittaDOP;
  }
  public BigDecimal getSupVitataDOP()
  {
    return supVitataDOP;
  }
  public void setSupVitataDOP(BigDecimal supVitataDOP)
  {
    this.supVitataDOP = supVitataDOP;
  }
  public BigDecimal getSupIscrittaVinoTavola()
  {
    return supIscrittaVinoTavola;
  }
  public void setSupIscrittaVinoTavola(BigDecimal supIscrittaVinoTavola)
  {
    this.supIscrittaVinoTavola = supIscrittaVinoTavola;
  }
  public BigDecimal getSupVitataVinoTavola()
  {
    return supVitataVinoTavola;
  }
  public void setSupVitataVinoTavola(BigDecimal supVitataVinoTavola)
  {
    this.supVitataVinoTavola = supVitataVinoTavola;
  }
  public Long getIdTipolgiaVino()
  {
    return idTipolgiaVino;
  }
  public void setIdTipolgiaVino(Long idTipolgiaVino)
  {
    this.idTipolgiaVino = idTipolgiaVino;
  }
  public BigDecimal getProdResa100()
  {
    return prodResa100;
  }
  public void setProdResa100(BigDecimal prodResa100)
  {
    this.prodResa100 = prodResa100;
  }
  public BigDecimal getProdResa70()
  {
    return prodResa70;
  }
  public void setProdResa70(BigDecimal prodResa70)
  {
    this.prodResa70 = prodResa70;
  }
  public BigDecimal getResa()
  {
    return resa;
  }
  public void setResa(BigDecimal resa)
  {
    this.resa = resa;
  }
  public BigDecimal getSupVitata100()
  {
    return supVitata100;
  }
  public void setSupVitata100(BigDecimal supVitata100)
  {
    this.supVitata100 = supVitata100;
  }
  public BigDecimal getSupVitata70()
  {
    return supVitata70;
  }
  public void setSupVitata70(BigDecimal supVitata70)
  {
    this.supVitata70 = supVitata70;
  }
  public BigDecimal getProdResa0()
  {
    return prodResa0;
  }
  public void setProdResa0(BigDecimal prodResa0)
  {
    this.prodResa0 = prodResa0;
  }
  public BigDecimal getSupVitata0()
  {
    return supVitata0;
  }
  public void setSupVitata0(BigDecimal supVitata0)
  {
    this.supVitata0 = supVitata0;
  }
  public String getDenominazioneAzienda()
  {
    return denominazioneAzienda;
  }
  public void setDenominazioneAzienda(String denominazioneAzienda)
  {
    this.denominazioneAzienda = denominazioneAzienda;
  }
  public String getCuaaAzienda()
  {
    return cuaaAzienda;
  }
  public void setCuaaAzienda(String cuaaAzienda)
  {
    this.cuaaAzienda = cuaaAzienda;
  }
  public String getDescVarieta()
  {
    return descVarieta;
  }
  public void setDescVarieta(String descVarieta)
  {
    this.descVarieta = descVarieta;
  }
  public Integer getNumElementi()
  {
    return numElementi;
  }
  public void setNumElementi(Integer numElementi)
  {
    this.numElementi = numElementi;
  }
  
  
  
	
}
