package it.csi.solmr.exception.services;

import it.csi.csi.wrapper.UserException;

public class MaxRecordException extends UserException
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -1269070670383994427L;

  private String errorDesc;

  public MaxRecordException(String message)
  {
    super(message);
    setErrorDesc(message);
    this.setNestedExcClassName("it.csi.solmr.exception.services.MaxRecordException");
    this.setNestedExcMsg(message);
  }

  private void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  public String getErrorDesc()
  {
    return errorDesc;
  }

}