package it.csi.solmr.dto.anag.services;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author: Mauro Vocale
 * @version 1.0
 */
import it.csi.solmr.dto.anag.sian.SianAnagTributariaVO;

import java.io.Serializable;
import java.util.Vector;

// Questo VO si occupa di codificare l'XML proveniente dalla servlet che richiama il servizio
// "anagrafica sintetica" del SIAN
public class SianAnagTributariaGaaVO extends SianAnagTributariaVO implements Serializable 
{

  /**
   * 
   */
  private static final long serialVersionUID = -1963852216665236452L;
  
  
  
  Vector<SianAnagTributariaAtecoSecGaaVO> vAtecoSecSian;

  public Vector<SianAnagTributariaAtecoSecGaaVO> getvAtecoSecSian()
  {
    return vAtecoSecSian;
  }

  public void setvAtecoSecSian(
      Vector<SianAnagTributariaAtecoSecGaaVO> vAtecoSecSian)
  {
    this.vAtecoSecSian = vAtecoSecSian;
  }
  
  

  
  
  
  
}
