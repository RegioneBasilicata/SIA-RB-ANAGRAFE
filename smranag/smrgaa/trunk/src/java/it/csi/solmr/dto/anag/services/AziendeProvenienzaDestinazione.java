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
public class AziendeProvenienzaDestinazione implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -7653162838119739336L;


  private Long idAziendaProvenienza;
  private Long[] idAziendaDestinazione;

  public Long getIdAziendaProvenienza()
  {
    return idAziendaProvenienza;
  }
  public void setIdAziendaProvenienza(Long idAziendaProvenienza)
  {
    this.idAziendaProvenienza = idAziendaProvenienza;
  }

  public Long[] getIdAziendaDestinazione()
  {
    return idAziendaDestinazione;
  }
  public void setIdAziendaDestinazione(Long[] idAziendaDestinazione)
  {
    this.idAziendaDestinazione = idAziendaDestinazione;
  }

}
