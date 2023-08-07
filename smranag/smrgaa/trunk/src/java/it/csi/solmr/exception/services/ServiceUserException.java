package it.csi.solmr.exception.services;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG & TOBECONFIG
 * @version 1.0
 */

import it.csi.csi.wrapper.*;
import java.io.*;

public class ServiceUserException extends UserException implements Serializable
{
  /**
     * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
     * compatibili con le versioni precedenti utilizzate da eventuali client
   */
   static final long serialVersionUID = -7254276069352258308L;

  private int errorType;
  private String errorDesc;

  public ServiceUserException(java.lang.String msg,int errorType)
  {
    super(msg);
    this.setErrorType(errorType);
    this.setErrorDesc(msg);
  }

  public ServiceUserException(String msg,
                              String nestedClass,
                              String nestedMsg,
                              int errorType)
  {
    super(msg,nestedClass,nestedMsg);
    this.setErrorType(errorType);
    this.setErrorDesc(msg);
  }

  public ServiceUserException(String msg,
                              String nestedClass,
                              String nestedMsg,
                              String stackTrace,
                              int errorType)
  {
    super(msg,nestedClass,nestedMsg,stackTrace);
    this.setErrorType(errorType);
    this.setErrorDesc(msg);
  }

  public ServiceUserException(String msg,
                              Throwable nested,
                              int errorType)
  {
    super(msg,nested);
    this.setErrorType(errorType);
    this.setErrorDesc(msg);
  }


  private void setErrorType(int errorType)
  {
    this.errorType = errorType;
  }
  public int getErrorType()
  {
    return errorType;
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