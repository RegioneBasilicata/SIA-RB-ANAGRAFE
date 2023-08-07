package it.csi.solmr.dto.anag.terreni;

import it.csi.smranag.smrgaa.dto.SuperficieDescription;
import it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoMetodoIrriguoVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.ComuneVO;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.FoglioVO;
import it.csi.solmr.dto.anag.ParticellaCertificataVO;
import it.csi.solmr.dto.anag.ParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe che si occupa di mappare la tabella DB_STORICO_PARTICELLA
 * @author Mauro Vocale
 *
 */
public class StoricoParticellaVO implements Serializable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4902537084091565857L;
	
	private Long idStoricoParticella = null;
	private Long idParticella = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	private String istatComune = null;
	private ComuneVO comuneParticellaVO = null;
	private String sezione = null;
	private String foglio = null;
	private String particella = null;
	private String subalterno = null;
	private String supCatastale = null;
	private Long idZonaAltimetrica = null;
	private CodeDescription zonaAltimetrica = null;
	private Long idAreaA = null;
	private CodeDescription areaA = null;
	private Long idAreaB = null;
	private CodeDescription areaB = null;
	private Long idAreaC = null;
	private CodeDescription areaC = null;
	private Long idAreaD = null;
	private CodeDescription areaD = null;
	private Long idAreaG = null;
  private CodeDescription areaG = null;
  private Long idAreaH = null;
  private CodeDescription areaH = null;
  private Long idAreaI = null;
  private CodeDescription areaI = null;
  private Long idAreaL = null;
  private CodeDescription areaL = null;
  private Long idAreaM = null;
  private CodeDescription areaM = null;
	private String flagCaptazionePozzi = null;
	private String flagIrrigabile = null;
	private Long idCasoParticolare = null;
	private CodeDescription casoParticolare = null;
	private String motivoModifica = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private UtenteIrideVO utenteAggiornamento = null;
	private Long idCausaleModParticella = null;
	private CodeDescription causaleModParticella = null;
	private String supNonEleggibile = null;
	private String supNeBoscoAcqueFabbricato = null;
	private String supNeForaggere = null;
	private String supElFruttaGuscio = null;
	private String supElPratoPascolo = null;
	private String supElColtureMiste = null;
	private String supColtivazArboreaCons = null;
	private String supColtivazArboreaSpec = null;
	private java.util.Date dataFoto = null;
	private String tipoFoto = null;
	private Long idFonte = null;
	private CodeDescription fonte = null;
	private Long idDocumento = null;
	private Long idDocumentoProtocollato = null;
	private DocumentoVO documentoVO = null;
	private ConduzioneParticellaVO[] elencoConduzioni = null;
	private ConduzioneDichiarataVO[] elencoConduzioniDichiarate = null;
	private Vector<ConduzioneDichiarataVO> vConduzioniDichiarate = null;
	private FoglioVO foglioVO = null;
	private SezioneVO sezioneVO = null;
	private Date dataCessazione = null;
	private Long idIrrigazione = null;
	private TipoIrrigazioneVO tipoIrrigazioneVO = null;
	private ParticellaVO particellaVO = null;
	private StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
	private UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = null;
	private ParticellaCertificataVO particellaCertificataVO = null;
	private Long idFasciaFluviale = null;
	private CodeDescription fasciaFluviale = null;
	private String supSpandibile = null;
	private String gisP30 = null;
	private String gisP25 = null;
	private String gisP26 = null;
	private String sospesaGis = null;
	private String biologico = null;
	private Long idPotenzialitaIrrigua = null;
	private TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO = null;
	private Long idRotazioneColturale = null;
	private TipoRotazioneColturaleVO tipoRotazioneColturaleVO = null;
	private Long idTerrazzamento = null;
	private TipoTerrazzamentoVO tipoTerrazzamentoVO = null;
	private String superficieGrafica = null;
	//private Date dataRichiestaRiesame = null;
	//Ricava i dati dell'eleggibilità fittizia
	private Vector<SuperficieDescription> vSupElegFit = null;
	//Contiene il valore restituito dal plsql
	private Long istanzaRiesame;
  //Contiene l'elenco delle fasi ricavat con query!!
	private Vector<IstanzaRiesameVO> vIstanzaRiesame = null;
	private String inNotifica = null;
	private String note = null;
	private BigDecimal percentualePendenzaMedia;
	private BigDecimal gradiPendenzaMedia;
	private BigDecimal gradiEsposizioneMedia;
	private BigDecimal metriAltitudineMedia;
	private Long idMetodoIrriguo;
	private TipoMetodoIrriguoVO tipoMetodoIrriguo;
	private Vector<TipoAreaVO> vValoriTipoArea;
	
	
  //Usato nell'inserimento terreni, legato all'evento
  private boolean cessaParticella;
  
  
  private BigDecimal percentualeUtilizzoEleg;
  
  
	
  
  
	public Vector<SuperficieDescription> getvSupElegFit()
  {
    return vSupElegFit;
  }

  public void setvSupElegFit(Vector<SuperficieDescription> vSupElegFit)
  {
    this.vSupElegFit = vSupElegFit;
  }

  public TipoPotenzialitaIrriguaVO getTipoPotenzialitaIrriguaVO()
  {
    return tipoPotenzialitaIrriguaVO;
  }

  public void setTipoPotenzialitaIrriguaVO(
      TipoPotenzialitaIrriguaVO tipoPotenzialitaIrriguaVO)
  {
    this.tipoPotenzialitaIrriguaVO = tipoPotenzialitaIrriguaVO;
  }

  public TipoRotazioneColturaleVO getTipoRotazioneColturaleVO()
  {
    return tipoRotazioneColturaleVO;
  }

  public void setTipoRotazioneColturaleVO(
      TipoRotazioneColturaleVO tipoRotazioneColturaleVO)
  {
    this.tipoRotazioneColturaleVO = tipoRotazioneColturaleVO;
  }

  public TipoTerrazzamentoVO getTipoTerrazzamentoVO()
  {
    return tipoTerrazzamentoVO;
  }

  public void setTipoTerrazzamentoVO(TipoTerrazzamentoVO tipoTerrazzamentoVO)
  {
    this.tipoTerrazzamentoVO = tipoTerrazzamentoVO;
  }

  public String getSuperficieGrafica()
  {
    return superficieGrafica;
  }

  public void setSuperficieGrafica(String superficieGrafica)
  {
    this.superficieGrafica = superficieGrafica;
  }

  public Long getIdRotazioneColturale()
  {
    return idRotazioneColturale;
  }

  public void setIdRotazioneColturale(Long idRotazioneColturale)
  {
    this.idRotazioneColturale = idRotazioneColturale;
  }

  public Long getIdTerrazzamento()
  {
    return idTerrazzamento;
  }

  public void setIdTerrazzamento(Long idTerrazzamento)
  {
    this.idTerrazzamento = idTerrazzamento;
  }

  public Long getIdPotenzialitaIrrigua()
  {
    return idPotenzialitaIrrigua;
  }

  public void setIdPotenzialitaIrrigua(Long idPotenzialitaIrrigua)
  {
    this.idPotenzialitaIrrigua = idPotenzialitaIrrigua;
  }
	
	public String getSospesaGis()
  {
    return sospesaGis;
  }

  public void setSospesaGis(String sospesaGis)
  {
    this.sospesaGis = sospesaGis;
  }

  public String getSupSpandibile()
  {
    return supSpandibile;
  }

  public void setSupSpandibile(String supSpandibile)
  {
    this.supSpandibile = supSpandibile;
  }

  /**
	 * Costruttore di default
	 *
	 */
	public StoricoParticellaVO() {
		super();
	}

	/**
	 * @return the idStoricoParticella
	 */
	public Long getIdStoricoParticella() {
		return idStoricoParticella;
	}

	/**
	 * @param idStoricoParticella the idStoricoParticella to set
	 */
	public void setIdStoricoParticella(Long idStoricoParticella) {
		this.idStoricoParticella = idStoricoParticella;
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
	 * @return the comuneParticellaVO
	 */
	public ComuneVO getComuneParticellaVO() {
		return comuneParticellaVO;
	}

	/**
	 * @param comuneParticellaVO the comuneParticellaVO to set
	 */
	public void setComuneParticellaVO(ComuneVO comuneParticellaVO) {
		this.comuneParticellaVO = comuneParticellaVO;
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
	 * @return the supCatastale
	 */
	public String getSupCatastale() {
		return supCatastale;
	}

	/**
	 * @param supCatastale the supCatastale to set
	 */
	public void setSupCatastale(String supCatastale) {
		this.supCatastale = supCatastale;
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
	 * @return the areaA
	 */
	public CodeDescription getAreaA() {
		return areaA;
	}

	/**
	 * @param areaA the areaA to set
	 */
	public void setAreaA(CodeDescription areaA) {
		this.areaA = areaA;
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
	 * @return the areaB
	 */
	public CodeDescription getAreaB() {
		return areaB;
	}

	/**
	 * @param areaB the areaB to set
	 */
	public void setAreaB(CodeDescription areaB) {
		this.areaB = areaB;
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
	 * @return the areaC
	 */
	public CodeDescription getAreaC() {
		return areaC;
	}

	/**
	 * @param areaC the areaC to set
	 */
	public void setAreaC(CodeDescription areaC) {
		this.areaC = areaC;
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
	 * @return the areaD
	 */
	public CodeDescription getAreaD() {
		return areaD;
	}

	/**
	 * @param areaD the areaD to set
	 */
	public void setAreaD(CodeDescription areaD) {
		this.areaD = areaD;
	}
	
	/**
   * @return the idAreaG
   */
  public Long getIdAreaG() {
    return idAreaG;
  }

  /**
   * @param idAreaD the idAreaG to set
   */
  public void setIdAreaG(Long idAreaG) {
    this.idAreaG = idAreaG;
  }

  /**
   * @return the areaG
   */
  public CodeDescription getAreaG() {
    return areaG;
  }

  /**
   * @param areaG the areaG to set
   */
  public void setAreaG(CodeDescription areaG) {
    this.areaG = areaG;
  }
  
  /**
   * @return the idAreaH
   */
  public Long getIdAreaH() {
    return idAreaH;
  }

  /**
   * @param idAreaH the idAreaH to set
   */
  public void setIdAreaH(Long idAreaH) {
    this.idAreaH = idAreaH;
  }

  /**
   * @return the areaH
   */
  public CodeDescription getAreaH() {
    return areaH;
  }

  /**
   * @param areaH the areaH to set
   */
  public void setAreaH(CodeDescription areaH) {
    this.areaH = areaH;
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
	 * @return the flagIrrigabile
	 */
	public String getFlagIrrigabile() {
		return flagIrrigabile;
	}

	/**
	 * @param flagIrrigabile the flagIrrigabile to set
	 */
	public void setFlagIrrigabile(String flagIrrigabile) {
		this.flagIrrigabile = flagIrrigabile;
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
	 * @return the casoParticolare
	 */
	public CodeDescription getCasoParticolare() {
		return casoParticolare;
	}

	/**
	 * @param casoParticolare the casoParticolare to set
	 */
	public void setCasoParticolare(CodeDescription casoParticolare) {
		this.casoParticolare = casoParticolare;
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
	 * @return the utenteAggiornamento
	 */
	public UtenteIrideVO getUtenteAggiornamento() {
		return utenteAggiornamento;
	}

	/**
	 * @param utenteAggiornamento the utenteAggiornamento to set
	 */
	public void setUtenteAggiornamento(UtenteIrideVO utenteAggiornamento) {
		this.utenteAggiornamento = utenteAggiornamento;
	}

	/**
	 * @return the idCausaleModParticella
	 */
	public Long getIdCausaleModParticella() {
		return idCausaleModParticella;
	}

	/**
	 * @param idCausaleModParticella the idCausaleModParticella to set
	 */
	public void setIdCausaleModParticella(Long idCausaleModParticella) {
		this.idCausaleModParticella = idCausaleModParticella;
	}

	/**
	 * @return the causaleModParticella
	 */
	public CodeDescription getCausaleModParticella() {
		return causaleModParticella;
	}

	/**
	 * @param causaleModParticella the causaleModParticella to set
	 */
	public void setCausaleModParticella(CodeDescription causaleModParticella) {
		this.causaleModParticella = causaleModParticella;
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
	 * @return the supNeForaggere
	 */
	public String getSupNeForaggere() {
		return supNeForaggere;
	}

	/**
	 * @param supNeForaggere the supNeForaggere to set
	 */
	public void setSupNeForaggere(String supNeForaggere) {
		this.supNeForaggere = supNeForaggere;
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
	 * @return the dataFoto
	 */
	public java.util.Date getDataFoto() {
		return dataFoto;
	}

	/**
	 * @param dataFoto the dataFoto to set
	 */
	public void setDataFoto(java.util.Date dataFoto) {
		this.dataFoto = dataFoto;
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
	 * @return the idFonte
	 */
	public Long getIdFonte() {
		return idFonte;
	}

	/**
	 * @param idFonte the idFonte to set
	 */
	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}

	/**
	 * @return the fonte
	 */
	public CodeDescription getFonte() {
		return fonte;
	}

	/**
	 * @param fonte the fonte to set
	 */
	public void setFonte(CodeDescription fonte) {
		this.fonte = fonte;
	}

	/**
	 * @return the idDocumento
	 */
	public Long getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idDocumento the idDocumento to set
	 */
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @return the idDocumentoProtocollato
	 */
	public Long getIdDocumentoProtocollato() {
		return idDocumentoProtocollato;
	}

	/**
	 * @param idDocumentoProtocollato the idDocumentoProtocollato to set
	 */
	public void setIdDocumentoProtocollato(Long idDocumentoProtocollato) {
		this.idDocumentoProtocollato = idDocumentoProtocollato;
	}

	/**
	 * @return the documentoVO
	 */
	public DocumentoVO getDocumentoVO() {
		return documentoVO;
	}

	/**
	 * @param documentoVO the documentoVO to set
	 */
	public void setDocumentoVO(DocumentoVO documentoVO) {
		this.documentoVO = documentoVO;
	}

	/**
	 * @return the elencoConduzioni
	 */
	public ConduzioneParticellaVO[] getElencoConduzioni() {
		return elencoConduzioni;
	}

	/**
	 * @param elencoConduzioni the elencoConduzioni to set
	 */
	public void setElencoConduzioni(ConduzioneParticellaVO[] elencoConduzioni) {
		this.elencoConduzioni = elencoConduzioni;
	}

	/**
	 * @return the elencoConduzioniDichiarate
	 */
	public ConduzioneDichiarataVO[] getElencoConduzioniDichiarate() {
		return elencoConduzioniDichiarate;
	}

	/**
	 * @param elencoConduzioniDichiarate the elencoConduzioniDichiarate to set
	 */
	public void setElencoConduzioniDichiarate(
			ConduzioneDichiarataVO[] elencoConduzioniDichiarate) {
		this.elencoConduzioniDichiarate = elencoConduzioniDichiarate;
	}

	/**
	 * @return the foglioVO
	 */
	public FoglioVO getFoglioVO() {
		return foglioVO;
	}

	/**
	 * @param foglioVO the foglioVO to set
	 */
	public void setFoglioVO(FoglioVO foglioVO) {
		this.foglioVO = foglioVO;
	}

	/**
	 * @return the sezioneVO
	 */
	public SezioneVO getSezioneVO() {
		return sezioneVO;
	}

	/**
	 * @param sezioneVO the sezioneVO to set
	 */
	public void setSezioneVO(SezioneVO sezioneVO) {
		this.sezioneVO = sezioneVO;
	}

	/**
	 * @return the dataCessazione
	 */
	public Date getDataCessazione() {
		return dataCessazione;
	}

	/**
	 * @param dataCessazione the dataCessazione to set
	 */
	public void setDataCessazione(Date dataCessazione) {
		this.dataCessazione = dataCessazione;
	}

	/**
	 * @return the idIrrigazione
	 */
	public Long getIdIrrigazione() {
		return idIrrigazione;
	}

	/**
	 * @param idIrrigazione the idIrrigazione to set
	 */
	public void setIdIrrigazione(Long idIrrigazione) {
		this.idIrrigazione = idIrrigazione;
	}

	/**
	 * @return the tipoIrrigazioneVO
	 */
	public TipoIrrigazioneVO getTipoIrrigazioneVO() {
		return tipoIrrigazioneVO;
	}

	/**
	 * @param tipoIrrigazioneVO the tipoIrrigazioneVO to set
	 */
	public void setTipoIrrigazioneVO(TipoIrrigazioneVO tipoIrrigazioneVO) {
		this.tipoIrrigazioneVO = tipoIrrigazioneVO;
	}

	/**
	 * @return the particellaVO
	 */
	public ParticellaVO getParticellaVO() {
		return particellaVO;
	}

	/**
	 * @param particellaVO the particellaVO to set
	 */
	public void setParticellaVO(ParticellaVO particellaVO) {
		this.particellaVO = particellaVO;
	}

	/**
	 * @return the storicoUnitaArboreaVO
	 */
	public StoricoUnitaArboreaVO getStoricoUnitaArboreaVO() {
		return storicoUnitaArboreaVO;
	}

	/**
	 * @param storicoUnitaArboreaVO the storicoUnitaArboreaVO to set
	 */
	public void setStoricoUnitaArboreaVO(StoricoUnitaArboreaVO storicoUnitaArboreaVO) {
		this.storicoUnitaArboreaVO = storicoUnitaArboreaVO;
	}

	/**
	 * @return the unitaArboreaDichiarataVO
	 */
	public UnitaArboreaDichiarataVO getUnitaArboreaDichiarataVO() {
		return unitaArboreaDichiarataVO;
	}

	/**
	 * @param unitaArboreaDichiarataVO the unitaArboreaDichiarataVO to set
	 */
	public void setUnitaArboreaDichiarataVO(
			UnitaArboreaDichiarataVO unitaArboreaDichiarataVO) {
		this.unitaArboreaDichiarataVO = unitaArboreaDichiarataVO;
	}

	/**
	 * @return the particellaCertificataVO
	 */
	public ParticellaCertificataVO getParticellaCertificataVO() {
		return particellaCertificataVO;
	}

	/**
	 * @param particellaCertificataVO the particellaCertificataVO to set
	 */
	public void setParticellaCertificataVO(ParticellaCertificataVO particellaCertificataVO) {
		this.particellaCertificataVO = particellaCertificataVO;
	}

	
	/**
	 * @return the idFasciaFluviale
	 */
	public Long getIdFasciaFluviale() {
		return idFasciaFluviale;
	}

	/**
	 * @param idFasciaFluviale the idFasciaFluviale to set
	 */
	public void setIdFasciaFluviale(Long idFasciaFluviale) {
		this.idFasciaFluviale = idFasciaFluviale;
	}

	/**
	 * @return the fasciaFluviale
	 */
	public CodeDescription getFasciaFluviale() {
		return fasciaFluviale;
	}

	/**
	 * @param fasciaFluviale the fasciaFluviale to set
	 */
	public void setFasciaFluviale(CodeDescription fasciaFluviale) {
		this.fasciaFluviale = fasciaFluviale;
	}

	
	
	/**
	 * Metodo che mi restituisce la stringa che dovrò visualizzare come tooltip
	 * durante la paginazione dei terreni
	 * 
	 * @return java.lang.String
	 */
	public String getDescForTooltip() {
		String toolitip = "Comune: "+this.comuneParticellaVO.getDescom();
		if(Validator.isNotEmpty(this.sezione)) {
			toolitip += " Sez. "+this.sezione;
		}
		toolitip += " Fgl. "+this.foglio;
		if(Validator.isNotEmpty(this.particella)) {
			toolitip += " Part. "+this.particella;
		}
		if(Validator.isNotEmpty(this.subalterno)) {
			toolitip += " Sub. "+this.subalterno;
		}
		return toolitip;
	}
		
	/**
	 * Metodo che indica se è stato modificato un dato della sezione dati territoriali
	 * e che quindi stabilisce l'obbligatorietà della causale mod particella
	 * 
	 * @param obj
	 * @return boolean
	 */
	public boolean equalsDatiTerritoriali(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StoricoParticellaVO other = (StoricoParticellaVO) obj;
		if(flagCaptazionePozzi == null) 
		{
			if(!other.flagCaptazionePozzi.equalsIgnoreCase(SolmrConstants.FLAG_N)) 
			{
				return false;
			}
		} 
		else if(!flagCaptazionePozzi.equals(other.flagCaptazionePozzi)) {
			return false;
		}
		if(flagIrrigabile == null) {
			if(!other.flagIrrigabile.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
				return false;
			}
		}
		else if(!flagIrrigabile.equals(other.flagIrrigabile)) {
			return false;
		}
		/*if(idAreaA == null) {
			if (other.idAreaA != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.idAreaA)) {
				return false;
			}
			else if(idAreaA.compareTo(other.idAreaA) != 0) {
				return false;
			}
		}
		if(idAreaB == null) {
			if (other.idAreaB != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.idAreaB)) {
				return false;
			}
			else if(idAreaB.compareTo(other.idAreaB) != 0) {
				return false;
			}
		}
		if(idAreaC == null) {
			if (other.idAreaC != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.idAreaC)) {
				return false;
			}
			else if(idAreaC.compareTo(other.idAreaC) != 0) {
				return false;
			}
		}
		if(idAreaD == null) {
			if (other.idAreaD != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.idAreaD)) {
				return false;
			}
			else if(idAreaD.compareTo(other.idAreaD) != 0) {
				return false;
			}
		}
		if(idAreaM == null) {
      if (other.idAreaM != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idAreaM)) {
        return false;
      }
      else if(idAreaM.compareTo(other.idAreaM) != 0) {
        return false;
      }
    }*/
		if(idIrrigazione == null) {
			if (other.idIrrigazione != null) {
				return false;
			}
		} 
		else {
			if(other.idIrrigazione == null && idIrrigazione != null) {
				return false;
			}
			else {
				if(idIrrigazione.compareTo(other.idIrrigazione) != 0) {
					return false;
				}
			}
		}
		/*if(idPotenzialitaIrrigua == null) {
      if (other.idPotenzialitaIrrigua != null) {
        return false;
      }
    } 
    else {
      if(other.idPotenzialitaIrrigua == null && idPotenzialitaIrrigua != null) {
        return false;
      }
      else {
        if(idPotenzialitaIrrigua.compareTo(other.idPotenzialitaIrrigua) != 0) {
          return false;
        }
      }
    }*/
		if(idRotazioneColturale == null) {
      if (other.idRotazioneColturale != null) {
        return false;
      }
    } 
    else {
      if(other.idRotazioneColturale == null && idRotazioneColturale != null) {
        return false;
      }
      else {
        if(idRotazioneColturale.compareTo(other.idRotazioneColturale) != 0) {
          return false;
        }
      }
    }
		if(idTerrazzamento == null) {
      if (other.idTerrazzamento != null) {
        return false;
      }
    } 
    else {
      if(other.idTerrazzamento == null && idTerrazzamento != null) {
        return false;
      }
      else {
        if(idTerrazzamento.compareTo(other.idTerrazzamento) != 0) {
          return false;
        }
      }
    }
		/*if(idZonaAltimetrica == null) {
			if (other.idZonaAltimetrica != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.idZonaAltimetrica)) {
				return false;
			}
			else if(idZonaAltimetrica.compareTo(other.idZonaAltimetrica) != 0) {
				return false;
			}
		}*/
		if(idCasoParticolare == null) {
			if(other.idCasoParticolare != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.idCasoParticolare)) {
				return false;
			}
			else if(!idCasoParticolare.equals(other.idCasoParticolare)) {
				return false;
			}
		}
		if(idMetodoIrriguo == null) {
      if(other.idMetodoIrriguo != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idMetodoIrriguo)) {
        return false;
      }
      else if(!idMetodoIrriguo.equals(other.idMetodoIrriguo)) {
        return false;
      }
    }
		return true;
	}
	
	
	
	public boolean equalsDatiTerritorialiInserimento(Object obj) 
	{
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final StoricoParticellaVO other = (StoricoParticellaVO) obj;  
    
    // SUPERFICIE CATASTALE
    if(Validator.isEmpty(this.supCatastale)) {
      if(Validator.isNotEmpty(other.supCatastale)) {
        return false;
      }
    }
    else if (Validator.isNotEmpty(this.supCatastale))
    {
      if(Validator.isEmpty(other.supCatastale)) 
      {
        return false;
      }
      else if(!StringUtils.parseSuperficieField(this.supCatastale).equalsIgnoreCase(StringUtils.parseSuperficieField(other.supCatastale)))
        return false; 
    }    
    if(flagCaptazionePozzi == null) 
    {
      if(!other.flagCaptazionePozzi.equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        return false;
      }
    } 
    else if(!flagCaptazionePozzi.equals(other.flagCaptazionePozzi)) 
    {
      return false;
    }
    if(flagIrrigabile == null) {
      if(!other.flagIrrigabile.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
        return false;
      }
    }
    else if(!flagIrrigabile.equals(other.flagIrrigabile)) {
      return false;
    }
    if(idAreaA == null) {
      if (other.idAreaA != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idAreaA)) {
        return false;
      }
      else if(idAreaA.compareTo(other.idAreaA) != 0) {
        return false;
      }
    }
    if(idAreaB == null) {
      if (other.idAreaB != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idAreaB)) {
        return false;
      }
      else if(idAreaB.compareTo(other.idAreaB) != 0) {
        return false;
      }
    }
    if(idAreaC == null) {
      if (other.idAreaC != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idAreaC)) {
        return false;
      }
      else if(idAreaC.compareTo(other.idAreaC) != 0) {
        return false;
      }
    }
    if(idAreaD == null) {
      if (other.idAreaD != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idAreaD)) {
        return false;
      }
      else if(idAreaD.compareTo(other.idAreaD) != 0) {
        return false;
      }
    }
    if(idAreaM == null) {
      if (other.idAreaM != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idAreaM)) {
        return false;
      }
      else if(idAreaM.compareTo(other.idAreaM) != 0) {
        return false;
      }
    }
    if(idIrrigazione == null) {
      if (other.idIrrigazione != null) {
        return false;
      }
    } 
    else {
      if(other.idIrrigazione == null && idIrrigazione != null) {
        return false;
      }
      else {
        if(idIrrigazione.compareTo(other.idIrrigazione) != 0) {
          return false;
        }
      }
    }
    if(idPotenzialitaIrrigua == null) {
      if (other.idPotenzialitaIrrigua != null) {
        return false;
      }
    } 
    else {
      if(other.idPotenzialitaIrrigua == null && idPotenzialitaIrrigua != null) {
        return false;
      }
      else {
        if(idPotenzialitaIrrigua.compareTo(other.idPotenzialitaIrrigua) != 0) {
          return false;
        }
      }
    }
    if(idRotazioneColturale == null) {
      if (other.idRotazioneColturale != null) {
        return false;
      }
    } 
    else {
      if(other.idRotazioneColturale == null && idRotazioneColturale != null) {
        return false;
      }
      else {
        if(idRotazioneColturale.compareTo(other.idRotazioneColturale) != 0) {
          return false;
        }
      }
    }
    if(idTerrazzamento == null) {
      if (other.idTerrazzamento != null) {
        return false;
      }
    } 
    else {
      if(other.idTerrazzamento == null && idTerrazzamento != null) {
        return false;
      }
      else {
        if(idTerrazzamento.compareTo(other.idTerrazzamento) != 0) {
          return false;
        }
      }
    }
    if(idZonaAltimetrica == null) {
      if (other.idZonaAltimetrica != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idZonaAltimetrica)) {
        return false;
      }
      else if(idZonaAltimetrica.compareTo(other.idZonaAltimetrica) != 0) {
        return false;
      }
    }
    if(idCasoParticolare == null) {
      if(other.idCasoParticolare != null) {
        return false;
      }
    }
    else {
      if(!Validator.isNotEmpty(other.idCasoParticolare)) {
        return false;
      }
      else if(!idCasoParticolare.equals(other.idCasoParticolare)) {
        return false;
      }
    }
    return true;
  }
		
	/**
   * Metodo che indica se è stato modificato un dato della sezione dati territoriali
   * e che quindi stabilisce l'obbligatorietà della causale mod particella
   * 
   * @param obj
   * @return boolean
   */
  public boolean equalsDatiIrrigabilita(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final StoricoParticellaVO other = (StoricoParticellaVO) obj;
    
    if(flagIrrigabile == null) {
      if(!other.flagIrrigabile.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
        return false;
      }
    }
    else if(!flagIrrigabile.equals(other.flagIrrigabile)) {
      return false;
    }
    
    if(idIrrigazione == null) {
      if (other.idIrrigazione != null) {
        return false;
      }
    } 
    else {
      if(other.idIrrigazione == null && idIrrigazione != null) {
        return false;
      }
      else {
        if(idIrrigazione.compareTo(other.idIrrigazione) != 0) {
          return false;
        }
      }
    }
    
    return true;
  }
		
	/**
	 * Metodo per controllare se due particelle sono uguali in relazione alla loro chiave logica
	 * (comune,sezione,foglio,particella,subalterno) 
	 */	
	public boolean equalsKey(Object o) {
		StoricoParticellaVO other = (StoricoParticellaVO)o;
		if(this.istatComune == null) {
			if(other.istatComune != null) {
				return false;
			}
		}
		else {
			if(!this.istatComune.equalsIgnoreCase(other.istatComune)) {
				return false;
			}
		}
		if(this.sezione == null || "".equalsIgnoreCase(this.sezione)) {
			if(Validator.isNotEmpty(other.sezione)) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.sezione)) {
				return false;
			}
			if(!this.sezione.equalsIgnoreCase(other.sezione)) {
				return false;
			}
		}
		if(this.foglio == null) {
			if(other.foglio != null) {
				return false;
			}
		}
		else {
			if(!this.foglio.equalsIgnoreCase(other.foglio)) {
				return false;
			}
		}
		if(this.particella == null) {
			if(other.particella != null) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.particella)) {
				return false;
			}
			if(!this.particella.equalsIgnoreCase(other.particella)) {
				return false;
			}
		}
		if(this.subalterno == null || "".equalsIgnoreCase(this.subalterno)) {
			if(Validator.isNotEmpty(subalterno)) {
				return false;
			}
		}
		else {
			if(!Validator.isNotEmpty(other.subalterno)) {
				return false;
			}
			if(!this.subalterno.equalsIgnoreCase(other.subalterno)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Metodo che indica se è stato modificato un dato della sezione dati territoriali
	 * e che quindi stabilisce l'obbligatorietà della causale mod particella
	 * 
	 * @param obj
	 * @return boolean
	 */
	public boolean isNotModify(Object obj, HttpServletRequest request) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StoricoParticellaVO other = (StoricoParticellaVO) obj;
		if(request.getParameter("flagCaptazionePozzi") == null) {
			if(!other.flagCaptazionePozzi.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
				return false;
			}
		} 
		else if(!request.getParameter("flagCaptazionePozzi").equals(other.flagCaptazionePozzi)) {
			return false;
		}
		if(request.getParameter("flagIrrigabile") == null) {
			if(!other.flagIrrigabile.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
				return false;
			}
		}
		else if(!request.getParameter("flagIrrigabile").equals(other.flagIrrigabile)) {
			return false;
		}
		if(!Validator.isNotEmpty(request.getParameter("idIrrigazione"))) {
			if (other.idIrrigazione != null) {
				return false;
			}
		} 
		else {
			if(other.idIrrigazione == null && Validator.isNotEmpty(request.getParameter("idIrrigazione"))) {
				return false;
			}
			else {
				if(Long.decode(request.getParameter("idIrrigazione")).compareTo(other.idIrrigazione) != 0) {
					return false;
				}
			}
		}
		
		if(!Validator.isNotEmpty(request.getParameter("idRotazioneColturale"))) {
      if (other.idRotazioneColturale != null) {
        return false;
      }
    } 
    else {
      if(other.idRotazioneColturale == null && Validator.isNotEmpty(request.getParameter("idRotazioneColturale"))) {
        return false;
      }
      else {
        if(Long.decode(request.getParameter("idRotazioneColturale")).compareTo(other.idRotazioneColturale) != 0) {
          return false;
        }
      }
    }
		if(!Validator.isNotEmpty(request.getParameter("idTerrazzamento"))) {
      if (other.idTerrazzamento != null) {
        return false;
      }
    } 
    else {
      if(other.idTerrazzamento == null && Validator.isNotEmpty(request.getParameter("idTerrazzamento"))) {
        return false;
      }
      else {
        if(Long.decode(request.getParameter("idTerrazzamento")).compareTo(other.idTerrazzamento) != 0) {
          return false;
        }
      }
    }
		
		if(!Validator.isNotEmpty(request.getParameter("idMetodoIrriguo"))) {
      if (other.idMetodoIrriguo != null) {
        return false;
      }
    } 
    else {
      if(other.idMetodoIrriguo == null && Validator.isNotEmpty(request.getParameter("idMetodoIrriguo"))) {
        return false;
      }
      else {
        if(Long.decode(request.getParameter("idMetodoIrriguo")).compareTo(other.idMetodoIrriguo) != 0) {
          return false;
        }
      }
    }
		
		return true;
	}
	
	public ValidationErrors validateModificaTerritorialeCondUso(HttpServletRequest request, ValidationErrors errors) 
	{
		if(errors == null) 
		{
			errors = new ValidationErrors();
		}
			
			
		StoricoParticellaVO storicoParticellaConfrontoVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaConfrontoVO");
		
		
    
    // Il tipo rotazione coltura è obbligatorio
    if(request.getParameter("idRotazioneColturale").equalsIgnoreCase("-1")) {
      errors.add("idRotazioneColturale", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
    // Il tipo terrazzamento è obbligatorio
    if(request.getParameter("idTerrazzamento").equalsIgnoreCase("-1")) {
      errors.add("idTerrazzamento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }
		// Se non è stato modificato nessun dato dell'area territoriale il documento
		// non deve essere valorizzato
		if(this.isNotModify(storicoParticellaConfrontoVO, request)) 
		{
			if(Validator.isNotEmpty(request.getParameter("idDocumentoProtocollato"))) {
				errors.add("idDocumentoProtocollato", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE_FOR_MODIFICA_TERRITORIALE));
			}
			if(Validator.isNotEmpty(request.getParameter("idCausaleModParticella"))) {
				errors.add("idCausaleModParticella", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE_FOR_MODIFICA_TERRITORIALE));
			}
			if(Validator.isNotEmpty(request.getParameter("motivoModifica"))) {
				errors.add("motivoModifica", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE_FOR_MODIFICA_TERRITORIALE));
			}
		}
		else 
		{
			// Se invece è stato modificato un dato dell'area territoriale
			// è obbligatorio specificare la causale della modifica
			if(!Validator.isNotEmpty(request.getParameter("idCausaleModParticella"))) {
				errors.add("idCausaleModParticella", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
			}
			else {
				if(Validator.isNotEmpty(request.getParameter("motivoModifica"))) {
					if(request.getParameter("motivoModifica").length() > 200) {
						errors.add("motivoModifica", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
					}
				}
			}
		}
		return errors;			
	}

  public String getGisP30()
  {
    return gisP30;
  }

  public void setGisP30(String gisP30)
  {
    this.gisP30 = gisP30;
  }

  public String getGisP25()
  {
    return gisP25;
  }

  public void setGisP25(String gisP25)
  {
    this.gisP25 = gisP25;
  }

  public String getGisP26()
  {
    return gisP26;
  }

  public void setGisP26(String gisP26)
  {
    this.gisP26 = gisP26;
  }

  public boolean isCessaParticella()
  {
    return cessaParticella;
  }

  public void setCessaParticella(boolean cessaParticella)
  {
    this.cessaParticella = cessaParticella;
  }

  public String getBiologico()
  {
    return biologico;
  }

  public void setBiologico(String biologico)
  {
    this.biologico = biologico;
  }

  public Long getIdAreaI()
  {
    return idAreaI;
  }

  public void setIdAreaI(Long idAreaI)
  {
    this.idAreaI = idAreaI;
  }

  public CodeDescription getAreaI()
  {
    return areaI;
  }

  public void setAreaI(CodeDescription areaI)
  {
    this.areaI = areaI;
  }

  public Long getIdAreaL()
  {
    return idAreaL;
  }

  public void setIdAreaL(Long idAreaL)
  {
    this.idAreaL = idAreaL;
  }

  public CodeDescription getAreaL()
  {
    return areaL;
  }

  public void setAreaL(CodeDescription areaL)
  {
    this.areaL = areaL;
  }

  public Long getIstanzaRiesame()
  {
    return istanzaRiesame;
  }

  public void setIstanzaRiesame(Long istanzaRiesame)
  {
    this.istanzaRiesame = istanzaRiesame;
  }

  public Vector<IstanzaRiesameVO> getvIstanzaRiesame()
  {
    return vIstanzaRiesame;
  }

  public void setvIstanzaRiesame(Vector<IstanzaRiesameVO> vIstanzaRiesame)
  {
    this.vIstanzaRiesame = vIstanzaRiesame;
  }

  public Long getIdAreaM()
  {
    return idAreaM;
  }

  public void setIdAreaM(Long idAreaM)
  {
    this.idAreaM = idAreaM;
  }

  public CodeDescription getAreaM()
  {
    return areaM;
  }

  public void setAreaM(CodeDescription areaM)
  {
    this.areaM = areaM;
  }

  public BigDecimal getPercentualeUtilizzoEleg()
  {
    return percentualeUtilizzoEleg;
  }

  public void setPercentualeUtilizzoEleg(BigDecimal percentualeUtilizzoEleg)
  {
    this.percentualeUtilizzoEleg = percentualeUtilizzoEleg;
  }

  public String getInNotifica()
  {
    return inNotifica;
  }

  public void setInNotifica(String inNotifica)
  {
    this.inNotifica = inNotifica;
  }

  public Vector<ConduzioneDichiarataVO> getvConduzioniDichiarate()
  {
    return vConduzioniDichiarate;
  }

  public void setvConduzioniDichiarate(
      Vector<ConduzioneDichiarataVO> vConduzioniDichiarate)
  {
    this.vConduzioniDichiarate = vConduzioniDichiarate;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public BigDecimal getPercentualePendenzaMedia()
  {
    return percentualePendenzaMedia;
  }

  public void setPercentualePendenzaMedia(BigDecimal percentualePendenzaMedia)
  {
    this.percentualePendenzaMedia = percentualePendenzaMedia;
  }

  public BigDecimal getGradiPendenzaMedia()
  {
    return gradiPendenzaMedia;
  }

  public void setGradiPendenzaMedia(BigDecimal gradiPendenzaMedia)
  {
    this.gradiPendenzaMedia = gradiPendenzaMedia;
  }

  public BigDecimal getGradiEsposizioneMedia()
  {
    return gradiEsposizioneMedia;
  }

  public void setGradiEsposizioneMedia(BigDecimal gradiEsposizioneMedia)
  {
    this.gradiEsposizioneMedia = gradiEsposizioneMedia;
  }

  public BigDecimal getMetriAltitudineMedia()
  {
    return metriAltitudineMedia;
  }

  public void setMetriAltitudineMedia(BigDecimal metriAltitudineMedia)
  {
    this.metriAltitudineMedia = metriAltitudineMedia;
  }

  public Long getIdMetodoIrriguo()
  {
    return idMetodoIrriguo;
  }

  public void setIdMetodoIrriguo(Long idMetodoIrriguo)
  {
    this.idMetodoIrriguo = idMetodoIrriguo;
  }

  public TipoMetodoIrriguoVO getTipoMetodoIrriguo()
  {
    return tipoMetodoIrriguo;
  }

  public void setTipoMetodoIrriguo(TipoMetodoIrriguoVO tipoMetodoIrriguo)
  {
    this.tipoMetodoIrriguo = tipoMetodoIrriguo;
  }

  public Vector<TipoAreaVO> getvValoriTipoArea()
  {
    return vValoriTipoArea;
  }

  public void setvValoriTipoArea(Vector<TipoAreaVO> vValoriTipoArea)
  {
    this.vValoriTipoArea = vValoriTipoArea;
  }
  
    
    
    
    
    

}