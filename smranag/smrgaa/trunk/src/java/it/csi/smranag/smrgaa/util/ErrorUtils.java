package it.csi.smranag.smrgaa.util;

import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;

public class ErrorUtils
{
 
  public static ValidationErrors setValidErrNoNull(ValidationErrors errors, String property, String msg)
  {
    if(errors == null)
    {
      errors = new ValidationErrors();
    }
    errors.add(property, new ValidationError(msg));
    
    return errors;
  }
  
  
}
