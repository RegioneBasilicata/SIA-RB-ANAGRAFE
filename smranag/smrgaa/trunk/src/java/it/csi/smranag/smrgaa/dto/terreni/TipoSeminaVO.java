package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_SEMINA
 * 
 * @author TOBECONFIG
 *
 */
public class TipoSeminaVO implements Serializable 
{  
   
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5528065241798860495L;
  
  
  
  private long idTipoSemina;
  private String codiceSemina;
  private String descrizioneSemina;
  private String flagDefault;
  
  
  public long getIdTipoSemina()
  {
    return idTipoSemina;
  }
  public void setIdTipoSemina(long idTipoSemina)
  {
    this.idTipoSemina = idTipoSemina;
  }
  public String getCodiceSemina()
  {
    return codiceSemina;
  }
  public void setCodiceSemina(String codiceSemina)
  {
    this.codiceSemina = codiceSemina;
  }
  public String getDescrizioneSemina()
  {
    return descrizioneSemina;
  }
  public void setDescrizioneSemina(String descrizioneSemina)
  {
    this.descrizioneSemina = descrizioneSemina;
  }
  public String getFlagDefault()
  {
    return flagDefault;
  }
  public void setFlagDefault(String flagDefault)
  {
    this.flagDefault = flagDefault;
  }
  
  
  
  
  
  
}
