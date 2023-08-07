package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;



/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 0.1
 */

public class TipoSpecieAnimale  implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = -6962525070029591217L;
  
  private long idSpecieAnimale;     //DB_TIPO_SPECIE_ANIMALE.ID_SPECIE_ANIMALE
  private String descrizione;         //DB_TIPO_SPECIE_ANIMALE.DESCRIZIONE 
  private String unitaDiMisura;       //DB_TIPO_SPECIE_ANIMALE.UNITA_MISURA
  private String dataInizioValidita;  //DB_TIPO_SPECIE_ANIMALE.DATA_INIZIO_VALIDITA
  private String dataFineValidita;    //DB_TIPO_SPECIE_ANIMALE.DATA_FINE_VALIDITA
  private boolean flagObbligoAsl;      //DB_TIPO_SPECIE_ANIMALE.FLAG_OBBLIGO_ASL
  private Boolean flagPascolabile;      //DB_TIPO_SPECIE_ANIMALE.FLAG_PASCOLABILE
  private boolean flagStallaPascolo;      //DB_TIPO_SPECIE_ANIMALE.FLAG_STALLA_PASCOLO
  private boolean flagMofCodAzZoot;   //DB_TIPO_SPECIE_ANIMALE.FLAG_MODIFICA_COD_AZ_ZOO
  
  
  
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final TipoSpecieAnimale other = (TipoSpecieAnimale) obj;
    if (dataFineValidita == null)
    {
      if (other.dataFineValidita != null)
        return false;
    }
    else
      if (!dataFineValidita.equals(other.dataFineValidita))
        return false;
    if (dataInizioValidita == null)
    {
      if (other.dataInizioValidita != null)
        return false;
    }
    else
      if (!dataInizioValidita.equals(other.dataInizioValidita))
        return false;
    if (descrizione == null)
    {
      if (other.descrizione != null)
        return false;
    }
    else
      if (!descrizione.equals(other.descrizione))
        return false;
    if (flagMofCodAzZoot != other.flagMofCodAzZoot)
      return false;
    if (flagObbligoAsl != other.flagObbligoAsl)
      return false;
    if (flagPascolabile == null)
    {
      if (other.flagPascolabile != null)
        return false;
    }
    else
      if (!flagPascolabile.equals(other.flagPascolabile))
        return false;
    if (flagStallaPascolo != other.flagStallaPascolo)
      return false;
    if (idSpecieAnimale != other.idSpecieAnimale)
      return false;
    if (unitaDiMisura == null)
    {
      if (other.unitaDiMisura != null)
        return false;
    }
    else
      if (!unitaDiMisura.equals(other.unitaDiMisura))
        return false;
    return true;
  }
  
  
  
  public long getIdSpecieAnimale()
  {
    return idSpecieAnimale;
  }
  public void setIdSpecieAnimale(long idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale;
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
  public boolean isFlagObbligoAsl()
  {
    return flagObbligoAsl;
  }
  public void setFlagObbligoAsl(boolean flagObbligoAsl)
  {
    this.flagObbligoAsl = flagObbligoAsl;
  }
  public Boolean getFlagPascolabile()
  {
    return flagPascolabile;
  }
  public void setFlagPascolabile(Boolean flagPascolabile)
  {
    this.flagPascolabile = flagPascolabile;
  }
  public boolean isFlagStallaPascolo()
  {
    return flagStallaPascolo;
  }
  public void setFlagStallaPascolo(boolean flagStallaPascolo)
  {
    this.flagStallaPascolo = flagStallaPascolo;
  }
  public boolean isFlagMofCodAzZoot()
  {
    return flagMofCodAzZoot;
  }
  public void setFlagMofCodAzZoot(boolean flagMofCodAzZoot)
  {
    this.flagMofCodAzZoot = flagMofCodAzZoot;
  }
  
}
