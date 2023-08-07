package it.csi.solmr.dto.anag.services;

import java.io.*;
import java.util.*;

/**
 * Value Object utilizzato per visualizzare i dati catastali certificati di una particella ad una certa data
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

public class ParticellaCatastaleCertificataVO implements Serializable {
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -6655691786633272971L;

  private String superficieCatastale = null;
  private java.util.Date dataInizioValidita = null;
  private java.util.Date dataFineValidita = null;
  private String annotazioni = null;
  private String ideImmobile = null;
  private java.util.Date dataIdeMutazioneInizio = null;
  private java.util.Date dataIdeMutazioneFine = null;
  private String redditoAgrario = null;
  private String redditoDominicale = null;
  private String qualitaCatastale = null;
  private String classeTerreno = null;
  private String partita = null;

  public ParticellaCatastaleCertificataVO() {
  }

  public String getAnnotazioni() {
    return annotazioni;
  }

  public String getClasseTerreno() {
    return classeTerreno;
  }

  public Date getDataInizioValidita() {
    return dataInizioValidita;
  }

  public Date getDataFineValidita() {
    return dataFineValidita;
  }

  public Date getDataIdeMutazioneFine() {
    return dataIdeMutazioneFine;
  }

  public String getSuperficieCatastale() {
    return superficieCatastale;
  }

  public String getRedditoDominicale() {
    return redditoDominicale;
  }

  public String getRedditoAgrario() {
    return redditoAgrario;
  }

  public String getQualitaCatastale() {
    return qualitaCatastale;
  }

  public String getPartita() {
    return partita;
  }

  public String getIdeImmobile() {
    return ideImmobile;
  }

  public Date getDataIdeMutazioneInizio() {
    return dataIdeMutazioneInizio;
  }

  public void setAnnotazioni(String annotazioni) {
    this.annotazioni = annotazioni;
  }

  public void setClasseTerreno(String classeTerreno) {
    this.classeTerreno = classeTerreno;
  }

  public void setDataInizioValidita(Date dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
  }

  public void setDataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

  public void setDataIdeMutazioneFine(Date dataIdeMutazioneFine) {
    this.dataIdeMutazioneFine = dataIdeMutazioneFine;
  }

  public void setIdeImmobile(String ideImmobile) {
    this.ideImmobile = ideImmobile;
  }

  public void setPartita(String partita) {
    this.partita = partita;
  }

  public void setQualitaCatastale(String qualitaCatastale) {
    this.qualitaCatastale = qualitaCatastale;
  }

  public void setRedditoAgrario(String redditoAgrario) {
    this.redditoAgrario = redditoAgrario;
  }

  public void setRedditoDominicale(String redditoDominicale) {
    this.redditoDominicale = redditoDominicale;
  }

  public void setSuperficieCatastale(String superficieCatastale) {
    this.superficieCatastale = superficieCatastale;
  }

  public void setDataIdeMutazioneInizio(Date dataIdeMutazioneInizio) {
    this.dataIdeMutazioneInizio = dataIdeMutazioneInizio;
  }


}
