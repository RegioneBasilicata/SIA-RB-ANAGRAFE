package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_CASO_PARTICOLARE
 * 
 * @author TOBECONFIG
 *
 */
public class CasoParticolareVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5402694856589619776L;
  
  
  private long idCasoParticolare;
  private String descrizione;
  private String particellaObbligatoria;
  
  
  public long getIdCasoParticolare()
  {
    return idCasoParticolare;
  }
  public void setIdCasoParticolare(long idCasoParticolare)
  {
    this.idCasoParticolare = idCasoParticolare;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getParticellaObbligatoria()
  {
    return particellaObbligatoria;
  }
  public void setParticellaObbligatoria(String particellaObbligatoria)
  {
    this.particellaObbligatoria = particellaObbligatoria;
  }
  
  
  
}
