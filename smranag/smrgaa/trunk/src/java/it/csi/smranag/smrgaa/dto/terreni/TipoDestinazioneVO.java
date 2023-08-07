package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_DESTINAZIONE
 * 
 * @author TOBECONFIG
 *
 */
public class TipoDestinazioneVO implements Serializable 
{  
 
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -8385726180023264715L;
  
  
  
  private long idTipoDestinazione;
  private String codiceDestinazione;
  private String descrizioneDestinazione;
  
  
  
  public long getIdTipoDestinazione()
  {
    return idTipoDestinazione;
  }
  public void setIdTipoDestinazione(long idTipoDestinazione)
  {
    this.idTipoDestinazione = idTipoDestinazione;
  }
  public String getCodiceDestinazione()
  {
    return codiceDestinazione;
  }
  public void setCodiceDestinazione(String codiceDestinazione)
  {
    this.codiceDestinazione = codiceDestinazione;
  }
  public String getDescrizioneDestinazione()
  {
    return descrizioneDestinazione;
  }
  public void setDescrizioneDestinazione(String descrizioneDestinazione)
  {
    this.descrizioneDestinazione = descrizioneDestinazione;
  }
  
  
  
  
  
  
}
