package it.csi.solmr.dto.anag.terreni;

 // <p>Title: S.O.L.M.R.</p>
 // <p>Description: Servizi On-Line per il Mondo Rurale</p>
 // <p>Copyright: Copyright (c) 2003</p>
 // <p>Company: TOBECONFIG</p>
 // @author
 // @version 1.0

import it.csi.solmr.dto.CodeDescription;

import java.io.Serializable;
import java.math.BigDecimal;

public class TipoUtilizzoVO implements Serializable {

	private static final long serialVersionUID = -373241782429791280L;
	
	private Long idUtilizzo = null;
	private Long idIndirizzoUtilizzo = null;
	private CodeDescription indirizzoUtilizzo = null;
	private String codice = null;
	private String descrizione = null;
	private String tipo = null;
	private String flagSau = null;
	private String flagArboreo = null;
	private String annoInizioValidita = null;
	private String annoFineValidita;
	private String flagSerra;
	private String flagPascolo;
	private String flagAlimentazioneAnimale;
	private String flagForaggera;
	private String flagUbaSostenibile;
	private String flagColturaSecondaria;
	private String flagNidi;
	private String flagPratoPascolo;
	private String codicePsr = null;
	private String flagFruttaGuscio = null;
	private String flagTipoAvvicendamento = null;
	private String flagOrticolo = null;
	private String flagPrincipale = null;
	private String flagAutunnoVernini = null;
	private String flagUsoAgronomico = null;
	private BigDecimal coefficienteRiduzione = null;
	private String flagUnar = null;
	private String codiceSpecie;
	private String descrizioneSpecie;
	private String codiceGenere;
  private String descrizioneGenere;
  private String codiceFamiglia;
  private String descrizioneFamiglia;
  private String tipoDiversificazione;
  private String codiceDiversificazione;
	
	
	/**
	 * Costruttore di default
	 *
	 */
	public TipoUtilizzoVO() {}
	
	
	public BigDecimal getCoefficienteRiduzione()
  {
    return coefficienteRiduzione;
  }

  public void setCoefficienteRiduzione(BigDecimal coefficienteRiduzione)
  {
    this.coefficienteRiduzione = coefficienteRiduzione;
  }

  /**
	 * @return the annoFineValidita
	 */
	public String getAnnoFineValidita() {
		return annoFineValidita;
	}

	/**
	 * @param annoFineValidita the annoFineValidita to set
	 */
	public void setAnnoFineValidita(String annoFineValidita) {
		this.annoFineValidita = annoFineValidita;
	}

	/**
	 * @return the annoInizioValidita
	 */
	public String getAnnoInizioValidita() {
		return annoInizioValidita;
	}

	/**
	 * @param annoInizioValidita the annoInizioValidita to set
	 */
	public void setAnnoInizioValidita(String annoInizioValidita) {
		this.annoInizioValidita = annoInizioValidita;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return the codicePsr
	 */
	public String getCodicePsr() {
		return codicePsr;
	}

	/**
	 * @param codicePsr the codicePsr to set
	 */
	public void setCodicePsr(String codicePsr) {
		this.codicePsr = codicePsr;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the flagAlimentazioneAnimale
	 */
	public String getFlagAlimentazioneAnimale() {
		return flagAlimentazioneAnimale;
	}

	/**
	 * @param flagAlimentazioneAnimale the flagAlimentazioneAnimale to set
	 */
	public void setFlagAlimentazioneAnimale(String flagAlimentazioneAnimale) {
		this.flagAlimentazioneAnimale = flagAlimentazioneAnimale;
	}

	/**
	 * @return the flagArboreo
	 */
	public String getFlagArboreo() {
		return flagArboreo;
	}

	/**
	 * @param flagArboreo the flagArboreo to set
	 */
	public void setFlagArboreo(String flagArboreo) {
		this.flagArboreo = flagArboreo;
	}

	/**
	 * @return the flagAutunnoVernini
	 */
	public String getFlagAutunnoVernini() {
		return flagAutunnoVernini;
	}

	/**
	 * @param flagAutunnoVernini the flagAutunnoVernini to set
	 */
	public void setFlagAutunnoVernini(String flagAutunnoVernini) {
		this.flagAutunnoVernini = flagAutunnoVernini;
	}

	/**
	 * @return the flagColturaSecondaria
	 */
	public String getFlagColturaSecondaria() {
		return flagColturaSecondaria;
	}

	/**
	 * @param flagColturaSecondaria the flagColturaSecondaria to set
	 */
	public void setFlagColturaSecondaria(String flagColturaSecondaria) {
		this.flagColturaSecondaria = flagColturaSecondaria;
	}

	/**
	 * @return the flagForaggera
	 */
	public String getFlagForaggera() {
		return flagForaggera;
	}

	/**
	 * @param flagForaggera the flagForaggera to set
	 */
	public void setFlagForaggera(String flagForaggera) {
		this.flagForaggera = flagForaggera;
	}

	/**
	 * @return the flagFruttaGuscio
	 */
	public String getFlagFruttaGuscio() {
		return flagFruttaGuscio;
	}

	/**
	 * @param flagFruttaGuscio the flagFruttaGuscio to set
	 */
	public void setFlagFruttaGuscio(String flagFruttaGuscio) {
		this.flagFruttaGuscio = flagFruttaGuscio;
	}

	/**
	 * @return the flagNidi
	 */
	public String getFlagNidi() {
		return flagNidi;
	}

	/**
	 * @param flagNidi the flagNidi to set
	 */
	public void setFlagNidi(String flagNidi) {
		this.flagNidi = flagNidi;
	}

	/**
	 * @return the flagOrticolo
	 */
	public String getFlagOrticolo() {
		return flagOrticolo;
	}

	/**
	 * @param flagOrticolo the flagOrticolo to set
	 */
	public void setFlagOrticolo(String flagOrticolo) {
		this.flagOrticolo = flagOrticolo;
	}

	/**
	 * @return the flagPascolo
	 */
	public String getFlagPascolo() {
		return flagPascolo;
	}

	/**
	 * @param flagPascolo the flagPascolo to set
	 */
	public void setFlagPascolo(String flagPascolo) {
		this.flagPascolo = flagPascolo;
	}

	/**
	 * @return the flagPratoPascolo
	 */
	public String getFlagPratoPascolo() {
		return flagPratoPascolo;
	}

	/**
	 * @param flagPratoPascolo the flagPratoPascolo to set
	 */
	public void setFlagPratoPascolo(String flagPratoPascolo) {
		this.flagPratoPascolo = flagPratoPascolo;
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
	 * @return the flagSau
	 */
	public String getFlagSau() {
		return flagSau;
	}

	/**
	 * @param flagSau the flagSau to set
	 */
	public void setFlagSau(String flagSau) {
		this.flagSau = flagSau;
	}

	/**
	 * @return the flagSerra
	 */
	public String getFlagSerra() {
		return flagSerra;
	}

	/**
	 * @param flagSerra the flagSerra to set
	 */
	public void setFlagSerra(String flagSerra) {
		this.flagSerra = flagSerra;
	}

	/**
	 * @return the flagTipoAvvicendamento
	 */
	public String getFlagTipoAvvicendamento() {
		return flagTipoAvvicendamento;
	}

	/**
	 * @param flagTipoAvvicendamento the flagTipoAvvicendamento to set
	 */
	public void setFlagTipoAvvicendamento(String flagTipoAvvicendamento) {
		this.flagTipoAvvicendamento = flagTipoAvvicendamento;
	}

	/**
	 * @return the flagUbaSostenibile
	 */
	public String getFlagUbaSostenibile() {
		return flagUbaSostenibile;
	}

	/**
	 * @param flagUbaSostenibile the flagUbaSostenibile to set
	 */
	public void setFlagUbaSostenibile(String flagUbaSostenibile) {
		this.flagUbaSostenibile = flagUbaSostenibile;
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
	 * @return the indirizzoUtilizzo
	 */
	public CodeDescription getIndirizzoUtilizzo() {
		return indirizzoUtilizzo;
	}

	/**
	 * @param indirizzoUtilizzo the indirizzoUtilizzo to set
	 */
	public void setIndirizzoUtilizzo(CodeDescription indirizzoUtilizzo) {
		this.indirizzoUtilizzo = indirizzoUtilizzo;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the idIndirizzoUtilizzo
	 */
	public Long getIdIndirizzoUtilizzo() {
		return idIndirizzoUtilizzo;
	}

	/**
	 * @param idIndirizzoUtilizzo the idIndirizzoUtilizzo to set
	 */
	public void setIdIndirizzoUtilizzo(Long idIndirizzoUtilizzo) {
		this.idIndirizzoUtilizzo = idIndirizzoUtilizzo;
	}


	//Introdotto nella modifica multipla
  public String getFlagUsoAgronomico()
  {
    return flagUsoAgronomico;
  }

  public void setFlagUsoAgronomico(String flagUsoAgronomico)
  {
    this.flagUsoAgronomico = flagUsoAgronomico;
  }

  public String getFlagUnar()
  {
    return flagUnar;
  }

  public void setFlagUnar(String flagUnar)
  {
    this.flagUnar = flagUnar;
  }

  public String getCodiceSpecie()
  {
    return codiceSpecie;
  }

  public void setCodiceSpecie(String codiceSpecie)
  {
    this.codiceSpecie = codiceSpecie;
  }

  public String getDescrizioneSpecie()
  {
    return descrizioneSpecie;
  }
  
  public void setDescrizioneSpecie(String descrizioneSpecie)
  {
    this.descrizioneSpecie = descrizioneSpecie;
  }
  
  public String getCodiceGenere()
  {
    return codiceGenere;
  }
  
  public void setCodiceGenere(String codiceGenere)
  {
    this.codiceGenere = codiceGenere;
  }
  
  public String getDescrizioneGenere()
  {
    return descrizioneGenere;
  }

  public void setDescrizioneGenere(String descrizioneGenere)
  {
    this.descrizioneGenere = descrizioneGenere;
  }

  public String getCodiceFamiglia()
  {
    return codiceFamiglia;
  }

  public void setCodiceFamiglia(String codiceFamiglia)
  {
    this.codiceFamiglia = codiceFamiglia;
  }

  public String getDescrizioneFamiglia()
  {
    return descrizioneFamiglia;
  }

  public void setDescrizioneFamiglia(String descrizioneFamiglia)
  {
    this.descrizioneFamiglia = descrizioneFamiglia;
  }

  public String getTipoDiversificazione()
  {
    return tipoDiversificazione;
  }

  public void setTipoDiversificazione(String tipoDiversificazione)
  {
    this.tipoDiversificazione = tipoDiversificazione;
  }


  public String getCodiceDiversificazione()
  {
    return codiceDiversificazione;
  }


  public void setCodiceDiversificazione(String codiceDiversificazione)
  {
    this.codiceDiversificazione = codiceDiversificazione;
  }
  
  
}