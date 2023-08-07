package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.util.Date;

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

public class TipoDimensioneAziendaVO implements Serializable
{ 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 26025668224428970L;
  
  
  
  private long idDimensioneAzienda;             // DB_TIPO_DIMENSIONE_AZIENDA.ID_DIMENSIONE_AZIENDA
  private String descrizione;                   // DB_TIPO_DIMENSIONE_AZIENDA.DESCRIZIONE
  private Date dataInizioValidita;              // DB_TIPO_DIMENSIONE_AZIENDA.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                // DB_TIPO_DIMENSIONE_AZIENDA.DATA_FINE_VALIDITA
  
  
  
  
  
  public long getIdDimensioneAzienda()
  {
    return idDimensioneAzienda;
  }
  public void setIdDimensioneAzienda(long idDimensioneAzienda)
  {
    this.idDimensioneAzienda = idDimensioneAzienda;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  
  
  

  

}
