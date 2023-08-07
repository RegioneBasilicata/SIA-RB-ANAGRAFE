package it.csi.solmr.dto.anag;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

/**
 * <p>Title: SMRGAA</p>
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: CSI - PIEMONTE</p>
 * @author Mauro Vocale
 * @version 1.0
 */
public class AnagParticellaExcelVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -278552451990061506L;

	private AnagAziendaVO anagAziendaVO = null;
	private Date dataSituazioneAl = null;
	private String labelUte = null;
	private String istatComuneParticella = null;
	private String descrizioneComuneParticella = null;
	private String siglaProvinciaParticella = null;
	private String sezione = null;
	private String foglio = null;
	private String particella = null;
	private String subalterno = null;
	private String superficieCatastale = null;
	private String superficieGrafica = null;
  private String superficieCondotta = null;
	private Long   idTitoloPossesso = null;
	private String superficieUtilizzata = null;
	private String superficieUtilizzataSecondaria = null;
	private String descrizioneZonaAltimetrica = null;
	private String natura2000 = null;
	private String descrizioneZonaVulnerabile = null;
	private String superficieSeminabile = null;
	private String superficieArboreaSpecializzata = null;
	private String supAgronomica = null;
	private String supUsoGrafico = null;
	private String supEleggibile = null;
	private String supEleggibileNetta = null;
	private Long   idCasoParticolare = null;
	private String irrigua = null;
	private String usoPrimario = null;
	private String varieta = null;
	private String usoSecondario = null;
	private String varietaSecondaria = null;
	private String numeroPianteCeppi = null;
	private String annoImpianto = null;
	private String note = null;
	private Long   idUtilizzoParticella = null;
	private String supBiologico;
	private String supConvenzionale;
	private String supInConversione;
	private Date   dataInizioConversione;
	private Date   dataFineConversione;
	private String documento;
	private BigDecimal   percentualePossesso;
	private String supRegPascoli;
	private String descFonteRegPascoli;
	private Date dataRichiesta;
	private Date dataEvasione;
	private String descrizioneCorpoIdrico = null;
	private String supEleggibileRiproporzionata = null;
	private Long idParticella = null;
	private String supAssegnataAllineamento;
	private String inNotifica;
	private String tipoDestinazionePrimario;
  private String tipoDestinazioneSecondario;
	private String tipoDettUsoPrimario;
	private String tipoDettUsoSecondario;
	private String tipoQualitaUsoPrimario;
  private String tipoQualitaUsoSecondario;
	private String tipoSeminaPrimario;
	private String tipoSeminaSecondario;
	private String tipoEpocaSeminaPrimario;
  private String tipoEpocaSeminaSecondario;
  private Date dataInzioDestPrim;
  private Date dataFineDestPrim;
  private Date dataInzioDestSec;
  private Date dataFineDestSec;
	private String tipoEfa;
	private String valoreOriginale;
	private String descUnitaMisura;
	private String valoreDopoConversione;
	private String valoreDopoPonderazione;
	private String mantenimento;
	private String allevamento;
	private Vector<String> vDescValoreArea;
	private Long idConduzioneParticella; 
	private Long idConduzioneDichiarata;

  
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}
	
	public String getSupRegPascoli()
  {
    return supRegPascoli;
  }

  public void setSupRegPascoli(String supRegPascoli)
  {
    this.supRegPascoli = supRegPascoli;
  }

  public String getDescFonteRegPascoli()
  {
    return descFonteRegPascoli;
  }

  public void setDescFonteRegPascoli(String descFonteRegPascoli)
  {
    this.descFonteRegPascoli = descFonteRegPascoli;
  }
	
	
	public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }

	/**
	 * @return the anagAziendaVO
	 */
	public AnagAziendaVO getAnagAziendaVO() {
		return anagAziendaVO;
	}

	/**
	 * @param anagAziendaVO the anagAziendaVO to set
	 */
	public void setAnagAziendaVO(AnagAziendaVO anagAziendaVO) {
		this.anagAziendaVO = anagAziendaVO;
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
	 * @return the descrizioneAltimetrica
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
	 * @return the descrizioneComuneParticella
	 */
	public String getDescrizioneComuneParticella() {
		return descrizioneComuneParticella;
	}

	/**
	 * @param descrizioneComuneParticella the descrizioneComuneParticella to set
	 */
	public void setDescrizioneComuneParticella(String descrizioneComuneParticella) {
		this.descrizioneComuneParticella = descrizioneComuneParticella;
	}

	/**
	 * @return the natura2000
	 */
	public String getNatura2000() {
		return natura2000;
	}

	/**
	 * @param natura2000 the natura2000 to set
	 */
	public void setNatura2000(String natura2000) {
		this.natura2000 = natura2000;
	}

	/**
	 * @return the descrizioneZonaVulnerabile
	 */
	public String getDescrizioneZonaVulnerabile() {
		return descrizioneZonaVulnerabile;
	}

	/**
	 * @param descrizioneZonaVulnerabile the descrizioneZonaVulnerabile to set
	 */
	public void setDescrizioneZonaVulnerabile(String descrizioneZonaVulnerabile) {
		this.descrizioneZonaVulnerabile = descrizioneZonaVulnerabile;
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
	 * @return the idTitoloPossesso
	 */
	public Long getIdTitoloPossesso() {
		return idTitoloPossesso;
	}

	/**
	 * @param idTitoloPossesso the idTitoloPossesso to set
	 */
	public void setIdTitoloPossesso(Long idTitoloPossesso) {
		this.idTitoloPossesso = idTitoloPossesso;
	}

	/**
	 * @return the irrigua
	 */
	public String getIrrigua() {
		return irrigua;
	}

	/**
	 * @param irrigua the irrigua to set
	 */
	public void setIrrigua(String irrigua) {
		this.irrigua = irrigua;
	}

	/**
	 * @return the istatComuneParticella
	 */
	public String getIstatComuneParticella() {
		return istatComuneParticella;
	}

	/**
	 * @param istatComuneParticella the istatComuneParticella to set
	 */
	public void setIstatComuneParticella(String istatComuneParticella) {
		this.istatComuneParticella = istatComuneParticella;
	}

	/**
	 * @return the labelUte
	 */
	public String getLabelUte() {
		return labelUte;
	}

	/**
	 * @param labelUte the labelUte to set
	 */
	public void setLabelUte(String labelUte) {
		this.labelUte = labelUte;
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
	 * @return the siglaProvinciaParticella
	 */
	public String getSiglaProvinciaParticella() {
		return siglaProvinciaParticella;
	}

	/**
	 * @param siglaProvinciaParticella the siglaProvinciaParticella to set
	 */
	public void setSiglaProvinciaParticella(String siglaProvinciaParticella) {
		this.siglaProvinciaParticella = siglaProvinciaParticella;
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
	 * @return the superficieArboreaSpecializzata
	 */
	public String getSuperficieArboreaSpecializzata() {
		return superficieArboreaSpecializzata;
	}

	/**
	 * @param superficieArboreaSpecializzata the superficieArboreaSpecializzata to set
	 */
	public void setSuperficieArboreaSpecializzata(
			String superficieArboreaSpecializzata) {
		this.superficieArboreaSpecializzata = superficieArboreaSpecializzata;
	}
	
	
  /**
  * @return the supUsoGrafico
  */
  public String getSupUsoGrafico() {
    return supUsoGrafico;
  }

  /**
   * @param supUsoGrafico the supUsoGrafico to set
   */
  public void setSupUsoGrafico(
      String supUsoGrafico) {
    this.supUsoGrafico = supUsoGrafico;
  }
  
  /**
   * @return the supEleggibile
   */
   public String getSupEleggibile() {
     return supEleggibile;
   }

   /**
    * @param supEleggibile the supEleggibile to set
    */
   public void setSupEleggibile(
       String supEleggibile) {
     this.supEleggibile = supEleggibile;
   }
	

	/**
	 * @return the superficieCatastale
	 */
	public String getSuperficieCatastale() {
		return superficieCatastale;
	}

	/**
	 * @param superficieCatastale the superficieCatastale to set
	 */
	public void setSuperficieCatastale(String superficieCatastale) {
		this.superficieCatastale = superficieCatastale;
	}

	/**
	 * @return the superficieCondotta
	 */
	public String getSuperficieCondotta() {
		return superficieCondotta;
	}

	/**
	 * @param superficieCondotta the superficieCondotta to set
	 */
	public void setSuperficieCondotta(String superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}

	/**
	 * @return the superficieSeminabile
	 */
	public String getSuperficieSeminabile() {
		return superficieSeminabile;
	}

	/**
	 * @param superficieSeminabile the superficieSeminabile to set
	 */
	public void setSuperficieSeminabile(String superficieSeminabile) {
		this.superficieSeminabile = superficieSeminabile;
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
	 * @return the superficieUtilizzataSecondaria
	 */
	public String getSuperficieUtilizzataSecondaria() {
		return superficieUtilizzataSecondaria;
	}

	/**
	 * @param superficieUtilizzataSecondaria the superficieUtilizzataSecondaria to set
	 */
	public void setSuperficieUtilizzataSecondaria(
			String superficieUtilizzataSecondaria) {
		this.superficieUtilizzataSecondaria = superficieUtilizzataSecondaria;
	}

	/**
	 * @return the usoPrimario
	 */
	public String getUsoPrimario() {
		return usoPrimario;
	}

	/**
	 * @param usoPrimario the usoPrimario to set
	 */
	public void setUsoPrimario(String usoPrimario) {
		this.usoPrimario = usoPrimario;
	}

	/**
	 * @return the usoSecondario
	 */
	public String getUsoSecondario() {
		return usoSecondario;
	}

	/**
	 * @param usoSecondario the usoSecondario to set
	 */
	public void setUsoSecondario(String usoSecondario) {
		this.usoSecondario = usoSecondario;
	}

	/**
	 * @return the varieta
	 */
	public String getVarieta() {
		return varieta;
	}

	/**
	 * @param varieta the varieta to set
	 */
	public void setVarieta(String varieta) {
		this.varieta = varieta;
	}

	/**
	 * @return the varietaSecondaria
	 */
	public String getVarietaSecondaria() {
		return varietaSecondaria;
	}

	/**
	 * @param varietaSecondaria the varietaSecondaria to set
	 */
	public void setVarietaSecondaria(String varietaSecondaria) {
		this.varietaSecondaria = varietaSecondaria;
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
	 * @return the idUtilizzoParticella
	 */
	public Long getIdUtilizzoParticella() {
		return idUtilizzoParticella;
	}

	/**
	 * @param idUtilizzoParticella the idUtilizzoParticella to set
	 */
	public void setIdUtilizzoParticella(Long idUtilizzoParticella) {
		this.idUtilizzoParticella = idUtilizzoParticella;
	}

  public String getSupAgronomica()
  {
    return supAgronomica;
  }

  public void setSupAgronomica(String supAgronomica)
  {
    this.supAgronomica = supAgronomica;
  }

  public String getSupBiologico()
  {
    return supBiologico;
  }

  public void setSupBiologico(String supBiologico)
  {
    this.supBiologico = supBiologico;
  }

  public String getSupConvenzionale()
  {
    return supConvenzionale;
  }

  public void setSupConvenzionale(String supConvenzionale)
  {
    this.supConvenzionale = supConvenzionale;
  }

  public String getSupInConversione()
  {
    return supInConversione;
  }

  public void setSupInConversione(String supInConversione)
  {
    this.supInConversione = supInConversione;
  }

  public Date getDataSituazioneAl()
  {
    return dataSituazioneAl;
  }

  public void setDataSituazioneAl(Date dataSituazioneAl)
  {
    this.dataSituazioneAl = dataSituazioneAl;
  }

  public Date getDataInizioConversione()
  {
    return dataInizioConversione;
  }

  public void setDataInizioConversione(Date dataInizioConversione)
  {
    this.dataInizioConversione = dataInizioConversione;
  }

  public Date getDataFineConversione()
  {
    return dataFineConversione;
  }

  public void setDataFineConversione(Date dataFineConversione)
  {
    this.dataFineConversione = dataFineConversione;
  }

  public String getDocumento()
  {
    return documento;
  }

  public void setDocumento(String documento)
  {
    this.documento = documento;
  }
  
  public String getSuperficieGrafica()
  {
    return superficieGrafica;
  }

  public void setSuperficieGrafica(String superficieGrafica)
  {
    this.superficieGrafica = superficieGrafica;
  }

  public Date getDataRichiesta()
  {
    return dataRichiesta;
  }

  public void setDataRichiesta(Date dataRichiesta)
  {
    this.dataRichiesta = dataRichiesta;
  }

  public Date getDataEvasione()
  {
    return dataEvasione;
  }

  public void setDataEvasione(Date dataEvasione)
  {
    this.dataEvasione = dataEvasione;
  }

  public String getSupEleggibileNetta()
  {
    return supEleggibileNetta;
  }

  public void setSupEleggibileNetta(String supEleggibileNetta)
  {
    this.supEleggibileNetta = supEleggibileNetta;
  }

  public String getDescrizioneCorpoIdrico()
  {
    return descrizioneCorpoIdrico;
  }

  public void setDescrizioneCorpoIdrico(String descrizioneCorpoIdrico)
  {
    this.descrizioneCorpoIdrico = descrizioneCorpoIdrico;
  }

  public String getSupEleggibileRiproporzionata()
  {
    return supEleggibileRiproporzionata;
  }

  public void setSupEleggibileRiproporzionata(String supEleggibileRiproporzionata)
  {
    this.supEleggibileRiproporzionata = supEleggibileRiproporzionata;
  }

  public Long getIdParticella()
  {
    return idParticella;
  }

  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }

  public String getSupAssegnataAllineamento()
  {
    return supAssegnataAllineamento;
  }

  public void setSupAssegnataAllineamento(String supAssegnataAllineamento)
  {
    this.supAssegnataAllineamento = supAssegnataAllineamento;
  }

  public String getInNotifica()
  {
    return inNotifica;
  }

  public void setInNotifica(String inNotifica)
  {
    this.inNotifica = inNotifica;
  }

  public String getTipoDettUsoPrimario()
  {
    return tipoDettUsoPrimario;
  }

  public void setTipoDettUsoPrimario(String tipoDettUsoPrimario)
  {
    this.tipoDettUsoPrimario = tipoDettUsoPrimario;
  }

  public String getTipoDettUsoSecondario()
  {
    return tipoDettUsoSecondario;
  }

  public void setTipoDettUsoSecondario(String tipoDettUsoSecondario)
  {
    this.tipoDettUsoSecondario = tipoDettUsoSecondario;
  }

  public String getTipoSeminaPrimario()
  {
    return tipoSeminaPrimario;
  }

  public void setTipoSeminaPrimario(String tipoSeminaPrimario)
  {
    this.tipoSeminaPrimario = tipoSeminaPrimario;
  }

  public String getTipoSeminaSecondario()
  {
    return tipoSeminaSecondario;
  }

  public void setTipoSeminaSecondario(String tipoSeminaSecondario)
  {
    this.tipoSeminaSecondario = tipoSeminaSecondario;
  }

  public String getTipoEfa()
  {
    return tipoEfa;
  }

  public void setTipoEfa(String tipoEfa)
  {
    this.tipoEfa = tipoEfa;
  }

  public String getValoreOriginale()
  {
    return valoreOriginale;
  }

  public void setValoreOriginale(String valoreOriginale)
  {
    this.valoreOriginale = valoreOriginale;
  }

  public String getDescUnitaMisura()
  {
    return descUnitaMisura;
  }

  public void setDescUnitaMisura(String descUnitaMisura)
  {
    this.descUnitaMisura = descUnitaMisura;
  }

  public String getValoreDopoConversione()
  {
    return valoreDopoConversione;
  }

  public void setValoreDopoConversione(String valoreDopoConversione)
  {
    this.valoreDopoConversione = valoreDopoConversione;
  }

  public String getValoreDopoPonderazione()
  {
    return valoreDopoPonderazione;
  }

  public void setValoreDopoPonderazione(String valoreDopoPonderazione)
  {
    this.valoreDopoPonderazione = valoreDopoPonderazione;
  }

  public String getTipoDestinazionePrimario()
  {
    return tipoDestinazionePrimario;
  }

  public void setTipoDestinazionePrimario(String tipoDestinazionePrimario)
  {
    this.tipoDestinazionePrimario = tipoDestinazionePrimario;
  }

  public String getTipoDestinazioneSecondario()
  {
    return tipoDestinazioneSecondario;
  }

  public void setTipoDestinazioneSecondario(String tipoDestinazioneSecondario)
  {
    this.tipoDestinazioneSecondario = tipoDestinazioneSecondario;
  }

  public String getTipoQualitaUsoPrimario()
  {
    return tipoQualitaUsoPrimario;
  }

  public void setTipoQualitaUsoPrimario(String tipoQualitaUsoPrimario)
  {
    this.tipoQualitaUsoPrimario = tipoQualitaUsoPrimario;
  }

  public String getTipoQualitaUsoSecondario()
  {
    return tipoQualitaUsoSecondario;
  }

  public void setTipoQualitaUsoSecondario(String tipoQualitaUsoSecondario)
  {
    this.tipoQualitaUsoSecondario = tipoQualitaUsoSecondario;
  }

  public String getTipoEpocaSeminaPrimario()
  {
    return tipoEpocaSeminaPrimario;
  }

  public void setTipoEpocaSeminaPrimario(String tipoEpocaSeminaPrimario)
  {
    this.tipoEpocaSeminaPrimario = tipoEpocaSeminaPrimario;
  }

  public String getTipoEpocaSeminaSecondario()
  {
    return tipoEpocaSeminaSecondario;
  }

  public void setTipoEpocaSeminaSecondario(String tipoEpocaSeminaSecondario)
  {
    this.tipoEpocaSeminaSecondario = tipoEpocaSeminaSecondario;
  }

  public Date getDataInzioDestPrim()
  {
    return dataInzioDestPrim;
  }

  public void setDataInzioDestPrim(Date dataInzioDestPrim)
  {
    this.dataInzioDestPrim = dataInzioDestPrim;
  }

  public Date getDataFineDestPrim()
  {
    return dataFineDestPrim;
  }

  public void setDataFineDestPrim(Date dataFineDestPrim)
  {
    this.dataFineDestPrim = dataFineDestPrim;
  }

  public Date getDataInzioDestSec()
  {
    return dataInzioDestSec;
  }

  public void setDataInzioDestSec(Date dataInzioDestSec)
  {
    this.dataInzioDestSec = dataInzioDestSec;
  }

  public Date getDataFineDestSec()
  {
    return dataFineDestSec;
  }

  public void setDataFineDestSec(Date dataFineDestSec)
  {
    this.dataFineDestSec = dataFineDestSec;
  }

  public String getMantenimento()
  {
    return mantenimento;
  }

  public void setMantenimento(String mantenimento)
  {
    this.mantenimento = mantenimento;
  }

  public String getAllevamento()
  {
    return allevamento;
  }

  public void setAllevamento(String allevamento)
  {
    this.allevamento = allevamento;
  }

  public Vector<String> getvDescValoreArea()
  {
    return vDescValoreArea;
  }

  public void setvDescValoreArea(Vector<String> vDescValoreArea)
  {
    this.vDescValoreArea = vDescValoreArea;
  }

  public Long getIdConduzioneParticella()
  {
    return idConduzioneParticella;
  }

  public void setIdConduzioneParticella(Long idConduzioneParticella)
  {
    this.idConduzioneParticella = idConduzioneParticella;
  }

  public Long getIdConduzioneDichiarata()
  {
    return idConduzioneDichiarata;
  }

  public void setIdConduzioneDichiarata(Long idConduzioneDichiarata)
  {
    this.idConduzioneDichiarata = idConduzioneDichiarata;
  }

	
  
}
