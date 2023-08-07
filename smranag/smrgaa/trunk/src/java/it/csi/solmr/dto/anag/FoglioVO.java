package it.csi.solmr.dto.anag;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_FOGLIO
 * @author Mauro Vocale
 *
 */
public class FoglioVO implements Serializable {
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -6613419466586550440L;

	private Long idFoglio = null;
	private Long idCasoParticolare = null;
	private String descrizioneCasoParticolare = null;
	private String sezione = null;
	private Long idAreaA = null;
	private String descrizioneAreaA = null;
	private Long idAreaB = null;
	private String descrizioneAreaB = null;
	private Long idAreaC = null;
	private String descrizioneAreaC = null;
	private Long idAreaD = null;
	private String descrizioneAreaD = null;
	private Long idAreaE = null;
	private String descrizioneAreaE = null;
	private Long foglio = null;
	private String istatComune = null;
	private String descrizioneComune = null;
	private String flagCaptazionePozzi = null;
	private Long idZonaAltimetrica = null;
	private String descrizioneZonaAltimetrica = null;
	private String descrizioneSezione = null;
	private String superficieFoglio = null;
	private String flagProvvisorio = null;
	private Long idAreaF = null;
	private String descrizioneAreaF = null;
  private Long idAreaPSN = null;
	private String descrizioneAreaPSN = null;
	private Long idAreaEFasciaFluviale = null;
  private String descrizioneAreaEFasciaFluviale = null;
  private Long idAreaG = null;
  private String descrizioneAreaG = null;
  private Long idAreaI = null;
  private String descrizioneAreaI = null;
  private Long idAreaL = null;
  private String descrizioneAreaL = null;
  private Integer flagStabilizzazione = null;
	
	

	/**
	 * @return the descrizioneAreaA
	 */
	public String getDescrizioneAreaA() {
		return descrizioneAreaA;
	}

	/**
	 * @param descrizioneAreaA the descrizioneAreaA to set
	 */
	public void setDescrizioneAreaA(String descrizioneAreaA) {
		this.descrizioneAreaA = descrizioneAreaA;
	}

	/**
	 * @return the descrizioneAreaB
	 */
	public String getDescrizioneAreaB() {
		return descrizioneAreaB;
	}

	/**
	 * @param descrizioneAreaB the descrizioneAreaB to set
	 */
	public void setDescrizioneAreaB(String descrizioneAreaB) {
		this.descrizioneAreaB = descrizioneAreaB;
	}

	/**
	 * @return the descrizioneAreaC
	 */
	public String getDescrizioneAreaC() {
		return descrizioneAreaC;
	}

	/**
	 * @param descrizioneAreaC the descrizioneAreaC to set
	 */
	public void setDescrizioneAreaC(String descrizioneAreaC) {
		this.descrizioneAreaC = descrizioneAreaC;
	}

	/**
	 * @return the descrizioneAreaD
	 */
	public String getDescrizioneAreaD() {
		return descrizioneAreaD;
	}

	/**
	 * @param descrizioneAreaD the descrizioneAreaD to set
	 */
	public void setDescrizioneAreaD(String descrizioneAreaD) {
		this.descrizioneAreaD = descrizioneAreaD;
	}

	/**
	 * @return the descrizioneAreaE
	 */
	public String getDescrizioneAreaE() {
		return descrizioneAreaE;
	}

	/**
	 * @param descrizioneAreaE the descrizioneAreaE to set
	 */
	public void setDescrizioneAreaE(String descrizioneAreaE) {
		this.descrizioneAreaE = descrizioneAreaE;
	}

	/**
	 * @return the descrizioneCasoParticolare
	 */
	public String getDescrizioneCasoParticolare() {
		return descrizioneCasoParticolare;
	}

	/**
	 * @param descrizioneCasoParticolare the descrizioneCasoParticolare to set
	 */
	public void setDescrizioneCasoParticolare(String descrizioneCasoParticolare) {
		this.descrizioneCasoParticolare = descrizioneCasoParticolare;
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
	 * @return the descrizioneSezione
	 */
	public String getDescrizioneSezione() {
		return descrizioneSezione;
	}

	/**
	 * @param descrizioneSezione the descrizioneSezione to set
	 */
	public void setDescrizioneSezione(String descrizioneSezione) {
		this.descrizioneSezione = descrizioneSezione;
	}

	/**
	 * @return the descrizioneZonaAltimetrica
	 */
	public String getDescrizioneZonaAltimetrica() {
		return descrizioneZonaAltimetrica;
	}

	/**
	 * @param descrizioneZonaAltimetrica the descrizioneZonaAltimetrica to set
	 */
	public void setDescrizioneZonaAltimetrica(String descrizioneZonaAltimetrica) {
		this.descrizioneZonaAltimetrica = descrizioneZonaAltimetrica;
	}

	/**
	 * @return the flagCaptazionePozzi
	 */
	public String getFlagCaptazionePozzi() {
		return flagCaptazionePozzi;
	}

	/**
	 * @param flagCaptazionePozzi the flagCaptazionePozzi to set
	 */
	public void setFlagCaptazionePozzi(String flagCaptazionePozzi) {
		this.flagCaptazionePozzi = flagCaptazionePozzi;
	}

	/**
	 * @return the flagProvvisorio
	 */
	public String getFlagProvvisorio() {
		return flagProvvisorio;
	}

	/**
	 * @param flagProvvisorio the flagProvvisorio to set
	 */
	public void setFlagProvvisorio(String flagProvvisorio) {
		this.flagProvvisorio = flagProvvisorio;
	}

	/**
	 * @return the foglio
	 */
	public Long getFoglio() {
		return foglio;
	}

	/**
	 * @param foglio the foglio to set
	 */
	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}

	/**
	 * @return the idAreaA
	 */
	public Long getIdAreaA() {
		return idAreaA;
	}

	/**
	 * @param idAreaA the idAreaA to set
	 */
	public void setIdAreaA(Long idAreaA) {
		this.idAreaA = idAreaA;
	}

	/**
	 * @return the idAreaB
	 */
	public Long getIdAreaB() {
		return idAreaB;
	}

	/**
	 * @param idAreaB the idAreaB to set
	 */
	public void setIdAreaB(Long idAreaB) {
		this.idAreaB = idAreaB;
	}

	/**
	 * @return the idAreaC
	 */
	public Long getIdAreaC() {
		return idAreaC;
	}

	/**
	 * @param idAreaC the idAreaC to set
	 */
	public void setIdAreaC(Long idAreaC) {
		this.idAreaC = idAreaC;
	}

	/**
	 * @return the idAreaD
	 */
	public Long getIdAreaD() {
		return idAreaD;
	}

	/**
	 * @param idAreaD the idAreaD to set
	 */
	public void setIdAreaD(Long idAreaD) {
		this.idAreaD = idAreaD;
	}

	/**
	 * @return the idAreaE
	 */
	public Long getIdAreaE() {
		return idAreaE;
	}

	/**
	 * @param idAreaE the idAreaE to set
	 */
	public void setIdAreaE(Long idAreaE) {
		this.idAreaE = idAreaE;
	}

	/**
	 * @return the idCasoParticolare
	 */
	public Long getIdCasoParticolare() {
		return idCasoParticolare;
	}

	/**
	 * @param idCasoParticolare the idCasoParticolare to set
	 */
	public void setIdCasoParticolare(Long idCasoParticolare) {
		this.idCasoParticolare = idCasoParticolare;
	}

	/**
	 * @return the idFoglio
	 */
	public Long getIdFoglio() {
		return idFoglio;
	}

	/**
	 * @param idFoglio the idFoglio to set
	 */
	public void setIdFoglio(Long idFoglio) {
		this.idFoglio = idFoglio;
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
	 * @return the superficieFoglio
	 */
	public String getSuperficieFoglio() {
		return superficieFoglio;
	}

	/**
	 * @param superficieFoglio the superficieFoglio to set
	 */
	public void setSuperficieFoglio(String superficieFoglio) {
		this.superficieFoglio = superficieFoglio;
	}

	/**
	 * @return the descrizioneAreaF
	 */
	public String getDescrizioneAreaF() {
		return descrizioneAreaF;
	}

	/**
	 * @param descrizioneAreaF the descrizioneAreaF to set
	 */
	public void setDescrizioneAreaF(String descrizioneAreaF) {
		this.descrizioneAreaF = descrizioneAreaF;
	}

	/**
	 * @return the idAreaF
	 */
	public Long getIdAreaF() {
		return idAreaF;
	}

	/**
	 * @param idAreaF the idAreaF to set
	 */
	public void setIdAreaF(Long idAreaF) {
		this.idAreaF = idAreaF;
	}

  public Long getIdAreaPSN()
  {
    return idAreaPSN;
  }

  public void setIdAreaPSN(Long idAreaPSN)
  {
    this.idAreaPSN = idAreaPSN;
  }

  public String getDescrizioneAreaPSN()
  {
    return descrizioneAreaPSN;
  }

  public void setDescrizioneAreaPSN(String descrizioneAreaPSN)
  {
    this.descrizioneAreaPSN = descrizioneAreaPSN;
  }

  public Long getIdAreaEFasciaFluviale()
  {
    return idAreaEFasciaFluviale;
  }

  public void setIdAreaEFasciaFluviale(Long idAreaEFasciaFluviale)
  {
    this.idAreaEFasciaFluviale = idAreaEFasciaFluviale;
  }

  public String getDescrizioneAreaEFasciaFluviale()
  {
    return descrizioneAreaEFasciaFluviale;
  }

  public void setDescrizioneAreaEFasciaFluviale(
      String descrizioneAreaEFasciaFluviale)
  {
    this.descrizioneAreaEFasciaFluviale = descrizioneAreaEFasciaFluviale;
  }

  public Long getIdAreaG()
  {
    return idAreaG;
  }

  public void setIdAreaG(Long idAreaG)
  {
    this.idAreaG = idAreaG;
  }

  public String getDescrizioneAreaG()
  {
    return descrizioneAreaG;
  }

  public void setDescrizioneAreaG(String descrizioneAreaG)
  {
    this.descrizioneAreaG = descrizioneAreaG;
  }

  public Long getIdAreaI()
  {
    return idAreaI;
  }

  public void setIdAreaI(Long idAreaI)
  {
    this.idAreaI = idAreaI;
  }

  public String getDescrizioneAreaI()
  {
    return descrizioneAreaI;
  }

  public void setDescrizioneAreaI(String descrizioneAreaI)
  {
    this.descrizioneAreaI = descrizioneAreaI;
  }

  public Long getIdAreaL()
  {
    return idAreaL;
  }

  public void setIdAreaL(Long idAreaL)
  {
    this.idAreaL = idAreaL;
  }

  public String getDescrizioneAreaL()
  {
    return descrizioneAreaL;
  }

  public void setDescrizioneAreaL(String descrizioneAreaL)
  {
    this.descrizioneAreaL = descrizioneAreaL;
  }

  public Integer getFlagStabilizzazione()
  {
    return flagStabilizzazione;
  }

  public void setFlagStabilizzazione(Integer flagStabilizzazione)
  {
    this.flagStabilizzazione = flagStabilizzazione;
  }

}
