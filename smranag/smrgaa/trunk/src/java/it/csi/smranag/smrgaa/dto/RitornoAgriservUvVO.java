package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;
import java.util.Vector;


public class RitornoAgriservUvVO implements Serializable
{
  
  /** serialVersionUID */
  private static final long serialVersionUID = 2344254717234188880L;
  private String                  errori[];
  private Vector<Long> vPraticheIdParticella; //idParticella è presente se c'e' una pratica ad essa relativa

  public String[] getErrori()
  {
    return errori;
  }

  public void setErrori(String[] errori)
  {
    this.errori = errori;
  }

  public Vector<Long> getvPraticheIdParticella()
  {
    return vPraticheIdParticella;
  }

  public void setvPraticheIdParticella(Vector<Long> vPraticheIdParticella)
  {
    this.vPraticheIdParticella = vPraticheIdParticella;
  }

  
  
  

  

}
