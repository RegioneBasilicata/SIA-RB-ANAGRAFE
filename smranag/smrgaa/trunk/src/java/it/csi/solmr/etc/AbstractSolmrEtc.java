package it.csi.solmr.etc;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class AbstractSolmrEtc {
  protected static final String MY_RESOURCE_BUNDLE = "";
  protected static final Class<?> THIS_CLASS = AbstractSolmrEtc.class;

  public static HashMap<Object,Object> OTHERS = new HashMap<Object,Object>();

  protected static synchronized void initClass(Class<?> theClass, String resourceName) {
    if (resourceName!=null && resourceName.trim().length()>0) {
      ResourceBundle resB = ResourceBundle.getBundle(resourceName);
      Enumeration<String> enumeration = resB.getKeys();
      while (enumeration.hasMoreElements()) {
	String currentKey = (String)enumeration.nextElement();
	try {
	  Field currentField = theClass.getField(currentKey);
	  currentField.set(null, resB.getString(currentKey).trim());
	} catch (IllegalArgumentException ex) {
	} catch (IllegalAccessException ex) {
	} catch (NoSuchFieldException ex) {
	} catch (SecurityException ex) {
	} catch (MissingResourceException ex) {
	}
	set(theClass, currentKey, resB.getObject(currentKey));
      }
    }
  }

  protected static synchronized void initClassNumeric(Class<?> theClass, String resourceName) {
    if (resourceName!=null && resourceName.trim().length()>0) {
      ResourceBundle resB = ResourceBundle.getBundle(resourceName);
      Enumeration<String> enumeration = resB.getKeys();
      while (enumeration.hasMoreElements()) {
	String currentKey = (String)enumeration.nextElement();
	Object currentObj = resB.getObject(currentKey);
	try {
	  Field currentField = theClass.getField(currentKey);
	  //SolmrLogger.debug(this, "CurrentField: "+currentField.getName()+" Type: "+currentField.getType());
	  if (currentField.getType().equals(Integer.class)||currentField.getType().equals(int.class)) {
	    currentField.set(null, Integer.valueOf((String)currentObj));
	  } else if (currentField.getType().equals(Long.class)||currentField.getType().equals(long.class)) {
	    currentField.set(null, Long.valueOf((String)currentObj));
	  } else if (currentField.getType().equals(Float.class)||currentField.getType().equals(float.class)) {
	    currentField.set(null, Float.valueOf((String)currentObj));
	  } else if (currentField.getType().equals(Double.class)||currentField.getType().equals(double.class)) {
	    currentField.set(null, Double.valueOf((String)currentObj));
	  }
	} catch (IllegalArgumentException ex) {
	} catch (IllegalAccessException ex) {
	} catch (NoSuchFieldException ex) {
	} catch (SecurityException ex) {
	} catch (MissingResourceException ex) {
	}
	Object toPut = null;
	try {
	  toPut = Long.valueOf((String)currentObj);
	} catch (Exception exc) {
	  try {
	    toPut = Double.valueOf((String)currentObj);
	  } catch (Exception ex) {
	    toPut = (String)currentObj;
	  }
	}
	set(theClass, currentKey, toPut);
      }
    }
  }

  protected static Object get(Class<?> theClass, String key) {
    Object result = null;
    if (key != null && theClass != null) {
      HashMap<?,?> innerHM = (HashMap<?,?>)OTHERS.get(theClass);
      if (innerHM != null)
	result = innerHM.get(key);
      if (result == null) {
        try {
          Field theField = theClass.getField(key);
          result = theField.get(result);
        }catch (SecurityException ex) {
        }catch (NoSuchFieldException ex) {
        }catch (IllegalAccessException ex) {
        }catch (IllegalArgumentException ex) {
        }
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private static void set(Class<?> theClass, String key, Object obj) {
    HashMap<Object,Object> innerHM = (HashMap)OTHERS.get(theClass);
    if (innerHM == null)
      innerHM = new HashMap<Object,Object>();
    if (key!=null) {
      innerHM.put(key, obj);
      OTHERS.put(theClass, innerHM);
    }
  }
}
