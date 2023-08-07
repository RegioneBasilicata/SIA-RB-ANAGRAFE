package it.csi.smranag.smrgaa.dto.search;

import java.util.HashMap;

import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;


public class FiltriRicercaTerrenoImportaAsservimentoVO extends FiltriRicercaTerrenoVO
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2932148712837565687L;
  
  
  
  private long              idAziendaSearch;
  private String            CUAA;
  private String            tipoRicerca;
  private HashMap<String,Long>           hIdSelezionati;

  public String getCUAA()
  {
    return CUAA;
  }

  public HashMap<String,Long> getHIdSelezionati()
  {
    return hIdSelezionati;
  }

  public void setHIdSelezionati(HashMap<String,Long> idSelezionati)
  {
    hIdSelezionati = idSelezionati;
  }

  public void setCUAA(String cuaa)
  {
    CUAA = cuaa;
  }

  
  
  public long getIdAziendaSearch()
  {
    return idAziendaSearch;
  }

  public void setIdAziendaSearch(long idAziendaSearch)
  {
    this.idAziendaSearch = idAziendaSearch;
  }

  // Metodo per effettuare la validazione formale dei dati:utilizzato nella
  // funzione ricerca con CUAA
  public ValidationErrors validateRicercaCUAA(String cuaaAziendaCorrente)
  {

    ValidationErrors errors = null;
    // Controllo che il CUAA sia stato valorizzato
    if(!Validator.isNotEmpty(CUAA)) 
    {
      if(errors == null)
      {
        errors = new ValidationErrors();
      }
      errors.add("cuaa", new ValidationError((String)AnagErrors.get("ERR_INSERIRE_CUAA")));
    }
    // Se è valorizzato...
    else 
    {
      //session.setAttribute("cuaaImportaAsservimento", cuaa);
      // Controllo che non sia lo stesso dell'azienda su cui si sta lavorando
      if(CUAA.toUpperCase().equalsIgnoreCase(cuaaAziendaCorrente.toUpperCase())) 
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("cuaa", new ValidationError((String)AnagErrors.get("ERR_CUAA_EQUALS_AZIENDA_IMPORT")));
      }
      // Se non lo è controllo la correttezza formale
      else 
      {
        // Se si tratta di un codice fiscale
        if(CUAA.length() == 16) 
        {
          if(!Validator.controlloCf(CUAA)) 
          {
            if(errors == null)
            {
              errors = new ValidationErrors();
            }
            errors.add("cuaa", new ValidationError((String)AnagErrors.get("ERR_CUAA_NO_CORRETTO")));
          }
        }
        else 
        {
          if(!Validator.controlloPIVA(CUAA)) 
          {
            if(errors == null)
            {
              errors = new ValidationErrors();
            }
            errors.add("cuaa", new ValidationError((String)AnagErrors.get("ERR_CUAA_NO_CORRETTO")));
          }
        }
      }
    }
    return errors;
  }

  public String getTipoRicerca()
  {
    return tipoRicerca;
  }

  public void setTipoRicerca(String tipoRicerca)
  {
    this.tipoRicerca = tipoRicerca;
  }
  

  
}
