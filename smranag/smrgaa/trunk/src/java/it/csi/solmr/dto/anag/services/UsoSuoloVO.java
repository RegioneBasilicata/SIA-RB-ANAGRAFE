package it.csi.solmr.dto.anag.services;

import java.io.*;
import java.util.*;

/**
 * Value Object utilizzato per visualizzare i dati cardine relativi all'uso del suolo di una particella
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */

public class UsoSuoloVO implements Serializable {
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -7902760471571810951L;

  private String annoCampagna = null;
  private String superficieColtivata = null;
  private String codiceColtura = null;
  private java.util.Date dataFineConduzione = null;
  private java.util.Date dataUltimoAggiornamentoUtilizzo = null;

  public UsoSuoloVO() {
  }

  public String getAnnoCampagna() {
    return annoCampagna;
  }

  public String getCodiceColtura() {
    return codiceColtura;
  }

  public Date getDataFineConduzione() {
    return dataFineConduzione;
  }

  public Date getDataUltimoAggiornamentoUtilizzo() {
    return dataUltimoAggiornamentoUtilizzo;
  }

  public String getSuperficieColtivata() {
    return superficieColtivata;
  }

  public void setAnnoCampagna(String annoCampagna) {
    this.annoCampagna = annoCampagna;
  }

  public void setCodiceColtura(String codiceColtura) {
    this.codiceColtura = codiceColtura;
  }

  public void setDataFineConduzione(Date dataFineConduzione) {
    this.dataFineConduzione = dataFineConduzione;
  }

  public void setDataUltimoAggiornamentoUtilizzo(Date
          dataUltimoAggiornamentoUtilizzo) {
    this.dataUltimoAggiornamentoUtilizzo = dataUltimoAggiornamentoUtilizzo;
  }

  public void setSuperficieColtivata(String superficieColtivata) {
    this.superficieColtivata = superficieColtivata;
  }

}
