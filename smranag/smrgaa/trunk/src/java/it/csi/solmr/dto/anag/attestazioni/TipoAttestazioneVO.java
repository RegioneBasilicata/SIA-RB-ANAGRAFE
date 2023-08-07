package it.csi.solmr.dto.anag.attestazioni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_ATTESTAZIONE
 * 
 * @author Mauro Vocale
 *
 */
public class TipoAttestazioneVO implements Serializable {

	private static final long serialVersionUID = 3793984475578742738L;
	
	private Long idAttestazione = null;
	private String codiceAttestazione = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private String flagVideo = null;
	private String flagStampa = null;
	private String tipoRiga = null;
	private String numeroColonneRiga = null;
	private Long idAttestazionePadre = null;
	private String tipoCarattere = null;
	private String gruppo = null;
	private String ordinamento = null;
	private String obbligatorio = null;
	private String disabilitato = null;
	private String selezionaVideo = null;
	private boolean isAttestazioneAzienda = false;
	private String attestazioniCorrelate = null;
	private String voceMenu = null;
	private ParametriAttAziendaVO parametriAttAziendaVO = null;
	private ParametriAttDichiarataVO parametriAttDichiarataVO = null;
	private TipoParametriAttestazioneVO tipoParametriAttestazioneVO = null;
	

	/**
	 * @return the idAttestazione
	 */
	public Long getIdAttestazione() {
		return idAttestazione;
	}

	/**
	 * @param idAttestazione the idAttestazione to set
	 */
	public void setIdAttestazione(Long idAttestazione) {
		this.idAttestazione = idAttestazione;
	}

	/**
	 * @return the codiceAttestazione
	 */
	public String getCodiceAttestazione() {
		return codiceAttestazione;
	}

	/**
	 * @param codiceAttestazione the codiceAttestazione to set
	 */
	public void setCodiceAttestazione(String codiceAttestazione) {
		this.codiceAttestazione = codiceAttestazione;
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
	 * @return the dataInizioValidita
	 */
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the dataFineValidita
	 */
	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the flagVideo
	 */
	public String getFlagVideo() {
		return flagVideo;
	}

	/**
	 * @param flagVideo the flagVideo to set
	 */
	public void setFlagVideo(String flagVideo) {
		this.flagVideo = flagVideo;
	}

	/**
	 * @return the flagStampa
	 */
	public String getFlagStampa() {
		return flagStampa;
	}

	/**
	 * @param flagStampa the flagStampa to set
	 */
	public void setFlagStampa(String flagStampa) {
		this.flagStampa = flagStampa;
	}

	/**
	 * @return the tipoRiga
	 */
	public String getTipoRiga() {
		return tipoRiga;
	}

	/**
	 * @param tipoRiga the tipoRiga to set
	 */
	public void setTipoRiga(String tipoRiga) {
		this.tipoRiga = tipoRiga;
	}

	/**
	 * @return the numeroColonneRiga
	 */
	public String getNumeroColonneRiga() {
		return numeroColonneRiga;
	}

	/**
	 * @param numeroColonneRiga the numeroColonneRiga to set
	 */
	public void setNumeroColonneRiga(String numeroColonneRiga) {
		this.numeroColonneRiga = numeroColonneRiga;
	}

	/**
	 * @return the idAttestazionePadre
	 */
	public Long getIdAttestazionePadre() {
		return idAttestazionePadre;
	}

	/**
	 * @param idAttestazionePadre the idAttestazionePadre to set
	 */
	public void setIdAttestazionePadre(Long idAttestazionePadre) {
		this.idAttestazionePadre = idAttestazionePadre;
	}

	/**
	 * @return the tipoCarattere
	 */
	public String getTipoCarattere() {
		return tipoCarattere;
	}

	/**
	 * @param tipoCarattere the tipoCarattere to set
	 */
	public void setTipoCarattere(String tipoCarattere) {
		this.tipoCarattere = tipoCarattere;
	}

	/**
	 * @return the gruppo
	 */
	public String getGruppo() {
		return gruppo;
	}

	/**
	 * @param gruppo the gruppo to set
	 */
	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}

	/**
	 * @return the ordinamento
	 */
	public String getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @param ordinamento the ordinamento to set
	 */
	public void setOrdinamento(String ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * @return the obbligatorio
	 */
	public String getObbligatorio() {
		return obbligatorio;
	}

	/**
	 * @param obbligatorio the obbligatorio to set
	 */
	public void setObbligatorio(String obbligatorio) {
		this.obbligatorio = obbligatorio;
	}

	/**
	 * @return the disabilitato
	 */
	public String getDisabilitato() {
		return disabilitato;
	}

	/**
	 * @param disabilitato the disabilitato to set
	 */
	public void setDisabilitato(String disabilitato) {
		this.disabilitato = disabilitato;
	}

	/**
	 * @return the selezionaVideo
	 */
	public String getSelezionaVideo() {
		return selezionaVideo;
	}

	/**
	 * @param selezionaVideo the selezionaVideo to set
	 */
	public void setSelezionaVideo(String selezionaVideo) {
		this.selezionaVideo = selezionaVideo;
	}

	/**
	 * @return the isAttestazioneAzienda
	 */
	public boolean isAttestazioneAzienda() {
		return isAttestazioneAzienda;
	}

	/**
	 * @param isAttestazioneAzienda the isAttestazioneAzienda to set
	 */
	public void setAttestazioneAzienda(boolean isAttestazioneAzienda) {
		this.isAttestazioneAzienda = isAttestazioneAzienda;
	}

	/**
	 * @return the attestazioniCorrelate
	 */
	public String getAttestazioniCorrelate() {
		return attestazioniCorrelate;
	}

	/**
	 * @param attestazioniCorrelate the attestazioniCorrelate to set
	 */
	public void setAttestazioniCorrelate(String attestazioniCorrelate) {
		this.attestazioniCorrelate = attestazioniCorrelate;
	}

	/**
	 * @return the voceMenu
	 */
	public String getVoceMenu() {
		return voceMenu;
	}

	/**
	 * @param voceMenu the voceMenu to set
	 */
	public void setVoceMenu(String voceMenu) {
		this.voceMenu = voceMenu;
	}

	/**
	 * @return the parametriAttAziendaVO
	 */
	public ParametriAttAziendaVO getParametriAttAziendaVO() {
		return parametriAttAziendaVO;
	}

	/**
	 * @param parametriAttAziendaVO the parametriAttAziendaVO to set
	 */
	public void setParametriAttAziendaVO(ParametriAttAziendaVO parametriAttAziendaVO) {
		this.parametriAttAziendaVO = parametriAttAziendaVO;
	}

	/**
	 * @return the parametriAttDichiarataVO
	 */
	public ParametriAttDichiarataVO getParametriAttDichiarataVO() {
		return parametriAttDichiarataVO;
	}

	/**
	 * @param parametriAttDichiarataVO the parametriAttDichiarataVO to set
	 */
	public void setParametriAttDichiarataVO(
			ParametriAttDichiarataVO parametriAttDichiarataVO) {
		this.parametriAttDichiarataVO = parametriAttDichiarataVO;
	}

	/**
	 * @return the tipoParametriAttestazioneVO
	 */
	public TipoParametriAttestazioneVO getTipoParametriAttestazioneVO() {
		return tipoParametriAttestazioneVO;
	}

	/**
	 * @param tipoParametriAttestazioneVO the tipoParametriAttestazioneVO to set
	 */
	public void setTipoParametriAttestazioneVO(TipoParametriAttestazioneVO tipoParametriAttestazioneVO) {
		this.tipoParametriAttestazioneVO = tipoParametriAttestazioneVO;
	}


}