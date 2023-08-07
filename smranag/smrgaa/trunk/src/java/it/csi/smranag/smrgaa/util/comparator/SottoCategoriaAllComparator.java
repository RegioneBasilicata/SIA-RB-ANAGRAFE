package it.csi.smranag.smrgaa.util.comparator;

import it.csi.smranag.smrgaa.dto.allevamenti.SottoCategoriaAllevamento;

import java.util.Comparator;

public class SottoCategoriaAllComparator implements Comparator<Object>
{
  public int compare(Object o1, Object o2)
  {
    if (o1 == null && o2 == null)
    {
      return 0;
    }
    else
    {
      if (o1 == null)
      {
        return -1;
      }
      else
      {
        if (o2 == null)
        {
          return 1;
        }
      }
    }
    SottoCategoriaAllevamento d1 = (SottoCategoriaAllevamento) o1;
    SottoCategoriaAllevamento d2 = (SottoCategoriaAllevamento) o2;

    if (d1.getIdCategoriaAnimale() > d2.getIdCategoriaAnimale())
      return 1;
    else
      if (d1.getIdCategoriaAnimale() == d2.getIdCategoriaAnimale())
        return 0;
      else
        return -1;
  }
}
