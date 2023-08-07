package it.csi.solmr.exception;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import java.lang.reflect.*;
import javax.ejb.*;
import it.csi.csi.wrapper.*;
import it.csi.solmr.etc.*;
import it.csi.iride2.policy.exceptions.IdentitaNonAutenticaException;
import it.csi.iride2.policy.exceptions.InternalException;
import it.csi.iride2.policy.exceptions.NoSuchApplicationException;
import it.csi.iride2.iridefed.exceptions.BadRuoloException;
import it.csi.iride2.policy.exceptions.NoSuchUseCaseException;
import it.csi.iride2.policy.exceptions.CertRevokedException;
import it.csi.iride2.policy.exceptions.CertOutsideValidityException;
import it.csi.iride2.policy.exceptions.IdProviderNotFoundException;
import it.csi.iride2.policy.exceptions.AuthException;
import it.csi.iride2.policy.exceptions.MalformedUsernameException;
import it.csi.solmr.exception.services.InvalidParameterException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.exception.services.MaxRecordException;
import it.csi.solmr.exception.services.NoDataFoundException;
import it.csi.solmr.exception.services.UserNotFoundException;
import it.csi.solmr.exception.services.ErrorTypesComune;
import it.csi.solmr.exception.services.ServiceSystemException;

public abstract class ExcManager
{

  public static Exception remap(Exception exc)
  {
    String message = "";
    SolmrException solmrExc = null;
    int errorType = 0;
    if (exc instanceof InvocationTargetException) {
      InvocationTargetException itexc = (InvocationTargetException)exc;
      Throwable subExc = itexc.getTargetException();
      return remap((Exception)subExc);
    }
    if (exc instanceof IdentitaNonAutenticaException)
    {
      return exc;
    }
    if (exc instanceof InternalException)
    {
      return exc;
    }
    if (exc instanceof NoSuchApplicationException)
    {
      return exc;
    }
    if (exc instanceof BadRuoloException)
    {
      return exc;
    }
    if (exc instanceof NoSuchUseCaseException)
    {
      return exc;
    }
    if (exc instanceof CertRevokedException)
    {
      return exc;
    }
    if (exc instanceof CertOutsideValidityException)
    {
      return exc;
    }
    if (exc instanceof IdProviderNotFoundException)
    {
      return exc;
    }
    if (exc instanceof AuthException)
    {
      return exc;
    }
    if (exc instanceof MalformedUsernameException)
    {
      return exc;
    }
    if(exc instanceof InvalidParameterException)
     {
      return new InvalidParameterException(ErrorTypes.STR_INVALID_PARAMETER);
    }
    else if(exc instanceof MaxRecordException)
     {
      return new MaxRecordException(ErrorTypes.STR_MAX_RECORD);
    }
    else if(exc instanceof NoDataFoundException)
     {
      return new NoDataFoundException(ErrorTypes.STR_NO_RECORD);
    }
    else if(exc instanceof UserNotFoundException)
    {
      return new UserNotFoundException(ErrorTypesComune.STR_USER_VIEW_IRIDE1_NOT_FOUND);
    }
    else if(exc instanceof ServiceSystemException)
    {
      return exc;
    }




    if (exc instanceof SolmrException) {
      SolmrException cexc = (SolmrException)exc;
      //message = cexc.getNestedExcMsg();
      message = cexc.getMessage();
      errorType = cexc.getErrorType();
      solmrExc = new SolmrException(message, errorType);
    } else if (exc instanceof UserException) {
      UserException uexc = (UserException)exc;
      message = uexc.getNestedExcMsg();
      solmrExc = new SolmrException(message/*, uexc*/);
    } else if (exc instanceof CSIException) {
      CSIException cexc = (CSIException)exc;
      message = cexc.getNestedExcMsg();
      solmrExc = new SolmrException(message/*, cexc*/);
    } else if (exc instanceof EJBException) {
      EJBException eexc = (EJBException)exc;
      message = eexc.getCausedByException().getMessage();
      solmrExc = new SolmrException(SolmrErrors.GENERIC_SYSTEM_EXCEPTION, eexc);
    } else if (exc instanceof UndeclaredThrowableException) {
      UndeclaredThrowableException utexc = (UndeclaredThrowableException)exc;
      message = utexc.getUndeclaredThrowable().getMessage();
      solmrExc = new SolmrException(SolmrErrors.GENERIC_SYSTEM_EXCEPTION, utexc);
    } else {
      message = exc.getMessage();
      solmrExc = new SolmrException(message/*, exc*/);
    }
    return solmrExc;
  }


  }
