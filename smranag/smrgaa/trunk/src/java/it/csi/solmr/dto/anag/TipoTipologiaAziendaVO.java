package it.csi.solmr.dto.anag;

import java.io.Serializable;
import it.csi.solmr.util.*;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class TipoTipologiaAziendaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 2023219850126221590L;

  private String flagaziendaprovvisoria;
  private String idTipologiaAzienda;
  private String flagControlliUnivocita;
  private String descrizione;
  private String flagFormaAssociata;

  public TipoTipologiaAziendaVO()
  {
  }


  public ValidationErrors validate() {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public String getFlagaziendaprovvisoria() {
    return flagaziendaprovvisoria;
  }

  public void setFlagaziendaprovvisoria(String flagaziendaprovvisoria) {
    this.flagaziendaprovvisoria = flagaziendaprovvisoria;
  }



  public String getIdTipologiaAzienda() {
    return idTipologiaAzienda;
  }

  public void setIdTipologiaAzienda(String idTipologiaAzienda) {
    this.idTipologiaAzienda = idTipologiaAzienda;
  }

  public String getFlagControlliUnivocita() {
    return flagControlliUnivocita;
  }

  public void setFlagControlliUnivocita(String flagControlliUnivocita) {
    this.flagControlliUnivocita = flagControlliUnivocita;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }


  public String getFlagFormaAssociata()
  {
    return flagFormaAssociata;
  }


  public void setFlagFormaAssociata(String flagFormaAssociata)
  {
    this.flagFormaAssociata = flagFormaAssociata;
  }
}