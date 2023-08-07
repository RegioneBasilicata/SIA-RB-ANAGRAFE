package it.csi.solmr.dto.anag.terreni;

import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;


/**
 * Classe che si occupa di mappare la tabella DB_UTILIZZO_PARTICELLA
 * @author Mauro Vocale
 *
 */
public class UtilizzoParticellaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3666705429966438084L;
	
	private Long idUtilizzoParticella = null;
	private Long idUtilizzo = null;
	private TipoUtilizzoVO tipoUtilizzoVO = null;
	private Long idConduzioneParticella = null;
	private ConduzioneParticellaVO conduzioneParticellaVO = null;
	private String superficieUtilizzata = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private UtenteIrideVO utenteAggiornamento = null;
	private String anno = null;
	private String note = null;
	private Long idUtilizzoSecondario = null;
	private TipoUtilizzoVO tipoUtilizzoSecondarioVO = null;
	private String supUtilizzataSecondaria = null;
	private Long idVarieta = null;
	private Long idTipoDettaglioUso = null;
	private TipoVarietaVO tipoVarietaVO = null;
	private Long idVarietaSecondaria = null;
	private Long idTipoDettaglioUsoSecondario = null;
	private TipoVarietaVO tipoVarietaSecondariaVO = null;
	private String annoImpianto = null;
	private Long idImpianto = null;
	private TipoImpiantoVO tipoImpiantoVO = null;
	private String sestoSuFile = null;
	private String sestoTraFile = null;
	private String numeroPianteCeppi = null;
	private UtilizzoConsociatoVO[] elencoUtilizziConsociati = null;
	private TipoMacroUsoVO tipoMacroUsoVO = null;
	private BigDecimal supEleggibile = null;
	private BigDecimal supEleggibileNetta = null;
	private BigDecimal supNetta = null;
	private boolean cambiataComboUtilizzo;
	private BigDecimal valoreOriginale;
	private BigDecimal valoreDopoConversione;
	private BigDecimal valoreDopoPonderazione;
	private Long idTipoEfa;
	private String dichiarabileEfa;
	private String descTipoEfaEfa;
	private String descUnitaMisuraEfa;
	private TipoDettaglioUsoVO tipoDettaglioUso;
	private TipoDettaglioUsoVO tipoDettaglioUsoSecondario;
	private Long idTipoPeriodoSemina;
	private Long idTipoPeriodoSeminaSecondario;
	private TipoPeriodoSeminaVO tipoPeriodoSemina;
  private TipoPeriodoSeminaVO tipoPeriodoSeminaSecondario;
  private String padreEfa;
  private Long idCatalogoMatrice;
  private Long idCatalogoMatriceSecondario;
  private Long idSemina;
  private TipoSeminaVO tipoSemina;
  private Long idSeminaSecondario;
  private TipoSeminaVO tipoSeminaSecondario;
  private Long idPraticaMantenimento;
  private TipoPraticaMantenimentoVO tipoPraticaMantenimento;
  private Date dataInizioDestinazione;
  private Date dataFineDestinazione;
  private Date dataInizioDestinazioneSec;
  private Date dataFineDestinazioneSec;
  private String dataInizioDestinazioneStr;
  private String dataFineDestinazioneStr;
  private String dataInizioDestinazioneSecStr;
  private String dataFineDestinazioneSecStr;
  private Long idTipoDestinazione;
  private TipoDestinazioneVO tipoDestinazione;
  private Long idTipoDestinazioneSecondario;
  private TipoDestinazioneVO tipoDestinazioneSecondario;
  private Long idTipoQualitaUso;
  private TipoQualitaUsoVO tipoQualitaUso;
  private Long idTipoQualitaUsoSecondario;
  private TipoQualitaUsoVO tipoQualitaUsoSecondario;
  private Long idFaseAllevamento;
  private TipoFaseAllevamentoVO tipoFaseAllevamento;
  private Integer abbaPonderazione;
  private BigDecimal coefficienteRiduzione;
  private String flagMatriceAttiva;
  private String flagMatriceAttivaSecondario;
  
	
	//private Vector<ParticellaEfaVO> vParticellaEfa = null;
	
	/**
	 * Costruttore
	 *
	 */
	public UtilizzoParticellaVO() {
		super();
	}
	
	
	
	
	
	
	
	public BigDecimal getSupNetta()
  {
    return supNetta;
  }







  public void setSupNetta(BigDecimal supNetta)
  {
    this.supNetta = supNetta;
  }







  public boolean isCambiataComboUtilizzo()
  {
    return cambiataComboUtilizzo;
  }





  public void setCambiataComboUtilizzo(boolean cambiataComboUtilizzo)
  {
    this.cambiataComboUtilizzo = cambiataComboUtilizzo;
  }





    public BigDecimal getSupEleggibile()
  {
    return supEleggibile;
  }





  public void setSupEleggibile(BigDecimal supEleggibile)
  {
    this.supEleggibile = supEleggibile;
  }





  public BigDecimal getSupEleggibileNetta()
  {
    return supEleggibileNetta;
  }





  public void setSupEleggibileNetta(BigDecimal supEleggibileNetta)
  {
    this.supEleggibileNetta = supEleggibileNetta;
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
	 * @return the idUtilizzo
	 */
	public Long getIdUtilizzo() {
		return idUtilizzo;
	}

	/**
	 * @param idUtilizzo the idUtilizzo to set
	 */
	public void setIdUtilizzo(Long idUtilizzo) {
		this.idUtilizzo = idUtilizzo;
	}

	/**
	 * @return the tipoUtilizzoVO
	 */
	public TipoUtilizzoVO getTipoUtilizzoVO() {
		return tipoUtilizzoVO;
	}

	/**
	 * @param tipoUtilizzoVO the tipoUtilizzoVO to set
	 */
	public void setTipoUtilizzoVO(TipoUtilizzoVO tipoUtilizzoVO) {
		this.tipoUtilizzoVO = tipoUtilizzoVO;
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
	 * @return the conduzioneParticellaVO
	 */
	public ConduzioneParticellaVO getConduzioneParticellaVO() {
		return conduzioneParticellaVO;
	}

	/**
	 * @param conduzioneParticellaVO the conduzioneParticellaVO to set
	 */
	public void setConduzioneParticellaVO(
			ConduzioneParticellaVO conduzioneParticellaVO) {
		this.conduzioneParticellaVO = conduzioneParticellaVO;
	}

	/**
	 * @return the superficieUtilizzata
	 */
	public String getSuperficieUtilizzata() {
		return superficieUtilizzata;
	}

	/**
	 * @param superficieUtilizzata the superficieUtilizzata to set
	 */
	public void setSuperficieUtilizzata(String superficieUtilizzata) {
		this.superficieUtilizzata = superficieUtilizzata;
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
	 * @return the anno
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(String anno) {
		this.anno = anno;
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
	 * @return the idUtilizzoSecondario
	 */
	public Long getIdUtilizzoSecondario() {
		return idUtilizzoSecondario;
	}

	/**
	 * @param idUtilizzoSecondario the idUtilizzoSecondario to set
	 */
	public void setIdUtilizzoSecondario(Long idUtilizzoSecondario) {
		this.idUtilizzoSecondario = idUtilizzoSecondario;
	}

	/**
	 * @return the tipoUtilizzoSecondarioVO
	 */
	public TipoUtilizzoVO getTipoUtilizzoSecondarioVO() {
		return tipoUtilizzoSecondarioVO;
	}

	/**
	 * @param tipoUtilizzoSecondarioVO the tipoUtilizzoSecondarioVO to set
	 */
	public void setTipoUtilizzoSecondarioVO(TipoUtilizzoVO tipoUtilizzoSecondarioVO) {
		this.tipoUtilizzoSecondarioVO = tipoUtilizzoSecondarioVO;
	}

	/**
	 * @return the supUtilizzataSecondaria
	 */
	public String getSupUtilizzataSecondaria() {
		return supUtilizzataSecondaria;
	}

	/**
	 * @param supUtilizzataSecondaria the supUtilizzataSecondaria to set
	 */
	public void setSupUtilizzataSecondaria(String supUtilizzataSecondaria) {
		this.supUtilizzataSecondaria = supUtilizzataSecondaria;
	}

	/**
	 * @return the idVarieta
	 */
	public Long getIdVarieta() {
		return idVarieta;
	}

	/**
	 * @param idVarieta the idVarieta to set
	 */
	public void setIdVarieta(Long idVarieta) {
		this.idVarieta = idVarieta;
	}

	/**
	 * @return the tipoVarietaVO
	 */
	public TipoVarietaVO getTipoVarietaVO() {
		return tipoVarietaVO;
	}

	/**
	 * @param tipoVarietaVO the tipoVarietaVO to set
	 */
	public void setTipoVarietaVO(TipoVarietaVO tipoVarietaVO) {
		this.tipoVarietaVO = tipoVarietaVO;
	}

	/**
	 * @return the idVarietaSecondaria
	 */
	public Long getIdVarietaSecondaria() {
		return idVarietaSecondaria;
	}

	/**
	 * @param idVarietaSecondaria the idVarietaSecondaria to set
	 */
	public void setIdVarietaSecondaria(Long idVarietaSecondaria) {
		this.idVarietaSecondaria = idVarietaSecondaria;
	}

	/**
	 * @return the tipoVarietaSecondariaVO
	 */
	public TipoVarietaVO getTipoVarietaSecondariaVO() {
		return tipoVarietaSecondariaVO;
	}

	/**
	 * @param tipoVarietaSecondariaVO the tipoVarietaSecondariaVO to set
	 */
	public void setTipoVarietaSecondariaVO(TipoVarietaVO tipoVarietaSecondariaVO) {
		this.tipoVarietaSecondariaVO = tipoVarietaSecondariaVO;
	}

	/**
	 * @return the annoImpianto
	 */
	public String getAnnoImpianto() {
		return annoImpianto;
	}

	/**
	 * @param annoImpianto the annoImpianto to set
	 */
	public void setAnnoImpianto(String annoImpianto) {
		this.annoImpianto = annoImpianto;
	}

	/**
	 * @return the idImpianto
	 */
	public Long getIdImpianto() {
		return idImpianto;
	}

	/**
	 * @param idImpianto the idImpianto to set
	 */
	public void setIdImpianto(Long idImpianto) {
		this.idImpianto = idImpianto;
	}

	/**
	 * @return the tipoImpiantoVO
	 */
	public TipoImpiantoVO getTipoImpiantoVO() {
		return tipoImpiantoVO;
	}

	/**
	 * @param tipoImpiantoVO the tipoImpiantoVO to set
	 */
	public void setTipoImpiantoVO(TipoImpiantoVO tipoImpiantoVO) {
		this.tipoImpiantoVO = tipoImpiantoVO;
	}

	/**
	 * @return the sestoSuFile
	 */
	public String getSestoSuFile() {
		return sestoSuFile;
	}

	/**
	 * @param sestoSuFile the sestoSuFile to set
	 */
	public void setSestoSuFile(String sestoSuFile) {
		this.sestoSuFile = sestoSuFile;
	}

	/**
	 * @return the sestoTraFile
	 */
	public String getSestoTraFile() {
		return sestoTraFile;
	}

	/**
	 * @param sestoTraFile the sestoTraFile to set
	 */
	public void setSestoTraFile(String sestoTraFile) {
		this.sestoTraFile = sestoTraFile;
	}

	/**
	 * @return the numeroPianteCeppi
	 */
	public String getNumeroPianteCeppi() {
		return numeroPianteCeppi;
	}

	/**
	 * @param numeroPianteCeppi the numeroPianteCeppi to set
	 */
	public void setNumeroPianteCeppi(String numeroPianteCeppi) {
		this.numeroPianteCeppi = numeroPianteCeppi;
	}

	/**
	 * @return the elencoUtilizziConsociati
	 */
	public UtilizzoConsociatoVO[] getElencoUtilizziConsociati() {
		return elencoUtilizziConsociati;
	}

	/**
	 * @param elencoUtilizziConsociati the elencoUtilizziConsociati to set
	 */
	public void setElencoUtilizziConsociati(
			UtilizzoConsociatoVO[] elencoUtilizziConsociati) {
		this.elencoUtilizziConsociati = elencoUtilizziConsociati;
	}

	/**
	 * @return the tipoMacroUsoVO
	 */
	public TipoMacroUsoVO getTipoMacroUsoVO() {
		return tipoMacroUsoVO;
	}

  /**
	 * @param tipoMacroUsoVO the tipoMacroUsoVO to set
	 */
	public void setTipoMacroUsoVO(TipoMacroUsoVO tipoMacroUsoVO) {
		this.tipoMacroUsoVO = tipoMacroUsoVO;
	}

	public ValidationErrors validateModificaTerritorialeCondUso(HttpServletRequest request, ValidationErrors errors, String flagFruttaGuscio, int indice) 
	{
		if(errors == null) 
		{
			errors = new ValidationErrors();
		}
		if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")) 
		    && indice < request.getParameterValues("idTipoUtilizzo").length) 
		{
			// Il tipo utilizzo è obbligatorio
			if(!Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[indice])) 
			{
				errors.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
			}
			
			if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina"))
			   && !Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")[indice])) 
      {
        errors.add("idTipoPeriodoSemina", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
			
			boolean flagDataIn = false;
			if(!Validator.validateDateF(request.getParameterValues("dataInizioDestinazione")[indice])) 
      {
        errors.add("dataInizioDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
			else
			{
			  flagDataIn = true;
			}
			
			boolean flagDataEnd = false;
			if(!Validator.validateDateF(request.getParameterValues("dataFineDestinazione")[indice])) 
      {
        errors.add("dataFineDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
			else
      {
			  flagDataEnd = true;
      }
			
			if(flagDataIn && flagDataEnd)
			{
			  try
			  {
  			  if(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazione")[indice])
  			      .after(DateUtils.parseDate(request.getParameterValues("dataFineDestinazione")[indice])))
  	      {
  			    errors.add("dataInizioDestinazione", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
  			    errors.add("dataFineDestinazione", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));	    
  	      }
			  }
			  catch(Exception ex)
			  {}
			}
			
			// La superficie utilizzata è obbligatoria
			boolean isSupOk = false;
			if(!Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[indice])) 
			{
				errors.add("supUtilizzata", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
			} 
			// Se lo è controllo che sia valida e maggiore di 0
			else if(Validator.validateDouble(request.getParameterValues("supUtilizzata")[indice], 999999.9999) == null) 
			{
				errors.add("supUtilizzata", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
			}
			else 
			{
				isSupOk = true;
			}
			if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[indice])) 
			{
			  
			  if(!Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[indice])) 
        {
          errors.add("idTipoPeriodoSeminaSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
        }
			  
			  flagDataIn = false;
	      if(!Validator.validateDateF(request.getParameterValues("dataInizioDestinazioneSec")[indice])) 
	      {
	        errors.add("dataInizioDestinazioneSec", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	      }
	      else
	      {
	        flagDataIn = true;
	      }
	      
	      flagDataEnd = false;
	      if(!Validator.validateDateF(request.getParameterValues("dataFineDestinazioneSec")[indice])) 
	      {
	        errors.add("dataFineDestinazioneSec", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	      }
	      else
	      {
	        flagDataEnd = true;
	      }
	      
	      if(flagDataIn && flagDataEnd)
	      {
	        try
	        {
	          if(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazioneSec")[indice])
	              .after(DateUtils.parseDate(request.getParameterValues("dataFineDestinazioneSec")[indice])))
	          {
	            errors.add("dataInizioDestinazioneSec", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
	            errors.add("dataFineDestinazioneSec", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));      
	          }
	        }
	        catch(Exception ex)
	        {}
	      }
			  
				// La superficie utilizzata secondaria è obbligatoria
				if(!Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[indice])) 
				{
					errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
				}	 
				// Se lo è controllo che sia valida e maggiore di 0
				else if(Validator.validateDouble(request.getParameterValues("supUtilizzataSecondaria")[indice], 999999.9999) == null) 
				{
					errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				}
				else 
				{
					if(isSupOk) 
					{
						if(Double.parseDouble(request.getParameterValues("supUtilizzataSecondaria")[indice].replace(',', '.')) > Double.parseDouble(request.getParameterValues("supUtilizzata")[indice].replace(',', '.'))) 
						{
							errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_KO_SUP_UTILIZZATA_SECONDARIA_MAGGIORE_SUP_UTILIZZATA));
						}	
					}
				}
			}
			// Altrimenti
			else 
			{
				// La superficie utilizzata secondaria non può essere valorizzata
				if(Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[indice])) 
				{
					errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE));
				}
				/*if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[indice])) 
        {
          errors.add("idTipoPeriodoSeminaSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE));
        }*/
			}
			
			if(Validator.isEmpty(request.getParameterValues("annoImpianto")[indice]) 
			  &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
			  errors.add("annoImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
			
			if(Validator.isEmpty(request.getParameterValues("idImpianto")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("idImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
			
			if(Validator.isEmpty(request.getParameterValues("numeroPianteCeppi")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("numeroPianteCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
			
			if(Validator.isEmpty(request.getParameterValues("sestoSuFile")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("sestoSuFile", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
			
			if(Validator.isEmpty(request.getParameterValues("sestoTraFile")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
			
			
			
		  // Se è stato inserito anche solo un valore relativo ai dati dell'impianto
      // diventano tutti obbligatori e devono avere un valore corretto
			if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[indice]) 
			    || Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[indice])) 
			{
				if(!Validator.isNotEmpty(request.getParameterValues("annoImpianto")[indice])) 
				{
					errors.add("annoImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
				}
				// Se è valorizzato controllo che sia valido
				else 
				{
					if(!Validator.isNumericInteger(request.getParameterValues("annoImpianto")[indice]) 
					    || Integer.parseInt(request.getParameterValues("annoImpianto")[indice]) < 1900 
					    || Integer.parseInt(request.getParameterValues("annoImpianto")[indice]) > DateUtils.getCurrentYear().intValue()) 
					{
						errors.add("annoImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
					}
				}				 
				if(!Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[indice])) 
				{
					errors.add("numeroPianteCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
				}
				// Se è valorizzato controllo che sia valido
				else 
				{
					if(!Validator.isNumericInteger(request.getParameterValues("numeroPianteCeppi")[indice]) || Integer.parseInt(request.getParameterValues("numeroPianteCeppi")[indice]) == 0) {
						errors.add("numeroPianteCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
					}
				}
			}
			// Se è stato indicato il valore controllo che sia valido
			if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[indice])) 
			{
				if(!Validator.isNumericInteger(request.getParameterValues("sestoSuFile")[indice]) 
				    || Integer.parseInt(request.getParameterValues("sestoSuFile")[indice]) == 0) 
				{
					errors.add("sestoSuFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				}	
			}
			// Se è stato indicato il valore controllo che sia valido
			if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[indice])) 
			{
				if(!Validator.isNumericInteger(request.getParameterValues("sestoTraFile")[indice]) 
				    || Integer.parseInt(request.getParameterValues("sestoTraFile")[indice]) == 0) 
				{
					errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				}
			}	
		}
		return errors;
	}
	
	
	public ValidationErrors validateModificaTerritorialeCondUsoEfa(HttpServletRequest request, ValidationErrors errors, int indice) 
  {
    if(errors == null) 
    {
      errors = new ValidationErrors();
    }
    if(request.getParameterValues("idTipoEfa") != null 
        && indice < request.getParameterValues("idTipoEfa").length) 
    {
      // Il tipo utilizzo è obbligatorio
      if(Validator.isEmpty(request.getParameterValues("idTipoEfa")[indice])) 
      {
        errors.add("idTipoEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      /*if(Validator.isEmpty(request.getParameterValues("idTipoUtilizzoEfa")[indice])) 
      {
        errors.add("idTipoUtilizzoEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(Validator.isEmpty(request.getParameterValues("idVarietaEfa")[indice])) 
      {
        errors.add("idVarietaEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }*/
      
      if(Validator.isEmpty(request.getParameterValues("valoreOriginale")[indice])) 
      {
        errors.add("valoreOriginale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      else
      {
        if(Validator.validateDouble(request.getParameterValues("valoreOriginale")[indice], 9999999999.9999) == null) 
        {
          errors.add("valoreOriginale", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
        }        
      }
      
    }
    return errors;
  }
	
	
	
	/*public ValidationErrors validateInsrisciTerritorialeCondUso(HttpServletRequest request, ValidationErrors errors, String flagFruttaGuscio, int indice) 
  {
    if(errors == null) 
    {
      errors = new ValidationErrors();
    }
    if(request.getParameterValues("idTipoUtilizzo") != null 
        && indice < request.getParameterValues("idTipoUtilizzo").length) 
    {
      // Il tipo utilizzo è obbligatorio
      if(!Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[indice])) 
      {
        errors.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(!Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")[indice])) 
      {
        errors.add("idTipoPeriodoSemina", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      // La superficie utilizzata è obbligatoria
      boolean isSupOk = false;
      if(!Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[indice])) 
      {
        errors.add("supUtilizzata", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      } 
      // Se lo è controllo che sia valida e maggiore di 0
      else if(Validator.validateDouble(request.getParameterValues("supUtilizzata")[indice], 999999.9999) == null) 
      {
        errors.add("supUtilizzata", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
      else 
      {
        isSupOk = true;
      }
      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[indice])) 
      {
        
        if(!Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[indice])) 
        {
          errors.add("idTipoPeriodoSeminaSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
        }
        
        // La superficie utilizzata secondaria è obbligatoria
        if(!Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[indice])) 
        {
          errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
        }  
        // Se lo è controllo che sia valida e maggiore di 0
        else if(Validator.validateDouble(request.getParameterValues("supUtilizzataSecondaria")[indice], 999999.9999) == null) 
        {
          errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
        }
        else 
        {
          if(isSupOk) 
          {
            if(Double.parseDouble(request.getParameterValues("supUtilizzataSecondaria")[indice].replace(',', '.')) > Double.parseDouble(request.getParameterValues("supUtilizzata")[indice].replace(',', '.'))) 
            {
              errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_KO_SUP_UTILIZZATA_SECONDARIA_MAGGIORE_SUP_UTILIZZATA));
            } 
          }
        }
      }
      // Altrimenti
      else 
      {
        // La superficie utilizzata secondaria non può essere valorizzata
        if(Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[indice])) 
        {
          errors.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE));
        }
      }
      
      if(Validator.isEmpty(request.getParameterValues("annoImpianto")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("annoImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(Validator.isEmpty(request.getParameterValues("idImpianto")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("idImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(Validator.isEmpty(request.getParameterValues("numeroPianteCeppi")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("numeroPianteCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(Validator.isEmpty(request.getParameterValues("sestoSuFile")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("sestoSuFile", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(Validator.isEmpty(request.getParameterValues("sestoTraFile")[indice]) 
        &&  "S".equalsIgnoreCase(flagFruttaGuscio))
      {
        errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      
      
      // Se è stato inserito anche solo un valore relativo ai dati dell'impianto
      // diventano tutti obbligatori e devono avere un valore corretto
      if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[indice]) 
          || Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[indice])) 
      {
        if(!Validator.isNotEmpty(request.getParameterValues("annoImpianto")[indice])) 
        {
          errors.add("annoImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
        }
        // Se è valorizzato controllo che sia valido
        else 
        {
          if(!Validator.isNumericInteger(request.getParameterValues("annoImpianto")[indice]) 
              || Integer.parseInt(request.getParameterValues("annoImpianto")[indice]) < 1900 
              || Integer.parseInt(request.getParameterValues("annoImpianto")[indice]) > DateUtils.getCurrentYear().intValue()) 
          {
            errors.add("annoImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
          }
        }        
        if(!Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[indice])) 
        {
          errors.add("numeroPianteCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
        }
        // Se è valorizzato controllo che sia valido
        else 
        {
          if(!Validator.isNumericInteger(request.getParameterValues("numeroPianteCeppi")[indice]) || Integer.parseInt(request.getParameterValues("numeroPianteCeppi")[indice]) == 0) {
            errors.add("numeroPianteCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
          }
        }
      }
      // Se è stato indicato il valore controllo che sia valido
      if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[indice])) 
      {
        if(!Validator.isNumericInteger(request.getParameterValues("sestoSuFile")[indice]) 
            || Integer.parseInt(request.getParameterValues("sestoSuFile")[indice]) == 0) 
        {
          errors.add("sestoSuFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
        } 
      }
      // Se è stato indicato il valore controllo che sia valido
      if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[indice])) 
      {
        if(!Validator.isNumericInteger(request.getParameterValues("sestoTraFile")[indice]) 
            || Integer.parseInt(request.getParameterValues("sestoTraFile")[indice]) == 0) 
        {
          errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
        }
      } 
    }
    return errors;
  }*/
	
	public ValidationErrors validateInserisciTerritorialeCondUsoEfa(HttpServletRequest request, ValidationErrors errors, int indice) 
  {
    if(errors == null) 
    {
      errors = new ValidationErrors();
    }
    if(request.getParameterValues("idTipoEfa") != null 
        && indice < request.getParameterValues("idTipoEfa").length) 
    {
      // Il tipo utilizzo è obbligatorio
      // sono polati in cascate impossibile che ce ne sia uno dopo vuoto...
      if(Validator.isEmpty(request.getParameterValues("idTipoEfa")[indice])) 
      {
        errors.add("idTipoEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      /*if(Validator.isEmpty(request.getParameterValues("idTipoUtilizzoEfa")[indice])) 
      {
        errors.add("idTipoUtilizzoEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      
      if(Validator.isEmpty(request.getParameterValues("idVarietaEfa")[indice])) 
      {
        errors.add("idVarietaEfa", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }*/
      
      if(Validator.isEmpty(request.getParameterValues("valoreOriginale")[indice])) 
      {
        errors.add("valoreOriginale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
      else
      {
        if(Validator.validateDouble(request.getParameterValues("valoreOriginale")[indice], 9999999999.9999) == null) 
        {
          errors.add("valoreOriginale", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
        }        
      }
      
    }
    return errors;
  }
	
	

  public Long getIdTipoDettaglioUso()
  {
    return idTipoDettaglioUso;
  }

  public void setIdTipoDettaglioUso(Long idTipoDettaglioUso)
  {
    this.idTipoDettaglioUso = idTipoDettaglioUso;
  }

  public Long getIdTipoDettaglioUsoSecondario()
  {
    return idTipoDettaglioUsoSecondario;
  }

  public void setIdTipoDettaglioUsoSecondario(Long idTipoDettaglioUsoSecondario)
  {
    this.idTipoDettaglioUsoSecondario = idTipoDettaglioUsoSecondario;
  }
  
  public BigDecimal getValoreOriginale()
  {
    return valoreOriginale;
  }
  
  public void setValoreOriginale(BigDecimal valoreOriginale)
  {
    this.valoreOriginale = valoreOriginale;
  }
  
  public BigDecimal getValoreDopoConversione()
  {
    return valoreDopoConversione;
  }
  
  public void setValoreDopoConversione(BigDecimal valoreDopoConversione)
  {
    this.valoreDopoConversione = valoreDopoConversione;
  }
  
  public BigDecimal getValoreDopoPonderazione()
  {
    return valoreDopoPonderazione;
  }

  public void setValoreDopoPonderazione(BigDecimal valoreDopoPonderazione)
  {
    this.valoreDopoPonderazione = valoreDopoPonderazione;
  }

  public Long getIdTipoEfa()
  {
    return idTipoEfa;
  }

  public void setIdTipoEfa(Long idTipoEfa)
  {
    this.idTipoEfa = idTipoEfa;
  }

  public TipoDettaglioUsoVO getTipoDettaglioUso()
  {
    return tipoDettaglioUso;
  }

  public void setTipoDettaglioUso(TipoDettaglioUsoVO tipoDettaglioUso)
  {
    this.tipoDettaglioUso = tipoDettaglioUso;
  }

  public TipoDettaglioUsoVO getTipoDettaglioUsoSecondario()
  {
    return tipoDettaglioUsoSecondario;
  }
  
  public void setTipoDettaglioUsoSecondario(
      TipoDettaglioUsoVO tipoDettaglioUsoSecondario)
  {
    this.tipoDettaglioUsoSecondario = tipoDettaglioUsoSecondario;
  }

  public Long getIdTipoPeriodoSemina()
  {
    return idTipoPeriodoSemina;
  }
  
  public void setIdTipoPeriodoSemina(Long idTipoPeriodoSemina)
  {
    this.idTipoPeriodoSemina = idTipoPeriodoSemina;
  }

  public Long getIdTipoPeriodoSeminaSecondario()
  {
    return idTipoPeriodoSeminaSecondario;
  }
  
  public void setIdTipoPeriodoSeminaSecondario(Long idTipoPeriodoSeminaSecondario)
  {
    this.idTipoPeriodoSeminaSecondario = idTipoPeriodoSeminaSecondario;
  }

  public String getDichiarabileEfa()
  {
    return dichiarabileEfa;
  }

  public void setDichiarabileEfa(String dichiarabileEfa)
  {
    this.dichiarabileEfa = dichiarabileEfa;
  }

  public TipoPeriodoSeminaVO getTipoPeriodoSemina()
  {
    return tipoPeriodoSemina;
  }

  public void setTipoPeriodoSemina(TipoPeriodoSeminaVO tipoPeriodoSemina)
  {
    this.tipoPeriodoSemina = tipoPeriodoSemina;
  }
  
  public TipoPeriodoSeminaVO getTipoPeriodoSeminaSecondario()
  {
    return tipoPeriodoSeminaSecondario;
  }

  public void setTipoPeriodoSeminaSecondario(
      TipoPeriodoSeminaVO tipoPeriodoSeminaSecondario)
  {
    this.tipoPeriodoSeminaSecondario = tipoPeriodoSeminaSecondario;
  }

  public String getDescTipoEfaEfa()
  {
    return descTipoEfaEfa;
  }
  
  public void setDescTipoEfaEfa(String descTipoEfaEfa)
  {
    this.descTipoEfaEfa = descTipoEfaEfa;
  }

  public String getDescUnitaMisuraEfa()
  {
    return descUnitaMisuraEfa;
  }

  public void setDescUnitaMisuraEfa(String descUnitaMisuraEfa)
  {
    this.descUnitaMisuraEfa = descUnitaMisuraEfa;
  }

  public String getPadreEfa()
  {
    return padreEfa;
  }

  public void setPadreEfa(String padreEfa)
  {
    this.padreEfa = padreEfa;
  }
  
  public Long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }

  public void setIdCatalogoMatrice(Long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }

  public Long getIdCatalogoMatriceSecondario()
  {
    return idCatalogoMatriceSecondario;
  }

  public void setIdCatalogoMatriceSecondario(Long idCatalogoMatriceSecondario)
  {
    this.idCatalogoMatriceSecondario = idCatalogoMatriceSecondario;
  }

  public Long getIdSemina()
  {
    return idSemina;
  }

  public void setIdSemina(Long idSemina)
  {
    this.idSemina = idSemina;
  }

  public Long getIdSeminaSecondario()
  {
    return idSeminaSecondario;
  }

  public void setIdSeminaSecondario(Long idSeminaSecondario)
  {
    this.idSeminaSecondario = idSeminaSecondario;
  }

  public Long getIdPraticaMantenimento()
  {
    return idPraticaMantenimento;
  }

  public void setIdPraticaMantenimento(Long idPraticaMantenimento)
  {
    this.idPraticaMantenimento = idPraticaMantenimento;
  }

  public Date getDataInizioDestinazione()
  {
    return dataInizioDestinazione;
  }

  public void setDataInizioDestinazione(Date dataInizioDestinazione)
  {
    this.dataInizioDestinazione = dataInizioDestinazione;
  }

  public Date getDataFineDestinazione()
  {
    return dataFineDestinazione;
  }

  public void setDataFineDestinazione(Date dataFineDestinazione)
  {
    this.dataFineDestinazione = dataFineDestinazione;
  }

  public Date getDataInizioDestinazioneSec()
  {
    return dataInizioDestinazioneSec;
  }

  public void setDataInizioDestinazioneSec(Date dataInizioDestinazioneSec)
  {
    this.dataInizioDestinazioneSec = dataInizioDestinazioneSec;
  }

  public Date getDataFineDestinazioneSec()
  {
    return dataFineDestinazioneSec;
  }

  public void setDataFineDestinazioneSec(Date dataFineDestinazioneSec)
  {
    this.dataFineDestinazioneSec = dataFineDestinazioneSec;
  }

  public Long getIdTipoDestinazione()
  {
    return idTipoDestinazione;
  }

  public void setIdTipoDestinazione(Long idTipoDestinazione)
  {
    this.idTipoDestinazione = idTipoDestinazione;
  }

  public TipoDestinazioneVO getTipoDestinazione()
  {
    return tipoDestinazione;
  }

  public void setTipoDestinazione(TipoDestinazioneVO tipoDestinazione)
  {
    this.tipoDestinazione = tipoDestinazione;
  }

  public Long getIdTipoDestinazioneSecondario()
  {
    return idTipoDestinazioneSecondario;
  }

  public void setIdTipoDestinazioneSecondario(Long idTipoDestinazioneSecondario)
  {
    this.idTipoDestinazioneSecondario = idTipoDestinazioneSecondario;
  }

  public TipoDestinazioneVO getTipoDestinazioneSecondario()
  {
    return tipoDestinazioneSecondario;
  }
  
  public void setTipoDestinazioneSecondario(
      TipoDestinazioneVO tipoDestinazioneSecondario)
  {
    this.tipoDestinazioneSecondario = tipoDestinazioneSecondario;
  }

  public Long getIdTipoQualitaUso()
  {
    return idTipoQualitaUso;
  }

  public void setIdTipoQualitaUso(Long idTipoQualitaUso)
  {
    this.idTipoQualitaUso = idTipoQualitaUso;
  }

  public TipoQualitaUsoVO getTipoQualitaUso()
  {
    return tipoQualitaUso;
  }

  public void setTipoQualitaUso(TipoQualitaUsoVO tipoQualitaUso)
  {
    this.tipoQualitaUso = tipoQualitaUso;
  }

  public Long getIdTipoQualitaUsoSecondario()
  {
    return idTipoQualitaUsoSecondario;
  }

  public void setIdTipoQualitaUsoSecondario(Long idTipoQualitaUsoSecondario)
  {
    this.idTipoQualitaUsoSecondario = idTipoQualitaUsoSecondario;
  }

  public TipoQualitaUsoVO getTipoQualitaUsoSecondario()
  {
    return tipoQualitaUsoSecondario;
  }

  public void setTipoQualitaUsoSecondario(
      TipoQualitaUsoVO tipoQualitaUsoSecondario)
  {
    this.tipoQualitaUsoSecondario = tipoQualitaUsoSecondario;
  }

  public Long getIdFaseAllevamento()
  {
    return idFaseAllevamento;
  }

  public void setIdFaseAllevamento(Long idFaseAllevamento)
  {
    this.idFaseAllevamento = idFaseAllevamento;
  }

  public TipoPraticaMantenimentoVO getTipoPraticaMantenimento()
  {
    return tipoPraticaMantenimento;
  }

  public void setTipoPraticaMantenimento(
      TipoPraticaMantenimentoVO tipoPraticaMantenimento)
  {
    this.tipoPraticaMantenimento = tipoPraticaMantenimento;
  }

  public TipoFaseAllevamentoVO getTipoFaseAllevamento()
  {
    return tipoFaseAllevamento;
  }

  public void setTipoFaseAllevamento(TipoFaseAllevamentoVO tipoFaseAllevamento)
  {
    this.tipoFaseAllevamento = tipoFaseAllevamento;
  }

  public TipoSeminaVO getTipoSemina()
  {
    return tipoSemina;
  }

  public void setTipoSemina(TipoSeminaVO tipoSemina)
  {
    this.tipoSemina = tipoSemina;
  }

  public TipoSeminaVO getTipoSeminaSecondario()
  {
    return tipoSeminaSecondario;
  }

  public void setTipoSeminaSecondario(TipoSeminaVO tipoSeminaSecondario)
  {
    this.tipoSeminaSecondario = tipoSeminaSecondario;
  }

  public Integer getAbbaPonderazione()
  {
    return abbaPonderazione;
  }

  public void setAbbaPonderazione(Integer abbaPonderazione)
  {
    this.abbaPonderazione = abbaPonderazione;
  }

  public BigDecimal getCoefficienteRiduzione()
  {
    return coefficienteRiduzione;
  }

  public void setCoefficienteRiduzione(BigDecimal coefficienteRiduzione)
  {
    this.coefficienteRiduzione = coefficienteRiduzione;
  }

  public String getDataInizioDestinazioneStr()
  {
    return dataInizioDestinazioneStr;
  }
  
  public void setDataInizioDestinazioneStr(String dataInizioDestinazioneStr)
  {
    this.dataInizioDestinazioneStr = dataInizioDestinazioneStr;
  }

  public String getDataFineDestinazioneStr()
  {
    return dataFineDestinazioneStr;
  }

  public void setDataFineDestinazioneStr(String dataFineDestinazioneStr)
  {
    this.dataFineDestinazioneStr = dataFineDestinazioneStr;
  }
  
  public String getDataInizioDestinazioneSecStr()
  {
    return dataInizioDestinazioneSecStr;
  }
  
  public void setDataInizioDestinazioneSecStr(String dataInizioDestinazioneSecStr)
  {
    this.dataInizioDestinazioneSecStr = dataInizioDestinazioneSecStr;
  }

  public String getDataFineDestinazioneSecStr()
  {
    return dataFineDestinazioneSecStr;
  }

  public void setDataFineDestinazioneSecStr(String dataFineDestinazioneSecStr)
  {
    this.dataFineDestinazioneSecStr = dataFineDestinazioneSecStr;
  }

  public String getFlagMatriceAttiva()
  {
    return flagMatriceAttiva;
  }

  public void setFlagMatriceAttiva(String flagMatriceAttiva)
  {
    this.flagMatriceAttiva = flagMatriceAttiva;
  }

  public String getFlagMatriceAttivaSecondario()
  {
    return flagMatriceAttivaSecondario;
  }

  public void setFlagMatriceAttivaSecondario(String flagMatriceAttivaSecondario)
  {
    this.flagMatriceAttivaSecondario = flagMatriceAttivaSecondario;
  }  
  
		
}