package it.csi.smranag.smrgaa.dto;

/**
 * Value Object per contenere coppie di id/descrizione Oct 22, 2008
 * 
 * @author TOBECONFIG (Matr. 71646)
 */
public class PlSqlCodeDescription extends BaseCodeDescription
{
  /**
   * 
   */
  private static final long serialVersionUID = -8537793404037431021L;
  
  
  private String            otherdescription;
  /** Codice secondario della decodifica (per le tabelle che lo prevedono) */



  public String getOtherdescription()
  {
    return otherdescription;
  }



  public void setOtherdescription(String otherdescription)
  {
    this.otherdescription = otherdescription;
  }
 
}
