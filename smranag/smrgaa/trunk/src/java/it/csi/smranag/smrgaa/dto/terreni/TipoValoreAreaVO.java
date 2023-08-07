package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_VALORE_AREA
 * 
 * @author TOBECONFIG
 *
 */
public class TipoValoreAreaVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4068739851275631621L;
  
  
  
  private long idTipoArea;
  private long idTipoValoreArea;
  private String descrizione;
  private String descrizioneEstesa;
  private String codice;
  private String valore;
  private BigDecimal supUtilizzata;
  
  
  public long getIdTipoArea()
  {
    return idTipoArea;
  }
  public void setIdTipoArea(long idTipoArea)
  {
    this.idTipoArea = idTipoArea;
  }
  public long getIdTipoValoreArea()
  {
    return idTipoValoreArea;
  }
  public void setIdTipoValoreArea(long idTipoValoreArea)
  {
    this.idTipoValoreArea = idTipoValoreArea;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getDescrizioneEstesa()
  {
    return descrizioneEstesa;
  }
  public void setDescrizioneEstesa(String descrizioneEstesa)
  {
    this.descrizioneEstesa = descrizioneEstesa;
  }
  public String getCodice()
  {
    return codice;
  }
  public void setCodice(String codice)
  {
    this.codice = codice;
  }
  public String getValore()
  {
    return valore;
  }
  public void setValore(String valore)
  {
    this.valore = valore;
  }
  public BigDecimal getSupUtilizzata()
  {
    return supUtilizzata;
  }
  public void setSupUtilizzata(BigDecimal supUtilizzata)
  {
    this.supUtilizzata = supUtilizzata;
  }
  
  
}
