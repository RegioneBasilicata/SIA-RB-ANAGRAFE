package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_FONTE
 * 
 * @author TOBECONFIG
 *
 */
public class TipoFonteVO implements Serializable 
{  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3607757655899422782L;
  
  
  private long idFonte;
  private String descrizione;
  
  
  public long getIdFonte()
  {
    return idFonte;
  }
  public void setIdFonte(long idFonte)
  {
    this.idFonte = idFonte;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  
  
}
