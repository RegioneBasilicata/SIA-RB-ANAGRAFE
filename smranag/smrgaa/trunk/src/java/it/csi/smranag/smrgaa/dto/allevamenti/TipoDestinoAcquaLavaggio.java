package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;

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
public class TipoDestinoAcquaLavaggio implements Serializable
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2931820357497820363L;
  
  
  Long idDestinoAcquaLavaggio;
  String descrizione;
  
  
  
  
  
  public Long getIdDestinoAcquaLavaggio()
  {
    return idDestinoAcquaLavaggio;
  }
  public void setIdDestinoAcquaLavaggio(Long idDestinoAcquaLavaggio)
  {
    this.idDestinoAcquaLavaggio = idDestinoAcquaLavaggio;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  
  
  
  
}
