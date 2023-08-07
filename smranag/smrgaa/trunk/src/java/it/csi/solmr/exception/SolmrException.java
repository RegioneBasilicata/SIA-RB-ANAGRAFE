package it.csi.solmr.exception;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import it.csi.csi.wrapper.*;
import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

public class SolmrException extends CSIException
{

  static final long serialVersionUID = 1872056012811237785L;
  
  
  public static int CODICE_ERRORE_WSBRIDGE_NON_DISPONIBILE = 0x20000001;
  
  public static int CODICE_ERRORE_DI_ACCESSO_A_WSBRIDGE = 0x20000003;
  
  public static int CODICE_ERRORE_AGRIWELL_NON_DISPONIBILE = 0x20000005;
  
  public static int CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL = 0x20000007;
  
  public static int CODICE_ERRORE_UMASERV_NON_DISPONIBILE = 0x20000009;
  
  public static int CODICE_ERRORE_DI_ACCESSO_A_UMASERV = 0x20000011;
  
  public static int CODICE_ERRORE_MODOLSERV_NON_DISPONIBILE = 0x20000013;
  
  public static int CODICE_ERRORE_DI_ACCESSO_A_MODOLSERV = 0x20000015;

  private int errorType;
  private String errorDesc;

  private String messaggioSolmr;
  private ValidationErrors validationErrors=null;

  public SolmrException()
  {
    this(SolmrErrors.EXC_BUSINESS);
  }

  public SolmrException(String message) {
    super(message);
    setMessaggioSolmr(message);
    this.setNestedExcClassName("it.csi.solmr.exception.SolmrException");
    this.setNestedExcMsg(message);
  }

  public SolmrException(String message, Throwable cause) {
    super(message, cause);
    setMessaggioSolmr(message);
  }

  public SolmrException(String message, ValidationErrors validationErrors) {
    super(message);
    setMessaggioSolmr(message);
    this.setValidationErrors(validationErrors);
  }

  public SolmrException(String msg,
                        int errorType)
  {
    super(msg);
    setMessaggioSolmr(msg);
    this.setErrorType(errorType);
    this.setErrorDesc(msg);
    this.setNestedExcClassName("it.csi.solmr.exception.SolmrException");
    this.setNestedExcMsg(msg);
  }


  public String getMessage() {
    return messaggioSolmr;
  }

  private void setMessaggioSolmr(String messaggio) {
    messaggioSolmr = messaggio;
  }
  public void setValidationErrors(it.csi.solmr.util.ValidationErrors validationErrors)
  {
    this.validationErrors = validationErrors;
  }
  public ValidationErrors getValidationErrors()
  {
    return validationErrors;
  }

  public void setErrorType(int errorType)
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
