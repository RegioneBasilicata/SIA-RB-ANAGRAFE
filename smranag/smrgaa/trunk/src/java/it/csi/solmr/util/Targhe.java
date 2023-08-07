package it.csi.solmr.util;

import it.csi.solmr.exception.WrongFormatException;

/**
 * Title:        Utilità di gestione delle targhe
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      CSI
 * @author TOBECONFIG
 * @version 1.0
 */

public class Targhe {
  public static final String VALIDCHARS="ABCDEFGHJKLMNPRSTVWXYZ";
  public static final String DIGITS="0123456789";
  public static final String CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String MSG_FORMATO_NON_VALIDO =
                                              "Formato della targa non valida!";

  public Targhe() {
  }

  /**
   * Restituisce la targa UMA successiva a quella passata come parametro.
   * @param current Targa di cui calcolare la successiva.
   * @return Targa successiva o null nel caso sia stata passata una targa
   * invalida
   */
  public String getNextUMA(String current) throws WrongFormatException
  {
    if (!isValidUMA(current))
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }
    current=current.toUpperCase();
    String prov=current.substring(0,2);
    String numstr=current.substring(2);
    int num=new Integer(numstr).intValue();
    num++;
    if (num>999999)
    {
      throw new WrongFormatException("Impossibile trovare una targa successiva!");
    }
    return prov+format(num,6);
  }

  /**
   * Restituisce la targa UMA successiva a quella passata come parametro.
   * @param current Targa di cui calcolare la successiva.
   * @return Targa successiva o null nel caso sia stata passata una targa
   * invalida
   */
   public String getPrevUMA(String current) throws WrongFormatException
  {
    if (!isValidUMA(current))
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }
    current=current.toUpperCase();
    String prov=current.substring(0,2);
    String numstr=current.substring(2);
    int num=new Integer(numstr).intValue();
    num--;
    if (num<0)
    {
      throw new WrongFormatException("Impossibile trovare una targa precedente!");
    }
    return prov+format(num,6);
  }

  /**
   * Restituisce la differenza di due targhe UMA
   * @param first prima targa (dovrebbe essere maggiore di second)
   * @param second seconda targa (dovrebbe essere minore di first)
   * @return differenza tra le targhe. Se second è maggiore di first il
   * risultato è negativo.
   * @throws WrongFormatException Formato delle targhe non valido
   */
  public int differenceUMA(String first, String second) throws WrongFormatException
  {
    if (!isValidUMA(first) || !isValidUMA(second))
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }
    first=first.toUpperCase();
    second=second.toUpperCase();
    int val1;
    int val2;
    String prov1=first.substring(0,2);
    String prov2=second.substring(0,2);
    if (!prov1.equals(prov2))
    {
      throw new WrongFormatException("Impossibile calcolare la differenza tra "+
                                     "targhe con provincia differente");
    }
    try
    {
      val1=new Integer(first.substring(2)).intValue();
      val2=new Integer(second.substring(2)).intValue();
    }
    catch (Exception e)
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }

    return val1-val2;
  }

  /**
   * Indica se la targa passata è corretta.
   * @param targa targa da controllare
   * @return true se è valida, false altrimenti (non valida o null)
   */
  public boolean isValidUMA(String targa)
  {
    if (targa==null || targa.length()!=8)
    {
      return false;
    }
    return CHARS.indexOf(targa.charAt(0))>=0 &&
           CHARS.indexOf(targa.charAt(1))>=0 &&
           DIGITS.indexOf(targa.charAt(2))>=0 &&
           DIGITS.indexOf(targa.charAt(3))>=0 &&
           DIGITS.indexOf(targa.charAt(4))>=0 &&
           DIGITS.indexOf(targa.charAt(5))>=0 &&
           DIGITS.indexOf(targa.charAt(6))>=0 &&
           DIGITS.indexOf(targa.charAt(7))>=0;
  }

  /**
   * Restituisce la targa successiva a quella passata.
   * @param current targa di cui si desidera il successivo
   * @return targa successiva
   */
    public String getNext(String current) throws WrongFormatException
  {
    if (!isValid(current))
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }
    current=current.toUpperCase();
    char xx[];
    int nnn;
    char y;
    xx=current.substring(0,2).toUpperCase().toCharArray();
    nnn=new Integer(current.substring(2,5)).intValue();
    y=current.substring(5).toUpperCase().charAt(0);
    if (nnn<999)
    {
      nnn++;
    }
    else
    {
      nnn=0;
      y=nextChar(y);
      if (y=='A') // Overflow: devo aggiustare  xx;
      {
        setNextCharBlock(xx);
        if (xx[0]=='A' && xx[1]=='A')
        {
          throw new WrongFormatException("Impossibile calcolare la targa successiva: valore"+
                              " massimo targa raggiunto");
        }
      }
    }
    return ""+xx[0]+xx[1]+format(nnn,3)+y;
  }

  /**
   * Restituisce la targa precedente a quella passata.
   * @param current targa di cui si desidera il successivo
   * @return targa precedente
   */
   public String getPrev(String current) throws WrongFormatException
  {
    if (!isValid(current))
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }
    current=current.toUpperCase();
    char xx[];
    int nnn;
    char y;

    xx=current.substring(0,2).toUpperCase().toCharArray();
    nnn=new Integer(current.substring(2,5)).intValue();
    y=current.substring(5).toUpperCase().charAt(0);
    if (nnn>0)
    {
      nnn--;
    }
    else
    {
      nnn=999;
      y=prevChar(y);
      if (y=='Z') // Underflow: devo aggiustare  xx;
      {
        setPrevCharBlock(xx);
        if (xx[0]=='Z' && xx[1]=='Z') // Underflow: devo aggiustare  xx;
        {
          throw new WrongFormatException("Impossibile calcolare la targa precedente: valore"+
                              " minimo raggiunto");
        }
      }
    }
    return ""+xx[0]+xx[1]+format(nnn,3)+y;
  }

  /**
   * Restituisce la differenza di due targhe
   * @param first prima targa (dovrebbe essere maggiore di second)
   * @param second seconda targa (dovrebbe essere minore di first)
   * @return differenza tra le targhe. Se second è maggiore di first il
   * risultato è negativo.
   * @throws WrongFormatException Formato delle targhe non valido
   */
  public int difference(String first, String second) throws WrongFormatException
  {
    if (!isValid(first) || !isValid(second))
    {
      throw new WrongFormatException(MSG_FORMATO_NON_VALIDO);
    }
    first=first.toUpperCase();
    second=second.toUpperCase();
    char xx[]=first.substring(0,2).toUpperCase().toCharArray();
    int nnn=new Integer(first.substring(2,5)).intValue();
    char y=first.substring(5).toUpperCase().charAt(0);

    char xx1[]=second.substring(0,2).toUpperCase().toCharArray();
    int nnn1=new Integer(second.substring(2,5)).intValue();
    char y1=second.substring(5).toUpperCase().charAt(0);

    return targa2Number(xx,nnn,y)-targa2Number(xx1,nnn1,y1);
  }

  /**
   * Indica se la targa passata è corretta.
   * @param targa targa da controllare
   * @return true se è valida, false altrimenti (non valida o null)
   */
  public boolean isValid(String targa)
  {
    if (targa==null || targa.length()!=6)
    {
      return false;
    }
    targa=targa.toUpperCase();
    return VALIDCHARS.indexOf(targa.charAt(0))>=0 &&
           VALIDCHARS.indexOf(targa.charAt(1))>=0 &&
           DIGITS.indexOf(targa.charAt(2))>=0 &&
           DIGITS.indexOf(targa.charAt(3))>=0 &&
           DIGITS.indexOf(targa.charAt(4))>=0 &&
           VALIDCHARS.indexOf(targa.charAt(5))>=0;
  }

  /**
   * Restituisce il successivo carattere valido per una targa.
   * @param carattere di cui si vuole il successivo
   * @return carattere successivo
   */
  private char nextChar(char chr)
  {
    int index=VALIDCHARS.indexOf(chr);
    if (index==VALIDCHARS.length()-1)
    {
      index=0;
    }
    else
    {
      index++;
    }
    return VALIDCHARS.charAt(index);
  }

  /**
   * Restituisce il precedente carattere valido per una targa.
   * @param carattere di cui si vuole il precedente
   * @return carattere precedente
   */
  private char prevChar(char chr)
  {
    int index=VALIDCHARS.indexOf(chr);
    if (index==0)
    {
      index=VALIDCHARS.length()-1;
    }
    else
    {
      index--;
    }
    return VALIDCHARS.charAt(index);
  }

  /**
   * Formatta un numero aggiungendo zeri all'inizio per estenderlo su n cifre.
   * @param nnn numero da formattare
   * @param length numero di caratteri su cui formattare il numero
   */
  private String format(int nnn,int length) throws WrongFormatException
  {
    if (length<3 || length>6)
    {
      throw new WrongFormatException("Lunghezza numero targa non corretta nella formattazione!");
    }
    String tmp=""+nnn;

    return "000000".substring(0,length-tmp.length())+tmp;
  }

  /**
   * Aumenta di una unità il valore dei caratteri del vettore. Utilizzato per
   * incrementare xx e yy nelle targhe in formato XXNNNYY. L'incremento del
   * valore 'ZZ' ha come risultato il valore 'AA'.
   * @param blk valore da incrementare. Deve essere un array di due elementi.
   */
  private void setNextCharBlock(char blk[])
  {
    blk[1]=nextChar(blk[1]);
    if (blk[1]=='A') // OVERFLOW DEVO CORREGGERE blk[0]
    {
      blk[0]=nextChar(blk[0]);
    }
  }

  /**
   * Diminuisce di una unità il valore dei caratteri del vettore. Utilizzato per
   * decrementare xx e yy nelle targhe in formato XXNNNYY. Il decremento del
   * valore 'AA' ha come risultato il valore 'ZZ'.
   * @param blk valore da decrementare. Deve essere un array di due elementi.
   */
  private void setPrevCharBlock(char blk[])
  {
    blk[1]=prevChar(blk[1]);
    if (blk[1]=='Z') // OVERFLOW DEVO CORREGGERE blk[0]
    {
      blk[0]=prevChar(blk[0]);
    }
  }

  /**
   * Converte una targa in un numero equivalente.
   * @param xx Componente XX della targa in formato XXNNNYY
   * @param nnn Componente NNN della targa in formato XXNNNYY
   * @param yy Componente YY della targa in formato XXNNNYY
   * @return numero che equivale alla targa
   */
  private int targa2Number(char xx[],int nnn,char y)
  {
    int xx0=Targhe.VALIDCHARS.indexOf(xx[0]);
    int xx1=Targhe.VALIDCHARS.indexOf(xx[1]);
    int yy0=Targhe.VALIDCHARS.indexOf(y);
    int val=nnn+1000*(yy0+xx1*22+xx0*22*22);
    return val;
  }
}
