package it.csi.smranag.smrgaa.dto.uma;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati del tipo macchina
 * 
 * @author TOBECONFIG
 *
 */
public class TipoMacchinaGaaVO implements Serializable 
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 7677972473294749816L;
  
  
  private long idTipoMacchina;
  private String codiceTipoMacchina;
  private String descrizione;
  private String flagIrroratrice;
  private Date dataInizioValidita;
  
  
  
  public long getIdTipoMacchina()
  {
    return idTipoMacchina;
  }
  public void setIdTipoMacchina(long idTipoMacchina)
  {
    this.idTipoMacchina = idTipoMacchina;
  }
  public String getCodiceTipoMacchina()
  {
    return codiceTipoMacchina;
  }
  public void setCodiceTipoMacchina(String codiceTipoMacchina)
  {
    this.codiceTipoMacchina = codiceTipoMacchina;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagIrroratrice()
  {
    return flagIrroratrice;
  }
  public void setFlagIrroratrice(String flagIrroratrice)
  {
    this.flagIrroratrice = flagIrroratrice;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  
 
  
  
  
	
}
