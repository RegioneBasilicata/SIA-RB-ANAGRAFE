package it.csi.solmr.util;

import java.util.Comparator;
import it.csi.solmr.dto.anag.sian.SianTerritorioVO;

public class TerritoriComparator implements Comparator<Object>
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

    int conf=compareString(t1.getDescComune(),t2.getDescComune());
    if (conf==0)
    {
      conf=compareString(t1.getSezione(),t2.getSezione());
      if (conf==0)
      {
        conf=compareNum(t1.getFoglio(),t2.getFoglio());
        if (conf==0)
        {
          conf=compareNum(t1.getParticella(),t2.getParticella());
          if (conf==0)
            conf=compareString(t1.getSubalterno(),t2.getSubalterno());
        }
      }
    }
    return conf;
  }

  public boolean equals(Object obj)
  {
    return obj instanceof TerritoriComparator;
  }

  private int compareNum(String str1,String str2)
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
    long num1=0,num2=0;
    try
    {
      num1=Long.parseLong(str1);
      num2=Long.parseLong(str2);
    }
    catch(Exception e){}

    if (num1>num2) return 1;
    else
      if (num2>num1) return -1;
    return 0;
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
