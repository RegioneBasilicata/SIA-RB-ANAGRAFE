package it.csi.smranag.smrgaa.dto.sigmater;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_PARTICELLA_SIGMATER
 * 
 * @author TOBECONFIG
 *
 */
public class ParticellaSigmaterVO implements Serializable 
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6282553481832111837L;
  
  
  
  
  
  private long idParticellaSigmater;
  private String comune;
  private String sezione;
  private Long foglio;
  private Long particella;  
  private String subalterno;
  private String denominatore;
  private String edificialita;
  
  
  
  
  public long getIdParticellaSigmater()
  {
    return idParticellaSigmater;
  }
  public void setIdParticellaSigmater(long idParticellaSigmater)
  {
    this.idParticellaSigmater = idParticellaSigmater;
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
  public Long getFoglio()
  {
    return foglio;
  }
  public void setFoglio(Long foglio)
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
  public String getDenominatore()
  {
    return denominatore;
  }
  public void setDenominatore(String denominatore)
  {
    this.denominatore = denominatore;
  }
  public String getEdificialita()
  {
    return edificialita;
  }
  public void setEdificialita(String edificialita)
  {
    this.edificialita = edificialita;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
}
