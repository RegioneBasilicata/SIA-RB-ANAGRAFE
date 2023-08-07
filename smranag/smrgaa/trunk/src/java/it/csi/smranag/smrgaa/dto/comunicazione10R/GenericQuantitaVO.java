package it.csi.smranag.smrgaa.dto.comunicazione10R;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.anag.AnagErrors;
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

public class GenericQuantitaVO implements Serializable
{
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -950205199133625848L;
  
  
  
  private long idUte;                                      
  private BigDecimal quantita;
  private String quantitaStr;
  
  
  public ValidationErrors validateConferma(ValidationErrors errors, String label1, String label2) 
  {
    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors, label1, AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    
    if(Validator.isNotEmpty(quantitaStr))
    {
      if(Validator.validateDouble(quantitaStr, 999999.9999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, label2,AnagErrors.ERR_VOLUME_REFLUO_ACQUE);
      }
      else
      {
        quantitaStr = quantitaStr.replace(',','.');
        quantita = new BigDecimal(quantitaStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors, label2 ,AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    return errors;
  }
  
  
  
  public long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(long idUte)
  {
    this.idUte = idUte;
  }
  public BigDecimal getQuantita()
  {
    return quantita;
  }
  public void setQuantita(BigDecimal quantita)
  {
    this.quantita = quantita;
  }
  public String getQuantitaStr()
  {
    return quantitaStr;
  }
  public void setQuantitaStr(String quantitaStr)
  {
    this.quantitaStr = quantitaStr;
  }  

  

}
