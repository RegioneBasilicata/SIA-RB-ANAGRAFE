package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_QUALITA_USO
 * 
 * @author TOBECONFIG
 *
 */
public class TipoQualitaUsoVO implements Serializable 
{  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -2067588364330414703L;
  
  
  
  private long idTipoQualitaUso;
  private String codiceQualitaUso;
  private String descrizioneQualitaUso;
  
  
  
  
  public long getIdTipoQualitaUso()
  {
    return idTipoQualitaUso;
  }
  public void setIdTipoQualitaUso(long idTipoQualitaUso)
  {
    this.idTipoQualitaUso = idTipoQualitaUso;
  }
  public String getCodiceQualitaUso()
  {
    return codiceQualitaUso;
  }
  public void setCodiceQualitaUso(String codiceQualitaUso)
  {
    this.codiceQualitaUso = codiceQualitaUso;
  }
  public String getDescrizioneQualitaUso()
  {
    return descrizioneQualitaUso;
  }
  public void setDescrizioneQualitaUso(String descrizioneQualitaUso)
  {
    this.descrizioneQualitaUso = descrizioneQualitaUso;
  }
  
  
}
