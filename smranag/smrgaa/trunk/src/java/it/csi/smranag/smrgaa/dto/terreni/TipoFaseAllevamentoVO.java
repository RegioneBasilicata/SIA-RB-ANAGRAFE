package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_FASE_ALLEVAMENTO
 * 
 * @author TOBECONFIG
 *
 */
public class TipoFaseAllevamentoVO implements Serializable 
{  
 
  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1427852067310273687L;
  
  
  
  private long idFaseAllevamento;
  private String codiceFaseAllevamento;
  private String descrizioneFaseAllevamento;
  private String flagDefault;
  
  
  public long getIdFaseAllevamento()
  {
    return idFaseAllevamento;
  }
  public void setIdFaseAllevamento(long idFaseAllevamento)
  {
    this.idFaseAllevamento = idFaseAllevamento;
  }
  public String getCodiceFaseAllevamento()
  {
    return codiceFaseAllevamento;
  }
  public void setCodiceFaseAllevamento(String codiceFaseAllevamento)
  {
    this.codiceFaseAllevamento = codiceFaseAllevamento;
  }
  public String getDescrizioneFaseAllevamento()
  {
    return descrizioneFaseAllevamento;
  }
  public void setDescrizioneFaseAllevamento(String descrizioneFaseAllevamento)
  {
    this.descrizioneFaseAllevamento = descrizioneFaseAllevamento;
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
