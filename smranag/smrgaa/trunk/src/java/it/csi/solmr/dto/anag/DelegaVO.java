package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Monica Di Marco
 * @version 1.0
 */

import java.io.*;
import java.util.*;

import it.csi.solmr.util.*;

public class DelegaVO implements Serializable {
	
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = 4884489618064072607L;

	private Long idDelega = null;
	private Long idIntermediario = null;
	private String denomIntermediario = null;
	private Long idProcedimento = null;
	private Long idAzienda = null;
	private Date dataInizio = null;
	private Date dataFine = null;
	private Long idUtenteInsDelega = null;
	private Long idUtenteFineDelega = null;
	private String codiceAmministrazione;
	private String ufficioFascicolo;
	private String indirizzo;
	private String istaComune;
	private String descComune;
	private String cap;
	private String recapito;
	private String siglaProvincia;
	private String dataOraInizio;
	private String dataOraFine;
	private String nomeUtenIrideIndDelega;
	private String enteUtenIrideIndDelega;
	private String nomeUtenIrideFineDelega;
	private Long idUfficioZonaIntermediario;
	private String codiceFiscaleIntermediario = null; // DB_INTERMEDIARIO.CODICE_FISCALE
	private Date dataInizioMandato = null;
	private Date dataFineMandato = null;
	private Date dataRicevutaRitornoDelega = null;
	private String codiceAgea = null;
	
	private String nomeUtenIrideRicRevoca;
  private String enteUtenIrideRicRevoca;


	private Integer totaleMandati = null; // Attributo usato come contatore totale di record nei riepiloghi per mandati.Non mappa nessuna tabella su DB 
	private Integer totaleMandatiValidati = null; //
	private String descrizioneProvincia = null;
	private String statoRicercaFascicolo = null; // Attributo usato per la ricerca dell'elenco dei fascicoli: non mappa nessuna colonna
	// su DB e non va usato per i servizi
	
	/**
	 * Attributi utilizzati per lo scarico in Excel del report 'Riepilogo mandati e validazioni'
	 */
	private String DenominazioneExcel = null;
	private String IndirizzoExcel = null;
	private String CodiceAgeaExcel = null;

  private String ProvinciaExcel = null;
	private String ComuneExcel = null;
	private Integer totaleMandatiExcel = null;  
	private Integer totaleMandatiValidatiExcel = null; 
	
	private ElencoMandatiValidazioniFiltroExcelVO elencoMandatiValidazioniFiltroEvcelVO = null;
	
	private String emailIntermediario;
	private String pecIntermediario;
	private String mailUfficioZona;
	
	/**
	 * Costruttore di default
	 */
	public DelegaVO() {
		super();
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
	 * @return the codiceAmministrazione
	 */
	public String getCodiceAmministrazione() {
		return codiceAmministrazione;
	}

	/**
	 * @param codiceAmministrazione the codiceAmministrazione to set
	 */
	public void setCodiceAmministrazione(String codiceAmministrazione) {
		this.codiceAmministrazione = codiceAmministrazione;
	}

	/**
	 * @return the codiceFiscaleIntermediario
	 */
	public String getCodiceFiscaleIntermediario() {
		return codiceFiscaleIntermediario;
	}

	/**
	 * @param codiceFiscaleIntermediario the codiceFiscaleIntermediario to set
	 */
	public void setCodiceFiscaleIntermediario(String codiceFiscaleIntermediario) {
		this.codiceFiscaleIntermediario = codiceFiscaleIntermediario;
	}

	/**
	 * @return the dataFine
	 */
	public Date getDataFine() {
		return dataFine;
	}

	/**
	 * @param dataFine the dataFine to set
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * @return the dataFineMandato
	 */
	public Date getDataFineMandato() {
		return dataFineMandato;
	}

	/**
	 * @param dataFineMandato the dataFineMandato to set
	 */
	public void setDataFineMandato(Date dataFineMandato) {
		this.dataFineMandato = dataFineMandato;
	}

	/**
	 * @return the dataInizio
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * @param dataInizio the dataInizio to set
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * @return the dataInizioMandato
	 */
	public Date getDataInizioMandato() {
		return dataInizioMandato;
	}

	/**
	 * @param dataInizioMandato the dataInizioMandato to set
	 */
	public void setDataInizioMandato(Date dataInizioMandato) {
		this.dataInizioMandato = dataInizioMandato;
	}

	/**
	 * @return the dataOraFine
	 */
	public String getDataOraFine() {
		return dataOraFine;
	}

	/**
	 * @param dataOraFine the dataOraFine to set
	 */
	public void setDataOraFine(String dataOraFine) {
		this.dataOraFine = dataOraFine;
	}

	/**
	 * @return the dataOraInizio
	 */
	public String getDataOraInizio() {
		return dataOraInizio;
	}

	/**
	 * @param dataOraInizio the dataOraInizio to set
	 */
	public void setDataOraInizio(String dataOraInizio) {
		this.dataOraInizio = dataOraInizio;
	}

	/**
	 * @return the denomIntermediario
	 */
	public String getDenomIntermediario() {
		return denomIntermediario;
	}

	/**
	 * @param denomIntermediario the denomIntermediario to set
	 */
	public void setDenomIntermediario(String denomIntermediario) {
		this.denomIntermediario = denomIntermediario;
	}

	/**
	 * @return the descComune
	 */
	public String getDescComune() {
		return descComune;
	}

	/**
	 * @param descComune the descComune to set
	 */
	public void setDescComune(String descComune) {
		this.descComune = descComune;
	}

	/**
	 * @return the descrizioneProvincia
	 */
	public String getDescrizioneProvincia() {
		return descrizioneProvincia;
	}

	/**
	 * @param descrizioneProvincia the descrizioneProvincia to set
	 */
	public void setDescrizioneProvincia(String descrizioneProvincia) {
		this.descrizioneProvincia = descrizioneProvincia;
	}

	/**
	 * @return the enteUtenIrideIndDelega
	 */
	public String getEnteUtenIrideIndDelega() {
		return enteUtenIrideIndDelega;
	}

	/**
	 * @param enteUtenIrideIndDelega the enteUtenIrideIndDelega to set
	 */
	public void setEnteUtenIrideIndDelega(String enteUtenIrideIndDelega) {
		this.enteUtenIrideIndDelega = enteUtenIrideIndDelega;
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
	 * @return the idDelega
	 */
	public Long getIdDelega() {
		return idDelega;
	}

	/**
	 * @param idDelega the idDelega to set
	 */
	public void setIdDelega(Long idDelega) {
		this.idDelega = idDelega;
	}

	/**
	 * @return the idIntermediario
	 */
	public Long getIdIntermediario() {
		return idIntermediario;
	}

	/**
	 * @param idIntermediario the idIntermediario to set
	 */
	public void setIdIntermediario(Long idIntermediario) {
		this.idIntermediario = idIntermediario;
	}

	/**
	 * @return the idProcedimento
	 */
	public Long getIdProcedimento() {
		return idProcedimento;
	}

	/**
	 * @param idProcedimento the idProcedimento to set
	 */
	public void setIdProcedimento(Long idProcedimento) {
		this.idProcedimento = idProcedimento;
	}

	/**
	 * @return the idUfficioZonaIntermediario
	 */
	public Long getIdUfficioZonaIntermediario() {
		return idUfficioZonaIntermediario;
	}

	/**
	 * @param idUfficioZonaIntermediario the idUfficioZonaIntermediario to set
	 */
	public void setIdUfficioZonaIntermediario(Long idUfficioZonaIntermediario) {
		this.idUfficioZonaIntermediario = idUfficioZonaIntermediario;
	}

	/**
	 * @return the idUtenteFineDelega
	 */
	public Long getIdUtenteFineDelega() {
		return idUtenteFineDelega;
	}

	/**
	 * @param idUtenteFineDelega the idUtenteFineDelega to set
	 */
	public void setIdUtenteFineDelega(Long idUtenteFineDelega) {
		this.idUtenteFineDelega = idUtenteFineDelega;
	}

	/**
	 * @return the idUtenteInsDelega
	 */
	public Long getIdUtenteInsDelega() {
		return idUtenteInsDelega;
	}

	/**
	 * @param idUtenteInsDelega the idUtenteInsDelega to set
	 */
	public void setIdUtenteInsDelega(Long idUtenteInsDelega) {
		this.idUtenteInsDelega = idUtenteInsDelega;
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
	 * @return the istaComune
	 */
	public String getIstaComune() {
		return istaComune;
	}

	/**
	 * @param istaComune the istaComune to set
	 */
	public void setIstaComune(String istaComune) {
		this.istaComune = istaComune;
	}

	/**
	 * @return the nomeUtenIrideFineDelega
	 */
	public String getNomeUtenIrideFineDelega() {
		return nomeUtenIrideFineDelega;
	}

	/**
	 * @param nomeUtenIrideFineDelega the nomeUtenIrideFineDelega to set
	 */
	public void setNomeUtenIrideFineDelega(String nomeUtenIrideFineDelega) {
		this.nomeUtenIrideFineDelega = nomeUtenIrideFineDelega;
	}

	/**
	 * @return the nomeUtenIrideIndDelega
	 */
	public String getNomeUtenIrideIndDelega() {
		return nomeUtenIrideIndDelega;
	}

	/**
	 * @param nomeUtenIrideIndDelega the nomeUtenIrideIndDelega to set
	 */
	public void setNomeUtenIrideIndDelega(String nomeUtenIrideIndDelega) {
		this.nomeUtenIrideIndDelega = nomeUtenIrideIndDelega;
	}

	/**
	 * @return the recapito
	 */
	public String getRecapito() {
		return recapito;
	}

	/**
	 * @param recapito the recapito to set
	 */
	public void setRecapito(String recapito) {
		this.recapito = recapito;
	}

	/**
	 * @return the siglaProvincia
	 */
	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	/**
	 * @param siglaProvincia the siglaProvincia to set
	 */
	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}	

	/**
	 * @return the totaleMandati
	 */
	public Integer getTotaleMandati() {
		return totaleMandati;
	}

	/**
	 * @param totaleMandati the totaleMandati to set
	 */
	public void setTotaleMandati(Integer totaleMandati) {
		this.totaleMandati = totaleMandati;
	}
	/**
	 * @return the totaleMandatiValidati
	 */
	public Integer getTotaleMandatiValidati() {
		return totaleMandatiValidati;
	}

	/**
	 * @param totaleMandati the totaleMandati to set
	 */
	public void setTotaleMandatiValidati(Integer totaleMandatiValidati) {
		this.totaleMandatiValidati = totaleMandatiValidati;
	}
	/**
	 * @return the ufficioFascicolo
	 */
	public String getUfficioFascicolo() {
		return ufficioFascicolo;
	}

	/**
	 * @param ufficioFascicolo the ufficioFascicolo to set
	 */
	public void setUfficioFascicolo(String ufficioFascicolo) {
		this.ufficioFascicolo = ufficioFascicolo;
	}

	
	/**
	 * Get & Set per lo scarico in Excel del report 'Riepilogo mandati e validazioni'
	 */
	public String getDenominazioneExcel(){
		return this.DenominazioneExcel;
	}
	public void setDenominazioneExcel(String DenominazioneExcel){
		this.DenominazioneExcel = DenominazioneExcel;
	}
	public String getIndirizzoExcel(){
		return this.IndirizzoExcel;
	}
	public void setIndirizzoExcel(String IndirizzoExcel){
		this.IndirizzoExcel = IndirizzoExcel;
	}
	public String getCodiceAgeaExcel(){
		return this.CodiceAgeaExcel;
	}
	public void setCodiceAgeaExcel(String CodiceAgeaExcel){
		this.CodiceAgeaExcel = CodiceAgeaExcel;
	}	
	public String getProvinciaExcel(){
		return this.ProvinciaExcel;
	}
	public void setProvinciaExcel(String ProvinciaExcel){
		this.ProvinciaExcel = ProvinciaExcel;
	}
	public String getComuneExcel(){
		return this.ComuneExcel;
	}
	public void setComuneExcel(String ComuneExcel){
		this.ComuneExcel = ComuneExcel;
	}
	public Integer getTotaleMandatiExcel(){
		return this.totaleMandatiExcel;
	}
	public void setTotaleMandatiExcel(Integer totaleMandatiExcel){
		this.totaleMandatiExcel = totaleMandatiExcel;
	}	
	public Integer getTotaleMandatiValidatiExcel(){
		return this.totaleMandatiValidatiExcel;
	}
	public void setTotaleMandatiValidatiExcel(Integer totaleMandatiValidatiExcel){
		this.totaleMandatiValidatiExcel = totaleMandatiValidatiExcel;
	}	
	public ElencoMandatiValidazioniFiltroExcelVO getElencoMandatiValidazioniFiltroEvcelVO(){
		return this.elencoMandatiValidazioniFiltroEvcelVO;
	}
	public void setElencoMandatiValidazioniFiltroEvcelVO(ElencoMandatiValidazioniFiltroExcelVO elFiltro){
		this.elencoMandatiValidazioniFiltroEvcelVO = elFiltro;
	}
	

	/**
	 * @param statoRicercaFascicolo the statoRicercaFascicolo to set
	 */
	public void setStatoRicercaFascicolo(String statoRicercaFascicolo) {
		this.statoRicercaFascicolo = statoRicercaFascicolo;
	}
	
	/**
	 * @return the statoRicercaFascicolo
	 */
	public String getStatoRicercaFascicolo() {
		return statoRicercaFascicolo;
	}
	
	public ValidationErrors validate() {
		ValidationErrors errors = new ValidationErrors();
		// denomIntermediario è obbligatorio
		if(!Validator.isNotEmpty(ufficioFascicolo))
			errors.add("ufficioFascicolo",new ValidationError("Inserire la denominazione dell''ufficio!"));
		else
			if (ufficioFascicolo.length()>100)
				errors.add("ufficioFascicolo",new ValidationError("La denominazione dell''ufficio non può essere più lunga di 100 caratteri"));
		// L'indirizzo è obbligatorio
		if(!Validator.isNotEmpty(indirizzo))
			errors.add("indirizzo",new ValidationError("Inserire l''indirizzo dell''ufficio!"));
		else
			if (indirizzo.length()>100)
				errors.add("indirizzo",new ValidationError("L''indirizzo dell''ufficio non può essere più lungo di 100 caratteri"));
		//La provincia è obbligatoria
		if(!Validator.isNotEmpty(siglaProvincia))
			errors.add("siglaProvincia",new ValidationError("Inserire la provincia dell''ufficio!"));
		// Il comune è obbligatorio
		if(!Validator.isNotEmpty(descComune))
			errors.add("descComune",new ValidationError("Inserire il comune dell''ufficio!"));
		// Il cap è obbligatorio
		if(!Validator.isNotEmpty(cap))
			errors.add("cap",new ValidationError("Inserire il cap dell''ufficio!"));
		if (Validator.isNotEmpty(this.getRecapito()) && this.getRecapito().length()>100)
			errors.add("recapito",new ValidationError("Il recapito dell''ufficio non può essere più lungo di 100 caratteri"));
		return errors;
	}

  public String getCodiceAgea()
  {
    return codiceAgea;
  }

  public void setCodiceAgea(String codiceAgea)
  {
    this.codiceAgea = codiceAgea;
  }

  public Date getDataRicevutaRitornoDelega()
  {
    return dataRicevutaRitornoDelega;
  }

  public void setDataRicevutaRitornoDelega(Date dataRicevutaRitornoDelega)
  {
    this.dataRicevutaRitornoDelega = dataRicevutaRitornoDelega;
  }
  
  public String getNomeUtenIrideRicRevoca()
  {
    return nomeUtenIrideRicRevoca;
  }

  public void setNomeUtenIrideRicRevoca(String nomeUtenIrideRicRevoca)
  {
    this.nomeUtenIrideRicRevoca = nomeUtenIrideRicRevoca;
  }

  public String getEnteUtenIrideRicRevoca()
  {
    return enteUtenIrideRicRevoca;
  }

  public void setEnteUtenIrideRicRevoca(String enteUtenIrideRicRevoca)
  {
    this.enteUtenIrideRicRevoca = enteUtenIrideRicRevoca;
  }

  public String getEmailIntermediario()
  {
    return emailIntermediario;
  }

  public void setEmailIntermediario(String emailIntermediario)
  {
    this.emailIntermediario = emailIntermediario;
  }

  public String getPecIntermediario()
  {
    return pecIntermediario;
  }

  public void setPecIntermediario(String pecIntermediario)
  {
    this.pecIntermediario = pecIntermediario;
  }

  public String getMailUfficioZona()
  {
    return mailUfficioZona;
  }

  public void setMailUfficioZona(String mailUfficioZona)
  {
    this.mailUfficioZona = mailUfficioZona;
  }
  
  

}
