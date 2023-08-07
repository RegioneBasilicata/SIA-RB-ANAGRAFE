/* Generated by Together */

package it.csi.solmr.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * <p>
 * Title: ADVI: Acquisizione Domande Via Internet
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CSI Piemonte
 * </p>
 * 
 * @author Luigi R. Viggiano
 * @version $Revision: 1.1 $
 */
public class ValidationErrors implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -7137107804008051397L;
  /** @link dependency */
  /* # ValidationError lnkValidationError; */

  private Map<String, Collection<ValidationError>> errors;

  public ValidationErrors()
  {
    errors = new HashMap<String, Collection<ValidationError>>();
  }

  public void add(String property, ValidationError error)
  {
    Collection<ValidationError> coll = (Collection<ValidationError>) errors
        .get(property);
    if (coll == null)
    {
      coll = new ArrayList<ValidationError>();
    }
    coll.add(error);
    errors.put(property, coll);
  }

  public void remove(String property)
  {
    errors.remove(property);
  }

  public void clear()
  {
    errors.clear();
  }

  public boolean empty()
  {
    return errors.isEmpty();
  }

  public java.util.Iterator<Collection<ValidationError>> get()
  {
    return errors.values().iterator();
  }

  public java.util.Iterator<ValidationError> get(String property)
  {
    Collection<ValidationError> coll = (Collection<ValidationError>) errors
        .get(property);
    if (coll == null)
    {
      return null;
    }
    else
    {
      return coll.iterator();
    }
  }

  public java.util.Iterator<String> properties()
  {
    return errors.keySet().iterator();
  }

  public int size()
  {
    return errors.size();
  }

  public int size(String property)
  {
    Collection<ValidationError> coll = (Collection<ValidationError>) errors
        .get(property);
    if (coll == null)
    {
      return 0;
    }
    else
    {
      return coll.size();
    }
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("ValidationErrors:\n");
    Set<Map.Entry<String, Collection<ValidationError>>> entries = errors.entrySet();
    Iterator<Map.Entry<String, Collection<ValidationError>>> iter = entries.iterator();
    while (iter.hasNext())
    {
      Map.Entry<String, Collection<ValidationError>> entry = (Map.Entry<String, Collection<ValidationError>>) iter.next();
      sb.append("\tProperty: " + String.valueOf(entry.getKey()) + ":\n");
      Collection<ValidationError> al = (Collection<ValidationError>) entry.getValue();
      Iterator<ValidationError> iter2 = al.iterator();
      while (iter2.hasNext())
      {
        ValidationError ve = (ValidationError) iter2.next();
        sb.append("\t\t" + ve.getMessage() + "\n");
      }
    }
    return sb.toString();
  }

  /**
   * 
   * MODIFICHE
   */
  public java.util.Iterator<String> getKeys()
  {
    return errors.keySet().iterator();
  }

  public boolean contains(String key)
  {

    if (errors == null)
    {
      return false;
    }
    else
    {
      Iterator<ValidationError> itr = get(key);
      return ((itr != null) && (itr.hasNext()));
    }
  }
}
