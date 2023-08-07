package it.csi.solmr.dto.anag.services;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author unascribed
 * @version 1.0
 */
public class CodeDate implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 1294444420078109689L;

  private String code;
  private java.util.Date data;

  public String getCode()
  {
    return code;
  }
  public void setCode(String code)
  {
    this.code = code;
  }
  public void setData(java.util.Date data)
  {
    this.data = data;
  }
  public java.util.Date getData()
  {
    return data;
  }

}