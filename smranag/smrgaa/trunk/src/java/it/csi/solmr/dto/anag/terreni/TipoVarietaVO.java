package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_VARIETA
 * @author Mauro Vocale
 *
 */
public class TipoVarietaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5374295365848198407L;
	
	private Long idVarieta = null;
	private Long idUtilizzo = null;
	private TipoUtilizzoVO tipoUtilizzo = null;
	private String codiceVarieta = null;
	private String descrizione = null;
	private String annoInizioValidita = null;
	private String annoFineValidita = null;
	private String richiestaRiserva = null;
	private String permanente = null;
	private String flagSiap = null;
	private String flagForaggeraPermanente = null;
	private String flagAvvicendamento = null;
	private Long idTipoPeriodoSemina;
	private Integer abbattimentoPonderazione;

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
	 * @return the tipoUtilizzo
	 */
	public TipoUtilizzoVO getTipoUtilizzo() {
		return tipoUtilizzo;
	}

	/**
	 * @param tipoUtilizzo the tipoUtilizzo to set
	 */
	public void setTipoUtilizzo(TipoUtilizzoVO tipoUtilizzo) {
		this.tipoUtilizzo = tipoUtilizzo;
	}

	/**
	 * @return the codiceVarieta
	 */
	public String getCodiceVarieta() {
		return codiceVarieta;
	}

	/**
	 * @param codiceVarieta the codiceVarieta to set
	 */
	public void setCodiceVarieta(String codiceVarieta) {
		this.codiceVarieta = codiceVarieta;
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
	 * @return the richiestaRiserva
	 */
	public String getRichiestaRiserva() {
		return richiestaRiserva;
	}

	/**
	 * @param richiestaRiserva the richiestaRiserva to set
	 */
	public void setRichiestaRiserva(String richiestaRiserva) {
		this.richiestaRiserva = richiestaRiserva;
	}

	/**
	 * @return the permanente
	 */
	public String getPermanente() {
		return permanente;
	}

	/**
	 * @param permanente the permanente to set
	 */
	public void setPermanente(String permanente) {
		this.permanente = permanente;
	}

	/**
	 * @return the flagSiap
	 */
	public String getFlagSiap() {
		return flagSiap;
	}

	/**
	 * @param flagSiap the flagSiap to set
	 */
	public void setFlagSiap(String flagSiap) {
		this.flagSiap = flagSiap;
	}

	/**
	 * @return the flagForaggeraPermanente
	 */
	public String getFlagForaggeraPermanente() {
		return flagForaggeraPermanente;
	}

	/**
	 * @param flagForaggeraPermanente the flagForaggeraPermanente to set
	 */
	public void setFlagForaggeraPermanente(String flagForaggeraPermanente) {
		this.flagForaggeraPermanente = flagForaggeraPermanente;
	}

	/**
	 * @return the flagAvvicendamento
	 */
	public String getFlagAvvicendamento() {
		return flagAvvicendamento;
	}

	/**
	 * @param flagAvvicendamento the flagAvvicendamento to set
	 */
	public void setFlagAvvicendamento(String flagAvvicendamento) {
		this.flagAvvicendamento = flagAvvicendamento;
	}

  public Long getIdTipoPeriodoSemina()
  {
    return idTipoPeriodoSemina;
  }

  public void setIdTipoPeriodoSemina(Long idTipoPeriodoSemina)
  {
    this.idTipoPeriodoSemina = idTipoPeriodoSemina;
  }

  public Integer getAbbattimentoPonderazione()
  {
    return abbattimentoPonderazione;
  }

  public void setAbbattimentoPonderazione(Integer abbattimentoPonderazione)
  {
    this.abbattimentoPonderazione = abbattimentoPonderazione;
  }

	

	
	
}