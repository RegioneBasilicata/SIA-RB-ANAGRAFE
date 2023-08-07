package it.csi.smranag.smrgaa.dto.ws;

import it.csi.solmr.dto.CodeDescription;

import java.util.Vector;


// Questo VO si occupa di codificare l'XML proveniente dalla servlet che richiama il servizio
// "leggiAllevamenti" del SIAN
public class ResponseWsBridgeVO  implements java.io.Serializable
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -5267542138648306214L;
  
  
  private Vector<Object> vDati;
  private Vector<CodeDescription> vErrori;
  
  
  public Vector<Object> getvDati()
  {
    return vDati;
  }
  public void setvDati(Vector<Object> vDati)
  {
    this.vDati = vDati;
  }
  public Vector<CodeDescription> getvErrori()
  {
    return vErrori;
  }
  public void setvErrori(Vector<CodeDescription> vErrori)
  {
    this.vErrori = vErrori;
  }
  


    
}
