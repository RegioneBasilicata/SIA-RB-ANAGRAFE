package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

import it.csi.solmr.util.*;

/**
 * OGGETTO DEPRECATO: DA NON UTILIZZARE.MANTENUTO IN QUANTO UTILIZZATO DAL SERVIZIO
 * serviceGetContrattoByIdConduzioneParticella RICHIAMATO DAGLI APPLICATIVI
 * INDPAWA, SAIFGE, VITI COME DA VERIFICA EFFETTUATA IN DATA 06/02/2007. DA RIMUOVERE IL PRIMA POSSIBILE
 */

public class ContrattoVO  extends AbstractValueObject implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -154329251301062441L;

  private String idContratto = null; //Long DB_CONTRATTO.ID_CONTRATTO
  private String idAzienda = null; //Long DB_CONTRATTO.ID_AZIENDA
  private String numeroRegistrazione = null; //String DB_CONTRATTO.NUMERO_REGISTRAZIONE
  private String dataRegistrazione = null; //Date DB_CONTRATTO.DATA_REGISTRAZIONE
  private String idTipologiaContratto = null; //Long DB_CONTRATTO.ID_TIPOLOGIA_CONTRATTO
  private String descTipologiaContratto = null; //String DB_TIPO_TIPOLOGIA_CONTRATTO.DESCRIZIONE da DB_CONTRATTO.ID_TIPOLOGIA_CONTRATTO
  private String dataInizioAffitto = null; //Date DB_CONTRATTO.DATA_INZIO_AFFITTO
  private String idScadenza = null; //Long DB_CONTRATTO.ID_SCADENZA
  private String descScadenza= null; //String DB_TIPO_SCADENZA.DESCRIZONE da  DB_CONTRATTO.
  private String dataScadenzaAffitto = null; //Date  DB_CONTRATTO.DATA_SCADENZA_AFFITTO
  private String dataAggiornamento = null; //Date  DB_CONTRATTO.DATA_AGGIORNAMENTO
  private String idUtenteAggiornamento = null; //Long DB_CONTRATTO.ID_UTENTE_AGGIORNAMENTO
  @SuppressWarnings("unchecked")
  private Vector vParticelle = null;
  @SuppressWarnings("unchecked")
  private Vector vProprietari = null;
  private String descUtenteAggiornamento; 

  public ContrattoVO()
  {
  }

  //Long
  public String getIdContratto(){return idContratto;}
  public void setIdContratto(String idContratto){this.idContratto = idContratto;}
  public Long getIdContrattoLong(){try{return new Long(idContratto);}catch (Exception ex){return null;}}
  public void setIdContrattoLong(Long idContratto){this.idContratto = idContratto==null?null:idContratto.toString();}

  //Long
  public String getIdAzienda(){return idAzienda;}
  public void setIdAzienda(String idAzienda){this.idAzienda = idAzienda;}
  public Long getIdAziendaLong(){try{return new Long(idAzienda);}catch (Exception ex){return null;}}
  public void setIdAziendaLong(Long idAzienda){this.idAzienda = idAzienda==null?null:idAzienda.toString();}

  //String
  public String getNumeroRegistrazione(){return numeroRegistrazione;}
  public void setNumeroRegistrazione(String numeroRegistrazione){this.numeroRegistrazione = numeroRegistrazione;}

  //Date
  public void setDataRegistrazione(String dataRegistrazione){this.dataRegistrazione = dataRegistrazione;}
  public String getDataRegistrazione(){return dataRegistrazione;}
  public void setDataRegistrazioneDate(Date dataRegistrazioneDate){try{this.dataRegistrazione=DateUtils.formatDate(dataRegistrazioneDate);}catch(Exception ex){}}
  public java.util.Date getDataRegistrazioneDate(){Date dataRegistrazioneDate = null;try{dataRegistrazioneDate = DateUtils.parseDate(dataRegistrazione);}catch (Exception ex){}return dataRegistrazioneDate;}

  //Long
  public String getIdTipologiaContratto(){return idTipologiaContratto;}
  public void setIdTipologiaContratto(String idTipologiaContratto){this.idTipologiaContratto = idTipologiaContratto;}
  public Long getIdTipologiaContrattoLong(){try{return new Long(idTipologiaContratto);}catch (Exception ex){return null;}}
  public void setIdTipologiaContrattoLong(Long idTipologiaContratto){this.idTipologiaContratto = idTipologiaContratto==null?null:idTipologiaContratto.toString();}

  //String
  public String getDescTipologiaContratto(){return descTipologiaContratto;}
  public void setDescTipologiaContratto(String descTipologiaContratto){this.descTipologiaContratto = descTipologiaContratto;}

  //Date
  public void setDataInizioAffitto(String dataInizioAffitto){this.dataInizioAffitto = dataInizioAffitto;}
  public String getDataInizioAffitto(){return dataInizioAffitto;}
  public void setDataInizioAffittoDate(Date dataInizioAffittoDate){try{this.dataInizioAffitto=DateUtils.formatDate(dataInizioAffittoDate);}catch(Exception ex){}}
  public java.util.Date getDataInizioAffittoDate(){Date dataInizioAffittoDate = null;try{dataInizioAffittoDate = DateUtils.parseDate(dataInizioAffitto);}catch (Exception ex){}return dataInizioAffittoDate;}

  //Long
  public String getIdScadenza(){return idScadenza;}
  public void setIdScadenza(String idScadenza){this.idScadenza = idScadenza;}
  public Long getIdScadenzaLong(){try{return new Long(idScadenza);}catch (Exception ex){return null;}}
  public void setIdScadenzaLong(Long idScadenza){this.idScadenza = idScadenza==null?null:idScadenza.toString();}

  //String
  public String getDescScadenza(){return descScadenza;}
  public void setDescScadenza(String descScadenza){this.descScadenza = descScadenza;}

  //Date
  public void setDataScadenzaAffitto(String dataScadenzaAffitto){this.dataScadenzaAffitto = dataScadenzaAffitto;}
  public String getDataScadenzaAffitto(){return dataScadenzaAffitto;}
  public void setDataScadenzaAffittoDate(Date dataScadenzaAffittoDate){try{this.dataScadenzaAffitto=DateUtils.formatDate(dataScadenzaAffittoDate);}catch(Exception ex){}}
  public java.util.Date getDataScadenzaAffittoDate(){Date dataScadenzaAffittoDate = null;try{dataScadenzaAffittoDate = DateUtils.parseDate(dataScadenzaAffitto);}catch (Exception ex){}return dataScadenzaAffittoDate;}

  //Date
  public void setDataAggiornamento(String dataAggiornamento){this.dataAggiornamento = dataAggiornamento;}
  public String getDataAggiornamento(){return dataAggiornamento;}
  public void setDataAggiornamentoDate(Date dataAggiornamentoDate){try{this.dataAggiornamento=DateUtils.formatDate(dataAggiornamentoDate);}catch(Exception ex){}}
  public java.util.Date getDataAggiornamentoDate(){Date dataAggiornamentoDate = null;try{dataAggiornamentoDate = DateUtils.parseDate(dataAggiornamento);}catch (Exception ex){}return dataAggiornamentoDate;}

  //Long
  public String getIdUtenteAggiornamento(){return idUtenteAggiornamento;}
  public void setIdUtenteAggiornamento(String idUtenteAggiornamento){this.idUtenteAggiornamento = idUtenteAggiornamento;}
  public Long getIdUtenteAggiornamentoLong(){try{return new Long(idUtenteAggiornamento);}catch (Exception ex){return null;}}
  public void setIdUtenteAggiornamentoLong(Long idUtenteAggiornamento){this.idUtenteAggiornamento = idUtenteAggiornamento==null?null:idUtenteAggiornamento.toString();}



  public String getDump()
  {
    StringBuffer str = new StringBuffer();
    str.append("\n idContratto="+this.idContratto
               +"\n idAzienda="+this.idAzienda
               +"\n numeroRegistrazione="+this.numeroRegistrazione
               +"\n dataRegistrazione="+this.dataRegistrazione
               +"\n idTipologiaContratto="+this.idTipologiaContratto
               +"\n descTipologiaContratto="+this.descTipologiaContratto
               +"\n dataInizioAffitto="+this.dataInizioAffitto
               +"\n idScadenza="+this.idScadenza
               +"\n descScadenza="+this.descScadenza
               +"\n dataScadenzaAffitto="+this.dataScadenzaAffitto
               +"\n dataAggiornamento="+this.dataAggiornamento
               +"\n idUtenteAggiornamento="+this.idUtenteAggiornamento);
    return str.toString();
  }
  @SuppressWarnings("unchecked")
  public Vector getVParticelle() {
    return vParticelle;
  }
  @SuppressWarnings("unchecked")
  public void setVParticelle(Vector vParticelle) {
    this.vParticelle = vParticelle;
  }
  @SuppressWarnings("unchecked")
  public void setVProprietari(Vector vProprietari) {
    this.vProprietari = vProprietari;
  }
  @SuppressWarnings("unchecked")
  public Vector getVProprietari() {
    return vProprietari;
  }

 /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN*/

  public ValidationErrors validate()
  {
    return new ValidationErrors();
  }

/**
* Controllo obbligatorietà e correttezza formale
* dei dati appartenenti al primo step di inserimento contratti
* @return ValidationErrors
*/
  public ValidationErrors validateContratto()
  {
    ValidationErrors errors = new ValidationErrors();

      //Controllo data inizio validità
      if(Validator.isNotEmpty(this.dataInizioAffitto))
      {
        if(!Validator.isDate(this.dataInizioAffitto))
        {
          SolmrLogger.debug(this, "\n#### Found error: Il formato della Data Inizio Validità deve essere gg/mm/aaaa\n\n");
          errors.add("dataInizioValidita", new ValidationError("Il formato della Data Inizio Validità deve essere gg/mm/aaaa."));
        }
      }
      else
      {
        SolmrLogger.debug(this, "\n#### Found error: La Data Inizio Validità è obbligatoria.\n\n");
        errors.add("dataInizioValidita", new ValidationError("La Data Inizio Validità è obbligatoria."));
      }

      //Controllo tipologia contratto
      if(Validator.isNotEmpty(this.idTipologiaContratto)) {
        if(!Validator.isNumericInteger(this.idTipologiaContratto)) {SolmrLogger.debug(this,"\n#### Found error: Il Codice Tipo Contratto è un campo numerico.\n\n");
          errors.add("tipoContratto",new ValidationError("Il Codice Tipo Contratto è un campo numerico."));
        }
        else if (this.idTipologiaContratto.length() > 4) {
          SolmrLogger.debug(this,"\n#### Found error: Il Codice Tipo Contratto è troppo lungo.\n\n");
          errors.add("tipoContratto",new ValidationError("Il Codice Tipo Contratto  è troppo lungo."));
        }
      }
      //Controllo Numero Registrazione
      if(Validator.isNotEmpty(this.numeroRegistrazione)) {
        if(this.numeroRegistrazione.length()>30) {
          SolmrLogger.debug(this, "\n#### Found error: Il Numero Registrazione è troppo lungo.\n\n");
          errors.add("numRegistrazione", new ValidationError("Il Numero Registrazione è troppo lungo."));
        }
      }

      //Controllo Data Registrazione
      if(Validator.isNotEmpty(this.dataRegistrazione)) {
        if(!Validator.isDate(this.dataRegistrazione)) {
          SolmrLogger.debug(this, "\n#### Found error: Il formato della Data Registrazione deve essere gg/mm/aaaa.\n\n");
          errors.add("dataRegistrazione", new ValidationError("Il formato della Data Registrazione deve essere gg/mm/aaaa."));
        }
      }

      //Data Scadenza
      if(Validator.isNotEmpty(this.dataScadenzaAffitto)) {
        if(!Validator.isDate(this.dataScadenzaAffitto)) {
          SolmrLogger.debug(this, "\n#### Found error: Il formato della Data Scadenza deve essere gg/mm/aaaa.\n\n");
          errors.add("dataScadenza", new ValidationError("Il formato della Data Scadenza deve essere gg/mm/aaaa."));
        }
        else if(Validator.isDate(this.dataInizioAffitto)) {
          Validator.validateDateAfterDate(this.dataInizioAffitto,this.dataScadenzaAffitto,"dataScadenza",false,errors);
        }

      }
      return errors;
  }


  public ValidationErrors validateToInsert()
  {
    return validateContratto();
  }
  /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END*/


  public void setDescUtenteAggiornamento(String descUtenteAggiornamento)
  {
    this.descUtenteAggiornamento = descUtenteAggiornamento;
  }
  public String getDescUtenteAggiornamento()
  {
    return descUtenteAggiornamento;
  }


}
