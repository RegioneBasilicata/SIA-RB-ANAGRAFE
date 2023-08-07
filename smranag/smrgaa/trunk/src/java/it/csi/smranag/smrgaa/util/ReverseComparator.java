package it.csi.smranag.smrgaa.util;

import java.util.Comparator;

/**
 * Comparator che ordina in modo inverso all'ordine naturale.
 * E' obbligatorio che gli elementi da comparare implementino
 * l'intefaccia Comparable
 * Oct 17, 2008
 * @author TOBECONFIG
 *
 */
public class ReverseComparator implements Comparator<Object>
{
  public ReverseComparator()
  {
    super();
  }

  @SuppressWarnings("unchecked")
  public int compare(Object o1, Object o2)
  {
    Comparable<Object> c1=(Comparable)o1;
    Comparable<Object> c2=(Comparable)o2;
    if (c1==null && c2==null)
    {
      return 0;
    }
    if (c1==null)
    {
      return -1; // 1 * -1
    }
    if (c2==null)
    {
      return 1; // -1 * -1
    }
    return -1 * c1.compareTo(c2);
  }

}
