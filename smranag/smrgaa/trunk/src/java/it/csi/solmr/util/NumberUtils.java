package it.csi.solmr.util;

/**
 * <p>Title: Servizi Educativi</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: CSI Piemonte</p>
 * @author Ivan Morra
 * @version 1.0
 */

public abstract class NumberUtils
{
  public static double arrotonda(double numero, int precisione)
  {
    double result;

    result = numero*Math.pow(10, precisione);

    result = Math.round(result);

    result = result/Math.pow(10, precisione);

    return result;
  }

  public static boolean isPari(int numero) {
    boolean isPari = false;
    int resto = 0;
    if(numero != 0) {
      resto = numero % 2;
      if(resto == 0) {
        isPari = true;
      }
    }
    else {
      isPari = true;
    }
    return isPari;
  }
  
  
  public static Long getIdPositiveKeyTemporay()
  {
    double dRandom = Math.random()*1000000000;
    long lRandom = Math.round(dRandom);
    return new Long(lRandom);
  }
}
