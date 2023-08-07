package it.csi.solmr.util;

import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO;

import java.util.Comparator;

public class TipoEffluentiComparator implements Comparator<Object>
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
    TipoEffluenteVO t1=(TipoEffluenteVO) o1;
    TipoEffluenteVO t2=(TipoEffluenteVO) o2;

    int conf=compareString(t1.getDescrizione(),t2.getDescrizione());
    
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
