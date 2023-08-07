package it.csi.smranag.smrgaa.dto;

/**
 * Value Object per contenere coppie di id/descrizione Oct 22, 2008
 * 
 * @author TOBECONFIG
 */
public class BaseCodeDescription implements ICodeDescription
{
  /** serialVersionUID */
  private static final long serialVersionUID = 7011936396602876167L;
  /** Codice (ID), ovviamente non nullabile */
  private long              code;
  /** Descrizione della decodifica */
  private String            description;
  /** Codice secondario della decodifica (per le tabelle che lo prevedono) */
  private Object            item;

  public Object getItem()
  {
    return item;
  } 

  public void setItem(Object item)
  {
    this.item = item;
  }

  public long getCode()
  {
    return code;
  }

  public void setCode(long code)
  {
    this.code = code;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
}
