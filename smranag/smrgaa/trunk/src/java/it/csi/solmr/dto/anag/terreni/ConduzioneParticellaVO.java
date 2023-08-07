
package it.csi.solmr.dto.anag.terreni;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.DocumentoConduzioneVO;
import it.csi.solmr.dto.anag.UteVO;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe che si occupa di mappare la tabella DB_CONDUZIONE_PARTICELLA
 * @author Mauro Vocale
 *
 */
public class ConduzioneParticellaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6642499787857816389L;
	
	private Long idConduzioneParticella = null;
	private Long idParticella = null;
	private Long idTitoloPossesso = null;
	private CodeDescription titoloPossesso = null;
	private Long idUte = null;
	private UteVO uteVO = null;
	private String superficieCondotta = null;
	private String flagUtilizzoParte = null;
	private java.util.Date dataInizioConduzione = null;
	private java.util.Date dataFineConduzione = null;
	private String note = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private UtenteIrideVO utenteAggiornamento = null;
	private String esitoControllo = null;
	private java.util.Date dataEsecuzione = null;
	private String recordModificato = null;
	private UtilizzoParticellaVO[] elencoUtilizzi = null;
	private DocumentoConduzioneVO[] elencoDocumentoConduzione = null;
	private String superficieAgronomica = null;
	private BigDecimal percentualePossesso = null;
	
	
	
	public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
	
	
	/**
	 * @return the idConduzioneParticella
	 */
	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}
	

  /**
	 * @param idConduzioneParticella the idConduzioneParticella to set
	 */
	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
	}

	/**
	 * @return the idParticella
	 */
	public Long getIdParticella() {
		return idParticella;
	}

	/**
	 * @param idParticella the idParticella to set
	 */
	public void setIdParticella(Long idParticella) {
		this.idParticella = idParticella;
	}

	/**
	 * @return the idTitoloPossesso
	 */
	public Long getIdTitoloPossesso() {
		return idTitoloPossesso;
	}

	/**
	 * @param idTitoloPossesso the idTitoloPossesso to set
	 */
	public void setIdTitoloPossesso(Long idTitoloPossesso) {
		this.idTitoloPossesso = idTitoloPossesso;
	}

	/**
	 * @return the titoloPossesso
	 */
	public CodeDescription getTitoloPossesso() {
		return titoloPossesso;
	}

	/**
	 * @param titoloPossesso the titoloPossesso to set
	 */
	public void setTitoloPossesso(CodeDescription titoloPossesso) {
		this.titoloPossesso = titoloPossesso;
	}

	/**
	 * @return the idUte
	 */
	public Long getIdUte() {
		return idUte;
	}

	/**
	 * @param idUte the idUte to set
	 */
	public void setIdUte(Long idUte) {
		this.idUte = idUte;
	}

	/**
	 * @return the uteVO
	 */
	public UteVO getUteVO() {
		return uteVO;
	}

	/**
	 * @param uteVO the uteVO to set
	 */
	public void setUteVO(UteVO uteVO) {
		this.uteVO = uteVO;
	}

	/**
	 * @return the superficieCondotta
	 */
	public String getSuperficieCondotta() {
		return superficieCondotta;
	}

	/**
	 * @param superficieCondotta the superficieCondotta to set
	 */
	public void setSuperficieCondotta(String superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}

	/**
	 * @return the flagUtilizzoParte
	 */
	public String getFlagUtilizzoParte() {
		return flagUtilizzoParte;
	}

	/**
	 * @param flagUtilizzoParte the flagUtilizzoParte to set
	 */
	public void setFlagUtilizzoParte(String flagUtilizzoParte) {
		this.flagUtilizzoParte = flagUtilizzoParte;
	}

	/**
	 * @return the dataInizioConduzione
	 */
	public java.util.Date getDataInizioConduzione() {
		return dataInizioConduzione;
	}

	/**
	 * @param dataInizioConduzione the dataInizioConduzione to set
	 */
	public void setDataInizioConduzione(java.util.Date dataInizioConduzione) {
		this.dataInizioConduzione = dataInizioConduzione;
	}

	/**
	 * @return the dataFineConduzione
	 */
	public java.util.Date getDataFineConduzione() {
		return dataFineConduzione;
	}

	/**
	 * @param dataFineConduzione the dataFineConduzione to set
	 */
	public void setDataFineConduzione(java.util.Date dataFineConduzione) {
		this.dataFineConduzione = dataFineConduzione;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the dataAggiornamento
	 */
	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	/**
	 * @param dataAggiornamento the dataAggiornamento to set
	 */
	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	/**
	 * @return the idUtenteAggiornamento
	 */
	public Long getIdUtenteAggiornamento() {
		return idUtenteAggiornamento;
	}

	/**
	 * @param idUtenteAggiornamento the idUtenteAggiornamento to set
	 */
	public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
		this.idUtenteAggiornamento = idUtenteAggiornamento;
	}

	/**
	 * @return the utenteAggiornamento
	 */
	public UtenteIrideVO getUtenteAggiornamento() {
		return utenteAggiornamento;
	}

	/**
	 * @param utenteAggiornamento the utenteAggiornamento to set
	 */
	public void setUtenteAggiornamento(UtenteIrideVO utenteAggiornamento) {
		this.utenteAggiornamento = utenteAggiornamento;
	}

	/**
	 * @return the esitoControllo
	 */
	public String getEsitoControllo() {
		return esitoControllo;
	}

	/**
	 * @param esitoControllo the esitoControllo to set
	 */
	public void setEsitoControllo(String esitoControllo) {
		this.esitoControllo = esitoControllo;
	}

	/**
	 * @return the dataEsecuzione
	 */
	public java.util.Date getDataEsecuzione() {
		return dataEsecuzione;
	}

	/**
	 * @param dataEsecuzione the dataEsecuzione to set
	 */
	public void setDataEsecuzione(java.util.Date dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

	/**
	 * @return the recordModificato
	 */
	public String getRecordModificato() {
		return recordModificato;
	}

	/**
	 * @param recordModificato the recordModificato to set
	 */
	public void setRecordModificato(String recordModificato) {
		this.recordModificato = recordModificato;
	}

	/**
	 * @return the elencoUtilizzi
	 */
	public UtilizzoParticellaVO[] getElencoUtilizzi() {
		return elencoUtilizzi;
	}

	/**
	 * @param elencoUtilizzi the elencoUtilizzi to set
	 */
	public void setElencoUtilizzi(UtilizzoParticellaVO[] elencoUtilizzi) {
		this.elencoUtilizzi = elencoUtilizzi;
	}

	/**
	 * @return the elencoDocumentoConduzione
	 */
	public DocumentoConduzioneVO[] getElencoDocumentoConduzione() {
		return elencoDocumentoConduzione;
	}

	/**
	 * @param elencoDocumentoConduzione the elencoDocumentoConduzione to set
	 */
	public void setElencoDocumentoConduzione(DocumentoConduzioneVO[] elencoDocumentoConduzione) {
		this.elencoDocumentoConduzione = elencoDocumentoConduzione;
	}
	
	/**
	 * @return the superficieAgronomica
	 */
	public String getSuperficieAgronomica() {
		return superficieAgronomica;
	}

	/**
	 * @param superficieAgronomica the superficieAgronomica to set
	 */
	public void setSuperficieAgronomica(String superficieAgronomica) {
		this.superficieAgronomica = superficieAgronomica;
	}

	

	/**
	 * Metodo equals()
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ConduzioneParticellaVO other = (ConduzioneParticellaVO) obj;
		if (dataAggiornamento == null) {
			if (other.dataAggiornamento != null)
				return false;
		} else if (!dataAggiornamento.equals(other.dataAggiornamento))
			return false;
		if (dataEsecuzione == null) {
			if (other.dataEsecuzione != null)
				return false;
		} else if (!dataEsecuzione.equals(other.dataEsecuzione))
			return false;
		if (dataFineConduzione == null) {
			if (other.dataFineConduzione != null)
				return false;
		} else if (!dataFineConduzione.equals(other.dataFineConduzione))
			return false;
		if (dataInizioConduzione == null) {
			if (other.dataInizioConduzione != null)
				return false;
		} else if (!dataInizioConduzione.equals(other.dataInizioConduzione))
			return false;
		if (!Arrays.equals(elencoDocumentoConduzione,
				other.elencoDocumentoConduzione))
			return false;
		if (!Arrays.equals(elencoUtilizzi, other.elencoUtilizzi))
			return false;
		if (esitoControllo == null) {
			if (other.esitoControllo != null)
				return false;
		} else if (!esitoControllo.equals(other.esitoControllo))
			return false;
		if (flagUtilizzoParte == null) {
			if (other.flagUtilizzoParte != null)
				return false;
		} else if (!flagUtilizzoParte.equals(other.flagUtilizzoParte))
			return false;
		if (idConduzioneParticella == null) {
			if (other.idConduzioneParticella != null)
				return false;
		} else if (!idConduzioneParticella.equals(other.idConduzioneParticella))
			return false;
		if (idParticella == null) {
			if (other.idParticella != null)
				return false;
		} else if (!idParticella.equals(other.idParticella))
			return false;
		if (idTitoloPossesso == null) {
			if (other.idTitoloPossesso != null)
				return false;
		} else if (!idTitoloPossesso.equals(other.idTitoloPossesso))
			return false;
		if (idUte == null) {
			if (other.idUte != null)
				return false;
		} else if (!idUte.equals(other.idUte))
			return false;
		if (idUtenteAggiornamento == null) {
			if (other.idUtenteAggiornamento != null)
				return false;
		} else if (!idUtenteAggiornamento.equals(other.idUtenteAggiornamento))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (recordModificato == null) {
			if (other.recordModificato != null)
				return false;
		} else if (!recordModificato.equals(other.recordModificato))
			return false;
		if (superficieCondotta == null) {
			if (other.superficieCondotta != null)
				return false;
		} else if (!superficieCondotta.equals(other.superficieCondotta))
			return false;
		if (titoloPossesso == null) {
			if (other.titoloPossesso != null)
				return false;
		} else if (!titoloPossesso.equals(other.titoloPossesso))
			return false;
		if (uteVO == null) {
			if (other.uteVO != null)
				return false;
		} else if (!uteVO.equals(other.uteVO))
			return false;
		if (utenteAggiornamento == null) {
			if (other.utenteAggiornamento != null)
				return false;
		} else if (!utenteAggiornamento.equals(other.utenteAggiornamento))
			return false;
		if (superficieAgronomica == null) {
			if (other.superficieAgronomica != null)
				return false;
		} else if (!superficieAgronomica.equals(other.superficieAgronomica))
			return false;
		return true;
	}

		/**
		 * Metodo equals specifico ai dati della conduzione sensibili per le modifiche
		 * del territoriale
		 */
		public boolean equalsDatiConduzione(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ConduzioneParticellaVO other = (ConduzioneParticellaVO) obj;
		  //TITOLO_POSSESSO
			if(Validator.isEmpty(this.idTitoloPossesso)) 
			{
			  if(Validator.isNotEmpty(other.idTitoloPossesso)) 
			  {
					return false;
			  }
			} 
			else if (Validator.isNotEmpty(this.idTitoloPossesso))
			{
			  if(Validator.isEmpty(other.idTitoloPossesso))
			  {
			    return false;
			  }
			  else if(this.idTitoloPossesso.compareTo(other.idTitoloPossesso) !=0)
          return false;
			}
		  // PERCENTUALE_POSSESSO
      if(Validator.isEmpty(this.percentualePossesso)) {
        if(Validator.isNotEmpty(other.percentualePossesso)) {
          return false;
        }
      }
      else if (Validator.isNotEmpty(this.percentualePossesso)) {
        if(Validator.isEmpty(other.percentualePossesso)) 
        {
          return false;
        }
        else if(this.percentualePossesso.compareTo(other.percentualePossesso) !=0)
          return false;
      }
		  // SUPERFICIE_CONDOTTA
      if(Validator.isEmpty(this.superficieCondotta)) {
        if(Validator.isNotEmpty(other.superficieCondotta)) {
          return false;
        }
      }
      else if (Validator.isNotEmpty(this.superficieCondotta))
      {
        if(Validator.isEmpty(other.superficieCondotta)) 
        {
          return false;
        }
        else if(!StringUtils.parseSuperficieField(this.superficieCondotta).equalsIgnoreCase(StringUtils.parseSuperficieField(other.superficieCondotta)))
          return false; 
      }
			// SUPERFICIE_AGRONOMICA
			if(Validator.isEmpty(this.superficieAgronomica)) {
				if(Validator.isNotEmpty(other.superficieAgronomica)) {
					return false;
				}
			} 
			else if(Validator.isNotEmpty(this.superficieAgronomica)) 
			{
				if(Validator.isEmpty(other.superficieAgronomica)) 
				{
					return false;
				}
				else if (!StringUtils.parseSuperficieField(this.superficieAgronomica).equalsIgnoreCase(StringUtils.parseSuperficieField(other.superficieAgronomica)))
	        return false;
			} 
			
			return true;
		}
	
		public ValidationErrors validateModificaTerritorialeCondUso(HttpServletRequest request) 
		{
			ValidationErrors errors = new ValidationErrors();
			// Il titolo di possesso è obbligatorio
			if(!Validator.isNotEmpty(request.getParameter("idTitoloPossesso"))) {
				errors.add("idTitoloPossesso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
			}
			// La superficie condotta è obbligatoria
			if(!Validator.isNotEmpty(request.getParameter("supCondotta"))) 
			{
				errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
			}
			// Se è valorizzata controllo che sia valida
			else 
			{
			  if(Validator.validateDouble(request.getParameter("supCondotta"), 999999.9999) == null) 
			  {
	        errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	      }			  
			}
			return errors;
		}
		
		
		
		
}