package it.csi.smranag.smrgaa.util;



import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.dto.log.Variabile;
import it.csi.solmr.util.SolmrLogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoggerUtils
{
  /**
   * Massimo numero di iterazioni utilizzabili durante la stampa di un VO
   * (numero massimo di livelli di introspezione che vengono utilizzati).<br/>
   * Esempio: se MAX_ITERATION_FOR_DUMPING=2 verranno visualizzati i figli e i
   * nipoti del VO in esame. Se esistno anche i pronipoti verrà visualizzato al
   * posto il tag <MAX_ITERATION_REACHED/>
   */
  public static final int MAX_ITERATION_FOR_DUMPING = 4;

  public static String getStackTrace(Throwable t)
  {
    StringWriter sw = new StringWriter();
    t.printStackTrace(new PrintWriter(sw));

    return sw.toString();
  }

  /**
   * Formato della data
   */
  public final static String DATA = "dd/MM/yyyy";

  public static String getXMLObjectDump(Object obj, String nome)
  {
    try
    {
      return getXMLObjectDump(obj, nome, 0);
    }
    catch (Throwable th)
    {
      try
      {
        // Faccio ancora un catch per evitare problemi con il th.toString()
        return "<dumping-error>" + th.toString() + "</dumping-error>";
      }
      catch (Throwable t)
      {
        return "<dumping-error>Unknown Error</dumping-error>";
      }
    }
  }

  /**
   * Restituisce una stringa con un xml che descrive i campi (definiti come
   * getXXX) dell'oggetto passato
   * 
   * @param obj
   *          oggetto di cui fare il dump
   * @param nome
   *          nome dell'oggetto (corrisponde al nome del tag di root del xml)
   * @return
   */
  public static String getXMLObjectDump(Object obj, String nome,
      int iterationNumber)
  {
    try
    {
      return getInfo(obj, nome, iterationNumber);
    }
    catch (Exception e)
    {
      return new StringBuffer("\nError inspecting variable ").append(nome)
          .append(". Error:").append(e.toString()).toString();
    }
  }

  /**
   * Restituisce una stringa con un xml che descrive i campi (definiti come
   * getXXX) dell'oggetto passato
   * 
   * @param obj
   *          oggetto di cui fare il dump
   * @param nome
   *          nome dell'oggetto (corrisponde al nome del tag di root del xml)
   * @return
   */
  public static String getInfo(Object obj, String nome, int iterationNumber)
  {
    if (iterationNumber > MAX_ITERATION_FOR_DUMPING)
    {
      return "<MAX_ITERATION_REACHED/>";
    }
    if (obj == null)
    {
      return getTagBaseType(nome, null);
    }

    if (obj instanceof String || obj instanceof Integer || obj instanceof Long
        || obj instanceof Double || obj instanceof Float
        || obj instanceof Boolean || obj instanceof Character
        || obj instanceof Byte || obj instanceof StringBuffer 
        || obj instanceof java.math.BigDecimal || obj instanceof java.math.BigInteger)
    {
      return getTagBaseType(nome, obj.toString());
    }

    if (obj instanceof java.util.Date)
    {
      return getTagBaseType(nome, obj.toString());
    }

    if (obj instanceof java.util.Map)
    {
      return getMap((Map<?,?>) obj, nome, iterationNumber + 1);
    }

    if (obj instanceof java.util.Vector)
    {
      Vector<?> vect = (Vector<?>) obj;
      int size = vect.size();
      StringBuffer sb = new StringBuffer("");

      for (int i = 0; i < size; i++)
      {
        String nomeOggetto = null;
        Object elem = vect.get(i);

        if (elem == null)
        {
          nomeOggetto = nome + i;
        }
        else
        {
          nomeOggetto = elem.getClass().getName();
          nomeOggetto = nomeOggetto.substring(nomeOggetto.lastIndexOf('.') + 1);
        }

        sb.append(getInfo(elem, nomeOggetto, iterationNumber + 1));
      }

      return getTag(nome, sb.toString());
    }

    if (obj instanceof java.lang.Object[])
    {
      Object[] vect = (Object[]) obj;
      int size = vect.length;
      StringBuffer sb = new StringBuffer("");

      for (int i = 0; i < size; i++)
      {
        String nomeOggetto = null;
        Object elem = vect[i];

        if (elem == null)
        {
          nomeOggetto = nome + i;
        }
        else
        {
          nomeOggetto = elem.getClass().getName();
          nomeOggetto = nomeOggetto.substring(nomeOggetto.lastIndexOf('.') + 1);
        }

        sb.append(getInfo(elem, nomeOggetto, iterationNumber + 1));
      }

      return getTag(nome, sb.toString());
    }
    if (obj instanceof long[])
    {
      long[] vect = (long[]) obj;
      int size = vect.length;
      StringBuffer sb = new StringBuffer("");

      for (int i = 0; i < size; i++)
      {
        String nomeOggetto = null;
        long elem = vect[i];

        nomeOggetto = nome + i;

        sb.append(getInfo(String.valueOf(elem), nomeOggetto,
            iterationNumber + 1));
      }
      return getTag(nome, sb.toString());
    }
    if (obj instanceof int[])
    {
      int[] vect = (int[]) obj;
      int size = vect.length;
      StringBuffer sb = new StringBuffer("");

      for (int i = 0; i < size; i++)
      {
        String nomeOggetto = null;
        int elem = vect[i];
        nomeOggetto = nome + i;
        sb.append(getInfo(String.valueOf(elem), nomeOggetto,
            iterationNumber + 1));

      }

      return getTag(nome, sb.toString());
    }
    if (obj instanceof boolean[])
    {
      boolean[] vect = (boolean[]) obj;
      int size = vect.length;
      StringBuffer sb = new StringBuffer("");

      for (int i = 0; i < size; i++)
      {
        String nomeOggetto = null;
        boolean elem = vect[i];

        nomeOggetto = nome + i;

        sb.append(getInfo(String.valueOf(elem), nomeOggetto,
            iterationNumber + 1));
      }

      return getTag(nome, sb.toString());
    }
    if (obj instanceof char[])
    {
      char[] vect = (char[]) obj;
      int size = vect.length;
      StringBuffer sb = new StringBuffer("");

      for (int i = 0; i < size; i++)
      {
        String nomeOggetto = null;
        char elem = vect[i];

        nomeOggetto = nome + i;

        sb.append(getInfo(String.valueOf(elem), nomeOggetto,
            iterationNumber + 1));
      }

      return getTag(nome, sb.toString());
    }

    // Lo considero come tipo base Object
    Method[] metodi = obj.getClass().getMethods();
    int length = 0;

    if (metodi != null)
    {
      length = metodi.length;
    }

    StringBuffer sb = new StringBuffer("");

    for (int i = 0; i < length; i++)
    {
      Method m = metodi[i];
      String nomeMetodo = m.getName();
      Class<?>[] parameters = m.getParameterTypes();

      if (nomeMetodo.startsWith("get") && !nomeMetodo.equals("getClass")
          && ((parameters == null) || (parameters.length == 0)))
      {
        try
        {
          sb.append(getInfo(m.invoke(obj, (Object[])null), nomeMetodo.substring(3),
              iterationNumber + 1));
        }
        catch (Exception e)
        {
        }
      }

      if (nomeMetodo.startsWith("is")
          && ((parameters == null) || (parameters.length == 0)))
      {
        try
        {
          sb.append(getInfo(m.invoke(obj, (Object[])null), nomeMetodo.substring(2),
              iterationNumber + 1));
        }
        catch (Exception e)
        {
        }
      }
    }

    return getTag(nome, sb.toString());
  }

  /**
   * Metodo di utilità che incapsula in un tag xml un valore di una proprietà.
   * Utilizzato da getInfo()
   * 
   * @param nome
   *          nome della proprietà
   * @param valore
   *          valore della proprietà
   * @return
   */
  public static String getTag(String nome, String valore)
  {
    StringBuffer sb = new StringBuffer("<");
    sb.append(nome);
    sb.append(">");
    sb.append(valore);
    sb.append("</");
    sb.append(nome);
    sb.append(">\n");

    return sb.toString();
  }

  /**
   * Funzione di utilità che converte un oggetto di tipo Map in un nodo xml
   * (Stringa). Utilizzata da getInfo()
   * 
   * @param map
   *          map da convertire
   * @param nome
   *          nome della map (corrisponde al nome del nodo)
   * @return
   */
  public static String getMap(Map<?,?> map, String nome, int iterationNumber)
  {
    Iterator<?> it = map.keySet().iterator();
    StringBuffer sb = new StringBuffer("\n<");
    sb.append(nome);
    sb.append(">");

    while (it.hasNext())
    {
      Object key = it.next();
      sb.append(getInfo(map.get(key), "" + key, iterationNumber + 1));
    }

    sb.append("</");
    sb.append(nome);
    sb.append(">\n");

    return sb.toString();
  }

  /**
   * Funzione di utilità che converte un dato di tipo base (long, int,
   * boolean...) in un tag xml.
   * 
   * @param nome
   *          nome del dato (corrisponde al nome del nodo)
   * @param valore
   *          valore
   * @return
   */
  public static String getTagBaseType(String nome, String valore)
  {
    StringBuffer sb = new StringBuffer("<");
    sb.append(nome);
    sb.append(">");
    sb.append(replaceXmlCharacters(valore));
    sb.append("</");
    sb.append(nome);
    sb.append(">\n");

    return sb.toString();
  }

  /**
   * Esegue l'escape di una stringa per inserirla dentro un xml.
   * 
   * @param value
   *          valore da convertire
   * @return
   */
  public static String replaceXmlCharacters(String value)
  {
    if (value == null)
    {
      return value;
    }

    // & deve essere sostituita prima degli altri
    value = replaceChar(value, '&', "&amp;");
    value = replaceChar(value, '<', "&lt;");
    value = replaceChar(value, '>', "&gt;");

    return value;
  }

  /**
   * Funzione di utilità che rimpiazza le occorrenze di un certo carattere di
   * una stringa con un'altra stringa.
   * 
   * @param str
   *          stringa da elaborare
   * @param chr
   *          carattere da sostituire
   * @param value
   *          stringa con cui rimpiazzare chr
   * @return
   */
  public static String replaceChar(String str, char chr, String value)
  {
    if (str == null)
    {
      return str;
    }

    int pos = str.indexOf(chr);
    int length = value.length() - 1;

    while (pos > -1)
    {
      str = str.substring(0, pos) + value + str.substring(pos + 1);
      pos = str.indexOf(chr, pos + length);
    }

    return str;
  }

  /**
   * Converte una data in una stringa (formato dd/MM/yyyy)
   * 
   * @param date
   * @return
   * @throws Exception
   */
  public static Date parseDate(String date) throws Exception
  {
    return parse(date, DATA);
  }

  /**
   * Convert euna data in una stringa (formato arbitrario)
   * 
   * @param toParse
   * @param format
   *          formato della stringa
   * @return
   * @throws Exception
   */
  public static Date parse(String toParse, String format) throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(format);

    return sdf.parse(toParse);
  }

  public static void logDAOError(Object thrower, String loggingPrefix,
      Throwable t, String query, Variabile variables[], Parametro parameters[])
  {
    SolmrLogger.error(thrower, loggingPrefix
        + " ERRORE NEL DAO: DUMP IN CORSO");
    SolmrLogger.error(thrower, loggingPrefix + " Eccezione:\n"
        + getStackTrace(t));
    SolmrLogger.error(thrower, loggingPrefix + "Query = " + query
        + "\nDUMP DELLE VARIABILI - INIZIO\n");
    dumpVariablesForErrors(thrower, loggingPrefix, variables);
    dumpParametersForErrors(thrower, loggingPrefix, parameters);
  }

  public static void logEJBError(Object thrower, String loggingPrefix,
      Throwable t, Variabile variables[], Parametro parameters[])
  {
    SolmrLogger.error(thrower, loggingPrefix
        + " ERRORE NELL'EJB: DUMP IN CORSO");
    SolmrLogger.error(thrower, loggingPrefix + " Eccezione:\n"
        + getStackTrace(t));
    dumpVariablesForErrors(thrower, loggingPrefix, variables);
    dumpParametersForErrors(thrower, loggingPrefix, parameters);
  }

  public static void logFacadeError(Object thrower, String loggingPrefix,
      Throwable t, Variabile variables[], Parametro parameters[])
  {
    SolmrLogger.error(thrower, loggingPrefix
        + " ERRORE NELLA FACADE: DUMP IN CORSO");
    SolmrLogger.error(thrower, loggingPrefix + " Eccezione:\n"
        + getStackTrace(t));
    dumpVariablesForErrors(thrower, loggingPrefix, variables);
    dumpParametersForErrors(thrower, loggingPrefix, parameters);
  }

  public static void dumpParametersForErrors(Object thrower,
      String loggingPrefix, Parametro parameters[])
  {
    SolmrLogger.error(thrower, loggingPrefix + " DUMP DEI PARAMETRI BEGIN.");
    logListOfObjects(thrower, loggingPrefix, "Parametro", parameters);
    SolmrLogger.error(thrower, loggingPrefix + " DUMP DEI PARAMETRI END.");
  }

  public static void dumpVariablesForErrors(Object thrower,
      String loggingPrefix, Variabile variables[])
  {
    SolmrLogger
        .error(thrower, loggingPrefix + " DUMP DELLE VARIABILI BEGIN.");
    logListOfObjects(thrower, loggingPrefix, "Variabile", variables);
    SolmrLogger.error(thrower, loggingPrefix + " DUMP DELLE VARIABILI END.");
  }

  public static void logListOfObjects(Object thrower, String loggingPrefix,
      String xmlRootName, Object objList[])
  {
    int length = objList == null ? 0 : objList.length;
    for (int i = 0; i < length; i++)
    {
      SolmrLogger.error(thrower, loggingPrefix + "\n"
          + getXMLObjectDump(objList[i], xmlRootName));
    }
  }
  
  
  public boolean isSafeForLogging(Object obj, String nome)
  {
    // Faccio il dump solo degli oggetti Sicuri
    if (obj == null)
    {
      return true;
    }
    String className = obj.getClass().getName();
    if (className.startsWith("it.csi.solmr."))
    {
      // Se è un oggetto "nostro" deve essere un dto"
      if (!className.startsWith("it.csi.solmr.dto."))
      {
        return false;
      }
    }
    else
    {
      if (className.startsWith("it.csi.smranag.smrgaa."))
      {
        // Se è un oggetto "nostro" deve essere un dto"
        if (!className.startsWith("it.csi.smranag.smrgaa.dto."))
        {
          return false;
        }
      }      
      else
      {
        // Se non è un oggetto "nostro" DEVE essere un oggetto "standard" di java (lang o util)
        if (!className.startsWith("java.lang.") && !className.startsWith("java.util."))
        {
          return false;
        }
      }
    }
    return true;
  }
  
  
  public void errorDumpObject(String msg, Object obj,String name){
	    StringBuffer sb = new StringBuffer(msg == null ? "" : msg)
	        .append(":\n").append(getXMLObjectDump(obj, name)).append(
	            "\n");
	    SolmrLogger.error(this,sb.toString());
  }
  
  
  public void dumpHttpSessionAttributes(HttpSession session)
  {    
    java.util.Enumeration<?> sessionEnum = session.getAttributeNames();
    while (sessionEnum.hasMoreElements())
    {
      String name = (String) sessionEnum.nextElement();
      Object attribute = session.getAttribute(name);
      if (isSafeForLogging(attribute, name))
      {
        errorDumpObject("[LoggerUtils::dumpHttpSessionAttributes ]"+"  Attributo " + name + ":\n", attribute, name);
      }
      else
      {
    	  SolmrLogger.error(this,"[LoggerUtils::dumpHttpSessionAttributes]"+"  Attributo " + name + " [" + attribute.getClass().getName()
            + "] SKIPPED BECAUSE IT ISN'T SAFE FOR LOGGING!");
      }
    }

  }

  public void dumpHttpRequestParameters(HttpServletRequest request)
  {
    final String THIS_METHOD = "dumpHttpRequestParameters";
    java.util.Enumeration<?> requestEnum = request.getParameterNames();

    while (requestEnum.hasMoreElements())
    {
      String name = (String) requestEnum.nextElement();
      String value = request.getParameter(name);
      SolmrLogger.info(this,
          "[LoggerUtils::dumpHttpRequestParameters" + THIS_METHOD + "] request.getParameter(\"" + name +
              "\")=" + "\"" + value + "\"");
    }
  }

  public void dumpHttpRequestAttributes(HttpServletRequest request)
  {
    final String THIS_METHOD = "dumpHttpRequestAttributes";
    java.util.Enumeration<?> sessionEnum = request.getAttributeNames();
    while (sessionEnum.hasMoreElements())
    {
      String name = (String) sessionEnum.nextElement();
      Object attribute = request.getAttribute(name);
      if (isSafeForLogging(attribute, name))
      {
    	  errorDumpObject("[LoggerUtils::dumpHttpRequestAttributes ]"+"  Attributo " + name + ":\n", attribute, name);
      }
      else
      {
    	  SolmrLogger.error(this,"[LoggerUtils::dumpHttpRequestAttributes]"+"  Attributo " + name + " [" + attribute.getClass().getName()
    	            + "] SKIPPED BECAUSE IT ISN'T SAFE FOR LOGGING!");
      }
    }

  }
  
}
