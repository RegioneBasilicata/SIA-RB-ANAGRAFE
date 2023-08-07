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

public class AcquaExtraVO implements Serializable
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -2219262251395350222L;
  
  
  private long idAcquaExtra10R;                            // DB_ACQUA_EXTRA_10R.ID_ACQUA_EXTRA_10R
  private long idComunicazione10R;                         // DB_ACQUA_EXTRA_10R.ID_COMUNICAZIONE_10R
  private long idAcquaAgronomica;                          // DB_ACQUA_EXTRA_10R.ID_ACQUA_AGRONOMICA
  private BigDecimal volumeRefluo;                         // DB_ACQUA_EXTRA_10R.VOLUME_REFLUO
  private String descrizione;                              // DB_TIPO_ACQUA_AGRONOMICA.DESCRIZIONE
  private long idUte;
  private String volumeRefluoStr;
  
  
  
  
  
  public long getIdAcquaExtra10R()
  {
    return idAcquaExtra10R;
  }
  public void setIdAcquaExtra10R(long idAcquaExtra10R)
  {
    this.idAcquaExtra10R = idAcquaExtra10R;
  }
  public long getIdComunicazione10R()
  {
    return idComunicazione10R;
  }
  public void setIdComunicazione10R(long idComunicazione10R)
  {
    this.idComunicazione10R = idComunicazione10R;
  }
  public long getIdAcquaAgronomica()
  {
    return idAcquaAgronomica;
  }
  public void setIdAcquaAgronomica(long idAcquaAgronomica)
  {
    this.idAcquaAgronomica = idAcquaAgronomica;
  }
  public BigDecimal getVolumeRefluo()
  {
    return volumeRefluo;
  }
  public void setVolumeRefluo(BigDecimal volumeRefluo)
  {
    this.volumeRefluo = volumeRefluo;
  }
  public String getVolumeRefluoStr()
  {
    return volumeRefluoStr;
  }
  public void setVolumeRefluoStr(String volumeRefluoStr)
  {
    this.volumeRefluoStr = volumeRefluoStr;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }                           
  
  
  public ValidationErrors validateConferma(ValidationErrors errors) 
  {
    
    if(idAcquaAgronomica == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idTipologiaAcquaAgronomica",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(idUte == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idUteAcqueReflue",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    
    if(Validator.isNotEmpty(volumeRefluoStr))
    {
      if(Validator.validateDouble(volumeRefluoStr, 999999.9999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "volumeRefluo",AnagErrors.ERR_VOLUME_REFLUO_ACQUE);
      }
      else
      {
        volumeRefluoStr = volumeRefluoStr.replace(',','.');
        volumeRefluo = new BigDecimal(volumeRefluoStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"volumeRefluo",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
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
  
  
  
  
  

  

}
