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

public class AziendaSezioniVO implements Serializable
{ 
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1038383658455138233L;
  
  
  
  private long idAziendaSezioni;               
  private long idAzienda;                       
  private long idTipoSezioniAaep;                 
  private String codiceSezione;
  private String descrizione;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  
  
  public long getIdAziendaSezioni()
  {
    return idAziendaSezioni;
  }
  public void setIdAziendaSezioni(long idAziendaSezioni)
  {
    this.idAziendaSezioni = idAziendaSezioni;
  }
  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public long getIdTipoSezioniAaep()
  {
    return idTipoSezioniAaep;
  }
  public void setIdTipoSezioniAaep(long idTipoSezioniAaep)
  {
    this.idTipoSezioniAaep = idTipoSezioniAaep;
  }
  public String getCodiceSezione()
  {
    return codiceSezione;
  }
  public void setCodiceSezione(String codiceSezione)
  {
    this.codiceSezione = codiceSezione;
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
