package it.csi.solmr.util;

import java.util.Comparator;
import it.csi.solmr.dto.CodeDescription;


public class CodeDescriptionComparator implements Comparator<Object>
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
    CodeDescription c1=(CodeDescription) o1;
    CodeDescription c2=(CodeDescription) o2;
    String str1=c1.getCode().toString();
    String str2=c2.getCode().toString();
    return str1.compareTo(str2);
  }

  public boolean equals(Object obj)
  {
    return obj instanceof CodeDescription;
  }
}
