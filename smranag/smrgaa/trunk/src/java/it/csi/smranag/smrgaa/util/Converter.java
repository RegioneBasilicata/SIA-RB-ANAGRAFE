package it.csi.smranag.smrgaa.util;

import java.util.Vector;

public class Converter
{
  /**
   * Converte un array di stringhe in un array di long.
   * @param array
   * @return
   */
  public static long[] stringArrayToLongBaseArray(String array[])
  {
    if (array == null)
    {
      return null;
    }
    int len = array.length;
    long results[] = new long[len];
    for (int i = 0; i < len; ++i)
    {
      results[i] = Long.decode(array[i]).longValue();
    }
    return results;
  }
  
  /**
   * Converte un array di long in un Vector.
   * @param array
   * @return
   */
  public static Vector<Long> longArrayToVector(long[] array)
  {
    if (array == null)
    {
      return null;
    }
    int len = array.length;
    Vector<Long> vett = new Vector<Long>();
    for (int i = 0; i < len; ++i)
    {
      vett.add(new Long(array[i]));
    }
    return vett;
  }
  
  /**
   * Converte un Vector in un array di long.
   * @param array
   * @return
   */
  public static long[] vectorToLongBaseArray(Vector<Long> array)
  {
    if (array == null)
    {
      return null;
    }
    long results[] = new long[array.size()];
    for (int i = 0; i < array.size(); ++i)
    {
      results[i] = ((Long)array.get(i)).longValue();
    }
    return results;
  }
}
