package it.csi.smranag.smrgaa.dto.comunicazione10R;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
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

public class Comunicazione10RVO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -2403615274556093064L;

  private long idComunicazione10R;                         // DB_COMUNICAZIONE_10R.ID_COMUNICAZIONE_10R
  private long idUtenteAggiornamento;                      // DB_COMUNICAZIONE_10R.ID_UTENTE_AGGIORNAMENTO
  private Date dataInizioValidita;                         // DB_COMUNICAZIONE_10R.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                           // DB_COMUNICAZIONE_10R.DATA_FINE_VALIDITA
  private Date dataAggiornamento;                          // DB_COMUNICAZIONE_10R.DATA_AGGIORNAMENTO
  private Date dataRicalcolo;                              // DB_COMUNICAZIONE_10R.DATA_RICALCOLO
  private BigDecimal volumeSottogrigliato;                 // DB_COMUNICAZIONE_10R.VOLUME_SOTTOGRIGLIATO
  private BigDecimal volumeRefluoAzienda;                  // DB_COMUNICAZIONE_10R.VOLUME_REFLUO_AZIENDA
  private BigDecimal superficieConduzioneZVN;              // DB_COMUNICAZIONE_10R.SUPERFICIE_CONDUZIONE
  private BigDecimal superficieAsservimentoZVN;            // DB_COMUNICAZIONE_10R.SUPERFICIE_ASSERVIMENTO_ZVN
  private BigDecimal superficieConduzioneNoZVN;            // DB_COMUNICAZIONE_10R.SUPERFICIE_CONDUZIONE_NO_ZVN
  private BigDecimal superficieAsservimentoNoZVN;          // DB_COMUNICAZIONE_10R.SUPERFICIE_ASSERVIMENTO_NO_ZVN
  private BigDecimal azotoConduzioneZVN;                   // DB_COMUNICAZIONE_10R.AZOTO_CONDUZIONE_ZVN
  private BigDecimal azotoAsservimentoZVN;                 // DB_COMUNICAZIONE_10R.AZOTO_ASSERVIMENTO_ZVN
  private BigDecimal azotoConduzioneNoZVN;                 // DB_COMUNICAZIONE_10R.AZOTO_CONDUZIONE_NO_ZVN
  private BigDecimal azotoAsservimentoNoZVN;               // DB_COMUNICAZIONE_10R.AZOTO_ASSERVIMENTO_NO_ZVN
  private BigDecimal totaleAzotoAziendale;                 // DB_COMUNICAZIONE_10R.TOTALE_AZOTO_AZIENDALE
  private BigDecimal stocNettoPalabile;                    // DB_COMUNICAZIONE_10R.STOC_NETTO_PALABILE
  private BigDecimal stocNettoNonPalabile;                 // DB_COMUNICAZIONE_10R.STOC_NETTO_NONPALABILE
  private BigDecimal stocDispPalabileVol;                  // DB_COMUNICAZIONE_10R.STOC_DISP_PALABILE_VOL
  private BigDecimal stocDispNonPalabileVol;               // DB_COMUNICAZIONE_10R.STOC_DISP_NONPALABILE_VOL
  private BigDecimal volumePiogge;                         // DB_COMUNICAZIONE_10R.VOLUME_PIOGGE
  private BigDecimal acqueLavaggio;                        // DB_COMUNICAZIONE_10R.ACQUE_LAVAGGIO
  private BigDecimal stocAcqueNettoCessione;               // DB_COMUNICAZIONE_10R.STOC_ACQUE_NETTO_CESSIONE
  private BigDecimal stocAcqueNecGG;                       // DB_COMUNICAZIONE_10R.STOC_ACQUE_NEC_GG
  private BigDecimal stocAcqueNecVol;                      // DB_COMUNICAZIONE_10R.STOC_ACQUE_NEC_VOL
  private String note;                                     // DB_COMUNICAZIONE_10R.NOTE
  private long idUte;                                      // DB_COMUNICAZIONE_10R.ID_UTE
  private BigDecimal totaleAzoto;                          // DB_COMUNICAZIONE_10R.TOTALE_AZOTO
  private BigDecimal superficieSauPiemonte;                // DB_COMUNICAZIONE_10R.SUPERFICIE_SAU_PIEMONTE
  private BigDecimal superficieSauPiemonteZVN;             // DB_COMUNICAZIONE_10R.SUPERFICIE_SAU_PIEMONTE_ZVN
  private String adesioneDeroga;                           // DB_COMUNICAZIONE_10R.ADESIONE_DEROGA
  private BigDecimal azotoEscretoPascolo;                  //DB_COMUNICAZIONE_!10R.AZOTO_ESCRETO_PASCOLO
  private String utenteUltimaModifica;                     // PAPUA_V_UTENTE_LOGIN.COGNOME/NOME 
  private String enteUltimaModifica;                       // PAPUA_V_UTENTE_LOGIN.DENOMINAZIONE
  private String volumeSottogrigliatoStr;                 
  private String volumeRefluoAziendaStr;               
  

  public String getVolumeSottogrigliatoStr()
  {
    return volumeSottogrigliatoStr;
  }

  public void setVolumeSottogrigliatoStr(String volumeSottogrigliatoStr)
  {
    this.volumeSottogrigliatoStr = volumeSottogrigliatoStr;
  }

  public String getVolumeRefluoAziendaStr()
  {
    return volumeRefluoAziendaStr;
  }

  public void setVolumeRefluoAziendaStr(String volumeRefluoAziendaStr)
  {
    this.volumeRefluoAziendaStr = volumeRefluoAziendaStr;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public BigDecimal getSuperficieConduzioneZVN()
  {
    return superficieConduzioneZVN;
  }

  public void setSuperficieConduzioneZVN(BigDecimal superficieConduzioneZVN)
  {
    this.superficieConduzioneZVN = superficieConduzioneZVN;
  }

  public BigDecimal getSuperficieAsservimentoZVN()
  {
    return superficieAsservimentoZVN;
  }

  public void setSuperficieAsservimentoZVN(BigDecimal superficieAsservimentoZVN)
  {
    this.superficieAsservimentoZVN = superficieAsservimentoZVN;
  }

  public BigDecimal getSuperficieConduzioneNoZVN()
  {
    return superficieConduzioneNoZVN;
  }

  public void setSuperficieConduzioneNoZVN(BigDecimal superficieConduzioneNoZVN)
  {
    this.superficieConduzioneNoZVN = superficieConduzioneNoZVN;
  }

  public BigDecimal getSuperficieAsservimentoNoZVN()
  {
    return superficieAsservimentoNoZVN;
  }

  public void setSuperficieAsservimentoNoZVN(
      BigDecimal superficieAsservimentoNoZVN)
  {
    this.superficieAsservimentoNoZVN = superficieAsservimentoNoZVN;
  }

  public BigDecimal getAzotoConduzioneZVN()
  {
    return azotoConduzioneZVN;
  }

  public void setAzotoConduzioneZVN(BigDecimal azotoConduzioneZVN)
  {
    this.azotoConduzioneZVN = azotoConduzioneZVN;
  }

  public BigDecimal getAzotoAsservimentoZVN()
  {
    return azotoAsservimentoZVN;
  }

  public void setAzotoAsservimentoZVN(BigDecimal azotoAsservimentoZVN)
  {
    this.azotoAsservimentoZVN = azotoAsservimentoZVN;
  }

  public BigDecimal getAzotoConduzioneNoZVN()
  {
    return azotoConduzioneNoZVN;
  }

  public void setAzotoConduzioneNoZVN(BigDecimal azotoConduzioneNoZVN)
  {
    this.azotoConduzioneNoZVN = azotoConduzioneNoZVN;
  }

  public BigDecimal getAzotoAsservimentoNoZVN()
  {
    return azotoAsservimentoNoZVN;
  }

  public void setAzotoAsservimentoNoZVN(BigDecimal azotoAsservimentoNoZVN)
  {
    this.azotoAsservimentoNoZVN = azotoAsservimentoNoZVN;
  }

  public BigDecimal getStocNettoPalabile()
  {
    return stocNettoPalabile;
  }

  public void setStocNettoPalabile(BigDecimal stocNettoPalabile)
  {
    this.stocNettoPalabile = stocNettoPalabile;
  }

  public BigDecimal getStocNettoNonPalabile()
  {
    return stocNettoNonPalabile;
  }

  public void setStocNettoNonPalabile(BigDecimal stocNettoNonPalabile)
  {
    this.stocNettoNonPalabile = stocNettoNonPalabile;
  }

  public BigDecimal getStocDispPalabileVol()
  {
    return stocDispPalabileVol;
  }

  public void setStocDispPalabileVol(BigDecimal stocDispPalabileVol)
  {
    this.stocDispPalabileVol = stocDispPalabileVol;
  }

  public BigDecimal getStocDispNonPalabileVol()
  {
    return stocDispNonPalabileVol;
  }

  public void setStocDispNonPalabileVol(BigDecimal stocDispNonPalabileVol)
  {
    this.stocDispNonPalabileVol = stocDispNonPalabileVol;
  }

  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }

  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }

  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }

  public Date getDataRicalcolo()
  {
    return dataRicalcolo;
  }

  public void setDataRicalcolo(Date dataRicalcolo)
  {
    this.dataRicalcolo = dataRicalcolo;
  }

  public BigDecimal getVolumeSottogrigliato()
  {
    return volumeSottogrigliato;
  }

  public void setVolumeSottogrigliato(BigDecimal volumeSottogrigliato)
  {
    this.volumeSottogrigliato = volumeSottogrigliato;
  }

  public BigDecimal getVolumeRefluoAzienda()
  {
    return volumeRefluoAzienda;
  }

  public void setVolumeRefluoAzienda(BigDecimal volumeRefluoAzienda)
  {
    this.volumeRefluoAzienda = volumeRefluoAzienda;
  }

  public long getIdComunicazione10R()
  {
    return idComunicazione10R;
  }

  public void setIdComunicazione10R(long idComunicazione10R)
  {
    this.idComunicazione10R = idComunicazione10R;
  }

  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public BigDecimal getTotaleAzotoAziendale()
  {
    return totaleAzotoAziendale;
  }

  public void setTotaleAzotoAziendale(BigDecimal totaleAzotoAziendale)
  {
    this.totaleAzotoAziendale = totaleAzotoAziendale;
  }

  public String getUtenteUltimaModifica()
  {
    return utenteUltimaModifica;
  }

  public void setUtenteUltimaModifica(String utenteUltimaModifica)
  {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public String getEnteUltimaModifica()
  {
    return enteUltimaModifica;
  }

  public void setEnteUltimaModifica(String enteUltimaModifica)
  {
    this.enteUltimaModifica = enteUltimaModifica;
  }
  
  
  public ValidationErrors validateConferma(boolean flagNoteObbligatorie) 
  {
    ValidationErrors errors = null;
    
    
    // Se le note sono state valorizzate controllo che non siano più lunghe di 300 caratteri
    // Se sono stati modificati i dichiarati tra gli effluenti le noter sono obbligatorie
    if(flagNoteObbligatorie) 
    {
      if(Validator.isNotEmpty(note)) 
      {
        if(note.length() > Integer.parseInt(SolmrConstants.LUNGHEZZA_MAX_NOTE_COMUNICAZIONE10R)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "noteComunicazione",(String)AnagErrors.get("ERR_NOTE_NO_MAX_TRECENTO"));
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "noteComunicazione",AnagErrors.NOTE_OBBLIGATORIE);
      }
      
    }
    else
    {
      if(Validator.isNotEmpty(note)) 
      {
        if(note.length() > Integer.parseInt(SolmrConstants.LUNGHEZZA_MAX_NOTE_COMUNICAZIONE10R)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "noteComunicazione",(String)AnagErrors.get("ERR_NOTE_NO_MAX_TRECENTO"));
        }
      }
      else
      {
        note = "";
      }
    }
    
    return errors;
  }

  public BigDecimal getVolumePiogge()
  {
    return volumePiogge;
  }

  public void setVolumePiogge(BigDecimal volumePiogge)
  {
    this.volumePiogge = volumePiogge;
  }

  public BigDecimal getAcqueLavaggio()
  {
    return acqueLavaggio;
  }

  public void setAcqueLavaggio(BigDecimal acqueLavaggio)
  {
    this.acqueLavaggio = acqueLavaggio;
  }

  public long getIdUte()
  {
    return idUte;
  }

  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }

  public BigDecimal getStocAcqueNettoCessione()
  {
    return stocAcqueNettoCessione;
  }

  public void setStocAcqueNettoCessione(BigDecimal stocAcqueNettoCessione)
  {
    this.stocAcqueNettoCessione = stocAcqueNettoCessione;
  }

  public BigDecimal getStocAcqueNecGG()
  {
    return stocAcqueNecGG;
  }

  public void setStocAcqueNecGG(BigDecimal stocAcqueNecGG)
  {
    this.stocAcqueNecGG = stocAcqueNecGG;
  }

  public BigDecimal getStocAcqueNecVol()
  {
    return stocAcqueNecVol;
  }

  public void setStocAcqueNecVol(BigDecimal stocAcqueNecVol)
  {
    this.stocAcqueNecVol = stocAcqueNecVol;
  }

  public BigDecimal getTotaleAzoto()
  {
    return totaleAzoto;
  }

  public void setTotaleAzoto(BigDecimal totaleAzoto)
  {
    this.totaleAzoto = totaleAzoto;
  }

  public String getAdesioneDeroga()
  {
    return adesioneDeroga;
  }

  public void setAdesioneDeroga(String adesioneDeroga)
  {
    this.adesioneDeroga = adesioneDeroga;
  }

  public BigDecimal getAzotoEscretoPascolo()
  {
    return azotoEscretoPascolo;
  }

  public void setAzotoEscretoPascolo(BigDecimal azotoEscretoPascolo)
  {
    this.azotoEscretoPascolo = azotoEscretoPascolo;
  }

  public BigDecimal getSuperficieSauPiemonte()
  {
    return superficieSauPiemonte;
  }

  public void setSuperficieSauPiemonte(BigDecimal superficieSauPiemonte)
  {
    this.superficieSauPiemonte = superficieSauPiemonte;
  }

  public BigDecimal getSuperficieSauPiemonteZVN()
  {
    return superficieSauPiemonteZVN;
  }

  public void setSuperficieSauPiemonteZVN(BigDecimal superficieSauPiemonteZVN)
  {
    this.superficieSauPiemonteZVN = superficieSauPiemonteZVN;
  }
  
  
  
  
}
