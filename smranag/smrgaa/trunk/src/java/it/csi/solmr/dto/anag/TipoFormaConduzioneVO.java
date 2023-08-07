package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class TipoFormaConduzioneVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 2023219850026221950L;

  private String idFormaConduzione;
  private String codice;
  private String forma;
  private String descrizione;
  private Vector<String> vDescAttivitaComplementari;
  
  

  public String getIdFormaConduzione() {
    return idFormaConduzione;
  }

  public void setIdFormaConduzione(String idFormaConduzione) {
    this.idFormaConduzione = idFormaConduzione;
  }

  public Long getIdFormaConduzioneLong() {
    try {
      return new Long(idFormaConduzione);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdFormaConduzioneLong(Long idFormaConduzione) {
    this.idFormaConduzione = idFormaConduzione == null ? null :
        idFormaConduzione.toString();
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getForma() {
    return forma;
  }

  public void setForma(String forma) {
    this.forma = forma;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Vector<String> getvDescAttivitaComplementari()
  {
    return vDescAttivitaComplementari;
  }

  public void setvDescAttivitaComplementari(
      Vector<String> vDescAttivitaComplementari)
  {
    this.vDescAttivitaComplementari = vDescAttivitaComplementari;
  }
  
}