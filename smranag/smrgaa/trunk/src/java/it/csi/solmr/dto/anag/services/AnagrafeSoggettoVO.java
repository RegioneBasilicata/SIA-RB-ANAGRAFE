package it.csi.solmr.dto.anag.services;

import java.io.*;
import java.util.*;

/**
 * Value Object che mappa le tabelle DB_ANAGRAFICA_AZIENDA e DB_PERSONA_FISICA
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

public class AnagrafeSoggettoVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -8027944311894209297L;

  private String cuaa = null;
  private String partitaIva = null;
  private String denominazione = null;
  private String nome = null;
  private String sesso = null;
  private java.util.Date dataNascita = null;
  private java.util.Date dataMorte = null;
  private String comuneNascita = null;
  private String residenzaIndirizzo = null;
  private String residenzaComune = null;
  private String naturaGiuridica = null;
  private String codiceFiscaleRappLegale = null;
  private java.util.Date dataAggiornamento = null;

  public AnagrafeSoggettoVO() {
  }

  public String getCodiceFiscaleRappLegale() {
    return codiceFiscaleRappLegale;
  }

  public String getComuneNascita() {
    return comuneNascita;
  }

  public String getCuaa() {
    return cuaa;
  }

  public Date getDataMorte() {
    return dataMorte;
  }

  public Date getDataNascita() {
    return dataNascita;
  }

  public String getDenominazione() {
    return denominazione;
  }

  public String getNaturaGiuridica() {
    return naturaGiuridica;
  }

  public String getNome() {
    return nome;
  }

  public String getPartitaIva() {
    return partitaIva;
  }

  public String getResidenzaComune() {
    return residenzaComune;
  }

  public String getResidenzaIndirizzo() {
    return residenzaIndirizzo;
  }

  public String getSesso() {
    return sesso;
  }

  public Date getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setCodiceFiscaleRappLegale(String codiceFiscaleRappLegale) {
    this.codiceFiscaleRappLegale = codiceFiscaleRappLegale;
  }

  public void setComuneNascita(String comuneNascita) {
    this.comuneNascita = comuneNascita;
  }

  public void setCuaa(String cuaa) {
    this.cuaa = cuaa;
  }

  public void setDataMorte(Date dataMorte) {
    this.dataMorte = dataMorte;
  }

  public void setDataNascita(Date dataNascita) {
    this.dataNascita = dataNascita;
  }

  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }

  public void setNaturaGiuridica(String naturaGiuridica) {
    this.naturaGiuridica = naturaGiuridica;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setPartitaIva(String partitaIva) {
    this.partitaIva = partitaIva;
  }

  public void setResidenzaComune(String residenzaComune) {
    this.residenzaComune = residenzaComune;
  }

  public void setResidenzaIndirizzo(String residenzaIndirizzo) {
    this.residenzaIndirizzo = residenzaIndirizzo;
  }

  public void setSesso(String sesso) {
    this.sesso = sesso;
  }

  public void setDataAggiornamento(java.util.Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }
}
