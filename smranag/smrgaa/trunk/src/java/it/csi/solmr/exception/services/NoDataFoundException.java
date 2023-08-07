package it.csi.solmr.exception.services;

import it.csi.csi.wrapper.UserException;

public class NoDataFoundException extends UserException
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -1789957842195799308L;

  private String errorDesc;

  public NoDataFoundException(String message)
  {
    super(message);
    setErrorDesc(message);
    this.setNestedExcClassName("it.csi.solmr.exception.services.NoDataFoundException");
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