package it.csi.smranag.smrgaa.dto.search;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.HashMap;

public class FiltriRicercaAziendeCollegateVO implements Serializable
{
 
 
  /**
   * 
   */
  private static final long serialVersionUID = 5942878041208161704L;
  
  
  private String            provinciaRicerca;
  private String            comuneRicerca;
  private String            istatComuneRicerca;
  private String            cuaaRicerca;
  private String            partitaIvaRicerca;
  private String            denominazioneRicerca;
  private int               paginaCorrente;
  private Long              idAzienda;
  private HashMap<String,String>      hIdSelezionati;
  private boolean           storico;
  

  public boolean isStorico()
  {
    return storico;
  }

  public void setStorico(boolean storico)
  {
    this.storico = storico;
  }

  public HashMap<String,String> getHIdSelezionati()
  {
    return hIdSelezionati;
  }

  public void setHIdSelezionati(HashMap<String,String> idSelezionati)
  {
    hIdSelezionati = idSelezionati;
  }

  public int getPaginaCorrente()
  {
    return paginaCorrente;
  }

  public void setPaginaCorrente(int paginaCorrente)
  {
    this.paginaCorrente = paginaCorrente;
  }

  public String getProvinciaRicerca()
  {
    return provinciaRicerca;
  }

  public void setProvinciaRicerca(String provinciaRicerca)
  {
    this.provinciaRicerca = provinciaRicerca;
  }

  public String getComuneRicerca()
  {
    return comuneRicerca;
  }

  public void setComuneRicerca(String comuneRicerca)
  {
    this.comuneRicerca = comuneRicerca;
  }

  public String getIstatComuneRicerca()
  {
    return istatComuneRicerca;
  }

  public void setIstatComuneRicerca(String istatComuneRicerca)
  {
    this.istatComuneRicerca = istatComuneRicerca;
  }

  public String getCuaaRicerca()
  {
    return cuaaRicerca;
  }

  public void setCuaaRicerca(String cuaaRicerca)
  {
    this.cuaaRicerca = cuaaRicerca;
  }

  public String getPartitaIvaRicerca()
  {
    return partitaIvaRicerca;
  }

  public void setPartitaIvaRicerca(String partitaIvaRicerca)
  {
    this.partitaIvaRicerca = partitaIvaRicerca;
  }

  public String getDenominazioneRicerca()
  {
    return denominazioneRicerca;
  }

  public void setDenominazioneRicerca(String denominazioneRicerca)
  {
    this.denominazioneRicerca = denominazioneRicerca;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  
  public ValidationErrors validateRicerca(ValidationErrors errors)
  {

    if(Validator.isNotEmpty(cuaaRicerca))
    {      
      // Se si tratta di un codice fiscale
      if(cuaaRicerca.length() == 16) 
      {
        if(!Validator.controlloCf(cuaaRicerca)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaaRicerca", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      else if (cuaaRicerca.length() == 11) 
      {
        if(!Validator.controlloPIVA(cuaaRicerca)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "cuaaRicerca", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      else //diverso da 11 e da 16
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "cuaaRicerca", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
      }
    }
    
    if(Validator.isNotEmpty(partitaIvaRicerca))
    {
      if (partitaIvaRicerca.length() == 11) 
      {
        if(!Validator.controlloPIVA(partitaIvaRicerca)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "partitaIvaRicerca", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
        }
      }
      else //diverso da 11
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "partitaIvaRicerca", (String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
      }
    }   
    
    return errors;
  }

  
}
