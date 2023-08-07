package it.csi.smranag.smrgaa.util;

import java.util.HashMap;
import java.util.TreeMap;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;

public class CodeDescriptionUtils
{
  public static final HashMap<Object,BaseCodeDescription> convertIntoHashMap(BaseCodeDescription array[],
      boolean idIsKey)
  {
    HashMap<Object,BaseCodeDescription> map = new HashMap<Object,BaseCodeDescription>();
    int len = array == null ? 0 : array.length;
    for (int i = 0; i < len; ++i)
    {
      BaseCodeDescription bcd = array[i];
      if (idIsKey)
      {
        Long code = new Long(bcd.getCode());
        map.put(code, bcd);
      }
      else
      {
        map.put(bcd.getDescription(), bcd);
      }
    }
    return map;
  }

  public static final TreeMap<Object,BaseCodeDescription> convertIntoTreeMap(BaseCodeDescription array[],
      boolean idIsKey)
  {
    TreeMap<Object,BaseCodeDescription> map = new TreeMap<Object,BaseCodeDescription>();
    int len = array == null ? 0 : array.length;
    for (int i = 0; i < len; ++i)
    {
      BaseCodeDescription bcd = array[i];
      if (idIsKey)
      {
        Long code = new Long(bcd.getCode());
        map.put(code, bcd);
      }
      else
      {
        map.put(bcd.getDescription(), bcd);
      }
    }
    return map;
  }
}
