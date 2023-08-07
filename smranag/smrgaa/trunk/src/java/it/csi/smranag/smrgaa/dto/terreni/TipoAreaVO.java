package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.util.Vector;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_AREA
 * 
 * @author TOBECONFIG
 *
 */
public class TipoAreaVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7026894095446797283L;
  
  
  
  private long idTipoArea;
  private String descrizione;
  private String descrizioneEstesa;
  private String codice;
  private String flagEsclusivoFoglio;
  private String flagAreaModificabile;
  private Vector<TipoValoreAreaVO> vTipoValoreArea;
  
  
  public long getIdTipoArea()
  {
    return idTipoArea;
  }
  public void setIdTipoArea(long idTipoArea)
  {
    this.idTipoArea = idTipoArea;
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
  public String getFlagEsclusivoFoglio()
  {
    return flagEsclusivoFoglio;
  }
  public void setFlagEsclusivoFoglio(String flagEsclusivoFoglio)
  {
    this.flagEsclusivoFoglio = flagEsclusivoFoglio;
  }
  public Vector<TipoValoreAreaVO> getvTipoValoreArea()
  {
    return vTipoValoreArea;
  }
  public void setvTipoValoreArea(Vector<TipoValoreAreaVO> vTipoValoreArea)
  {
    this.vTipoValoreArea = vTipoValoreArea;
  }
  public String getFlagAreaModificabile()
  {
    return flagAreaModificabile;
  }
  public void setFlagAreaModificabile(String flagAreaModificabile)
  {
    this.flagAreaModificabile = flagAreaModificabile;
  }
  
  
  
}
