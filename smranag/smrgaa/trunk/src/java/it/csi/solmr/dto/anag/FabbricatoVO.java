package it.csi.solmr.dto.anag;

import it.csi.smranag.smrgaa.util.ErrorUtils;
import it.csi.solmr.dto.anag.fabbricati.TipoColturaSerraVO;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.dto.anag.services.FabbricatoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;

public class FabbricatoVO implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -4556198466033171624L;

	private Long idFabbricato = null;
	private Long idUte = null;
	private Long idTipologiaFabbricato = null;
	private String descrizioneTipoFabbricato = null;
	private String tipiFormaFabbricato = null;
	private String descrizioneTipoFormaFabbricato = null;
	private String denominazioneFabbricato = null;
	private String superficieFabbricato = null;
	private String superficieCopertaFabbricato = null;
	private String superficieScopertaFabbricato = null;
	private String superficieScopertaExtraFabbricato = null;
	private String annoCostruzioneFabbricato = null;
	private String dimensioneFabbricato = null;
	private String larghezzaFabbricato = null;
	private String lunghezzaFabbricato = null;
	private java.util.Date dataAggiornamento = null;
	private String altezzaFabbricato = null;
	private java.util.Date dataInizioValiditaFabbricato = null;
	private java.util.Date dataFineValiditaFabbricato = null;
	private String noteFabbricato = null;
	private String unitaMisura = null;
	private String strDataInizioValiditaFabbricato = null;
	private String utmx = null;
	private String utmy = null;
	private Long idUnitaProduttivaFabbricato = null;
	private Long idUtilizzoParticellaFabbricato = null;
	private String siglaProvUte = null;
	private String descComuneUte = null;
	private String ultimaModificaFabbricato = null;
	private String strDataFineValiditaFabbricato = null;
	private java.util.Date oldDataInizioValiditaFabbricato = null;
	private String oldAnnoCostruzioneFabbricato = null;
	private FabbricatoParticellaVO[] fabbricatoParticellaVO = null;
	private String tipologiaColturaSerra;
	private double fattoreCubatura;
	private String mesiRiscSerra;
	private String oreRisc;
	private String descrizioneTipologiaColturaSerra;
	private TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = null;
	private Long idFormaFabbricato = null;
	private TipoFormaFabbricatoVO tipoFormaFabbricatoVO = null;
	private Long idUtenteAggiornamento = null;
	private Long idColturaSerra = null;
	private TipoColturaSerraVO tipoColturaSerraVO = null;
	
	//Dimensioni dinamiche e Nuova tipolofia fabbricato
	private String larghezzaFabbricatoLabel = null;
	private String lunghezzaFabbricatoLabel = null;
	private String altezzaFabbricatoLabel = null;
	private String volumeUtilePresuntoFabbricato = null;
	
	//Campi ultima modifica spezzata
  private String utenteUltimaModifica = null;
  private String enteUltimaModifica = null;
  private String motivoModifica = null;
  
  private String biologico = null;


	public FabbricatoVO() 
	{}

	public String getAltezzaFabbricato() {
		return altezzaFabbricato;
	}
	public void setAltezzaFabbricato(String altezzaFabbricato) {
		this.altezzaFabbricato = altezzaFabbricato;
	}
	public String getAnnoCostruzioneFabbricato() {
		return annoCostruzioneFabbricato;
	}
	public void setAnnoCostruzioneFabbricato(String annoCostruzioneFabbricato) {
		this.annoCostruzioneFabbricato = annoCostruzioneFabbricato;
	}
	public java.util.Date getDataInizioValiditaFabbricato() {
		return dataInizioValiditaFabbricato;
	}
	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setDataInizioValiditaFabbricato(java.util.Date dataInizioValiditaFabbricato) {
		this.dataInizioValiditaFabbricato = dataInizioValiditaFabbricato;
	}
	public String getDenominazioneFabbricato() {
		return denominazioneFabbricato;
	}
	public void setDenominazioneFabbricato(String denominazioneFabbricato) {
		this.denominazioneFabbricato = denominazioneFabbricato;
	}
	public String getDimensioneFabbricato() {
		return dimensioneFabbricato;
	}
	public void setDimensioneFabbricato(String dimensioneFabbricato) {
		this.dimensioneFabbricato = dimensioneFabbricato;
	}
	public String getVolumeUtilePresuntoFabbricato() {
    return volumeUtilePresuntoFabbricato;
  }
  public void setVolumeUtilePresuntoFabbricato(String volumeUtilePresuntoFabbricato) {
    this.volumeUtilePresuntoFabbricato = volumeUtilePresuntoFabbricato;
  }
	public Long getIdFabbricato() {
		return idFabbricato;
	}
	public void setIdFabbricato(Long idFabbricato) {
		this.idFabbricato = idFabbricato;
	}
	public Long getIdUte() {
		return idUte;
	}
	public void setIdUte(Long idUte) {
		this.idUte = idUte;
	}
	public String getLarghezzaFabbricato() {
		return larghezzaFabbricato;
	}
	public void setLarghezzaFabbricato(String larghezzaFabbricato) {
		this.larghezzaFabbricato = larghezzaFabbricato;
	}
	public String getLunghezzaFabbricato() {
		return lunghezzaFabbricato;
	}
	public void setLunghezzaFabbricato(String lunghezzaFabbricato) {
		this.lunghezzaFabbricato = lunghezzaFabbricato;
	}
	public String getSuperficieFabbricato() {
		return superficieFabbricato;
	}
	public String getSuperficieCopertaFabbricato() {
    return superficieCopertaFabbricato;
  }
	public String getSuperficieScopertaFabbricato() {
    return superficieScopertaFabbricato;
  }
	public void setNoteFabbricato(String noteFabbricato) {
		this.noteFabbricato = noteFabbricato;
	}
	public String getNoteFabbricato() {
		return noteFabbricato;
	}
	public void setSuperficieFabbricato(String superficieFabbricato) {
		this.superficieFabbricato = superficieFabbricato;
	}
	public void setSuperficieCopertaFabbricato(String superficieCopertaFabbricato) {
    this.superficieCopertaFabbricato = superficieCopertaFabbricato;
  }
	public void setSuperficieScopertaFabbricato(String superficieScopertaFabbricato) {
    this.superficieScopertaFabbricato = superficieScopertaFabbricato;
  }
	public String getTipiFormaFabbricato() {
		return tipiFormaFabbricato;
	}
	public void setTipiFormaFabbricato(String tipiFormaFabbricato) {
		this.tipiFormaFabbricato = tipiFormaFabbricato;
	}
	public Long getIdTipologiaFabbricato() {
		return idTipologiaFabbricato;
	}
	public void setIdTipologiaFabbricato(Long idTipologiaFabbricato) {
		this.idTipologiaFabbricato = idTipologiaFabbricato;
	}
	public String getUnitaMisura() {
		return unitaMisura;
	}
	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}
	public String getStrDataInizioValiditaFabbricato() {
		return strDataInizioValiditaFabbricato;
	}
	public void setStrDataInizioValiditaFabbricato(String strDataInizioValiditaFabbricato) {
		this.strDataInizioValiditaFabbricato = strDataInizioValiditaFabbricato;
	}
	public Long getIdUnitaProduttivaFabbricato() {
		return idUnitaProduttivaFabbricato;
	}
	public void setIdUnitaProduttivaFabbricato(Long idUnitaProduttivaFabbricato) {
		this.idUnitaProduttivaFabbricato = idUnitaProduttivaFabbricato;
	}
	public String getUtmx() {
		return utmx;
	}
	public void setUtmx(String utmx) {
		this.utmx = utmx;
	}
	public void setUtmy(String utmy) {
		this.utmy = utmy;
	}
	public String getUtmy() {
		return utmy;
	}
	public Long getIdUtilizzoParticellaFabbricato() {
		return idUtilizzoParticellaFabbricato;
	}
	public void setIdUtilizzoParticellaFabbricato(Long idUtilizzoParticellaFabbricato) {
		this.idUtilizzoParticellaFabbricato = idUtilizzoParticellaFabbricato;
	}
	public java.util.Date getDataFineValiditaFabbricato() {
		return dataFineValiditaFabbricato;
	}
	public void setDataFineValiditaFabbricato(java.util.Date dataFineValiditaFabbricato) {
		this.dataFineValiditaFabbricato = dataFineValiditaFabbricato;
	}
	public String getDescComuneUte() {
		return descComuneUte;
	}
	public void setDescComuneUte(String descComuneUte) {
		this.descComuneUte = descComuneUte;
	}
	public String getSiglaProvUte() {
		return siglaProvUte;
	}
	public void setSiglaProvUte(String siglaProvUte) {
		this.siglaProvUte = siglaProvUte;
	}
	public String getDescrizioneTipoFabbricato() {
		return descrizioneTipoFabbricato;
	}
	public void setDescrizioneTipoFabbricato(String descrizioneTipoFabbricato) {
		this.descrizioneTipoFabbricato = descrizioneTipoFabbricato;
	}
	public String getDescrizioneTipoFormaFabbricato() {
		return descrizioneTipoFormaFabbricato;
	}
	public void setDescrizioneTipoFormaFabbricato(String descrizioneTipoFormaFabbricato) {
		this.descrizioneTipoFormaFabbricato = descrizioneTipoFormaFabbricato;
	}
	public String getUltimaModificaFabbricato() {
		return ultimaModificaFabbricato;
	}
	public void setUltimaModificaFabbricato(String ultimaModificaFabbricato) {
		this.ultimaModificaFabbricato = ultimaModificaFabbricato;
	}

	public String getStrDataFineValiditaFabbricato() {
		return strDataFineValiditaFabbricato;
	}
	public void setStrDataFineValiditaFabbricato(String strDataFineValiditaFabbricato) {
		this.strDataFineValiditaFabbricato = strDataFineValiditaFabbricato;
	}
	public java.util.Date getOldDataInizioValiditaFabbricato() {
		return oldDataInizioValiditaFabbricato;
	}
	public void setOldDataInizioValiditaFabbricato(java.util.Date oldDataInizioValiditaFabbricato) {
		this.oldDataInizioValiditaFabbricato = oldDataInizioValiditaFabbricato;
	}
	public String getOldAnnoCostruzioneFabbricato() {
		return oldAnnoCostruzioneFabbricato;
	}
	public void setOldAnnoCostruzioneFabbricato(String oldAnnoCostruzioneFabbricato) {
		this.oldAnnoCostruzioneFabbricato = oldAnnoCostruzioneFabbricato;
	}

	public FabbricatoParticellaVO[] getFabbricatoParticellaVO()
	{
		return fabbricatoParticellaVO;
	}
	public void setFabbricatoParticellaVO(FabbricatoParticellaVO[] fabbricatoParticellaVO)
	{
		this.fabbricatoParticellaVO = fabbricatoParticellaVO;
	}

	public String getTipologiaColturaSerra() {
		return tipologiaColturaSerra;
	}
	public void setTipologiaColturaSerra(String tipologiaColturaSerra) {
		this.tipologiaColturaSerra = tipologiaColturaSerra;
	}
	public double getFattoreCubatura() {
		return fattoreCubatura;
	}
	public void setFattoreCubatura(double fattoreCubatura) {
		this.fattoreCubatura = fattoreCubatura;
	}
	public String getMesiRiscSerra() {
		return mesiRiscSerra;
	}
	public void setMesiRiscSerra(String mesiRiscSerra) {
		this.mesiRiscSerra = mesiRiscSerra;
	}
	public String getOreRisc() {
		return oreRisc;
	}
	public void setOreRisc(String oreRisc) {
		this.oreRisc = oreRisc;
	}
	public String getDescrizioneTipologiaColturaSerra() {
		return descrizioneTipologiaColturaSerra;
	}
	public void setDescrizioneTipologiaColturaSerra(String descrizioneTipologiaColturaSerra) {
		this.descrizioneTipologiaColturaSerra = descrizioneTipologiaColturaSerra;
	}

	/**
	 * @return the tipoTipologiaFabbricatoVO
	 */
	 public TipoTipologiaFabbricatoVO getTipoTipologiaFabbricatoVO() {
		return tipoTipologiaFabbricatoVO;
	}

	/**
	 * @param tipoTipologiaFabbricatoVO the tipoTipologiaFabbricatoVO to set
	 */
	 public void setTipoTipologiaFabbricatoVO(
			 TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO) {
		this.tipoTipologiaFabbricatoVO = tipoTipologiaFabbricatoVO;
	}

	/**
	 * @return the idFormaFabbricato
	 */
	public Long getIdFormaFabbricato() {
		return idFormaFabbricato;
	}

	/**
	 * @param idFormaFabbricato the idFormaFabbricato to set
	 */
	public void setIdFormaFabbricato(Long idFormaFabbricato) {
		this.idFormaFabbricato = idFormaFabbricato;
	}

	/**
	 * @return the tipoFormaFabbricatoVO
	 */
	public TipoFormaFabbricatoVO getTipoFormaFabbricatoVO() {
		return tipoFormaFabbricatoVO;
	}

	/**
	 * @param tipoFormaFabbricatoVO the tipoFormaFabbricatoVO to set
	 */
	public void setTipoFormaFabbricatoVO(TipoFormaFabbricatoVO tipoFormaFabbricatoVO) {
		this.tipoFormaFabbricatoVO = tipoFormaFabbricatoVO;
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
	 * @return the idColturaSerra
	 */
	public Long getIdColturaSerra() {
		return idColturaSerra;
	}

	/**
	 * @param idColturaSerra the idColturaSerra to set
	 */
	public void setIdColturaSerra(Long idColturaSerra) {
		this.idColturaSerra = idColturaSerra;
	}

	/**
	 * @return the tipoColturaSerraVO
	 */
	public TipoColturaSerraVO getTipoColturaSerraVO() {
		return tipoColturaSerraVO;
	}

	/**
	 * @param tipoColturaSerraVO the tipoColturaSerraVO to set
	 */
	public void setTipoColturaSerraVO(TipoColturaSerraVO tipoColturaSerraVO) {
		this.tipoColturaSerraVO = tipoColturaSerraVO;
	}

	/*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN*/
	// Metodo per effettuare la validazione dei dati
	public ValidationErrors validateInsFabbricato(int numMesi) 
	{
	  String tipoFormula = SolmrConstants.STRUTTURA_STANDARD;
    if((tipoTipologiaFabbricatoVO !=null) && tipoTipologiaFabbricatoVO.getTipoFormula() !=null)
    {
      tipoFormula = tipoTipologiaFabbricatoVO.getTipoFormula();
    }
    
		ValidationErrors errors = null;

		// Il tipo fabbricato è obbligatorio
		if(!Validator.isNotEmpty(idTipologiaFabbricato)) 
		{
			errors = ErrorUtils.setValidErrNoNull(errors, "tipologiaFabbricato",(String)AnagErrors.get("ERR_TIPOLOGIA_FABBRICATO_OBBLIGATORIA"));
		}
		// Se la larghezza è valorizzata controllo che sia un valore numerico valido
		String messaggioErrore = "";
		if(Validator.isNotEmpty(larghezzaFabbricato)) 
		{
			if(Validator.validateDouble(larghezzaFabbricato, 999.9) == null) 
			{
			  if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { //Larghezza usata come diametro
  				messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO+" ";
  				errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);
        }
			  /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Larghezza usata come potenza
          messaggioErrore += AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO);
        }*/
			  else
			  {
			    messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA")+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",(String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
			  }
			}
			else if(Double.parseDouble(Validator.validateDouble(larghezzaFabbricato, 999.9)) <= 0) 
			{
			  if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { // larghezza usata come diametro
          messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);
        }
			  /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Larghezza usata come potenza
          messaggioErrore += AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO);
        }*/
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA")+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",(String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
        }
			}
			else 
			{
				larghezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(larghezzaFabbricato, 999.9));
			}
		}
		/*else
		{
		  if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
      { //Larghezza usata come potenza
        messaggioErrore += AnagErrors.ERR_POTENZA_FABBRICATO_OBBLIGATORIA+" ";
        errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_POTENZA_FABBRICATO_OBBLIGATORIA);
      }
		}*/
		// Se la lunghezza è valorizzata controllo che sia un valore numerico valido
		if(Validator.isNotEmpty(lunghezzaFabbricato)) 
		{
			if(Validator.validateDouble(lunghezzaFabbricato, 999.9) == null) 
			{
			  if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
			  {
			    //lunghezza usata come altezza
  				messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA+ " ";
  				errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
			  }
			  /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Lunghezza usata come volume
          messaggioErrore += AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO);
        }*/
			  else
			  {
			    messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA")+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",(String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
			  }
			}
			else if(Double.parseDouble(Validator.validateDouble(lunghezzaFabbricato, 999.9)) <= 0) 
			{
			  if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { //lunghezza usata come altezza
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
			  /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Lunghezza usata come volume
          messaggioErrore += AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO);
        }*/
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA")+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",(String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
        }
			}
			else {
				lunghezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(lunghezzaFabbricato, 999.9));
			}
		}
		/*else
    {
      if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
      { //Larghezza usata come potenza
        messaggioErrore += AnagErrors.ERR_VOLUME_FABBRICATO_OBBLIGATORIA+" ";
        errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_VOLUME_FABBRICATO_OBBLIGATORIA);
      }
    }*/
		// Se l'altezza è presente a video è obbligatoria!!!!
		if(Validator.isNotEmpty(tipoTipologiaFabbricatoVO) 
		    && Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getVLabel()) && (tipoTipologiaFabbricatoVO.getVLabel().size() == 3))
		{
		  if(!Validator.isNotEmpty(altezzaFabbricato)) 
	    {
		    messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA;
	      errors = ErrorUtils.setValidErrNoNull(errors, "altezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA);
	    }
		  else
		  {		    
		    if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_PLATEA))
        {
          if(Validator.validateDoubleIncludeZero(altezzaFabbricato, 999.9) == null) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else if(Double.parseDouble(Validator.validateDoubleIncludeZero(altezzaFabbricato, 999.9)) < 0) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else {
            altezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDoubleIncludeZero(altezzaFabbricato, 999.9));
          }
        }
        else
        {
          if(Validator.validateDouble(altezzaFabbricato, 999.9) == null) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else if(Double.parseDouble(Validator.validateDouble(altezzaFabbricato, 999.9)) <= 0) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else {
            altezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(altezzaFabbricato, 999.9));
          }       
        }
		  }
		}
		else
		{
  		if(Validator.isNotEmpty(altezzaFabbricato)) 
  		{
  		  if(Validator.validateDouble(altezzaFabbricato, 999.9) == null) {
  		    messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
          errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
        }
        else if(Double.parseDouble(Validator.validateDouble(altezzaFabbricato, 999.9)) <= 0) {
          messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
          errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
        }
        else 
        {
          altezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(altezzaFabbricato, 999.9));
        }
  		}
		}
		
		
		if(errors !=null) {
			errors.add("calcola",new ValidationError(messaggioErrore));
		}
		
		boolean flagCoperta = false;
    boolean flagScoperta = false;
    boolean flagSuperficie = false;
		// La superficie è obbligatoria
		if(!Validator.isNotEmpty(superficieFabbricato)) 
		{
			errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato", AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
		}
		// Se è valorizzata controllare che sia un valore numerico valido
		else 
		{
			if(Validator.validateDouble(superficieFabbricato, 999999999.9) == null) 
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato", AnagErrors.ERRORE_CAMPO_ERRATO);
			}
			else if(Double.parseDouble(Validator.validateDouble(superficieFabbricato, 999999999.9)) <= 0) 
			{
			  errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato", AnagErrors.ERRORE_CAMPO_ERRATO);
			}
			else 
			{
			  flagSuperficie = true;
				superficieFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(superficieFabbricato, 999999999.9));
			}
		}
    
    if(Validator.isNotEmpty(superficieCopertaFabbricato))
    {
      if(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICIE_COPERTA_ERRATA);
      }
      else if(Double.parseDouble(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICIE_COPERTA_ERRATA);
      }
      else 
      {
        if(flagSuperficie && !"potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
        {
          Double val1 = new Double(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9));
          Double val2 = new Double(Validator.validateDouble(superficieFabbricato, 999999999.9));
          if(val1.doubleValue() > val2.doubleValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICE_COPERTA_MAGGIORE_SUPERFICIE);
          }
        }
        flagCoperta = true;
        superficieCopertaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9));
      }
    }
    else
    {
      if("potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      }
    }
    
    if(Validator.isNotEmpty(superficieScopertaFabbricato))
    {
      if(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICIE_SCOPERTA_ERRATA);
      }
      else if(Double.parseDouble(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICIE_SCOPERTA_ERRATA);
      }
      else 
      {
        if(flagSuperficie)
        {
          Double val1 = new Double(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9));
          Double val2 = new Double(Validator.validateDouble(superficieFabbricato, 999999999.9));
          if(val1.doubleValue() > val2.doubleValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOPERTA_MAGGIORE_SUPERFICIE);
          }
        }
        flagScoperta = true;
        superficieScopertaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9));
      }
    }
    
    if(flagCoperta && flagScoperta && flagSuperficie)
    {
      Double val1 = new Double(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9));
      Double val2 = new Double(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9));
      Double val3 = new Double(Validator.validateDouble(superficieFabbricato, 999999999.9));
      if((val1.doubleValue() + val2.doubleValue()) > val3.doubleValue())
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE);
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE);
      }
    }
    
    boolean flagDimensione = false;
		// La dimensione è obbligatoria
		if(!Validator.isNotEmpty(dimensioneFabbricato)) 
		{
			errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_OBBLIGATORIA"));
		}
		// Se è stata valorizzata controllo che sia un numero valido
		else 
		{
			if(Validator.validateDouble(dimensioneFabbricato, 999999999.9) == null) 
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_ERRATA"));
			}
			else if(Double.parseDouble(Validator.validateDouble(dimensioneFabbricato, 999999999.9)) <= 0) 
			{
			  errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_ERRATA"));
			}
			else 
			{
			  flagDimensione = true;
				dimensioneFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(dimensioneFabbricato, 999999999.9));
			}
		}
		
		//obbligatorio per quelli con flag_stoccaggio = S
    if((tipoTipologiaFabbricatoVO !=null))
    {
      if(Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getFlagStoccaggio())
          && tipoTipologiaFabbricatoVO.getFlagStoccaggio().equalsIgnoreCase("S"))
      {
        if(Validator.isNotEmpty(volumeUtilePresuntoFabbricato))
        {
          if(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9) == null) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_ERRATO);
          }
          else if(Double.parseDouble(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9)) <= 0) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_ERRATO);
          }
          else 
          {
            if(!"potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
            {
              if(flagDimensione)
              {
                Double val1 = new Double(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9));
                Double val2 = new Double(Validator.validateDouble(dimensioneFabbricato, 999999999.9));
                if(val1.doubleValue() > val2.doubleValue())
                {
                  errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_MAGGIORE_DIMENSIONE);
                }
              }
            }
            volumeUtilePresuntoFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9));
          }
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_OBBLIGATORIO);
        }
        if(Validator.isNotEmpty(superficieScopertaExtraFabbricato))
        {
          if(Validator.validateDouble(superficieScopertaExtraFabbricato, 999999999.9) == null) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaExtraFabbricato",AnagErrors.SUP_SCOPERTA_EXTRA_FABBRICATO);
          }
          else if(Double.parseDouble(Validator.validateDouble(superficieScopertaExtraFabbricato, 999999999.9)) < 0) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaExtraFabbricato",AnagErrors.SUP_SCOPERTA_EXTRA_FABBRICATO);
          }
        }
        //Se flag_stoccaggio == 'S' una delle due superifici deve essere valorizzata
        if(Validator.isEmpty(superficieScopertaFabbricato) && Validator.isEmpty(superficieCopertaFabbricato))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato", AnagErrors.SUP_SCOP_COP_OBBLIGATORIA_STOCCAGGIO);
          errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato", AnagErrors.SUP_SCOP_COP_OBBLIGATORIA_STOCCAGGIO);
        }
      }
    }
		
		// Se l'anno di costruzione è valorizzato controllo che sia un numero valido
		if(Validator.isNotEmpty(annoCostruzioneFabbricato)) 
		{
			if(!Validator.isNumericInteger(annoCostruzioneFabbricato)) 
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "annoCostruzioneFabbricato",(String)AnagErrors.get("ERR_ANNO_COSTRUZIONE_FABBRICATO_ERRATO"));
			}
			else 
			{
				int annoCorrente = DateUtils.getCurrentYear().intValue();
				int annoCostruzione = Integer.parseInt(annoCostruzioneFabbricato);
				// L'anno di costruzione fabbricato non può essere posteriore all'anno di sistema
				if(annoCostruzione > annoCorrente) 
				{
					errors = ErrorUtils.setValidErrNoNull(errors, "annoCostruzioneFabbricato",(String)AnagErrors.get("ERR_ANNO_COSTRUZIONE_FABBRICATO_NO_POST_ODIERNO"));
				}
			}
		}
		// La data di inizio utilizzo è obbligatoria
		if(!Validator.isNotEmpty(strDataInizioValiditaFabbricato)) 
		{
			errors = ErrorUtils.setValidErrNoNull(errors, "dataInizioValiditaFabbricato",(String)AnagErrors.get("ERR_DATA_INIZIO_VALIDITA_FABBRICATO_OBBLIGATORIA"));
		}
		// Se è valorizzata controllo che sia valida
		else 
		{
			if(!Validator.validateDateF(strDataInizioValiditaFabbricato)) 
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "dataInizioValiditaFabbricato",(String)AnagErrors.get("ERR_DATA_INIZIO_VALIDITA_FABBRICATO_ERRATA"));
			}
			else 
			{
				// Se è valida controllo che non sia posteriore alla data di sistema e antecedente
				// all'anno di costruzione
				int annoInizioValidita = DateUtils.extractYearFromDate(dataInizioValiditaFabbricato);
				try 
				{
					if(dataInizioValiditaFabbricato.after(DateUtils.parseDate(DateUtils.getCurrent()))) 
					{
						errors = ErrorUtils.setValidErrNoNull(errors, "dataInizioValiditaFabbricato",(String)AnagErrors.get("ERR_DATA_VALIDITA_UTILIZZO_FABBRICATO_NO_POST_ODIERNO"));
					}
					if(Validator.isNotEmpty(annoCostruzioneFabbricato)) 
					{
						int annoCostruzione = Integer.parseInt(annoCostruzioneFabbricato);
						if(annoInizioValidita < annoCostruzione) 
						{
							errors = ErrorUtils.setValidErrNoNull(errors, "dataInizioValiditaFabbricato",(String)AnagErrors.get("ERR_DATA_INIZIO_VALIDITA_FABBRICATO_NO_ANTE_ANNO_COSTRUZIONE"));
						}
					}
				}
				catch(Exception e) {}
			}
		}

		// Se le note sono state valorizzate controllo che non siano più lunghe di 300 caratteri
		if(Validator.isNotEmpty(noteFabbricato)) 
		{
			if(noteFabbricato.length() > Integer.parseInt(((Long)SolmrConstants.get("LUNGHEZZA_MAX_NOTE_FABBRICATO")).toString())) 
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "noteFabbricato",(String)AnagErrors.get("ERR_NOTE_NO_MAX_TRECENTO"));
			}
		}
		
		if (tipiFormaFabbricato!=null)
    {
      if ("".equals(tipiFormaFabbricato))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "tipiFormaFabbricato",AnagErrors.ERR_TIPO_FORMA_FABBRICATO_ERRATA);
      }
    }

		if (tipologiaColturaSerra!=null)
		{
			if ("".equals(tipologiaColturaSerra))
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "tipologiaColturaSerra",AnagErrors.ERR_TIPOLOGIA_COLTURA_SERRA_ERRATA);
			}
		}
		if (oreRisc!=null)
		{
			if ("".equals(oreRisc))
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "oreRisc",AnagErrors.ERR_ORE_RISC_OBBLIGATORIA);
			}
			else
			{
				if(!Validator.isNumericInteger(oreRisc))
				{
					errors = ErrorUtils.setValidErrNoNull(errors, "oreRisc",AnagErrors.ERR_ORE_RISC_ERRATA);
				}
				try
				{
					int oreRiscInt = Integer.parseInt(oreRisc);
					int mesiRiscSerraInt = Integer.parseInt(mesiRiscSerra);
					int limiteOre=mesiRiscSerraInt*SolmrConstants.MAX_NUMERO_GIORNI_MESE*SolmrConstants.MAX_NUMERO_ORE_GIORNO;
					if (oreRiscInt>limiteOre)
					{
						errors = ErrorUtils.setValidErrNoNull(errors, "oreRisc",AnagErrors.ERR_ORE_RISC_ERRATA_SUP_ORE_LIMITE+limiteOre);
					}
				}
				catch(Exception e){}
			}
		}
		if (mesiRiscSerra!=null)
		{
			if ("".equals(mesiRiscSerra))
			{
				errors = ErrorUtils.setValidErrNoNull(errors, "mesiRiscSerra",AnagErrors.ERR_MESI_RISC_SERRA_OBBLIGATORIA);
			}
			else
			{
				if(!Validator.isNumericInteger(mesiRiscSerra))
				{
					errors = ErrorUtils.setValidErrNoNull(errors, "mesiRiscSerra",AnagErrors.ERR_MESI_RISC_SERRA_ERRATA);
				}
				else
				{
					if (Integer.parseInt(mesiRiscSerra)>numMesi)
					{
					  errors = ErrorUtils.setValidErrNoNull(errors, "mesiRiscSerra",AnagErrors.ERR_MESI_RISC_SERRA_ERRATA);
					}
				}
			}
		}
		
		



		return errors;
	}

	// Metodo per effettuare la validazione dei dati sull'ubicazione
	public ValidationErrors validateInsFabbricatoUbicazione(boolean obbligoCoordinate) {
		ValidationErrors errors = new ValidationErrors();

		// Se la coordinata utmx è valorizzata controllo che sia un numero intero
		if(Validator.isNotEmpty(utmx)) 
		{
			if(!Validator.isNumericInteger(utmx)) {
				errors.add("utmx",new ValidationError((String)AnagErrors.get("ERR_UTMX_ERRATA")));
			}
		}
		else
		{
		  if(obbligoCoordinate)
		  {
		    errors.add("utmx",new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		  }
		}
		// Se la coordinata utmy è valorizzata controllo che sia un numero intero
		if(Validator.isNotEmpty(utmy)) 
		{
			if(!Validator.isNumericInteger(utmy)) {
				errors.add("utmy",new ValidationError((String)AnagErrors.get("ERR_UTMY_ERRATA")));
			}
		}
		else
		{
		  if(obbligoCoordinate)
      {
        errors.add("utmy",new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
		}
		// L'ubicazione dell'ute è obbligatoria
		if(!Validator.isNotEmpty(idUnitaProduttivaFabbricato)) {
			errors.add("idUnitaProduttiva",new ValidationError((String)AnagErrors.get("ERR_UNITA_PRODUTTIVA_OBBLIGATORIA")));
		}
		return errors;
	}

	// Metodo per effettuare la validazione dei dati nella modifica del fabbricato
	public ValidationErrors validateModFabbricato(int numMesi) 
	{
	  String tipoFormula = SolmrConstants.STRUTTURA_STANDARD;
    if((tipoTipologiaFabbricatoVO !=null) && tipoTipologiaFabbricatoVO.getTipoFormula() !=null)
    {
      tipoFormula = tipoTipologiaFabbricatoVO.getTipoFormula();
    }
    
    ValidationErrors errors = null;

    // Il tipo fabbricato è obbligatorio
    if(!Validator.isNotEmpty(idTipologiaFabbricato)) 
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "tipologiaFabbricato",(String)AnagErrors.get("ERR_TIPOLOGIA_FABBRICATO_OBBLIGATORIA"));
    }
    // Se la larghezza è valorizzata controllo che sia un valore numerico valido
    String messaggioErrore = "";
    if(Validator.isNotEmpty(larghezzaFabbricato)) 
    {
      if(Validator.validateDouble(larghezzaFabbricato, 999.9) == null) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { //Larghezza usata come diametro
          messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);
        }
        /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Larghezza usata come potenza
          messaggioErrore += AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO);
        }*/
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA")+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",(String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else if(Double.parseDouble(Validator.validateDouble(larghezzaFabbricato, 999.9)) <= 0) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { // larghezza usata come diametro
          messaggioErrore += AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_DIAMETRO_FABBRICATO_ERRATO);
        }
        /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Larghezza usata come potenza
          messaggioErrore += AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_POTENZA_FABBRICATO_ERRATO);
        }*/
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA")+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",(String)AnagErrors.get("ERR_LARGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else 
      {
        larghezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(larghezzaFabbricato, 999.9));
      }
    }
    /*else
    {
      if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
      { //Larghezza usata come potenza
        messaggioErrore += AnagErrors.ERR_POTENZA_FABBRICATO_OBBLIGATORIA+" ";
        errors = ErrorUtils.setValidErrNoNull(errors, "larghezza",AnagErrors.ERR_POTENZA_FABBRICATO_OBBLIGATORIA);
      }
    }*/
    // Se la lunghezza è valorizzata controllo che sia un valore numerico valido
    if(Validator.isNotEmpty(lunghezzaFabbricato)) 
    {
      if(Validator.validateDouble(lunghezzaFabbricato, 999.9) == null) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        {
          //lunghezza usata come altezza
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
        /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Lunghezza usata come volume
          messaggioErrore += AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO);
        }*/
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA")+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",(String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else if(Double.parseDouble(Validator.validateDouble(lunghezzaFabbricato, 999.9)) <= 0) 
      {
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_VASCA_CIRCOLARE))
        { //lunghezza usata come altezza
          messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_ERRATA);
        }
        /*else if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
        { //Lunghezza usata come volume
          messaggioErrore += AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO+" ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_VOLUME_FABBRICATO_ERRATO);
        }*/
        else
        {
          messaggioErrore += (String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA")+ " ";
          errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",(String)AnagErrors.get("ERR_LUNGHEZZA_FABBRICATO_ERRATA"));
        }
      }
      else {
        lunghezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(lunghezzaFabbricato, 999.9));
      }
    }
    /*else
    {
      if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_POTENZA))
      { //Larghezza usata come potenza
        messaggioErrore += AnagErrors.ERR_VOLUME_FABBRICATO_OBBLIGATORIA+" ";
        errors = ErrorUtils.setValidErrNoNull(errors, "lunghezza",AnagErrors.ERR_VOLUME_FABBRICATO_OBBLIGATORIA);
      }
    }*/
    
    // Se l'altezza è presente a video è obbligatoria!!!!
    if(Validator.isNotEmpty(tipoTipologiaFabbricatoVO) 
        && Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getVLabel()) && (tipoTipologiaFabbricatoVO.getVLabel().size() == 3))
    {
      if(!Validator.isNotEmpty(altezzaFabbricato)) 
      {
        messaggioErrore += AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA;
        errors = ErrorUtils.setValidErrNoNull(errors, "altezza",AnagErrors.ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA);
      }
      else
      {       
        if(tipoFormula.equalsIgnoreCase(SolmrConstants.STRUTTURA_PLATEA))
        {
          if(Validator.validateDoubleIncludeZero(altezzaFabbricato, 999.9) == null) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else if(Double.parseDouble(Validator.validateDoubleIncludeZero(altezzaFabbricato, 999.9)) < 0) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else {
            altezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDoubleIncludeZero(altezzaFabbricato, 999.9));
          }
        }
        else
        {
          if(Validator.validateDouble(altezzaFabbricato, 999.9) == null) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else if(Double.parseDouble(Validator.validateDouble(altezzaFabbricato, 999.9)) <= 0) {
            messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
            errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
          }
          else {
            altezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(altezzaFabbricato, 999.9));
          }       
        }
      }
    }
    else
    {
      if(Validator.isNotEmpty(altezzaFabbricato)) 
      {
        if(Validator.validateDouble(altezzaFabbricato, 999.9) == null) {
          messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
          errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
        }
        else if(Double.parseDouble(Validator.validateDouble(altezzaFabbricato, 999.9)) <= 0) {
          messaggioErrore += (String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA");
          errors = ErrorUtils.setValidErrNoNull(errors, "altezza",(String)AnagErrors.get("ERR_ALTEZZA_FABBRICATO_ERRATA"));
        }
        else 
        {
          altezzaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(altezzaFabbricato, 999.9));
        }
      }
    }
    
    if(errors !=null) {
      errors.add("calcola",new ValidationError(messaggioErrore));
    }
    boolean flagCoperta = false;
    boolean flagScoperta = false;
    boolean flagSuperficie = false;
    // La superficie è obbligatoria
    if(!Validator.isNotEmpty(superficieFabbricato)) 
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato",(String)AnagErrors.get("ERR_SUPERFICIE_FABBRICATO_OBBLIGATORIA"));
    }
    // Se è valorizzata controllare che sia un valore numerico valido
    else 
    {
      if(Validator.validateDouble(superficieFabbricato, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato",(String)AnagErrors.get("ERR_SUPERFICIE_FABBRICATO_ERRATA"));
      }
      else if(Double.parseDouble(Validator.validateDouble(superficieFabbricato, 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieFabbricato",(String)AnagErrors.get("ERR_SUPERFICIE_FABBRICATO_ERRATA"));
      }
      else 
      {
        flagSuperficie = true;
        superficieFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(superficieFabbricato, 999999999.9));
      }
    }
    
    if(Validator.isNotEmpty(superficieCopertaFabbricato))
    {
      if(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICIE_COPERTA_ERRATA);
      }
      else if(Double.parseDouble(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICIE_COPERTA_ERRATA);
      }
      else 
      {
        if(flagSuperficie && !"potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
        {
          Double val1 = new Double(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9));
          Double val2 = new Double(Validator.validateDouble(superficieFabbricato, 999999999.9));
          if(val1.doubleValue() > val2.doubleValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICE_COPERTA_MAGGIORE_SUPERFICIE);
          }
        }
        flagCoperta = true;
        superficieCopertaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9));
      }
    }
    else
    {
      if("potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
      }
    }
    
    if(Validator.isNotEmpty(superficieScopertaFabbricato))
    {
      if(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICIE_SCOPERTA_ERRATA);
      }
      else if(Double.parseDouble(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICIE_SCOPERTA_ERRATA);
      }
      else 
      {
        if(flagSuperficie)
        {
          Double val1 = new Double(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9));
          Double val2 = new Double(Validator.validateDouble(superficieFabbricato, 999999999.9));
          if(val1.doubleValue() > val2.doubleValue())
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOPERTA_MAGGIORE_SUPERFICIE);
          }
        }
        flagScoperta = true;
        superficieScopertaFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9));
      }
    }
    
    if(flagCoperta && flagScoperta && flagSuperficie)
    {
      Double val1 = new Double(Validator.validateDouble(superficieCopertaFabbricato, 999999999.9));
      Double val2 = new Double(Validator.validateDouble(superficieScopertaFabbricato, 999999999.9));
      Double val3 = new Double(Validator.validateDouble(superficieFabbricato, 999999999.9));
      if((val1.doubleValue() + val2.doubleValue()) > val3.doubleValue())
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE);
        errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato",AnagErrors.ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE);
      }
    }
    
    
    boolean flagDimensione = false;
    // La dimensione è obbligatoria
    if(!Validator.isNotEmpty(dimensioneFabbricato)) 
    {
      errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_OBBLIGATORIA"));
    }
    // Se è stata valorizzata controllo che sia un numero valido
    else 
    {
      if(Validator.validateDouble(dimensioneFabbricato, 999999999.9) == null) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_ERRATA"));
      }
      else if(Double.parseDouble(Validator.validateDouble(dimensioneFabbricato, 999999999.9)) <= 0) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "dimensioneFabbricato",(String)AnagErrors.get("ERR_DIMENSIONE_FABBRICATO_ERRATA"));
      }
      else 
      {
        flagDimensione = true;
        dimensioneFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(dimensioneFabbricato, 999999999.9));
      }
    }
    
    //obbligatorio per quelli con flag_stoccaggio = S
    if((tipoTipologiaFabbricatoVO !=null))
    {
      if(Validator.isNotEmpty(tipoTipologiaFabbricatoVO.getFlagStoccaggio())
          && tipoTipologiaFabbricatoVO.getFlagStoccaggio().equalsIgnoreCase("S"))
      {
        if(Validator.isNotEmpty(volumeUtilePresuntoFabbricato))
        {
          if(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9) == null) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_ERRATO);
          }
          else if(Double.parseDouble(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9)) <= 0) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_ERRATO);
          }
          else 
          {
            if(!"potenza".equalsIgnoreCase(tipoTipologiaFabbricatoVO.getUnitaMisura()))
            {
              if(flagDimensione)
              {
                Double val1 = new Double(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9));
                Double val2 = new Double(Validator.validateDouble(dimensioneFabbricato, 999999999.9));
                if(val1.doubleValue() > val2.doubleValue())
                {
                  errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_MAGGIORE_DIMENSIONE);
                }
              }
            }
            volumeUtilePresuntoFabbricato = StringUtils.parseDoubleFieldOneDecimal(Validator.validateDouble(volumeUtilePresuntoFabbricato, 999999999.9));
          }
        }
        else
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "volumeUtilePresunto",AnagErrors.ERR_VOLUME_UTILE_PRESUNTO_OBBLIGATORIO);
        }
        if(Validator.isNotEmpty(superficieScopertaExtraFabbricato))
        {
          if(Validator.validateDouble(superficieScopertaExtraFabbricato, 999999999.9) == null) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaExtraFabbricato",AnagErrors.SUP_SCOPERTA_EXTRA_FABBRICATO);
          }
          else if(Double.parseDouble(Validator.validateDouble(superficieScopertaExtraFabbricato, 999999999.9)) < 0) 
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaExtraFabbricato",AnagErrors.SUP_SCOPERTA_EXTRA_FABBRICATO);
          }
        }
        
        //Se flag_stoccaggio == 'S' una delle due superifici deve essere valorizzata
        if(Validator.isEmpty(superficieScopertaFabbricato) && Validator.isEmpty(superficieCopertaFabbricato))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "superficieCopertaFabbricato", AnagErrors.SUP_SCOP_COP_OBBLIGATORIA_STOCCAGGIO);
          errors = ErrorUtils.setValidErrNoNull(errors, "superficieScopertaFabbricato", AnagErrors.SUP_SCOP_COP_OBBLIGATORIA_STOCCAGGIO);
        }
        
      }
    }


    // Se l'anno di costruzione è valorizzato controllo che sia un numero valido
    if(Validator.isNotEmpty(annoCostruzioneFabbricato)) 
    {
      if(!Validator.isNumericInteger(annoCostruzioneFabbricato)) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "annoCostruzioneFabbricato",(String)AnagErrors.get("ERR_ANNO_COSTRUZIONE_FABBRICATO_ERRATO"));
      }
      else 
      {
        int annoCorrente = DateUtils.getCurrentYear().intValue();
        int annoCostruzione = Integer.parseInt(annoCostruzioneFabbricato);
        // L'anno di costruzione fabbricato non può essere posteriore all'anno di sistema
        if(annoCostruzione > annoCorrente) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "annoCostruzioneFabbricato",(String)AnagErrors.get("ERR_ANNO_COSTRUZIONE_FABBRICATO_NO_POST_ODIERNO"));
        }
      }
    }

    // Se le note sono state valorizzate controllo che non siano più lunghe di 300 caratteri
    if(Validator.isNotEmpty(noteFabbricato)) 
    {
      if(noteFabbricato.length() > Integer.parseInt(((Long)SolmrConstants.get("LUNGHEZZA_MAX_NOTE_FABBRICATO")).toString())) 
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "noteFabbricato",(String)AnagErrors.get("ERR_NOTE_NO_MAX_TRECENTO"));
      }
    }
    
    if (tipiFormaFabbricato!=null)
    {
      if ("".equals(tipiFormaFabbricato))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "tipiFormaFabbricato",AnagErrors.ERR_TIPO_FORMA_FABBRICATO_ERRATA);
      }
    }

    if (tipologiaColturaSerra!=null)
    {
      if ("".equals(tipologiaColturaSerra))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "tipologiaColturaSerra",AnagErrors.ERR_TIPOLOGIA_COLTURA_SERRA_ERRATA);
      }
    }
    
    if (oreRisc!=null)
    {
      if ("".equals(oreRisc))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "oreRisc",AnagErrors.ERR_ORE_RISC_OBBLIGATORIA);
      }
      else
      {
        if(!Validator.isNumericInteger(oreRisc))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "oreRisc",AnagErrors.ERR_ORE_RISC_ERRATA);
        }
        try
        {
          int oreRiscInt = Integer.parseInt(oreRisc);
          int mesiRiscSerraInt = Integer.parseInt(mesiRiscSerra);
          int limiteOre=mesiRiscSerraInt*SolmrConstants.MAX_NUMERO_GIORNI_MESE*SolmrConstants.MAX_NUMERO_ORE_GIORNO;
          if (oreRiscInt>limiteOre)
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "oreRisc",AnagErrors.ERR_ORE_RISC_ERRATA_SUP_ORE_LIMITE+limiteOre);
          }
        }
        catch(Exception e){}
      }
    }
    if (mesiRiscSerra!=null)
    {
      if ("".equals(mesiRiscSerra))
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "mesiRiscSerra",AnagErrors.ERR_MESI_RISC_SERRA_OBBLIGATORIA);
      }
      else
      {
        if(!Validator.isNumericInteger(mesiRiscSerra))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "mesiRiscSerra",AnagErrors.ERR_MESI_RISC_SERRA_ERRATA);
        }
        else
        {
          if (Integer.parseInt(mesiRiscSerra)>numMesi)
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "mesiRiscSerra",AnagErrors.ERR_MESI_RISC_SERRA_ERRATA);
          }
        }
      }
    }



		return errors;
	}

	// Metodo per effettuare la validazione dei dati relativi alla modifica dell'ubicazione
	public ValidationErrors validateModFabbricatoUbicazione(boolean obbligoCoordinate) 
	{
		ValidationErrors errors = new ValidationErrors();

		// Se la coordinata utmx è valorizzata controllo che sia un numero intero
		if(Validator.isNotEmpty(utmx)) 
		{
			if(!Validator.isNumericInteger(utmx)) 
			{
				errors.add("utmx",new ValidationError((String)AnagErrors.get("ERR_UTMX_ERRATA")));
			}
		}
		else
    {
      if(obbligoCoordinate)
      {
        errors.add("utmx",new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
    }
		// Se la coordinata utmy è valorizzata controllo che sia un numero intero
		if(Validator.isNotEmpty(utmy)) 
		{
			if(!Validator.isNumericInteger(utmy)) 
			{
				errors.add("utmy",new ValidationError((String)AnagErrors.get("ERR_UTMY_ERRATA")));
			}
		}
		else
    {
      if(obbligoCoordinate)
      {
        errors.add("utmy",new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
    }
		
		return errors;
	}

	/**
	 * Questo metodo restituisce true se il fabbricato passato è diverso da quello
	 * this. Non uso il metodo equals perchè devo solo controllare determinate
	 * proprietà
	 *
	 * @param fo FabbricatoVO
	 * @return boolean
	 */
	public boolean modificaFabbricato(FabbricatoVO fo)
	{
		return
		Validator.confrontaOggetto(getIdTipologiaFabbricato(),fo.getIdTipologiaFabbricato())
		||
		Validator.confrontaOggetto(getTipiFormaFabbricato(),fo.getTipiFormaFabbricato())
		||
		Validator.confrontaOggetto(getDenominazioneFabbricato(),fo.getDenominazioneFabbricato())
		||
		Validator.confrontaOggetto(getLarghezzaFabbricato(),fo.getLarghezzaFabbricato())
		||
		Validator.confrontaOggetto(getLunghezzaFabbricato(),fo.getLunghezzaFabbricato())
		||
		Validator.confrontaOggetto(getAltezzaFabbricato(),fo.getAltezzaFabbricato())
		||
		Validator.confrontaOggetto(getDimensioneFabbricato(),fo.getDimensioneFabbricato())
		||
		Validator.confrontaOggetto(getAnnoCostruzioneFabbricato(),fo.getAnnoCostruzioneFabbricato())
		||
		Validator.confrontaOggetto(getUtmx(),fo.getUtmx())
		||
		Validator.confrontaOggetto(getUtmy(),fo.getUtmy());
	}
	
	//Campi utilizzati per l'ultima modifica spezzata

  public String getUtenteUltimaModifica()
  {
    return utenteUltimaModifica;
  }

  public void setUtenteUltimaModifica(String utenteUltimaModifica)
  {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public String getEnteUltimaModifica()
  {
    return enteUltimaModifica;
  }

  public void setEnteUltimaModifica(String enteUltimaModifica)
  {
    this.enteUltimaModifica = enteUltimaModifica;
  }

  public String getMotivoModifica()
  {
    return motivoModifica;
  }

  public void setMotivoModifica(String motivoModifica)
  {
    this.motivoModifica = motivoModifica;
  }

  public String getLarghezzaFabbricatoLabel()
  {
    return larghezzaFabbricatoLabel;
  }

  public void setLarghezzaFabbricatoLabel(String larghezzaFabbricatoLabel)
  {
    this.larghezzaFabbricatoLabel = larghezzaFabbricatoLabel;
  }

  public String getLunghezzaFabbricatoLabel()
  {
    return lunghezzaFabbricatoLabel;
  }

  public void setLunghezzaFabbricatoLabel(String lunghezzaFabbricatoLabel)
  {
    this.lunghezzaFabbricatoLabel = lunghezzaFabbricatoLabel;
  }

  public String getAltezzaFabbricatoLabel()
  {
    return altezzaFabbricatoLabel;
  }

  public void setAltezzaFabbricatoLabel(String altezzaFabbricatoLabel)
  {
    this.altezzaFabbricatoLabel = altezzaFabbricatoLabel;
  }

  public String getSuperficieScopertaExtraFabbricato()
  {
    return superficieScopertaExtraFabbricato;
  }

  public void setSuperficieScopertaExtraFabbricato(String superficieScopertaExtraFabbricato)
  {
    this.superficieScopertaExtraFabbricato = superficieScopertaExtraFabbricato;
  }

  public String getBiologico()
  {
    return biologico;
  }

  public void setBiologico(String biologico)
  {
    this.biologico = biologico;
  }
  
	
	/*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END*/
}
