package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_METODO_IRRIGUO
 * 
 * @author TOBECONFIG
 *
 */
public class TipoMetodoIrriguoVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 1762705144902086328L;
  
  
  private long idMetodoIrriguo;
  private String codiceMetodoIrriguo;
  private String descrizioneMetodoIrriguo;
  
  
  public long getIdMetodoIrriguo()
  {
    return idMetodoIrriguo;
  }
  public void setIdMetodoIrriguo(long idMetodoIrriguo)
  {
    this.idMetodoIrriguo = idMetodoIrriguo;
  }
  public String getCodiceMetodoIrriguo()
  {
    return codiceMetodoIrriguo;
  }
  public void setCodiceMetodoIrriguo(String codiceMetodoIrriguo)
  {
    this.codiceMetodoIrriguo = codiceMetodoIrriguo;
  }
  public String getDescrizioneMetodoIrriguo()
  {
    return descrizioneMetodoIrriguo;
  }
  public void setDescrizioneMetodoIrriguo(String descrizioneMetodoIrriguo)
  {
    this.descrizioneMetodoIrriguo = descrizioneMetodoIrriguo;
  }
  
  
  
  
  
}
