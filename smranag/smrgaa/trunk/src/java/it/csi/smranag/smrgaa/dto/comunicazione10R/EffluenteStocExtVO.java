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

public class EffluenteStocExtVO implements Serializable
{
  

  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -2618843846247954642L;
  
  
  private long idEffluenteStocExt10R;                      // DB_EFFLUENTE_STOC_EXT_10R.ID_EFFLUENTE_STOC_EXT_10R
  private long idComunicazione10R;                         // DB_EFFLUENTE_STOC_EXT_10R.ID_COMUNICAZIONE_10R
  private Long idAzienda;                                  // DB_EFFLUENTE_STOC_EXT_10R.ID_AZIENDA
  private String cuaa;                                     // DB_EFFLUENTE_STOC_EXT_10R.CUAA
  private String denominazione;                            // DB_EFFLUENTE_STOC_EXT_10R.DENOMINAZIONE
  private String istatComune;                              // DB_EFFLUENTE_STOC_EXT_10R.ISTAT_COMUNE
  private long idTipologiaFabbricato;                      // DB_EFFLUENTE_STOC_EXT_10R.ID_TIPOLOGIA_FABBRICATO
  private BigDecimal quantita;                             // DB_EFFLUENTE_STOC_EXT_10R.QUANTITA
  private String descrizione;                              // DB_TIPO_TIPOLOGIA_FABBRICATO.DESCRIZIONE
  private String descComune;                               // COMUNE.DESCOM
  private String sglProv;                                  // PROVINCIA.SIGLA_PROVINCIA
  private long idUte;                                      // DB_COMUNICAZIONE_10R.ID_UTE
  private String quantitaStr;                             
  
  
  public String getQuantitaStr()
  {
    return quantitaStr;
  }
  public void setQuantitaStr(String quantitaStr)
  {
    this.quantitaStr = quantitaStr;
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
  public long getIdEffluenteStocExt10R()
  {
    return idEffluenteStocExt10R;
  }
  public void setIdEffluenteStocExt10R(long idEffluenteStocExt10R)
  {
    this.idEffluenteStocExt10R = idEffluenteStocExt10R;
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
  public String getDenominazione()
  {
    return denominazione;
  }
  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }
  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public long getIdTipologiaFabbricato()
  {
    return idTipologiaFabbricato;
  }
  public void setIdTipologiaFabbricato(long idTipologiaFabbricato)
  {
    this.idTipologiaFabbricato = idTipologiaFabbricato;
  }
  public BigDecimal getQuantita()
  {
    return quantita;
  }
  public void setQuantita(BigDecimal quantita)
  {
    this.quantita = quantita;
  }
  
  
  
  public ValidationErrors validateConferma(ValidationErrors errors) 
  {
    
    if(idTipologiaFabbricato == 0)
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"idTipologiaFabbricato",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    if(Validator.isNotEmpty(quantitaStr))
    {
      if(Validator.validateDouble(quantitaStr, 999999.9999) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "quantitaStocc",AnagErrors.ERR_QUANTITA_STOC_ERRATO);
      }
      else
      {
        quantitaStr = quantitaStr.replace(',','.');
        quantita = new BigDecimal(quantitaStr);
      }
    }
    else
    {
      errors = ErrorUtils.setValidErrNoNull(errors,"quantitaStocc",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    }
    
    return errors;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
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
