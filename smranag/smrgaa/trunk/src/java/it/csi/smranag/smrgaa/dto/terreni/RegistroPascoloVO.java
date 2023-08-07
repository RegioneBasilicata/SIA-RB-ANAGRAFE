package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati del registro pascolo
 * 
 * @author TOBECONFIG
 *
 */
public class RegistroPascoloVO implements Serializable 
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 7013857471184051731L;
  
  
  
  
  private BigDecimal superficie;
  private Long idFonte;
  private String descFonte;
  private Integer annoCampagna;
  private String comune;
  private String sezione;
  private Long idParticellaCertificata;
  private Integer foglio;
  private Long particella;
  private String subalterno;
  private Date dataInizioValidita;
  
  
  
  
  public BigDecimal getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }
  public Long getIdFonte()
  {
    return idFonte;
  }
  public void setIdFonte(Long idFonte)
  {
    this.idFonte = idFonte;
  }
  public String getDescFonte()
  {
    return descFonte;
  }
  public void setDescFonte(String descFonte)
  {
    this.descFonte = descFonte;
  }
  public Integer getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(Integer annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
  }
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public Long getIdParticellaCertificata()
  {
    return idParticellaCertificata;
  }
  public void setIdParticellaCertificata(Long idParticellaCertificata)
  {
    this.idParticellaCertificata = idParticellaCertificata;
  }
  public Integer getFoglio()
  {
    return foglio;
  }
  public void setFoglio(Integer foglio)
  {
    this.foglio = foglio;
  }
  public Long getParticella()
  {
    return particella;
  }
  public void setParticella(Long particella)
  {
    this.particella = particella;
  }
  public String getSubalterno()
  {
    return subalterno;
  }
  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  
  
  
  
	
}
