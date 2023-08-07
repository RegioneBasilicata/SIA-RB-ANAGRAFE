package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * <br>
 *
 * Value Object per il trasporto di dati pertinenti le superfici
 *
 * @author Luca Romanello
 * @version 1.0
 */

import java.io.Serializable;

public class SuperficieVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -3220265727489273128L;

  private String tipo;
  private String descrizione;
  private Double valore;

  public SuperficieVO() {}
  public String getTipo()
  {
    return tipo;
  }
  public void setTipo(String tipo)
  {
    this.tipo = tipo;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setValore(Double valore)
  {
    this.valore = valore;
  }
  public Double getValore()
  {
    return valore;
  }
}