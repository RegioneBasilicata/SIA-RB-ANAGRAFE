package it.csi.solmr.dto.anag;
/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */


import java.io.Serializable;

public class PersonaGiuridicaVO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -3459210211890616602L;
  
  
  String codiceFiscale;
  String partitaIva;
  Long idSoggetto;
  String istatComune;
  String denominazione;
  
  
  
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }
  public String getPartitaIva()
  {
    return partitaIva;
  }
  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }
  public Long getIdSoggetto()
  {
    return idSoggetto;
  }
  public void setIdSoggetto(Long idSoggetto)
  {
    this.idSoggetto = idSoggetto;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  
  
  
  
}
