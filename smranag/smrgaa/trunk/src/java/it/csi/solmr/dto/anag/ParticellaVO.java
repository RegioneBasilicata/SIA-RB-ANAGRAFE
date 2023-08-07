package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.CodeDescription;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;

public class ParticellaVO implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = 7314841840037206872L;

	private Long idParticella = null;
	private Long idStoricoParticella = null;
	private String sezione = null;
	private String istatComuneParticella = null;
	private String descComuneParticella = null;
	private String siglaProvinciaParticella = null;
	private String descStatoEsteroParticella = null;
	private java.util.Date dataCreazione = null;
	private java.util.Date dataCessazione = null;
	private Long foglio = null;
	private java.util.Date dataFineValidita = null;
	private Long particella = null;
	private String supCatastale = null;
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
	private String subalterno = null;
	private Long idZonaAltimetrica = null;
	private String descrizioneZonaAltimetrica = null;
	private boolean flagIrrigabile = false;
	private Long idCasoParticolare = null;
	private String descrizioneCasoParticolare = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private String denominazioneUtenteAggiornamento = null;
	private String motivoModifica = null;
	private Long idUnitaProduttiva = null;
	private String descrizioneUnitaProduttiva = null;
	private boolean particellaProvvisoria = false;
	private java.util.Date dataInizioConduzione = null;
	private String strDataInizioConduzione = null;
	private Long idTitoloPossesso = null;
	private String descrizioneTitoloPossesso = null;
	private BigDecimal percentualePossesso = null;
	private String superficieCondotta = null;
	private String note = null;
	private Long idConduzioneParticella = null;
	private Long idEventoCreazione = null;
	private String descrizioneEventoCreazione = null;
	private java.util.Date dataInizioValidita = null;
	private String superficieUtilizzata = null;
	private boolean checked = false;
	private Long idUtilizzoParticella = null;
	private String descUtilizzoParticella = null;
	private boolean flagCaptazionePozzi = false;
	private boolean flagEstero = false;
	private boolean flagNoCatastale = false;
	private String flagSAU = null;
	private String flagContrattoOperazione;
	private String descrizioneProvinciaParticella;
	private Long idConduzioneDichiarata = null;
	private ContrattoVO contrattoVO = null;
	private ContrattoProprietariVO contrattoProprietariVO = null;
	private java.util.Date dataFineConduzione = null;
	private ParticellaUtilizzoVO particellaUtilizzoVO = null;
	private Long idDichiarazioneConsistenza = null;
	private String strFoglio = null;
	private String strParticella = null;
	private String tipiAreaA = null;
	private String tipiAreaB = null;
	private String tipiAreaC = null;
	private String tipiAreaD = null;
	private String tipiZonaAltimetrica = null;
	private String tipiCasoParticolare = null;
	private CodeDescription fonteDato = null;
	private CodeDescription causaleModParticella = null;
	private CodeDescription tipoDocumento = null;
	private Long idSegnalazione = null;
	private Long idIrrigazione = null;
  private String descrizioneIrrigazione = null;

	private boolean flagUtilizziTutti = false; // campo relativo a nessuna tabella su DB ma ad uso esclusivo di caso d'uso
	private boolean flagSingoloUtilizzo = false; // campo relativo a nessuna tabella su DB ma ad uso esclusivo di caso d'uso
	private boolean flagNoUtilizzi = false; // campo relativo a nessuna tabella su DB ma ad uso esclusivo di caso d'uso
  @SuppressWarnings("rawtypes")
  private Vector elencoProprietari = null; // vettore usato a scope application per inserire i record di DB_CONTRATTO_PROPRIETARI
	private String descrizioneSezione = null; // Attributo che mappa il campo DESCRIZIONE della tabella DB_SEZIONE
	private String zonaVulnerabileNitrati; // Attributo che serve per la gestione delle combo con HtmplUtil relativo al campo ID_TITOLO_POSSESSO su DB_CONDUZIONE_PARTICELLA
	private boolean isParticellaAttiva = false; // Attributo utilizzato per la gestione logica del dato.Non corrisponde a nessun campo del DB
	private boolean isContenzioso = false; // Attributo utilizzato per la gestione logica del dato.Non corrisponde a nessun campo del DB
	private boolean isSupero = false; // Attributo utilizzato per la gestione logica del dato.Non corrisponde a nessun campo del DB
	private Long idCausaleModParticella = null; // Attributo che mappa il campo ID_CAUSALE_MOD_PARTICELLA sulla tabella DB_STORICO_PARTICELLA
	private String descCausaleModParticella = null; // Attributo che mappa il campo DESCRIZIONE sulla tabella DB_TIPO_CAUSALE_MOD_PARTICELLA
	private String tipiCausaleModParticella = null; // Attributo che serve per la gestione delle combo con HtmplUtil relativo al campo ID_CAUSALE_MOD_PARTICELLA su DB_STORICO_PARTICELLA
	private String esitoControllo = null; // Campo ESITO_CONTROLLO su DB_CONDUZIONE_PARTICELLA
	private boolean flagStorico = false; // Attributo che gestisce il flag storico della pagina relativa alla ricerca delle particelle
	private boolean recordModificato;  //Indica se questo record è stato modificato o meno
	private String biologico = null;
	private String codUtilizzo;
	private String codVarieta;
	private String descVarieta;
	private String flagSchedario = null;
	private Long idAreaPsnFoglio = null;
	private String descAreaPsn = null;
  private String supAgronomico;
  private String superficieGrafica;
  private Long idPotenzaIrrigua = null;
  private Long idRotazioneColturale = null;
  private Long idTerrazzamento = null;
  private String prioritaLavorazione = null;
  private Long idAreaM = null;
  private String proprietari = null;
  private String codQualita = null;
  private String descDettaglioUso = null;
  
  private String descUtilizzoParticellaSecondaria = null;
  private String descVarietaSecondaria = null;
  private String descDettaglioUsoSecondario = null;
  private BigDecimal superficieUtilizzataSecondaria = null;
  
  private String descDestinazione = null;
  private String descDestinazioneSecondaria = null;
  private String descQualitaUso = null;
  private String descQualitaUsoSecondaria = null;
  private Vector<Long> vIdFonte;

  public Long getIdPotenzaIrrigua()
  {
    return idPotenzaIrrigua;
  }

  public void setIdPotenzaIrrigua(Long idPotenzaIrrigua)
  {
    this.idPotenzaIrrigua = idPotenzaIrrigua;
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

  //Usato nella stampa
  private String documento;

  
  
  
	public String getSuperficieGrafica()
  {
    return superficieGrafica;
  }

  public void setSuperficieGrafica(String superficieGrafica)
  {
    this.superficieGrafica = superficieGrafica;
  }

  /**
	 * @return the biologico
	 */
	public String getBiologico() {
		return biologico;
	}

	/**
	 * @param biologico the biologico to set
	 */
	public void setBiologico(String biologico) {
		this.biologico = biologico;
	}

	public ParticellaVO() {
	}

	public void setIdStoricoParticella(Long idStoricoParticella) {
		this.idStoricoParticella = idStoricoParticella;
	}
	public Long getIdStoricoParticella() {
		return idStoricoParticella;
	}
	public Long getIdParticella() {
		return idParticella;
	}
	public void setIdParticella(Long idParticella) {
		this.idParticella = idParticella;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getSezione() {
		return sezione;
	}
	public void setIstatComuneParticella(String istatComuneParticella) {
		this.istatComuneParticella = istatComuneParticella;
	}
	public String getIstatComuneParticella() {
		return istatComuneParticella;
	}
	public void setDescComuneParticella(String descComuneParticella) {
		this.descComuneParticella = descComuneParticella;
	}
	public String getDescComuneParticella() {
		return descComuneParticella;
	}
	public void setSiglaProvinciaParticella(String siglaProvinciaParticella) {
		this.siglaProvinciaParticella = siglaProvinciaParticella;
	}
	public String getSiglaProvinciaParticella() {
		return siglaProvinciaParticella;
	}
	public void setDescStatoEsteroParticella(String descStatoEsteroParticella) {
		this.descStatoEsteroParticella = descStatoEsteroParticella;
	}
	public String getDescStatoEsteroParticella() {
		return descStatoEsteroParticella;
	}
	public void setDataCreazione(java.util.Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public java.util.Date getDataCreazione() {
		return dataCreazione;
	}
	public void setFoglio(Long foglio) {
		this.foglio = foglio;
	}
	public Long getFoglio() {
		return foglio;
	}
	public void setDataFineValidita(java.util.Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	public java.util.Date getDataFineValidita() {
		return dataFineValidita;
	}
	public void setParticella(Long particella) {
		this.particella = particella;
	}
	public Long getParticella() {
		return particella;
	}
	public void setSupCatastale(String supCatastale) {
		this.supCatastale = supCatastale;
	}
	public String getSupCatastale() {
		return supCatastale;
	}
	public void setIdAreaA(Long idAreaA) {
		this.idAreaA = idAreaA;
	}
	public Long getIdAreaA() {
		return idAreaA;
	}
	public void setDescrizioneAreaA(String descrizioneAreaA) {
		this.descrizioneAreaA = descrizioneAreaA;
	}
	public String getDescrizioneAreaA() {
		return descrizioneAreaA;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getSubalterno() {
		return subalterno;
	}
	public void setIdZonaAltimetrica(Long idZonaAltimetrica) {
		this.idZonaAltimetrica = idZonaAltimetrica;
	}
	public Long getIdZonaAltimetrica() {
		return idZonaAltimetrica;
	}
	public void setDescrizioneZonaAltimetrica(String descrizioneZonaAltimetrica) {
		this.descrizioneZonaAltimetrica = descrizioneZonaAltimetrica;
	}
	public String getDescrizioneZonaAltimetrica() {
		return descrizioneZonaAltimetrica;
	}
	public void setFlagIrrigabile(boolean flagIrrigabile) {
		this.flagIrrigabile = flagIrrigabile;
	}
	public boolean getFlagIrrigabile() {
		return flagIrrigabile;
	}
	public void setIdCasoParticolare(Long idCasoParticolare) {
		this.idCasoParticolare = idCasoParticolare;
	}
	public Long getIdCasoParticolare() {
		return idCasoParticolare;
	}
	public void setDescrizioneCasoParticolare(String descrizioneCasoParticolare) {
		this.descrizioneCasoParticolare = descrizioneCasoParticolare;
	}
	public String getDescrizioneCasoParticolare() {
		return descrizioneCasoParticolare;
	}
	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
		this.idUtenteAggiornamento = idUtenteAggiornamento;
	}
	public Long getIdUtenteAggiornamento() {
		return idUtenteAggiornamento;
	}
	public void setDenominazioneUtenteAggiornamento(String denominazioneUtenteAggiornamento) {
		this.denominazioneUtenteAggiornamento = denominazioneUtenteAggiornamento;
	}
	public String getDenominazioneUtenteAggiornamento() {
		return denominazioneUtenteAggiornamento;
	}
	public void setMotivoModifica(String motivoModifica) {
		this.motivoModifica = motivoModifica;
	}
	public String getMotivoModifica() {
		return motivoModifica;
	}
	public String getDescrizioneTitoloPossesso() {
		return descrizioneTitoloPossesso;
	}
	public void setDescrizioneTitoloPossesso(String descrizioneTitoloPossesso) {
		this.descrizioneTitoloPossesso = descrizioneTitoloPossesso;
	}
	public String getSuperficieUtilizzata() {
		return superficieUtilizzata;
	}
	public void setSuperficieUtilizzata(String superficieUtilizzata) {
		this.superficieUtilizzata = superficieUtilizzata;
	}
	public Long getIdUtilizzoParticella() {
		return idUtilizzoParticella;
	}
	public void setIdUtilizzoParticella(Long idUtilizzoParticella) {
		this.idUtilizzoParticella = idUtilizzoParticella;
	}
	public Long getIdUnitaProduttiva() {
		return idUnitaProduttiva;
	}
	public void setIdUnitaProduttiva(Long idUnitaProduttiva) {
		this.idUnitaProduttiva = idUnitaProduttiva;
	}
	public boolean getParticellaProvvisoria() {
		return particellaProvvisoria;
	}
	public void setParticellaProvvisoria(boolean particellaProvvisoria) {
		this.particellaProvvisoria = particellaProvvisoria;
	}

	public String getDescrizioneUnitaProduttiva() {
		return descrizioneUnitaProduttiva;
	}
	public void setDescrizioneUnitaProduttiva(String descrizioneUnitaProduttiva) {
		this.descrizioneUnitaProduttiva = descrizioneUnitaProduttiva;
	}
	public java.util.Date getDataInizioConduzione() {
		return dataInizioConduzione;
	}
	public void setDataInizioConduzione(java.util.Date dataInizioConduzione) {
		this.dataInizioConduzione = dataInizioConduzione;
	}
	public Long getIdTitoloPossesso() {
		return idTitoloPossesso;
	}
	public void setIdTitoloPossesso(Long idTitoloPossesso) {
		this.idTitoloPossesso = idTitoloPossesso;
	}
	public String getStrDataInizioConduzione() {
		return strDataInizioConduzione;
	}
	public void setStrDataInizioConduzione(String strDataInizioConduzione) {
		this.strDataInizioConduzione = strDataInizioConduzione;
	}
	public String getSuperficieCondotta() {
		return superficieCondotta;
	}
	public void setSuperficieCondotta(String superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}
	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
	}

	public java.util.Date getDataCessazione() {
		return dataCessazione;
	}
	public void setDataCessazione(java.util.Date dataCessazione) {
		this.dataCessazione = dataCessazione;
	}
	public Long getIdEventoCreazione() {
		return idEventoCreazione;
	}
	public void setIdEventoCreazione(Long idEventoCreazione) {
		this.idEventoCreazione = idEventoCreazione;
	}
	public String getDescrizioneEventoCreazione() {
		return descrizioneEventoCreazione;
	}
	public void setDescrizioneEventoCreazione(String descrizioneEventoCreazione) {
		this.descrizioneEventoCreazione = descrizioneEventoCreazione;
	}
	public java.util.Date getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(java.util.Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getDescrizioneAreaB() {
		return descrizioneAreaB;
	}
	public void setDescrizioneAreaB(String descrizioneAreaB) {
		this.descrizioneAreaB = descrizioneAreaB;
	}
	public void setDescrizioneAreaC(String descrizioneAreaC) {
		this.descrizioneAreaC = descrizioneAreaC;
	}
	public String getDescrizioneAreaC() {
		return descrizioneAreaC;
	}
	public String getDescrizioneAreaD() {
		return descrizioneAreaD;
	}
	public void setDescrizioneAreaD(String descrizioneAreaD) {
		this.descrizioneAreaD = descrizioneAreaD;
	}
	public Long getIdAreaB() {
		return idAreaB;
	}
	public void setIdAreaB(Long idAreaB) {
		this.idAreaB = idAreaB;
	}
	public void setIdAreaC(Long idAreaC) {
		this.idAreaC = idAreaC;
	}
	public Long getIdAreaC() {
		return idAreaC;
	}
	public Long getIdAreaD() {
		return idAreaD;
	}
	public void setIdAreaD(Long idAreaD) {
		this.idAreaD = idAreaD;
	}
	public boolean getFlagCaptazionePozzi() {
		return flagCaptazionePozzi;
	}
	public void setFlagCaptazionePozzi(boolean flagCaptazionePozzi) {
		this.flagCaptazionePozzi = flagCaptazionePozzi;
	}
	public String getDescrizioneAreaE() {
		return descrizioneAreaE;
	}
	public void setDescrizioneAreaE(String descrizioneAreaE) {
		this.descrizioneAreaE = descrizioneAreaE;
	}
	public Long getIdAreaE() {
		return idAreaE;
	}
	public void setIdAreaE(Long idAreaE) {
		this.idAreaE = idAreaE;
	}
	public boolean getFlagEstero() {
		return flagEstero;
	}
	public void setFlagEstero(boolean flagEstero) {
		this.flagEstero = flagEstero;
	}
	public boolean getFlagNoCatastale() {
		return flagNoCatastale;
	}
	public void setFlagNoCatastale(boolean flagNoCatastale) {
		this.flagNoCatastale = flagNoCatastale;
	}
	public String getDescUtilizzoParticella() {
		return descUtilizzoParticella;
	}
	public void setDescUtilizzoParticella(String descUtilizzoParticella) {
		this.descUtilizzoParticella = descUtilizzoParticella;
	}
	public String getFlagSAU() {
		return flagSAU;
	}
	public void setFlagSAU(String flagSAU) {
		this.flagSAU = flagSAU;
	}
	public Long getIdConduzioneDichiarata() {
		return idConduzioneDichiarata;
	}
	public void setIdConduzioneDichiarata(Long idConduzioneDichiarata) {
		this.idConduzioneDichiarata = idConduzioneDichiarata;
	}
	public ContrattoProprietariVO getContrattoProprietariVO() {
		return contrattoProprietariVO;
	}
	public void setContrattoProprietariVO(ContrattoProprietariVO contrattoProprietariVO) {
		this.contrattoProprietariVO = contrattoProprietariVO;
	}
	public ContrattoVO getContrattoVO() {
		return contrattoVO;
	}
	public void setContrattoVO(ContrattoVO contrattoVO) {
		this.contrattoVO = contrattoVO;
	}
	public java.util.Date getDataFineConduzione() {
		return dataFineConduzione;
	}
	public void setDataFineConduzione(java.util.Date dataFineConduzione) {
		this.dataFineConduzione = dataFineConduzione;
	}
	public ParticellaUtilizzoVO getParticellaUtilizzoVO() {
		return particellaUtilizzoVO;
	}
	public void setParticellaUtilizzoVO(ParticellaUtilizzoVO particellaUtilizzoVO) {
		this.particellaUtilizzoVO = particellaUtilizzoVO;
	}
	public Long getIdDichiarazioneConsistenza() {
		return idDichiarazioneConsistenza;
	}
	public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza) {
		this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
	}
	public boolean getFlagUtilizziTutti() {
		return flagUtilizziTutti;
	}
	public void setFlagUtilizziTutti(boolean flagUtilizziTutti) {
		this.flagUtilizziTutti = flagUtilizziTutti;
	}
	public boolean getFlagSingoloUtilizzo() {
		return flagSingoloUtilizzo;
	}
	public void setFlagSingoloUtilizzo(boolean flagSingoloUtilizzo) {
		this.flagSingoloUtilizzo = flagSingoloUtilizzo;
	}
	public boolean getFlagNoUtilizzi() {
		return flagNoUtilizzi;
	}
	public void setFlagNoUtilizzi(boolean flagNoUtilizzi) {
		this.flagNoUtilizzi = flagNoUtilizzi;
	}
	
  @SuppressWarnings("rawtypes")
  public Vector getElencoProprietari() {
		return elencoProprietari;
	}
	
  @SuppressWarnings("rawtypes")
  public void setElencoProprietari(Vector elencoProprietari) {
		this.elencoProprietari = elencoProprietari;
	}
	public String getDescrizioneSezione() {
		return descrizioneSezione;
	}
	public void setDescrizioneSezione(String descrizioneSezione) {
		this.descrizioneSezione = descrizioneSezione;
	}
	public void setIsParticellaAttiva(boolean isParticellaAttiva) {
		this.isParticellaAttiva = isParticellaAttiva;
	}
	public boolean isParticellaAttiva() {
		return isParticellaAttiva;
	}
	public void setIsContenzioso(boolean isContenzioso) {
		this.isContenzioso = isContenzioso;
	}
	public boolean isContenzioso() {
		return isContenzioso;
	}
	public void setIsSupero(boolean isSupero) {
		this.isSupero = isSupero;
	}
	public boolean isSupero() {
		return isSupero;
	}

	public void setFlagContrattoOperazione(String flagContrattoOperazione) {
		this.flagContrattoOperazione = flagContrattoOperazione;
	}
	public String getFlagContrattoOperazione() {
		return flagContrattoOperazione;
	}

	public void setDescrizioneProvinciaParticella(String descrizioneProvinciaParticella)
	{
		this.descrizioneProvinciaParticella = descrizioneProvinciaParticella;
	}
	public String getDescrizioneProvinciaParticella()
	{
		return descrizioneProvinciaParticella;
	}

	public void setZonaVulnerabileNitrati(String zonaVulnerabileNitrati) {
		this.zonaVulnerabileNitrati = zonaVulnerabileNitrati;
	}

	public void setDescCausaleModParticella(String descCausaleModParticella) {
		this.descCausaleModParticella = descCausaleModParticella;
	}

	public void setIdCausaleModParticella(Long idCausaleModParticella) {
		this.idCausaleModParticella = idCausaleModParticella;
	}

	public void setTipiCausaleModParticella(String tipiCausaleModParticella) {
		this.tipiCausaleModParticella = tipiCausaleModParticella;
	}

	public String getZonaVulnerabileNitrati() {
		return zonaVulnerabileNitrati;
	}

	public String getDescCausaleModParticella() {
		return descCausaleModParticella;
	}

	public Long getIdCausaleModParticella() {
		return idCausaleModParticella;
	}

	public String getTipiCausaleModParticella() {
		return tipiCausaleModParticella;
	}

	public void setEsitoControllo(String esitoControllo) {
		this.esitoControllo = esitoControllo;
	}

	public String getEsitoControllo() {
		return esitoControllo;
	}

	/**
	 * @return the flagSchedario
	 */
	public String getFlagSchedario() {
		return flagSchedario;
	}

	/**
	 * @param flagSchedario the flagSchedario to set
	 */
	public void setFlagSchedario(String flagSchedario) {
		this.flagSchedario = flagSchedario;
	}
	
	public Long getIdAreaPsnFoglio() {
    return idAreaPsnFoglio;
  }
  public void setIdAreaPsnFoglio(Long idAreaPsnFoglio) {
    this.idAreaPsnFoglio = idAreaPsnFoglio;
  }

	/**
	 * @return the descAreaPsn
	 */
	public String getDescAreaPsn() {
		return descAreaPsn;
	}

	/**
	 * @param descAreaPsn the descAreaPsn to set
	 */
	public void setDescAreaPsn(String descAreaPsn) {
		this.descAreaPsn = descAreaPsn;
	}
	
	public String getStrFoglio() {
		return strFoglio;
	}
	public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }

  public void setStrFoglio(String strFoglio) {
		this.strFoglio = strFoglio;
	}
	public String getStrParticella() {
		return strParticella;
	}
	public void setStrParticella(String strParticella) {
		this.strParticella = strParticella;
	}
	public String getTipiAreaA() {
		return tipiAreaA;
	}
	public void setTipiAreaA(String tipiAreaA) {
		this.tipiAreaA = tipiAreaA;
	}
	public String getTipiAreaB() {
		return tipiAreaB;
	}
	public void setTipiAreaB(String tipiAreaB) {
		this.tipiAreaB = tipiAreaB;
	}
	public String getTipiAreaC() {
		return tipiAreaC;
	}
	public void setTipiAreaC(String tipiAreaC) {
		this.tipiAreaC = tipiAreaC;
	}
	public String getTipiAreaD() {
		return tipiAreaD;
	}
	public void setTipiAreaD(String tipiAreaD) {
		this.tipiAreaD = tipiAreaD;
	}
	public String getTipiZonaAltimetrica() {
		return tipiZonaAltimetrica;
	}
	public void setTipiZonaAltimetrica(String tipiZonaAltimetrica) {
		this.tipiZonaAltimetrica = tipiZonaAltimetrica;
	}
	public String getTipiCasoParticolare() {
		return tipiCasoParticolare;
	}
	public void setTipiCasoParticolare(String tipiCasoParticolare) {
		this.tipiCasoParticolare = tipiCasoParticolare;
	}
	public void setFlagStorico(boolean flagStorico) {
		this.flagStorico = flagStorico;
	}

	public void setFonteDato(CodeDescription fonteDato) {
		this.fonteDato = fonteDato;
	}

	public void setCausaleModParticella(CodeDescription causaleModParticella) {
		this.causaleModParticella = causaleModParticella;
	}

	public void setTipoDocumento(CodeDescription tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public boolean getFlagStorico() {
		return flagStorico;
	}

	public CodeDescription getFonteDato() {
		return fonteDato;
	}

	public CodeDescription getCausaleModParticella() {
		return causaleModParticella;
	}

	public CodeDescription getTipoDocumento() {
		return tipoDocumento;
	}

	public boolean isRecordModificato() {
		return recordModificato;
	}

	public void setRecordModificato(boolean recordModificato) {
		this.recordModificato = recordModificato;
	}

	public void setIdSegnalazione(Long idSegnalazione) {
		this.idSegnalazione = idSegnalazione;
	}

	public Long getIdSegnalazione() {
		return idSegnalazione;
	}
	public String getCodUtilizzo() {
		return codUtilizzo;
	}
	public void setCodUtilizzo(String codUtilizzo) {
		this.codUtilizzo = codUtilizzo;
	}
	public String getCodVarieta() {
		return codVarieta;
	}
	public void setCodVarieta(String codVarieta) {
		this.codVarieta = codVarieta;
	}
	public String getDescVarieta() {
		return descVarieta;
	}
	public void setDescVarieta(String descVarieta) {
		this.descVarieta = descVarieta;
	}
  public String getSupAgronomico() {
    return supAgronomico;
  }
  public void setSupAgronomico(String supAgronomico) {
    this.supAgronomico = supAgronomico;
  }

  public Long getIdIrrigazione()
  {
    return idIrrigazione;
  }

  public void setIdIrrigazione(Long idIrrigazione)
  {
    this.idIrrigazione = idIrrigazione;
  }

  public String getDescrizioneIrrigazione()
  {
    return descrizioneIrrigazione;
  }

  public void setDescrizioneIrrigazione(String descrizioneIrrigazione)
  {
    this.descrizioneIrrigazione = descrizioneIrrigazione;
  }

  public String getDocumento()
  {
    return documento;
  }

  public void setDocumento(String documento)
  {
    this.documento = documento;
  }

  public String getPrioritaLavorazione()
  {
    return prioritaLavorazione;
  }

  public void setPrioritaLavorazione(String prioritaLavorazione)
  {
    this.prioritaLavorazione = prioritaLavorazione;
  }

  public Long getIdAreaM()
  {
    return idAreaM;
  }

  public void setIdAreaM(Long idAreaM)
  {
    this.idAreaM = idAreaM;
  }

  public String getProprietari()
  {
    return proprietari;
  }

  public void setProprietari(String proprietari)
  {
    this.proprietari = proprietari;
  }

  public String getCodQualita()
  {
    return codQualita;
  }

  public void setCodQualita(String codQualita)
  {
    this.codQualita = codQualita;
  }

  public String getDescDettaglioUso()
  {
    return descDettaglioUso;
  }

  public void setDescDettaglioUso(String descDettaglioUso)
  {
    this.descDettaglioUso = descDettaglioUso;
  }

  public String getDescUtilizzoParticellaSecondaria()
  {
    return descUtilizzoParticellaSecondaria;
  }

  public void setDescUtilizzoParticellaSecondaria(
      String descUtilizzoParticellaSecondaria)
  {
    this.descUtilizzoParticellaSecondaria = descUtilizzoParticellaSecondaria;
  }

  public String getDescVarietaSecondaria()
  {
    return descVarietaSecondaria;
  }

  public void setDescVarietaSecondaria(String descVarietaSecondaria)
  {
    this.descVarietaSecondaria = descVarietaSecondaria;
  }

  public String getDescDettaglioUsoSecondario()
  {
    return descDettaglioUsoSecondario;
  }

  public void setDescDettaglioUsoSecondario(String descDettaglioUsoSecondario)
  {
    this.descDettaglioUsoSecondario = descDettaglioUsoSecondario;
  }

  public BigDecimal getSuperficieUtilizzataSecondaria()
  {
    return superficieUtilizzataSecondaria;
  }

  public void setSuperficieUtilizzataSecondaria(
      BigDecimal superficieUtilizzataSecondaria)
  {
    this.superficieUtilizzataSecondaria = superficieUtilizzataSecondaria;
  }

  public String getDescDestinazione()
  {
    return descDestinazione;
  }

  public void setDescDestinazione(String descDestinazione)
  {
    this.descDestinazione = descDestinazione;
  }

  public String getDescDestinazioneSecondaria()
  {
    return descDestinazioneSecondaria;
  }

  public void setDescDestinazioneSecondaria(String descDestinazioneSecondaria)
  {
    this.descDestinazioneSecondaria = descDestinazioneSecondaria;
  }

  public String getDescQualitaUso()
  {
    return descQualitaUso;
  }

  public void setDescQualitaUso(String descQualitaUso)
  {
    this.descQualitaUso = descQualitaUso;
  }

  public String getDescQualitaUsoSecondaria()
  {
    return descQualitaUsoSecondaria;
  }

  public void setDescQualitaUsoSecondaria(String descQualitaUsoSecondaria)
  {
    this.descQualitaUsoSecondaria = descQualitaUsoSecondaria;
  }

  public Vector<Long> getvIdFonte()
  {
    return vIdFonte;
  }

  public void setvIdFonte(Vector<Long> vIdFonte)
  {
    this.vIdFonte = vIdFonte;
  }

  

  
  
}
