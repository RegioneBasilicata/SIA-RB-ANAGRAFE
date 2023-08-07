package it.csi.smranag.smrgaa.dto.comunicazione10R;

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

public class TipoCausaleEffluenteVO implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 643629289819274374L;
  
  
  
  private long idCausaleEffluente;                         // DB_TIPO_CAUSALE_EFFLUENTE.ID_CAUSALE_EFFLUENTE
  private String descrizione;                              // DB_TIPO_CAUSALE_EFFLUENTE.DESCRIZIONE
  private Date dataInizioValidita;                         // DB_TIPO_CAUSALE_EFFLUENTE.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                           // DB_TIPO_CAUSALE_EFFLUENTE.DATA_FINE_VALIDITA
  
  
  
  public long getIdCausaleEffluente()
  {
    return idCausaleEffluente;
  }
  public void setIdCausaleEffluente(long idCausaleEffluente)
  {
    this.idCausaleEffluente = idCausaleEffluente;
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
