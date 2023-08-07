package it.csi.solmr.dto.anag;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class TesserinoFitoSanitarioVO implements Serializable
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 7360438461372766590L;
  
  
  
  private String descTipoCertificato;
  private Date dataScadenza;
  private Date dataRilascio;
  private String flagTesserinoScaduto;
  
  
  
  
  public String getDescTipoCertificato()
  {
    return descTipoCertificato;
  }
  public void setDescTipoCertificato(String descTipoCertificato)
  {
    this.descTipoCertificato = descTipoCertificato;
  }
  public Date getDataScadenza()
  {
    return dataScadenza;
  }
  public void setDataScadenza(Date dataScadenza)
  {
    this.dataScadenza = dataScadenza;
  }
  public Date getDataRilascio()
  {
    return dataRilascio;
  }
  public void setDataRilascio(Date dataRilascio)
  {
    this.dataRilascio = dataRilascio;
  }
  public String getFlagTesserinoScaduto()
  {
    return flagTesserinoScaduto;
  }
  public void setFlagTesserinoScaduto(String flagTesserinoScaduto)
  {
    this.flagTesserinoScaduto = flagTesserinoScaduto;
  }
  
  
  
  
  
}