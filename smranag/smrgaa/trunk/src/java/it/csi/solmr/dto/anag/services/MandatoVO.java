package it.csi.solmr.dto.anag.services;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

public class MandatoVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 6202796101513078154L;

  private DelegaAnagrafeVO delegaAnagrafe;
  private Boolean esenteDelega;

  public DelegaAnagrafeVO getDelegaAnagrafe()
  {
    return delegaAnagrafe;
  }
  public void setDelegaAnagrafe(DelegaAnagrafeVO delegaAnagrafe)
  {
    this.delegaAnagrafe = delegaAnagrafe;
  }

  public void setEsenteDelega(Boolean esenteDelega)
  {
    this.esenteDelega = esenteDelega;
  }
  public Boolean getEsenteDelega()
  {
    return esenteDelega;
  }


}