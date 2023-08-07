package it.csi.smranag.smrgaa.dto.allevamenti;

import it.csi.solmr.dto.anag.AllevamentoAnagVO;

import java.io.Serializable;
import java.util.Vector;

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
public class ControlloAllevamenti implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 1020957808814706307L;
  
  
  
  
  private long    idControllo;                  // DB_ESITO_CONTROLLO_ALLEVAMENTO.ID_CONTROLLO
  private String  bloccante;                    // DB_ESITO_CONTROLLO_ALLEVAMENTO.BLOCCANTE
  private String  descrizione;                  // DB_ESITO_CONTROLLO_ALLEVAMENTO.DESCRIZIONE
  private Vector<AllevamentoAnagVO> vAllevamenti;
  
  
  
  
  public long getIdControllo()
  {
    return idControllo;
  }
  public void setIdControllo(long idControllo)
  {
    this.idControllo = idControllo;
  }
  public String getBloccante()
  {
    return bloccante;
  }
  public void setBloccante(String bloccante)
  {
    this.bloccante = bloccante;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Vector<AllevamentoAnagVO> getvAllevamenti()
  {
    return vAllevamenti;
  }
  public void setvAllevamenti(Vector<AllevamentoAnagVO> vAllevamenti)
  {
    this.vAllevamenti = vAllevamenti;
  }
  
  
  
}
