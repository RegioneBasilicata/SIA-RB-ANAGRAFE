package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare gli elementi relativi ai filtri di ricerca dei terreni
 * @author Mauro Vocale
 *
 */
public class FiltriUnitaArboreaRicercaVO implements Serializable {
	
	private static final long serialVersionUID = -1722756726534633966L;
	
	private Long idPianoRiferimento = null;
	private Long idUtilizzo = null;
	private Long idVarieta = null;
	private String istatComune = null;
	private String sezione = null;
	private String foglio = null;
	private String particella = null;
	private String subalterno = null;
	private String tipoSegnalazioneBloccante = null;
	private String tipoSegnalazioneWarning = null;
	private String tipoSegnalazioneOk = null;
	private String dataEsecuzioneControlli = null;
	private String dataEsecuzioneControlliDichiarati = null;
	private String orderBy = null;
	private java.util.Date dataInserimentoDichiarazione = null;
	private Long idTipologiaVino = null;
	private Long idGenereIscrizione = null;
	private Long idCausaleModifica = null;
	//usato per discriminare se vi sono o meno
	//unita vitate che appartengono alla stessa provincia del ruolo utente provinciale.
	private boolean flagProvinciaCompetenza;
	private int paginaCorrente;
	private String istatProvincia = null;
	private Long idControllo = null;
	private Long idTipologiaNotifica;
	private Long idCategoriaNotifica;
	private String flagNotificheChiuse;
	

  public String getIstatProvincia()
  {
    return istatProvincia;
  }

  public void setIstatProvincia(String istatProvincia)
  {
    this.istatProvincia = istatProvincia;
  }

  public int getPaginaCorrente()
  {
    return paginaCorrente;
  }

  public void setPaginaCorrente(int paginaCorrente)
  {
    this.paginaCorrente = paginaCorrente;
  }

  /**
	 * @return the dataEsecuzioneControlli
	 */
	public String getDataEsecuzioneControlli() {
		return dataEsecuzioneControlli;
	}

	/**
	 * @param dataEsecuzioneControlli the dataEsecuzioneControlli to set
	 */
	public void setDataEsecuzioneControlli(String dataEsecuzioneControlli) {
		this.dataEsecuzioneControlli = dataEsecuzioneControlli;
	}

	/**
	 * @return the dataEsecuzioneControlliDichiarati
	 */
	public String getDataEsecuzioneControlliDichiarati() {
		return dataEsecuzioneControlliDichiarati;
	}

	/**
	 * @param dataEsecuzioneControlliDichiarati the dataEsecuzioneControlliDichiarati to set
	 */
	public void setDataEsecuzioneControlliDichiarati(
			String dataEsecuzioneControlliDichiarati) {
		this.dataEsecuzioneControlliDichiarati = dataEsecuzioneControlliDichiarati;
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
	 * @return the idPianoRiferimento
	 */
	public Long getIdPianoRiferimento() {
		return idPianoRiferimento;
	}

	/**
	 * @param idPianoRiferimento the idPianoRiferimento to set
	 */
	public void setIdPianoRiferimento(Long idPianoRiferimento) {
		this.idPianoRiferimento = idPianoRiferimento;
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
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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
	 * @return the tipoSegnalazioneBloccante
	 */
	public String getTipoSegnalazioneBloccante() {
		return tipoSegnalazioneBloccante;
	}

	/**
	 * @param tipoSegnalazioneBloccante the tipoSegnalazioneBloccante to set
	 */
	public void setTipoSegnalazioneBloccante(String tipoSegnalazioneBloccante) {
		this.tipoSegnalazioneBloccante = tipoSegnalazioneBloccante;
	}

	/**
	 * @return the tipoSegnalazioneOk
	 */
	public String getTipoSegnalazioneOk() {
		return tipoSegnalazioneOk;
	}

	/**
	 * @param tipoSegnalazioneOk the tipoSegnalazioneOk to set
	 */
	public void setTipoSegnalazioneOk(String tipoSegnalazioneOk) {
		this.tipoSegnalazioneOk = tipoSegnalazioneOk;
	}

	/**
	 * @return the tipoSegnalazioneWarning
	 */
	public String getTipoSegnalazioneWarning() {
		return tipoSegnalazioneWarning;
	}

	/**
	 * @param tipoSegnalazioneWarning the tipoSegnalazioneWarning to set
	 */
	public void setTipoSegnalazioneWarning(String tipoSegnalazioneWarning) {
		this.tipoSegnalazioneWarning = tipoSegnalazioneWarning;
	}

	/**
	 * @return the dataInserimentoDichiarazione
	 */
	public java.util.Date getDataInserimentoDichiarazione() {
		return dataInserimentoDichiarazione;
	}

	/**
	 * @param dataInserimentoDichiarazione the dataInserimentoDichiarazione to set
	 */
	public void setDataInserimentoDichiarazione(
			java.util.Date dataInserimentoDichiarazione) {
		this.dataInserimentoDichiarazione = dataInserimentoDichiarazione;
	}

	
  public boolean isFlagProvinciaCompetenza()
  {
    return flagProvinciaCompetenza;
  }

  public void setFlagProvinciaCompetenza(boolean flagProvinciaCompetenza)
  {
    this.flagProvinciaCompetenza = flagProvinciaCompetenza;
  }

  public Long getIdTipologiaVino()
  {
    return idTipologiaVino;
  }

  public void setIdTipologiaVino(Long idTipologiaVino)
  {
    this.idTipologiaVino = idTipologiaVino;
  }
  
  public Long getIdGenereIscrizione()
  {
    return idGenereIscrizione;
  }

  public void setIdGenereIscrizione(Long idGenereIscrizione)
  {
    this.idGenereIscrizione = idGenereIscrizione;
  }

  public Long getIdCausaleModifica()
  {
    return idCausaleModifica;
  }

  public void setIdCausaleModifica(Long idCausaleModifica)
  {
    this.idCausaleModifica = idCausaleModifica;
  }

  public Long getIdControllo()
  {
    return idControllo;
  }

  public void setIdControllo(Long idControllo)
  {
    this.idControllo = idControllo;
  }

  public Long getIdTipologiaNotifica()
  {
    return idTipologiaNotifica;
  }

  public void setIdTipologiaNotifica(Long idTipologiaNotifica)
  {
    this.idTipologiaNotifica = idTipologiaNotifica;
  }

  public Long getIdCategoriaNotifica()
  {
    return idCategoriaNotifica;
  }

  public void setIdCategoriaNotifica(Long idCategoriaNotifica)
  {
    this.idCategoriaNotifica = idCategoriaNotifica;
  }

  public String getFlagNotificheChiuse()
  {
    return flagNotificheChiuse;
  }

  public void setFlagNotificheChiuse(String flagNotificheChiuse)
  {
    this.flagNotificheChiuse = flagNotificheChiuse;
  }
  
  
  
	
}