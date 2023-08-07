package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.*;
import it.csi.solmr.etc.anag.*;
import it.csi.solmr.util.*;

import java.io.*;
import java.util.*;

public class UteVO implements Serializable {
  
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -5503016782840659843L;

	private String flagPrincipale = null;
	private String idComune = null;
	private String indirizzo = null;
	private String provincia = null;
	private String cap = null;
	private String denominazione = null;
	private String telefono = null;
	private String fax = null;
	private String zonaAltimetrica = null;
	private String ateco = null;
	private String ote = null;
	private String causaleCessazione = null;
	private String ultimaModifica = null;
	private String note = null;
	private Long utmY = null;
	private java.util.Date dataFineAttivita = null;
	private java.util.Date dataInizioAttivita = null;
	private Long idUte = null;
	private Double supTotale = null;
	private Double supUtilizzata = null;
	private Long utmX = null;
	private String comune = null;
	private String istat = null;
	private String statoEstero = null;
	private CodeDescription tipoZonaAltimetrica = null;
	private Long idZonaAltimetrica = null;
	private Long idAzienda = null;
	private CodeDescription tipoAttivitaATECO = null;
	private CodeDescription tipoAttivitaOTE = null;
	private String codeOte;
	private String idOte;
	private String descOte;
	private String idAteco;
	private String codeAteco;
	private String descAteco;
	private String dataFineAttivitaStr;
	private String dataInizioAttivitaStr;
	private String motivoModifica = null;
	private Long utenteAggiornamento = null;
	private ComuneVO comuneUte = null;
	private java.util.Date dataAggiornamento = null;
	private UtenteIrideVO datiUtenteAggiornamento = null;
	private String tipoSede = null;
	
	//Campi ultima modifica spezzata
  private String utenteUltimaModifica = null;
  private String enteUltimaModifica = null;
  
  private Vector<UteAtecoSecondariVO> vUteAtecoSec;
	
	/**
	 * Costruttore di default
	 *
	 */
	public UteVO() {
		super();
		tipoZonaAltimetrica = new CodeDescription();
		tipoAttivitaATECO = new CodeDescription();
		tipoAttivitaOTE = new CodeDescription();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param flagPrincipale
	 * @param idComune
	 * @param indirizzo
	 * @param provincia
	 * @param cap
	 * @param denominazione
	 * @param telefono
	 * @param fax
	 * @param zonaAltimetrica
	 * @param ateco
	 * @param ote
	 * @param causaleCessazione
	 * @param ultimaModifica
	 * @param note
	 * @param utmY
	 * @param dataFineAttivita
	 * @param dataInizioAttivita
	 * @param idUte
	 * @param supTotale
	 * @param supUtilizzata
	 * @param utmX
	 * @param comune
	 * @param istat
	 * @param statoEstero
	 * @param tipoZonaAltimetrica
	 * @param idZonaAltimetrica
	 * @param idAzienda
	 * @param tipoAttivitaATECO
	 * @param tipoAttivitaOTE
	 * @param codeOte
	 * @param idOte
	 * @param descOte
	 * @param idAteco
	 * @param codeAteco
	 * @param descAteco
	 * @param dataFineAttivitaStr
	 * @param dataInizioAttivitaStr
	 * @param motivoModifica
	 * @param utenteAggiornamento
	 * @param comuneUte
	 * @param dataAggiornamento
	 * @param datiUtenteAggiornamento
	 * @param tipoSede
	 */
	public UteVO(String flagPrincipale, String idComune, String indirizzo, String provincia, String cap, String denominazione, String telefono, String fax, String zonaAltimetrica, String ateco, String ote, String causaleCessazione, String ultimaModifica, String note, Long utmY, Date dataFineAttivita, Date dataInizioAttivita, Long idUte, Double supTotale, Double supUtilizzata, Long utmX, String comune, String istat, String statoEstero, CodeDescription tipoZonaAltimetrica, Long idZonaAltimetrica, Long idAzienda, CodeDescription tipoAttivitaATECO, CodeDescription tipoAttivitaOTE, String codeOte, String idOte, String descOte, String idAteco, String codeAteco, String descAteco, String dataFineAttivitaStr, String dataInizioAttivitaStr, String motivoModifica, Long utenteAggiornamento, ComuneVO comuneUte, Date dataAggiornamento, UtenteIrideVO datiUtenteAggiornamento, String tipoSede) {
		super();
		this.flagPrincipale = flagPrincipale;
		this.idComune = idComune;
		this.indirizzo = indirizzo;
		this.provincia = provincia;
		this.cap = cap;
		this.denominazione = denominazione;
		this.telefono = telefono;
		this.fax = fax;
		this.zonaAltimetrica = zonaAltimetrica;
		this.ateco = ateco;
		this.ote = ote;
		this.causaleCessazione = causaleCessazione;
		this.ultimaModifica = ultimaModifica;
		this.note = note;
		this.utmY = utmY;
		this.dataFineAttivita = dataFineAttivita;
		this.dataInizioAttivita = dataInizioAttivita;
		this.idUte = idUte;
		this.supTotale = supTotale;
		this.supUtilizzata = supUtilizzata;
		this.utmX = utmX;
		this.comune = comune;
		this.istat = istat;
		this.statoEstero = statoEstero;
		this.tipoZonaAltimetrica = tipoZonaAltimetrica;
		this.idZonaAltimetrica = idZonaAltimetrica;
		this.idAzienda = idAzienda;
		this.tipoAttivitaATECO = tipoAttivitaATECO;
		this.tipoAttivitaOTE = tipoAttivitaOTE;
		this.codeOte = codeOte;
		this.idOte = idOte;
		this.descOte = descOte;
		this.idAteco = idAteco;
		this.codeAteco = codeAteco;
		this.descAteco = descAteco;
		this.dataFineAttivitaStr = dataFineAttivitaStr;
		this.dataInizioAttivitaStr = dataInizioAttivitaStr;
		this.motivoModifica = motivoModifica;
		this.utenteAggiornamento = utenteAggiornamento;
		this.comuneUte = comuneUte;
		this.dataAggiornamento = dataAggiornamento;
		this.datiUtenteAggiornamento = datiUtenteAggiornamento;
		this.tipoSede = tipoSede;
	}

	/**
	 * @return the ateco
	 */
	public String getAteco() {
		return ateco;
	}

	/**
	 * @param ateco the ateco to set
	 */
	public void setAteco(String ateco) {
		this.ateco = ateco;
	}

	/**
	 * @return the cap
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * @param cap the cap to set
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}

	/**
	 * @return the causaleCessazione
	 */
	public String getCausaleCessazione() {
		return causaleCessazione;
	}

	/**
	 * @param causaleCessazione the causaleCessazione to set
	 */
	public void setCausaleCessazione(String causaleCessazione) {
		this.causaleCessazione = causaleCessazione;
	}

	/**
	 * @return the codeAteco
	 */
	public String getCodeAteco() {
		return codeAteco;
	}

	/**
	 * @param codeAteco the codeAteco to set
	 */
	public void setCodeAteco(String codeAteco) {
		this.codeAteco = codeAteco;
	}

	/**
	 * @return the codeOte
	 */
	public String getCodeOte() {
		return codeOte;
	}

	/**
	 * @param codeOte the codeOte to set
	 */
	public void setCodeOte(String codeOte) {
		this.codeOte = codeOte;
	}

	/**
	 * @return the comune
	 */
	public String getComune() {
		return comune;
	}

	/**
	 * @param comune the comune to set
	 */
	public void setComune(String comune) {
		this.comune = comune;
	}

	/**
	 * @return the comuneUte
	 */
	public ComuneVO getComuneUte() {
		return comuneUte;
	}

	/**
	 * @param comuneUte the comuneUte to set
	 */
	public void setComuneUte(ComuneVO comuneUte) {
		this.comuneUte = comuneUte;
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
	 * @return the dataFineAttivita
	 */
	public java.util.Date getDataFineAttivita() {
		return dataFineAttivita;
	}

	/**
	 * @param dataFineAttivita the dataFineAttivita to set
	 */
	public void setDataFineAttivita(java.util.Date dataFineAttivita) {
		this.dataFineAttivita = dataFineAttivita;
	}

	/**
	 * @return the dataFineAttivitaStr
	 */
	public String getDataFineAttivitaStr() {
		return dataFineAttivitaStr;
	}

	/**
	 * @param dataFineAttivitaStr the dataFineAttivitaStr to set
	 */
	public void setDataFineAttivitaStr(String dataFineAttivitaStr) {
		this.dataFineAttivitaStr = dataFineAttivitaStr;
	}

	/**
	 * @return the dataInizioAttivita
	 */
	public java.util.Date getDataInizioAttivita() {
		return dataInizioAttivita;
	}

	/**
	 * @param dataInizioAttivita the dataInizioAttivita to set
	 */
	public void setDataInizioAttivita(java.util.Date dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}

	/**
	 * @return the dataInizioAttivitaStr
	 */
	public String getDataInizioAttivitaStr() {
		return dataInizioAttivitaStr;
	}

	/**
	 * @param dataInizioAttivitaStr the dataInizioAttivitaStr to set
	 */
	public void setDataInizioAttivitaStr(String dataInizioAttivitaStr) {
		this.dataInizioAttivitaStr = dataInizioAttivitaStr;
	}

	/**
	 * @return the datiUtenteAggiornamento
	 */
	public UtenteIrideVO getDatiUtenteAggiornamento() {
		return datiUtenteAggiornamento;
	}

	/**
	 * @param datiUtenteAggiornamento the datiUtenteAggiornamento to set
	 */
	public void setDatiUtenteAggiornamento(UtenteIrideVO datiUtenteAggiornamento) {
		this.datiUtenteAggiornamento = datiUtenteAggiornamento;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @param denominazione the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @return the descAteco
	 */
	public String getDescAteco() {
		return descAteco;
	}

	/**
	 * @param descAteco the descAteco to set
	 */
	public void setDescAteco(String descAteco) {
		this.descAteco = descAteco;
	}

	/**
	 * @return the descOte
	 */
	public String getDescOte() {
		return descOte;
	}

	/**
	 * @param descOte the descOte to set
	 */
	public void setDescOte(String descOte) {
		this.descOte = descOte;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the flagPrincipale
	 */
	public String getFlagPrincipale() {
		return flagPrincipale;
	}

	/**
	 * @param flagPrincipale the flagPrincipale to set
	 */
	public void setFlagPrincipale(String flagPrincipale) {
		this.flagPrincipale = flagPrincipale;
	}

	/**
	 * @return the idAteco
	 */
	public String getIdAteco() {
		return idAteco;
	}

	/**
	 * @param idAteco the idAteco to set
	 */
	public void setIdAteco(String idAteco) {
		this.idAteco = idAteco;
	}

	/**
	 * @return the idAzienda
	 */
	public Long getIdAzienda() {
		return idAzienda;
	}

	/**
	 * @param idAzienda the idAzienda to set
	 */
	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	/**
	 * @return the idComune
	 */
	public String getIdComune() {
		return idComune;
	}

	/**
	 * @param idComune the idComune to set
	 */
	public void setIdComune(String idComune) {
		this.idComune = idComune;
	}

	/**
	 * @return the idOte
	 */
	public String getIdOte() {
		return idOte;
	}

	/**
	 * @param idOte the idOte to set
	 */
	public void setIdOte(String idOte) {
		this.idOte = idOte;
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
	 * @return the idZonaAltimetrica
	 */
	public Long getIdZonaAltimetrica() {
		return idZonaAltimetrica;
	}

	/**
	 * @param idZonaAltimetrica the idZonaAltimetrica to set
	 */
	public void setIdZonaAltimetrica(Long idZonaAltimetrica) {
		this.idZonaAltimetrica = idZonaAltimetrica;
	}

	/**
	 * @return the indirizzo
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * @param indirizzo the indirizzo to set
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @return the istat
	 */
	public String getIstat() {
		return istat;
	}

	/**
	 * @param istat the istat to set
	 */
	public void setIstat(String istat) {
		this.istat = istat;
	}

	/**
	 * @return the motivoModifica
	 */
	public String getMotivoModifica() {
		return motivoModifica;
	}

	/**
	 * @param motivoModifica the motivoModifica to set
	 */
	public void setMotivoModifica(String motivoModifica) {
		this.motivoModifica = motivoModifica;
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
	 * @return the ote
	 */
	public String getOte() {
		return ote;
	}

	/**
	 * @param ote the ote to set
	 */
	public void setOte(String ote) {
		this.ote = ote;
	}

	/**
	 * @return the provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 * @return the statoEstero
	 */
	public String getStatoEstero() {
		return statoEstero;
	}

	/**
	 * @param statoEstero the statoEstero to set
	 */
	public void setStatoEstero(String statoEstero) {
		this.statoEstero = statoEstero;
	}

	/**
	 * @return the supTotale
	 */
	public Double getSupTotale() {
		return supTotale;
	}

	/**
	 * @param supTotale the supTotale to set
	 */
	public void setSupTotale(Double supTotale) {
		this.supTotale = supTotale;
	}

	/**
	 * @return the supUtilizzata
	 */
	public Double getSupUtilizzata() {
		return supUtilizzata;
	}

	/**
	 * @param supUtilizzata the supUtilizzata to set
	 */
	public void setSupUtilizzata(Double supUtilizzata) {
		this.supUtilizzata = supUtilizzata;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the tipoAttivitaATECO
	 */
	public CodeDescription getTipoAttivitaATECO() {
		return tipoAttivitaATECO;
	}

	/**
	 * @param tipoAttivitaATECO the tipoAttivitaATECO to set
	 */
	public void setTipoAttivitaATECO(CodeDescription tipoAttivitaATECO) {
		this.tipoAttivitaATECO = tipoAttivitaATECO;
	}

	/**
	 * @return the tipoAttivitaOTE
	 */
	public CodeDescription getTipoAttivitaOTE() {
		return tipoAttivitaOTE;
	}

	/**
	 * @param tipoAttivitaOTE the tipoAttivitaOTE to set
	 */
	public void setTipoAttivitaOTE(CodeDescription tipoAttivitaOTE) {
		this.tipoAttivitaOTE = tipoAttivitaOTE;
	}

	/**
	 * @return the tipoSede
	 */
	public String getTipoSede() {
		return tipoSede;
	}

	/**
	 * @param tipoSede the tipoSede to set
	 */
	public void setTipoSede(String tipoSede) {
		this.tipoSede = tipoSede;
	}

	/**
	 * @return the tipoZonaAltimetrica
	 */
	public CodeDescription getTipoZonaAltimetrica() {
		return tipoZonaAltimetrica;
	}

	/**
	 * @param tipoZonaAltimetrica the tipoZonaAltimetrica to set
	 */
	public void setTipoZonaAltimetrica(CodeDescription tipoZonaAltimetrica) {
		this.tipoZonaAltimetrica = tipoZonaAltimetrica;
	}

	/**
	 * @return the ultimaModifica
	 */
	public String getUltimaModifica() {
		return ultimaModifica;
	}

	/**
	 * @param ultimaModifica the ultimaModifica to set
	 */
	public void setUltimaModifica(String ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}

	/**
	 * @return the utenteAggiornamento
	 */
	public Long getUtenteAggiornamento() {
		return utenteAggiornamento;
	}

	/**
	 * @param utenteAggiornamento the utenteAggiornamento to set
	 */
	public void setUtenteAggiornamento(Long utenteAggiornamento) {
		this.utenteAggiornamento = utenteAggiornamento;
	}

	/**
	 * @return the utmX
	 */
	public Long getUtmX() {
		return utmX;
	}

	/**
	 * @param utmX the utmX to set
	 */
	public void setUtmX(Long utmX) {
		this.utmX = utmX;
	}

	/**
	 * @return the utmY
	 */
	public Long getUtmY() {
		return utmY;
	}

	/**
	 * @param utmY the utmY to set
	 */
	public void setUtmY(Long utmY) {
		this.utmY = utmY;
	}

	/**
	 * @return the zonaAltimetrica
	 */
	public String getZonaAltimetrica() {
		return zonaAltimetrica;
	}

	/**
	 * @param zonaAltimetrica the zonaAltimetrica to set
	 */
	public void setZonaAltimetrica(String zonaAltimetrica) {
		this.zonaAltimetrica = zonaAltimetrica;
	}

	

	

		public ValidationErrors validateInsUTE(){
			ValidationErrors errors = new ValidationErrors();
	
			if(!Validator.isNotEmpty(indirizzo)) {
				errors.add("indirizzo",new ValidationError("Inserire l'indirizzo"));
			}
			if(!Validator.isNotEmpty(comune)) {
				errors.add("comune",new ValidationError("Inserire provincia e comune"));
			}
			if(Validator.isEmpty(cap)) 
			{
				errors.add("cap",new ValidationError("Inserire cap"));
			}
			else
			{
				if(!Validator.isCapOk(cap))
					errors.add("cap",new ValidationError("Inserire il cap in formato numerico"));
			}
			if(this.idZonaAltimetrica == null){
				errors.add("idZonaAltimetrica",new ValidationError("Selezionare la zona altimetrica"));
			}
			if(Validator.isNotEmpty(dataInizioAttivitaStr)){
				if(!Validator.validateDateF(this.dataInizioAttivitaStr)) {
					errors.add("dataInizioAttivitaStr",new ValidationError("Inserire la data inizio attività correttamente"));
				}
				else{
					try {
						this.dataInizioAttivita=DateUtils.parseDate(dataInizioAttivitaStr);
					}
					catch (Exception ex) {
					}
					if(dataInizioAttivita.after(new Date(System.currentTimeMillis()))) {
						errors.add("dataInizioAttivitaStr",new ValidationError("La data inizio attività non può essere superiore alla data corrente"));
					}
				}
			}
			else errors.add("dataInizioAttivitaStr",new ValidationError("Inserire la data inizio attività"));
	
			if(Validator.isNotEmpty(note)) {
				if(note.length() > 300) {
					errors.add("note",new ValidationError((String)AnagErrors.get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
				}
			}
	
			return errors;
		}
	
		public ValidationErrors validateModUTE(){
			ValidationErrors errors = new ValidationErrors();
	
			if(!Validator.isNotEmpty(indirizzo)) {
				errors.add("indirizzo",new ValidationError("Inserire l'indirizzo"));
			}
			if(!Validator.isNotEmpty(provincia) && !Validator.isNotEmpty(comune)) {
				errors.add("comune",new ValidationError("Inserire provincia e comune"));
			}
			if(Validator.isEmpty(cap)) 
			{
				errors.add("cap",new ValidationError("Inserire cap"));
			}
			else
			{
				if(!Validator.isCapOk(cap))
					errors.add("cap",new ValidationError("Inserire il cap in formato numerico"));
			}
			if(this.idZonaAltimetrica == null){
				errors.add("idZonaAltimetrica",new ValidationError("Selezionare la zona altimetrica"));
			}
			if(Validator.isNotEmpty(dataInizioAttivitaStr)){
				if(!Validator.validateDateF(this.dataInizioAttivitaStr)) {
					errors.add("dataInizioAttivitaStr",new ValidationError("Inserire la data inizio attività correttamente"));
				}
				else{
					Date oggi=null;
					try {
						dataInizioAttivita = DateUtils.parseDate(dataInizioAttivitaStr);
						oggi = DateUtils.parseDate(DateUtils.getCurrentDateString());
						if(oggi.before(dataInizioAttivita)){
							errors.add("dataInizioAttivitaStr",new ValidationError("La data inizio attività non può essere maggiore della data del giorno"));
						}
					}
					catch (Exception ex) {
						errors.add("dataInizioAttivitaStr",new ValidationError("Errore nel formato della data"));
					}
				}
			}
			else
				errors.add("dataInizioAttivitaStr",new ValidationError("Inserire la data inizio attività"));
	
			if(Validator.isNotEmpty(note)) {
				if(note.length() > 300) {
					errors.add("note",new ValidationError((String)AnagErrors.get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
				}
			}
			if(Validator.isNotEmpty(motivoModifica)) {
				if(motivoModifica.length() > 200) {
					errors.add("motivoModifica",new ValidationError((String)AnagErrors.get("ERR_MOTIVO_MODIFICA_ERRATO")));
				}
			}
			return errors;
		}
		
		/**
		 * Questo metodo mi dice se un UTE puo' essere modificata, questo e' possibile
	  	 * nel caso in cui la data fine attivita' non sia valorizzata o nel caso in cui
	     * la data fine attivita' sia maggiore o uguale alla data del giorno.
	     * E' stato inserito nel VO perche' la modifica dell'UTE avviene su un VO gia'
	     * caricato, quindi si e' ritenuto inutile fare un ulteriore accesso al DB.
		 */
		public boolean isUteModificabile(){
			if(dataFineAttivita==null || dataFineAttivita.toString().equals(""))
				return true;
			else{
				try{
					if(dataFineAttivita.getTime()>=DateUtils.parseDate(DateUtils.getCurrent()).getTime())
						return true;
					else
						return false;
				}
				catch(Exception ex){
					return false;
				}
			}
		}
		
	//Campi ultima modifica spezzata

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

  public Vector<UteAtecoSecondariVO> getvUteAtecoSec()
  {
    return vUteAtecoSec;
  }

  public void setvUteAtecoSec(Vector<UteAtecoSecondariVO> vUteAtecoSec)
  {
    this.vUteAtecoSec = vUteAtecoSec;
  }

	

}
