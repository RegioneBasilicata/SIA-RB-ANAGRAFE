package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.StringcodeDescription;
import it.csi.solmr.dto.anag.terreni.PorzioneCertificataVO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

/**
 * Classe che si occupa di mappare la tabella DB_PARTICELLA_CERTIFICATA
 * @author Mauro Vocale
 *
 */
public class ParticellaCertificataVO implements Serializable {

	
	

	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -7831116232363924049L;

	// Attributi con nomi corrispondenti ai campi della tabella DB_PARTICELLA_CERTIFICATA
	private Long idParticellaCertificata = null;
	private String istatComune = null;
	private String descrizioneComune = null;
	private String sezione = null;
	private String foglio = null;
	private String particella = null;
	private CodeDescription zonaAltimetrica = null;
	private Long idParticella = null;
	private String supCatastaleCertificata = null;
	private String supNonEleggibile = null;
	private String supNeBoscoAcqueFabbricato = null;
	private String supNeForaggiere = null;
	private String supElFruttaGuscio = null;
	private String supElPratoPascolo = null;
	private String supElColtureMiste = null;
	private String supColtivazArboreaCons = null;
	private String supColtivazArboreaSpec = null;
	private Date dataFoto = null;
	private String tipoFoto = null;
	private Date dataCaricamento = null;
	private CodeDescription fonteDato = null;
	private Date dataValidazioneFonteElegg = null;
	private String stato = null;
	private String subalterno = null;
	private String qualita = null; // ===> Da eliminare
	private Long idQualita = null;
	private CodeDescription tipoQualita = null;
	private String classe = null;
	private String partita = null;
	private Date dataUltimoAggiornamento = null;
	private String denominatore = null;
	private Date dataValidazioneFonteCatasto = null;
	private PorzioneCertificataVO[] elencoPorzioniCertificate = null;
	private StringcodeDescription tipologiaFoto = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	private String flagProvenienza = null;
	private java.util.Date dataSoppressione = null;
	private String supSeminabile = null;
	private Integer particellaAGis = null;
	private String supAcque = null;
	private String supIncolti = null;
	private String supColtArboreaSpecializzata = null;
	private String supUsoNonAgricolo = null;
	private String supStrade = null;
	private String supColtivazioneArboreaConso = null;
	private String supAreaScoperta = null;
	private String supAreaCoperta = null;
	private Integer esito = null;
	private Long idFonteElegg = null;
	private CodeDescription fonteDatoElegg = null;
	//nuova eleggibilita
	private String supGrafica = null;
	private Vector<ParticellaCertElegVO> vParticellaCertEleg;
	private String supUsoGrafica = null;
	private Date dataSospensione;
	private String motivazioneGis;

	// Variabili di utilizzo applicativo non usate per i campi della tabella
	private boolean univoca = false;
	private boolean certificata = false;
	private String descrizioneFoto;

	/**
	 * Costruttore di default
	 *
	 */
	public ParticellaCertificataVO() {
		super();
	}

	/**
	 * @return the certificata
	 */
	public boolean isCertificata() {
		return certificata;
	}

	/**
	 * @param certificata the certificata to set
	 */
	public void setCertificata(boolean certificata) {
		this.certificata = certificata;
	}

	/**
	 * @return the classe
	 */
	public String getClasse() {
		return classe;
	}

	/**
	 * @param classe the classe to set
	 */
	public void setClasse(String classe) {
		this.classe = classe;
	}

	/**
	 * @return the dataCaricamento
	 */
	public Date getDataCaricamento() {
		return dataCaricamento;
	}

	/**
	 * @param dataCaricamento the dataCaricamento to set
	 */
	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}

	/**
	 * @return the dataFineValidita
	 */
	public java.util.Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(java.util.Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the dataFoto
	 */
	public Date getDataFoto() {
		return dataFoto;
	}

	/**
	 * @param dataFoto the dataFoto to set
	 */
	public void setDataFoto(Date dataFoto) {
		this.dataFoto = dataFoto;
	}

	/**
	 * @return the dataInizioValidita
	 */
	public java.util.Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(java.util.Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the dataSoppressione
	 */
	public java.util.Date getDataSoppressione() {
		return dataSoppressione;
	}

	/**
	 * @param dataSoppressione the dataSoppressione to set
	 */
	public void setDataSoppressione(java.util.Date dataSoppressione) {
		this.dataSoppressione = dataSoppressione;
	}

	/**
	 * @return the dataUltimoAggiornamento
	 */
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	/**
	 * @param dataUltimoAggiornamento the dataUltimoAggiornamento to set
	 */
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	/**
	 * @return the dataValidazioneFonteCatasto
	 */
	public Date getDataValidazioneFonteCatasto() {
		return dataValidazioneFonteCatasto;
	}

	/**
	 * @param dataValidazioneFonteCatasto the dataValidazioneFonteCatasto to set
	 */
	public void setDataValidazioneFonteCatasto(Date dataValidazioneFonteCatasto) {
		this.dataValidazioneFonteCatasto = dataValidazioneFonteCatasto;
	}

	/**
	 * @return the dataValidazioneFonteElegg
	 */
	public Date getDataValidazioneFonteElegg() {
		return dataValidazioneFonteElegg;
	}

	/**
	 * @param dataValidazioneFonteElegg the dataValidazioneFonteElegg to set
	 */
	public void setDataValidazioneFonteElegg(Date dataValidazioneFonteElegg) {
		this.dataValidazioneFonteElegg = dataValidazioneFonteElegg;
	}

	/**
	 * @return the denominatore
	 */
	public String getDenominatore() {
		return denominatore;
	}

	/**
	 * @param denominatore the denominatore to set
	 */
	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}

	/**
	 * @return the descrizioneComune
	 */
	public String getDescrizioneComune() {
		return descrizioneComune;
	}

	/**
	 * @param descrizioneComune the descrizioneComune to set
	 */
	public void setDescrizioneComune(String descrizioneComune) {
		this.descrizioneComune = descrizioneComune;
	}

	/**
	 * @return the descrizioneFoto
	 */
	public String getDescrizioneFoto() {
		return descrizioneFoto;
	}

	/**
	 * @param descrizioneFoto the descrizioneFoto to set
	 */
	public void setDescrizioneFoto(String descrizioneFoto) {
		this.descrizioneFoto = descrizioneFoto;
	}

	/**
	 * @return the elencoPorzioniCertificate
	 */
	public PorzioneCertificataVO[] getElencoPorzioniCertificate() {
		return elencoPorzioniCertificate;
	}

	/**
	 * @param elencoPorzioniCertificate the elencoPorzioniCertificate to set
	 */
	public void setElencoPorzioniCertificate(
			PorzioneCertificataVO[] elencoPorzioniCertificate) {
		this.elencoPorzioniCertificate = elencoPorzioniCertificate;
	}

	/**
	 * @return the esito
	 */
	public Integer getEsito() {
		return esito;
	}

	/**
	 * @param esito the esito to set
	 */
	public void setEsito(Integer esito) {
		this.esito = esito;
	}

	/**
	 * @return the flagProvenienza
	 */
	public String getFlagProvenienza() {
		return flagProvenienza;
	}

	/**
	 * @param flagProvenienza the flagProvenienza to set
	 */
	public void setFlagProvenienza(String flagProvenienza) {
		this.flagProvenienza = flagProvenienza;
	}

	/**
	 * @return the foglio
	 */
	public String getFoglio() {
		return foglio;
	}

	/**
	 * @param foglio the foglio to set
	 */
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	/**
	 * @return the fonteDato
	 */
	public CodeDescription getFonteDato() {
		return fonteDato;
	}

	/**
	 * @param fonteDato the fonteDato to set
	 */
	public void setFonteDato(CodeDescription fonteDato) {
		this.fonteDato = fonteDato;
	}

	/**
	 * @return the fonteDatoElegg
	 */
	public CodeDescription getFonteDatoElegg() {
		return fonteDatoElegg;
	}

	/**
	 * @param fonteDatoElegg the fonteDatoElegg to set
	 */
	public void setFonteDatoElegg(CodeDescription fonteDatoElegg) {
		this.fonteDatoElegg = fonteDatoElegg;
	}

	/**
	 * @return the idFonteElegg
	 */
	public Long getIdFonteElegg() {
		return idFonteElegg;
	}

	/**
	 * @param idFonteElegg the idFonteElegg to set
	 */
	public void setIdFonteElegg(Long idFonteElegg) {
		this.idFonteElegg = idFonteElegg;
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
	 * @return the idParticellaCertificata
	 */
	public Long getIdParticellaCertificata() {
		return idParticellaCertificata;
	}

	/**
	 * @param idParticellaCertificata the idParticellaCertificata to set
	 */
	public void setIdParticellaCertificata(Long idParticellaCertificata) {
		this.idParticellaCertificata = idParticellaCertificata;
	}

	/**
	 * @return the idQualita
	 */
	public Long getIdQualita() {
		return idQualita;
	}

	/**
	 * @param idQualita the idQualita to set
	 */
	public void setIdQualita(Long idQualita) {
		this.idQualita = idQualita;
	}

	/**
	 * @return the istatComune
	 */
	public String getIstatComune() {
		return istatComune;
	}

	/**
	 * @param istatComune the istatComune to set
	 */
	public void setIstatComune(String istatComune) {
		this.istatComune = istatComune;
	}

	/**
	 * @return the particella
	 */
	public String getParticella() {
		return particella;
	}

	/**
	 * @param particella the particella to set
	 */
	public void setParticella(String particella) {
		this.particella = particella;
	}

	/**
	 * @return the particellaAGis
	 */
	public Integer getParticellaAGis() {
		return particellaAGis;
	}

	/**
	 * @param particellaAGis the particellaAGis to set
	 */
	public void setParticellaAGis(Integer particellaAGis) {
		this.particellaAGis = particellaAGis;
	}

	/**
	 * @return the partita
	 */
	public String getPartita() {
		return partita;
	}

	/**
	 * @param partita the partita to set
	 */
	public void setPartita(String partita) {
		this.partita = partita;
	}

	/**
	 * @return the qualita
	 */
	public String getQualita() {
		return qualita;
	}

	/**
	 * @param qualita the qualita to set
	 */
	public void setQualita(String qualita) {
		this.qualita = qualita;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @param sezione the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the subalterno
	 */
	public String getSubalterno() {
		return subalterno;
	}

	/**
	 * @param subalterno the subalterno to set
	 */
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	/**
	 * @return the supAcque
	 */
	public String getSupAcque() {
		return supAcque;
	}

	/**
	 * @param supAcque the supAcque to set
	 */
	public void setSupAcque(String supAcque) {
		this.supAcque = supAcque;
	}

	/**
	 * @return the supAreaCoperta
	 */
	public String getSupAreaCoperta() {
		return supAreaCoperta;
	}

	/**
	 * @param supAreaCoperta the supAreaCoperta to set
	 */
	public void setSupAreaCoperta(String supAreaCoperta) {
		this.supAreaCoperta = supAreaCoperta;
	}

	/**
	 * @return the supAreaScoperta
	 */
	public String getSupAreaScoperta() {
		return supAreaScoperta;
	}

	/**
	 * @param supAreaScoperta the supAreaScoperta to set
	 */
	public void setSupAreaScoperta(String supAreaScoperta) {
		this.supAreaScoperta = supAreaScoperta;
	}

	/**
	 * @return the supCatastaleCertificata
	 */
	public String getSupCatastaleCertificata() {
		return supCatastaleCertificata;
	}

	/**
	 * @param supCatastaleCertificata the supCatastaleCertificata to set
	 */
	public void setSupCatastaleCertificata(String supCatastaleCertificata) {
		this.supCatastaleCertificata = supCatastaleCertificata;
	}

	/**
	 * @return the supColtArboreaSpecializzata
	 */
	public String getSupColtArboreaSpecializzata() {
		return supColtArboreaSpecializzata;
	}

	/**
	 * @param supColtArboreaSpecializzata the supColtArboreaSpecializzata to set
	 */
	public void setSupColtArboreaSpecializzata(String supColtArboreaSpecializzata) {
		this.supColtArboreaSpecializzata = supColtArboreaSpecializzata;
	}

	/**
	 * @return the supColtivazArboreaCons
	 */
	public String getSupColtivazArboreaCons() {
		return supColtivazArboreaCons;
	}

	/**
	 * @param supColtivazArboreaCons the supColtivazArboreaCons to set
	 */
	public void setSupColtivazArboreaCons(String supColtivazArboreaCons) {
		this.supColtivazArboreaCons = supColtivazArboreaCons;
	}

	/**
	 * @return the supColtivazArboreaSpec
	 */
	public String getSupColtivazArboreaSpec() {
		return supColtivazArboreaSpec;
	}

	/**
	 * @param supColtivazArboreaSpec the supColtivazArboreaSpec to set
	 */
	public void setSupColtivazArboreaSpec(String supColtivazArboreaSpec) {
		this.supColtivazArboreaSpec = supColtivazArboreaSpec;
	}

	/**
	 * @return the supColtivazioneArboreaConso
	 */
	public String getSupColtivazioneArboreaConso() {
		return supColtivazioneArboreaConso;
	}

	/**
	 * @param supColtivazioneArboreaConso the supColtivazioneArboreaConso to set
	 */
	public void setSupColtivazioneArboreaConso(String supColtivazioneArboreaConso) {
		this.supColtivazioneArboreaConso = supColtivazioneArboreaConso;
	}

	/**
	 * @return the supElColtureMiste
	 */
	public String getSupElColtureMiste() {
		return supElColtureMiste;
	}

	/**
	 * @param supElColtureMiste the supElColtureMiste to set
	 */
	public void setSupElColtureMiste(String supElColtureMiste) {
		this.supElColtureMiste = supElColtureMiste;
	}

	/**
	 * @return the supElFruttaGuscio
	 */
	public String getSupElFruttaGuscio() {
		return supElFruttaGuscio;
	}

	/**
	 * @param supElFruttaGuscio the supElFruttaGuscio to set
	 */
	public void setSupElFruttaGuscio(String supElFruttaGuscio) {
		this.supElFruttaGuscio = supElFruttaGuscio;
	}

	/**
	 * @return the supElPratoPascolo
	 */
	public String getSupElPratoPascolo() {
		return supElPratoPascolo;
	}

	/**
	 * @param supElPratoPascolo the supElPratoPascolo to set
	 */
	public void setSupElPratoPascolo(String supElPratoPascolo) {
		this.supElPratoPascolo = supElPratoPascolo;
	}

	/**
	 * @return the supIncolti
	 */
	public String getSupIncolti() {
		return supIncolti;
	}

	/**
	 * @param supIncolti the supIncolti to set
	 */
	public void setSupIncolti(String supIncolti) {
		this.supIncolti = supIncolti;
	}

	/**
	 * @return the supNeBoscoAcqueFabbricato
	 */
	public String getSupNeBoscoAcqueFabbricato() {
		return supNeBoscoAcqueFabbricato;
	}

	/**
	 * @param supNeBoscoAcqueFabbricato the supNeBoscoAcqueFabbricato to set
	 */
	public void setSupNeBoscoAcqueFabbricato(String supNeBoscoAcqueFabbricato) {
		this.supNeBoscoAcqueFabbricato = supNeBoscoAcqueFabbricato;
	}

	/**
	 * @return the supNeForaggiere
	 */
	public String getSupNeForaggiere() {
		return supNeForaggiere;
	}

	/**
	 * @param supNeForaggiere the supNeForaggiere to set
	 */
	public void setSupNeForaggiere(String supNeForaggiere) {
		this.supNeForaggiere = supNeForaggiere;
	}

	/**
	 * @return the supNonEleggibile
	 */
	public String getSupNonEleggibile() {
		return supNonEleggibile;
	}

	/**
	 * @param supNonEleggibile the supNonEleggibile to set
	 */
	public void setSupNonEleggibile(String supNonEleggibile) {
		this.supNonEleggibile = supNonEleggibile;
	}

	/**
	 * @return the supSeminabile
	 */
	public String getSupSeminabile() {
		return supSeminabile;
	}

	/**
	 * @param supSeminabile the supSeminabile to set
	 */
	public void setSupSeminabile(String supSeminabile) {
		this.supSeminabile = supSeminabile;
	}

	/**
	 * @return the supStrade
	 */
	public String getSupStrade() {
		return supStrade;
	}

	/**
	 * @param supStrade the supStrade to set
	 */
	public void setSupStrade(String supStrade) {
		this.supStrade = supStrade;
	}

	/**
	 * @return the supUsoNonAgricolo
	 */
	public String getSupUsoNonAgricolo() {
		return supUsoNonAgricolo;
	}

	/**
	 * @param supUsoNonAgricolo the supUsoNonAgricolo to set
	 */
	public void setSupUsoNonAgricolo(String supUsoNonAgricolo) {
		this.supUsoNonAgricolo = supUsoNonAgricolo;
	}

	/**
	 * @return the tipoFoto
	 */
	public String getTipoFoto() {
		return tipoFoto;
	}

	/**
	 * @param tipoFoto the tipoFoto to set
	 */
	public void setTipoFoto(String tipoFoto) {
		this.tipoFoto = tipoFoto;
	}

	/**
	 * @return the tipologiaFoto
	 */
	public StringcodeDescription getTipologiaFoto() {
		return tipologiaFoto;
	}

	/**
	 * @param tipologiaFoto the tipologiaFoto to set
	 */
	public void setTipologiaFoto(StringcodeDescription tipologiaFoto) {
		this.tipologiaFoto = tipologiaFoto;
	}

	/**
	 * @return the tipoQualita
	 */
	public CodeDescription getTipoQualita() {
		return tipoQualita;
	}

	/**
	 * @param tipoQualita the tipoQualita to set
	 */
	public void setTipoQualita(CodeDescription tipoQualita) {
		this.tipoQualita = tipoQualita;
	}

	/**
	 * @return the univoca
	 */
	public boolean isUnivoca() {
		return univoca;
	}

	/**
	 * @param univoca the univoca to set
	 */
	public void setUnivoca(boolean univoca) {
		this.univoca = univoca;
	}

	/**
	 * @return the zonaAltimetrica
	 */
	public CodeDescription getZonaAltimetrica() {
		return zonaAltimetrica;
	}

	/**
	 * @param zonaAltimetrica the zonaAltimetrica to set
	 */
	public void setZonaAltimetrica(CodeDescription zonaAltimetrica) {
		this.zonaAltimetrica = zonaAltimetrica;
	}
	
	//nuova eleggibilità
	public String getSupGrafica()
  {
    return supGrafica;
  }

  public void setSupGrafica(String supGrafica)
  {
    this.supGrafica = supGrafica;
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
		final ParticellaCertificataVO other = (ParticellaCertificataVO) obj;
		if (certificata != other.certificata)
			return false;
		if (classe == null) {
			if (other.classe != null)
				return false;
		} else if (!classe.equals(other.classe))
			return false;
		if (dataCaricamento == null) {
			if (other.dataCaricamento != null)
				return false;
		} else if (!dataCaricamento.equals(other.dataCaricamento))
			return false;
		if (dataFineValidita == null) {
			if (other.dataFineValidita != null)
				return false;
		} else if (!dataFineValidita.equals(other.dataFineValidita))
			return false;
		if (dataFoto == null) {
			if (other.dataFoto != null)
				return false;
		} else if (!dataFoto.equals(other.dataFoto))
			return false;
		if (dataInizioValidita == null) {
			if (other.dataInizioValidita != null)
				return false;
		} else if (!dataInizioValidita.equals(other.dataInizioValidita))
			return false;
		if (dataSoppressione == null) {
			if (other.dataSoppressione != null)
				return false;
		} else if (!dataSoppressione.equals(other.dataSoppressione))
			return false;
		if (dataUltimoAggiornamento == null) {
			if (other.dataUltimoAggiornamento != null)
				return false;
		} else if (!dataUltimoAggiornamento.equals(other.dataUltimoAggiornamento))
			return false;
		if (dataValidazioneFonteCatasto == null) {
			if (other.dataValidazioneFonteCatasto != null)
				return false;
		} else if (!dataValidazioneFonteCatasto.equals(other.dataValidazioneFonteCatasto))
			return false;
		if (dataValidazioneFonteElegg == null) {
			if (other.dataValidazioneFonteElegg != null)
				return false;
		} else if (!dataValidazioneFonteElegg.equals(other.dataValidazioneFonteElegg))
			return false;
		if (denominatore == null) {
			if (other.denominatore != null)
				return false;
		} else if (!denominatore.equals(other.denominatore))
			return false;
		if (descrizioneComune == null) {
			if (other.descrizioneComune != null)
				return false;
		} else if (!descrizioneComune.equals(other.descrizioneComune))
			return false;
		if (descrizioneFoto == null) {
			if (other.descrizioneFoto != null)
				return false;
		} else if (!descrizioneFoto.equals(other.descrizioneFoto))
			return false;
		if (!Arrays.equals(elencoPorzioniCertificate, other.elencoPorzioniCertificate))
			return false;
		if (esito == null) {
			if (other.esito != null)
				return false;
		} else if (!esito.equals(other.esito))
			return false;
		if (flagProvenienza == null) {
			if (other.flagProvenienza != null)
				return false;
		} else if (!flagProvenienza.equals(other.flagProvenienza))
			return false;
		if (foglio == null) {
			if (other.foglio != null)
				return false;
		} else if (!foglio.equals(other.foglio))
			return false;
		if (fonteDato == null) {
			if (other.fonteDato != null)
				return false;
		} else if (!fonteDato.equals(other.fonteDato))
			return false;
		if (fonteDatoElegg == null) {
			if (other.fonteDatoElegg != null)
				return false;
		} else if (!fonteDatoElegg.equals(other.fonteDatoElegg))
			return false;
		if (idFonteElegg == null) {
			if (other.idFonteElegg != null)
				return false;
		} else if (!idFonteElegg.equals(other.idFonteElegg))
			return false;
		if (idParticella == null) {
			if (other.idParticella != null)
				return false;
		} else if (!idParticella.equals(other.idParticella))
			return false;
		if (idParticellaCertificata == null) {
			if (other.idParticellaCertificata != null)
				return false;
		} else if (!idParticellaCertificata.equals(other.idParticellaCertificata))
			return false;
		if (idQualita == null) {
			if (other.idQualita != null)
				return false;
		} else if (!idQualita.equals(other.idQualita))
			return false;
		if (istatComune == null) {
			if (other.istatComune != null)
				return false;
		} else if (!istatComune.equals(other.istatComune))
			return false;
		if (particella == null) {
			if (other.particella != null)
				return false;
		} else if (!particella.equals(other.particella))
			return false;
		if (particellaAGis == null) {
			if (other.particellaAGis != null)
				return false;
		} else if (!particellaAGis.equals(other.particellaAGis))
			return false;
		if (partita == null) {
			if (other.partita != null)
				return false;
		} else if (!partita.equals(other.partita))
			return false;
		if (qualita == null) {
			if (other.qualita != null)
				return false;
		} else if (!qualita.equals(other.qualita))
			return false;
		if (sezione == null) {
			if (other.sezione != null)
				return false;
		} else if (!sezione.equals(other.sezione))
			return false;
		if (stato == null) {
			if (other.stato != null)
				return false;
		} else if (!stato.equals(other.stato))
			return false;
		if (subalterno == null) {
			if (other.subalterno != null)
				return false;
		} else if (!subalterno.equals(other.subalterno))
			return false;
		if (supAcque == null) {
			if (other.supAcque != null)
				return false;
		} else if (!supAcque.equals(other.supAcque))
			return false;
		if (supAreaCoperta == null) {
			if (other.supAreaCoperta != null)
				return false;
		} else if (!supAreaCoperta.equals(other.supAreaCoperta))
			return false;
		if (supAreaScoperta == null) {
			if (other.supAreaScoperta != null)
				return false;
		} else if (!supAreaScoperta.equals(other.supAreaScoperta))
			return false;
		if (supCatastaleCertificata == null) {
			if (other.supCatastaleCertificata != null)
				return false;
		} else if (!supCatastaleCertificata.equals(other.supCatastaleCertificata))
			return false;
		if (supColtArboreaSpecializzata == null) {
			if (other.supColtArboreaSpecializzata != null)
				return false;
		} else if (!supColtArboreaSpecializzata.equals(other.supColtArboreaSpecializzata))
			return false;
		if (supColtivazArboreaCons == null) {
			if (other.supColtivazArboreaCons != null)
				return false;
		} else if (!supColtivazArboreaCons.equals(other.supColtivazArboreaCons))
			return false;
		if (supColtivazArboreaSpec == null) {
			if (other.supColtivazArboreaSpec != null)
				return false;
		} else if (!supColtivazArboreaSpec.equals(other.supColtivazArboreaSpec))
			return false;
		if (supColtivazioneArboreaConso == null) {
			if (other.supColtivazioneArboreaConso != null)
				return false;
		} else if (!supColtivazioneArboreaConso.equals(other.supColtivazioneArboreaConso))
			return false;
		if (supElColtureMiste == null) {
			if (other.supElColtureMiste != null)
				return false;
		} else if (!supElColtureMiste.equals(other.supElColtureMiste))
			return false;
		if (supElFruttaGuscio == null) {
			if (other.supElFruttaGuscio != null)
				return false;
		} else if (!supElFruttaGuscio.equals(other.supElFruttaGuscio))
			return false;
		if (supElPratoPascolo == null) {
			if (other.supElPratoPascolo != null)
				return false;
		} else if (!supElPratoPascolo.equals(other.supElPratoPascolo))
			return false;
		if (supIncolti == null) {
			if (other.supIncolti != null)
				return false;
		} else if (!supIncolti.equals(other.supIncolti))
			return false;
		if (supNeBoscoAcqueFabbricato == null) {
			if (other.supNeBoscoAcqueFabbricato != null)
				return false;
		} else if (!supNeBoscoAcqueFabbricato.equals(other.supNeBoscoAcqueFabbricato))
			return false;
		if (supNeForaggiere == null) {
			if (other.supNeForaggiere != null)
				return false;
		} else if (!supNeForaggiere.equals(other.supNeForaggiere))
			return false;
		if (supNonEleggibile == null) {
			if (other.supNonEleggibile != null)
				return false;
		} else if (!supNonEleggibile.equals(other.supNonEleggibile))
			return false;
		if (supSeminabile == null) {
			if (other.supSeminabile != null)
				return false;
		} else if (!supSeminabile.equals(other.supSeminabile))
			return false;
		if (supStrade == null) {
			if (other.supStrade != null)
				return false;
		} else if (!supStrade.equals(other.supStrade))
			return false;
		if (supUsoNonAgricolo == null) {
			if (other.supUsoNonAgricolo != null)
				return false;
		} else if (!supUsoNonAgricolo.equals(other.supUsoNonAgricolo))
			return false;
		if (tipoFoto == null) {
			if (other.tipoFoto != null)
				return false;
		} else if (!tipoFoto.equals(other.tipoFoto))
			return false;
		if (tipoQualita == null) {
			if (other.tipoQualita != null)
				return false;
		} else if (!tipoQualita.equals(other.tipoQualita))
			return false;
		if (tipologiaFoto == null) {
			if (other.tipologiaFoto != null)
				return false;
		} else if (!tipologiaFoto.equals(other.tipologiaFoto))
			return false;
		if (univoca != other.univoca)
			return false;
		if (zonaAltimetrica == null) {
			if (other.zonaAltimetrica != null)
				return false;
		} else if (!zonaAltimetrica.equals(other.zonaAltimetrica))
			return false;
		return true;
	}

  public Vector<ParticellaCertElegVO> getVParticellaCertEleg()
  {
    return vParticellaCertEleg;
  }

  public void setVParticellaCertEleg(Vector<ParticellaCertElegVO> particellaCertEleg)
  {
    vParticellaCertEleg = particellaCertEleg;
  }

  public String getSupUsoGrafica()
  {
    return supUsoGrafica;
  }

  public void setSupUsoGrafica(String supUsoGrafica)
  {
    this.supUsoGrafica = supUsoGrafica;
  }

  public Date getDataSospensione()
  {
    return dataSospensione;
  }

  public void setDataSospensione(Date dataSospensione)
  {
    this.dataSospensione = dataSospensione;
  }

  public String getMotivazioneGis()
  {
    return motivazioneGis;
  }

  public void setMotivazioneGis(String motivazioneGis)
  {
    this.motivazioneGis = motivazioneGis;
  }
	
	

  

}
