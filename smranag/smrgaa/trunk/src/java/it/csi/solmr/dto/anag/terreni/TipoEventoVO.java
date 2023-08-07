package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_EVENTO
 * 
 * @author 71646
 *
 */

public class TipoEventoVO implements Serializable {

 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2484198421422780244L;
  
  
  
  private long idEvento;
  private String descrizione;
  private boolean cessParticella;
  
  
  
  
  public long getIdEvento()
  {
    return idEvento;
  }
  public void setIdEvento(long idEvento)
  {
    this.idEvento = idEvento;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public boolean isCessParticella()
  {
    return cessParticella;
  }
  public void setCessParticella(boolean cessParticella)
  {
    this.cessParticella = cessParticella;
  }

  
}
