package it.csi.solmr.dto.anag.services;

import java.io.*;
import java.util.*;

/**
 * Value Object utilizzato per visualizzare i dati catastali e della conduzione di una particella
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

public class ParticellaConduzioneVO implements Serializable {
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 9018411454901775251L;

  private String istatComune = null;
  private String sezione = null;
  private String foglio = null;
  private String particella = null;
  private String subalterno = null;
  private String superficieCatastale = null;
  private String idTitoloPossesso = null;
  private String descrizioneTitoloPossesso = null;
  private String superficieCondotta = null;
  private java.util.Date dataInizioConduzione = null;
  private java.util.Date dataFineConduzione = null;
  private java.util.Date dataAggiornamento = null;

  public ParticellaConduzioneVO() {
  }

  public Date getDataAggiornamento() {
    return dataAggiornamento;
  }

  public Date getDataFineConduzione() {
    return dataFineConduzione;
  }

  public Date getDataInizioConduzione() {
    return dataInizioConduzione;
  }

  public String getDescrizioneTitoloPossesso() {
    return descrizioneTitoloPossesso;
  }

  public String getIdTitoloPossesso() {
    return idTitoloPossesso;
  }

  public String getFoglio() {
    return foglio;
  }

  public String getIstatComune() {
    return istatComune;
  }

  public String getParticella() {
    return particella;
  }

  public String getSubalterno() {
    return subalterno;
  }

  public String getSezione() {
    return sezione;
  }

  public String getSuperficieCatastale() {
    return superficieCatastale;
  }

  public String getSuperficieCondotta() {
    return superficieCondotta;
  }

  public void setSuperficieCondotta(String superficieCondotta) {
    this.superficieCondotta = superficieCondotta;
  }

  public void setSuperficieCatastale(String superficieCatastale) {
    this.superficieCatastale = superficieCatastale;
  }

  public void setSubalterno(String subalterno) {
    this.subalterno = subalterno;
  }

  public void setSezione(String sezione) {
    this.sezione = sezione;
  }

  public void setParticella(String particella) {
    this.particella = particella;
  }

  public void setIstatComune(String istatComune) {
    this.istatComune = istatComune;
  }

  public void setIdTitoloPossesso(String idTitoloPossesso) {
    this.idTitoloPossesso = idTitoloPossesso;
  }

  public void setFoglio(String foglio) {
    this.foglio = foglio;
  }

  public void setDescrizioneTitoloPossesso(String descrizioneTitoloPossesso) {
    this.descrizioneTitoloPossesso = descrizioneTitoloPossesso;
  }

  public void setDataInizioConduzione(Date dataInizioConduzione) {
    this.dataInizioConduzione = dataInizioConduzione;
  }

  public void setDataFineConduzione(Date dataFineConduzione) {
    this.dataFineConduzione = dataFineConduzione;
  }

  public void setDataAggiornamento(Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

}
