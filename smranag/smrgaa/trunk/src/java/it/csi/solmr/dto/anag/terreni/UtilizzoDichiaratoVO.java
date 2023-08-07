package it.csi.solmr.dto.anag.terreni;

import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoFaseAllevamentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPeriodoSeminaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoPraticaMantenimentoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoSeminaVO;
import it.csi.solmr.dto.UtenteIrideVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_UTILIZZO_DICHIARATO
 * @author Mauro Vocale
 *
 */
public class UtilizzoDichiaratoVO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 9160008544817126727L;
	
	private Long idUtilizzoDichiarato = null;
	private String codiceFotografiaTerreni = null;
	private Long idConduzioneDichiarata = null;
	private ConduzioneDichiarataVO conduzioneDichiarataVO = null;
	private String anno = null;
	private Long idUtilizzo = null;
	private TipoUtilizzoVO tipoUtilizzoVO = null;
	private String superficieUtilizzata = null;
	private String note = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private UtenteIrideVO utenteAggiornamento = null;	
	private Long idUtilizzoSecondario = null;
	private TipoUtilizzoVO tipoUtilizzoSecondarioVO = null;
	private String supUtilizzataSecondaria = null;
	private Long idVarieta = null;
	private TipoVarietaVO tipoVarietaVO = null;
	private Long idVarietaSecondaria = null;
	private TipoVarietaVO tipoVarietaSecondariaVO = null;
	private String annoImpianto = null;
	private Long idImpianto = null;
	private TipoImpiantoVO tipoImpiantoVO = null;
	private String sestoSuFile = null;
	private String sestoTraFile = null;
	private String numeroPianteCeppi = null;
	private UtilizzoConsociatoDichiaratoVO[] elencoUtilizziConsociatiDich = null;
	private TipoMacroUsoVO tipoMacroUsoVO = null;
	private BigDecimal supUtilizzataBg = null;
	private Long idTipoDettaglioUso = null;
	private Long idTipoDettaglioUsoSecondario = null;
	private TipoDettaglioUsoVO tipoDettaglioUso;
  private TipoDettaglioUsoVO tipoDettaglioUsoSecondario;
  private Long idTipoPeriodoSemina;
  private Long idTipoPeriodoSeminaSecondario;
  private TipoPeriodoSeminaVO tipoPeriodoSemina;
  private TipoPeriodoSeminaVO tipoPeriodoSeminaSecondario;
  private BigDecimal valoreOriginale;
  private BigDecimal valoreDopoConversione;
  private BigDecimal valoreDopoPonderazione;
  private Long idTipoEfa;
  private String dichiarabileEfa;
  private String descTipoEfaEfa;
  private String descUnitaMisuraEfa;
  
  private Long idCatalogoMatrice;
  private Long idCatalogoMatriceSecondario;
  private Long idSemina;
  private TipoSeminaVO tipoSemina;
  private Long idSeminaSecondario;
  private TipoSeminaVO tipoSeminaSecondario;
  private Long idPraticaMantenimento;
  private TipoPraticaMantenimentoVO tipoPraticaMantenimento;
  private Date dataInizioDestinazione;
  private Date dataFineDestinazione;
  private Date dataInizioDestinazioneSec;
  private Date dataFineDestinazioneSec;
  private Long idTipoDestinazione;
  private TipoDestinazioneVO tipoDestinazione;
  private Long idTipoDestinazioneSecondario;
  private TipoDestinazioneVO tipoDestinazioneSecondario;
  private Long idTipoQualitaUso;
  private TipoQualitaUsoVO tipoQualitaUso;
  private Long idTipoQualitaUsoSecondario;
  private TipoQualitaUsoVO tipoQualitaUsoSecondario;
  private Long idFaseAllevamento;
  private TipoFaseAllevamentoVO tipoFaseAllevamento;

	/**
	 * @return the idUtilizzoDichiarato
	 */
	public Long getIdUtilizzoDichiarato() {
		return idUtilizzoDichiarato;
	}

	/**
	 * @param idUtilizzoDichiarato the idUtilizzoDichiarato to set
	 */
	public void setIdUtilizzoDichiarato(Long idUtilizzoDichiarato) {
		this.idUtilizzoDichiarato = idUtilizzoDichiarato;
	}

	/**
	 * @return the codiceFotografiaTerreni
	 */
	public String getCodiceFotografiaTerreni() {
		return codiceFotografiaTerreni;
	}

	/**
	 * @param codiceFotografiaTerreni the codiceFotografiaTerreni to set
	 */
	public void setCodiceFotografiaTerreni(String codiceFotografiaTerreni) {
		this.codiceFotografiaTerreni = codiceFotografiaTerreni;
	}

	/**
	 * @return the idConduzioneDichiarata
	 */
	public Long getIdConduzioneDichiarata() {
		return idConduzioneDichiarata;
	}

	/**
	 * @param idConduzioneDichiarata the idConduzioneDichiarata to set
	 */
	public void setIdConduzioneDichiarata(Long idConduzioneDichiarata) {
		this.idConduzioneDichiarata = idConduzioneDichiarata;
	}

	/**
	 * @return the conduzioneDichiarataVO
	 */
	public ConduzioneDichiarataVO getConduzioneDichiarataVO() {
		return conduzioneDichiarataVO;
	}

	/**
	 * @param conduzioneDichiarataVO the conduzioneDichiarataVO to set
	 */
	public void setConduzioneDichiarataVO(
			ConduzioneDichiarataVO conduzioneDichiarataVO) {
		this.conduzioneDichiarataVO = conduzioneDichiarataVO;
	}

	/**
	 * @return the anno
	 */
	public String getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(String anno) {
		this.anno = anno;
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
	 * @return the tipoUtilizzoVO
	 */
	public TipoUtilizzoVO getTipoUtilizzoVO() {
		return tipoUtilizzoVO;
	}

	/**
	 * @param tipoUtilizzoVO the tipoUtilizzoVO to set
	 */
	public void setTipoUtilizzoVO(TipoUtilizzoVO tipoUtilizzoVO) {
		this.tipoUtilizzoVO = tipoUtilizzoVO;
	}

	/**
	 * @return the superficieUtilizzata
	 */
	public String getSuperficieUtilizzata() {
		return superficieUtilizzata;
	}

	/**
	 * @param superficieUtilizzata the superficieUtilizzata to set
	 */
	public void setSuperficieUtilizzata(String superficieUtilizzata) {
		this.superficieUtilizzata = superficieUtilizzata;
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
	 * @return the idUtilizzoSecondario
	 */
	public Long getIdUtilizzoSecondario() {
		return idUtilizzoSecondario;
	}

	/**
	 * @param idUtilizzoSecondario the idUtilizzoSecondario to set
	 */
	public void setIdUtilizzoSecondario(Long idUtilizzoSecondario) {
		this.idUtilizzoSecondario = idUtilizzoSecondario;
	}

	/**
	 * @return the tipoUtilizzoSecondarioVO
	 */
	public TipoUtilizzoVO getTipoUtilizzoSecondarioVO() {
		return tipoUtilizzoSecondarioVO;
	}

	/**
	 * @param tipoUtilizzoSecondarioVO the tipoUtilizzoSecondarioVO to set
	 */
	public void setTipoUtilizzoSecondarioVO(TipoUtilizzoVO tipoUtilizzoSecondarioVO) {
		this.tipoUtilizzoSecondarioVO = tipoUtilizzoSecondarioVO;
	}

	/**
	 * @return the supUtilizzataSecondaria
	 */
	public String getSupUtilizzataSecondaria() {
		return supUtilizzataSecondaria;
	}

	/**
	 * @param supUtilizzataSecondaria the supUtilizzataSecondaria to set
	 */
	public void setSupUtilizzataSecondaria(String supUtilizzataSecondaria) {
		this.supUtilizzataSecondaria = supUtilizzataSecondaria;
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
	 * @return the tipoVarietaVO
	 */
	public TipoVarietaVO getTipoVarietaVO() {
		return tipoVarietaVO;
	}

	/**
	 * @param tipoVarietaVO the tipoVarietaVO to set
	 */
	public void setTipoVarietaVO(TipoVarietaVO tipoVarietaVO) {
		this.tipoVarietaVO = tipoVarietaVO;
	}

	/**
	 * @return the idVarietaSecondaria
	 */
	public Long getIdVarietaSecondaria() {
		return idVarietaSecondaria;
	}

	/**
	 * @param idVarietaSecondaria the idVarietaSecondaria to set
	 */
	public void setIdVarietaSecondaria(Long idVarietaSecondaria) {
		this.idVarietaSecondaria = idVarietaSecondaria;
	}

	/**
	 * @return the tipoVarietaSecondariaVO
	 */
	public TipoVarietaVO getTipoVarietaSecondariaVO() {
		return tipoVarietaSecondariaVO;
	}

	/**
	 * @param tipoVarietaSecondariaVO the tipoVarietaSecondariaVO to set
	 */
	public void setTipoVarietaSecondariaVO(TipoVarietaVO tipoVarietaSecondariaVO) {
		this.tipoVarietaSecondariaVO = tipoVarietaSecondariaVO;
	}

	/**
	 * @return the annoImpianto
	 */
	public String getAnnoImpianto() {
		return annoImpianto;
	}

	/**
	 * @param annoImpianto the annoImpianto to set
	 */
	public void setAnnoImpianto(String annoImpianto) {
		this.annoImpianto = annoImpianto;
	}

	/**
	 * @return the idImpianto
	 */
	public Long getIdImpianto() {
		return idImpianto;
	}

	/**
	 * @param idImpianto the idImpianto to set
	 */
	public void setIdImpianto(Long idImpianto) {
		this.idImpianto = idImpianto;
	}

	/**
	 * @return the tipoImpiantoVO
	 */
	public TipoImpiantoVO getTipoImpiantoVO() {
		return tipoImpiantoVO;
	}

	/**
	 * @param tipoImpiantoVO the tipoImpiantoVO to set
	 */
	public void setTipoImpiantoVO(TipoImpiantoVO tipoImpiantoVO) {
		this.tipoImpiantoVO = tipoImpiantoVO;
	}

	/**
	 * @return the sestoSuFile
	 */
	public String getSestoSuFile() {
		return sestoSuFile;
	}

	/**
	 * @param sestoSuFile the sestoSuFile to set
	 */
	public void setSestoSuFile(String sestoSuFile) {
		this.sestoSuFile = sestoSuFile;
	}

	/**
	 * @return the sestoTraFile
	 */
	public String getSestoTraFile() {
		return sestoTraFile;
	}

	/**
	 * @param sestoTraFile the sestoTraFile to set
	 */
	public void setSestoTraFile(String sestoTraFile) {
		this.sestoTraFile = sestoTraFile;
	}

	/**
	 * @return the numeroPianteCeppi
	 */
	public String getNumeroPianteCeppi() {
		return numeroPianteCeppi;
	}

	/**
	 * @param numeroPianteCeppi the numeroPianteCeppi to set
	 */
	public void setNumeroPianteCeppi(String numeroPianteCeppi) {
		this.numeroPianteCeppi = numeroPianteCeppi;
	}

	/**
	 * @return the elencoUtilizziConsociatiDich
	 */
	public UtilizzoConsociatoDichiaratoVO[] getElencoUtilizziConsociatiDich() {
		return elencoUtilizziConsociatiDich;
	}

	/**
	 * @param elencoUtilizziConsociatiDich the elencoUtilizziConsociatiDich to set
	 */
	public void setElencoUtilizziConsociatiDich(
			UtilizzoConsociatoDichiaratoVO[] elencoUtilizziConsociatiDich) {
		this.elencoUtilizziConsociatiDich = elencoUtilizziConsociatiDich;
	}

	/**
	 * @return the tipoMacroUsoVO
	 */
	public TipoMacroUsoVO getTipoMacroUsoVO() {
		return tipoMacroUsoVO;
	}

	/**
	 * @param tipoMacroUsoVO the tipoMacroUsoVO to set
	 */
	public void setTipoMacroUsoVO(TipoMacroUsoVO tipoMacroUsoVO) {
		this.tipoMacroUsoVO = tipoMacroUsoVO;
	}


  public BigDecimal getSupUtilizzataBg()
  {
    return supUtilizzataBg;
  }

  public void setSupUtilizzataBg(BigDecimal supUtilizzataBg)
  {
    this.supUtilizzataBg = supUtilizzataBg;
  }

  public Long getIdTipoDettaglioUso()
  {
    return idTipoDettaglioUso;
  }

  public void setIdTipoDettaglioUso(Long idTipoDettaglioUso)
  {
    this.idTipoDettaglioUso = idTipoDettaglioUso;
  }

  public Long getIdTipoDettaglioUsoSecondario()
  {
    return idTipoDettaglioUsoSecondario;
  }

  public void setIdTipoDettaglioUsoSecondario(Long idTipoDettaglioUsoSecondario)
  {
    this.idTipoDettaglioUsoSecondario = idTipoDettaglioUsoSecondario;
  }

  public TipoDettaglioUsoVO getTipoDettaglioUso()
  {
    return tipoDettaglioUso;
  }

  public void setTipoDettaglioUso(TipoDettaglioUsoVO tipoDettaglioUso)
  {
    this.tipoDettaglioUso = tipoDettaglioUso;
  }

  public TipoDettaglioUsoVO getTipoDettaglioUsoSecondario()
  {
    return tipoDettaglioUsoSecondario;
  }

  public void setTipoDettaglioUsoSecondario(
      TipoDettaglioUsoVO tipoDettaglioUsoSecondario)
  {
    this.tipoDettaglioUsoSecondario = tipoDettaglioUsoSecondario;
  }

  public Long getIdTipoPeriodoSemina()
  {
    return idTipoPeriodoSemina;
  }

  public void setIdTipoPeriodoSemina(Long idTipoPeriodoSemina)
  {
    this.idTipoPeriodoSemina = idTipoPeriodoSemina;
  }

  public Long getIdTipoPeriodoSeminaSecondario()
  {
    return idTipoPeriodoSeminaSecondario;
  }

  public void setIdTipoPeriodoSeminaSecondario(Long idTipoPeriodoSeminaSecondario)
  {
    this.idTipoPeriodoSeminaSecondario = idTipoPeriodoSeminaSecondario;
  }

  public TipoPeriodoSeminaVO getTipoPeriodoSemina()
  {
    return tipoPeriodoSemina;
  }

  public void setTipoPeriodoSemina(TipoPeriodoSeminaVO tipoPeriodoSemina)
  {
    this.tipoPeriodoSemina = tipoPeriodoSemina;
  }

  public TipoPeriodoSeminaVO getTipoPeriodoSeminaSecondario()
  {
    return tipoPeriodoSeminaSecondario;
  }

  public void setTipoPeriodoSeminaSecondario(
      TipoPeriodoSeminaVO tipoPeriodoSeminaSecondario)
  {
    this.tipoPeriodoSeminaSecondario = tipoPeriodoSeminaSecondario;
  }

  public BigDecimal getValoreOriginale()
  {
    return valoreOriginale;
  }

  public void setValoreOriginale(BigDecimal valoreOriginale)
  {
    this.valoreOriginale = valoreOriginale;
  }

  public BigDecimal getValoreDopoConversione()
  {
    return valoreDopoConversione;
  }

  public void setValoreDopoConversione(BigDecimal valoreDopoConversione)
  {
    this.valoreDopoConversione = valoreDopoConversione;
  }

  public BigDecimal getValoreDopoPonderazione()
  {
    return valoreDopoPonderazione;
  }

  public void setValoreDopoPonderazione(BigDecimal valoreDopoPonderazione)
  {
    this.valoreDopoPonderazione = valoreDopoPonderazione;
  }

  public Long getIdTipoEfa()
  {
    return idTipoEfa;
  }

  public void setIdTipoEfa(Long idTipoEfa)
  {
    this.idTipoEfa = idTipoEfa;
  }

  public String getDichiarabileEfa()
  {
    return dichiarabileEfa;
  }

  public void setDichiarabileEfa(String dichiarabileEfa)
  {
    this.dichiarabileEfa = dichiarabileEfa;
  }

  public String getDescTipoEfaEfa()
  {
    return descTipoEfaEfa;
  }

  public void setDescTipoEfaEfa(String descTipoEfaEfa)
  {
    this.descTipoEfaEfa = descTipoEfaEfa;
  }

  public String getDescUnitaMisuraEfa()
  {
    return descUnitaMisuraEfa;
  }

  public void setDescUnitaMisuraEfa(String descUnitaMisuraEfa)
  {
    this.descUnitaMisuraEfa = descUnitaMisuraEfa;
  }

  public Long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }

  public void setIdCatalogoMatrice(Long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }

  public Long getIdCatalogoMatriceSecondario()
  {
    return idCatalogoMatriceSecondario;
  }

  public void setIdCatalogoMatriceSecondario(Long idCatalogoMatriceSecondario)
  {
    this.idCatalogoMatriceSecondario = idCatalogoMatriceSecondario;
  }

  public Long getIdSemina()
  {
    return idSemina;
  }

  public void setIdSemina(Long idSemina)
  {
    this.idSemina = idSemina;
  }

  public Long getIdSeminaSecondario()
  {
    return idSeminaSecondario;
  }

  public void setIdSeminaSecondario(Long idSeminaSecondario)
  {
    this.idSeminaSecondario = idSeminaSecondario;
  }

  public Long getIdPraticaMantenimento()
  {
    return idPraticaMantenimento;
  }

  public void setIdPraticaMantenimento(Long idPraticaMantenimento)
  {
    this.idPraticaMantenimento = idPraticaMantenimento;
  }

  public TipoPraticaMantenimentoVO getTipoPraticaMantenimento()
  {
    return tipoPraticaMantenimento;
  }

  public void setTipoPraticaMantenimento(
      TipoPraticaMantenimentoVO tipoPraticaMantenimento)
  {
    this.tipoPraticaMantenimento = tipoPraticaMantenimento;
  }

  public Date getDataInizioDestinazione()
  {
    return dataInizioDestinazione;
  }

  public void setDataInizioDestinazione(Date dataInizioDestinazione)
  {
    this.dataInizioDestinazione = dataInizioDestinazione;
  }

  public Date getDataFineDestinazione()
  {
    return dataFineDestinazione;
  }

  public void setDataFineDestinazione(Date dataFineDestinazione)
  {
    this.dataFineDestinazione = dataFineDestinazione;
  }

  public Date getDataInizioDestinazioneSec()
  {
    return dataInizioDestinazioneSec;
  }

  public void setDataInizioDestinazioneSec(Date dataInizioDestinazioneSec)
  {
    this.dataInizioDestinazioneSec = dataInizioDestinazioneSec;
  }

  public Date getDataFineDestinazioneSec()
  {
    return dataFineDestinazioneSec;
  }

  public void setDataFineDestinazioneSec(Date dataFineDestinazioneSec)
  {
    this.dataFineDestinazioneSec = dataFineDestinazioneSec;
  }

  public Long getIdTipoDestinazione()
  {
    return idTipoDestinazione;
  }

  public void setIdTipoDestinazione(Long idTipoDestinazione)
  {
    this.idTipoDestinazione = idTipoDestinazione;
  }

  public TipoDestinazioneVO getTipoDestinazione()
  {
    return tipoDestinazione;
  }

  public void setTipoDestinazione(TipoDestinazioneVO tipoDestinazione)
  {
    this.tipoDestinazione = tipoDestinazione;
  }

  public Long getIdTipoDestinazioneSecondario()
  {
    return idTipoDestinazioneSecondario;
  }

  public void setIdTipoDestinazioneSecondario(Long idTipoDestinazioneSecondario)
  {
    this.idTipoDestinazioneSecondario = idTipoDestinazioneSecondario;
  }

  public TipoDestinazioneVO getTipoDestinazioneSecondario()
  {
    return tipoDestinazioneSecondario;
  }

  public void setTipoDestinazioneSecondario(
      TipoDestinazioneVO tipoDestinazioneSecondario)
  {
    this.tipoDestinazioneSecondario = tipoDestinazioneSecondario;
  }

  public Long getIdTipoQualitaUso()
  {
    return idTipoQualitaUso;
  }

  public void setIdTipoQualitaUso(Long idTipoQualitaUso)
  {
    this.idTipoQualitaUso = idTipoQualitaUso;
  }

  public TipoQualitaUsoVO getTipoQualitaUso()
  {
    return tipoQualitaUso;
  }

  public void setTipoQualitaUso(TipoQualitaUsoVO tipoQualitaUso)
  {
    this.tipoQualitaUso = tipoQualitaUso;
  }

  public Long getIdTipoQualitaUsoSecondario()
  {
    return idTipoQualitaUsoSecondario;
  }

  public void setIdTipoQualitaUsoSecondario(Long idTipoQualitaUsoSecondario)
  {
    this.idTipoQualitaUsoSecondario = idTipoQualitaUsoSecondario;
  }

  public TipoQualitaUsoVO getTipoQualitaUsoSecondario()
  {
    return tipoQualitaUsoSecondario;
  }

  public void setTipoQualitaUsoSecondario(
      TipoQualitaUsoVO tipoQualitaUsoSecondario)
  {
    this.tipoQualitaUsoSecondario = tipoQualitaUsoSecondario;
  }

  public Long getIdFaseAllevamento()
  {
    return idFaseAllevamento;
  }

  public void setIdFaseAllevamento(Long idFaseAllevamento)
  {
    this.idFaseAllevamento = idFaseAllevamento;
  }

  public TipoFaseAllevamentoVO getTipoFaseAllevamento()
  {
    return tipoFaseAllevamento;
  }

  public void setTipoFaseAllevamento(TipoFaseAllevamentoVO tipoFaseAllevamento)
  {
    this.tipoFaseAllevamento = tipoFaseAllevamento;
  }

  public TipoSeminaVO getTipoSemina()
  {
    return tipoSemina;
  }

  public void setTipoSemina(TipoSeminaVO tipoSemina)
  {
    this.tipoSemina = tipoSemina;
  }

  public TipoSeminaVO getTipoSeminaSecondario()
  {
    return tipoSeminaSecondario;
  }

  public void setTipoSeminaSecondario(TipoSeminaVO tipoSeminaSecondario)
  {
    this.tipoSeminaSecondario = tipoSeminaSecondario;
  }
	
	
	
}