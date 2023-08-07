package it.csi.smranag.smrgaa.dto.search;

import java.io.Serializable;

import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

public class FiltriRicercaTerrenoVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -1777177785167169712L;
  private String            siglaProvinciaParticella;
  private String            descComuneParticella;
  private String            descStatoEsteroParticella;
  private String            sezione;
  private String            foglio;
  private String            particella;
  private boolean           particellaProvvisoria;
  private String            subalterno;
  private boolean           particellaAttiva;
  private String            istatComuneParticella;
  private int               paginaCorrente;

  public int getPaginaCorrente()
  {
    return paginaCorrente;
  }

  public void setPaginaCorrente(int paginaCorrente)
  {
    this.paginaCorrente = paginaCorrente;
  }

  public String getSiglaProvinciaParticella()
  {
    return siglaProvinciaParticella;
  }

  public void setSiglaProvinciaParticella(String siglaProvinciaParticella)
  {
    this.siglaProvinciaParticella = siglaProvinciaParticella;
  }

  public String getDescComuneParticella()
  {
    return descComuneParticella;
  }

  public void setDescComuneParticella(String descComuneParticella)
  {
    this.descComuneParticella = descComuneParticella;
  }

  public String getDescStatoEsteroParticella()
  {
    return descStatoEsteroParticella;
  }

  public void setDescStatoEsteroParticella(String descStatoEsteroParticella)
  {
    this.descStatoEsteroParticella = descStatoEsteroParticella;
  }

  public String getSezione()
  {
    return sezione;
  }

  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }

  public String getFoglio()
  {
    return foglio;
  }

  public void setFoglio(String foglio)
  {
    this.foglio = foglio;
  }

  public String getParticella()
  {
    return particella;
  }

  public void setParticella(String particella)
  {
    this.particella = particella;
  }

  public boolean isParticellaProvvisoria()
  {
    return particellaProvvisoria;
  }

  public void setParticellaProvvisoria(boolean particellaProvvisoria)
  {
    this.particellaProvvisoria = particellaProvvisoria;
  }

  public String getSubalterno()
  {
    return subalterno;
  }

  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }

  public boolean isParticellaAttiva()
  {
    return particellaAttiva;
  }

  public void setParticellaAttiva(boolean particellaAttiva)
  {
    this.particellaAttiva = particellaAttiva;
  }

  // Metodo per effettuare la validazione formale dei dati:utilizzato nella
  // funzione ricerca terreno
  public ValidationErrors validateRicercaTerreno()
  {

    ValidationErrors errors = new ValidationErrors();

    // Se lo stato estero non è valorizzato controllo che siano stati settati i
    // valori di
    // provincia e comune
    if (!Validator.isNotEmpty(descStatoEsteroParticella))
    {
      if (!Validator.isNotEmpty(siglaProvinciaParticella))
      {
        errors.add("siglaProvinciaParticella", new ValidationError(
            (String) AnagErrors.get("ERR_SIGLA_PROV_OBBLIGATORIA")));
      }
      if (!Validator.isNotEmpty(descComuneParticella))
      {
        errors.add("descComuneParticella", new ValidationError(
            (String) AnagErrors.get("ERR_DESC_COMUNE_OBBLIGATORIO")));
      }
    }
    // Altrimenti lo stato estero è obbligatorio
    else
    {
      if (Validator.isNotEmpty(siglaProvinciaParticella))
      {
        errors.add("siglaProvinciaParticella", new ValidationError(
            (String) AnagErrors.get("ERR_PROVINCIA_NO_VALORIZZABILE")));
      }
      if (Validator.isNotEmpty(descComuneParticella))
      {
        errors.add("descComuneParticella", new ValidationError(
            (String) AnagErrors.get("ERR_COMUNE_NO_VALORIZZABILE")));
      }

    }

    // Il foglio è obbligatorio
    if (!Validator.isNotEmpty(foglio))
    {
      errors.add("foglio", new ValidationError((String) AnagErrors
          .get("ERR_FOGLIO_OBBLIGATORIO")));
    }
    // Se è stato valorizzato controllo che sia un numero intero
    else
    {
      if (!Validator.isNumericInteger(foglio))
      {
        errors.add("foglio", new ValidationError((String) AnagErrors
            .get("ERR_FOGLIO_ERRATO")));
      }
      // Se è un numero intero lo setto nel VO
      else
      {
        if (foglio.equalsIgnoreCase("0"))
        {
          errors.add("foglio", new ValidationError((String) AnagErrors
              .get("ERR_FOGLIO_ERRATO")));
        }
      }
    }

    // Se è stato selezionato il flag particella provvisoria il numero
    // particella non può essere valorizzato
    if (particellaProvvisoria)
    {
      if (Validator.isNotEmpty(particella))
      {
        errors.add("particella", new ValidationError((String) AnagErrors
            .get("ERR_STR_PARTICELLA_NO_VALORIZZABILE")));
      }
      // Anche il subalterno non può essere valorizzato se la particella è
      // provvisoria
      if (Validator.isNotEmpty(subalterno))
      {
        errors.add("subalterno", new ValidationError((String) AnagErrors
            .get("ERR_SUBALTERNO_NO_VALORIZZABILE")));
      }
    }
    // Altrimenti
    else
    {
      // Se il numero particella è stato valorizzato controllo che sia un numero
      // intero
      if (Validator.isNotEmpty(particella))
      {
        if (!Validator.isNumericInteger(particella))
        {
          errors.add("particella", new ValidationError((String) AnagErrors
              .get("ERR_PARTICELLA_ERRATA")));
        }
        else
        {
          if (particella.equalsIgnoreCase("0"))
          {
            errors.add("particella", new ValidationError((String) AnagErrors
                .get("ERR_PARTICELLA_ERRATA")));
          }
        }
      }
    }
    return errors;
  }

  public String getIstatComuneParticella()
  {
    return istatComuneParticella;
  }

  public void setIstatComuneParticella(String istatComuneParticella)
  {
    this.istatComuneParticella = istatComuneParticella;
  }
}
