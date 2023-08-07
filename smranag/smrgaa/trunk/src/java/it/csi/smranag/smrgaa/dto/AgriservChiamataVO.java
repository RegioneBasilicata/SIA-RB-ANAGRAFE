package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;

/**
 * Mappa i dati delle tabelle db_agriserv_xxxx.
 * Individua un singolo modulo di esposizione del servizio Agriserv
 * verso l'analisi e contiene le informazioni necessare per far
 * puntare la pd generica al server che espone il servizio, ossia
 * il nome jndi e l'url t3. 
 * Oct 14, 2008
 * @author TOBECONFIG
 *
 */
public class AgriservChiamataVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -3465125791586646370L;
  private String urlPD;
  private String nomeJndiPA;
  private String codiceChiamata;
  private String descrizioneChiamata;
  private String codiceEspositore;
  private String descrizioneEspositore;

  public String getDescrizioneEspositore()
  {
    return descrizioneEspositore;
  }

  public void setDescrizioneEspositore(String descrizioneEspositore)
  {
    this.descrizioneEspositore = descrizioneEspositore;
  }

  public String getCodiceEspositore()
  {
    return codiceEspositore;
  }

  public void setCodiceEspositore(String codiceEspositore)
  {
    this.codiceEspositore = codiceEspositore;
  }

  public String getUrlPD()
  {
    return urlPD;
  }

  public void setUrlPD(String urlPD)
  {
    this.urlPD = urlPD;
  }

  public String getNomeJndiPA()
  {
    return nomeJndiPA;
  }

  public void setNomeJndiPA(String nomeJndiPA)
  {
    this.nomeJndiPA = nomeJndiPA;
  }

  public String getCodiceChiamata()
  {
    return codiceChiamata;
  }

  public void setCodiceChiamata(String codiceChiamata)
  {
    this.codiceChiamata = codiceChiamata;
  }

  public String getDescrizioneChiamata()
  {
    return descrizioneChiamata;
  }

  public void setDescrizioneChiamata(String descrizioneChiamata)
  {
    this.descrizioneChiamata = descrizioneChiamata;
  }
}
