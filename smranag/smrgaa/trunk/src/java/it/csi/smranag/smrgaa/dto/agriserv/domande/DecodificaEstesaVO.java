package it.csi.smranag.smrgaa.dto.agriserv.domande;

import java.io.*;

/**
 * Value object utilizzato da agriservSearchDomande
 *
 * @author TOBECONFIG
 * @since 2.5.0
 */
public class DecodificaEstesaVO extends DecodificaVO implements Serializable
{
  /** serialVersionUID */  
  private static final long serialVersionUID = 5967198244525751890L;
  

  private String codice;//Codice dell’entità rappresentata

  public DecodificaEstesaVO()
  {
  }

  public String getCodice()
  {
    return codice;
  }
  public void setCodice(String codice)
  {
    this.codice = codice;
  }
}
