package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

import java.io.*;

public class DelegaAnagrafePadriVO implements Serializable
{
 /**
  * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
  * compatibili con le versioni precedenti utilizzate da eventuali client
  */
  static final long serialVersionUID = 6187305094947463893L;

  private String cuaa;
  private String codDelegaRegione;
  private String descDelegaRegione;
  private String codDelegaProvincia;
  private String descDelegaProvincia;
  private String codDelegaZona;
  private String descDelegaZona;

  public String getCuaa() {
    return cuaa;
  }
  public void setCuaa(String cuaa) {
    this.cuaa = cuaa;
  }
  public String getCodDelegaRegione() {
    return codDelegaRegione;
  }
  public void setCodDelegaRegione(String codDelegaRegione) {
    this.codDelegaRegione = codDelegaRegione;
  }
  public String getDescDelegaRegione() {
    return descDelegaRegione;
  }
  public void setDescDelegaRegione(String descDelegaRegione) {
    this.descDelegaRegione = descDelegaRegione;
  }
  public String getCodDelegaProvincia() {
    return codDelegaProvincia;
  }
  public void setCodDelegaProvincia(String codDelegaProvincia) {
    this.codDelegaProvincia = codDelegaProvincia;
  }
  public String getDescDelegaProvincia() {
    return descDelegaProvincia;
  }
  public void setDescDelegaProvincia(String descDelegaProvincia) {
    this.descDelegaProvincia = descDelegaProvincia;
  }
  public String getCodDelegaZona() {
    return codDelegaZona;
  }
  public void setCodDelegaZona(String codDelegaZona) {
    this.codDelegaZona = codDelegaZona;
  }
  public String getDescDelegaZona() {
    return descDelegaZona;
  }
  public void setDescDelegaZona(String descDelegaZona) {
    this.descDelegaZona = descDelegaZona;
  }
}
