package it.csi.smranag.smrgaa.util;

import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.util.Validator;

import java.util.Comparator;

public class StoricoParticellaUVComparator implements Comparator<Object>
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
    StoricoParticellaVO t1=(StoricoParticellaVO) o1;
    StoricoParticellaVO t2=(StoricoParticellaVO) o2;

    int conf=compareString(t1.getComuneParticellaVO().getDescom(),t2.getComuneParticellaVO().getDescom());
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
            //entro qui solo se trattasi di uv
            if((Validator.isNotEmpty(t1.getStoricoUnitaArboreaVO()) && Validator.isNotEmpty(t1.getStoricoUnitaArboreaVO().getProgrUnar()))
              || (Validator.isNotEmpty(t2.getStoricoUnitaArboreaVO()) && Validator.isNotEmpty(t2.getStoricoUnitaArboreaVO().getProgrUnar())))
            {
              if (conf==0)
              {
                Long progUnar1 = null;
                Long progUnar2 = null;
                if(Validator.isNotEmpty(t1.getStoricoUnitaArboreaVO())
                  && Validator.isNotEmpty(t1.getStoricoUnitaArboreaVO().getProgrUnar()))
                {                  
                  progUnar1 = new Long(t1.getStoricoUnitaArboreaVO().getProgrUnar());
                }
                if(Validator.isNotEmpty(t2.getStoricoUnitaArboreaVO())
                  && Validator.isNotEmpty(t2.getStoricoUnitaArboreaVO().getProgrUnar()))
                {                  
                  progUnar2 = new Long(t2.getStoricoUnitaArboreaVO().getProgrUnar());
                }
                
                conf=compareNum(progUnar1, progUnar2);
              }
            }
            
            if((Validator.isNotEmpty(t1.getUnitaArboreaDichiarataVO()) && Validator.isNotEmpty(t1.getUnitaArboreaDichiarataVO().getProgrUnar()))
              || (Validator.isNotEmpty(t2.getUnitaArboreaDichiarataVO()) && Validator.isNotEmpty(t2.getUnitaArboreaDichiarataVO().getProgrUnar())) )
            {
              if (conf==0)
              {
                Long progUnar1 = null;
                Long progUnar2 = null;
                if(Validator.isNotEmpty(t1.getUnitaArboreaDichiarataVO())
                  && Validator.isNotEmpty(t1.getUnitaArboreaDichiarataVO().getProgrUnar()))
                {                  
                  progUnar1 = new Long(t1.getUnitaArboreaDichiarataVO().getProgrUnar());
                }
                if(Validator.isNotEmpty(t2.getUnitaArboreaDichiarataVO())
                  && Validator.isNotEmpty(t2.getUnitaArboreaDichiarataVO().getProgrUnar()))
                {                  
                  progUnar2 = new Long(t2.getUnitaArboreaDichiarataVO().getProgrUnar());
                }
                
                conf=compareNum(progUnar1, progUnar2);
              }
            }
          }
        }
      }
    }
    return conf;
  }

  public boolean equals(Object obj)
  {
    return obj instanceof StoricoParticellaUVComparator;
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
