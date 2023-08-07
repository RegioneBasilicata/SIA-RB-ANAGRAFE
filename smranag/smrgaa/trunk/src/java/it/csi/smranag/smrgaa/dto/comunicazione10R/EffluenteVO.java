package it.csi.smranag.smrgaa.dto.comunicazione10R;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;

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

public class EffluenteVO implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = -6700471940238728935L;
  
  
  private long idEffluente10R;                                // DB_EFFLUENTE_10R.ID_EFFLUENTE_10R
  private long idComunicazione10R;                            // DB_EFFLUENTE_10R.ID_COMUNICAZIONE_10R
  private long idEffluente;                                   // DB_EFFLUENTE_10R.ID_EFFLUENTE
  private String idEffluenteStr;                             
  private BigDecimal volumePostTrattamento;                   // DB_EFFLUENTE_10R.VOLUME_POST_TRATTAMENTO
  private BigDecimal azotoPostTrattamento;                    // DB_EFFLUENTE_10R.AZOTO_POST_TRATTAMENTO
  private BigDecimal volumePostTrattamentoNonPal;                   
  private BigDecimal azotoPostTrattamentoNonPal;                    
  private BigDecimal volumeCessione;                          // DB_EFFLUENTE_10R.VOLUME_CESSIONE
  private BigDecimal azotoCessione;                           // DB_EFFLUENTE_10R.AZOTO_CESSIONE
  private BigDecimal volumeAcquisizione;                      // DB_EFFLUENTE_10R.VOLUME_ACQUISIZIONE
  private BigDecimal azotoAcquisizione;                       // DB_EFFLUENTE_10R.AZOTO_ACQUISIZIONE
  private BigDecimal stocDispGg;                              // DB_EFFLUENTE_10R.STOC_DISPONIBILE_GG
  private BigDecimal stocNecVol;                              // DB_EFFLUENTE_10R.STOC_NECESSARIO_VOL
  private BigDecimal stocNecGg;                               // DB_EFFLUENTE_10R.STOC_NECESSARIO_GG
  private BigDecimal volumeCessioneStoccato;                  // DB_EFFLUENTE_10R.VOLUME_CESSIONE_STOCCATO
  private BigDecimal volumeAcquisizioneStoccato;              // DB_EFFLUENTE_10R.VOLUME_ACQUISIZIONE_STOCCATO
  private BigDecimal volumeInizialeCon;                       // DB_EFFLUENTE_10R.VOLUME_INIZIALE_CON
  private BigDecimal azotoInizialeDec;                        // DB_EFFLUENTE_10R.AZOTO_INIZIALE_DEC
  private BigDecimal volumePostTrattamentoCon;                // DB_EFFLUENTE_10R.VOLUME_POST_TRATTAMENTO_CON
  private BigDecimal azotoPostTrattamentoDec;                 // DB_EFFLUENTE_10R.AZOTO_POST_TRATTAMENTO_DEC
  private String tipoEffluente;                               // DB_TIPO_EFFLUENTE.FLAG_PALABILE (palabile/non palabile)
  private String descrizione;                                 // DB_TIPO_EFFLUENTE.DESCRIZIONE
  
  //Trattamenti
  private BigDecimal volumeIniziale;                          // DB_EFFLUENTE_10R.VOLUME_INIZIALE
  private String volumeInizialeStr;
  private BigDecimal azotoIniziale;                           // DB_EFFLUENTE_10R.AZOTO_INIZIALE
  private String azotoInizialeStr;
  private BigDecimal volumePostDichiarato;                    // DB_EFFLUENTE_10R.VOLUME_POST_DICHIARATO
  private String volumePostDichiaratoStr;
  private BigDecimal azotoPostDichiarato;                     // DB_EFFLUENTE_10R.AZOTO_POST_DICHIARATO
  private String azotoPostDichiaratoStr;
  private BigDecimal volumePostDichiaratoNonPal;
  private BigDecimal azotoPostDichiaratoNonPal;
  private String volumePostDichiaratoNonPalStr;
  private String azotoPostDichiaratoNonPalStr;
  private long idUte;
  private Long idTrattamento;
  private String idTrattamentoStr;
  private String volumePreTrattStr;
  private BigDecimal volumePreTratt;
  private String maxVolumePostDichiaratoStr;                   
  private String maxAzotoPostDichiaratoStr;
  private String maxVolumePostDichiaratoNonPalStr;
  private String maxAzotoPostDichiaratoNonPalStr;
  private Long idEffluenteOrigine;
  private String idEffluenteOrigineStr;
  private String codiceTrattamento;
  private Long idEffluentePalabile;
  private Long idEffluenteNonPalabile;
  private BigDecimal volumeTrattato;
  private BigDecimal volumeCessioneNoStoccato;
  
  
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getTipoEffluente()
  {
    return tipoEffluente;
  }
  public void setTipoEffluente(String tipoEffluente)
  {
    this.tipoEffluente = tipoEffluente;
  }
  public long getIdEffluente10R()
  {
    return idEffluente10R;
  }
  public void setIdEffluente10R(long idEffluente10R)
  {
    this.idEffluente10R = idEffluente10R;
  }
  public long getIdComunicazione10R()
  {
    return idComunicazione10R;
  }
  public void setIdComunicazione10R(long idComunicazione10R)
  {
    this.idComunicazione10R = idComunicazione10R;
  }
  public long getIdEffluente()
  {
    return idEffluente;
  }
  public void setIdEffluente(long idEffluente)
  {
    this.idEffluente = idEffluente;
  }
  public BigDecimal getVolumeIniziale()
  {
    return volumeIniziale;
  }
  public void setVolumeIniziale(BigDecimal volumeIniziale)
  {
    this.volumeIniziale = volumeIniziale;
  }
  public BigDecimal getAzotoIniziale()
  {
    return azotoIniziale;
  }
  public void setAzotoIniziale(BigDecimal azotoIniziale)
  {
    this.azotoIniziale = azotoIniziale;
  }
  public BigDecimal getVolumePostTrattamento()
  {
    return volumePostTrattamento;
  }
  public void setVolumePostTrattamento(BigDecimal volumePostTrattamento)
  {
    this.volumePostTrattamento = volumePostTrattamento;
  }
  public BigDecimal getVolumePostDichiarato()
  {
    return volumePostDichiarato;
  }
  public void setVolumePostDichiarato(BigDecimal volumePostDichiarato)
  {
    this.volumePostDichiarato = volumePostDichiarato;
  }
  public BigDecimal getAzotoPostTrattamento()
  {
    return azotoPostTrattamento;
  }
  public void setAzotoPostTrattamento(BigDecimal azotoPostTrattamento)
  {
    this.azotoPostTrattamento = azotoPostTrattamento;
  }
  public BigDecimal getAzotoPostDichiarato()
  {
    return azotoPostDichiarato;
  }
  public void setAzotoPostDichiarato(BigDecimal azotoPostDichiarato)
  {
    this.azotoPostDichiarato = azotoPostDichiarato;
  }
  public BigDecimal getVolumeCessione()
  {
    return volumeCessione;
  }
  public void setVolumeCessione(BigDecimal volumeCessione)
  {
    this.volumeCessione = volumeCessione;
  }
  public BigDecimal getAzotoCessione()
  {
    return azotoCessione;
  }
  public void setAzotoCessione(BigDecimal azotoCessione)
  {
    this.azotoCessione = azotoCessione;
  }
  public BigDecimal getVolumeAcquisizione()
  {
    return volumeAcquisizione;
  }
  public void setVolumeAcquisizione(BigDecimal volumeAcquisizione)
  {
    this.volumeAcquisizione = volumeAcquisizione;
  }
  public BigDecimal getAzotoAcquisizione()
  {
    return azotoAcquisizione;
  }
  public void setAzotoAcquisizione(BigDecimal azotoAcquisizione)
  {
    this.azotoAcquisizione = azotoAcquisizione;
  }
  public String getVolumePostDichiaratoStr()
  {
    return volumePostDichiaratoStr;
  }
  public void setVolumePostDichiaratoStr(String volumePostDichiaratoStr)
  {
    this.volumePostDichiaratoStr = volumePostDichiaratoStr;
  }
  public String getAzotoPostDichiaratoStr()
  {
    return azotoPostDichiaratoStr;
  }
  public void setAzotoPostDichiaratoStr(String azotoPostDichiaratoStr)
  {
    this.azotoPostDichiaratoStr = azotoPostDichiaratoStr;
  }
    
  
  public ValidationErrors validateConferma() 
  {
    ValidationErrors errors = null;
    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteTratt",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(Validator.isEmpty(idEffluenteOrigineStr))
    {
      errors = setErrorsControlNull("idEffluenteTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idEffluenteOrigine = new Long(idEffluenteOrigineStr);
    }
    
    boolean trattamentoOK = false;
    if(Validator.isEmpty(idTrattamentoStr))
    {
      errors = setErrorsControlNull("idTrattamento",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idTrattamento = new Long(idTrattamentoStr);
      trattamentoOK = true;
    }
    
    
    if(Validator.isNotEmpty(volumeInizialeStr))
    {
      if(Validator.validateDoubleIncludeZero(volumeInizialeStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaTratt",AnagErrors.ERR_VOLUME_TRATT);
      }
      else
      {
        volumeInizialeStr = volumeInizialeStr.replace(',','.');
        volumeIniziale = new BigDecimal(volumeInizialeStr);
        
        if(Validator.isNotEmpty(volumePreTrattStr))
        {
          volumePreTrattStr = volumePreTrattStr.replace(',','.');
          volumePreTratt = new BigDecimal(volumePreTrattStr);
            
          if(volumeIniziale.compareTo(volumePreTratt) > 0 )
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "quantitaTratt", AnagErrors.ERR_SUP_TRATT_CALC);
          }
        }
      }
    }
    else
    {
      errors = setErrorsControlNull("quantitaTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      volumeIniziale = null;
    }
    
    //se corretto prendo anch eil valore dell'azoto hidden
    if(Validator.isNotEmpty(azotoInizialeStr))
    {
      //Calcolo azoto iniziale
      azotoInizialeStr = azotoInizialeStr.replace(',','.');      
      azotoIniziale = new BigDecimal(azotoInizialeStr);
    }
    else
    {
      azotoIniziale = null;
    }
    
    BigDecimal maxVolumePostDichiarato = new BigDecimal(0);
    if(Validator.isNotEmpty(maxVolumePostDichiaratoStr))
    {
      maxVolumePostDichiaratoStr = maxVolumePostDichiaratoStr.replace(',','.');
      maxVolumePostDichiarato = new BigDecimal(maxVolumePostDichiaratoStr);
      if(trattamentoOK && (idTrattamento.compareTo(new Long(8)) !=0))
        volumePostTrattamento = maxVolumePostDichiarato; 
    }
    
    if(Validator.isNotEmpty(volumePostDichiaratoStr))
    {
      if(Validator.validateDoubleIncludeZero(volumePostDichiaratoStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
      }
      else
      {
        volumePostDichiaratoStr = volumePostDichiaratoStr.replace(',','.');
        volumePostDichiarato = new BigDecimal(volumePostDichiaratoStr);
        
        
        if(trattamentoOK && (idTrattamento.compareTo(SolmrConstants.TRATTAMENTO_ALTRI) != 0))
        {
          if(volumePostDichiarato.compareTo(maxVolumePostDichiarato) > 0 )
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "quantitaPalDichTratt", AnagErrors.ERR_SUP_TRATT_CALC);
          }
        }
      }
    }
    else
    {
      if(maxVolumePostDichiarato.compareTo(new BigDecimal(0)) > 0)
        errors = setErrorsControlNull("quantitaPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      
      volumePostDichiarato = null;
    }
    
    
    BigDecimal maxAzotoPostDichiarato = new BigDecimal(0);
    if(Validator.isNotEmpty(maxAzotoPostDichiaratoStr))
    {
      maxAzotoPostDichiaratoStr = maxAzotoPostDichiaratoStr.replace(',','.');
      maxAzotoPostDichiarato = new BigDecimal(maxAzotoPostDichiaratoStr);
      if(trattamentoOK && (idTrattamento.compareTo(new Long(8)) !=0))
        azotoPostTrattamento = maxAzotoPostDichiarato; 
    }
    
    if(Validator.isNotEmpty(azotoPostDichiaratoStr))
    {
      if(Validator.validateDoubleIncludeZero(azotoPostDichiaratoStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "azotoPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
      }
      else
      {
        azotoPostDichiaratoStr = azotoPostDichiaratoStr.replace(',','.');
        azotoPostDichiarato = new BigDecimal(azotoPostDichiaratoStr);
        
        if(trattamentoOK && (idTrattamento.compareTo(SolmrConstants.TRATTAMENTO_ALTRI) != 0))
        {
          if(azotoPostDichiarato.compareTo(maxAzotoPostDichiarato) > 0 )
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "azotoPalDichTratt", AnagErrors.ERR_SUP_TRATT_CALC);
          }
        }
      }
    }
    else
    {
      if(maxAzotoPostDichiarato.compareTo(new BigDecimal(0)) > 0)
        errors = setErrorsControlNull("azotoPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      
      azotoPostDichiarato = null;
    }
    
    
    BigDecimal maxVolumePostDichiaratoNonPal = new BigDecimal(0);
    if(Validator.isNotEmpty(maxVolumePostDichiaratoNonPalStr))
    {
      maxVolumePostDichiaratoNonPalStr = maxVolumePostDichiaratoNonPalStr.replace(',','.');
      maxVolumePostDichiaratoNonPal = new BigDecimal(maxVolumePostDichiaratoNonPalStr);
      if(trattamentoOK && (idTrattamento.compareTo(new Long(8)) !=0))
        volumePostTrattamentoNonPal = maxVolumePostDichiaratoNonPal; 
    }
    
    if(Validator.isNotEmpty(volumePostDichiaratoNonPalStr))
    {
      if(Validator.validateDoubleIncludeZero(volumePostDichiaratoNonPalStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaNonPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
      }
      else
      {
        volumePostDichiaratoNonPalStr = volumePostDichiaratoNonPalStr.replace(',','.');
        volumePostDichiaratoNonPal = new BigDecimal(volumePostDichiaratoNonPalStr);
        
        
        if(trattamentoOK && (idTrattamento.compareTo(SolmrConstants.TRATTAMENTO_ALTRI) != 0))
        {
          if(volumePostDichiaratoNonPal.compareTo(maxVolumePostDichiaratoNonPal) > 0 )
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "quantitaNonPalDichTratt", AnagErrors.ERR_SUP_TRATT_CALC);
          }
        }
      }
    }
    else
    {
      if(maxVolumePostDichiaratoNonPal.compareTo(new BigDecimal(0)) > 0)
        errors = setErrorsControlNull("quantitaNonPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      
      volumePostDichiaratoNonPal = null;
    }
    
    
    BigDecimal maxAzotoPostDichiaratoNonPal = new BigDecimal(0);
    if(Validator.isNotEmpty(maxAzotoPostDichiaratoNonPalStr))
    {
      maxAzotoPostDichiaratoNonPalStr = maxAzotoPostDichiaratoNonPalStr.replace(',','.');
      maxAzotoPostDichiaratoNonPal = new BigDecimal(maxAzotoPostDichiaratoNonPalStr);
      if(trattamentoOK && (idTrattamento.compareTo(new Long(8)) !=0))
        azotoPostTrattamentoNonPal = maxAzotoPostDichiaratoNonPal; 
    }
    
    if(Validator.isNotEmpty(azotoPostDichiaratoNonPalStr))
    {
      if(Validator.validateDoubleIncludeZero(azotoPostDichiaratoNonPalStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "azotoNonPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
      }
      else
      {
        azotoPostDichiaratoNonPalStr = azotoPostDichiaratoNonPalStr.replace(',','.');
        azotoPostDichiaratoNonPal = new BigDecimal(azotoPostDichiaratoNonPalStr);
        
        if(trattamentoOK && (idTrattamento.compareTo(SolmrConstants.TRATTAMENTO_ALTRI) != 0))
        {
          if(azotoPostDichiaratoNonPal.compareTo(maxAzotoPostDichiaratoNonPal) > 0 )
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "azotoNonPalDichTratt", AnagErrors.ERR_SUP_TRATT_CALC);
          }
        }
      }
    }
    else
    {
      if(maxAzotoPostDichiaratoNonPal.compareTo(new BigDecimal(0)) > 0)
        errors = setErrorsControlNull("azotoNonPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      
      azotoPostDichiaratoNonPal = null;
    }
    
    
    
    
    return errors;
  }
  
  
  public ValidationErrors validateConfermaCessazioni(boolean palabile) 
  {
    ValidationErrors errors = new ValidationErrors();
    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteTratt",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(Validator.isEmpty(idEffluenteOrigineStr))
    {
      errors = setErrorsControlNull("idEffluenteTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idEffluenteOrigine = new Long(idEffluenteOrigineStr);
    }
    
    if(Validator.isEmpty(idTrattamentoStr))
    {
      errors = setErrorsControlNull("idTrattamento",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idTrattamento = new Long(idTrattamentoStr);
    }
    
    
    if(Validator.isNotEmpty(volumeInizialeStr))
    {
      if(Validator.validateDoubleIncludeZero(volumeInizialeStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaTratt",AnagErrors.ERR_VOLUME_TRATT);
      }
    }
    else
    {
      errors = setErrorsControlNull("quantitaTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    
    if(palabile)
    {
      if(Validator.isNotEmpty(volumePostDichiaratoStr))
      {
        if(Validator.validateDoubleIncludeZero(volumePostDichiaratoStr, 999999999.9) == null) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "quantitaPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
        }
        else
        {
          volumePostDichiaratoStr = volumePostDichiaratoStr.replace(',','.');
          volumePostDichiarato = new BigDecimal(volumePostDichiaratoStr);
        }
      }
      else
      {
        errors = setErrorsControlNull("quantitaPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);      
      }
    }
    
    
    if(palabile)
    {
      if(Validator.isNotEmpty(azotoPostDichiaratoStr))
      {
        if(Validator.validateDoubleIncludeZero(azotoPostDichiaratoStr, 999999999.9) == null) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "azotoPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
        }
        else
        {
          azotoPostDichiaratoStr = azotoPostDichiaratoStr.replace(',','.');
          azotoPostDichiarato = new BigDecimal(azotoPostDichiaratoStr);
        }
      }
      else
      {
        errors = setErrorsControlNull("azotoPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO); 
      }
    }
    
    
    
    if(!palabile)
    {
      if(Validator.isNotEmpty(volumePostDichiaratoNonPalStr))
      {
        if(Validator.validateDoubleIncludeZero(volumePostDichiaratoNonPalStr, 999999999.9) == null) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "quantitaNonPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
        }
        else
        {
          volumePostDichiaratoNonPalStr = volumePostDichiaratoNonPalStr.replace(',','.');
          volumePostDichiaratoNonPal = new BigDecimal(volumePostDichiaratoNonPalStr);
        }
      }
      else
      {
        errors = setErrorsControlNull("quantitaNonPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      }
    }
    
    
    
    if(!palabile)
    {
      if(Validator.isNotEmpty(azotoPostDichiaratoNonPalStr))
      {
        if(Validator.validateDoubleIncludeZero(azotoPostDichiaratoNonPalStr, 999999999.9) == null) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "azotoNonPalDichTratt",AnagErrors.ERR_CAMPO_VALIDO);
        }
        else
        {
          azotoPostDichiaratoNonPalStr = azotoPostDichiaratoNonPalStr.replace(',','.');
          azotoPostDichiaratoNonPal = new BigDecimal(azotoPostDichiaratoNonPalStr);
        }
      }
      else
      {
        errors = setErrorsControlNull("azotoNonPalDichTratt",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      }
    }
    
    
    
    
    return errors;
  }
  
  
  public BigDecimal getStocDispGg()
  {
    return stocDispGg;
  }
  public void setStocDispGg(BigDecimal stocDispGg)
  {
    this.stocDispGg = stocDispGg;
  }
  public BigDecimal getStocNecVol()
  {
    return stocNecVol;
  }
  public void setStocNecVol(BigDecimal stocNecVol)
  {
    this.stocNecVol = stocNecVol;
  }
  public BigDecimal getStocNecGg()
  {
    return stocNecGg;
  }
  public void setStocNecGg(BigDecimal stocNecGg)
  {
    this.stocNecGg = stocNecGg;
  }
  public BigDecimal getVolumeCessioneStoccato()
  {
    return volumeCessioneStoccato;
  }
  public void setVolumeCessioneStoccato(BigDecimal volumeCessioneStoccato)
  {
    this.volumeCessioneStoccato = volumeCessioneStoccato;
  }
  public BigDecimal getVolumeAcquisizioneStoccato()
  {
    return volumeAcquisizioneStoccato;
  }
  public void setVolumeAcquisizioneStoccato(BigDecimal volumeAcquisizioneStoccato)
  {
    this.volumeAcquisizioneStoccato = volumeAcquisizioneStoccato;
  }
  public BigDecimal getVolumeInizialeCon()
  {
    return volumeInizialeCon;
  }
  public void setVolumeInizialeCon(BigDecimal volumeInizialeCon)
  {
    this.volumeInizialeCon = volumeInizialeCon;
  }
  public BigDecimal getAzotoInizialeDec()
  {
    return azotoInizialeDec;
  }
  public void setAzotoInizialeDec(BigDecimal azotoInizialeDec)
  {
    this.azotoInizialeDec = azotoInizialeDec;
  }
  public BigDecimal getVolumePostTrattamentoCon()
  {
    return volumePostTrattamentoCon;
  }
  public void setVolumePostTrattamentoCon(BigDecimal volumePostTrattamentoCon)
  {
    this.volumePostTrattamentoCon = volumePostTrattamentoCon;
  }
  public BigDecimal getAzotoPostTrattamentoDec()
  {
    return azotoPostTrattamentoDec;
  }
  public void setAzotoPostTrattamentoDec(BigDecimal azotoPostTrattamentoDec)
  {
    this.azotoPostTrattamentoDec = azotoPostTrattamentoDec;
  }
  public long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }
  public Long getIdTrattamento()
  {
    return idTrattamento;
  }
  public void setIdTrattamento(Long idTrattamento)
  {
    this.idTrattamento = idTrattamento;
  }
  public String getIdEffluenteStr()
  {
    return idEffluenteStr;
  }
  public void setIdEffluenteStr(String idEffluenteStr)
  {
    this.idEffluenteStr = idEffluenteStr;
  }
  public String getIdTrattamentoStr()
  {
    return idTrattamentoStr;
  }
  public void setIdTrattamentoStr(String idTrattamentoStr)
  {
    this.idTrattamentoStr = idTrattamentoStr;
  }
  public String getVolumeInizialeStr()
  {
    return volumeInizialeStr;
  }
  public void setVolumeInizialeStr(String volumeInizialeStr)
  {
    this.volumeInizialeStr = volumeInizialeStr;
  }
  public BigDecimal getVolumePostDichiaratoNonPal()
  {
    return volumePostDichiaratoNonPal;
  }
  public void setVolumePostDichiaratoNonPal(BigDecimal volumePostDichiaratoNonPal)
  {
    this.volumePostDichiaratoNonPal = volumePostDichiaratoNonPal;
  }
  public BigDecimal getAzotoPostDichiaratoNonPal()
  {
    return azotoPostDichiaratoNonPal;
  }
  public void setAzotoPostDichiaratoNonPal(BigDecimal azotoPostDichiaratoNonPal)
  {
    this.azotoPostDichiaratoNonPal = azotoPostDichiaratoNonPal;
  }
  public String getVolumePostDichiaratoNonPalStr()
  {
    return volumePostDichiaratoNonPalStr;
  }
  public void setVolumePostDichiaratoNonPalStr(
      String volumePostDichiaratoNonPalStr)
  {
    this.volumePostDichiaratoNonPalStr = volumePostDichiaratoNonPalStr;
  }
  public String getAzotoPostDichiaratoNonPalStr()
  {
    return azotoPostDichiaratoNonPalStr;
  }
  public void setAzotoPostDichiaratoNonPalStr(String azotoPostDichiaratoNonPalStr)
  {
    this.azotoPostDichiaratoNonPalStr = azotoPostDichiaratoNonPalStr;
  }
  
  
  private ValidationErrors setErrorsControlNull(String nomeCampo, ValidationErrors errors, String labelErrore)
  {
    if(errors == null)
    {
      errors = new ValidationErrors();
    }
    errors.add(nomeCampo, new ValidationError(labelErrore));
    
    return errors; 
  }
  public String getAzotoInizialeStr()
  {
    return azotoInizialeStr;
  }
  public void setAzotoInizialeStr(String azotoInizialeStr)
  {
    this.azotoInizialeStr = azotoInizialeStr;
  }
  public String getVolumePreTrattStr()
  {
    return volumePreTrattStr;
  }
  public void setVolumePreTrattStr(String volumePreTrattStr)
  {
    this.volumePreTrattStr = volumePreTrattStr;
  }
  public String getMaxVolumePostDichiaratoStr()
  {
    return maxVolumePostDichiaratoStr;
  }
  public void setMaxVolumePostDichiaratoStr(String maxVolumePostDichiaratoStr)
  {
    this.maxVolumePostDichiaratoStr = maxVolumePostDichiaratoStr;
  }
  public String getMaxAzotoPostDichiaratoStr()
  {
    return maxAzotoPostDichiaratoStr;
  }
  public void setMaxAzotoPostDichiaratoStr(String maxAzotoPostDichiaratoStr)
  {
    this.maxAzotoPostDichiaratoStr = maxAzotoPostDichiaratoStr;
  }
  public String getMaxVolumePostDichiaratoNonPalStr()
  {
    return maxVolumePostDichiaratoNonPalStr;
  }
  public void setMaxVolumePostDichiaratoNonPalStr(
      String maxVolumePostDichiaratoNonPalStr)
  {
    this.maxVolumePostDichiaratoNonPalStr = maxVolumePostDichiaratoNonPalStr;
  }
  public String getMaxAzotoPostDichiaratoNonPalStr()
  {
    return maxAzotoPostDichiaratoNonPalStr;
  }
  public void setMaxAzotoPostDichiaratoNonPalStr(
      String maxAzotoPostDichiaratoNonPalStr)
  {
    this.maxAzotoPostDichiaratoNonPalStr = maxAzotoPostDichiaratoNonPalStr;
  }
  public Long getIdEffluenteOrigine()
  {
    return idEffluenteOrigine;
  }
  public void setIdEffluenteOrigine(Long idEffluenteOrigine)
  {
    this.idEffluenteOrigine = idEffluenteOrigine;
  }
  public String getIdEffluenteOrigineStr()
  {
    return idEffluenteOrigineStr;
  }
  public void setIdEffluenteOrigineStr(String idEffluenteOrigineStr)
  {
    this.idEffluenteOrigineStr = idEffluenteOrigineStr;
  }
  public String getCodiceTrattamento()
  {
    return codiceTrattamento;
  }
  public void setCodiceTrattamento(String codiceTrattamento)
  {
    this.codiceTrattamento = codiceTrattamento;
  }
  public Long getIdEffluentePalabile()
  {
    return idEffluentePalabile;
  }
  public void setIdEffluentePalabile(Long idEffluentePalabile)
  {
    this.idEffluentePalabile = idEffluentePalabile;
  }
  public Long getIdEffluenteNonPalabile()
  {
    return idEffluenteNonPalabile;
  }
  public void setIdEffluenteNonPalabile(Long idEffluenteNonPalabile)
  {
    this.idEffluenteNonPalabile = idEffluenteNonPalabile;
  }
  public BigDecimal getVolumePreTratt()
  {
    return volumePreTratt;
  }
  public void setVolumePreTratt(BigDecimal volumePreTratt)
  {
    this.volumePreTratt = volumePreTratt;
  }
  public BigDecimal getVolumeTrattato()
  {
    return volumeTrattato;
  }
  public void setVolumeTrattato(BigDecimal volumeTrattato)
  {
    this.volumeTrattato = volumeTrattato;
  }
  public BigDecimal getVolumeCessioneNoStoccato()
  {
    return volumeCessioneNoStoccato;
  }
  public void setVolumeCessioneNoStoccato(BigDecimal volumeCessioneNoStoccato)
  {
    this.volumeCessioneNoStoccato = volumeCessioneNoStoccato;
  }
  public BigDecimal getVolumePostTrattamentoNonPal()
  {
    return volumePostTrattamentoNonPal;
  }
  public void setVolumePostTrattamentoNonPal(
      BigDecimal volumePostTrattamentoNonPal)
  {
    this.volumePostTrattamentoNonPal = volumePostTrattamentoNonPal;
  }
  public BigDecimal getAzotoPostTrattamentoNonPal()
  {
    return azotoPostTrattamentoNonPal;
  }
  public void setAzotoPostTrattamentoNonPal(BigDecimal azotoPostTrattamentoNonPal)
  {
    this.azotoPostTrattamentoNonPal = azotoPostTrattamentoNonPal;
  }
  

}
