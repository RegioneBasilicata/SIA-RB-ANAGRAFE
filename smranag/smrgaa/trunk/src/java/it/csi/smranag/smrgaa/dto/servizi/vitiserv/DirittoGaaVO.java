/**
 * DirittoGaaVO.java
 *
 * VO utilizzato per wrappare DirittoVO restituito da vitiServ
 */

package it.csi.smranag.smrgaa.dto.servizi.vitiserv;

import it.csi.smrvit.vitiserv.dto.diritto.DirittoVO;

public class DirittoGaaVO  implements java.io.Serializable
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 825329423696691387L;
  
  
  DirittoVO diritto;
  String siglaProvinciaOrigine;
  String descAmmCompetenzaReimpianto;
  String varieta;
  String vino;
  boolean scaduto;
  

  public String getSiglaProvinciaOrigine()
  {
    return siglaProvinciaOrigine;
  }

  public void setSiglaProvinciaOrigine(String siglaProvinciaOrigine)
  {
    this.siglaProvinciaOrigine = siglaProvinciaOrigine;
  }

  public String getDescAmmCompetenzaReimpianto()
  {
    return descAmmCompetenzaReimpianto;
  }

  public void setDescAmmCompetenzaReimpianto(String descAmmCompetenzaReimpianto)
  {
    this.descAmmCompetenzaReimpianto = descAmmCompetenzaReimpianto;
  }

  public String getVarieta()
  {
    return varieta;
  }

  public void setVarieta(String varieta)
  {
    this.varieta = varieta;
  }

  public String getVino()
  {
    return vino;
  }

  public void setVino(String vino)
  {
    this.vino = vino;
  }

  public DirittoVO getDiritto()
  {
    return diritto;
  }

  public void setDiritto(DirittoVO diritto)
  {
    this.diritto = diritto;
  }

  public boolean isScaduto()
  {
    return scaduto;
  }

  public void setScaduto(boolean scaduto)
  {
    this.scaduto = scaduto;
  }
}
