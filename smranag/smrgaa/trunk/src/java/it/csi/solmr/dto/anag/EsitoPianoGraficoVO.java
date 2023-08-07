package it.csi.solmr.dto.anag;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

public class EsitoPianoGraficoVO implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 1575161830681334896L;
  
  
  
  private String messaggioErrore;
  private String messaggio;
  private String link;
  private String etichettaPulsante;
  private Long idEsitoGrafico;
  private String flagEseguiControlli;
  
  
  public String getMessaggioErrore()
  {
    return messaggioErrore;
  }
  public void setMessaggioErrore(String messaggioErrore)
  {
    this.messaggioErrore = messaggioErrore;
  }
  public String getMessaggio()
  {
    return messaggio;
  }
  public void setMessaggio(String messaggio)
  {
    this.messaggio = messaggio;
  }
  public String getLink()
  {
    return link;
  }
  public void setLink(String link)
  {
    this.link = link;
  }
  public String getEtichettaPulsante()
  {
    return etichettaPulsante;
  }
  public void setEtichettaPulsante(String etichettaPulsante)
  {
    this.etichettaPulsante = etichettaPulsante;
  }
  public Long getIdEsitoGrafico()
  {
    return idEsitoGrafico;
  }
  public void setIdEsitoGrafico(Long idEsitoGrafico)
  {
    this.idEsitoGrafico = idEsitoGrafico;
  }
  public String getFlagEseguiControlli()
  {
    return flagEseguiControlli;
  }
  public void setFlagEseguiControlli(String flagEseguiControlli)
  {
    this.flagEseguiControlli = flagEseguiControlli;
  }
  
  
  
}