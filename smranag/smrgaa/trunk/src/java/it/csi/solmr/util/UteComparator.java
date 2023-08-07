package it.csi.solmr.util;

import java.util.Comparator;
import it.csi.solmr.dto.anag.*;

public class UteComparator implements Comparator<Object>
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
    UteVO t1=(UteVO) o1;
    UteVO t2=(UteVO) o2;

    int conf=compareString(t1.getComune(),t2.getComune());
    if (conf==0)
    {
      conf=compareString(t1.getIndirizzo(),t2.getIndirizzo());
    }
    return conf;
  }

  public boolean equals(Object obj)
  {
    return obj instanceof UteComparator;
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
