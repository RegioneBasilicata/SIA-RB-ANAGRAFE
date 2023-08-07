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

public class TipoAcquaAgronomicaVO implements Serializable
{ 
  
  /**
   * 
   */
  private static final long serialVersionUID = -556126525313845382L;
  
  
  
  
  private long idAcquaAgronomica;                          // DB_TIPO_ACQUA_AGRONOMICA.ID_ACQUA_AGRONOMICA
  private String descrizione;                              // DB_TIPO_ACQUA_AGRONOMICA.DESCRIZIONE
  private Date dataInizioValidita;                         // DB_TIPO_ACQUA_AGRONOMICA.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                           // DB_TIPO_ACQUA_AGRONOMICA.DATA_FINE_VALIDITA
  
  
  
  
  
  public long getIdAcquaAgronomica()
  {
    return idAcquaAgronomica;
  }
  public void setIdAcquaAgronomica(long idAcquaAgronomica)
  {
    this.idAcquaAgronomica = idAcquaAgronomica;
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
