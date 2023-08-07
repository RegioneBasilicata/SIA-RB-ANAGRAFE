package it.csi.smranag.smrgaa.util.comparator;

import java.util.Comparator;

public abstract class SmrgaaComparator implements Comparator<Object>
{
  protected int compareNum(String str1,String str2)
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


  protected int compareString(String str1,String str2)
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
