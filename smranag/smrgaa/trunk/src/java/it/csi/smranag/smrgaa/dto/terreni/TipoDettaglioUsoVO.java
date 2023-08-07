package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_DETTAGLIO_USO
 * 
 * @author TOBECONFIG
 *
 */
public class TipoDettaglioUsoVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = -3542049227680447993L;
  
  
  private long idTipoDettaglioUso;
  private String codiceDettaglioUso;
  private String descrizione;
  private long idTipoDestinazione;
  //private long idVarieta;
  private Integer abbattimentoPonderazione;
  private String flagNonAmmissibileEfa;
  private String flagPratoPermanente;
  private String flagColturaSommersa;
  private String flagRiposo;
  private String flagPratoForaggera;
  private String flagLeguminosa;
  private String flagProteaginosa;
  private String flagBiologico;
  private String flagSeminativo;
  
  
  public long getIdTipoDettaglioUso()
  {
    return idTipoDettaglioUso;
  }
  public void setIdTipoDettaglioUso(long idTipoDettaglioUso)
  {
    this.idTipoDettaglioUso = idTipoDettaglioUso;
  }
  public String getCodiceDettaglioUso()
  {
    return codiceDettaglioUso;
  }
  public void setCodiceDettaglioUso(String codiceDettaglioUso)
  {
    this.codiceDettaglioUso = codiceDettaglioUso;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Integer getAbbattimentoPonderazione()
  {
    return abbattimentoPonderazione;
  }
  public void setAbbattimentoPonderazione(Integer abbattimentoPonderazione)
  {
    this.abbattimentoPonderazione = abbattimentoPonderazione;
  }
  public String getFlagNonAmmissibileEfa()
  {
    return flagNonAmmissibileEfa;
  }
  public void setFlagNonAmmissibileEfa(String flagNonAmmissibileEfa)
  {
    this.flagNonAmmissibileEfa = flagNonAmmissibileEfa;
  }
  public String getFlagPratoPermanente()
  {
    return flagPratoPermanente;
  }
  public void setFlagPratoPermanente(String flagPratoPermanente)
  {
    this.flagPratoPermanente = flagPratoPermanente;
  }
  public String getFlagColturaSommersa()
  {
    return flagColturaSommersa;
  }
  public void setFlagColturaSommersa(String flagColturaSommersa)
  {
    this.flagColturaSommersa = flagColturaSommersa;
  }
  public String getFlagRiposo()
  {
    return flagRiposo;
  }
  public void setFlagRiposo(String flagRiposo)
  {
    this.flagRiposo = flagRiposo;
  }
  public String getFlagPratoForaggera()
  {
    return flagPratoForaggera;
  }
  public void setFlagPratoForaggera(String flagPratoForaggera)
  {
    this.flagPratoForaggera = flagPratoForaggera;
  }
  public String getFlagLeguminosa()
  {
    return flagLeguminosa;
  }
  public void setFlagLeguminosa(String flagLeguminosa)
  {
    this.flagLeguminosa = flagLeguminosa;
  }
  public String getFlagProteaginosa()
  {
    return flagProteaginosa;
  }
  public void setFlagProteaginosa(String flagProteaginosa)
  {
    this.flagProteaginosa = flagProteaginosa;
  }
  public String getFlagBiologico()
  {
    return flagBiologico;
  }
  public void setFlagBiologico(String flagBiologico)
  {
    this.flagBiologico = flagBiologico;
  }
  public String getFlagSeminativo()
  {
    return flagSeminativo;
  }
  public void setFlagSeminativo(String flagSeminativo)
  {
    this.flagSeminativo = flagSeminativo;
  }
  public long getIdTipoDestinazione()
  {
    return idTipoDestinazione;
  }
  public void setIdTipoDestinazione(long idTipoDestinazione)
  {
    this.idTipoDestinazione = idTipoDestinazione;
  }
  
  
  
  
  
  
  
}
