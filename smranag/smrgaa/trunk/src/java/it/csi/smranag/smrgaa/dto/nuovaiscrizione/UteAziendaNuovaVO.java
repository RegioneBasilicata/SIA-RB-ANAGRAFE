package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import java.io.Serializable;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */

public class UteAziendaNuovaVO implements Serializable
{  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -5779264606683409005L;
  
  
  
  private Long idUteAziendaNuova;
  private Long idAziendaNuova;
  private String denominazione;
  private String indirizzo;
  private String istatComune;
  private String desCom;
  private String istatProvincia;
  private String sglProvincia;
  private String cap;
  private String telefono;
  private String fax;
  private String note;
  
  
  
  public Long getIdUteAziendaNuova()
  {
    return idUteAziendaNuova;
  }
  public void setIdUteAziendaNuova(Long idUteAziendaNuova)
  {
    this.idUteAziendaNuova = idUteAziendaNuova;
  }
  public Long getIdAziendaNuova()
  {
    return idAziendaNuova;
  }
  public void setIdAziendaNuova(Long idAziendaNuova)
  {
    this.idAziendaNuova = idAziendaNuova;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getTelefono()
  {
    return telefono;
  }
  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }
  public String getFax()
  {
    return fax;
  }
  public void setFax(String fax)
  {
    this.fax = fax;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getDesCom()
  {
    return desCom;
  }
  public void setDesCom(String desCom)
  {
    this.desCom = desCom;
  }
  public String getIstatProvincia()
  {
    return istatProvincia;
  }
  public void setIstatProvincia(String istatProvincia)
  {
    this.istatProvincia = istatProvincia;
  }
  public String getSglProvincia()
  {
    return sglProvincia;
  }
  public void setSglProvincia(String sglProvincia)
  {
    this.sglProvincia = sglProvincia;
  }
  
  
  
  
  
}
