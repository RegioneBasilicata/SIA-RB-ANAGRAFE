package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.math.BigDecimal;
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

public class TipoUdeVO implements Serializable
{ 
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6577052860374692232L;
  
  
  
  private long idUde;                           // DB_TIPO_UDE.ID_UDE
  private long classeUde;                       // DB_TIPO_UDE.CLASSE_UDE
  private long limInfUde;                       // DB_TIPO_UDE.LIM_INF_UDE
  private long limSupUde;                       // DB_TIPO_UDE.LIM_SUP_UDE
  private BigDecimal limInfRls;                 // DB_TIPO_UDE.LIM_INF_RLS
  private BigDecimal limSupRls;                 // DB_TIPO_UDE.LIM_SUP_RLS
  private Date dataInizioValidita;              // DB_TIPO_UDE.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                // DB_TIPO_UDE.DATA_FINE_VALIDITA
  
  
  public long getIdUde()
  {
    return idUde;
  }
  public void setIdUde(long idUde)
  {
    this.idUde = idUde;
  }
  public long getClasseUde()
  {
    return classeUde;
  }
  public void setClasseUde(long classeUde)
  {
    this.classeUde = classeUde;
  }
  public long getLimInfUde()
  {
    return limInfUde;
  }
  public void setLimInfUde(long limInfUde)
  {
    this.limInfUde = limInfUde;
  }
  public long getLimSupUde()
  {
    return limSupUde;
  }
  public void setLimSupUde(long limSupUde)
  {
    this.limSupUde = limSupUde;
  }
  public BigDecimal getLimInfRls()
  {
    return limInfRls;
  }
  public void setLimInfRls(BigDecimal limInfRls)
  {
    this.limInfRls = limInfRls;
  }
  public BigDecimal getLimSupRls()
  {
    return limSupRls;
  }
  public void setLimSupRls(BigDecimal limSupRls)
  {
    this.limSupRls = limSupRls;
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
