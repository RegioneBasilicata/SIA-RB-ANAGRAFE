package it.csi.smranag.smrgaa.util;

import java.util.Comparator;

public abstract class BaseComparator implements Comparator<Object>
{
  /** Compara gli oggetti che implementano l'intefaccia Comparable. Es Long, String, Integer, Boolean etc
   * @param c1
   * @param c2
   * @return
   */
  @SuppressWarnings("unchecked")
  public static int compareComparableObjects(Comparable c1, Comparable c2)
  {
    return compareComparableObjects(c1, c2,true,true);
  }
  
  @SuppressWarnings("unchecked")
  public static int compareComparableObjects(Comparable c1, Comparable c2,boolean asc,boolean nullsFirst)
  {
    int order=asc?1:-1;
    if (c1==c2) // Null o stessa stringa
    {
      return 0;
    }
    if (c1==null)
    {
      return (nullsFirst?-1:1)*order;
    }
    if (c2==null)
    {
      return (nullsFirst?1:-1)*order;
    }
    // Sono entrambi diversi da null
    return c1.compareTo(c2);
  }
}
