package it.csi.smranag.smrgaa.util.comparator;

import it.csi.smranag.smrgaa.dto.ws.cciaa.*;

public class DatiUvCCIAAComparator extends SmrgaaComparator
{
  //Tipi di ordinamento possibili
  public static final int ORD_COM_SEZ_FOGLIO_PART_ASC=1;
  public static final int ORD_COM_ASC=2;
  public static final int ORD_COM_DESC=3;
  public static final int ORD_ALBO_ASC=4;
  public static final int ORD_ALBO_DESC=5;
  public static final int ORD_ALBO_CUAA_ASC=6;

  private int ordinamento=1;

  public DatiUvCCIAAComparator(int ordinamento)
  {
    this.ordinamento=ordinamento;
  }

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
    int conf=0;
    DatiUvCCIAA d1=(DatiUvCCIAA) o1;
    DatiUvCCIAA d2=(DatiUvCCIAA) o2;

    if (ordinamento==ORD_COM_SEZ_FOGLIO_PART_ASC)
    {
      //Ordimanento per chiave catastale (comune + sezione + foglio + particella) in ordine crescente.
      conf=compareString(d1.getDescComune(),d2.getDescComune());
      if (conf==0)
      {
        conf=compareString(d1.getSezione(),d2.getSezione());
        if (conf==0)
        {
          conf=compareNum(d1.getFoglio(),d2.getFoglio());
          if (conf==0)
          {
            conf=compareNum(d1.getParticellaSearch(),d2.getParticellaSearch());
          }
        }
      }
    }

    if (ordinamento==ORD_COM_ASC)
    {
      //Ordimanento per comune in ordine crescente.
      conf=compareString(d1.getDescComune(),d2.getDescComune());
    }
    if (ordinamento==ORD_COM_DESC)
    {
      //Ordimanento per comune in ordine decrescente.
      conf=compareString(d2.getDescComune(),d1.getDescComune());
    }

    if (ordinamento==ORD_ALBO_ASC)
    {
      //Ordimanento per albo in ordine crescente.
      conf=compareString(d1.getDescAlbo(),d2.getDescAlbo());
    }
    if (ordinamento==ORD_ALBO_DESC)
    {
      //Ordimanento per albo in ordine decrescente.
      conf=compareString(d2.getDescAlbo(),d1.getDescAlbo());
    }
    if (ordinamento==ORD_ALBO_CUAA_ASC)
    {
      //Ordimanento per Albo e CUAA in ordine crescente
      conf=compareString(d1.getDescAlbo(),d2.getDescAlbo());
      if (conf==0) conf=compareString(d1.getCuaa(),d2.getCuaa());
    }

    return conf;
  }

  public boolean equals(Object obj)
  {
    return obj instanceof DatiUvCCIAAComparator;
  }
}
