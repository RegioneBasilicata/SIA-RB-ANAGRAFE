package it.csi.smranag.smrgaa.dto;

import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoCCVO;

import java.io.Serializable;
import java.util.HashMap;

public class RitornoPraticheCCAgriservVO implements Serializable
{
   
  
  /**
   * 
   */
  private static final long serialVersionUID = -4287476441564647020L;
  
  
  
  private String                  errori[];
  private HashMap<Long, PraticaProcedimentoCCVO[]> hPraticheCC;

  public String[] getErrori()
  {
    return errori;
  }

  public void setErrori(String[] errori)
  {
    this.errori = errori;
  }

  public HashMap<Long, PraticaProcedimentoCCVO[]> getHPraticheCC()
  {
    return hPraticheCC;
  }

  public void setHPraticheCC(HashMap<Long, PraticaProcedimentoCCVO[]> praticheCC)
  {
    hPraticheCC = praticheCC;
  }

  

  

  

}
