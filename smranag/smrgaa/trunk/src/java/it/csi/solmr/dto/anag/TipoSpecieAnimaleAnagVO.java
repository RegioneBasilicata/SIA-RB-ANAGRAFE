package it.csi.solmr.dto.anag;

import it.csi.solmr.util.ValidationErrors;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author Castagno Raffaele
 * @version 0.1
 */

public class TipoSpecieAnimaleAnagVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long           serialVersionUID = 4797264076263987978L;

  private String              idSpecieAnimale  = null;
  private String              descrizione      = null;
  private String              unitaDiMisura    = null;
  private String              flagObbligoAsl   = null;
  private final static String FLAG_SI          = "S";
  @SuppressWarnings("unchecked")
  private Vector              categorie        = null;
  private String              dataInizioValidita;
  private String              dataFineValidita;
  private boolean             flagMofCodAzZoot;
  private boolean             flagStallaPascolo;
  private double            altLettieraPermMin;  //ALT_LETTIERA_PERMANENTE_MIN
  private double            altLettieraPermMax;  //ALT_LETTIERA_PERMANENTE_MAX
  private String            flagControlloComune;
  

  @SuppressWarnings("unchecked")
  public TipoSpecieAnimaleAnagVO()
  {
    categorie = new Vector();
  }

  public int hashCode()
  {
    return 0;
  }

  public boolean equals(Object o)
  {
    if (o instanceof AllevamentoAnagVO)
    {
      return true;
    }
    else
      return false;
  }

  /**
   * Restituisce una stringa contenente i valori di tutte le proprietà del bean
   */
  public String getDump()
  {
    StringBuffer str = new StringBuffer();
    str.append(" IdSpecieAnimale=" + this.idSpecieAnimale + "\n Descrizione=" + this.descrizione + "\n UnitaDiMisura=" + this.unitaDiMisura);
    return str.toString();
  }

  public String getIdSpecieAnimale()
  {
    return idSpecieAnimale;
  }

  public void setIdSpecieAnimale(String idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale;
  }

  public Long getIdSpecieAnimaleLong()
  {
    try
    {
      return new Long(idSpecieAnimale);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setIdSpecieAnimaleLong(Long idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale == null ? null : idSpecieAnimale.toString();
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getUnitaDiMisura()
  {
    return unitaDiMisura;
  }

  public void setUnitaDiMisura(String unitaDiMisura)
  {
    this.unitaDiMisura = unitaDiMisura;
  }

  @SuppressWarnings("unchecked")
  public Vector getCategorie()
  {
    return categorie;
  }

  @SuppressWarnings("unchecked")
  public void setCategorie(Vector categorie)
  {
    this.categorie = categorie;
  }

  public void setFlagObbligoAsl(String flagObbligoAsl)
  {
    this.flagObbligoAsl = flagObbligoAsl;
  }

  public String getFlagObbligoAsl()
  {
    return flagObbligoAsl;
  }

  public boolean isObbligoAsl()
  {
    if (flagObbligoAsl.equalsIgnoreCase(TipoSpecieAnimaleAnagVO.FLAG_SI))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public String getDataInizioValidita()
  {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(String dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }

  public String getDataFineValidita()
  {
    return dataFineValidita;
  }

  public void setDataFineValidita(String dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }

  public boolean isFlagMofCodAzZoot()
  {
    return flagMofCodAzZoot;
  }

  public void setFlagMofCodAzZoot(boolean flagMofCodAzZoot)
  {
    this.flagMofCodAzZoot = flagMofCodAzZoot;
  }
 
  public boolean isFlagStallaPascolo()
  {
    return flagStallaPascolo;
  }

  public void setFlagStallaPascolo(boolean flagStallaPascolo)
  {
    this.flagStallaPascolo = flagStallaPascolo;
  }

  /* BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN */
  public ValidationErrors validate()
  {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public ValidationErrors validateUpdate()
  {
    ValidationErrors errors = validate();
    return errors;
  }
  /* BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END */

  public double getAltLettieraPermMin()
  {
    return altLettieraPermMin;
  }

  public void setAltLettieraPermMin(double altLettieraPermMin)
  {
    this.altLettieraPermMin = altLettieraPermMin;
  }

  public double getAltLettieraPermMax()
  {
    return altLettieraPermMax;
  }

  public void setAltLettieraPermMax(double altLettieraPermMax)
  {
    this.altLettieraPermMax = altLettieraPermMax;
  }

  public String getFlagControlloComune()
  {
    return flagControlloComune;
  }

  public void setFlagControlloComune(String flagControlloComune)
  {
    this.flagControlloComune = flagControlloComune;
  }

  

}
