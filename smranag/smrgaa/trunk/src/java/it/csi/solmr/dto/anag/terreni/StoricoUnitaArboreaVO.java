package it.csi.solmr.dto.anag.terreni;

import it.csi.smranag.smrgaa.dto.terreni.IsolaParcellaVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDestinazioneVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoDettaglioUsoVO;
import it.csi.smranag.smrgaa.dto.terreni.TipoQualitaUsoVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.util.StringUtils;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

/**
 * Classe che si occupa di mappare la tabella DB_STORICO_UNITA_ARBOREA
 * 
 * @author Mauro Vocale
 *
 */
public class StoricoUnitaArboreaVO implements Serializable {
	
	private static final long serialVersionUID = 3933939670040938444L;
	
	private Long idStoricoUnitaArborea = null;
	private Long idUnitaArborea = null;
	private Long idParticella = null;
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
	private String tipoImpianto = null;
	private String numeroCastagni = null;
	private String gruppo = null;
	private String ricaduta = null;
	private Long idGiacituraUnar = null;
	private Long idRocciaUnar = null;
	private Long idScheletroUnar = null;
	private Long idStatoVegetativoUnar = null;
	private Long idPotaturaUnar = null;
	private Long idGiudizioUnar = null;
	private String supplementari = null;
	private String meccanizzabile = null;
	private String dimensioneChioma = null;
	private Long idEtaImpiantoUnar = null;
	private String provinciaCCIAA = null;
	private String matricolaCCIAA = null;
	private String confermaPrecIscrizioneAlbo = null;
	private String richiestaNuovaIscrAlbo = null;
	private String confermaRichNuovaIscrAlbo = null;
	private String superficieDaIscrivereAlbo = null;
	private String annoIscrizioneAlbo = null;
	private Date dataIscrizioneAlbo = null;
	private Long idFonte = null;
	private CodeDescription tipoFonte = null;
	private Long idVariazioneUnar = null;
	private TipoVarietaUnitaArboreaVO tipoVarietaUnitaArboreaVO = null;
	private String note = null;
	private Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private Long idVarieta = null;
	private TipoVarietaVO tipoVarietaVO = null;
	private Long idUtilizzo = null;
	private TipoUtilizzoVO tipoUtilizzoVO = null;
	private String percentualeVarieta = null;
	private Long idVino = null;
	private TipoVinoVO tipoVinoVO = null;
	private Date dataEsecuzione = null;
	private String recordModificato = null;
	private String esitoControllo = null;
	private UtenteIrideVO utenteIrideVO = null;
	private Long idAzienda = null;
	private Date dataCessazione = null;
	private Long idCessazioneUnar = null;
	private TipoCessazioneUnarVO tipoCessazioneUnarVO = null;
	private Long idCausaleModifica = null;
	private TipoCausaleModificaVO tipoCausaleModificaVO = null;
	private String cuaa = null;
	private String denominazione = null;
	private String codFiscaleIntermediario = null;
	private String importabile = null;
	private Long idTipologiaVino = null;
	private TipoTipologiaVinoVO tipoTipologiaVinoVO = null;
	private AltroVitignoVO[] elencoAltriVitigni = null;
	private String statoUnitaArborea = null;
	private String annoRiferimento = null;
	private String colturaSpecializzata = null;
	private String annoPrimaProduzione = null;
	private String vigna = null;
	private Long idVigna = null;
	private Long idGenereIscrizione = null;
	private TipoGenereIscrizioneVO tipoGenereIscrizioneVO = null;
	private Date dataImpianto = null;
	private Date dataPrimaProduzione = null;
	private boolean bloccaModificaIdoneitaValida = false;
	private Vector<IsolaParcellaVO> vIsolaParcella = null;
	private Integer tolleranza = null;
	private String supPostVit = null;
	private String etichetta = null;
	private Long idMenzioneGeografica = null;
	private Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = null;
	private Date dataConsolidamentoGis = null;
	private BigDecimal percentualeFallanza;
	private String flagImproduttiva = "N";
	private TipoInterventoViticoloVO tipoInterventoViticoloVO = null;
	private Date dataIntervento = null;
	private Date dataSovrainnesto = null;
	private Long idTipoInterventoViticolo = null;
	private Long idFiloSostegno = null;
	private Long idPaloTestata = null;
	private Long idPaloTessitura = null;
	private Long idAncoraggioUnar = null;
	private Long idStatoAncoraggioUnar = null;
	private Long idStatoColtivazioneUnar = null;
	private Long distanzaPali = null;
	private Long altitudineSlm = null;
	private BigDecimal areaServizio = null;
	private String inNotifica = null;
	private VignaVO vignaVO = null;
	private Long idUnitaArboreaMadre = null;
	private Long idCatalogoMatrice;
	private Long idTipoDestinazione;
	private TipoDestinazioneVO tipoDestinazioneVO;
	private Long idTipoDettaglioUso;
	private TipoDettaglioUsoVO tipoDettaglioUsoVO;
	private Long idTipoQualitaUso;
	private TipoQualitaUsoVO tipoQualitaUsoVO;
	private BigDecimal percentualePendenzaMedia;
	private BigDecimal gradiPendenzaMedia;
	private BigDecimal gradiEsposizioneMedia;
	private Integer metriAltitudineMedia;

  public String getSupPostVit()
  {
    return supPostVit;
  }

  public void setSupPostVit(String supPostVit)
  {
    this.supPostVit = supPostVit;
  }

  public Integer getTolleranza()
  {
    return tolleranza;
  }

  public void setTolleranza(Integer tolleranza)
  {
    this.tolleranza = tolleranza;
  }

  public Vector<IsolaParcellaVO> getvIsolaParcella()
  {
    return vIsolaParcella;
  }

  public void setvIsolaParcella(Vector<IsolaParcellaVO> vIsolaParcella)
  {
    this.vIsolaParcella = vIsolaParcella;
  }

  public boolean isBloccaModificaIdoneitaValida()
  {
    return bloccaModificaIdoneitaValida;
  }

  public void setBloccaModificaIdoneitaValida(boolean bloccaModificaIdoneitaValida)
  {
    this.bloccaModificaIdoneitaValida = bloccaModificaIdoneitaValida;
  }
  
  

  public Date getDataPrimaProduzione()
  {
    return dataPrimaProduzione;
  }

  public void setDataPrimaProduzione(Date dataPrimaProduzione)
  {
    this.dataPrimaProduzione = dataPrimaProduzione;
  }

  //Compo introdotto per non mettere a null il campo note durante l'inserimento in una 
	//storicizzazione
	private boolean noNoteNullable;
	

	/**
	 * Costruttore di default
	 */
	public StoricoUnitaArboreaVO() {
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
	 * @return the idUnitaArborea
	 */
	public Long getIdUnitaArborea() {
		return idUnitaArborea;
	}

	/**
	 * @param idUnitaArborea the idUnitaArborea to set
	 */
	public void setIdUnitaArborea(Long idUnitaArborea) {
		this.idUnitaArborea = idUnitaArborea;
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
	public void setTipoFormaAllevamentoVO(TipoFormaAllevamentoVO tipoFormaAllevamentoVO) {
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
	public void setTipoIrrigazioneUnitaArboreaVO(TipoIrrigazioneUnitaArboreaVO tipoIrrigazioneUnitaArboreaVO) {
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
	public void setTipoColtivazioneUnitaArboreaVO(TipoColtivazioneUnitaArboreaVO tipoColtivazioneUnitaArboreaVO) {
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
   * @return the dataIscrizioneAlbo
   */
  public Date getDataIscrizioneAlbo() {
    return dataIscrizioneAlbo;
  }

  /**
   * @param dataIscrizioneAlbo the dataIscrizioneAlbo to set
   */
  public void setDataIscrizioneAlbo(Date dataIscrizioneAlbo) {
    this.dataIscrizioneAlbo = dataIscrizioneAlbo;
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
	 * @return the idVariazioneUnar
	 */
	public Long getIdVariazioneUnar() {
		return idVariazioneUnar;
	}

	/**
	 * @param idVariazioneUnar the idVariazioneUnar to set
	 */
	public void setIdVariazioneUnar(Long idVariazioneUnar) {
		this.idVariazioneUnar = idVariazioneUnar;
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
	 * @return the recordModificato
	 */
	public String getRecordModificato() {
		return recordModificato;
	}

	/**
	 * @param recordModificato the recordModificato to set
	 */
	public void setRecordModificato(String recordModificato) {
		this.recordModificato = recordModificato;
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
	 * @return the cuaa
	 */
	public String getCuaa() {
		return cuaa;
	}

	/**
	 * @param cuaa the cuaa to set
	 */
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	/**
	 * @return the denominazione
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @param denominazione the denominazione to set
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @return the codFiscaleIntermediario
	 */
	public String getCodFiscaleIntermediario() {
		return codFiscaleIntermediario;
	}

	/**
	 * @param codFiscaleIntermediario the codFiscaleIntermediario to set
	 */
	public void setCodFiscaleIntermediario(String codFiscaleIntermediario) {
		this.codFiscaleIntermediario = codFiscaleIntermediario;
	}

	/**
	 * @return the importabile
	 */
	public String getImportabile() {
		return importabile;
	}

	/**
	 * @param importabile the importabile to set
	 */
	public void setImportabile(String importabile) {
		this.importabile = importabile;
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
	 * @return the elencoAltriVitigni
	 */
	public AltroVitignoVO[] getElencoAltriVitigni() {
		return elencoAltriVitigni;
	}

	/**
	 * @param elencoAltriVitigni the elencoAltriVitigni to set
	 */
	public void setElencoAltriVitigni(AltroVitignoVO[] elencoAltriVitigni) {
		this.elencoAltriVitigni = elencoAltriVitigni;
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
	
	public Date getDataImpianto()
  {
    return dataImpianto;
  }

  public void setDataImpianto(Date dataImpianto)
  {
    this.dataImpianto = dataImpianto;
  }

  public boolean equalsForUpdate(Object obj) 
  {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StoricoUnitaArboreaVO other = (StoricoUnitaArboreaVO) obj;
		// Destinazione produttiva
		if(this.idUtilizzo == null) {
			if(other.idUtilizzo != null) {
				return false;
			}
		} 
		else if(!this.idUtilizzo.equals(other.idUtilizzo)) {
			return false;
		}
		// Vitigno
		if(this.idVarieta == null) {
			if(other.idVarieta != null) {
				return false;
			}
		} 
		else if(!this.idVarieta.equals(other.idVarieta)) {
			return false;
		}
		// Superficie vitata
		if(!Validator.isNotEmpty(this.area)) {
			if(other.area != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.area)) {
			if(this.area != null) {
				return false;
			}
		} 
		else if (!StringUtils.parseSuperficieField(this.area).equals(StringUtils.parseSuperficieField(other.area))) {
			return false;
		}
		// Data impianto
		if(!Validator.isNotEmpty(this.dataImpianto)) {
			if(other.dataImpianto != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.dataImpianto)) {
			if(this.dataImpianto != null) {
				return false;
			}
		}
		else if(!this.dataImpianto.equals(other.dataImpianto)) {
			return false;
		}
	  // Data prima produzione
    if(!Validator.isNotEmpty(this.dataPrimaProduzione)) {
      if(other.dataPrimaProduzione != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.dataPrimaProduzione)) {
      if(this.dataPrimaProduzione != null) {
        return false;
      }
    }
    else if(!this.dataPrimaProduzione.equals(other.dataPrimaProduzione)) {
      return false;
    }
		// Causale Modifica
    if(this.idCausaleModifica == null) {
      if(other.idCausaleModifica != null) {
        return false;
      }
    } 
    else if(!this.idCausaleModifica.equals(other.idCausaleModifica)) {
      return false;
    }
    //matricolaCCIAA
    if(this.matricolaCCIAA == null) {
      if(other.matricolaCCIAA != null) {
        return false;
      }
    } 
    else if(!this.matricolaCCIAA.equals(other.matricolaCCIAA)) {
      return false;
    }
    //anno iscrizione albo
    if(this.annoIscrizioneAlbo == null) {
      if(other.annoIscrizioneAlbo != null) {
        return false;
      }
    } 
    else if(!this.annoIscrizioneAlbo.equals(other.annoIscrizioneAlbo)) {
      return false;
    }
    // Tipologia Vino
    if(this.idTipologiaVino == null) {
      if(other.idTipologiaVino != null) {
        return false;
      }
    } 
    else if(!this.idTipologiaVino.equals(other.idTipologiaVino)) {
      return false;
    }
    // Vigna
    if(!Validator.isNotEmpty(this.vigna)) {
      if(other.vigna != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.vigna)) {
      if(this.vigna != null) {
        return false;
      }
    }
    else if(!this.vigna.equalsIgnoreCase(other.vigna)) {
      return false;
    }
    // idVigna
    if(this.idVigna == null) {
      if(other.idVigna != null) {
        return false;
      }
    } 
    else if(!this.idVigna.equals(other.idVigna)) {
      return false;
    }
    // idMenzioneGeografica
    if(this.idMenzioneGeografica == null) {
      if(other.idMenzioneGeografica != null) {
        return false;
      }
    } 
    else if(!this.idMenzioneGeografica.equals(other.idMenzioneGeografica)) {
      return false;
    }
    // Etichetta
    if(!Validator.isNotEmpty(this.etichetta)) {
      if(other.etichetta != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.etichetta)) {
      if(this.etichetta != null) {
        return false;
      }
    }
    else if(!this.etichetta.equalsIgnoreCase(other.etichetta)) {
      return false;
    }
    // percentualeFallanza
    if(this.percentualeFallanza == null) {
      if(other.percentualeFallanza != null) {
        return false;
      }
    }
    else if(other.percentualeFallanza == null) {
      if(this.percentualeFallanza != null) {
        return false;
      }
    }
    else if(this.percentualeFallanza.compareTo(other.percentualeFallanza) !=0) 
    {
      return false;
    }
    // flagImproduttiva (not null)
    if(this.flagImproduttiva == null) {
      if(other.flagImproduttiva != null) {
        return false;
      }
    }
    else if(other.flagImproduttiva == null) {
      if(this.flagImproduttiva != null) {
        return false;
      }
    }
    else if(!this.flagImproduttiva.equals(other.flagImproduttiva)) {
      return false;
    }
    
		return true;
	}
		
	/**
	 * Metodo per verificare se l'oggetto è stato modificato
	 * 
	 * @param obj
	 * @return boolean
	 */
	public boolean isEqual(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StoricoUnitaArboreaVO other = (StoricoUnitaArboreaVO) obj;
		// Destinazione produttiva
		if(this.idUtilizzo == null) {
			if(other.idUtilizzo != null) {
				return false;
			}
		}
		else if(other.idUtilizzo == null) {
			if(this.idUtilizzo != null) {
				return false;
			}
		}
		else if(!this.idUtilizzo.equals(other.idUtilizzo)) {
			return false;
		}
		// Vitigno
		if(this.idVarieta == null) {
			if(other.idVarieta != null) {
				return false;
			}
		}
		else if(other.idVarieta == null) {
			if(this.idVarieta != null) {
				return false;
			}
		} 
		else if(!this.idVarieta.equals(other.idVarieta)) {
			return false;
		}
		// Superficie vitata
		if(!Validator.isNotEmpty(this.area)) {
			if(other.area != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.area)) {
			if(this.area != null) {
				return false;
			}
		} 
		else if (!StringUtils.parseSuperficieField(this.area).equals(StringUtils.parseSuperficieField(other.area))) {
			return false;
		}
		// Data impianto
		if(!Validator.isNotEmpty(this.dataImpianto)) {
			if(other.dataImpianto != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.dataImpianto)) {
			if(this.dataImpianto != null) {
				return false;
			}
		}
		else if(!this.dataImpianto.equals(other.dataImpianto)) {
			return false;
		}
	  // Data prima produzione
    if(!Validator.isNotEmpty(this.dataPrimaProduzione)) {
      if(other.dataPrimaProduzione != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.dataPrimaProduzione)) {
      if(this.dataPrimaProduzione != null) {
        return false;
      }
    }
    else if(!this.dataPrimaProduzione.equals(other.dataPrimaProduzione)) {
      return false;
    }
		// Anno riferimento
		if(!Validator.isNotEmpty(this.annoRiferimento)) {
			if(other.annoRiferimento != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.annoRiferimento)) {
			if(this.annoRiferimento != null) {
				return false;
			}
		}
		else if(!this.annoRiferimento.equalsIgnoreCase(other.annoRiferimento)) {
			return false;
		}
		// Sesto su fila
		if(!Validator.isNotEmpty(this.sestoSuFila)) {
			if(other.sestoSuFila != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.sestoSuFila)) {
			if(this.sestoSuFila != null) {
				return false;
			}
		}
		else if(!this.sestoSuFila.equalsIgnoreCase(other.sestoSuFila)) {
			return false;
		}
		// Sesto tra file
		if(!Validator.isNotEmpty(this.sestoTraFile)) {
			if(other.sestoTraFile != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.sestoTraFile)) {
			if(this.sestoTraFile != null) {
				return false;
			}
		}
		else if(!this.sestoTraFile.equalsIgnoreCase(other.sestoTraFile)) {
			return false;
		}
		// Numero ceppi
		if(!Validator.isNotEmpty(this.numCeppi)) {
			if(other.numCeppi != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.numCeppi)) {
			if(this.numCeppi != null) {
				return false;
			}
		}
		else if(!this.numCeppi.equalsIgnoreCase(other.numCeppi)) {
			return false;
		}
		// Forma allevamento
		if(this.idFormaAllevamento == null) {
			if(other.idFormaAllevamento != null) {
				return false;
			}
		}
		else if(other.idFormaAllevamento == null) {
			if(this.idFormaAllevamento != null) {
				return false;
			}
		}
		else if(!this.idFormaAllevamento.equals(other.idFormaAllevamento)) {
			return false;
		}
		// % Vitigno
		if(!Validator.isNotEmpty(this.percentualeVarieta)) {
			if(other.percentualeVarieta != null) {
				return false;
			}
		}
		else if(!Validator.isNotEmpty(other.percentualeVarieta)) {
			if(this.percentualeVarieta != null) {
				return false;
			}
		}
		else if(!this.percentualeVarieta.equalsIgnoreCase(other.percentualeVarieta)) {
			return false;
		}
		// Ricaduta
		if(!this.ricaduta.equalsIgnoreCase(other.ricaduta)) {
			return false;
		}
		// Coltura specializzata
		if(!this.colturaSpecializzata.equalsIgnoreCase(other.colturaSpecializzata)) {
			return false;
		}
		// Altri vitigni
		if(!this.presenzaAltriVitigni.equalsIgnoreCase(other.presenzaAltriVitigni)) {
			return false;
		}
		// Elenco altri vitigni
		if(this.elencoAltriVitigni == null) {
			if(other.elencoAltriVitigni != null) {
				return false;
			}
		}
		else if(other.elencoAltriVitigni == null) {
			if(this.elencoAltriVitigni != null) {
				return false;
			}
		}
		else if(this.elencoAltriVitigni.length != other.elencoAltriVitigni.length) {
			return false;
		}
		else 
		{
			for(int i = 0; i < this.elencoAltriVitigni.length; i++) 
			{
				AltroVitignoVO thisAltro = (AltroVitignoVO)this.elencoAltriVitigni[i];
				AltroVitignoVO otherAltro = (AltroVitignoVO)other.elencoAltriVitigni[i];
				// Vitigno
				if(!thisAltro.getIdVarieta().equals(otherAltro.getIdVarieta())) 
				{
					return false;
				}
				// Percentuale vitigno
				if(!thisAltro.getPercentualeVitigno().equals(otherAltro.getPercentualeVitigno())) 
				{
					return false;
				}
			}
		}
		// Causale Modifica
    if(this.idCausaleModifica == null) {
      if(other.idCausaleModifica != null) {
        return false;
      }
    }
    else if(other.idCausaleModifica == null) {
      if(this.idCausaleModifica != null) {
        return false;
      }
    }
    else if(!this.idCausaleModifica.equals(other.idCausaleModifica)) {
      return false;
    }
		// ProvinciaCCIAA
    if(this.provinciaCCIAA == null) {
      if(other.provinciaCCIAA != null) {
        return false;
      }
    }
    else if(other.provinciaCCIAA == null) {
      if(this.provinciaCCIAA != null) {
        return false;
      }
    }
    else if(!this.provinciaCCIAA.equals(other.provinciaCCIAA)) {
      return false;
    }
    // matricolaCCIAA
    if(this.matricolaCCIAA == null) {
      if(other.matricolaCCIAA != null) {
        return false;
      }
    }
    else if(other.matricolaCCIAA == null) {
      if(this.matricolaCCIAA != null) {
        return false;
      }
    }
    else if(!this.matricolaCCIAA.equals(other.matricolaCCIAA)) {
      return false;
    }
    // Anno iscrizione Albo
    if(this.annoIscrizioneAlbo == null) {
      if(other.annoIscrizioneAlbo != null) {
        return false;
      }
    }
    else if(other.annoIscrizioneAlbo == null) {
      if(this.annoIscrizioneAlbo != null) {
        return false;
      }
    }
    else if(!this.annoIscrizioneAlbo.equals(other.annoIscrizioneAlbo)) {
      return false;
    }
    // Data iscrizione Albo
    if(this.dataIscrizioneAlbo == null) {
      if(other.dataIscrizioneAlbo != null) {
        return false;
      }
    }
    else if(other.dataIscrizioneAlbo == null) {
      if(this.dataIscrizioneAlbo != null) {
        return false;
      }
    }
    else if(!this.dataIscrizioneAlbo.equals(other.dataIscrizioneAlbo)) {
      return false;
    }
    // idTipologiaVino
    if(this.idTipologiaVino == null) {
      if(other.idTipologiaVino != null) {
        return false;
      }
    }
    else if(other.idTipologiaVino == null) {
      if(this.idTipologiaVino != null) {
        return false;
      }
    }
    else if(!this.idTipologiaVino.equals(other.idTipologiaVino)) {
      return false;
    }
    // Superficie da iscrivere albo
    if(!Validator.isNotEmpty(this.superficieDaIscrivereAlbo)) {
      if(other.superficieDaIscrivereAlbo != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.superficieDaIscrivereAlbo)) {
      if(this.superficieDaIscrivereAlbo != null) {
        return false;
      }
    } 
    else if (!StringUtils.parseSuperficieField(this.superficieDaIscrivereAlbo).equals(StringUtils.parseSuperficieField(other.superficieDaIscrivereAlbo))) {
      return false;
    }
    // Vigna
    if(!Validator.isNotEmpty(this.vigna)) {
      if(other.vigna != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.vigna)) {
      if(this.vigna != null) {
        return false;
      }
    }
    else if(!this.vigna.equalsIgnoreCase(other.vigna)) {
      return false;
    }
    // idVigna
    if(this.idVigna == null) {
      if(other.idVigna != null) {
        return false;
      }
    }
    else if(other.idVigna == null) {
      if(this.idVigna != null) {
        return false;
      }
    }
    else if(!this.idVigna.equals(other.idVigna)) {
      return false;
    }
    // idMenzioneGeografica
    if(this.idMenzioneGeografica == null) {
      if(other.idMenzioneGeografica != null) {
        return false;
      }
    }
    else if(other.idMenzioneGeografica == null) {
      if(this.idMenzioneGeografica != null) {
        return false;
      }
    }
    else if(!this.idMenzioneGeografica.equals(other.idMenzioneGeografica)) {
      return false;
    }
    // Etichetta
    if(!Validator.isNotEmpty(this.etichetta)) {
      if(other.etichetta != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.etichetta)) {
      if(this.etichetta != null) {
        return false;
      }
    }
    else if(!this.etichetta.equalsIgnoreCase(other.etichetta)) {
      return false;
    }
    // idGenereIscrizione
    if(!Validator.isNotEmpty(this.idGenereIscrizione)) {
      if(other.idGenereIscrizione != null) {
        return false;
      }
    }
    else if(!Validator.isNotEmpty(other.idGenereIscrizione)) {
      if(this.idGenereIscrizione != null) {
        return false;
      }
    }
    else if(!this.idGenereIscrizione.equals(other.idGenereIscrizione)) {
      return false;
    }
    //percentualeFallanza (not null)
    if(this.percentualeFallanza != other.percentualeFallanza) 
    {
      return false;
    }
    // flagImproduttiva (not null)
    if(this.flagImproduttiva == null) {
      if(other.flagImproduttiva != null) {
        return false;
      }
    }
    else if(other.flagImproduttiva == null) {
      if(this.flagImproduttiva != null) {
        return false;
      }
    }
    else if(!this.flagImproduttiva.equals(other.flagImproduttiva)) {
      return false;
    }
		return true;
	}

  public boolean isNoNoteNullable()
  {
    return noNoteNullable;
  }

  public void setNoNoteNullable(boolean noNoteNullable)
  {
    this.noNoteNullable = noNoteNullable;
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
    if(vigna != null)
    { 
      vigna = vigna.toUpperCase();
    }
    this.vigna = vigna;
  }
  
  
  public String getEtichetta()
  {
    return etichetta;
  }

  public void setEtichetta(String etichetta)
  {
    if(etichetta != null)
    { 
      etichetta = etichetta.toUpperCase();
    }
    this.etichetta = etichetta;
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

  public Long getIdGenereIscrizione()
  {
    return idGenereIscrizione;
  }

  public void setIdGenereIscrizione(Long idGenereIscrizione)
  {
    this.idGenereIscrizione = idGenereIscrizione;
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

  public Vector<TipoMenzioneGeograficaVO> getElencoMenzioneGeografica()
  {
    return elencoMenzioneGeografica;
  }

  public void setElencoMenzioneGeografica(
      Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica)
  {
    this.elencoMenzioneGeografica = elencoMenzioneGeografica;
  }

  public Date getDataConsolidamentoGis()
  {
    return dataConsolidamentoGis;
  }

  public void setDataConsolidamentoGis(Date dataConsolidamentoGis)
  {
    this.dataConsolidamentoGis = dataConsolidamentoGis;
  }

  public BigDecimal getPercentualeFallanza()
  {
    return percentualeFallanza;
  }

  public void setPercentualeFallanza(BigDecimal percentualeFallanza)
  {
    this.percentualeFallanza = percentualeFallanza;
  }

  public String getFlagImproduttiva()
  {
    return flagImproduttiva;
  }

  public void setFlagImproduttiva(String flagImproduttiva)
  {
    this.flagImproduttiva = flagImproduttiva;
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

  public Long getIdTipoInterventoViticolo()
  {
    return idTipoInterventoViticolo;
  }

  public void setIdTipoInterventoViticolo(Long idTipoInterventoViticolo)
  {
    this.idTipoInterventoViticolo = idTipoInterventoViticolo;
  }

  public Long getIdFiloSostegno()
  {
    return idFiloSostegno;
  }

  public void setIdFiloSostegno(Long idFiloSostegno)
  {
    this.idFiloSostegno = idFiloSostegno;
  }

  public Long getIdPaloTestata()
  {
    return idPaloTestata;
  }

  public void setIdPaloTestata(Long idPaloTestata)
  {
    this.idPaloTestata = idPaloTestata;
  }

  public Long getIdPaloTessitura()
  {
    return idPaloTessitura;
  }

  public void setIdPaloTessitura(Long idPaloTessitura)
  {
    this.idPaloTessitura = idPaloTessitura;
  }

  public Long getIdAncoraggioUnar()
  {
    return idAncoraggioUnar;
  }

  public void setIdAncoraggioUnar(Long idAncoraggioUnar)
  {
    this.idAncoraggioUnar = idAncoraggioUnar;
  }

  public Long getIdStatoAncoraggioUnar()
  {
    return idStatoAncoraggioUnar;
  }

  public void setIdStatoAncoraggioUnar(Long idStatoAncoraggioUnar)
  {
    this.idStatoAncoraggioUnar = idStatoAncoraggioUnar;
  }

  public Long getIdStatoColtivazioneUnar()
  {
    return idStatoColtivazioneUnar;
  }

  public void setIdStatoColtivazioneUnar(Long idStatoColtivazioneUnar)
  {
    this.idStatoColtivazioneUnar = idStatoColtivazioneUnar;
  }

  public Long getDistanzaPali()
  {
    return distanzaPali;
  }

  public void setDistanzaPali(Long distanzaPali)
  {
    this.distanzaPali = distanzaPali;
  }

  public Long getAltitudineSlm()
  {
    return altitudineSlm;
  }

  public void setAltitudineSlm(Long altitudineSlm)
  {
    this.altitudineSlm = altitudineSlm;
  }

  public BigDecimal getAreaServizio()
  {
    return areaServizio;
  }

  public void setAreaServizio(BigDecimal areaServizio)
  {
    this.areaServizio = areaServizio;
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
  
  public Long getIdUnitaArboreaMadre()
  {
    return idUnitaArboreaMadre;
  }

  public void setIdUnitaArboreaMadre(Long idUnitaArboreaMadre)
  {
    this.idUnitaArboreaMadre = idUnitaArboreaMadre;
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

  public Integer getMetriAltitudineMedia()
  {
    return metriAltitudineMedia;
  }

  public void setMetriAltitudineMedia(Integer metriAltitudineMedia)
  {
    this.metriAltitudineMedia = metriAltitudineMedia;
  }
    
    
	
}
