package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_PRATICA_MANTENIMENTO
 * 
 * @author TOBECONFIG
 *
 */
public class TipoPraticaMantenimentoVO implements Serializable 
{  
 
  

  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6919226221454212463L;
  
  
  private long idPraticaMantenimento;
  private String codicePraticaMantenimento;
  private String descrizionePraticaMantenim;
  
  
  public long getIdPraticaMantenimento()
  {
    return idPraticaMantenimento;
  }
  public void setIdPraticaMantenimento(long idPraticaMantenimento)
  {
    this.idPraticaMantenimento = idPraticaMantenimento;
  }
  public String getCodicePraticaMantenimento()
  {
    return codicePraticaMantenimento;
  }
  public void setCodicePraticaMantenimento(String codicePraticaMantenimento)
  {
    this.codicePraticaMantenimento = codicePraticaMantenimento;
  }
  public String getDescrizionePraticaMantenim()
  {
    return descrizionePraticaMantenim;
  }
  public void setDescrizionePraticaMantenim(String descrizionePraticaMantenim)
  {
    this.descrizionePraticaMantenim = descrizionePraticaMantenim;
  }
  
  
  
  
  
}
