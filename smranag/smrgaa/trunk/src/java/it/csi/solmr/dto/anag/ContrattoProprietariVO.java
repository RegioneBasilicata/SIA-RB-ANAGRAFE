package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Date;
import it.csi.solmr.util.*;
import it.csi.solmr.etc.anag.*;

/**
 * OGGETTO DEPRECATO: DA NON UTILIZZARE.MANTENUTO IN QUANTO UTILIZZATO DAL SERVIZIO
 * serviceGetContrattoByIdConduzioneParticella RICHIAMATO DAGLI APPLICATIVI
 * INDPAWA, SAIFGE, VITI COME DA VERIFICA EFFETTUATA IN DATA 06/02/2007. DA RIMUOVERE IL PRIMA POSSIBILE
 * 
 */

public class ContrattoProprietariVO  extends AbstractValueObject implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -7272046497776432046L;

  private String idContrattoProprietari = null; //Long
  private String idContratto = null; //Long
  private String codiceFiscale = null;
  private String denominazione = null;
  private String dataAggiornamento = null;
  private Date dataAggiornamentoDate = null;
  private String idUtenteAggiornamento = null;
  private String flagContrattoOperazione; //Long

  public ContrattoProprietariVO()
  {
  }

  /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN*/
  public ValidationErrors validate()
  {
    return new ValidationErrors();
  }


/*------------------------- VALIDATE -----------------------------------------*/

  public ValidationErrors validateDenominazioneCF()
  {
    ValidationErrors errors = new ValidationErrors();

      //Controllo Denominazione
      if(!Validator.isNotEmpty(this.denominazione))
      {
          SolmrLogger.debug(this, "\n#### Found error: La denominazione proprietario è obbligatoria.\n\n");
          errors.add("denominazione", new ValidationError("La denominazione proprietario è obbligatoria."));
      }
      else if(this.denominazione.length()>120)
      {
          SolmrLogger.debug(this, "\n#### Found error: La denominazione proprietario è troppo lunga.\n\n");
          errors.add("denominazione", new ValidationError("La denominazione è troppo lunga."));
      }
      // Controllo che il codice fiscale/partita iva sia valorizzato
      if(!Validator.isNotEmpty(this.codiceFiscale)) {
        SolmrLogger.debug(this, "\n#### Found error: Ilcodice fiscale è obbligatorio.\n\n");
        errors.add("codiceFiscale", new ValidationError("Il codice fiscale è obbligatorio."));
      }
      // Se lo è ...
      else {
        // controllo che si tratta di un codice fiscale
        if(codiceFiscale.length() == 16) {
          // sia formalmente corretto
          if(!Validator.controlloCf(this.codiceFiscale)) {
            SolmrLogger.debug(this, "\n#### Found error: Ilcodice fiscale è formalmente errato.\n\n");
            errors.add("codiceFiscale", new ValidationError("Il codice fiscale è formalmente errato."));
          }
        }
        // Se si tratta di una partita IVA
        else if(codiceFiscale.length() == 11) {
          if(!Validator.controlloPIVA(codiceFiscale)) {
            errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_PARTITA_IVA_ERRATA")));
          }
        }
        // Se il campo ha una qualsiasi altra lunghezza significa che è errato
        else {
          errors.add("codiceFiscale", new ValidationError((String)AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA")));
        }
      }

  return errors;
  }

  public ValidationErrors validateToInsert()
  {
    return validateDenominazioneCF();
  }

/*------------------------- VALIDATE -----------------------------------------*/

  /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END*/


  public String getDump()
  {
    StringBuffer str = new StringBuffer();
    str.append("\n idContratto="+this.idContratto
               +"\n idContrattoProprietari="+this.idContrattoProprietari
               +"\n idUtenteAggiornamento="+this.idUtenteAggiornamento
               +"\n dataAggiornamento="+this.dataAggiornamento
               +"\n codiceFiscale="+this.codiceFiscale
               +"\n denominazione="+this.denominazione);
    return str.toString();
  }

  public String getIdContrattoProprietari() {
    return idContrattoProprietari;
  }

  public void setIdContrattoProprietari(String idContrattoProprietari) {
    this.idContrattoProprietari = idContrattoProprietari;
  }

  public Long getIdContrattoProprietariLong() {
    try {
      return new Long(idContrattoProprietari);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdContrattoProprietariLong(Long idContrattoProprietari) {
    this.idContrattoProprietari = idContrattoProprietari == null ? null : idContrattoProprietari.toString();
  }

  public String getIdContratto() {
    return idContratto;
  }

  public void setIdContratto(String idContratto) {
    this.idContratto = idContratto;
  }

  public Long getIdContrattoLong() {
    try {
      return new Long(idContratto);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdContrattoLong(Long idContratto) {
    this.idContratto = idContratto == null ? null : idContratto.toString();
  }

  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public String getDenominazione() {
    return denominazione;
  }

  public void setDenominazione(String denominazione) {
    this.denominazione = denominazione;
  }

  public String getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(String dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
    try {
      this.dataAggiornamentoDate = DateUtils.parseDate(dataAggiornamento);
    }
    catch (Exception ex) {
    }
  }

  public Date getDataAggiornamentoDate() {
    return dataAggiornamentoDate;
  }

  public void setDataAggiornamentoDate(Date dataAggiornamentoDate) {
    this.dataAggiornamentoDate = dataAggiornamentoDate;
    try {
      this.dataAggiornamento = DateUtils.formatDate(dataAggiornamentoDate);
    }
    catch (Exception ex) {
    }
  }

  public String getIdUtenteAggiornamento() {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(String idUtenteAggiornamento) {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public Long getIdUtenteAggiornamentoLong() {
    try {
      return new Long(idUtenteAggiornamento);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdUtenteAggiornamentoLong(Long idUtenteAggiornamento) {
    this.idUtenteAggiornamento = idUtenteAggiornamento == null ? null : idUtenteAggiornamento.toString();
  }
  public void setFlagContrattoOperazione(String flagContrattoOperazione) {
    this.flagContrattoOperazione = flagContrattoOperazione;
  }
  public String getFlagContrattoOperazione() {
    return flagContrattoOperazione;
  }
}
