package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_ESPORTAZIONE_DATI
 * 
 * @author TOBECONFIG
 *
 */
public class TipoEsportazioneDatiVO implements Serializable 
{  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3060213174371628427L;
  
  
  private long idTipoEsportazioneDati;
  private String nome;
  private String descrizione;
  private String codiceEsportazione;
  private String esportazioneDichiarazione;
  private String esportazioneInLavorazione;
  
  
  
  
  public long getIdTipoEsportazioneDati()
  {
    return idTipoEsportazioneDati;
  }
  public void setIdTipoEsportazioneDati(long idTipoEsportazioneDati)
  {
    this.idTipoEsportazioneDati = idTipoEsportazioneDati;
  }
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getCodiceEsportazione()
  {
    return codiceEsportazione;
  }
  public void setCodiceEsportazione(String codiceEsportazione)
  {
    this.codiceEsportazione = codiceEsportazione;
  }
  public String getEsportazioneDichiarazione()
  {
    return esportazioneDichiarazione;
  }
  public void setEsportazioneDichiarazione(String esportazioneDichiarazione)
  {
    this.esportazioneDichiarazione = esportazioneDichiarazione;
  }
  public String getEsportazioneInLavorazione()
  {
    return esportazioneInLavorazione;
  }
  public void setEsportazioneInLavorazione(String esportazioneInLavorazione)
  {
    this.esportazioneInLavorazione = esportazioneInLavorazione;
  }
  
  
  
  
  
  
}
