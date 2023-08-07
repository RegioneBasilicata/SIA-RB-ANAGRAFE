package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

import it.csi.solmr.util.*;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Castagno Raffaele
 * @version 0.1
 */

public class TipoASLAnagVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -118101549457625852L;

  private String idASL = null;
  private String descrizione = null;
  private String istatComune = null;
  private Long extIdAmmCompetenza;
  private Date dataInizioValidita = null;
  private Date dataFineValidita = null;

  public TipoASLAnagVO()
  {
  }

  public String getDump()
  {
    StringBuffer str = new StringBuffer();
    str.append(" idASL="+this.idASL
               +"\n descrizione="+this.descrizione
               +"\n istatComune="+this.istatComune);
    return str.toString();
  }

  public int hashCode()
  {
    return 0;
  }

  public boolean equals(Object o)
  {
    if (o instanceof AllevamentoAnagVO) {
      return true;
    } else
      return false;
  }


  public String getIdASL()
  {
    return idASL;
  }
  public void setIdASL(String idASL)
  {
    this.idASL = idASL;
  }
  public Long getIdASLLong()
  {
    try
    {
      return new Long(idASL);
    }
    catch (Exception ex)
    {
      return null;
    }
  }
  public void setIdASLLong(Long idASL)
  {
    this.idASL = idASL==null?null:idASL.toString();
  }

  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getIstatComune()
  {
    return istatComune;
  }
  public void setIstatComune(String istatComune)
  {
    this.istatComune = istatComune;
  }
  public Long getExtIdAmmCompetenza() {
    return extIdAmmCompetenza;
  }

  public Date getDataInizioValidita() {
    return dataInizioValidita;
  }

  public Date getDataFineValidita() {
    return dataFineValidita;
  }

  public void setExtIdAmmCompetenza(Long extIdAmmCompetenza) {
    this.extIdAmmCompetenza = extIdAmmCompetenza;
  }

  public void setDataInizioValidita(Date dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
  }

  public void setDataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

  /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN*/
  public ValidationErrors validate()
  {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  public ValidationErrors validateUpdate()
  {
    ValidationErrors errors=validate();
    return errors;
  }
  /*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END*/

}
