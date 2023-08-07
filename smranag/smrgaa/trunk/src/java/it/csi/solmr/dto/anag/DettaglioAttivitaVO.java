package it.csi.solmr.dto.anag;

import java.io.Serializable;
import it.csi.solmr.util.*;
import it.csi.solmr.dto.CodeDescription;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class DettaglioAttivitaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 5862843481936923551L;

  private String idDettaglioAttivita = null;
  private String idManodopera = null;
  private CodeDescription attivitaComplementari = null;
  private String descrizione = null;

  public DettaglioAttivitaVO() {
    attivitaComplementari = new CodeDescription();
  }

  public String getDump() {
    return null;
  }

  public ValidationErrors validate() {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public String getIdDettaglioAttivita() {
    return idDettaglioAttivita;
  }

  public void setIdDettaglioAttivita(String idDettaglioAttivita) {
    this.idDettaglioAttivita = idDettaglioAttivita;
  }

  public Long getIdDettaglioAttivitaLong() {
    try {
      return new Long(idDettaglioAttivita);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdDettaglioAttivitaLong(Long idDettaglioAttivita) {
    this.idDettaglioAttivita = idDettaglioAttivita == null ? null :
        idDettaglioAttivita.toString();
  }

  public String getIdManodopera() {
    return idManodopera;
  }

  public void setIdManodopera(String idManodopera) {
    this.idManodopera = idManodopera;
  }

  public Long getIdManodoperaLong() {
    try {
      return new Long(idManodopera);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdManodoperaLong(Long idManodopera) {
    this.idManodopera = idManodopera == null ? null : idManodopera.toString();
  }

  public CodeDescription getAttivitaComplementari() {
    return attivitaComplementari;
  }

  public void setAttivitaComplementari(CodeDescription attivitaComplementari) {
    this.attivitaComplementari = attivitaComplementari;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }
}