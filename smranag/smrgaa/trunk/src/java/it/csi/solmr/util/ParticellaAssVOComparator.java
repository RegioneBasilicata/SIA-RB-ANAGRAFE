package it.csi.solmr.util;

import java.util.Comparator;
import it.csi.solmr.dto.anag.ParticellaAssVO;

public class ParticellaAssVOComparator implements Comparator<Object>
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
    ParticellaAssVO t1=(ParticellaAssVO) o1;
    ParticellaAssVO t2=(ParticellaAssVO) o2;

    int conf=compareString(t1.getDescComuneParticella(),t2.getDescComuneParticella());
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
          {
            conf=compareString(t1.getSubalterno(),t2.getSubalterno());
            if (conf==0)
              conf=compareNum(t1.getIdConduzione(),t2.getIdConduzione());
          }
        }
      }
    }
    return conf;
  }

  public boolean equals(Object obj)
  {
    return obj instanceof ParticellaAssVOComparator;
  }

  private int compareNum(Long str1,Long str2)
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
    if (str1.longValue()>str2.longValue()) return 1;
    else
      if (str2.longValue()>str1.longValue()) return -1;
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
  
  
  /**
   * Metodo privato per effettuare i confronti di campi da considerare
   * numerici
   * 
   * @param str1
   * @param str2
   * @return int
   */
  private int compareNum(String str1,String str2) {
    if(str1 == null && str2 == null) {
      return 0;
    }
    else {
      if(str1 == null) {
        return -1;
      }
      else {
        if(str2 == null) {
          return 1;
        }
      }
    }
    long num1 = 0;
    long num2 = 0;
    try {
      num1 = Long.parseLong(str1);
      num2 = Long.parseLong(str2);
    }
    catch(Exception e){}

    if(num1 > num2) {
      return 1;
    }
    else {
      if(num2 > num1) {
        return -1;
      }
    }
    return 0;
  }



}
