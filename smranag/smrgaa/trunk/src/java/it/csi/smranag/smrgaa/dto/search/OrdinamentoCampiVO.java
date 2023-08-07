package it.csi.smranag.smrgaa.dto.search;

import it.csi.jsf.htmpl.Htmpl;

import java.io.Serializable;
import java.util.*;

/**
 * Restituisce la stringa necessaria a fare un ordinamento dinamico
 * 
 * @author TOBECONFIG
 * 
 */
public class OrdinamentoCampiVO implements Serializable
{
  
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -1471948687498421193L;
  
  private Vector<CampoVO>            campi;
  
  
  /**
   * Popola l'oggetto campi
   * @param dati
   */
  public OrdinamentoCampiVO(CampoVO dati[])
  {
    if (dati!=null)
    {
      campi=new Vector<CampoVO>();
      for (int i=0;i<dati.length;i++)
        campi.add(dati[i]);
    }
  }
  
  /**
   * restituisce la stringa che mi permette di fare OrderBy
   * @return
   */
  public String getOrderBy()
  {
    StringBuffer orderBy=new StringBuffer("");
    if (campi!=null) 
    {
      int size=campi.size();
      if (size>0)
      {
        orderBy.append("ORDER BY ");
        for (int i=0;i<size;i++)
        {
          CampoVO dato=(CampoVO)campi.get(i);
          orderBy.append(dato.getCampo());
          if (!dato.isOrdinamento()) orderBy.append(" ").append("DESC");
          if (i!=size-1) orderBy.append(",");
        }
      }
    }
    return orderBy.toString();
  }
  
  /**
   * Mi restituisce l'oggetto associato alla descrizione
   * @param desc
   * @return
   */
  public CampoVO getCampoVOByDesc(String desc)
  {
    if (campi!=null) 
    {
      int size=campi.size();
      for (int i=0;i<size;i++)
      {
        CampoVO dato=(CampoVO)campi.get(i);
        if (desc.equalsIgnoreCase(dato.getCampo()))
          return dato;
      }
    }
    return null;
  }
  
  /**
   * Inverte l'ordinamento dell'oggetto associato alla descrizione
   * Sposta l'oggetto associato alla descrizione in prima posizione
   * @param desc
   */
  public void setCampoVOByDesc(String desc)
  {
    if (campi!=null) 
    {
      int size=campi.size();
      for (int i=0;i<size;i++)
      {
        CampoVO dato=(CampoVO)campi.get(i);
        if (desc.equalsIgnoreCase(dato.getSegnaPostoHtmpl()))
        {
          //inverto l'ordinamento dell'oggetto
          dato.setOrdinamento(!dato.isOrdinamento());
          //sposto l'oggetto dalla posizione in cui si trova in testa
          campi.remove(i);
          campi.insertElementAt(dato, 0);
          return;
        }
      }
    }
  }

  public Vector<CampoVO> getCampi()
  {
    return campi;
  }
  
  /**
   * Metodo usato per scrivere sulla pagina html i dati relativi all'ordinamento
   * @param htmpl
   * @param blocco
   */
  public void writeOrdinamento(Htmpl htmpl,String blocco)
  {
    if (campi!=null)
    {
      int size=campi.size();
      for (int i=0;i<size;i++)
      {
        CampoVO dato=(CampoVO)campi.get(i);
        dato.writeOrdinamento(htmpl, blocco);
      }
    }
  }
}
