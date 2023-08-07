package it.csi.solmr.dto.anag;

import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class ContoCorrenteVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -5976625367762896500L;
  private Long idContoCorrente; // DB_CONTO_CORRENTE.ID_CONTO_CORRENTE
  private Long idAzienda; // DB_CONTO_CORRENTE.ID_AZIENDA
  private String numeroContoCorrente; // DB_CONTO_CORRENTE.NUMERO_CONTO_CORRENTE
  private String cin; // DB_CONTO_CORRENTE.CIN
  private String bban; // DB_CONTO_CORRENTE.BBAN
  private String iban; // DB_CONTO_CORRENTE.IBAN
  private String cifraCtrl;// DB_CONTO_CORRENTE.CIFRA_CONTROLLO
  private String flagValidato;// DB:CONTO_CORRENTE_FLAG_VALIDATO
  private String bic;
  private String codPaese;// DB_TIPO_SPORTELLO.CODICE_PAESE
  private String intestazione; // DB_CONTO_CORRENTE.INTESTAZIONE
  private String descUtenteAggiornamento; // DB_CONTO_CORRENTE.
  private Long idUtenteAggiornamento; // DB_CONTO_CORRENTE.ID_UTENTE_AGGIORNAMENTO
  private java.util.Date dataAggiornamento; // DB_CONTO_CORRENTE.DATA_AGGIORNAMENTO
  private java.util.Date dataEstinzione; // DB_CONTO_CORRENTE.DATA_ESTINZIONE
  private java.util.Date dataInizioValiditaContoCorrente; //DB_CONTO_CORRENTE.DATA_INIZIO_VALIDITA
  private java.util.Date dataFineValiditaContoCorrente; // DB_CONTO_CORRENTE.DATA_FINE_VALIDITA

  private String denominazioneBanca; // DB_TIPO_BANCA.DENOMINAZIONE
  private String cab; // DB_TIPO_SPORTELLO.CAB
  private String indirizzoSportello; // DB_TIPO_SPORTELLO.INDIRIZZO
  private String capSportello; // DB_TIPO_SPORTELLO.CAP
  private String istatComuneSportello; // DB_TIPO_SPORTELLO.COMUNE
  private String denominazioneSportello; // DB_TIPO_SPORTELLO.DENOMINAZIONE
  private String descrizioneComuneSportello; // COMUNE.DESCOM da DB_TIPO_SPORTELLO.COMUNE
  private String flagSportelloGf; //DB_TIPO_SPORTELLO.FLAG_SPORTELLO_GF
  
  private String abi; // DB_TIPO_BANCA.ABI
  private Date dataInizioValiditaBanca; // DB_TIPO_BANCA.DATA_INIZIO_VALIDITA
  private Date dataInizioValiditaSportello; // DB_TIPO_SPORTELLO.DATA_INIZIO_VALIDITA
  private Date dataFineValiditaSportello; // DB_TIPO_SPORTELLO.DATA_FINE_VALIDITA
  private Date dataFineValiditaBanca; // DB_TIPO_BANCA.DATA_FINE_VALIDITA
  private Long idBanca; // DB_TIPO_BANCA.ID_BANCA
  private Long idSportello; // DB_TIPO_SPORTELLO.ID_SPORTELLO
  private String provinciaSportello; // PROVINCIA.DESCRIZIONE da
                                      // COMUNE.ISTAT_PROVINCIA da
                                      // DB_TIPO_SPORTELLO.COMUNE
  private String siglaProvincia;                // PROVINCIA.SIGLA_PROVINCIA
                                                          // da
                                                          // COMUNE.ISTAT_PROVINCIA
                                                          // da
                                                          // DB_TIPO_SPORTELLO.COMUNE

  private String note; // DB_CONTO_CORRENTE.NOTE
  private String motivoRivalidazione; // DB_CONTO_CORRENTE.MOTIVO_RIVALIDAZIONE
  private Long idTipoCausaInvalidazione; //DB_CONTO_CORRENTE.ID_TIPO_CAUSA_INVALIDAZIONE_CC
  private String descrizioneCausaInvalidazione; // DB_TIPO_CAUSA_INVALIDAZIONE_CC.DESCRIZIONE
  private String flagContoGf;
  private String flagContoVincolato;

  public Long getIdBanca()
  {
    return idBanca;
  }

  public void setIdBanca(Long idBanca)
  {
    this.idBanca = idBanca;
  }

  public void setAbi(String abi)
  {
    this.abi = abi;
  }

  public String getAbi()
  {
    return abi;
  }

  public void setDenominazioneBanca(String denominazioneBanca)
  {
    this.denominazioneBanca = denominazioneBanca;
  }

  public String getDenominazioneBanca()
  {
    return denominazioneBanca;
  }

  public void setIdSportello(Long idSportello)
  {
    this.idSportello = idSportello;
  }

  public Long getIdSportello()
  {
    return idSportello;
  }

  public void setCab(String cab)
  {
    this.cab = cab;
  }

  public String getCab()
  {
    return cab;
  }

  public void setIndirizzoSportello(String indirizzoSportello)
  {
    this.indirizzoSportello = indirizzoSportello;
  }

  public String getIndirizzoSportello()
  {
    return indirizzoSportello;
  }

  public void setCapSportello(String capSportello)
  {
    this.capSportello = capSportello;
  }

  public String getCapSportello()
  {
    return capSportello;
  }

  public void setIstatComuneSportello(String istatComuneSportello)
  {
    this.istatComuneSportello = istatComuneSportello;
  }

  public String getIstatComuneSportello()
  {
    return istatComuneSportello;
  }

  public void setDenominazioneSportello(String denominazioneSportello)
  {
    this.denominazioneSportello = denominazioneSportello;
  }

  public String getDenominazioneSportello()
  {
    return denominazioneSportello;
  }

  public void setDataInizioValiditaBanca(Date dataInizioValiditaBanca)
  {
    this.dataInizioValiditaBanca = dataInizioValiditaBanca;
  }

  public Date getDataInizioValiditaBanca()
  {
    return dataInizioValiditaBanca;
  }

  public void setDataFineValiditaBanca(Date dataFineValiditaBanca)
  {
    this.dataFineValiditaBanca = dataFineValiditaBanca;
  }

  public Date getDataFineValiditaBanca()
  {
    return dataFineValiditaBanca;
  }

  public void setDataInizioValiditaSportello(Date dataInizioValiditaSportello)
  {
    this.dataInizioValiditaSportello = dataInizioValiditaSportello;
  }

  public Date getDataInizioValiditaSportello()
  {
    return dataInizioValiditaSportello;
  }

  public void setDataFineValiditaSportello(Date dataFineValiditaSportello)
  {
    this.dataFineValiditaSportello = dataFineValiditaSportello;
  }

  public Date getDataFineValiditaSportello()
  {
    return dataFineValiditaSportello;
  }

  public void setDescrizioneComuneSportello(String descrizioneComuneSportello)
  {
    this.descrizioneComuneSportello = descrizioneComuneSportello;
  }

  public String getDescrizioneComuneSportello()
  {
    return descrizioneComuneSportello;
  }

  public void setProvinciaSportello(String provinciaSportello)
  {
    this.provinciaSportello = provinciaSportello;
  }

  public String getProvinciaSportello()
  {
    return provinciaSportello;
  }

  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }

  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }

  public ContoCorrenteVO()
  {

  }

  public Long getIdContoCorrente()
  {
    return idContoCorrente;
  }

  public void setIdContoCorrente(Long idContoCorrente)
  {
    this.idContoCorrente = idContoCorrente;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setNumeroContoCorrente(String numeroContoCorrente)
  {
    this.numeroContoCorrente = numeroContoCorrente;
  }

  public String getNumeroContoCorrente()
  {
    return numeroContoCorrente;
  }

  public void setCin(String cin)
  {
    this.cin = cin;
  }

  public String getCin()
  {
    return cin;
  }

  public void setBban(String bban)
  {
    this.bban = bban;
  }

  public String getBban()
  {
    return bban;
  }

  public void setIban(String Iban)
  {
    this.iban = Iban;
  }

  public String getIban()
  {
    return this.iban;
  }

  public void setCifraCtrl(String cCtrl)
  {
    this.cifraCtrl = cCtrl;
  }

  public String getCifraCtrl()
  {
    return this.cifraCtrl;
  }

  public void setBic(String bic)
  {
    this.bic = bic;
  }

  public String getBic()
  {
    return this.bic;
  }

  public void setflagValidato(String flagValidato)
  {
    this.flagValidato = flagValidato;
  }

  public String getflagValidato()
  {
    return this.flagValidato;
  }

  public void setCodPaese(String codPaese)
  {
    this.codPaese = codPaese;
  }

  public String getCodPaese()
  {
    return this.codPaese;
  }

  public void setIntestazione(String intestazione)
  {
    if(intestazione != null)
    {
      intestazione = intestazione.trim();
    }
    this.intestazione = intestazione;
  }

  public String getIntestazione()
  {
    if(intestazione != null)
    {
      intestazione = intestazione.trim();
    }
    return intestazione;
  }

  public void setDataInizioValiditaContoCorrente(
      java.util.Date dataInizioValiditaContoCorrente)
  {
    this.dataInizioValiditaContoCorrente = dataInizioValiditaContoCorrente;
  }

  public java.util.Date getDataInizioValiditaContoCorrente()
  {
    return dataInizioValiditaContoCorrente;
  }

  public void setDataFineValiditaContoCorrente(
      java.util.Date dataFineValiditaContoCorrente)
  {
    this.dataFineValiditaContoCorrente = dataFineValiditaContoCorrente;
  }

  public java.util.Date getDataFineValiditaContoCorrente()
  {
    return dataFineValiditaContoCorrente;
  }

  public void setDataEstinzione(java.util.Date dataEstinzione)
  {
    this.dataEstinzione = dataEstinzione;
  }

  public java.util.Date getDataEstinzione()
  {
    return dataEstinzione;
  }

  public void setDataAggiornamento(java.util.Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }

  public java.util.Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }

  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }

  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }

  public ValidationErrors validateInsert(ValidationErrors errors)
  {
    if (!Validator.isNotEmpty(this.getAbi()))
    {
      errors.add("abi", new ValidationError("Inserire il codice ABI"));
    }
    else
    {
      if (this.getAbi().length() != 5)
      {
        errors.add("abi", new ValidationError(
            "Il codice ABI deve essere di 5 cifre"));
      }
      else
      {
        if (!Validator.isNumericInteger(this.getAbi()))
        {
          errors.add("abi", new ValidationError(
              "Il codice ABI deve essere numerico"));
        }
      }
    }
    if (!Validator.isNotEmpty(this.getCab()))
    {
      errors.add("cab", new ValidationError("Inserire il codice CAB"));
    }
    else
    {
      if (this.getCab().length() != 5)
      {
        errors.add("cab", new ValidationError(
            "Il codice CAB deve essere di 5 cifre"));
      }
      else
      {
        if (!Validator.isNumericInteger(this.getCab()))
        {
          errors.add("cab", new ValidationError(
              "Il codice CAB deve essere numerico"));
        }
      }
    }

    char cinChr = ' ';
    if (Validator.isNotEmpty(this.getCin()))
    {
      this.setCin(this.getCin().toUpperCase());
      cinChr = this.getCin().toUpperCase().charAt(0);
      if (cinChr < 'A' || cinChr > 'Z')
      {
        errors.add("cin", new ValidationError(
            "Il CIN deve essere un carattere alfabetico"));
      }
    }
    // Altrimenti...
    else
    {
      // Il Cin è obbligatorio
      errors.add("cin", new ValidationError(
          AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }

    if (!Validator.isNotEmpty(this.getNumeroContoCorrente()))
    {
      errors.add("numeroContoCorrente", new ValidationError(
          "Inserire il numero di conto corrente"));
    }
    else
    {
      this.setNumeroContoCorrente(this.getNumeroContoCorrente().toUpperCase());
      int length = this.getNumeroContoCorrente().length();
      if (length != 12)
      {
        errors
            .add(
                "numeroContoCorrente",
                new ValidationError(
                    "Il numero di conto corrente deve essere composto da 12 caratteri alfanumerici"));
      }
      if (!Validator.isAlphaNumeric(getNumeroContoCorrente()))
      {
        errors
            .add(
                "numeroContoCorrente",
                new ValidationError(
                    "Il numero di conto corrente deve essere composto solo da lettere e cifre"));
      }
    }
    if (!Validator.checkIBAN(this.iban).equalsIgnoreCase("OK"))
    {
      errors.add("cin", new ValidationError(
          "Il CIN inserito relativo all''IBAN non è valido"));
    }
    // L'intestatario è obbligatorio
    if (!Validator.isNotEmpty(intestazione))
    {
      errors.add("intestatario", new ValidationError((String) AnagErrors
          .get("ERR_INTESTATARIO_OBBLIGATORIO")));
    }
    // Verifiche su cifra di controllo
    if (this.cifraCtrl.length() < 2
        || !Validator.isNumericInteger(this.cifraCtrl))
    {
      errors.add("cifraCtrl", new ValidationError(
          (String) SolmrConstants.MSG_ERR_CIFRA_CONTROLLO));
    }
    // Verifiche su IBAN - l'eventuale errore è applicato alla cifra di
    // controllo
    // (Gestire la stringa di ritorno del checkIBAN per specializzare l'errore)
    if (iban != null)
      iban = iban.toUpperCase();
    if (!Validator.checkIBAN(this.iban).equalsIgnoreCase("OK"))
    {
      errors.add("cifraCtrl", new ValidationError(
          (String) SolmrConstants.MSG_ERR_IBAN));
    }
    return errors.size() == 0 ? null : errors;
  }

  public ValidationErrors validateForUpdate(HttpServletRequest request,
      String ibanConcatenato)
  {
    ValidationErrors errors = new ValidationErrors();
    // Recupero il parametro OCIN
    String parametroOCin = (String) request.getAttribute("parametroOCin");
    char cinChr = ' ';
    if (Validator.isNotEmpty(this.getCin()))
    {
      this.setCin(this.getCin().toUpperCase());
      cinChr = this.getCin().toUpperCase().charAt(0);
      if (cinChr < 'A' || cinChr > 'Z')
      {
        errors.add("cin", new ValidationError(
            "Il CIN deve essere un carattere alfabetico"));
      }
    }
    // Altrimenti il CIN deve essere sempre valorizzato
    else
    {
      // Il Cin è obbligatorio in funzione del valore del parametro OCIN
      if (Validator.isNotEmpty(parametroOCin))
      {
        if (parametroOCin.equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          errors.add("cin", new ValidationError(
              AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
        }
      }
      else
      {
        errors.add("cin", new ValidationError(
            AnagErrors.ERRORE_KO_INSERT_CONTO_CORRENTE_FOR_PARAMETRO_OCIN));
      }
    }

    // L'intestatario è obbligatorio
    if (!Validator.isNotEmpty(intestazione))
    {
      errors.add("intestatario", new ValidationError((String) AnagErrors
          .get("ERR_INTESTATARIO_OBBLIGATORIO")));
    }
    // motivoRivalidazione
    if (!Validator.isNotEmpty(motivoRivalidazione))
    {
      if(Validator.isNotEmpty(flagValidato) && flagValidato.equalsIgnoreCase("N"))
      {
        errors.add("motivoValidazione", new ValidationError(AnagErrors.
            ERRORE_MOTIVO_VALIDAZIONE));
      }
    }    

    // Verifiche su cifra di controllo
    if (this.cifraCtrl.length() < 2
        || !Validator.isNumericInteger(this.cifraCtrl))
    {
      errors.add("cctrl", new ValidationError(
          (String) SolmrConstants.MSG_ERR_CIFRA_CONTROLLO));
    }

    // Verifiche su IBAN - l'eventuale errore è applicato alla cifra di
    // controllo
    // (Gestire la stringa di ritorno del checkIBAN per specializzare
    // l'errore)
    if (!Validator.checkIBAN(this.iban.toUpperCase()).equalsIgnoreCase("OK"))
    {
      errors.add("cctrl", new ValidationError(
          (String) SolmrConstants.MSG_ERR_IBAN));
    }
    
    if (errors.size() == 0)
      // Se non ci sono stati errori controllo che il campo IBAN (scaricasto da
      // DB)
      // e quelle calcolato concatenando le varie parti siano uguali
      if (!this.iban.equalsIgnoreCase(ibanConcatenato))
      {
        errors
            .add(
                "cctrl",
                new ValidationError(
                    "La cifra di controllo e/o il CIN non sono congruenti con l''IBAN"));
        errors
            .add(
                "cin",
                new ValidationError(
                    "La cifra di controllo e/o il CIN non sono congruenti con l''IBAN"));
      }
    return errors.size() == 0 ? null : errors;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getMotivoRivalidazione()
  {
    if(motivoRivalidazione != null)
    {
      motivoRivalidazione = motivoRivalidazione.trim();
    }
    return motivoRivalidazione;
  }

  public void setMotivoRivalidazione(String motivoRivalidazione)
  {
    if(motivoRivalidazione != null)
    {
      motivoRivalidazione = motivoRivalidazione.trim();
    }
    this.motivoRivalidazione = motivoRivalidazione;
  }

  public String getDescrizioneCausaInvalidazione()
  {
    return descrizioneCausaInvalidazione;
  }

  public void setDescrizioneCausaInvalidazione(
      String descrizioneCausaInvalidazione)
  {
    this.descrizioneCausaInvalidazione = descrizioneCausaInvalidazione;
  }
  
  
  public boolean equalsDatiContiCorrenti(Object obj) 
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final ContoCorrenteVO other = (ContoCorrenteVO) obj;
    if(this.numeroContoCorrente == null || "".equalsIgnoreCase(this.numeroContoCorrente)) {
      if(Validator.isNotEmpty(other.numeroContoCorrente)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.numeroContoCorrente)) {
        return false;
      }
      if(!this.numeroContoCorrente.equalsIgnoreCase(other.numeroContoCorrente)) {
        return false;
      }
    }
    if(this.cin == null || "".equalsIgnoreCase(this.cin)) {
      if(Validator.isNotEmpty(other.cin)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.cin)) {
        return false;
      }
      if(!this.cin.equalsIgnoreCase(other.cin)) {
        return false;
      }
    }
    if(this.iban == null || "".equalsIgnoreCase(this.iban)) {
      if(Validator.isNotEmpty(other.iban)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.iban)) {
        return false;
      }
      if(!this.iban.equalsIgnoreCase(other.iban)) {
        return false;
      }
    }
    if(this.bban == null || "".equalsIgnoreCase(this.bban)) {
      if(Validator.isNotEmpty(other.bban)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.bban)) {
        return false;
      }
      if(!this.bban.equalsIgnoreCase(other.bban)) {
        return false;
      }
    }
    if(this.cifraCtrl == null || "".equalsIgnoreCase(this.cifraCtrl)) {
      if(Validator.isNotEmpty(other.cifraCtrl)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.cifraCtrl)) {
        return false;
      }
      if(!this.cifraCtrl.equalsIgnoreCase(other.cifraCtrl)) {
        return false;
      }
    }
    if(this.flagValidato == null || "".equalsIgnoreCase(this.flagValidato)) {
      if(Validator.isNotEmpty(other.flagValidato)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.flagValidato)) {
        return false;
      }
      if(!this.flagValidato.equalsIgnoreCase(other.flagValidato)) {
        return false;
      }
    }
    if(this.bic == null || "".equalsIgnoreCase(this.bic)) {
      if(Validator.isNotEmpty(other.bic)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.bic)) {
        return false;
      }
      if(!this.bic.equalsIgnoreCase(other.bic)) {
        return false;
      }
    }
    if(this.intestazione == null || "".equalsIgnoreCase(this.intestazione)) {
      if(Validator.isNotEmpty(other.intestazione)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.intestazione)) {
        return false;
      }
      if(!this.intestazione.equalsIgnoreCase(other.intestazione)) {
        return false;
      }
    }
    if(idTipoCausaInvalidazione == null) {
      if (other.idTipoCausaInvalidazione != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idTipoCausaInvalidazione)) {
        return false;
      }
      else if(idTipoCausaInvalidazione.compareTo(other.idTipoCausaInvalidazione) != 0) {
        return false;
      }
    }
    if(idSportello == null) {
      if (other.idSportello != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idSportello)) {
        return false;
      }
      else if(idSportello.compareTo(other.idSportello) != 0) {
        return false;
      }
    }
    if(this.note == null || "".equalsIgnoreCase(this.note)) {
      if(Validator.isNotEmpty(other.note)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.note)) {
        return false;
      }
      if(!this.note.equalsIgnoreCase(other.note)) {
        return false;
      }
    }
    if(this.motivoRivalidazione == null || "".equalsIgnoreCase(this.motivoRivalidazione)) {
      if(Validator.isNotEmpty(other.motivoRivalidazione)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.motivoRivalidazione)) {
        return false;
      }
      if(!this.motivoRivalidazione.equalsIgnoreCase(other.motivoRivalidazione)) {
        return false;
      }
    }
    if(dataEstinzione == null) {
      if (other.dataEstinzione != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.dataEstinzione)) {
        return false;
      }
      else if(dataEstinzione.compareTo(other.dataEstinzione) != 0) {
        return false;
      }
    }
    if(this.flagContoGf == null || "".equalsIgnoreCase(this.flagContoGf)) {
      if(Validator.isNotEmpty(other.flagContoGf)) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.flagContoGf)) {
        return false;
      }
      if(!this.flagContoGf.equalsIgnoreCase(other.flagContoGf)) {
        return false;
      }
    }
    return true;
  }

  public Long getIdTipoCausaInvalidazione()
  {
    return idTipoCausaInvalidazione;
  }

  public void setIdTipoCausaInvalidazione(Long idTipoCausaInvalidazione)
  {
    this.idTipoCausaInvalidazione = idTipoCausaInvalidazione;
  }

  public String getFlagContoGf()
  {
    return flagContoGf;
  }

  public void setFlagContoGf(String flagContoGf)
  {
    this.flagContoGf = flagContoGf;
  }

  public String getFlagSportelloGf()
  {
    return flagSportelloGf;
  }

  public void setFlagSportelloGf(String flagSportelloGf)
  {
    this.flagSportelloGf = flagSportelloGf;
  }
  public String getFlagContoVincolato()
  {
    return flagContoVincolato;
  }

  public void setFlagContoVincolato(String flagContoVincolato)
  {
    this.flagContoVincolato = flagContoVincolato;
  }

}
