package it.csi.smranag.smrgaa.util;

import java.util.Vector;

import it.csi.solmr.util.Validator;

public class AnagUtils
{
  public static final void addToVector(Vector<Object> vect, Object objs[], boolean skipNulls)
  {
    if (vect == null)
    {
      return;
    }
    int len = objs == null ? 0 : objs.length;
    for (int i = 0; i < len; ++i)
    {
      Object obj=objs[i];
      if (!skipNulls || obj!=null)
      {
        vect.add(obj);
      }
    }
  }

  public static final long[] merge(long arr1[], long arr2[])
  {
    int len1 = arr1 == null ? 0 : arr1.length;
    int len2 = arr2 == null ? 0 : arr2.length;
    if (len1 + len2 == 0)
    {
      return new long[0];
    }
    int len = len1 + len2;
    if (len == len1)
    {
      return arr1;
    }
    if (len == len2)
    {
      return arr2;
    }
    long newArr[] = new long[len];
    System.arraycopy(arr1, 0, newArr, 0, len1);
    System.arraycopy(arr2, 0, newArr, len1, len2);
    return newArr;
  }

  /**
   * Concatena 2 blocchi mettendo il "." in mezzo s e necessario
   * 
   * @param blk1
   *          Nome del primo blocco (facoltativo)
   * @param blk2
   *          Nome del secondo blocco (obbligatorio)
   * @return
   */
  public static String concatBlk(String blk1, String blk2)
  {
    if (Validator.isNotEmpty(blk1))
    {
      if (!Validator.isNotEmpty(blk2))
      {
        return blk1;
      }
      if (blk1.endsWith("."))
      {
        return blk1 + blk2;
      }
      return blk1 + "." + blk2;
    }
    else
    {
      return blk2;
    }
  }
  
  /**
   * Questo metodo inserisce a sinistra della stringa input un numero di caratteri valorizzati a filler
   * tali da fare in modo che la stringa così composta abbia lunghezza length. Se la stringa input 
   * ha una lunghezza maggiore o uguale a length oppure è null questo metodo non fa nulla.
   * @param input: stringa da fillare
   * @param filler: carattere riempitivo
   * @param length: lunghezza finale della stringa
   * @return stringa fillata
   */
  public static String leftFill(String input,char filler,int length)
  {
	StringBuffer output=new StringBuffer("");
	if (input!=null)
	{
		if (input.length()<length)
		{
			for (int i=input.length();i<length;i++)
				output.append(filler);
		}
		output.append(input);
	}
	
	if (output!=null)
		return output.toString();
	else return null;
  }
  
  
  
  public static String valSupCatGraf(String superficieCatastale, String superficieGrafica)
  {
    double supCatDb = 0.0;
    double supGrafDb = 0.0;
    
    if(Validator.isNotEmpty(superficieCatastale))
    {
      supCatDb = Double.parseDouble(superficieCatastale.replace(',', '.'));
    }
    
    if(Validator.isNotEmpty(superficieGrafica))
    {
      supGrafDb = Double.parseDouble(superficieGrafica.replace(',', '.'));
    }
    
    if(supGrafDb > supCatDb)
    {
      return superficieGrafica;
    }
    
    return superficieCatastale;   
    
  }
  
  
  
}
