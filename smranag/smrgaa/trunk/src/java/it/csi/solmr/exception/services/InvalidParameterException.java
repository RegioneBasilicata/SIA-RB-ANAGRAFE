package it.csi.solmr.exception.services;

import it.csi.csi.wrapper.UserException;

public class InvalidParameterException extends UserException
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -2236678858878308135L;

  private String errorDesc;
  private int codeError;
  private String parameterName = "Funzionalità non implementata";

  public InvalidParameterException(String message)
  {
    super(message);
    setErrorDesc(message);
    this.setNestedExcClassName("it.csi.solmr.exception.services.InvalidParameterException");
    this.setNestedExcMsg(message);
  }

  public InvalidParameterException(String message, String parameterName)
  {
    this(message);
    this.parameterName=parameterName;
  }

  /**
   * Costruttore con il messaggio di errore e il codice di riferimento
   *
   * @param message
   * @param code
   */
  public InvalidParameterException(String message, int code) {
    super(message);
    setErrorDesc(message);
    this.setNestedExcClassName("it.csi.solmr.exception.services.InvalidParameterException");
    this.setNestedExcMsg(message);
    this.setCodeError(code);
  }

  private void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  public String getErrorDesc()
  {
    return errorDesc;
  }

  /**
   * @return the codeError
   */
  public int getCodeError() {
    return codeError;
  }

  /**
   * @param codeError the codeError to set
   */
  public void setCodeError(int codeError) {
    this.codeError = codeError;
  }
  public String getParameterName()
  {
    return parameterName;
  }
  public void setParameterName(String parameterName)
  {
    this.parameterName = parameterName;
  }

}