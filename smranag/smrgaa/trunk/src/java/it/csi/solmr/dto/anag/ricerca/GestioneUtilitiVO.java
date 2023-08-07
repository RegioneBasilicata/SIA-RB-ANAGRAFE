package it.csi.solmr.dto.anag.ricerca;

import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class GestioneUtilitiVO implements Serializable
{
	/**
   * 
   */
  private static final long serialVersionUID = 3158069149813950544L;

  /**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	


	public GestioneUtilitiVO() {
	}

	public ValidationErrors validateRicercaTrasferimentoAzienda(HttpServletRequest request, ValidationErrors errors) {
	  if(errors == null) {
      errors = new ValidationErrors();
    }
	  
	  String intermediario_1 = (String)request.getParameter("idIntermediario_1");
	  String intermediario_2 = (String)request.getParameter("idIntermediario_2");
	  String cuaa = (String)request.getParameter("cuaa");
	  
	  if(!Validator.isNotEmpty(intermediario_1)) {
      errors.add("idIntermediario_1", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    } 
	  
	  if(!Validator.isNotEmpty(intermediario_2)) {
      errors.add("idIntermediario_2", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
	  
	  if(Validator.isNotEmpty(intermediario_1) && Validator.isNotEmpty(intermediario_2))
	  {
	    if(intermediario_1.equals(intermediario_2))
	    {
	      errors.add("idIntermediario_2", new ValidationError(AnagErrors.ERR_CAA_IDENTICI));
	    }
	  }
	  
	  if(Validator.isNotEmpty(cuaa))
    {
	    if((cuaa.length() !=11) && (cuaa.length() !=16))
      {
	      errors.add("cuaa", new ValidationError(AnagErrors.ERR_CUAA_ERRATO_GU));
      }
      else
      {
        if((cuaa.length() == 16) && !Validator.controlloCf(cuaa)) 
        {
          errors.add("cuaa", new ValidationError(AnagErrors.ERR_CODICE_FISCALE));
        }
        if((cuaa.length() == 11) && !Validator.controlloPIVA(cuaa)) 
        {
          errors.add("cuaa", new ValidationError(AnagErrors.ERR_PARTITA_IVA_ERRATA));
        }
      }
    }

		
		return errors;
	}

	

	
}
