package it.csi.smranag.smrgaa.dto.agriserv.domande;

import java.io.*;

/**
 * Value object utilizzato da agriservSearchDomande
 *
 * @author TOBECONFIG
 * @since 2.5.0
 */
public class DecodificaVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 5728778209132519846L;

  private String id; //Identificativo dell’entità rappresentata
  private String descrizione; //Descrizione dell’entità rappresentata

  public DecodificaVO()
  {
  }


  public String getId()
  {
    return id;
  }
  public void setId(String id)
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
}
