package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * <br>
 *
 * Value Object per il trasporto di dati pertinenti le superfici
 *
 * @author Luca Romanello
 * @version 1.0
 */

import java.io.Serializable;
import java.util.Vector;

public class ElencoNotificheVO implements Serializable
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 3249980422302384171L;
  
  
  private String messaggioErrore;
  private Vector<NotificaVO> vElencoNotifiche;
  
  
  public String getMessaggioErrore()
  {
    return messaggioErrore;
  }
  public void setMessaggioErrore(String messaggioErrore)
  {
    this.messaggioErrore = messaggioErrore;
  }
  public Vector<NotificaVO> getvElencoNotifiche()
  {
    return vElencoNotifiche;
  }
  public void setvElencoNotifiche(Vector<NotificaVO> vElencoNotifiche)
  {
    this.vElencoNotifiche = vElencoNotifiche;
  }
  
  
  
}