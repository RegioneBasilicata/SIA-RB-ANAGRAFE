package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;

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
public class EsitoControlloAllevamento implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 1020957808814706307L;
  
  
  
  
  private long    idEsitoControlloAllevamento;  // DB_ESITO_CONTROLLO_ALLEVAMENTO.ID_ESITO_CONTROLLO_ALLEVAMENTO
  private long    idAllevamento;                // DB_ESITO_CONTROLLO_ALLEVAMENTO.ID_ALLEVAMENTO
  private long    idControllo;                  // DB_ESITO_CONTROLLO_ALLEVAMENTO.ID_CONTROLLO
  private String  bloccante;                    // DB_ESITO_CONTROLLO_ALLEVAMENTO.BLOCCANTE
  private String  descrizione;                  // DB_ESITO_CONTROLLO_ALLEVAMENTO.DESCRIZIONE
  private String  descControllo;
  
  
  
  
  
  public long getIdEsitoControlloAllevamento()
  {
    return idEsitoControlloAllevamento;
  }
  public void setIdEsitoControlloAllevamento(long idEsitoControlloAllevamento)
  {
    this.idEsitoControlloAllevamento = idEsitoControlloAllevamento;
  }
  public long getIdAllevamento()
  {
    return idAllevamento;
  }
  public void setIdAllevamento(long idAllevamento)
  {
    this.idAllevamento = idAllevamento;
  }
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
  public String getDescControllo()
  {
    return descControllo;
  }
  public void setDescControllo(String descControllo)
  {
    this.descControllo = descControllo;
  }
  
  
  
}
