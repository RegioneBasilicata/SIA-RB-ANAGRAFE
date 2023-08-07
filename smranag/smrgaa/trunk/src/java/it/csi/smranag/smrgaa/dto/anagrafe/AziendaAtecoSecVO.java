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

public class AziendaAtecoSecVO implements Serializable
{ 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -5100595298950569117L;
  
  
  
  private long idAziendaAtecoSec;               // DB_AZIENDA_ATECO_SEC.ID_AZIENDA_ATECO_SEC
  private long idAzienda;                       // DB_AZIENDA_ATECO_SEC.ID_AZIENDA
  private long idAttivitaAteco;                 // DB_AZIENDA_ATECO_SEC.ID_ATTIVITA_ATECO
  private String codAttivitaAteco;              // DB_TIPO_ATTIVITA_ATECO.CODICE
  private String descAttivitaAteco;             // DB_TIPO_ATTIVITA_ATECO.DESCRIZIONE
  private Date dataInizioValidita;              // DB_AZIENDA_ATECO_SEC.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                // DB_AZIENDA_ATECO_SEC.DATA_FINE_VALIDITA
  
  
  
  
  
  
  
  public long getIdAziendaAtecoSec()
  {
    return idAziendaAtecoSec;
  }
  public void setIdAziendaAtecoSec(long idAziendaAtecoSec)
  {
    this.idAziendaAtecoSec = idAziendaAtecoSec;
  }
  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public long getIdAttivitaAteco()
  {
    return idAttivitaAteco;
  }
  public void setIdAttivitaAteco(long idAttivitaAteco)
  {
    this.idAttivitaAteco = idAttivitaAteco;
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
  public String getCodAttivitaAteco()
  {
    return codAttivitaAteco;
  }
  public void setCodAttivitaAteco(String codAttivitaAteco)
  {
    this.codAttivitaAteco = codAttivitaAteco;
  }
  public String getDescAttivitaAteco()
  {
    return descAttivitaAteco;
  }
  public void setDescAttivitaAteco(String descAttivitaAteco)
  {
    this.descAttivitaAteco = descAttivitaAteco;
  }
  
  
  
  

  

}
