package it.csi.solmr.dto.anag;
/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author
 * @version 1.0
 */

import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.AbstractValueObject;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;

public class PersonaFisicaVO extends AbstractValueObject implements Serializable, Comparable<Object>
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -6184819366683977776L;

  private Long idPersonaFisica = null;
  private Long idSoggetto = null;
  private String codiceFiscale = null;
  private String cognome = null;
  private String nome = null;
  private String sesso = null;
  private String nascitaComune = null;
  private String descNascitaComune = null;
  private java.util.Date nascitaData = null;
  private String resIndirizzo = null;
  private String resComune = null;
  private String descResComune = null;
  private String nascitaProv = null;
  private String flagEstero = null;
  private String resCAP = null;
  private String resTelefono = null;
  private String resFax = null;
  private String numeroCellulare = null;
  private String resMail = null;
  private String desPrefissoCellulareInt = null;
  private Long idPrefissoCellulareNaz = null;
  private String desPrefissoCellulareNan = null;
  private String desNumeroCellulare = null;
  private String note = null;
  private java.util.Date dataAggiornamento = null;
  private Long idUtenteAggiornamento = null;
  private Long idAzienda = null;
  private String luogoNascita = null;
  private java.util.Date dataInizioRuolo = null;
  private java.util.Date dataFineRuolo = null;
  private java.util.Date dataInizioRuoloMod = null;
  private java.util.Date dataFineRuoloMod = null;
  private String descResProvincia = null;
  private String statoEsteroRes = null;
  private String domIndirizzo;
  private String domComune;
  private String istatComuneDomicilio = null;
  private String domProvincia;
  private String domCAP;
  private String resCittaEstero;
  private String nascitaCittaEstero;
  private boolean newPersonaFisica = true;
  private String strNascitaData;
  private String resProvincia;
  private String nascitaStatoEstero;
  private String nascitaFlagEstero;
  private String descStatoEsteroResidenza = null;
  private String cittaNascita = null;
  private String cittaResidenza = null;
  private String istatEsteroNascita = null;
  private String ruolo = null;
  private Long idRuolo = null;
  private String descUtenteAggiornamento = null;
  private Long tipiRuoloNonTitolare = null;
  private String strDataInizioRuolo = null;
  private String strDataFineRuolo = null;
  private String strDataInizioRuoloMod = null;
  private String strDataFineRuoloMod = null;
  private Long idContitolare = null;
  private Long idTitoloStudio = null;
  private String descrizioneTitoloStudio = null;
  private Long idIndirizzoStudio = null;
  private String descrizioneIndirizzoStudio = null;
  private String descCittaEsteroDomicilio = null;
  private String domicilioFlagEstero = null;
  private String domicilioStatoEstero = null;
  private Long tipiRuoloNonTitolareAndNonSpecificato = null; // Attributo che non mappa nessun campo di nessuna tabella del DB.Usato solo per l'applicativo
  private java.util.Date dataInizioResidenza;
  private String strDataInizioResidenza;
  private boolean isModificaResidenza = false; // Attributo che non mappa nessun campo delle tabelle sul DB. Usato solo per logica applicativo
  private boolean isStoricizzaResidenza = false; // Attributo che non mappa nessun campo delle tabelle sul DB. Usato solo per logica applicativo
  private String flagCfOk = null;
  private String codiceRuoloAAEP; // parametro relativo al campo FLAG_CF_OK su DB_PERSONA_FISICA

  public PersonaFisicaVO() {
  }

  public int hashCode() {
    return (idPersonaFisica == null ? 0 : idPersonaFisica.hashCode())+
            (idSoggetto == null ? 0 : idSoggetto.hashCode())+
            (codiceFiscale == null ? 0 : codiceFiscale.hashCode())+
            (cognome == null ? 0 : cognome.hashCode())+
            (nome == null ? 0 : nome.hashCode())+
            (sesso == null ? 0 : sesso.hashCode())+
            (nascitaComune == null ? 0 : nascitaComune.hashCode())+
            (descNascitaComune == null ? 0 : descNascitaComune.hashCode())+
            (nascitaData == null ? 0 : nascitaData.hashCode())+
            (resIndirizzo == null ? 0 : resIndirizzo.hashCode())+
            (resComune == null ? 0 : resComune.hashCode())+
            (descResComune == null ? 0 : descResComune.hashCode())+
            (nascitaProv == null ? 0 : nascitaProv.hashCode())+
            (flagEstero == null ? 0 : flagEstero.hashCode())+
            (resCAP == null ? 0 : resCAP.hashCode())+
            (resTelefono == null ? 0 : resTelefono.hashCode())+
            (resFax == null ? 0 : resFax.hashCode())+
                        (numeroCellulare == null ? 0 : numeroCellulare.hashCode())+
            (resMail == null ? 0 : resMail.hashCode())+
            (note == null ? 0 : note.hashCode())+
            (dataAggiornamento == null ? 0 : dataAggiornamento.hashCode())+
            (idUtenteAggiornamento == null ? 0 : idUtenteAggiornamento.hashCode())+
            (idAzienda == null ? 0 : idAzienda.hashCode())+
            (luogoNascita == null ? 0 : luogoNascita.hashCode())+
            (dataInizioRuolo == null ? 0 : dataInizioRuolo.hashCode())+
            (strDataInizioRuolo == null ? 0 : strDataInizioRuolo.hashCode())+
            (dataFineRuolo == null ? 0 : dataFineRuolo.hashCode())+
            (strDataFineRuolo == null ? 0 : strDataFineRuolo.hashCode())+
            (descResProvincia == null ? 0 : descResProvincia.hashCode())+
            (statoEsteroRes == null ? 0 : statoEsteroRes.hashCode())+
            (nascitaFlagEstero == null ? 0 : nascitaFlagEstero.hashCode())+
            (nascitaStatoEstero==null ? 0 : nascitaStatoEstero.hashCode())+
            (descStatoEsteroResidenza == null ? 0 : descStatoEsteroResidenza.hashCode())+
            (resCittaEstero==null ? 0 : resCittaEstero.hashCode())+
            (nascitaCittaEstero == null ? 0 : nascitaCittaEstero.hashCode())+
            (ruolo == null ? 0 : ruolo.hashCode())+
            (idRuolo == null? 0 : idRuolo.hashCode())+
            (descUtenteAggiornamento == null? 0 :descUtenteAggiornamento.hashCode())+
            (idContitolare == null? 0 : idContitolare.hashCode())+
            (idTitoloStudio == null? 0 : idTitoloStudio.hashCode())+
            (descrizioneTitoloStudio == null? 0 : descrizioneTitoloStudio.hashCode())+
            (idIndirizzoStudio == null? 0 : idIndirizzoStudio.hashCode())+
            (descrizioneIndirizzoStudio == null? 0 : descrizioneIndirizzoStudio.hashCode())+
            (istatComuneDomicilio == null? 0 : istatComuneDomicilio.hashCode())+
            (descCittaEsteroDomicilio == null? 0 : descCittaEsteroDomicilio.hashCode())+
            (domicilioFlagEstero == null? 0 : domicilioFlagEstero.hashCode())+
            (domicilioStatoEstero == null? 0 : domicilioStatoEstero.hashCode())+
            (tipiRuoloNonTitolareAndNonSpecificato == null? 0 : tipiRuoloNonTitolareAndNonSpecificato.hashCode())+
            (flagCfOk == null? 0 : flagCfOk.hashCode());
  }

  public boolean equals(Object o) {
    if (o instanceof PersonaFisicaVO) {
      PersonaFisicaVO other = (PersonaFisicaVO)o;
      return (this.idPersonaFisica == null && other.idPersonaFisica == null || this.idPersonaFisica.equals(other.idPersonaFisica) &&
              this.idSoggetto == null && other.idSoggetto == null || this.idSoggetto.equals(other.idSoggetto) &&
              this.codiceFiscale == null && other.codiceFiscale == null || this.codiceFiscale.equals(other.codiceFiscale) &&
              this.cognome == null && other.cognome == null || this.cognome.equals(other.cognome) &&
              this.nome == null && other.nome == null || this.nome.equals(other.nome) &&
              this.descNascitaComune == null && other.descNascitaComune == null || this.descNascitaComune.equals(other.descNascitaComune) &&
              this.sesso == null && other.sesso == null || this.sesso.equals(other.sesso) &&
              this.nascitaComune == null && other.nascitaComune == null || this.nascitaComune.equals(other.nascitaComune) &&
              this.nascitaData == null && other.nascitaData == null || this.nascitaData.equals(other.nascitaData) &&
              this.resIndirizzo == null && other.resIndirizzo == null || this.resIndirizzo.equals(other.resIndirizzo) &&
              this.resComune == null && other.resComune == null || this.resComune.equals(other.resComune) &&
              this.descResComune == null && other.descResComune == null || this.descResComune.equals(other.descResComune) &&
              this.nascitaProv == null && other.nascitaProv == null || this.nascitaProv.equals(other.nascitaProv) &&
              this.flagEstero == null && other.flagEstero == null || this.flagEstero.equals(other.flagEstero) &&
              this.resCAP == null && other.resCAP == null || this.resCAP.equals(other.resCAP) &&
              this.resTelefono == null && other.resTelefono == null || this.resTelefono.equals(other.resTelefono) &&
              this.resFax == null && other.resFax == null || this.resFax.equals(other.resFax) &&
                          this.numeroCellulare == null && other.numeroCellulare == null || this.numeroCellulare.equals(other.numeroCellulare) &&
              this.resMail == null && other.resMail == null || this.resMail.equals(other.resMail) &&
              this.note == null && other.note == null || this.note.equals(other.note) &&
              this.dataAggiornamento == null && other.dataAggiornamento == null || this.dataAggiornamento.equals(other.dataAggiornamento) &&
              this.idUtenteAggiornamento == null && other.idUtenteAggiornamento == null || this.idUtenteAggiornamento.equals(other.idUtenteAggiornamento) &&
              this.idAzienda == null && other.idAzienda == null || this.idAzienda.equals(other.idAzienda) &&
              this.luogoNascita == null && other.luogoNascita == null || this.luogoNascita.equals(other.luogoNascita) &&
              this.dataInizioRuolo == null && other.dataInizioRuolo == null || this.dataInizioRuolo.equals(other.dataInizioRuolo) &&
              this.strDataInizioRuolo == null && other.strDataInizioRuolo == null || this.strDataInizioRuolo.equals(other.strDataInizioRuolo) &&
              this.dataFineRuolo == null && other.dataFineRuolo == null || this.dataFineRuolo.equals(other.dataFineRuolo) &&
              this.strDataFineRuolo == null && other.strDataFineRuolo == null || this.strDataFineRuolo.equals(other.strDataFineRuolo) &&
              this.descResProvincia == null && other.descResProvincia == null || this.descResProvincia.equals(other.descResProvincia) &&
              this.nascitaStatoEstero== null && other.nascitaStatoEstero == null || this.nascitaStatoEstero.equals(other.nascitaStatoEstero) &&
              this.nascitaFlagEstero== null && other.nascitaFlagEstero == null || this.nascitaFlagEstero.equals(other.nascitaFlagEstero) &&
              this.statoEsteroRes == null && other.statoEsteroRes == null || this.statoEsteroRes.equals(other.statoEsteroRes) &&
              this.descStatoEsteroResidenza == null && other.descStatoEsteroResidenza == null || this.descStatoEsteroResidenza.equals(other.descStatoEsteroResidenza) &&
              this.ruolo == null && other.ruolo== null || this.ruolo.equals(other.ruolo)&&
              this.idRuolo == null && other.idRuolo == null || this.idRuolo.equals(other.idRuolo)&&
              this.descUtenteAggiornamento == null && other.descUtenteAggiornamento == null || this.descUtenteAggiornamento.equals(other.descUtenteAggiornamento)&&
              this.idContitolare == null && other.idContitolare == null || this.idContitolare.equals(other.idContitolare) &&
              this.idTitoloStudio == null && other.idTitoloStudio == null || this.idTitoloStudio.equals(other.idTitoloStudio) &&
              this.descrizioneTitoloStudio == null && other.descrizioneTitoloStudio == null || this.descrizioneTitoloStudio.equals(other.descrizioneTitoloStudio) &&
              this.idIndirizzoStudio == null && other.idIndirizzoStudio == null || this.idIndirizzoStudio.equals(other.idIndirizzoStudio) &&
              this.descrizioneIndirizzoStudio == null && other.descrizioneIndirizzoStudio == null || this.descrizioneIndirizzoStudio.equals(other.descrizioneIndirizzoStudio) &&
              this.istatComuneDomicilio == null && other.istatComuneDomicilio == null || this.istatComuneDomicilio.equals(other.istatComuneDomicilio) &&
              this.descCittaEsteroDomicilio == null && other.descCittaEsteroDomicilio == null || this.descCittaEsteroDomicilio.equals(other.descCittaEsteroDomicilio) &&
              this.domicilioFlagEstero == null && other.domicilioFlagEstero == null || this.domicilioFlagEstero.equals(other.domicilioFlagEstero) &&
              this.domicilioStatoEstero == null && other.domicilioStatoEstero == null || this.domicilioStatoEstero.equals(other.domicilioStatoEstero) &&
              this.tipiRuoloNonTitolareAndNonSpecificato == null && other.tipiRuoloNonTitolareAndNonSpecificato == null || this.tipiRuoloNonTitolareAndNonSpecificato.equals(other.tipiRuoloNonTitolareAndNonSpecificato) &&
              this.flagCfOk == null && other.flagCfOk == null || this.flagCfOk.equals(other.flagCfOk));
    } else
      return false;
  }

  // Metodo per controllare se due persone sono uguali in relazione a indirizzo, provincia, comune, cap
  // stato estero, città estero
  public boolean equalsResidenza(Object o) {
    if (o instanceof PersonaFisicaVO) {
      PersonaFisicaVO other = (PersonaFisicaVO)o;
      return (confrontaStr(resIndirizzo,other.resIndirizzo) &&
              confrontaStr(resProvincia,other.resProvincia) &&
              confrontaStr(resComune,other.resComune) &&
              confrontaStr(resCAP,other.resCAP) &&
              confrontaStr(resCittaEstero,other.resCittaEstero));
    }
    else {
      return false;
    }
  }


  private boolean confrontaStr(String str1,String str2)
  {
    if (str1==null)
    {
      if (str2==null || "".equals(str2.trim())) return true;
      else return false;
    }
    else
    {
      if ("".equals(str1.trim()))
      {
        if (str2==null || "".equals(str2.trim())) return true;
        else return false;
      }
      else
      {
        if (str2==null) return false;
        if (str1.trim().equalsIgnoreCase(str2.trim())) return true;
      }
      return false;
    }
  }


  public Long getIdPersonaFisica() {
    return idPersonaFisica;
  }
  public void setIdPersonaFisica(Long idPersonaFisica) {
    this.idPersonaFisica = idPersonaFisica;
  }
  public void setIdSoggetto(Long idSoggetto) {
    this.idSoggetto = idSoggetto;
  }
  public Long getIdSoggetto() {
    return idSoggetto;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }
  public String getCognome() {
    return cognome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }
  public String getNome() {
    return nome;
  }
  public void setSesso(String sesso) {
    this.sesso = sesso;
  }
  public String getSesso() {
    return sesso;
  }
  public void setNascitaComune(String nascitaComune) {
    this.nascitaComune = nascitaComune;
  }
  public String getNascitaComune() {
    return nascitaComune;
  }
  public void setDescNascitaComune(String descNascitaComune) {
    this.descNascitaComune = descNascitaComune;
  }
  public String getDescNascitaComune() {
    return descNascitaComune;
  }
  public void setNascitaData(java.util.Date nascitaData) {
    this.nascitaData = nascitaData;
  }
  public java.util.Date getNascitaData() {
    return nascitaData;
  }
  public void setResIndirizzo(String resIndirizzo) {
    this.resIndirizzo = resIndirizzo;
  }
  public String getResIndirizzo() {
    return resIndirizzo;
  }
  public void setResComune(String resComune) {
    this.resComune = resComune;
  }
  public String getResComune() {
    return resComune;
  }
  public void setDescResComune(String descResComune) {
    this.descResComune = descResComune;
  }
  public String getDescResComune() {
    return descResComune;
  }
  public void setNascitaProv(String nascitaProv) {
    this.nascitaProv = nascitaProv;
  }
  public String getNascitaProv() {
    return nascitaProv;
  }
  public void setFlagEstero(String flagEstero) {
    this.flagEstero = flagEstero;
  }
  public String getFlagEstero() {
    return flagEstero;
  }
  public void setResCAP(String resCAP) {
    this.resCAP = resCAP;
  }
  public String getResCAP() {
    return resCAP;
  }
  public void setResTelefono(String resTelefono) {
    this.resTelefono = resTelefono;
  }
  public String getResTelefono() {
    return resTelefono;
  }
  public void setResFax(String resFax) {
    this.resFax = resFax;
  }
  public String getResFax() {
    return resFax;
  }
  public void setNumeroCellulare(String numeroCellulare) {
    this.numeroCellulare = numeroCellulare;
  }
  public String getNumeroCellulare() {
        return numeroCellulare;
  }
  public void setResMail(String resMail) {
    this.resMail = resMail;
  }
  public String getResMail() {
    return resMail;
  }
  public void setdesPrefissoCellulareInt(String prefissoInt){
          this.desPrefissoCellulareInt = prefissoInt;
  }
  public String getdesPrefissoCellulareInt(){
          return this.desPrefissoCellulareInt;
  }
  public void setIdPrefissoCellulareNaz(Long prefissoNaz){
          this.idPrefissoCellulareNaz = prefissoNaz;
  }
  public Long getIdPrefissoCellulareNaz(){
          return this.idPrefissoCellulareNaz;
  }
  public void setdesPrefissoCellulareNaz(String PrefissoCellulareNaz){
          this.desPrefissoCellulareNan = PrefissoCellulareNaz;
  }
  public String getdesPrefissoCellulareNaz(){
          return this.desPrefissoCellulareNan;
  }
  public void setdesNumeroCellulare(String numeroCellulare){
          this.desNumeroCellulare = numeroCellulare;
  }
  public String getdesNumeroCellulare(){
          return this.desNumeroCellulare;
  }
  public void setNote(String note) {
    this.note = note;
  }
  public String getNote() {
    return note;
  }
  public void setDataAggiornamento(java.util.Date dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }
  public java.util.Date getDataAggiornamento() {
    return dataAggiornamento;
  }
  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public Long getIdUtenteAggiornamento() {
    return idUtenteAggiornamento;
  }
  public void setIdAzienda(Long idAzienda) {
    this.idAzienda = idAzienda;
  }
  public Long getIdAzienda() {
    return idAzienda;
  }
  public void setLuogoNascita(String luogoNascita) {
    this.luogoNascita = luogoNascita;
  }
  public String getLuogoNascita() {
    return luogoNascita;
  }
  public void setDataInizioRuolo(java.util.Date dataInizioRuolo) {
    this.dataInizioRuolo = dataInizioRuolo;
  }
  public java.util.Date getDataInizioRuolo() {
    return dataInizioRuolo;
  }
  public void setDataFineRuolo(java.util.Date dataFineRuolo) {
    this.dataFineRuolo = dataFineRuolo;
  }
  public java.util.Date getDataFineRuolo() {
    return dataFineRuolo;
  }
  public void setDescResProvincia(String descResProvincia) {
    this.descResProvincia = descResProvincia;
  }
  public String getDescResProvincia() {
    return descResProvincia;
  }
  public void setStatoEsteroRes(String statoEsteroRes) {
    this.statoEsteroRes = statoEsteroRes;
  }
  public String getStatoEsteroRes() {
    return statoEsteroRes;
  }
  public void setDomIndirizzo(String domIndirizzo) {
    this.domIndirizzo = domIndirizzo;
  }
  public String getDomIndirizzo() {
    return domIndirizzo;
  }
  public void setDomComune(String domComune) {
    this.domComune = domComune;
  }
  public String getDomComune() {
    return domComune;
  }
  public void setDomProvincia(String domProvincia) {
    this.domProvincia = domProvincia;
  }
  public String getDomProvincia() {
    return domProvincia;
  }
  public void setDomCAP(String domCAP) {
    this.domCAP = domCAP;
  }
  public String getDomCAP() {
    return domCAP;
  }
  public boolean isNewPersonaFisica() {
    return newPersonaFisica;
  }
  public void setNewPersonaFisica(boolean newPersonaFisica) {
    this.newPersonaFisica = newPersonaFisica;
  }
  public void setDescStatoEsteroResidenza(String descStatoEsteroResidenza) {
    this.descStatoEsteroResidenza = descStatoEsteroResidenza;
  }
  public String getDescStatoEsteroResidenza() {
    return descStatoEsteroResidenza;
  }
  public String getDescCittaEsteroDomicilio() {
    return descCittaEsteroDomicilio;
  }
  public void setDescCittaEsteroDomicilio(String descCittaEsteroDomicilio) {
    this.descCittaEsteroDomicilio = descCittaEsteroDomicilio;
  }
  public String getDomicilioFlagEstero() {
    return domicilioFlagEstero;
  }
  public void setDomicilioFlagEstero(String domicilioFlagEstero) {
    this.domicilioFlagEstero = domicilioFlagEstero;
  }
  public String getDomicilioStatoEstero() {
    return domicilioStatoEstero;
  }
  public void setDomicilioStatoEstero(String domicilioStatoEstero) {
    this.domicilioStatoEstero = domicilioStatoEstero;
  }
  public boolean isModificaResidenza() {
    return isModificaResidenza;
  }
  public void setIsModificaResidenza(boolean isModificaResidenza) {
    this.isModificaResidenza = isModificaResidenza;
  }
  public boolean isStoricizzaResidenza() {
    return isStoricizzaResidenza;
  }
  public void setIsStoricizzaResidenza(boolean isStoricizzaResidenza) {
    this.isStoricizzaResidenza = isStoricizzaResidenza;
  }
  public String getFlagCfOk() {
    return flagCfOk;
  }
  public void setFlagCfOk(String flagCfOk) {
    this.flagCfOk = flagCfOk;
  }



  public void setStrNascitaData(String strNascitaData) {
    this.strNascitaData = strNascitaData;
  }
  public String getStrNascitaData() {
    return strNascitaData;
  }
  public void setResProvincia(String resProvincia) {
    this.resProvincia = resProvincia;
  }
  public String getResProvincia() {
    return resProvincia;
  }

  
  // Metodo per controllare se è stata cambiata l'identità della persona attraverso un tentativo di
  // bonifica
  public boolean isEqualIdentity(Object o) 
  {
    boolean isEqual = true;
    if(o instanceof PersonaFisicaVO) {
      PersonaFisicaVO other = (PersonaFisicaVO)o;
      if(Validator.isNotEmpty(this.cognome) && Validator.isNotEmpty(other.cognome)) {
        if(!this.cognome.equalsIgnoreCase(other.cognome)) {
          isEqual = false;
        }
      }
      if(Validator.isNotEmpty(this.nome) && Validator.isNotEmpty(other.nome)) {
        if(!this.nome.equalsIgnoreCase(other.nome)) {
          isEqual = false;
        }
      }
      if(Validator.isNotEmpty(this.strNascitaData) && Validator.isNotEmpty(other.strNascitaData)) {
        if(this.strNascitaData.compareTo(other.strNascitaData) != 0) {
          isEqual = false;
        }
      }
      if(Validator.isNotEmpty(this.nascitaComune) && Validator.isNotEmpty(other.nascitaComune)) {
        if(!this.nascitaComune.equalsIgnoreCase(other.nascitaComune)) {
          isEqual = false;
        }
      }
      if(Validator.isNotEmpty(this.nascitaStatoEstero) && Validator.isNotEmpty(other.nascitaStatoEstero)) {
        if(!this.nascitaStatoEstero.equalsIgnoreCase(other.nascitaStatoEstero)) {
          isEqual = false;
        }
      }
    }
    return isEqual;
  }

  public ValidationErrors validate() {
    ValidationErrors errors = new ValidationErrors();

    if (!Validator.isNotEmpty(cognome))
      errors.add("cognome", new ValidationError("Il Cognome deve essere valorizzato!"));

    if (!Validator.isNotEmpty(nome))
      errors.add("nome", new ValidationError("Il Nome deve essere valorizzato!"));

    if (!Validator.isNotEmpty(sesso))
      errors.add("sesso", new ValidationError("Il Sesso deve essere valorizzato!"));

    if (!Validator.isNotEmpty(strNascitaData))
      errors.add("strNascitaData", new ValidationError("La Data di Nascita deve essere valorizzata!"));
    else {
      try {
        nascitaData = DateUtils.parseDate(strNascitaData);
      } catch (Exception ex) {
        errors.add("strNascitaData", new ValidationError("La data di nascita inserita non è corretta!"));
      }
    }

    if (!Validator.isNotEmpty(descNascitaComune))
      errors.add("descNascitaComune", new ValidationError("Il Luogo di Nascita deve essere valorizzato!"));

    if (!Validator.isNotEmpty(resIndirizzo))
      errors.add("resIndirizzo", new ValidationError("L'Indirizzo deve essere valorizzato!"));

    if (!Validator.isNotEmpty(statoEsteroRes)) {
      if (!Validator.isNotEmpty(resCAP))
        errors.add("resCAP", new ValidationError("Se lo Stato Estero non è valorizzato, il CAP è obbligatorio!"));
      if (!Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError("Se lo Stato Estero non è valorizzato, il Comune è obbligatorio!"));
      //if (!Validator.isNotEmpty(resComune))
        //errors.add("descResComune", new ValidationError("Se lo Stato Estero non è valorizzato, il Comune è obbligatorio!"));
      if (!Validator.isNotEmpty(resProvincia))
        errors.add("descResComune", new ValidationError("Se lo Stato Estero non è valorizzato, la Provincia è obbligatoria!"));
      if (!Validator.isNotEmpty(resCAP)&&
          !Validator.isNotEmpty(descResComune)&&
          !Validator.isNotEmpty(resProvincia))
        errors.add("statoEsteroRes", new ValidationError("Se Provincia, Comune e CAP non sono valorizzati, lo Stato Estero è obbligatorio!"));
    } else {
      if (Validator.isNotEmpty(resCAP))
        errors.add("resCAP", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      if (Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      //if (Validator.isNotEmpty(resComune))
        //errors.add("descResComune", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      if (Validator.isNotEmpty(resProvincia))
        errors.add("resProvincia", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));

      errors.add("statoEsteroRes", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
    }
    if (!Validator.isNotEmpty(codiceFiscale))
      errors.add("codiceFiscale", new ValidationError("Il Codice Fiscale deve essere valorizzato!"));
    else if (!Validator.controlloCf(codiceFiscale))
      errors.add("codiceFiscale", new ValidationError("Inserire un Codice Fiscale corretto!"));
    else if (!Validator.controlloCf(nome, cognome, sesso, nascitaData, "", codiceFiscale))
      errors.add("codiceFiscale", new ValidationError(AnagErrors.NB_CF_SBAGLIATO));

    if (!Validator.isValidEmail(resMail))
      errors.add("resMail", new ValidationError("La Mail inserita non è corretta!"));

    return errors;
  }


  public ValidationErrors validateNuovoRappresentanteLegale() {

    ValidationErrors errors = new ValidationErrors();
    // Il cognome è obbligatorio
    if (!Validator.isNotEmpty(cognome))
      errors.add("cognome", new ValidationError(AnagErrors.ERR_COGNOME_TITOLARE_OBBLIGATORIO));
    // Il nome è obbligatorio
    if (!Validator.isNotEmpty(nome))
      errors.add("nome", new ValidationError(AnagErrors.ERR_NOME_TITOLARE_OBBLIGATORIO));
    // Il sesso è obbligatorio
    if (!Validator.isNotEmpty(sesso))
      errors.add("sesso", new ValidationError(AnagErrors.ERR_SESSO_TITOLARE_OBBLIGATORIO));
    // Il campo data di nascita è obbligatorio
    if (!Validator.isNotEmpty(strNascitaData))
      errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_OBBLIGATORIA));
    else 
    {
      if(strNascitaData.length() != 10) 
      {
        errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(strNascitaData)) 
      {
        errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
      else 
      {
        try 
        {
          nascitaData = DateUtils.parseDate(strNascitaData);
        }
        catch (Exception ex) 
        {
          errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
        }
      }
    }
    // Il luogo di nascita è obbligatorio
    if(!Validator.isNotEmpty(luogoNascita) && !Validator.isNotEmpty(nascitaProv)) 
    {
      if(!Validator.isNotEmpty(nascitaStatoEstero)) 
      {
        errors.add("nascitaStatoEstero", new ValidationError(AnagErrors.ERR_STATO_ESTERO_NASCITA_TITOLARE_OBBLIGATORIO));
      }
    }
    else 
    {
      if(!Validator.isNotEmpty(luogoNascita)) 
      {
        errors.add("descNascitaComune", new ValidationError(AnagErrors.ERR_COMUNE_NASCITA_TITOLARE_OBBLIGATORIO));
      }
      if(!Validator.isNotEmpty(nascitaProv)) 
      {
        errors.add("nascitaProv", new ValidationError(AnagErrors.ERR_PROVINCIA_NASCITA_TITOLARE_OBBLIGATORIA));
      }
      if(istatEsteroNascita != null) 
      {
        if(!istatEsteroNascita.equals(SolmrConstants.ISTAT_STATO_ESTERO)) 
        {
          if(Validator.isNotEmpty(cittaNascita)) 
          {
            errors.add("cittaNascita", new ValidationError(AnagErrors.ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA));
          }
        }
      }
      if(!Validator.isNotEmpty(nascitaStatoEstero)) 
      {
        if(Validator.isNotEmpty(cittaNascita)) 
        {
          errors.add("cittaNascita", new ValidationError(AnagErrors.ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA));
        }
      }
      if(Validator.isNotEmpty(nascitaStatoEstero)) 
      {
        errors.add("nascitaStatoEstero", new ValidationError(AnagErrors.ERR_STATO_ESTERO_NASCITA_TITOLARE_ERRATO));
      }
    }
    // L'indirizzo è obbligatorio
    if (!Validator.isNotEmpty(resIndirizzo))
      errors.add("resIndirizzo", new ValidationError(AnagErrors.ERR_INDIRIZZO_RESIDENZA_TITOLARE_OBBLIGATORIO));

    if (!Validator.isNotEmpty(descStatoEsteroResidenza)) 
    {
      if (Validator.isEmpty(resCAP)) 
      {
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_OBBLIGATORIO));
      }
      else 
      {
        if(!Validator.isCapOk(resCAP)) 
        {
          errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_ERRATO));
        }
      }
      
      if (!Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_OBBLIGATORIO));
      if (!Validator.isNotEmpty(this.resProvincia))
        errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_OBBLIGATORIO));
      if (!Validator.isNotEmpty(resCAP) && !Validator.isNotEmpty(descResComune) && !Validator.isNotEmpty(resProvincia)) {
        errors.add("descStatoEsteroResidenza", new ValidationError(AnagErrors.ERR_STATO_ESTERO_RESIDENZA_TITOLARE_OBBLIGATORIO));
      }
      if (Validator.isNotEmpty(cittaResidenza)) {
        errors.add("cittaResidenza", new ValidationError(AnagErrors.ERR_CITTA_ESTERA_RESIDENZA_TITOLARE_ERRATA));
      }
    }
    else
    {
      if (Validator.isNotEmpty(resCAP))
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
      if (Validator.isNotEmpty(resProvincia))
        errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
      if (Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
    }
    if (Validator.isNotEmpty(resMail) && !Validator.isValidEmail(resMail)) {
      errors.add("resMail", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
    }
    // Controlli relativi al domicilio
    if(!Validator.isNotEmpty(domicilioStatoEstero)) 
    {
      // Se è stata valorizzata la provincia controllo che sia stato valorizzato il comune
      if(Validator.isNotEmpty(domProvincia)) {
        if(!Validator.isNotEmpty(domComune)) {
          errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_COMUNE_DOMICILIO_OBBLIGATORIO")));
        }
        if(!Validator.isNotEmpty(domCAP)) {
          errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_CAP_DOMICILIO_OBBLIGATORIO")));
        }
      }
      if(Validator.isNotEmpty(domComune)) {
        if(!Validator.isNotEmpty(domProvincia)) {
          errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_PROVINCIA_DOMICILIO_OBBLIGATORIA")));
        }
        if(Validator.isEmpty(domCAP)) 
        {
          errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_CAP_DOMICILIO_OBBLIGATORIO")));
        }
        else 
        {
          if(!Validator.isCapOk(domCAP)) 
          {
            errors.add("domCAP", new ValidationError(AnagErrors.ERR_CAP_DOMICILIO_ERRATO));
          }
        }
      }
      if(Validator.isNotEmpty(domCAP)) 
      {
        if(!Validator.isNotEmpty(domComune)) {
          errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_COMUNE_DOMICILIO_OBBLIGATORIO_FOR_CAP")));
        }
        if(!Validator.isNotEmpty(domProvincia)) {
          errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_PROVINCIA_DOMICILIO_OBBLIGATORIA_FOR_CAP")));
        }
      }
      if(Validator.isNotEmpty(descCittaEsteroDomicilio)) {
        errors.add("descCittaEsteroDomicilio", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_STATO_ESTERO_NO_VALORIZZABILE")));
      }
    }
    else {
      if(Validator.isNotEmpty(domProvincia)) {
        errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_PROVINCIA_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domComune)) {
        errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_COMUNE_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domCAP)) {
        errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_CAP_NO_VALORIZZABILE")));
      }
    }
    
    if(Validator.isNotEmpty(numeroCellulare)) 
    {
      if(numeroCellulare.length() < 9) 
      {
        errors.add("numeroCellulare", new ValidationError(AnagErrors.ERR_NUM_CELLULARE));
      }
    }

    return errors;
  }

  // Metodo per effettuare la validazione dei dati richiamato in modifica rappresentante legale
  public ValidationErrors validateModificaNuovoRappresentanteLegale() 
  {

    ValidationErrors errors = new ValidationErrors();

    // Il codice fiscale è obbligatorio
    if(!Validator.isNotEmpty(codiceFiscale)) {
      errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_OBBLIGATORIO")));
    }
    // Se è stato valorizzato controllo che sia corretto
    else {
      if(!Validator.controlloCf(codiceFiscale)) {
        errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE")));
      }
    }

    if(!Validator.isNotEmpty(cognome)) {
      errors.add("cognome", new ValidationError("Il Cognome deve essere valorizzato!"));
    }

    if(!Validator.isNotEmpty(nome)) {
      errors.add("nome", new ValidationError("Il Nome deve essere valorizzato!"));
    }

    if (!Validator.isNotEmpty(sesso)) {
      errors.add("sesso", new ValidationError("Il Sesso deve essere valorizzato!"));
    }

    // Il campo data di nascita è obbligatorio
    if(!Validator.isNotEmpty(strNascitaData)) {
      errors.add("strNascitaData", new ValidationError((String)AnagErrors.get("ERR_DATA_NASCITA_RAPPR_LEGALE_OBBLIGATORIA")));
    }
    else {
      if(strNascitaData.length() != 10) {
        errors.add("strNascitaData", new ValidationError((String)AnagErrors.get("ERR_DATA_NASCITA_RAPPR_LEGALE_ERRATA")));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(strNascitaData)) {
        errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
      try {
        nascitaData = DateUtils.parseDate(strNascitaData);
      }
      catch (Exception ex) {
        errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
    }

    if(!Validator.isNotEmpty(resIndirizzo)) {
      errors.add("resIndirizzo", new ValidationError("L'Indirizzo deve essere valorizzato!"));
    }
    // Il luogo di nascita è obbligatorio
    if(!Validator.isNotEmpty(descNascitaComune) && !Validator.isNotEmpty(nascitaProv)) {
      if(!Validator.isNotEmpty(nascitaStatoEstero)) {
        errors.add("nascitaStatoEstero", new ValidationError(AnagErrors.ERR_STATO_ESTERO_NASCITA_TITOLARE_OBBLIGATORIO));
      }
    }
    else {
      if(!Validator.isNotEmpty(descNascitaComune)) {
        errors.add("descNascitaComune", new ValidationError(AnagErrors.ERR_COMUNE_NASCITA_TITOLARE_OBBLIGATORIO));
      }
      if(!Validator.isNotEmpty(nascitaProv)) {
        errors.add("nascitaProv", new ValidationError(AnagErrors.ERR_PROVINCIA_NASCITA_TITOLARE_OBBLIGATORIA));
      }
      if(istatEsteroNascita != null) {
        if(!istatEsteroNascita.equals(SolmrConstants.ISTAT_STATO_ESTERO)) {
          if(Validator.isNotEmpty(cittaNascita)) {
            errors.add("cittaNascita", new ValidationError(AnagErrors.ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA));
          }
        }
      }
      if(!Validator.isNotEmpty(nascitaStatoEstero)) {
        if(Validator.isNotEmpty(cittaNascita)) {
          errors.add("cittaNascita", new ValidationError(AnagErrors.ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA));
        }
      }
      if(Validator.isNotEmpty(nascitaStatoEstero)) {
        errors.add("nascitaStatoEstero", new ValidationError(AnagErrors.ERR_STATO_ESTERO_NASCITA_TITOLARE_ERRATO));
      }
    }
    if (!Validator.isNotEmpty(descStatoEsteroResidenza)) 
    {
      if (!Validator.isNotEmpty(resCAP)) 
      {
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_OBBLIGATORIO));
      }
      else 
      {
        if(!Validator.isCapOk(resCAP)) 
        {
          errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_ERRATO));
        }
      }
      if (!Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_OBBLIGATORIO));
      if (!Validator.isNotEmpty(this.resProvincia))
        errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_OBBLIGATORIO));
      if (!Validator.isNotEmpty(resCAP) && !Validator.isNotEmpty(descResComune) && !Validator.isNotEmpty(resProvincia)) {
        errors.add("descStatoEsteroResidenza", new ValidationError(AnagErrors.ERR_STATO_ESTERO_RESIDENZA_TITOLARE_OBBLIGATORIO));
      }
      if (Validator.isNotEmpty(cittaResidenza)) {
        errors.add("cittaResidenza", new ValidationError(AnagErrors.ERR_CITTA_ESTERA_RESIDENZA_TITOLARE_ERRATA));
      }
    }
    else
    {
      if (Validator.isNotEmpty(resCAP))
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
      if (Validator.isNotEmpty(resProvincia))
        errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
      if (Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
    }

    // Controlli relativi al domicilio
    if(!Validator.isNotEmpty(domicilioStatoEstero)) 
    {
      // Se è stata valorizzato l'indirizzo controllo che siano stati valorizzati provincia, comune, cap
      if(Validator.isNotEmpty(domIndirizzo) || Validator.isNotEmpty(domProvincia) 
          || Validator.isNotEmpty(domComune) || Validator.isNotEmpty(domCAP)) 
      {
        if(!Validator.isNotEmpty(domComune)) {
          errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_COMUNE_DOMICILIO_OBBLIGATORIO")));
        }
        if(Validator.isEmpty(domCAP)) 
        {
          errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_CAP_DOMICILIO_OBBLIGATORIO")));
        }
        else
        {
          if(!Validator.isCapOk(domCAP)) 
          {
            errors.add("domCAP", new ValidationError(AnagErrors.ERR_CAP_DOMICILIO_ERRATO));
          }
        }
        if(!Validator.isNotEmpty(domProvincia)) {
          errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_PROVINCIA_DOMICILIO_OBBLIGATORIA")));
        }
        if(!Validator.isNotEmpty(domIndirizzo)) {
          errors.add("domIndirizzo", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_INDIRIZZO_OBLIGATORIO")));
        }
      }
      if(Validator.isNotEmpty(descCittaEsteroDomicilio)) {
        errors.add("descCittaEsteroDomicilio", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_STATO_ESTERO_NO_VALORIZZABILE")));
      }
    }
    else {
      if(Validator.isNotEmpty(domProvincia)) {
        errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_PROVINCIA_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domComune)) {
        errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_COMUNE_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domCAP)) {
        errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_CAP_NO_VALORIZZABILE")));
      }
      if(!Validator.isNotEmpty(domIndirizzo)) {
        errors.add("domIndirizzo", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_INDIRIZZO_OBLIGATORIO")));
      }
    }

    if(!Validator.isNotEmpty(codiceFiscale)) {
      errors.add("codiceFiscale", new ValidationError("Il codice fiscale deve essere valorizzato!"));
    }

    if(Validator.isNotEmpty(resMail) && !Validator.isValidEmail(resMail)) {
      errors.add("resMail", new ValidationError("La Mail inserita non è corretta!"));
    }
    
    if(Validator.isNotEmpty(numeroCellulare)) 
    {
      if(numeroCellulare.length() < 9) 
      {
        errors.add("numeroCellulare", new ValidationError(AnagErrors.ERR_NUM_CELLULARE));
      }
    }

    // Le note, se valorizzate, non possono essere più lunghe di trecento caratteri
    if(Validator.isNotEmpty(note)) {
      if(note.length() > 300) {
        errors.add("note", new ValidationError((String)AnagErrors.get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
      }
    }

    return errors;
  }



  // Metodo per effettuare la validazione dei dati del rappresentante legale richiamanto
  //dall'insediamento giovani
  public ValidationErrors validateModificaNuovoRappresentanteLegale(ValidationErrors errors)
  {
    // Il codice fiscale è obbligatorio
    if(!Validator.isNotEmpty(codiceFiscale)) {
      errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_OBBLIGATORIO")));
    }
    // Se è stato valorizzato controllo che sia corretto
    else {
      if(!Validator.controlloCf(codiceFiscale)) {
        errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE")));
      }
    }

    if(!Validator.isNotEmpty(cognome)) {
      errors.add("cognome", new ValidationError("Il Cognome deve essere valorizzato!"));
    }

    if(!Validator.isNotEmpty(nome)) {
      errors.add("nome", new ValidationError("Il Nome deve essere valorizzato!"));
    }

    if (!Validator.isNotEmpty(sesso)) {
      errors.add("sesso", new ValidationError("Il Sesso deve essere valorizzato!"));
    }

    // Il campo data di nascita è obbligatorio
    if(!Validator.isNotEmpty(strNascitaData)) {
      errors.add("strNascitaData", new ValidationError((String)AnagErrors.get("ERR_DATA_NASCITA_RAPPR_LEGALE_OBBLIGATORIA")));
    }
    else {
      if(strNascitaData.length() != 10) {
        errors.add("strNascitaData", new ValidationError((String)AnagErrors.get("ERR_DATA_NASCITA_RAPPR_LEGALE_ERRATA")));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(strNascitaData)) {
        errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
      try {
        nascitaData = DateUtils.parseDate(strNascitaData);
      }
      catch (Exception ex) {
        errors.add("strNascitaData", new ValidationError(AnagErrors.ERR_DATA_NASCITA_TITOLARE_ERRATA));
      }
    }

    if(!Validator.isNotEmpty(resIndirizzo)) {
      errors.add("resIndirizzo", new ValidationError("L'Indirizzo deve essere valorizzato!"));
    }
    // Il luogo di nascita è obbligatorio
    if(!Validator.isNotEmpty(descNascitaComune) && !Validator.isNotEmpty(nascitaProv)) {
      if(!Validator.isNotEmpty(nascitaStatoEstero)) {
        errors.add("nascitaStatoEstero", new ValidationError(AnagErrors.ERR_STATO_ESTERO_NASCITA_TITOLARE_OBBLIGATORIO));
      }
    }
    else {
      if(!Validator.isNotEmpty(descNascitaComune)) {
        errors.add("descNascitaComune", new ValidationError(AnagErrors.ERR_COMUNE_NASCITA_TITOLARE_OBBLIGATORIO));
      }
      if(!Validator.isNotEmpty(nascitaProv)) {
        errors.add("nascitaProv", new ValidationError(AnagErrors.ERR_PROVINCIA_NASCITA_TITOLARE_OBBLIGATORIA));
      }
      if(istatEsteroNascita != null) {
        if(!istatEsteroNascita.equals(SolmrConstants.ISTAT_STATO_ESTERO)) {
          if(Validator.isNotEmpty(cittaNascita)) {
            errors.add("cittaNascita", new ValidationError(AnagErrors.ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA));
          }
        }
      }
      if(!Validator.isNotEmpty(nascitaStatoEstero)) {
        if(Validator.isNotEmpty(cittaNascita)) {
          errors.add("cittaNascita", new ValidationError(AnagErrors.ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA));
        }
      }
      if(Validator.isNotEmpty(nascitaStatoEstero)) {
        errors.add("nascitaStatoEstero", new ValidationError(AnagErrors.ERR_STATO_ESTERO_NASCITA_TITOLARE_ERRATO));
      }
    }
    if (!Validator.isNotEmpty(descStatoEsteroResidenza)) 
    {
      if (Validator.isEmpty(resCAP)) 
      {
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_OBBLIGATORIO));
      }
      else 
      {
        if(!Validator.isCapOk(resCAP)) 
        {
          errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_ERRATO));
        }
      }
      if (!Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_OBBLIGATORIO));
      if (!Validator.isNotEmpty(this.resProvincia))
        errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_OBBLIGATORIO));
      if (!Validator.isNotEmpty(resCAP) && !Validator.isNotEmpty(descResComune) && !Validator.isNotEmpty(resProvincia)) {
        errors.add("descStatoEsteroResidenza", new ValidationError(AnagErrors.ERR_STATO_ESTERO_RESIDENZA_TITOLARE_OBBLIGATORIO));
      }
      if (Validator.isNotEmpty(cittaResidenza)) {
        errors.add("cittaResidenza", new ValidationError(AnagErrors.ERR_CITTA_ESTERA_RESIDENZA_TITOLARE_ERRATA));
      }
    }
    else
    {
      if (Validator.isNotEmpty(resCAP))
        errors.add("resCAP", new ValidationError(AnagErrors.ERR_CAP_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
      if (Validator.isNotEmpty(resProvincia))
        errors.add("resProvincia", new ValidationError(AnagErrors.ERR_PROVINCIA_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
      if (Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError(AnagErrors.ERR_COMUNE_RESIDENZA_TITOLARE_NO_VALORIZZABILE));
    }



    if(Validator.isNotEmpty(resMail) && !Validator.isValidEmail(resMail)) {
      errors.add("resMail", new ValidationError("La Mail inserita non è corretta!"));
    }


    return errors;
  }



  public ValidationErrors validateNuovoSoggettoCollegato() {

    ValidationErrors errors = new ValidationErrors();
    // Il ruolo è obbligatorio
    if(!Validator.isNotEmpty(idRuolo)) {
      errors.add("tipiRuoloNonTitolareAndNonSpecificato",new ValidationError("Il Ruolo deve essere valorizzato"));
    }
    // La data inizio ruolo è obbligatoria
    if(!Validator.isNotEmpty(strDataInizioRuoloMod)) {
      errors.add("strDataInizioRuoloMod", new ValidationError("La data di inizio ruolo deve essere valorizzata!"));
    }
    else {
      // Se la data è valorizzata controllo che sia corretta
      if (strDataInizioRuoloMod.length() != 10) {
        errors.add("strDataInizioRuoloMod", new ValidationError("La data di inizio ruolo inserita non è corretta!"));
      }
      else if (!Validator.isDate(strDataInizioRuoloMod)) {
        errors.add("strDataInizioRuoloMod", new ValidationError("La data di inizio ruolo inserita non è corretta!"));
      }
      else {
        try {
          dataInizioRuoloMod = DateUtils.parseDate(strDataInizioRuoloMod);
        }
        catch (Exception ex) {
          errors.add("strDataInizioRuoloMod", new ValidationError("La data di inizio ruolo inserita non è corretta!"));
        }
      }
    }
    // Il cognome è obbligatorio
    if (!Validator.isNotEmpty(cognome))
      errors.add("cognome", new ValidationError("Il Cognome deve essere valorizzato!"));
    // Il nome è obbligatorio
    if (!Validator.isNotEmpty(nome))
      errors.add("nome", new ValidationError("Il Nome deve essere valorizzato!"));
    // Il sesso è obbligatorio
    if (!Validator.isNotEmpty(sesso))
      errors.add("sesso", new ValidationError("Il Sesso deve essere valorizzato!"));
    // Il campo data di nascita è obbligatorio
    if (!Validator.isNotEmpty(strNascitaData))
      errors.add("strNascitaData", new ValidationError("La data di nascita deve essere valorizzata!"));
    else {
      if(strNascitaData.length() != 10) {
        errors.add("strNascitaData", new ValidationError("La data di nascita inserita non è corretta!"));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(strNascitaData)) {
        errors.add("strNascitaData", new ValidationError("La data di nascita inserita non è corretta!"));
      }
      try {
        nascitaData = DateUtils.parseDate(strNascitaData);
      } catch (Exception ex) {
        errors.add("strNascitaData", new ValidationError("La data di nascita inserita non è corretta!"));
      }
    }
    // Il luogo di nascita è obbligatorio
    if(!Validator.isNotEmpty(luogoNascita) && !Validator.isNotEmpty(nascitaProv)) {
      if(!Validator.isNotEmpty(nascitaStatoEstero)) {
        errors.add("nascitaStatoEstero", new ValidationError("Se la provincia e il luogo di nascita non sono specificati lo stato estero di nascita del soggetto è obbligatorio!"));
      }
    }
    else {
      if(!Validator.isNotEmpty(luogoNascita)) {
        errors.add("descNascitaComune", new ValidationError("Se il luogo di nascita non è uno stato estero il comune di nascita del soggetto è obbligatorio!"));
      }
      if(!Validator.isNotEmpty(nascitaProv)) {
        errors.add("nascitaProv", new ValidationError("Se il luogo di nascita non è uno stato estero la provincia di nascita del soggetto è obbligatoria!"));
      }
      if(istatEsteroNascita != null) {
        if(!istatEsteroNascita.equals(SolmrConstants.ISTAT_STATO_ESTERO)) {
          if(Validator.isNotEmpty(cittaNascita)) {
            errors.add("cittaNascita", new ValidationError("Se il luogo di nascita non è uno stato estero non è permesso inserire la città estera del soggetto!"));
          }
        }
      }
      if(!Validator.isNotEmpty(nascitaStatoEstero)) {
        if(Validator.isNotEmpty(cittaNascita)) {
          errors.add("cittaNascita", new ValidationError("Se il luogo di nascita non è uno stato estero non è permesso inserire la città estera del soggetto!"));
        }
      }
      if(Validator.isNotEmpty(nascitaStatoEstero)) {
        errors.add("nascitaStatoEstero", new ValidationError("Se il luogo di nascita è in Italia non è permesso inserire lo stato estero di nascita del soggetto!"));
      }
    }
    // L'indirizzo è obbligatorio
    if (!Validator.isNotEmpty(resIndirizzo))
      errors.add("resIndirizzo", new ValidationError("L'' indirizzo deve essere valorizzato!"));

    if (!Validator.isNotEmpty(descStatoEsteroResidenza)) 
    {
      if (Validator.isEmpty(resCAP)) 
      {
        errors.add("resCAP", new ValidationError("Se lo Stato Estero non è valorizzato, il CAP è obbligatorio!"));
      }
      else 
      {
        if(!Validator.isCapOk(resCAP)) 
        {
          errors.add("resCAP", new ValidationError("Il CAP inserito non e'' valido!"));
        }
      }
      if (!Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError("Se lo Stato Estero non è valorizzato, il Comune è obbligatorio!"));
      if (!Validator.isNotEmpty(this.resProvincia))
        errors.add("resProvincia", new ValidationError("Se lo Stato Estero non è valorizzato, la Provincia è obbligatoria!"));
      if (!Validator.isNotEmpty(resCAP) && !Validator.isNotEmpty(descResComune) && !Validator.isNotEmpty(resProvincia)) {
        errors.add("descStatoEsteroResidenza", new ValidationError("Se Provincia, Comune e CAP non sono valorizzati, lo Stato Estero è obbligatorio!"));
      }
      if (Validator.isNotEmpty(cittaResidenza)) {
        errors.add("cittaResidenza", new ValidationError("Se lo stato estero della residenza non è valorizzato non è possibile valorizzare il campo città!"));
      }
    }
    else
    {
      if (Validator.isNotEmpty(resCAP))
        errors.add("resCAP", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      if (Validator.isNotEmpty(resProvincia))
        errors.add("resProvincia", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      if (Validator.isNotEmpty(descResComune))
        errors.add("descResComune", new ValidationError("Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
    }
    if (Validator.isNotEmpty(resMail) && !Validator.isValidEmail(resMail))
      errors.add("resMail", new ValidationError("La Mail inserita non è corretta!"));
    //Se viene inserito il prefisso il numero del cellulare è obbligatorio e viceversa...
    //if(Validator.isNotEmpty(idPrefissoCellulareNaz) && !Validator.isNotEmpty(desNumeroCellulare))
     //errors.add("idCellulareNumero", new ValidationError("Inserendo il prefisso, il numero di cellulare è obbligatorio!"));
    //else if(!Validator.isNotEmpty(idPrefissoCellulareNaz) && Validator.isNotEmpty(desNumeroCellulare))
           // errors.add("idCellulareNumero", new ValidationError("Inserendo il numero, il prefisso del cellulare è obbligatorio!"));
    //if(Validator.isNotEmpty(idPrefissoCellulareNaz) && Validator.isNotEmpty(desNumeroCellulare)){
    if(Validator.isNotEmpty(desNumeroCellulare))
    {
      if(desNumeroCellulare.length() < 9) 
      {
        errors.add("idCellulareNumero", new ValidationError(AnagErrors.ERR_NUM_CELLULARE));
      }
    }
    

    // Il cap di domicilio non è obbligatorio, quindi verifico se è corretto solo quando è valorizzato
    if(Validator.isNotEmpty(domCAP) && !Validator.isCapOk(domCAP))
      errors.add("domCAP", new ValidationError("Il CAP inserito non è valido"));

    // Controlli relativi al domicilio
    if(!Validator.isNotEmpty(domicilioStatoEstero)) 
    {
      // Se è stata valorizzato l'indirizzo controllo che siano stati valorizzati provincia, comune, cap
      if(Validator.isNotEmpty(domIndirizzo) || Validator.isNotEmpty(domProvincia) 
          || Validator.isNotEmpty(domComune) || Validator.isNotEmpty(domCAP)) 
      {
        if(!Validator.isNotEmpty(domComune)) {
          errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_COMUNE_DOMICILIO_OBBLIGATORIO")));
        }
        if(Validator.isEmpty(domCAP)) 
        {
          errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_CAP_DOMICILIO_OBBLIGATORIO")));
        }
        else 
        {
          if(!Validator.isCapOk(domCAP)) 
          {
            errors.add("domCAP", new ValidationError(AnagErrors.ERR_CAP_DOMICILIO_ERRATO));
          }
        }
        if(!Validator.isNotEmpty(domProvincia)) {
          errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_PROVINCIA_DOMICILIO_OBBLIGATORIA")));
        }
        if(!Validator.isNotEmpty(domIndirizzo)) {
          errors.add("domIndirizzo", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_INDIRIZZO_OBLIGATORIO")));
        }
      }
      if(Validator.isNotEmpty(descCittaEsteroDomicilio)) {
        errors.add("descCittaEsteroDomicilio", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_STATO_ESTERO_NO_VALORIZZABILE")));
      }
    }
    else {
      if(Validator.isNotEmpty(domProvincia)) {
        errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_PROVINCIA_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domComune)) {
        errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_COMUNE_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domCAP)) {
        errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_CAP_NO_VALORIZZABILE")));
      }
      if(!Validator.isNotEmpty(domIndirizzo)) {
        errors.add("domIndirizzo", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_INDIRIZZO_OBLIGATORIO")));
      }
    }

    // Le note, se valorizzate non devono essere più lunghe di 300 caratteri
    if(Validator.isNotEmpty(note)) {
      if(note.length() > 300) {
        errors.add("note", new ValidationError((String)AnagErrors.get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
      }
    }

    return errors;
  }

  public ValidationErrors validateModificaSoggetto()
  {
    ValidationErrors errors = new ValidationErrors();
    errors = validateNuovoSoggettoCollegato();
    // Dato che la data fine ruolo non è obbligatoria (la modifica potrebbe riguardare
    // i dati anagrafici del soggetto), ne verifico la correttezza solo quando risulta
    // valorizzata
    if (strDataFineRuoloMod!=null&&!strDataFineRuoloMod.equals(""))
    {
      if(strDataFineRuoloMod.length() != 10) {
        errors.add("strDataFineRuoloMod", new ValidationError("La data di fine ruolo del soggetto è errata!"));
      }
      // Se la data è valorizzata controllo che sia corretta
      if(!Validator.isDate(strDataFineRuoloMod)) {
        errors.add("strDataFineRuoloMod", new ValidationError("La data di fine ruolo del soggetto è errata!"));
      }
      try {
        dataFineRuoloMod = DateUtils.parseDate(strDataFineRuoloMod);
      }
      catch (Exception ex) {
        errors.add("strDataFineRuoloMod", new ValidationError("La data di fine ruolo del soggetto è errata!"));
      }
      // La data fine ruolo non deve essere maggiore della data fine ruolo...
      if(dataInizioRuoloMod!=null&&dataFineRuoloMod!=null&&dataFineRuoloMod.before(dataInizioRuoloMod)) {
        errors.add("strDataFineRuoloMod", new ValidationError("La data fine ruolo non può essere antecedente alla data di inizio ruolo"));
      }
    }
    // Controlli relativi al domicilio
    if(!Validator.isNotEmpty(domicilioStatoEstero)) 
    {
      // Se è stata valorizzato l'indirizzo controllo che siano stati valorizzati provincia, comune, cap
      if(Validator.isNotEmpty(domIndirizzo) || Validator.isNotEmpty(domProvincia) 
          || Validator.isNotEmpty(domComune) || Validator.isNotEmpty(domCAP)) 
      {
        if(!Validator.isNotEmpty(domComune)) 
        {
          errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_COMUNE_DOMICILIO_OBBLIGATORIO")));
        }
        if(Validator.isEmpty(domCAP)) 
        {
          errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_CAP_DOMICILIO_OBBLIGATORIO")));
        }
        else 
        {
          if(!Validator.isCapOk(domCAP)) 
          {
            errors.add("domCAP", new ValidationError(AnagErrors.ERR_CAP_DOMICILIO_ERRATO));
          }
        }
        if(!Validator.isNotEmpty(domProvincia)) {
          errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_PROVINCIA_DOMICILIO_OBBLIGATORIA")));
        }
        if(!Validator.isNotEmpty(domIndirizzo)) {
          errors.add("domIndirizzo", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_INDIRIZZO_OBLIGATORIO")));
        }
      }
      if(Validator.isNotEmpty(descCittaEsteroDomicilio)) {
        errors.add("descCittaEsteroDomicilio", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_STATO_ESTERO_NO_VALORIZZABILE")));
      }
    }
    else {
      if(Validator.isNotEmpty(domProvincia)) {
        errors.add("domProvincia", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_PROVINCIA_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domComune)) {
        errors.add("domComune", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_COMUNE_NO_VALORIZZABILE")));
      }
      if(Validator.isNotEmpty(domCAP)) {
        errors.add("domCAP", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_CAP_NO_VALORIZZABILE")));
      }
      if(!Validator.isNotEmpty(domIndirizzo)) {
        errors.add("domIndirizzo", new ValidationError((String)AnagErrors.get("ERR_DOMICILIO_INDIRIZZO_OBLIGATORIO")));
      }
    }

    // Il codice fiscale è obbligatorio
    if(!Validator.isNotEmpty(codiceFiscale)) {
      errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_OBBLIGATORIO")));
    }
    else {
      if(!Validator.controlloCf(codiceFiscale)) {
        errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE")));
      }
    }

    // Le note, se valorizzate non devono essere più lunghe di 300 caratteri
    if(Validator.isNotEmpty(note)) {
      if(note.length() > 300) {
        errors.add("note", new ValidationError((String)AnagErrors.get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
      }
    }

    // Se lo stato estero non è valorizzato, la città estera non può essere valorizzata
    if(!Validator.isNotEmpty(descStatoEsteroResidenza)) {
      if(Validator.isNotEmpty(resCittaEstero)) {
        errors.add("cittaResidenza", new ValidationError( (String) AnagErrors.get("ERR_RESIDENZA_CITTA_ESTERO_NO_VALORIZZABILE")));
      }
    }
   
    if(Validator.isNotEmpty(desNumeroCellulare))
    {
      if(desNumeroCellulare.length() < 9) 
      {
        errors.add("idCellulareNumero", new ValidationError(AnagErrors.ERR_NUM_CELLULARE));
      }
    }

    return errors;
  }





  public void setNascitaStatoEstero(String nascitaStatoEstero) {
    this.nascitaStatoEstero = nascitaStatoEstero;
  }
  public String getNascitaStatoEstero() {
    return nascitaStatoEstero;
  }
  public void setNascitaFlagEstero(String nascitaFlagEstero) {
    this.nascitaFlagEstero = nascitaFlagEstero;
  }
  public String getNascitaFlagEstero() {
    return nascitaFlagEstero;
  }
  public String getResCittaEstero() {
    return resCittaEstero;
  }
  public void setResCittaEstero(String resCittaEstero) {
    this.resCittaEstero = resCittaEstero;
  }
  public String getNascitaCittaEstero() {
    return nascitaCittaEstero;
  }
  public void setNascitaCittaEstero(String nascitaCittaEstero) {
    this.nascitaCittaEstero = nascitaCittaEstero;
  }

  public void setCittaNascita(String cittaNascita) {
    this.cittaNascita = cittaNascita;
  }

  public String getCittaNascita() {
    return cittaNascita;
  }

  public void setCittaResidenza(String cittaResidenza) {
    this.cittaResidenza = cittaResidenza;
  }

  public String getCittaResidenza() {
    return cittaResidenza;
  }

  public void setIstatEsteroNascita(String istatEsteroNascita) {
    this.istatEsteroNascita = istatEsteroNascita;
  }

  public String getFlagEsteroNascita() {
    return istatEsteroNascita;
  }
  public void setRuolo(String ruolo) {
    this.ruolo = ruolo;
  }
  public String getRuolo() {
    return ruolo;
  }
  public void setIdRuolo(Long idRuolo) {
    this.idRuolo = idRuolo;
  }
  public Long getIdRuolo() {
    return idRuolo;
  }
  public void setDescUtenteAggiornamento(String descUtenteAggiornamento) {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }
  public String getDescUtenteAggiornamento() {
    return descUtenteAggiornamento;
  }
  public void setTipiRuoloNonTitolare(Long tipiRuoloNonTitolare) {
    this.tipiRuoloNonTitolare = tipiRuoloNonTitolare;
  }
  public Long getTipiRuoloNonTitolare() {
    return tipiRuoloNonTitolare;
  }
  public String getIstatComuneDomicilio() {
    return istatComuneDomicilio;
  }
  public void setIstatComuneDomicilio(String istatComuneDomicilio) {
    this.istatComuneDomicilio = istatComuneDomicilio;
  }

  public Long getTipiRuoloNonTitolareAndNonSpecificato() {
    return tipiRuoloNonTitolareAndNonSpecificato;
  }
  public void setTipiRuoloNonTitolareAndNonSpecificato(Long tipiRuoloNonTitolareAndNonSpecificato) {
    this.tipiRuoloNonTitolareAndNonSpecificato = tipiRuoloNonTitolareAndNonSpecificato;
  }

  public void setStrDataInizioRuolo(String strDataInizioRuolo) {
    this.strDataInizioRuolo = strDataInizioRuolo;
  }
  public String getStrDataInizioRuolo() {
    return strDataInizioRuolo;
  }
  public void setStrDataFineRuolo(String strDataFineRuolo) {
    this.strDataFineRuolo = strDataFineRuolo;
  }
  public String getStrDataFineRuolo() {
    return strDataFineRuolo;
  }
  public void setIdContitolare(Long idContitolare) {
    this.idContitolare = idContitolare;
  }
  public Long getIdContitolare() {
    return idContitolare;
  }
  public Long getIdTitoloStudio() {
    return idTitoloStudio;
  }
  public void setIdTitoloStudio(Long idTitoloStudio) {
    this.idTitoloStudio = idTitoloStudio;
  }
  public String getDescrizioneTitoloStudio() {
    return descrizioneTitoloStudio;
  }
  public void setDescrizioneTitoloStudio(String descrizioneTitoloStudio) {
    this.descrizioneTitoloStudio = descrizioneTitoloStudio;
  }
  public String getDescrizioneIndirizzoStudio() {
    return descrizioneIndirizzoStudio;
  }
  public void setDescrizioneIndirizzoStudio(String descrizioneIndirizzoStudio) {
    this.descrizioneIndirizzoStudio = descrizioneIndirizzoStudio;
  }
  public Long getIdIndirizzoStudio() {
    return idIndirizzoStudio;
  }
  public void setIdIndirizzoStudio(Long idIndirizzoStudio) {
    this.idIndirizzoStudio = idIndirizzoStudio;
  }

  public void setDataInizioResidenza(java.util.Date dataInizioResidenza) {
    this.dataInizioResidenza = dataInizioResidenza;
  }
  public java.util.Date getDataInizioResidenza() {
    return dataInizioResidenza;
  }
  public void setStrDataInizioResidenza(String strDataInizioResidenza) {
    this.strDataInizioResidenza = strDataInizioResidenza;
  }
  public String getStrDataInizioResidenza() {
    return strDataInizioResidenza;
  }

  public int compareTo(Object obj) {
    PersonaFisicaVO persVO = (PersonaFisicaVO)obj;
    int result = 0;

    if(dataFineRuolo == null) {
      if(persVO.dataFineRuolo != null) {
        result = -1;
      }
      else {
        result = dataInizioRuolo.compareTo(persVO.dataInizioRuolo);
      }
    }
    else {
      if(persVO.dataFineRuolo == null) {
        result = 1;
      }
      else {
        result = dataInizioRuolo.compareTo(persVO.dataInizioRuolo);
        if(result == 0) {
          result = dataFineRuolo.compareTo(persVO.dataFineRuolo);
        }
      }
    }
    return result;
  }

  public String getCodiceRuoloAAEP() {
    return codiceRuoloAAEP;
  }
  public void setCodiceRuoloAAEP(String codiceRuoloAAEP) {
    this.codiceRuoloAAEP = codiceRuoloAAEP;
  }

  public java.util.Date getDataInizioRuoloMod()
  {
    return dataInizioRuoloMod;
  }

  public void setDataInizioRuoloMod(java.util.Date dataInizioRuoloMod)
  {
    this.dataInizioRuoloMod = dataInizioRuoloMod;
  }

  public java.util.Date getDataFineRuoloMod()
  {
    return dataFineRuoloMod;
  }

  public void setDataFineRuoloMod(java.util.Date dataFineRuoloMod)
  {
    this.dataFineRuoloMod = dataFineRuoloMod;
  }

  public String getStrDataInizioRuoloMod()
  {
    return strDataInizioRuoloMod;
  }

  public void setStrDataInizioRuoloMod(String strDataInizioRuoloMod)
  {
    this.strDataInizioRuoloMod = strDataInizioRuoloMod;
  }

  public String getStrDataFineRuoloMod()
  {
    return strDataFineRuoloMod;
  }

  public void setStrDataFineRuoloMod(String strDataFineRuoloMod)
  {
    this.strDataFineRuoloMod = strDataFineRuoloMod;
  }
}
