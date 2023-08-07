package it.csi.solmr.dto.anag;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca D.
 * @version 1.0
 */

public class SoggettoAssociatoVO implements Serializable {
	/**
   * 
   */
  
  
  private static final long serialVersionUID = 4200772830516298472L;
  /**
   * 
   */
 
	
  private long idSoggettoAssociato;
  private String cuaa;
  private String partitaIva;
  private String denominazione;
  private String indirizzo;
  private String comune;
  private String cap;
  private String sglProv;
  private String denominazioneComune;
  
  
  
  public long getIdSoggettoAssociato()
  {
    return idSoggettoAssociato;
  }
  public void setIdSoggettoAssociato(long idSoggettoAssociato)
  {
    this.idSoggettoAssociato = idSoggettoAssociato;
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
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public String getComune()
  {
    return comune;
  }
  public void setComune(String comune)
  {
    this.comune = comune;
  }
  public String getCap()
  {
    return cap;
  }
  public void setCap(String cap)
  {
    this.cap = cap;
  }
  public String getSglProv()
  {
    return sglProv;
  }
  public void setSglProv(String sglProv)
  {
    this.sglProv = sglProv;
  }
  public String getDenominazioneComune()
  {
    return denominazioneComune;
  }
  public void setDenominazioneComune(String denominazioneComune)
  {
    this.denominazioneComune = denominazioneComune;
  }
  
  public ValidationErrors validateInserimento(ValidationErrors errors) 
  {
    
    if(!Validator.isNotEmpty(denominazione))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"denominazione",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      denominazione = denominazione.toUpperCase();
    }
    
    if(!Validator.isNotEmpty(cuaa))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"cuaa",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      cuaa = cuaa.toUpperCase();
      if((cuaa.length() !=16) && (cuaa.length() !=11))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"cuaa", AnagErrors.ERRORE_ES_CUAA_ERRATO);
      }
      else
      {
        if(cuaa.length() == 16)
        {
          if (!Validator.controlloCf(cuaa))
          {
            errors = ErrorUtils.setValidErrNoNull(errors,"cuaa", AnagErrors.ERRORE_ES_CUAA_ERRATO);
          }
        }
        
        if(cuaa.length() == 11)
        {
          if (!Validator.controlloPIVA(cuaa))
          {
            errors = ErrorUtils.setValidErrNoNull(errors,"cuaa",AnagErrors.ERRORE_ES_CUAA_ERRATO);
          }
        }
      }
    }
    
    if(Validator.isNotEmpty(partitaIva))
    {
      if (partitaIva.length() != 11 || !Validator.isNumericInteger(partitaIva))
      {
        errors = ErrorUtils.setValidErrNoNull(errors,"partitaIvaIns", AnagErrors.ERRORE_ES_PIVA_ERRATO);
      }
      else
      {
        if (!Validator.controlloPIVA(partitaIva))
        {
          errors = ErrorUtils.setValidErrNoNull(errors,"partitaIvaIns", AnagErrors.ERRORE_ES_PIVA_ERRATO);
        }
      }
    }
    
    if(!Validator.isNotEmpty(indirizzo))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"indirizzoIns",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      indirizzo = indirizzo.toUpperCase();
    }
   
    
    if(!Validator.isNotEmpty(sglProv))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"provinciaIns",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      sglProv = sglProv.toUpperCase();
    }
    
    if(!Validator.isNotEmpty(denominazioneComune))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"comuneIns",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    else
    {
      denominazioneComune = denominazioneComune.toUpperCase();
    }
    
    if(!Validator.isNotEmpty(cap))
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"capIns",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    
    return errors;
  }
  

	
}
