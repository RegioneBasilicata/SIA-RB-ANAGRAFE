package it.csi.solmr.dto.anag;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class DichiarazioneConsistenzaVO
    implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -2173231678878497778L;
  private String idGruppoControllo;
  private String descGruppoControllo;
  private String tipoAnomaliaErrore;
  private String descAnomaliaErrore;
  private boolean bloccante;

  public String getIdGruppoControllo()
  {
    return idGruppoControllo;
  }
  public void setIdGruppoControllo(String idGruppoControllo)
  {
    this.idGruppoControllo = idGruppoControllo;
  }

  public void setDescGruppoControllo(String descGruppoControllo)
  {
    this.descGruppoControllo = descGruppoControllo;
  }
  public String getDescGruppoControllo()
  {
    return descGruppoControllo;
  }

  public void setTipoAnomaliaErrore(String tipoAnomaliaErrore)
  {
    this.tipoAnomaliaErrore = tipoAnomaliaErrore;
  }
  public String getTipoAnomaliaErrore()
  {
    return tipoAnomaliaErrore;
  }

  public void setDescAnomaliaErrore(String descAnomaliaErrore)
  {
    this.descAnomaliaErrore = descAnomaliaErrore;
  }
  public String getDescAnomaliaErrore()
  {
    return descAnomaliaErrore;
  }

  public void setBloccante(boolean bloccante)
  {
    this.bloccante = bloccante;
  }
  public boolean isBloccante()
  {
    return bloccante;
  }
}