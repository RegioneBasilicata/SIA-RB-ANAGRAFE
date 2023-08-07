package it.csi.smranag.smrgaa.dto.terreni;


/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati delle particelle legate all'istanza di riesame
 * 
 * @author TOBECONFIG
 *
 */
public class ParticellaIstanzaRiesameVO extends IstanzaRiesameVO  
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 4348343087662716525L;
  
  
  private String descComune;
  private String sezione;
  private String foglio;
  private String particella;
  
  
  
  
 
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public String getFoglio()
  {
    return foglio;
  }
  public void setFoglio(String foglio)
  {
    this.foglio = foglio;
  }
  public String getParticella()
  {
    return particella;
  }
  public void setParticella(String particella)
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
  private String subalterno;
  
  
  
  
  
  
	
}
