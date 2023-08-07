package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;

public class RitornoAgriservVO implements Serializable
{
  /** serialVersionUID */
  private static final long       serialVersionUID = -6538739026792736272L;
  private String                  errori[];
  private RigaPraticaParticellaVO righe[];

  public String[] getErrori()
  {
    return errori;
  }

  public void setErrori(String[] errori)
  {
    this.errori = errori;
  }

  public RigaPraticaParticellaVO[] getRighe()
  {
    return righe;
  }

  public void setRighe(RigaPraticaParticellaVO[] righe)
  {
    this.righe = righe;
  }

}
