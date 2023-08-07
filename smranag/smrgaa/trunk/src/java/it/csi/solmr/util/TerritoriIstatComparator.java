package it.csi.solmr.util;

import java.util.Comparator;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;

public class TerritoriIstatComparator implements Comparator<Object>
{
  public int compare(Object o1, Object o2)
  {
    if (o1==null && o2==null)
    {
      return 0;
    }
    else
    {
      if (o1==null)
      {
        return -1;
      }
      else
      {
        if (o2==null)
        {
          return 1;
        }
      }
    }
    SianTerritorioVO t1=(SianTerritorioVO) o1;
    SianTerritorioVO t2=(SianTerritorioVO) o2;

    return compareString(t1.getProvincia()+t1.getComune(),t2.getProvincia()+t2.getComune());

  }

  public boolean equals(Object obj)
  {
    return obj instanceof TerritoriIstatComparator;
  }




  private int compareString(String str1,String str2)
  {
    if (str1==null && str2==null)
    {
      return 0;
    }
    else
    {
      if (str1==null)
      {
        return -1;
      }
      else
      {
        if (str2==null)
        {
          return 1;
        }
      }
    }
    return str1.compareTo(str2);
  }


}
