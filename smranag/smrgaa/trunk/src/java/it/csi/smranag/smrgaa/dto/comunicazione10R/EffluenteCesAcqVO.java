package it.csi.smranag.smrgaa.dto.comunicazione10R;

import it.csi.smranag.smrgaa.util.ErrorUtils;
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

public class EffluenteCesAcqVO implements Serializable
{
  

  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -3744492878346132435L;
  
  
  private long idEffluenteCesAcq10R;                       // DB_EFFLUENTE_CES_ACQ_10R.ID_EFFLUENTE_CES_ACQ_10R
  private long idComunicazione10R;                         // DB_EFFLUENTE_CES_ACQ_10R.ID_COMUNICAZIONE_10R
  private Long idAzienda;                                  // DB_EFFLUENTE_CES_ACQ_10R.ID_AZIENDA
  private String cuaa;                                     // DB_EFFLUENTE_CES_ACQ_10R.CUAA
  private String istatComune;                              // DB_EFFLUENTE_CES_ACQ_10R.ISTAT_COMUNE
  private String denominazione;                            // DB_EFFLUENTE_CES_ACQ_10R.DENOMINAZIONE
  private Long idEffluente;                                // DB_EFFLUENTE_CES_ACQ_10R.ID_EFFLUENTE
  private Long idCausaleEffluente;                         // DB_EFFLUENTE_CES_ACQ_10R.ID_CAUSALE_EFFLUENTE
  private BigDecimal quantita;                             // DB_EFFLUENTE_CES_ACQ_10R.QUANTITA
  private BigDecimal quantitaAzoto;                        // DB_EFFLUENTE_CES_ACQ_10R.QUATITA_AZOTO
  private BigDecimal quantitaAzotoDichiarato;              // DB_EFFLUENTE_CES_ACQ_10R.QUATITA_AZOTO_DICHIARATO
  private String flagStoccaggio;                           // DB_EFFLUENTE_CES_ACQ_10R.FLAG_STOCCAGGIO
  private String descrizione;                              // DB_TIPO_CAUSALE_EFFLUENTE.DESCRIZIONE
  private String descComune;                               // COMUNE.DESCOM
  private String sglProv;                                  // PROVINCIA.SIGLA_PROVINCIA
  private long idUte;                                      // DB_COMUNICAZIONE_10R.ID_UTE
  private String quantitaStr;
  private String quantitaAzotoStr;
  private String quantitaAzotoDichiaratoStr;
  private String idEffluenteStr;                                
  private String idCausaleEffluenteStr;                         
  private String descTipoEffluente;                         // DB_TIPO_EFFLUENTE.DESCRIZIONE
  private boolean flagStoccaggioBl;
  private String quantitaPreCessioniStr;
  private String quantitaAzotoPreCessioniStr;
  
  
  public boolean isFlagStoccaggioBl()
  {
    return flagStoccaggioBl;
  }
  public void setFlagStoccaggioBl(boolean flagStoccaggioBl)
  {
    this.flagStoccaggioBl = flagStoccaggioBl;
  }
  public String getQuantitaAzotoStr()
  {
    return quantitaAzotoStr;
  }
  public void setQuantitaAzotoStr(String quantitaAzotoStr)
  {
    this.quantitaAzotoStr = quantitaAzotoStr;
  }
  public String getQuantitaStr()
  {
    return quantitaStr;
  }
  public void setQuantitaStr(String quantitaStr)
  {
    this.quantitaStr = quantitaStr;
  }
  public long getIdEffluenteCesAcq10R()
  {
    return idEffluenteCesAcq10R;
  }
  public void setIdEffluenteCesAcq10R(long idEffluenteCesAcq10R)
  {
    this.idEffluenteCesAcq10R = idEffluenteCesAcq10R;
  }
  public long getIdComunicazione10R()
  {
    return idComunicazione10R;
  }
  public void setIdComunicazione10R(long idComunicazione10R)
  {
    this.idComunicazione10R = idComunicazione10R;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public Long getIdEffluente()
  {
    return idEffluente;
  }
  public void setIdEffluente(Long idEffluente)
  {
    this.idEffluente = idEffluente;
  }
  public Long getIdCausaleEffluente()
  {
    return idCausaleEffluente;
  }
  public void setIdCausaleEffluente(Long idCausaleEffluente)
  {
    this.idCausaleEffluente = idCausaleEffluente;
  }
  public BigDecimal getQuantita()
  {
    return quantita;
  }
  public void setQuantita(BigDecimal quantita)
  {
    this.quantita = quantita;
  }
  public BigDecimal getQuantitaAzoto()
  {
    return quantitaAzoto;
  }
  public void setQuantitaAzoto(BigDecimal quantitaAzoto)
  {
    this.quantitaAzoto = quantitaAzoto;
  }
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  
  public ValidationErrors validateConferma(ValidationErrors errors) 
  {    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    /*if(!Validator.isNotEmpty(idCausaleEffluenteStr))
    {
      errors = setErrorsControlNull("idCausaleEffluente",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idCausaleEffluente = new Long(idCausaleEffluenteStr);
    }*/
    
    if(!Validator.isNotEmpty(idEffluenteStr))
    {
      errors = setErrorsControlNull("idEffluente",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idEffluente = new Long(idEffluenteStr);
    }
    
    if(Validator.isNotEmpty(quantitaStr))
    {
      if(Validator.validateDouble(quantitaStr, 999999.9999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaCessAcqu",AnagErrors.ERR_QUANTITA_STOC_ERRATO);
      }
      else
      {
        quantitaStr = quantitaStr.replace(',','.');
        quantita = new BigDecimal(quantitaStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"quantitaCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(!Validator.isNotEmpty(quantitaAzoto))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"azotoCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if("S".equalsIgnoreCase(flagStoccaggio))
    {
      flagStoccaggioBl = true;
    }
    else
    {
      flagStoccaggioBl = false;
    }
    
    if(Validator.isNotEmpty(quantitaAzotoDichiaratoStr))
    {
      if(Validator.validateDoubleIncludeZero(quantitaAzotoDichiaratoStr, 999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "azotoDichiaratoCessAcqu",AnagErrors.ERR_QUANTITA_AZOTO_DICHIARATO_CESS_ACQU_ERRATO);
      }
      else
      {
        quantitaAzotoDichiaratoStr = quantitaAzotoDichiaratoStr.replace(',','.');
        quantitaAzotoDichiarato = new BigDecimal(quantitaAzotoDichiaratoStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"azotoDichiaratoCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    return errors;
  }
  
  
  
  
  
  
  public ValidationErrors validateConfermaTrattamenti() 
  { 
    ValidationErrors errors = new ValidationErrors();
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(!Validator.isNotEmpty(idEffluenteStr))
    {
      errors = setErrorsControlNull("idEffluente",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idEffluente = new Long(idEffluenteStr);
    }
    
    if(Validator.isNotEmpty(quantitaStr))
    {
      if(Validator.validateDouble(quantitaStr, 999999.9999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaCessAcqu",AnagErrors.ERR_QUANTITA_STOC_ERRATO);
      }
      else
      {
        quantitaStr = quantitaStr.replace(',','.');
        quantita = new BigDecimal(quantitaStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"quantitaCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(Validator.isNotEmpty(quantitaAzotoDichiaratoStr))
    {
      if(Validator.validateDoubleIncludeZero(quantitaAzotoDichiaratoStr, 999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "azotoDichiaratoCessAcqu",AnagErrors.ERR_QUANTITA_AZOTO_DICHIARATO_CESS_ACQU_ERRATO);
      }
      else
      {
        quantitaAzotoDichiaratoStr = quantitaAzotoDichiaratoStr.replace(',','.');
        quantitaAzotoDichiarato = new BigDecimal(quantitaAzotoDichiaratoStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"azotoDichiaratoCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    return errors;
  }
  
  
  
  public ValidationErrors validateCalcolaAcquisizioni(ValidationErrors errors) 
  {
    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    
    if(!Validator.isNotEmpty(idEffluenteStr))
    {
      errors = setErrorsControlNull("idEffluente",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idEffluente = new Long(idEffluenteStr);
    }
    
    if(Validator.isNotEmpty(quantitaStr))
    {
      if(Validator.validateDouble(quantitaStr, 999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaCessAcqu",AnagErrors.ERR_QUANTITA_STOC_ERRATO);
      }
      else
      {
        quantitaStr = quantitaStr.replace(',','.');
        quantita = new BigDecimal(quantitaStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"quantitaCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(Validator.isNotEmpty(quantitaAzotoDichiaratoStr))
    {
      if(Validator.validateDoubleIncludeZero(quantitaAzotoDichiaratoStr, 999999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "azotoDichiaratoCessAcqu",AnagErrors.ERR_QUANTITA_AZOTO_DICHIARATO_CESS_ACQU_ERRATO);
      }
      else
      {
        quantitaAzotoDichiaratoStr = quantitaAzotoDichiaratoStr.replace(',','.');
        quantitaAzotoDichiarato = new BigDecimal(quantitaAzotoDichiaratoStr);
      }
    }
    else
    {
      quantitaAzotoDichiaratoStr = "";
      quantitaAzotoDichiarato = null;
    }
    
    return errors;
  }
  
  
  
  public ValidationErrors validateConfermaCessioni(ValidationErrors errors, String flagPalabile, 
      BigDecimal maxVolNonPal, BigDecimal maxAzotoNonPal) 
  {
    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteCessAcqu",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    
    if(!Validator.isNotEmpty(idEffluenteStr))
    {
      errors = setErrorsControlNull("idEffluente",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      idEffluente = new Long(idEffluenteStr);
    }
    
    
    BigDecimal quantitaPreCessioni = new BigDecimal(0);
    if(Validator.isNotEmpty(quantitaPreCessioniStr))
    {
      quantitaPreCessioniStr = quantitaPreCessioniStr.replace(',','.');
      quantitaPreCessioni = new BigDecimal(quantitaPreCessioniStr);
    }
    
    if("N".equalsIgnoreCase(flagPalabile))
    {
      quantitaPreCessioni = maxVolNonPal;
    }
    
    if(Validator.isNotEmpty(quantitaStr))
    {
      if(Validator.validateDoubleIncludeZero(quantitaStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaCessioni",AnagErrors.ERR_CAMPO_VALIDO);
      }
      else
      {
        quantitaStr = quantitaStr.replace(',','.');
        quantita = new BigDecimal(quantitaStr);
        
        
        if(quantita.compareTo(quantitaPreCessioni) > 0 )
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "quantitaCessioni", AnagErrors.ERR_SUP_TRATT_CALC);
        }
      }
    }
    else
    {
      errors = setErrorsControlNull("quantitaCessioni",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);      
      quantita = null;
    }
    
    BigDecimal quantitaAzotoPreCessioni = new BigDecimal(0);
    if(Validator.isNotEmpty(quantitaAzotoPreCessioniStr))
    {
      quantitaAzotoPreCessioniStr = quantitaAzotoPreCessioniStr.replace(',','.');
      quantitaAzotoPreCessioni = new BigDecimal(quantitaAzotoPreCessioniStr);
    }
    
    if("N".equalsIgnoreCase(flagPalabile))
    {
      quantitaAzotoPreCessioni = maxAzotoNonPal;
    }
    
    if(Validator.isNotEmpty(quantitaAzotoStr))
    {
      if(Validator.validateDoubleIncludeZero(quantitaAzotoStr, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "azotoCessioni",AnagErrors.ERR_CAMPO_VALIDO);
      }
      else
      {
        quantitaAzotoStr = quantitaAzotoStr.replace(',','.');
        quantitaAzoto = new BigDecimal(quantitaAzotoStr);
        quantitaAzotoDichiarato = new BigDecimal(quantitaAzotoStr);
        
        
        if(quantitaAzoto.compareTo(quantitaAzotoPreCessioni) > 0 )
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "azotoCessioni", AnagErrors.ERR_SUP_TRATT_CALC);
        }
      }
    }
    else
    {
      errors = setErrorsControlNull("azotoCessioni",errors,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);      
      quantitaAzoto = null;
    }
    
    return errors;
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
  public String getIdEffluenteStr()
  {
    return idEffluenteStr;
  }
  public void setIdEffluenteStr(String idEffluenteStr)
  {
    this.idEffluenteStr = idEffluenteStr;
  }
  public String getIdCausaleEffluenteStr()
  {
    return idCausaleEffluenteStr;
  }
  public void setIdCausaleEffluenteStr(String idCausaleEffluenteStr)
  {
    this.idCausaleEffluenteStr = idCausaleEffluenteStr;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getDescTipoEffluente()
  {
    return descTipoEffluente;
  }
  public void setDescTipoEffluente(String descTipoEffluente)
  {
    this.descTipoEffluente = descTipoEffluente;
  }
  public String getFlagStoccaggio()
  {
    return flagStoccaggio;
  }
  public void setFlagStoccaggio(String flagStoccaggio)
  {
    this.flagStoccaggio = flagStoccaggio;
  }
  public long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }
  public BigDecimal getQuantitaAzotoDichiarato()
  {
    return quantitaAzotoDichiarato;
  }
  public void setQuantitaAzotoDichiarato(BigDecimal quantitaAzotoDichiarato)
  {
    this.quantitaAzotoDichiarato = quantitaAzotoDichiarato;
  }
  public String getQuantitaAzotoDichiaratoStr()
  {
    return quantitaAzotoDichiaratoStr;
  }
  public void setQuantitaAzotoDichiaratoStr(String quantitaAzotoDichiaratoStr)
  {
    this.quantitaAzotoDichiaratoStr = quantitaAzotoDichiaratoStr;
  }
  public String getQuantitaPreCessioniStr()
  {
    return quantitaPreCessioniStr;
  }
  public void setQuantitaPreCessioniStr(String quantitaPreCessioniStr)
  {
    this.quantitaPreCessioniStr = quantitaPreCessioniStr;
  }
  public String getQuantitaAzotoPreCessioniStr()
  {
    return quantitaAzotoPreCessioniStr;
  }
  public void setQuantitaAzotoPreCessioniStr(String quantitaAzotoPreCessioniStr)
  {
    this.quantitaAzotoPreCessioniStr = quantitaAzotoPreCessioniStr;
  }
  
  
  
  
  

  

}
