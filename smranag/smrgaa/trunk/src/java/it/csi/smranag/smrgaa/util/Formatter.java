package it.csi.smranag.smrgaa.util;

import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formatter
{
  private static final DecimalFormat DOUBLEFORMAT4            = new DecimalFormat(
                                                                  "0.0000");
  

  
  private static final DecimalFormat DOUBLEFORMAT2            = new DecimalFormat(
                                                                  "0.00");
  private static final DecimalFormat DOUBLEFORMAT1            = new DecimalFormat(
                                                                  "0.0");
  private static final DecimalFormat DOUBLEFORMAT0            = new DecimalFormat(
                                                                  "0");
  
  private static final DecimalFormat SEPARATOR_FORMAT         = new DecimalFormat(
                                                                  "###,###,###,###,###,###");
  private static final DecimalFormat DOUBLE_SEPARATOR_FORMAT2 = new DecimalFormat(
                                                                  "###,###,###,###,###,##0.00");
  
  private static final DecimalFormat DOUBLE_SEPARATOR_FORMAT4 = new DecimalFormat(
  "###,###,###,###,###,##0.0000");
  
  private static final DecimalFormat DOUBLE_SEPARATOR_FORMAT2_LIBERO = new DecimalFormat(
                                                                  "###,###,###,###,###,###.##");
  

  /**
   * Formatta un double con 4 decimali
   * 
   * @return
   */
  public static String formatDouble4(double d)
  {
    String val = DOUBLEFORMAT4.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  public static String formatDouble6(double d)
  {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALY);
    symbols.setDecimalSeparator('.');
    DecimalFormat DOUBLEFORMAT6  = new DecimalFormat("##.######",symbols);  
    String val = DOUBLEFORMAT6.format(d);
    
    return val;
  }

  public static String formatDouble4ZeroOnError(double d)
  {
    String value = formatDouble4(d);
    if (!Validator.isNotEmpty(value))
    {
      return formatDouble4(0);
    }
    return value;
  }

  public static String formatDouble4ZeroOnError(Object d)
  {
    String value = formatDouble4(d);
    if (!Validator.isNotEmpty(value))
    {
      return formatDouble4(0);
    }
    return value;
  }
  
  public static String formatDouble4ZeroOnErrorForBigDecimal(Object d)
  {
    String value = formatDouble4ForBigDecimal(d);
    if (!Validator.isNotEmpty(value))
    {
      return "0.0000";
    }
    return value;
  }

  public static String formatDouble2Separator(double d)
  {
    String val = DOUBLE_SEPARATOR_FORMAT2.format(d);
    return val;
  }
  
  public static String formatDouble2Separator(Object str)
  {
    try
    {
      String val = formatDouble2Separator(new Double(str.toString().replace(',', '.'))
          .doubleValue());
      return val;
    }
    catch (Exception e)
    {
      return "";
    }
  }

  public static String formatDouble2Separator(String str)
  {
    try
    {
      String val = formatDouble2Separator(new Double(str.replace(',', '.'))
          .doubleValue());
      return val;
    }
    catch (Exception e)
    {
      return "";
    }
  }
  
  public static String formatDouble2ForBigDecimal(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT2.format(d).toString().replace(',', '.');
    return val;
  }
  
  public static String parseDouble2SeparatorForBigDecimal(String str)
  {
    String val = "";
    try
    {
      val = DOUBLE_SEPARATOR_FORMAT2_LIBERO.parse(str).toString();
    }
    catch (Exception ex)
    {
      return "";
    }
    
    return val;
  }

  /**
   * Formatta un double con 4 decimali
   * 
   * @return
   */
  public static String formatDouble4(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT4.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  public static String formatDouble6(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALY);
    symbols.setDecimalSeparator('.');
    DecimalFormat DOUBLEFORMAT6  = new DecimalFormat("##.######",symbols);  
    String val = DOUBLEFORMAT6.format(d);
    
    return val;
  }
  
  public static String formatDouble4ForBigDecimal(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT4.format(d).toString().replace(',', '.');
    return val;
  }
  
  /**
   * Formatta una stringa con 4 decimali
   * 
   * @return
   */
  public static String formatDouble4(String str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT4.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  public static String formatDouble6(String str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALY);
    symbols.setDecimalSeparator('.');
    DecimalFormat DOUBLEFORMAT6  = new DecimalFormat("##.######",symbols);  
    String val = DOUBLEFORMAT6.format(d);
    
    return val;
  }

  /**
   * Formatta un double con 2 decimali
   * 
   * @return
   */
  public static String formatDouble2(double d)
  {
    String val = DOUBLEFORMAT2.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }

  /**
   * Formatta un double con 2 decimali
   * 
   * @return
   */
  public static String formatDouble2(String str)
  {
    double d = 0.0;
    try
    {
      str = str.replace(',', '.');
      d = Double.parseDouble(str);
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT2.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  /**
   * Formatta un object con 2 decimali
   * 
   * @return
   */
  public static String formatDouble2(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT2.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  
  /**
   * Formatta un double con 1 decimali
   * 
   * @return
   */
  public static String formatDouble1(double d)
  {
    String val = DOUBLEFORMAT1.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }

  /**
   * Formatta un double con 1 decimali
   * 
   * @return
   */
  public static String formatDouble1(String str)
  {
    double d = 0.0;
    try
    {
      str = str.replace(',', '.');
      d = Double.parseDouble(str);
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT1.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  /**
   * Formatta un object con 1 decimali
   * 
   * @return
   */
  public static String formatDouble1(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT1.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  /**
   * Formatta un object con 0 decimali
   * 
   * @return
   */
  public static String formatDouble(Object str)
  {
    double d = 0.0;
    try
    {
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT0.format(d);
    return val;
  }
  
  
  /**
   * Formatta e arrotonda a 0 cifre decimali un BigDecimal
   * 
   * @return
   */
  public static String formatAndRoundBigDecimal0(BigDecimal str)
  {
    double d = 0.0;
    try
    {
      str = str.setScale(0, BigDecimal.ROUND_HALF_UP);
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT0.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  /**
   * Arrotonda a 0 cifre decimali un BigDecimal
   * 
   * @return
   */
  public static BigDecimal roundBigDecimal0(BigDecimal str)
  {
    str = str.setScale(0, BigDecimal.ROUND_HALF_UP);
    
    return str;
  }
  
  /**
   * Arrotonda a 1 cifre decimali un BigDecimal
   * 
   * @return
   */
  public static BigDecimal roundBigDecimal1(BigDecimal str)
  {
    str = str.setScale(1, BigDecimal.ROUND_HALF_UP);
    
    return str;
  }
  
  /**
   * Formatta e arrotonda a 1 cifra decimale un BigDecimal
   * 
   * @return
   */
  public static String formatAndRoundBigDecimal1(BigDecimal str)
  {
    double d = 0.0;
    try
    {
      str = str.setScale(1, BigDecimal.ROUND_HALF_UP);
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT1.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }
  
  /**
   * Formatta e arrotonda a 0 cifre decimali un BigDecimal con 4 decimali
   * 
   * @return
   */
  /*public static String formatAndRoundBigDecimal4(BigDecimal str)
  {
    
    double d = 0.0;
    try
    {
      str = str.setScale(4, BigDecimal.ROUND_HALF_UP);
      d = Double.parseDouble(str.toString().replace(',', '.'));
    }
    catch (Exception ex)
    {
      return "";
    }
    String val = DOUBLEFORMAT0.format(d);
    if (val != null)
    {
      val.replace('.', ',');
    }
    return val;
  }*/

  public static String formatMigliaia(long value)
  {
    try
    {
      String val = SEPARATOR_FORMAT.format(value);
      return val.replace(',', '.');
    }
    catch (NumberFormatException ex)
    {
      return "";
    }
  }

  public static String formatMigliaia(String value)
  {
    try
    {
      return formatMigliaia(new Long(value.replace(',', '.')).longValue());
    }
    catch (Exception ex)
    {
      return "";
    }
  }

  /**
   * Formatta una stringa come numero double di 4 cifre se il dato è
   * effettivamente un double altrimenti restituisce il valore passato (utile
   * per la visualizzazione dei dati dell'utente nelle view)
   * 
   * @param value
   * @return
   */
  public static String formatDouble4IfNumber(String value)
  {
    String result = formatDouble4(value);
    if (Validator.isNotEmpty(result))
    {
      return result;
    }
    else
    {
      return value;
    }
  }
  
  /**
   * Questo metodo Data una stringa contente un numero restituisce questo numero,
   * arrotondato alla seconda cifra decimale e con i separatori delle migliaia
   * L'uso del bigdecimal garantisce di non perdere precisione
   * @param value
   * @return
   */
  public static String formatEuro(String value)
  {
	  BigDecimal euroB=null;
      String euro="";
      try
      {
    	  euroB=new BigDecimal(value);
        DecimalFormat nf=new DecimalFormat("#,##0.00");
        euro=nf.format(euroB);
      }
      catch(Exception e) 
      {}
      return euro;
  }
  
  
  public static String formatDouble4Separator(double d)
  {
    String val = DOUBLE_SEPARATOR_FORMAT4.format(d);
    return val;
  }
  
  public static String formatDouble4Separator(Object str)
  {
    try
    {
      String val = formatDouble4Separator(new Double(str.toString().replace(',', '.'))
          .doubleValue());
      return val;
    }
    catch (Exception e)
    {
      return "";
    }
  }
  
  
}
