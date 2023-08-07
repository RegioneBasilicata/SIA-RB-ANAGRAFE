package it.csi.solmr.dto.anag;

import it.csi.solmr.etc.*;
import it.csi.solmr.util.*;

/**
 * Classe che si occupa di rimappare il VO SianTitoloVO in modo da poter effettuare
 * correttamente lo scarico in excel dei dati da utilizzare
 * 
 * @author Mauro Vocale
 *
 */
public class TitoloExcelVO extends it.csi.solmr.dto.anag.sian.SianTitoloVO  implements java.io.Serializable {
	
	private static final long serialVersionUID = 2555161535022066012L;
	
	private AnagAziendaVO anagAziendaVO = null;
	
	/**
	 * Costruttore di default
	 *
	 */
    public TitoloExcelVO() {
		super();
	}
    
    /**
     * Costruttore con i campi
     * 
     * @param anagAziendaVO
     */
	public TitoloExcelVO(AnagAziendaVO anagAziendaVO) {
		super();
		this.anagAziendaVO = anagAziendaVO;
	}

	/**
	 * @return the anagAziendaVO
	 */
	public AnagAziendaVO getAnagAziendaVO() {
		return anagAziendaVO;
	}

	/**
	 * @param anagAziendaVO the anagAziendaVO to set
	 */
	public void setAnagAziendaVO(AnagAziendaVO anagAziendaVO) {
		this.anagAziendaVO = anagAziendaVO;
	}
	
	/**
	 * Metodo per recuperare la data del movimento
	 * 
	 * @return java.lang.String
	 */
	public String getDataMovimentoExcel() {
		String dataMovimento = "";
		if(Validator.isNotEmpty(this.getDataMovimento())) {
			dataMovimento = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.DATE_AMERICAN_FORMAT, SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT, this.getDataMovimento());
	  		if(dataMovimento.equalsIgnoreCase(SolmrConstants.ORACLE_FINAL_DATE)) {
	  			dataMovimento = "";
	  		}
		}
  		return dataMovimento;
	}
	
	/**
	 * Metodo per recuperare la descrizione del codice validazione
	 * 
	 * @return java.lang.String
	 */
	public String getValidazioneExcel() {
		String descrizioneValidazione = "";
		if(this.getCodiceValidazioneMovimento().equalsIgnoreCase(String.valueOf(SolmrConstants.CODICE_ATTESA_VALIDAZIONE_SIAN))) {
			descrizioneValidazione = SolmrConstants.DESCRIZIONE_ATTESA_VALIDAZIONE_SIAN;
  		}
  		else if(this.getCodiceValidazioneMovimento().equalsIgnoreCase(String.valueOf(SolmrConstants.CODICE_RESPINTO_SIAN))) {
  			descrizioneValidazione = SolmrConstants.DESCRIZIONE_RESPINTO_SIAN;
  		}
  		else if(this.getCodiceValidazioneMovimento().equalsIgnoreCase(String.valueOf(SolmrConstants.CODICE_VALIDATO_SIAN))) {
  			descrizioneValidazione = SolmrConstants.DESCRIZIONE_VALIDATO_SIAN;
  		}
		return descrizioneValidazione;
	}
    
	/**
	 * Metodo per recuperare la data ultimo utilizzo
	 * 
	 * @return java.lang.String
	 */
	public String getDataUltimoUtilExcel() {
		String dataUltimoUtil = "";
		if(Validator.isNotEmpty(this.getDataUltimoUtilizzo())) {
			dataUltimoUtil = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.DATE_AMERICAN_FORMAT, SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT, this.getDataUltimoUtilizzo());
	  		if(dataUltimoUtil.equalsIgnoreCase(SolmrConstants.ORACLE_FINAL_DATE)) {
	  			dataUltimoUtil = "";
	  		}
		}
  		return dataUltimoUtil;
	}
	
	/**
	 * Metodo per recuperare la data fine possesso
	 * 
	 * @return java.lang.String
	 */
	public String getDataFinePossessoExcel() {
		String dataFinePossesso = "";
		if(Validator.isNotEmpty(this.getDataFinePossesso())) {
			dataFinePossesso = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.DATE_AMERICAN_FORMAT, SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT, this.getDataFinePossesso());
	  		if(dataFinePossesso.equalsIgnoreCase(SolmrConstants.ORACLE_FINAL_DATE)) {
	  			dataFinePossesso = "";
	  		}
		}
  		return dataFinePossesso;
	}
	
	/**
	 * Metodo per recuperare la descrizione dello stato
	 * 
	 * @return java.lang.String
	 */
	public String getStatoExcel() {
		String statoExcel = "";
		if(Validator.isNotEmpty(this.getSALtitolo())) {
      		if(this.getSALtitolo().equalsIgnoreCase(String.valueOf(SolmrConstants.SAL_CODICE_TITOLO_DEFINITIVO_SIAN))) {
      			statoExcel = SolmrConstants.SAL_DESCRIZIONE_TITOLO_DEFINITIVO_SIAN;
      		}
      		else if(this.getSALtitolo().equalsIgnoreCase(String.valueOf(SolmrConstants.SAL_CODICE_TITOLO_PROVVISORIO_SIAN))) {
      			statoExcel = SolmrConstants.SAL_DESCRIZIONE_TITOLO_PROVVISORIO_SIAN;
      		}
      	}
		return statoExcel;
	}
	
	/**
	 * Metodo per recuperare l'anno fine campagna
	 * 
	 * @return java.lang.String
	 */
	public String getCampagnaFineValiExcel() {
		String campagnaFine = "";
		if(Validator.isNotEmpty(this.getCampagnaFineVali())) {
	  		if(!this.getCampagnaFineVali().equalsIgnoreCase(SolmrConstants.ORACLE_DEFAULT_YEAR)) {
	  			campagnaFine = this.getCampagnaFineVali();
	  		}
		}
  		return campagnaFine;
	}
}
