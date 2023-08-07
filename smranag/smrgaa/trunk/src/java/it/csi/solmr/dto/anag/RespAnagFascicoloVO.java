package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * <br>
 *
 * Value Object per il trasporto di dati pertinenti le superfici
 *
 * @author Luca Romanello
 * @version 1.0
 */

import java.io.Serializable;

public class RespAnagFascicoloVO implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 6686380198522207665L;
  
  
  
  private String organismoPagatore;
  private String dataAperturaFascicolo;
  private String dataChiusuraFascicolo;
  
  
  public String getOrganismoPagatore()
  {
    return organismoPagatore;
  }
  public void setOrganismoPagatore(String organismoPagatore)
  {
    this.organismoPagatore = organismoPagatore;
  }
  public String getDataAperturaFascicolo()
  {
    return dataAperturaFascicolo;
  }
  public void setDataAperturaFascicolo(String dataAperturaFascicolo)
  {
    this.dataAperturaFascicolo = dataAperturaFascicolo;
  }
  public String getDataChiusuraFascicolo()
  {
    return dataChiusuraFascicolo;
  }
  public void setDataChiusuraFascicolo(String dataChiusuraFascicolo)
  {
    this.dataChiusuraFascicolo = dataChiusuraFascicolo;
  }

  
}