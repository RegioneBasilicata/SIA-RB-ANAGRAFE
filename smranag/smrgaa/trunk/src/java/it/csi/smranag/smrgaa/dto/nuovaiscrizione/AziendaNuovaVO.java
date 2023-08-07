package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */

public class AziendaNuovaVO implements Serializable
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 8420476909468802137L;
  
  
  
  private Long idAziendaNuova;
  private Long idTipologiaAzienda;
  private String cuaa;
  private String partitaIva;
  private String denominazione;
  private String mail;
  private String telefono;
  private String fax;
  private String pec;
  private Long idFormaGiuridica;
  private String sedelegComune;
  private String descComune;
  private String sedelegProv;
  private String sedelegIndirizzo;
  private String sedelegCittaEstero;
  private String sedelegCap;
  private Long idAziendaSubentro;
  private String note;
  private Long idStatoRichiesta;
  private String tipoSoggetto; //se R=rapperesentante legale, P = persona fisica
  private String cognome;
  private String nome;
  private String codiceFiscale;
  private String sesso;
  private Date dataNascita;
  private String comuneNascita;
  private String nascitaProv;
  private String descNascitaComune;
  private String cittaNascitaEstero;
  private String resComune;
  private String resProvincia;
  private String descResComune;
  private String resIndirizzo;
  private String resCittaEstero;
  private String resCap;
  private String mailSoggetto;
  private String telefonoSoggetto;
  private String faxSoggetto;
  private String sedeLegUte;
  private String cuaaSubentro;
  private String denomSubentro;
  private Long idRichiestaAzienda;
  private String descFormaGiur;
  private String descTipoRichiesta;
  private Long idTipoRichiesta;
  private Long idIterRichiestaAzienda;
  private Long idAzienda;
  private String descStatoRichiesta;
  private Date dataAggiornamento;
  private String flagDichiarazioneAllegati;
  private String descEstesaTipoRichiesta;
  private String nomAllegato;
  private Date dataAggiornamentoIter;
  private Long idUtenteAggiornamentoRich;
  private String denomUtenteModifica;
  private String testoAnnullamento;
  private Integer idMotivoRichiesta;
  private String descMotivoRichiesta;
  private String noteRichiestaAzienda;
  private String flagNoteObbligatorie;
  private Integer idMotivoDichiarazione;
  private String descMotivoDichiarazione;
  private Long idCessazione;
  private Date dataCessazione;
  private String descCessazione;
  private String cuaaSubentrante;
  private String denominazioneSubentrante;
  private Long idAziendaSubentrante;
  private Date dataValidazione;
  private String codEnte;
  private String flagSoloAggiunta;
  
  public Long getIdAziendaNuova()
  {
    return idAziendaNuova;
  }
  public void setIdAziendaNuova(Long idAziendaNuova)
  {
    this.idAziendaNuova = idAziendaNuova;
  }
  public Long getIdTipologiaAzienda()
  {
    return idTipologiaAzienda;
  }
  public void setIdTipologiaAzienda(Long idTipologiaAzienda)
  {
    this.idTipologiaAzienda = idTipologiaAzienda;
  }
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }
  public String getPartitaIva()
  {
    return partitaIva;
  }
  public void setPartitaIva(String partitaIva)
  {
    this.partitaIva = partitaIva;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getMail()
  {
    return mail;
  }
  public void setMail(String mail)
  {
    this.mail = mail;
  }
  public String getTelefono()
  {
    return telefono;
  }
  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }
  public String getFax()
  {
    return fax;
  }
  public void setFax(String fax)
  {
    this.fax = fax;
  }
  public String getPec()
  {
    return pec;
  }
  public void setPec(String pec)
  {
    this.pec = pec;
  }
  public Long getIdFormaGiuridica()
  {
    return idFormaGiuridica;
  }
  public void setIdFormaGiuridica(Long idFormaGiuridica)
  {
    this.idFormaGiuridica = idFormaGiuridica;
  }
  public String getSedelegComune()
  {
    return sedelegComune;
  }
  public void setSedelegComune(String sedelegComune)
  {
    this.sedelegComune = sedelegComune;
  }  
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSedelegProv()
  {
    return sedelegProv;
  }
  public void setSedelegProv(String sedelegProv)
  {
    this.sedelegProv = sedelegProv;
  }
  public String getSedelegIndirizzo()
  {
    return sedelegIndirizzo;
  }
  public void setSedelegIndirizzo(String sedelegIndirizzo)
  {
    this.sedelegIndirizzo = sedelegIndirizzo;
  }
  public String getSedelegCittaEstero()
  {
    return sedelegCittaEstero;
  }
  public void setSedelegCittaEstero(String sedelegCittaEstero)
  {
    this.sedelegCittaEstero = sedelegCittaEstero;
  }
  public String getSedelegCap()
  {
    return sedelegCap;
  }
  public void setSedelegCap(String sedelegCap)
  {
    this.sedelegCap = sedelegCap;
  }
  public Long getIdAziendaSubentro()
  {
    return idAziendaSubentro;
  }
  public void setIdAziendaSubentro(Long idAziendaSubentro)
  {
    this.idAziendaSubentro = idAziendaSubentro;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public Long getIdStatoRichiesta()
  {
    return idStatoRichiesta;
  }
  public void setIdStatoRichiesta(Long idStatoRichiesta)
  {
    this.idStatoRichiesta = idStatoRichiesta;
  }
  public String getTipoSoggetto()
  {
    return tipoSoggetto;
  }
  public void setTipoSoggetto(String tipoSoggetto)
  {
    this.tipoSoggetto = tipoSoggetto;
  }
  public String getCognome()
  {
    return cognome;
  }
  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }
  public String getNome()
  {
    return nome;
  }
  public void setNome(String nome)
  {
    this.nome = nome;
  }
  public String getCodiceFiscale()
  {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale)
  {
    this.codiceFiscale = codiceFiscale;
  }
  public String getSesso()
  {
    return sesso;
  }
  public void setSesso(String sesso)
  {
    this.sesso = sesso;
  }
  public Date getDataNascita()
  {
    return dataNascita;
  }
  public void setDataNascita(Date dataNascita)
  {
    this.dataNascita = dataNascita;
  }
  public String getComuneNascita()
  {
    return comuneNascita;
  }
  public void setComuneNascita(String comuneNascita)
  {
    this.comuneNascita = comuneNascita;
  }
  
  public String getNascitaProv()
  {
    return nascitaProv;
  }
  public void setNascitaProv(String nascitaProv)
  {
    this.nascitaProv = nascitaProv;
  }
  public String getDescNascitaComune()
  {
    return descNascitaComune;
  }
  public void setDescNascitaComune(String descNascitaComune)
  {
    this.descNascitaComune = descNascitaComune;
  }
  public String getCittaNascitaEstero()
  {
    return cittaNascitaEstero;
  }
  public void setCittaNascitaEstero(String cittaNascitaEstero)
  {
    this.cittaNascitaEstero = cittaNascitaEstero;
  }
  public String getResComune()
  {
    return resComune;
  }
  public void setResComune(String resComune)
  {
    this.resComune = resComune;
  }
  public String getResProvincia()
  {
    return resProvincia;
  }
  public void setResProvincia(String resProvincia)
  {
    this.resProvincia = resProvincia;
  }
  public String getDescResComune()
  {
    return descResComune;
  }
  public void setDescResComune(String descResComune)
  {
    this.descResComune = descResComune;
  }
  public String getResIndirizzo()
  {
    return resIndirizzo;
  }
  public void setResIndirizzo(String resIndirizzo)
  {
    this.resIndirizzo = resIndirizzo;
  }
  public String getResCittaEstero()
  {
    return resCittaEstero;
  }
  public void setResCittaEstero(String resCittaEstero)
  {
    this.resCittaEstero = resCittaEstero;
  }
  public String getResCap()
  {
    return resCap;
  }
  public void setResCap(String resCap)
  {
    this.resCap = resCap;
  }
  public String getMailSoggetto()
  {
    return mailSoggetto;
  }
  public void setMailSoggetto(String mailSoggetto)
  {
    this.mailSoggetto = mailSoggetto;
  }
  public String getTelefonoSoggetto()
  {
    return telefonoSoggetto;
  }
  public void setTelefonoSoggetto(String telefonoSoggetto)
  {
    this.telefonoSoggetto = telefonoSoggetto;
  }
  public String getFaxSoggetto()
  {
    return faxSoggetto;
  }
  public void setFaxSoggetto(String faxSoggetto)
  {
    this.faxSoggetto = faxSoggetto;
  }
  public String getSedeLegUte()
  {
    return sedeLegUte;
  }
  public void setSedeLegUte(String sedeLegUte)
  {
    this.sedeLegUte = sedeLegUte;
  }
  public String getCuaaSubentro()
  {
    return cuaaSubentro;
  }
  public void setCuaaSubentro(String cuaaSubentro)
  {
    this.cuaaSubentro = cuaaSubentro;
  }
  public String getDenomSubentro()
  {
    return denomSubentro;
  }
  public void setDenomSubentro(String denomSubentro)
  {
    this.denomSubentro = denomSubentro;
  }
  public Long getIdRichiestaAzienda()
  {
    return idRichiestaAzienda;
  }
  public void setIdRichiestaAzienda(Long idRichiestaAzienda)
  {
    this.idRichiestaAzienda = idRichiestaAzienda;
  }
  public String getDescFormaGiur()
  {
    return descFormaGiur;
  }
  public void setDescFormaGiur(String descFormaGiur)
  {
    this.descFormaGiur = descFormaGiur;
  }
  public String getDescTipoRichiesta()
  {
    return descTipoRichiesta;
  }
  public void setDescTipoRichiesta(String descTipoRichiesta)
  {
    this.descTipoRichiesta = descTipoRichiesta;
  }
  public Long getIdIterRichiestaAzienda()
  {
    return idIterRichiestaAzienda;
  }
  public void setIdIterRichiestaAzienda(Long idIterRichiestaAzienda)
  {
    this.idIterRichiestaAzienda = idIterRichiestaAzienda;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public Long getIdTipoRichiesta()
  {
    return idTipoRichiesta;
  }
  public void setIdTipoRichiesta(Long idTipoRichiesta)
  {
    this.idTipoRichiesta = idTipoRichiesta;
  }
  public String getDescStatoRichiesta()
  {
    return descStatoRichiesta;
  }
  public void setDescStatoRichiesta(String descStatoRichiesta)
  {
    this.descStatoRichiesta = descStatoRichiesta;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public String getFlagDichiarazioneAllegati()
  {
    return flagDichiarazioneAllegati;
  }
  public void setFlagDichiarazioneAllegati(String flagDichiarazioneAllegati)
  {
    this.flagDichiarazioneAllegati = flagDichiarazioneAllegati;
  }
  public String getDescEstesaTipoRichiesta()
  {
    return descEstesaTipoRichiesta;
  }
  public void setDescEstesaTipoRichiesta(String descEstesaTipoRichiesta)
  {
    this.descEstesaTipoRichiesta = descEstesaTipoRichiesta;
  }
  public String getNomAllegato()
  {
    return nomAllegato;
  }
  public void setNomAllegato(String nomAllegato)
  {
    this.nomAllegato = nomAllegato;
  }
  public Date getDataAggiornamentoIter()
  {
    return dataAggiornamentoIter;
  }
  public void setDataAggiornamentoIter(Date dataAggiornamentoIter)
  {
    this.dataAggiornamentoIter = dataAggiornamentoIter;
  }
  public String getDenomUtenteModifica()
  {
    return denomUtenteModifica;
  }
  public void setDenomUtenteModifica(String denomUtenteModifica)
  {
    this.denomUtenteModifica = denomUtenteModifica;
  }
  public String getTestoAnnullamento()
  {
    return testoAnnullamento;
  }
  public void setTestoAnnullamento(String testoAnnullamento)
  {
    this.testoAnnullamento = testoAnnullamento;
  }
  public Integer getIdMotivoRichiesta()
  {
    return idMotivoRichiesta;
  }
  public void setIdMotivoRichiesta(Integer idMotivoRichiesta)
  {
    this.idMotivoRichiesta = idMotivoRichiesta;
  }
  public String getDescMotivoRichiesta()
  {
    return descMotivoRichiesta;
  }
  public void setDescMotivoRichiesta(String descMotivoRichiesta)
  {
    this.descMotivoRichiesta = descMotivoRichiesta;
  }
  public String getNoteRichiestaAzienda()
  {
    return noteRichiestaAzienda;
  }
  public void setNoteRichiestaAzienda(String noteRichiestaAzienda)
  {
    this.noteRichiestaAzienda = noteRichiestaAzienda;
  }
  public String getFlagNoteObbligatorie()
  {
    return flagNoteObbligatorie;
  }
  public void setFlagNoteObbligatorie(String flagNoteObbligatorie)
  {
    this.flagNoteObbligatorie = flagNoteObbligatorie;
  }
  public Long getIdUtenteAggiornamentoRich()
  {
    return idUtenteAggiornamentoRich;
  }
  public void setIdUtenteAggiornamentoRich(Long idUtenteAggiornamentoRich)
  {
    this.idUtenteAggiornamentoRich = idUtenteAggiornamentoRich;
  }
  public Integer getIdMotivoDichiarazione()
  {
    return idMotivoDichiarazione;
  }
  public void setIdMotivoDichiarazione(Integer idMotivoDichiarazione)
  {
    this.idMotivoDichiarazione = idMotivoDichiarazione;
  }
  public String getDescMotivoDichiarazione()
  {
    return descMotivoDichiarazione;
  }
  public void setDescMotivoDichiarazione(String descMotivoDichiarazione)
  {
    this.descMotivoDichiarazione = descMotivoDichiarazione;
  }
  public Long getIdCessazione()
  {
    return idCessazione;
  }
  public void setIdCessazione(Long idCessazione)
  {
    this.idCessazione = idCessazione;
  }
  public Date getDataCessazione()
  {
    return dataCessazione;
  }
  public void setDataCessazione(Date dataCessazione)
  {
    this.dataCessazione = dataCessazione;
  }
  public String getCuaaSubentrante()
  {
    return cuaaSubentrante;
  }
  public void setCuaaSubentrante(String cuaaSubentrante)
  {
    this.cuaaSubentrante = cuaaSubentrante;
  }
  public String getDenominazioneSubentrante()
  {
    return denominazioneSubentrante;
  }
  public void setDenominazioneSubentrante(String denominazioneSubentrante)
  {
    this.denominazioneSubentrante = denominazioneSubentrante;
  }
  public Long getIdAziendaSubentrante()
  {
    return idAziendaSubentrante;
  }
  public void setIdAziendaSubentrante(Long idAziendaSubentrante)
  {
    this.idAziendaSubentrante = idAziendaSubentrante;
  }
  public String getDescCessazione()
  {
    return descCessazione;
  }
  public void setDescCessazione(String descCessazione)
  {
    this.descCessazione = descCessazione;
  }
  public Date getDataValidazione()
  {
    return dataValidazione;
  }
  public void setDataValidazione(Date dataValidazione)
  {
    this.dataValidazione = dataValidazione;
  }
  public String getCodEnte()
  {
    return codEnte;
  }
  public void setCodEnte(String codEnte)
  {
    this.codEnte = codEnte;
  }
  public String getFlagSoloAggiunta()
  {
    return flagSoloAggiunta;
  }
  public void setFlagSoloAggiunta(String flagSoloAggiunta)
  {
    this.flagSoloAggiunta = flagSoloAggiunta;
  }
  
  
  
  
  
  
  
  
  
}
