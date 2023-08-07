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

public class TipoInfoAggiuntivaVO implements Serializable
{ 
   
  /**
   * 
   */
  private static final long serialVersionUID = 3477134504893607662L;
  
  
  
  
  private long idInfoAggiuntiva;                // DB_TIPO_INFO_AGGIUNTIVA.ID_INFO_AGGIUNTIVA
  private String codiceInfoAggiuntiva;          // DB_TIPO_INFO_AGGIUNTIVA.CODICE_INFO_AGGIUNTIVA
  private String descrizione;                   // DB_TIPO_INFO_AGGIUNTIVA.DESCRIZIONE
  private Date dataInizioValidita;              // DB_TIPO_INFO_AGGIUNTIVA.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                // DB_TIPO_INFO_AGGIUNTIVA.DATA_FINE_VALIDITA
  
  
  
  public long getIdInfoAggiuntiva()
  {
    return idInfoAggiuntiva;
  }
  public void setIdInfoAggiuntiva(long idInfoAggiuntiva)
  {
    this.idInfoAggiuntiva = idInfoAggiuntiva;
  }
  public String getCodiceInfoAggiuntiva()
  {
    return codiceInfoAggiuntiva;
  }
  public void setCodiceInfoAggiuntiva(String codiceInfoAggiuntiva)
  {
    this.codiceInfoAggiuntiva = codiceInfoAggiuntiva;
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
