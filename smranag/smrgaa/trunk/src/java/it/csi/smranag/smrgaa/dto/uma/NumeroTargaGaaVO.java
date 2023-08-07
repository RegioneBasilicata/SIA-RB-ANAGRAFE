package it.csi.smranag.smrgaa.dto.uma;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati del numero targa
 * 
 * @author TOBECONFIG
 *
 */
public class NumeroTargaGaaVO implements Serializable 
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 1906241910159864235L;
  
  
  
  private long idNumeroTarga;
  private long idMacchina;
  private long idTarga;
  private String descTipoTarga;
  private String numeroTarga;
  private String idProvincia;
  private String flagTargaNuova;
  private String mc824;
  private Date dataPrimaImmatricolazione;
  
  
  public long getIdNumeroTarga()
  {
    return idNumeroTarga;
  }
  public void setIdNumeroTarga(long idNumeroTarga)
  {
    this.idNumeroTarga = idNumeroTarga;
  }
  public long getIdMacchina()
  {
    return idMacchina;
  }
  public void setIdMacchina(long idMacchina)
  {
    this.idMacchina = idMacchina;
  }
  public long getIdTarga()
  {
    return idTarga;
  }
  public void setIdTarga(long idTarga)
  {
    this.idTarga = idTarga;
  }
  public String getDescTipoTarga()
  {
    return descTipoTarga;
  }
  public void setDescTipoTarga(String descTipoTarga)
  {
    this.descTipoTarga = descTipoTarga;
  }
  public String getNumeroTarga()
  {
    return numeroTarga;
  }
  public void setNumeroTarga(String numeroTarga)
  {
    this.numeroTarga = numeroTarga;
  }
  public String getIdProvincia()
  {
    return idProvincia;
  }
  public void setIdProvincia(String idProvincia)
  {
    this.idProvincia = idProvincia;
  }
  public String getFlagTargaNuova()
  {
    return flagTargaNuova;
  }
  public void setFlagTargaNuova(String flagTargaNuova)
  {
    this.flagTargaNuova = flagTargaNuova;
  }
  public String getMc824()
  {
    return mc824;
  }
  public void setMc824(String mc824)
  {
    this.mc824 = mc824;
  }
  public Date getDataPrimaImmatricolazione()
  {
    return dataPrimaImmatricolazione;
  }
  public void setDataPrimaImmatricolazione(Date dataPrimaImmatricolazione)
  {
    this.dataPrimaImmatricolazione = dataPrimaImmatricolazione;
  }
  
  
  
  
 
	
}
