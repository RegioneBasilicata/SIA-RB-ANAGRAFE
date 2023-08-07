package it.csi.solmr.dto.anag.terreni;

import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe che si occupa di mappare la tabella DB_UTILIZZO_CONSOCIATO
 * 
 * @author Mauro Vocale
 *
 */

public class UtilizzoConsociatoVO implements Serializable {
		
	
	private static final long serialVersionUID = -7151340834774689137L;
	private Long idUtilizzoConsociato = null;
	private Long idUtilizzoParticella = null;
	private Long idPianteConsociate = null;
	private String numeroPiante = null;
	
	
	
	/**
	 * @return the idPianteConsociate
	 */
	public Long getIdPianteConsociate() {
		return idPianteConsociate;
	}
	/**
	 * @param idPianteConsociate the idPianteConsociate to set
	 */
	public void setIdPianteConsociate(Long idPianteConsociate) {
		this.idPianteConsociate = idPianteConsociate;
	}
	/**
	 * @return the idUtilizzoConsociato
	 */
	public Long getIdUtilizzoConsociato() {
		return idUtilizzoConsociato;
	}
	/**
	 * @param idUtilizzoConsociato the idUtilizzoConsociato to set
	 */
	public void setIdUtilizzoConsociato(Long idUtilizzoConsociato) {
		this.idUtilizzoConsociato = idUtilizzoConsociato;
	}
	/**
	 * @return the idUtilizzoParticella
	 */
	public Long getIdUtilizzoParticella() {
		return idUtilizzoParticella;
	}
	/**
	 * @param idUtilizzoParticella the idUtilizzoParticella to set
	 */
	public void setIdUtilizzoParticella(Long idUtilizzoParticella) {
		this.idUtilizzoParticella = idUtilizzoParticella;
	}
	/**
	 * @return the numeroPiante
	 */
	public String getNumeroPiante() {
		return numeroPiante;
	}
	/**
	 * @param numeroPiante the numeroPiante to set
	 */
	public void setNumeroPiante(String numeroPiante) {
		this.numeroPiante = numeroPiante;
	}
	
	
	public ValidationErrors validateModificaTerritorialeCondUso(HttpServletRequest request, ValidationErrors errors, TipoImpiantoVO tipoImpiantoVO, int indice) {
    if(errors == null) {
      errors = new ValidationErrors();
    }
    if(request.getParameterValues("contatore") != null) {
      // Il numero piante non può essere valorizzato se non viene indicato
      // il tipo impianto o se il FLAG_PIANTE_CONSOCIATE è uguale a N
      if(request.getParameterValues("numeroPianteConsociate") != null && indice < request.getParameterValues("numeroPianteConsociate").length) {
        if(tipoImpiantoVO == null || tipoImpiantoVO.getFlagPianteConsociate().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
          if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[indice])) {
            errors.add("numeroPianteConsociate", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE));
          }
        }
        else {
          // Altrimenti, se viene valorizzato, controllo che abbia un valore corretto
          if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[indice])) {
            if(!Validator.isNumericInteger(request.getParameterValues("numeroPianteConsociate")[indice]) || Integer.parseInt(request.getParameterValues("numeroPianteConsociate")[indice]) == 0) {
              errors.add("numeroPianteConsociate", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            }
          }
        }
      }
    }
    return errors;      
  }
	
	
	
	
}