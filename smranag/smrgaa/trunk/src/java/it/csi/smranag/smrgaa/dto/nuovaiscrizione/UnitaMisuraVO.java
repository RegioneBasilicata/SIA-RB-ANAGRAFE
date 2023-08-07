package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import java.io.Serializable;
import java.math.BigDecimal;

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

public class UnitaMisuraVO implements Serializable
{  
 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7183596412495481172L;
  
  
  private Long idUnitaMisura;
  private String descrizione;
  private Long idUnitaMisuraDef;
  private BigDecimal coeffConversione;
  
  
  public Long getIdUnitaMisura()
  {
    return idUnitaMisura;
  }
  public void setIdUnitaMisura(Long idUnitaMisura)
  {
    this.idUnitaMisura = idUnitaMisura;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Long getIdUnitaMisuraDef()
  {
    return idUnitaMisuraDef;
  }
  public void setIdUnitaMisuraDef(Long idUnitaMisuraDef)
  {
    this.idUnitaMisuraDef = idUnitaMisuraDef;
  }
  public BigDecimal getCoeffConversione()
  {
    return coeffConversione;
  }
  public void setCoeffConversione(BigDecimal coeffConversione)
  {
    this.coeffConversione = coeffConversione;
  }
  
  
  
  
}
