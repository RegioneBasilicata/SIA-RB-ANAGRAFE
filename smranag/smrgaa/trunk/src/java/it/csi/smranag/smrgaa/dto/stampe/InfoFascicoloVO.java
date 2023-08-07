package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;

public class InfoFascicoloVO implements Serializable
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 7250425204861316807L;
  
  
  
  
  private String denominazione;
  private String descrizione;
  private String indirizzo;
  private String cap;
  private String descComune;
  private String siglaProv;
  private String codiceFiscale;
  private String descComuneIntermediario;
  private String siglaProvIntermediario;
  private String responsabile;
  
  
  
  
  
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSiglaProv()
  {
    return siglaProv;
  }
  public void setSiglaProv(String siglaProv)
  {
    this.siglaProv = siglaProv;
  }
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }
  public String getDescComuneIntermediario()
  {
    return descComuneIntermediario;
  }
  public void setDescComuneIntermediario(String descComuneIntermediario)
  {
    this.descComuneIntermediario = descComuneIntermediario;
  }
  public String getSiglaProvIntermediario()
  {
    return siglaProvIntermediario;
  }
  public void setSiglaProvIntermediario(String siglaProvIntermediario)
  {
    this.siglaProvIntermediario = siglaProvIntermediario;
  }
  public String getResponsabile()
  {
    return responsabile;
  }
  public void setResponsabile(String responsabile)
  {
    this.responsabile = responsabile;
  }
  
  
  
  
}
