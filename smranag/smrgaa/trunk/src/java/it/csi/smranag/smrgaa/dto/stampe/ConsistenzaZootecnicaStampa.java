package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale Questo VO viene utilizzato
 * esclusivamente dalle stampe
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
public class ConsistenzaZootecnicaStampa implements Serializable
{
  /**
   * serial version UID
   */
  private static final long serialVersionUID = 5652428062791239018L;

  private String            comuneProvUTE;
  private String            codiceAziendaZootecnica;
  private String            descrizioneSpecie;
  private long              totaleCapi;

  public String getComuneProvUTE()
  {
    return comuneProvUTE;
  }

  public void setComuneProvUTE(String comuneProvUTE)
  {
    this.comuneProvUTE = comuneProvUTE;
  }

  public String getCodiceAziendaZootecnica()
  {
    return codiceAziendaZootecnica;
  }

  public void setCodiceAziendaZootecnica(String codiceAziendaZootecnica)
  {
    this.codiceAziendaZootecnica = codiceAziendaZootecnica;
  }

  public String getDescrizioneSpecie()
  {
    return descrizioneSpecie;
  }

  public void setDescrizioneSpecie(String descrizioneSpecie)
  {
    this.descrizioneSpecie = descrizioneSpecie;
  }

  public long getTotaleCapi()
  {
    return totaleCapi;
  }

  public void setTotaleCapi(long totaleCapi)
  {
    this.totaleCapi = totaleCapi;
  }

}
