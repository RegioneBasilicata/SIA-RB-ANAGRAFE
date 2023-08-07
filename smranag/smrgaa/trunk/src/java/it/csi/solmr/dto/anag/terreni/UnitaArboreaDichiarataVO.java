package it.csi.solmr.dto.anag.terreni;

import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

/**
 * Classe che si occupa di mappare la tabella DB_UNITA_ARBOREA_DICHIARATA
 * 
 * @author Mauro Vocale
 *
 */
public class UnitaArboreaDichiarataVO implements Serializable {
	
	private static final long serialVersionUID = 3448396990096680537L;
	
	private Long idUnitaArboreaDichiarata = null;
	private String codiceFotografiaTerreni = null;
	private Long idStoricoUnitaArborea = null;
	private Long idStoricoParticella = null;
	private String progrUnar = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private Date dataLavorazione = null;
	private Long idTipologiaUnar = null;
	private TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO = null;
	private String area = null;
	private String sestoSuFila = null;
	private String sestoTraFile = null;
	private String numCeppi = null;
	private String annoImpianto = null;
	private String annoReinnesto = null;
	private Long idFormaAllevamento = null;
	private TipoFormaAllevamentoVO tipoFormaAllevamentoVO = null;
	private Long idIrrigazioneUnar = null;
	private TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO = null;
	private Long idColtivazioneUnar = null;
	private TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO = null;
	private String codiceTipoVarieta = null;
	private String presenzaAltriVitigni = null;
	private String numeroPianteProduttivo = null;
	private String numeroAltrePiante = null;
	private String campagna = null;
	private Long idTipologiaVigneto = null;
	private TipoTipologiaVignetoVO tipoTipologiaVignetoVO = null;
	private String tipoImpianto = null;
	private String numeroCastagni = null;
	private String gruppo = null;
	private String ricaduta = null;
	private Long idGiacituraUnar = null;
	private TipoGiacituraUnitaArboreaVO tipoGiacituraUnitaArboreaVO = null;
	private Long idRocciaUnar = null;
	private TipoRocciaUnitaArboreaVO tipoRocciaUnitaArboreaVO = null;
	private Long idScheletroUnar = null;
	private TipoScheletroUnitaArboreaVO tipoScheletroUnitaArboreaVO = null;
	private Long idStatoVegetativoUnar = null;
	private TipoStatoVegetativoUnitaArboreaVO tipoStatoVegetativoUnitaArboreaVO = null;
	private Long idPotaturaUnar = null;
	private TipoPotaturaUnitaArboreaVO tipoPotaturaUnitaArboreaVO = null;
	private Long idGiudizioUnar = null;
	private TipoGiudizioUnitaArboreaVO tipoGiudizioUnitaArboreaVO = null;
	private String supplementari = null;
	private String meccanizzabile = null;
	private String dimensioneChioma = null;
	private Long idEtaImpiantoUnar = null;
	private TipoEtaImpiantoUnitaArboreaVO tipoEtaImpiantoUnitaArboreaVO = null;
	private String provinciaCCIAA = null;
	private String matricolaCCIAA = null;
	private String confermaPrecIscrizioneAlbo = null;
	private String richiestaNuovaIscrAlbo = null;
	private String confermaRichNuovaIscrAlbo = null;
	private String superficieDaIscrivereAlbo = null;
	private String annoIscrizioneAlbo = null;
	private Long idFonte = null;
	private CodeDescription tipoFonte = null;
	private TipoVarietaUnitaArboreaVO tipoVarietaUnitaArboreaVO = null;
	private String note = null;
	private Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private Long idVarieta = null;
	private TipoVarietaVO tipoVarietaVO = null;
	private Long idUtilizzo = null;
	private TipoUtilizzoVO tipoUtilizzoVO = null;
	private Long idVino = null;
	private TipoVinoVO tipoVinoVO = null;
	private String percentualeVarieta = null;
	private Long idTipologiaVino = null;
	private TipoTipologiaVinoVO tipoTipologiaVinoVO = null;
	private Date dataCessazione = null;
	private Long idCessazioneUnar = null;
	private TipoCessazioneUnarVO tipoCessazioneUnarVO = null;
	private Long idCausaleModifica = null;
	private TipoCausaleModificaVO tipoCausaleModificaVO = null;
	private Date dataEsecuzione = null;
	private String esitoControllo = null;
	private UtenteIrideVO utenteIrideVO = null;
	private Long idAzienda = null;
	private String statoUnitaArborea = null;
	private String annoRiferimento = null;
	private String colturaSpecializzata = null;
	private String annoPrimaProduzione = null;
  private String vigna = null;
  private TipoGenereIscrizioneVO tipoGenereIscrizioneVO = null;
  private Date dataInserimentoDichiarazione = null;
  private Date dataImpianto = null;
  private Date dataPrimaProduzione = null;
  private Integer tolleranza = null;
  private String etichetta = null;
  private Long idVigna = null;
  private Long idMenzioneGeografica = null;
  private Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = null;
  private String flagImproduttiva = "N";
  private BigDecimal percentualeFallanza = null;
  private TipoInterventoViticoloVO tipoInterventoViticoloVO = null;
  private Date dataIntervento = null;
  private Date dataSovrainnesto = null;
  private String inNotifica = null;
  private VignaVO vignaVO = null;
  private Long idCatalogoMatrice;
  private Long idTipoDestinazione;
  private TipoDestinazioneVO tipoDestinazioneVO;
  private Long idTipoDettaglioUso;
  private TipoDettaglioUsoVO tipoDettaglioUsoVO;
  private Long idTipoQualitaUso;
  private TipoQualitaUsoVO tipoQualitaUsoVO;
	
  public Integer getTolleranza()
  {
    return tolleranza;
  }

  public void setTolleranza(Integer tolleranza)
  {
    this.tolleranza = tolleranza;
  }

  public Date getDataPrimaProduzione()
  {
    return dataPrimaProduzione;
  }

  public void setDataPrimaProduzione(Date dataPrimaProduzione)
  {
    this.dataPrimaProduzione = dataPrimaProduzione;
  }

  public Date getDataImpianto()
  {
    return dataImpianto;
  }

  public void setDataImpianto(Date dataImpianto)
  {
    this.dataImpianto = dataImpianto;
  }

  /**
	 * Costruttore di default
	 */
	public UnitaArboreaDichiarataVO() {
		super();
	}

	/**
	 * @return the idUnitaArboreaDichiarata
	 */
	public Long getIdUnitaArboreaDichiarata() {
		return idUnitaArboreaDichiarata;
	}

	/**
	 * @param idUnitaArboreaDichiarata the idUnitaArboreaDichiarata to set
	 */
	public void setIdUnitaArboreaDichiarata(Long idUnitaArboreaDichiarata) {
		this.idUnitaArboreaDichiarata = idUnitaArboreaDichiarata;
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
	 * @return the idStoricoUnitaArborea
	 */
	public Long getIdStoricoUnitaArborea() {
		return idStoricoUnitaArborea;
	}

	/**
	 * @param idStoricoUnitaArborea the idStoricoUnitaArborea to set
	 */
	public void setIdStoricoUnitaArborea(Long idStoricoUnitaArborea) {
		this.idStoricoUnitaArborea = idStoricoUnitaArborea;
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
	 * @return the progrUnar
	 */
	public String getProgrUnar() {
		return progrUnar;
	}

	/**
	 * @param progrUnar the progrUnar to set
	 */
	public void setProgrUnar(String progrUnar) {
		this.progrUnar = progrUnar;
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
	 * @return the dataLavorazione
	 */
	public Date getDataLavorazione() {
		return dataLavorazione;
	}

	/**
	 * @param dataLavorazione the dataLavorazione to set
	 */
	public void setDataLavorazione(Date dataLavorazione) {
		this.dataLavorazione = dataLavorazione;
	}

	/**
	 * @return the idTipologiaUnar
	 */
	public Long getIdTipologiaUnar() {
		return idTipologiaUnar;
	}

	/**
	 * @param idTipologiaUnar the idTipologiaUnar to set
	 */
	public void setIdTipologiaUnar(Long idTipologiaUnar) {
		this.idTipologiaUnar = idTipologiaUnar;
	}

	/**
	 * @return the tipoTipologiaUnitaArboreaVO
	 */
	public TipoTipologiaUnitaArboreaVO getTipoTipologiaUnitaArboreaVO() {
		return tipoTipologiaUnitaArboreaVO;
	}

	/**
	 * @param tipoTipologiaUnitaArboreaVO the tipoTipologiaUnitaArboreaVO to set
	 */
	public void setTipoTipologiaUnitaArboreaVO(
			TipoTipologiaUnitaArboreaVO tipoTipologiaUnitaArboreaVO) {
		this.tipoTipologiaUnitaArboreaVO = tipoTipologiaUnitaArboreaVO;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the sestoSuFila
	 */
	public String getSestoSuFila() {
		return sestoSuFila;
	}

	/**
	 * @param sestoSuFila the sestoSuFila to set
	 */
	public void setSestoSuFila(String sestoSuFila) {
		this.sestoSuFila = sestoSuFila;
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
	 * @return the numCeppi
	 */
	public String getNumCeppi() {
		return numCeppi;
	}

	/**
	 * @param numCeppi the numCeppi to set
	 */
	public void setNumCeppi(String numCeppi) {
		this.numCeppi = numCeppi;
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
	 * @return the annoReinnesto
	 */
	public String getAnnoReinnesto() {
		return annoReinnesto;
	}

	/**
	 * @param annoReinnesto the annoReinnesto to set
	 */
	public void setAnnoReinnesto(String annoReinnesto) {
		this.annoReinnesto = annoReinnesto;
	}

	/**
	 * @return the idFormaAllevamento
	 */
	public Long getIdFormaAllevamento() {
		return idFormaAllevamento;
	}

	/**
	 * @param idFormaAllevamento the idFormaAllevamento to set
	 */
	public void setIdFormaAllevamento(Long idFormaAllevamento) {
		this.idFormaAllevamento = idFormaAllevamento;
	}

	/**
	 * @return the tipoFormaAllevamentoVO
	 */
	public TipoFormaAllevamentoVO getTipoFormaAllevamentoVO() {
		return tipoFormaAllevamentoVO;
	}

	/**
	 * @param tipoFormaAllevamentoVO the tipoFormaAllevamentoVO to set
	 */
	public void setTipoFormaAllevamentoVO(
			TipoFormaAllevamentoVO tipoFormaAllevamentoVO) {
		this.tipoFormaAllevamentoVO = tipoFormaAllevamentoVO;
	}

	/**
	 * @return the idIrrigazioneUnar
	 */
	public Long getIdIrrigazioneUnar() {
		return idIrrigazioneUnar;
	}

	/**
	 * @param idIrrigazioneUnar the idIrrigazioneUnar to set
	 */
	public void setIdIrrigazioneUnar(Long idIrrigazioneUnar) {
		this.idIrrigazioneUnar = idIrrigazioneUnar;
	}

	/**
	 * @return the tipoIrrigazioneUnitaArboreaVO
	 */
	public TipoIrrigazioneUnitaArboreaVO getTipoIrrigazioneUnitaArboreaVO() {
		return tipoIrrigazioneUnitaArboreaVO;
	}

	/**
	 * @param tipoIrrigazioneUnitaArboreaVO the tipoIrrigazioneUnitaArboreaVO to set
	 */
	public void setTipoIrrigazioneUnitaArboreaVO(
			TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO) {
		this.tipoIrrigazioneUnitaArboreaVO = tipoIrrigazioneUnitaArboreaVO;
	}

	/**
	 * @return the idColtivazioneUnar
	 */
	public Long getIdColtivazioneUnar() {
		return idColtivazioneUnar;
	}

	/**
	 * @param idColtivazioneUnar the idColtivazioneUnar to set
	 */
	public void setIdColtivazioneUnar(Long idColtivazioneUnar) {
		this.idColtivazioneUnar = idColtivazioneUnar;
	}

	/**
	 * @return the tipoColtivazioneUnitaArboreaVO
	 */
	public TipoColtivazioneUnitaArboreaVO getTipoColtivazioneUnitaArboreaVO() {
		return tipoColtivazioneUnitaArboreaVO;
	}

	/**
	 * @param tipoColtivazioneUnitaArboreaVO the tipoColtivazioneUnitaArboreaVO to set
	 */
	public void setTipoColtivazioneUnitaArboreaVO(
			TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO) {
		this.tipoColtivazioneUnitaArboreaVO = tipoColtivazioneUnitaArboreaVO;
	}

	/**
	 * @return the codiceTipoVarieta
	 */
	public String getCodiceTipoVarieta() {
		return codiceTipoVarieta;
	}

	/**
	 * @param codiceTipoVarieta the codiceTipoVarieta to set
	 */
	public void setCodiceTipoVarieta(String codiceTipoVarieta) {
		this.codiceTipoVarieta = codiceTipoVarieta;
	}

	/**
	 * @return the presenzaAltriVitigni
	 */
	public String getPresenzaAltriVitigni() {
		return presenzaAltriVitigni;
	}

	/**
	 * @param presenzaAltriVitigni the presenzaAltriVitigni to set
	 */
	public void setPresenzaAltriVitigni(String presenzaAltriVitigni) {
		this.presenzaAltriVitigni = presenzaAltriVitigni;
	}

	/**
	 * @return the numeroPianteProduttivo
	 */
	public String getNumeroPianteProduttivo() {
		return numeroPianteProduttivo;
	}

	/**
	 * @param numeroPianteProduttivo the numeroPianteProduttivo to set
	 */
	public void setNumeroPianteProduttivo(String numeroPianteProduttivo) {
		this.numeroPianteProduttivo = numeroPianteProduttivo;
	}

	/**
	 * @return the numeroAltrePiante
	 */
	public String getNumeroAltrePiante() {
		return numeroAltrePiante;
	}

	/**
	 * @param numeroAltrePiante the numeroAltrePiante to set
	 */
	public void setNumeroAltrePiante(String numeroAltrePiante) {
		this.numeroAltrePiante = numeroAltrePiante;
	}

	/**
	 * @return the campagna
	 */
	public String getCampagna() {
		return campagna;
	}

	/**
	 * @param campagna the campagna to set
	 */
	public void setCampagna(String campagna) {
		this.campagna = campagna;
	}

	/**
	 * @return the idTipologiaVigneto
	 */
	public Long getIdTipologiaVigneto() {
		return idTipologiaVigneto;
	}

	/**
	 * @param idTipologiaVigneto the idTipologiaVigneto to set
	 */
	public void setIdTipologiaVigneto(Long idTipologiaVigneto) {
		this.idTipologiaVigneto = idTipologiaVigneto;
	}

	/**
	 * @return the tipoTipologiaVignetoVO
	 */
	public TipoTipologiaVignetoVO getTipoTipologiaVignetoVO() {
		return tipoTipologiaVignetoVO;
	}

	/**
	 * @param tipoTipologiaVignetoVO the tipoTipologiaVignetoVO to set
	 */
	public void setTipoTipologiaVignetoVO(
			TipoTipologiaVignetoVO tipoTipologiaVignetoVO) {
		this.tipoTipologiaVignetoVO = tipoTipologiaVignetoVO;
	}

	/**
	 * @return the tipoImpianto
	 */
	public String getTipoImpianto() {
		return tipoImpianto;
	}

	/**
	 * @param tipoImpianto the tipoImpianto to set
	 */
	public void setTipoImpianto(String tipoImpianto) {
		this.tipoImpianto = tipoImpianto;
	}

	/**
	 * @return the numeroCastagni
	 */
	public String getNumeroCastagni() {
		return numeroCastagni;
	}

	/**
	 * @param numeroCastagni the numeroCastagni to set
	 */
	public void setNumeroCastagni(String numeroCastagni) {
		this.numeroCastagni = numeroCastagni;
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
	 * @return the ricaduta
	 */
	public String getRicaduta() {
		return ricaduta;
	}

	/**
	 * @param ricaduta the ricaduta to set
	 */
	public void setRicaduta(String ricaduta) {
		this.ricaduta = ricaduta;
	}

	/**
	 * @return the idGiacituraUnar
	 */
	public Long getIdGiacituraUnar() {
		return idGiacituraUnar;
	}

	/**
	 * @param idGiacituraUnar the idGiacituraUnar to set
	 */
	public void setIdGiacituraUnar(Long idGiacituraUnar) {
		this.idGiacituraUnar = idGiacituraUnar;
	}

	/**
	 * @return the tipoGiacituraUnitaArboreaVO
	 */
	public TipoGiacituraUnitaArboreaVO getTipoGiacituraUnitaArboreaVO() {
		return tipoGiacituraUnitaArboreaVO;
	}

	/**
	 * @param tipoGiacituraUnitaArboreaVO the tipoGiacituraUnitaArboreaVO to set
	 */
	public void setTipoGiacituraUnitaArboreaVO(
			TipoGiacituraUnitaArboreaVO tipoGiacituraUnitaArboreaVO) {
		this.tipoGiacituraUnitaArboreaVO = tipoGiacituraUnitaArboreaVO;
	}

	/**
	 * @return the idRocciaUnar
	 */
	public Long getIdRocciaUnar() {
		return idRocciaUnar;
	}

	/**
	 * @param idRocciaUnar the idRocciaUnar to set
	 */
	public void setIdRocciaUnar(Long idRocciaUnar) {
		this.idRocciaUnar = idRocciaUnar;
	}

	/**
	 * @return the tipoRocciaUnitaArboreaVO
	 */
	public TipoRocciaUnitaArboreaVO getTipoRocciaUnitaArboreaVO() {
		return tipoRocciaUnitaArboreaVO;
	}

	/**
	 * @param tipoRocciaUnitaArboreaVO the tipoRocciaUnitaArboreaVO to set
	 */
	public void setTipoRocciaUnitaArboreaVO(
			TipoRocciaUnitaArboreaVO tipoRocciaUnitaArboreaVO) {
		this.tipoRocciaUnitaArboreaVO = tipoRocciaUnitaArboreaVO;
	}

	/**
	 * @return the idScheletroUnar
	 */
	public Long getIdScheletroUnar() {
		return idScheletroUnar;
	}

	/**
	 * @param idScheletroUnar the idScheletroUnar to set
	 */
	public void setIdScheletroUnar(Long idScheletroUnar) {
		this.idScheletroUnar = idScheletroUnar;
	}

	/**
	 * @return the tipoScheletroUnitaArboreaVO
	 */
	public TipoScheletroUnitaArboreaVO getTipoScheletroUnitaArboreaVO() {
		return tipoScheletroUnitaArboreaVO;
	}

	/**
	 * @param tipoScheletroUnitaArboreaVO the tipoScheletroUnitaArboreaVO to set
	 */
	public void setTipoScheletroUnitaArboreaVO(
			TipoScheletroUnitaArboreaVO tipoScheletroUnitaArboreaVO) {
		this.tipoScheletroUnitaArboreaVO = tipoScheletroUnitaArboreaVO;
	}

	/**
	 * @return the idStatoVegetativoUnar
	 */
	public Long getIdStatoVegetativoUnar() {
		return idStatoVegetativoUnar;
	}

	/**
	 * @param idStatoVegetativoUnar the idStatoVegetativoUnar to set
	 */
	public void setIdStatoVegetativoUnar(Long idStatoVegetativoUnar) {
		this.idStatoVegetativoUnar = idStatoVegetativoUnar;
	}

	/**
	 * @return the tipoStatoVegetativoUnitaArboreaVO
	 */
	public TipoStatoVegetativoUnitaArboreaVO getTipoStatoVegetativoUnitaArboreaVO() {
		return tipoStatoVegetativoUnitaArboreaVO;
	}

	/**
	 * @param tipoStatoVegetativoUnitaArboreaVO the tipoStatoVegetativoUnitaArboreaVO to set
	 */
	public void setTipoStatoVegetativoUnitaArboreaVO(
			TipoStatoVegetativoUnitaArboreaVO tipoStatoVegetativoUnitaArboreaVO) {
		this.tipoStatoVegetativoUnitaArboreaVO = tipoStatoVegetativoUnitaArboreaVO;
	}

	/**
	 * @return the idPotaturaUnar
	 */
	public Long getIdPotaturaUnar() {
		return idPotaturaUnar;
	}

	/**
	 * @param idPotaturaUnar the idPotaturaUnar to set
	 */
	public void setIdPotaturaUnar(Long idPotaturaUnar) {
		this.idPotaturaUnar = idPotaturaUnar;
	}

	/**
	 * @return the tipoPotaturaUnitaArboreaVO
	 */
	public TipoPotaturaUnitaArboreaVO getTipoPotaturaUnitaArboreaVO() {
		return tipoPotaturaUnitaArboreaVO;
	}

	/**
	 * @param tipoPotaturaUnitaArboreaVO the tipoPotaturaUnitaArboreaVO to set
	 */
	public void setTipoPotaturaUnitaArboreaVO(
			TipoPotaturaUnitaArboreaVO tipoPotaturaUnitaArboreaVO) {
		this.tipoPotaturaUnitaArboreaVO = tipoPotaturaUnitaArboreaVO;
	}

	/**
	 * @return the idGiudizioUnar
	 */
	public Long getIdGiudizioUnar() {
		return idGiudizioUnar;
	}

	/**
	 * @param idGiudizioUnar the idGiudizioUnar to set
	 */
	public void setIdGiudizioUnar(Long idGiudizioUnar) {
		this.idGiudizioUnar = idGiudizioUnar;
	}

	/**
	 * @return the tipoGiudizioUnitaArboreaVO
	 */
	public TipoGiudizioUnitaArboreaVO getTipoGiudizioUnitaArboreaVO() {
		return tipoGiudizioUnitaArboreaVO;
	}

	/**
	 * @param tipoGiudizioUnitaArboreaVO the tipoGiudizioUnitaArboreaVO to set
	 */
	public void setTipoGiudizioUnitaArboreaVO(
			TipoGiudizioUnitaArboreaVO tipoGiudizioUnitaArboreaVO) {
		this.tipoGiudizioUnitaArboreaVO = tipoGiudizioUnitaArboreaVO;
	}

	/**
	 * @return the supplementari
	 */
	public String getSupplementari() {
		return supplementari;
	}

	/**
	 * @param supplementari the supplementari to set
	 */
	public void setSupplementari(String supplementari) {
		this.supplementari = supplementari;
	}

	/**
	 * @return the meccanizzabile
	 */
	public String getMeccanizzabile() {
		return meccanizzabile;
	}

	/**
	 * @param meccanizzabile the meccanizzabile to set
	 */
	public void setMeccanizzabile(String meccanizzabile) {
		this.meccanizzabile = meccanizzabile;
	}

	/**
	 * @return the dimensioneChioma
	 */
	public String getDimensioneChioma() {
		return dimensioneChioma;
	}

	/**
	 * @param dimensioneChioma the dimensioneChioma to set
	 */
	public void setDimensioneChioma(String dimensioneChioma) {
		this.dimensioneChioma = dimensioneChioma;
	}

	/**
	 * @return the idEtaImpiantoUnar
	 */
	public Long getIdEtaImpiantoUnar() {
		return idEtaImpiantoUnar;
	}

	/**
	 * @param idEtaImpiantoUnar the idEtaImpiantoUnar to set
	 */
	public void setIdEtaImpiantoUnar(Long idEtaImpiantoUnar) {
		this.idEtaImpiantoUnar = idEtaImpiantoUnar;
	}

	/**
	 * @return the tipoEtaImpiantoUnitaArboreaVO
	 */
	public TipoEtaImpiantoUnitaArboreaVO getTipoEtaImpiantoUnitaArboreaVO() {
		return tipoEtaImpiantoUnitaArboreaVO;
	}

	/**
	 * @param tipoEtaImpiantoUnitaArboreaVO the tipoEtaImpiantoUnitaArboreaVO to set
	 */
	public void setTipoEtaImpiantoUnitaArboreaVO(
			TipoEtaImpiantoUnitaArboreaVO tipoEtaImpiantoUnitaArboreaVO) {
		this.tipoEtaImpiantoUnitaArboreaVO = tipoEtaImpiantoUnitaArboreaVO;
	}

	/**
	 * @return the provinciaCCIAA
	 */
	public String getProvinciaCCIAA() {
		return provinciaCCIAA;
	}

	/**
	 * @param provinciaCCIAA the provinciaCCIAA to set
	 */
	public void setProvinciaCCIAA(String provinciaCCIAA) {
		this.provinciaCCIAA = provinciaCCIAA;
	}

	/**
	 * @return the matricolaCCIAA
	 */
	public String getMatricolaCCIAA() {
		return matricolaCCIAA;
	}

	/**
	 * @param matricolaCCIAA the matricolaCCIAA to set
	 */
	public void setMatricolaCCIAA(String matricolaCCIAA) {
		this.matricolaCCIAA = matricolaCCIAA;
	}

	/**
	 * @return the confermaPrecIscrizioneAlbo
	 */
	public String getConfermaPrecIscrizioneAlbo() {
		return confermaPrecIscrizioneAlbo;
	}

	/**
	 * @param confermaPrecIscrizioneAlbo the confermaPrecIscrizioneAlbo to set
	 */
	public void setConfermaPrecIscrizioneAlbo(String confermaPrecIscrizioneAlbo) {
		this.confermaPrecIscrizioneAlbo = confermaPrecIscrizioneAlbo;
	}

	/**
	 * @return the richiestaNuovaIscrAlbo
	 */
	public String getRichiestaNuovaIscrAlbo() {
		return richiestaNuovaIscrAlbo;
	}

	/**
	 * @param richiestaNuovaIscrAlbo the richiestaNuovaIscrAlbo to set
	 */
	public void setRichiestaNuovaIscrAlbo(String richiestaNuovaIscrAlbo) {
		this.richiestaNuovaIscrAlbo = richiestaNuovaIscrAlbo;
	}

	/**
	 * @return the confermaRichNuovaIscrAlbo
	 */
	public String getConfermaRichNuovaIscrAlbo() {
		return confermaRichNuovaIscrAlbo;
	}

	/**
	 * @param confermaRichNuovaIscrAlbo the confermaRichNuovaIscrAlbo to set
	 */
	public void setConfermaRichNuovaIscrAlbo(String confermaRichNuovaIscrAlbo) {
		this.confermaRichNuovaIscrAlbo = confermaRichNuovaIscrAlbo;
	}

	/**
	 * @return the superficieDaIscrivereAlbo
	 */
	public String getSuperficieDaIscrivereAlbo() {
		return superficieDaIscrivereAlbo;
	}

	/**
	 * @param superficieDaIscrivereAlbo the superficieDaIscrivereAlbo to set
	 */
	public void setSuperficieDaIscrivereAlbo(String superficieDaIscrivereAlbo) {
		this.superficieDaIscrivereAlbo = superficieDaIscrivereAlbo;
	}

	/**
	 * @return the annoIscrizioneAlbo
	 */
	public String getAnnoIscrizioneAlbo() {
		return annoIscrizioneAlbo;
	}

	/**
	 * @param annoIscrizioneAlbo the annoIscrizioneAlbo to set
	 */
	public void setAnnoIscrizioneAlbo(String annoIscrizioneAlbo) {
		this.annoIscrizioneAlbo = annoIscrizioneAlbo;
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
	 * @return the tipoFonte
	 */
	public CodeDescription getTipoFonte() {
		return tipoFonte;
	}

	/**
	 * @param tipoFonte the tipoFonte to set
	 */
	public void setTipoFonte(CodeDescription tipoFonte) {
		this.tipoFonte = tipoFonte;
	}

	/**
	 * @return the tipoVarietaUnitaArboreaVO
	 */
	public TipoVarietaUnitaArboreaVO getTipoVarietaUnitaArboreaVO() {
		return tipoVarietaUnitaArboreaVO;
	}

	/**
	 * @param tipoVarietaUnitaArboreaVO the tipoVarietaUnitaArboreaVO to set
	 */
	public void setTipoVarietaUnitaArboreaVO(
			TipoVarietaUnitaArboreaVO tipoVarietaUnitaArboreaVO) {
		this.tipoVarietaUnitaArboreaVO = tipoVarietaUnitaArboreaVO;
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
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	/**
	 * @param dataAggiornamento the dataAggiornamento to set
	 */
	public void setDataAggiornamento(Date dataAggiornamento) {
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
	 * @return the idVino
	 */
	public Long getIdVino() {
		return idVino;
	}

	/**
	 * @param idVino the idVino to set
	 */
	public void setIdVino(Long idVino) {
		this.idVino = idVino;
	}

	/**
	 * @return the tipoVinoVO
	 */
	public TipoVinoVO getTipoVinoVO() {
		return tipoVinoVO;
	}

	/**
	 * @param tipoVinoVO the tipoVinoVO to set
	 */
	public void setTipoVinoVO(TipoVinoVO tipoVinoVO) {
		this.tipoVinoVO = tipoVinoVO;
	}

	/**
	 * @return the percentualeVarieta
	 */
	public String getPercentualeVarieta() {
		return percentualeVarieta;
	}

	/**
	 * @param percentualeVarieta the percentualeVarieta to set
	 */
	public void setPercentualeVarieta(String percentualeVarieta) {
		this.percentualeVarieta = percentualeVarieta;
	}

	/**
	 * @return the idTipologiaVino
	 */
	public Long getIdTipologiaVino() {
		return idTipologiaVino;
	}

	/**
	 * @param idTipologiaVino the idTipologiaVino to set
	 */
	public void setIdTipologiaVino(Long idTipologiaVino) {
		this.idTipologiaVino = idTipologiaVino;
	}

	/**
	 * @return the tipoTipologiaVinoVO
	 */
	public TipoTipologiaVinoVO getTipoTipologiaVinoVO() {
		return tipoTipologiaVinoVO;
	}

	/**
	 * @param tipoTipologiaVinoVO the tipoTipologiaVinoVO to set
	 */
	public void setTipoTipologiaVinoVO(TipoTipologiaVinoVO tipoTipologiaVinoVO) {
		this.tipoTipologiaVinoVO = tipoTipologiaVinoVO;
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
	 * @return the idCessazioneUnar
	 */
	public Long getIdCessazioneUnar() {
		return idCessazioneUnar;
	}

	/**
	 * @param idCessazioneUnar the idCessazioneUnar to set
	 */
	public void setIdCessazioneUnar(Long idCessazioneUnar) {
		this.idCessazioneUnar = idCessazioneUnar;
	}

	/**
	 * @return the tipoCessazioneUnarVO
	 */
	public TipoCessazioneUnarVO getTipoCessazioneUnarVO() {
		return tipoCessazioneUnarVO;
	}

	/**
	 * @param tipoCessazioneUnarVO the tipoCessazioneUnarVO to set
	 */
	public void setTipoCessazioneUnarVO(TipoCessazioneUnarVO tipoCessazioneUnarVO) {
		this.tipoCessazioneUnarVO = tipoCessazioneUnarVO;
	}

	/**
	 * @return the idCausaleModifica
	 */
	public Long getIdCausaleModifica() {
		return idCausaleModifica;
	}

	/**
	 * @param idCausaleModifica the idCausaleModifica to set
	 */
	public void setIdCausaleModifica(Long idCausaleModifica) {
		this.idCausaleModifica = idCausaleModifica;
	}

	/**
	 * @return the tipoCausaleModificaVO
	 */
	public TipoCausaleModificaVO getTipoCausaleModificaVO() {
		return tipoCausaleModificaVO;
	}

	/**
	 * @param tipoCausaleModificaVO the tipoCausaleModificaVO to set
	 */
	public void setTipoCausaleModificaVO(TipoCausaleModificaVO tipoCausaleModificaVO) {
		this.tipoCausaleModificaVO = tipoCausaleModificaVO;
	}

	/**
	 * @return the dataEsecuzione
	 */
	public Date getDataEsecuzione() {
		return dataEsecuzione;
	}

	/**
	 * @param dataEsecuzione the dataEsecuzione to set
	 */
	public void setDataEsecuzione(Date dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

	/**
	 * @return the esitoControllo
	 */
	public String getEsitoControllo() {
		return esitoControllo;
	}

	/**
	 * @param esitoControllo the esitoControllo to set
	 */
	public void setEsitoControllo(String esitoControllo) {
		this.esitoControllo = esitoControllo;
	}

	/**
	 * @return the utenteIrideVO
	 */
	public UtenteIrideVO getUtenteIrideVO() {
		return utenteIrideVO;
	}

	/**
	 * @param utenteIrideVO the utenteIrideVO to set
	 */
	public void setUtenteIrideVO(UtenteIrideVO utenteIrideVO) {
		this.utenteIrideVO = utenteIrideVO;
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
	 * @return the statoUnitaArborea
	 */
	public String getStatoUnitaArborea() {
		return statoUnitaArborea;
	}

	/**
	 * @param statoUnitaArborea the statoUnitaArborea to set
	 */
	public void setStatoUnitaArborea(String statoUnitaArborea) {
		this.statoUnitaArborea = statoUnitaArborea;
	}

	/**
	 * @return the annoRiferimento
	 */
	public String getAnnoRiferimento() {
		return annoRiferimento;
	}

	/**
	 * @param annoRiferimento the annoRiferimento to set
	 */
	public void setAnnoRiferimento(String annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	/**
	 * @return the colturaSpecializzata
	 */
	public String getColturaSpecializzata() {
		return colturaSpecializzata;
	}

	/**
	 * @param colturaSpecializzata the colturaSpecializzata to set
	 */
	public void setColturaSpecializzata(String colturaSpecializzata) {
		this.colturaSpecializzata = colturaSpecializzata;
	}
	
	public String getAnnoPrimaProduzione()
  {
    return annoPrimaProduzione;
  }

  public void setAnnoPrimaProduzione(String annoPrimaProduzione)
  {
    this.annoPrimaProduzione = annoPrimaProduzione;
  }

  public String getVigna()
  {
    return vigna;
  }

  public void setVigna(String vigna)
  {
    this.vigna = vigna;
  }

  public TipoGenereIscrizioneVO getTipoGenereIscrizioneVO()
  {
    return tipoGenereIscrizioneVO;
  }

  public void setTipoGenereIscrizioneVO(
      TipoGenereIscrizioneVO tipoGenereIscrizioneVO)
  {
    this.tipoGenereIscrizioneVO = tipoGenereIscrizioneVO;
  }

  public Date getDataInserimentoDichiarazione()
  {
    return dataInserimentoDichiarazione;
  }

  public void setDataInserimentoDichiarazione(Date dataInserimentoDichiarazione)
  {
    this.dataInserimentoDichiarazione = dataInserimentoDichiarazione;
  }

  public String getEtichetta()
  {
    return etichetta;
  }

  public void setEtichetta(String etichetta)
  {
    this.etichetta = etichetta;
  }

  public Vector<TipoMenzioneGeograficaVO> getElencoMenzioneGeografica()
  {
    return elencoMenzioneGeografica;
  }

  public void setElencoMenzioneGeografica(
      Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica)
  {
    this.elencoMenzioneGeografica = elencoMenzioneGeografica;
  }

  public Long getIdVigna()
  {
    return idVigna;
  }

  public void setIdVigna(Long idVigna)
  {
    this.idVigna = idVigna;
  }

  public Long getIdMenzioneGeografica()
  {
    return idMenzioneGeografica;
  }

  public void setIdMenzioneGeografica(Long idMenzioneGeografica)
  {
    this.idMenzioneGeografica = idMenzioneGeografica;
  }

  public String getFlagImproduttiva()
  {
    return flagImproduttiva;
  }

  public void setFlagImproduttiva(String flagImproduttiva)
  {
    this.flagImproduttiva = flagImproduttiva;
  }

  public BigDecimal getPercentualeFallanza()
  {
    return percentualeFallanza;
  }

  public void setPercentualeFallanza(BigDecimal percentualeFallanza)
  {
    this.percentualeFallanza = percentualeFallanza;
  }

  public TipoInterventoViticoloVO getTipoInterventoViticoloVO()
  {
    return tipoInterventoViticoloVO;
  }

  public void setTipoInterventoViticoloVO(
      TipoInterventoViticoloVO tipoInterventoViticoloVO)
  {
    this.tipoInterventoViticoloVO = tipoInterventoViticoloVO;
  }

  public Date getDataIntervento()
  {
    return dataIntervento;
  }

  public void setDataIntervento(Date dataIntervento)
  {
    this.dataIntervento = dataIntervento;
  }

  public Date getDataSovrainnesto()
  {
    return dataSovrainnesto;
  }

  public void setDataSovrainnesto(Date dataSovrainnesto)
  {
    this.dataSovrainnesto = dataSovrainnesto;
  }

  public String getInNotifica()
  {
    return inNotifica;
  }

  public void setInNotifica(String inNotifica)
  {
    this.inNotifica = inNotifica;
  }

  public VignaVO getVignaVO()
  {
    return vignaVO;
  }

  public void setVignaVO(VignaVO vignaVO)
  {
    this.vignaVO = vignaVO;
  }

  public Long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }

  public void setIdCatalogoMatrice(Long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }

  public Long getIdTipoDestinazione()
  {
    return idTipoDestinazione;
  }

  public void setIdTipoDestinazione(Long idTipoDestinazione)
  {
    this.idTipoDestinazione = idTipoDestinazione;
  }

  public TipoDestinazioneVO getTipoDestinazioneVO()
  {
    return tipoDestinazioneVO;
  }

  public void setTipoDestinazioneVO(TipoDestinazioneVO tipoDestinazioneVO)
  {
    this.tipoDestinazioneVO = tipoDestinazioneVO;
  }

  public Long getIdTipoDettaglioUso()
  {
    return idTipoDettaglioUso;
  }

  public void setIdTipoDettaglioUso(Long idTipoDettaglioUso)
  {
    this.idTipoDettaglioUso = idTipoDettaglioUso;
  }

  public TipoDettaglioUsoVO getTipoDettaglioUsoVO()
  {
    return tipoDettaglioUsoVO;
  }

  public void setTipoDettaglioUsoVO(TipoDettaglioUsoVO tipoDettaglioUsoVO)
  {
    this.tipoDettaglioUsoVO = tipoDettaglioUsoVO;
  }

  public Long getIdTipoQualitaUso()
  {
    return idTipoQualitaUso;
  }

  public void setIdTipoQualitaUso(Long idTipoQualitaUso)
  {
    this.idTipoQualitaUso = idTipoQualitaUso;
  }

  public TipoQualitaUsoVO getTipoQualitaUsoVO()
  {
    return tipoQualitaUsoVO;
  }

  public void setTipoQualitaUsoVO(TipoQualitaUsoVO tipoQualitaUsoVO)
  {
    this.tipoQualitaUsoVO = tipoQualitaUsoVO;
  }
  
  
  
	
}
