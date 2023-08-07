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

public class CCAziendaNuovaVO implements Serializable
{  
 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5915519916815049136L;
  
  
  
  private Long idCCAziendaNuova;
  private Long idAziendaNuova;
  private String iban;
  private String descBanca;
  private String descFiliale;
  private String capFiliale;
  private String indirizzoFiliale;
  private String descComuneFiliale;
  private String sglProvFiliale;
  
  
  public Long getIdCCAziendaNuova()
  {
    return idCCAziendaNuova;
  }
  public void setIdCCAziendaNuova(Long idCCAziendaNuova)
  {
    this.idCCAziendaNuova = idCCAziendaNuova;
  }
  public String getIban()
  {
    return iban;
  }
  public void setIban(String iban)
  {
    this.iban = iban;
  }
  public Long getIdAziendaNuova()
  {
    return idAziendaNuova;
  }
  public void setIdAziendaNuova(Long idAziendaNuova)
  {
    this.idAziendaNuova = idAziendaNuova;
  }
  public String getDescBanca()
  {
    return descBanca;
  }
  public void setDescBanca(String descBanca)
  {
    this.descBanca = descBanca;
  }
  public String getDescFiliale()
  {
    return descFiliale;
  }
  public void setDescFiliale(String descFiliale)
  {
    this.descFiliale = descFiliale;
  }
  public String getCapFiliale()
  {
    return capFiliale;
  }
  public void setCapFiliale(String capFiliale)
  {
    this.capFiliale = capFiliale;
  }
  public String getIndirizzoFiliale()
  {
    return indirizzoFiliale;
  }
  public void setIndirizzoFiliale(String indirizzoFiliale)
  {
    this.indirizzoFiliale = indirizzoFiliale;
  }
  public String getDescComuneFiliale()
  {
    return descComuneFiliale;
  }
  public void setDescComuneFiliale(String descComuneFiliale)
  {
    this.descComuneFiliale = descComuneFiliale;
  }
  public String getSglProvFiliale()
  {
    return sglProvFiliale;
  }
  public void setSglProvFiliale(String sglProvFiliale)
  {
    this.sglProvFiliale = sglProvFiliale;
  }
  
  
  
  
}
