package it.csi.smranag.smrgaa.dto.agriserv.domande;

import java.io.*;


/**
 * Value object utilizzato da agriservSearchDomande
 *
 * @author TOBECONFIG
 * @since 2.5.0
 */
public class StatoPraticaVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4658618675329840034L;
 
  private Long id;
  private String descrizione;
  private java.util.Date data;

  public StatoPraticaVO()
  {
  }

  public Long getId()
  {
    return id;
  }
  public void setId(Long id)
  {
    this.id = id;
  }

  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public java.util.Date getData()
  {
    return data;
  }
  public void setData(java.util.Date data)
  {
    this.data = data;
  }
}
