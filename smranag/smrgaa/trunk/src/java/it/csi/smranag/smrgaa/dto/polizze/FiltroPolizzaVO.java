package it.csi.smranag.smrgaa.dto.polizze;

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

public class FiltroPolizzaVO implements Serializable
{ 
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6108044251569494082L;
  
  
  
  private String annoCampagna;
  private String intervento;
  private String idPolizzaAssicurativa;
  
  
  
  public String getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(String annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
  public String getIntervento()
  {
    return intervento;
  }
  public void setIntervento(String intervento)
  {
    this.intervento = intervento;
  }
  public String getIdPolizzaAssicurativa()
  {
    return idPolizzaAssicurativa;
  }
  public void setIdPolizzaAssicurativa(String idPolizzaAssicurativa)
  {
    this.idPolizzaAssicurativa = idPolizzaAssicurativa;
  }
  
  
  

  

}
