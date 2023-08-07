package it.csi.solmr.dto.anag.terreni;

import it.csi.solmr.dto.anag.AnagAziendaVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Title: SMRGAA</p>
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: CSI - PIEMONTE</p>
 * @author Mauro Vocale
 * @version 1.0
 */
public class StoricoParticellaArboreaExcelVO implements Serializable {

	private static final long serialVersionUID = -2207979905292025913L;
	
	private AnagAziendaVO anagAziendaVO = null;
	private String descrizioneComuneParticella = null;
	private String sezione = null;
	private String foglio = null;
	private String particella = null;
	private String subalterno = null;
	private String superficieCatastale = null;
	private String superficieGrafica = null;
	private String descTipoUnar = null;
	private String progressivo = null;
	private String sestoSuFila = null;
	private String sestoTraFile = null;
	private String numeroCeppi = null;
	private String descFormaAllevamento = null;
	private String area = null;
	private String destinazioneProduttiva = null;
	private String varieta = null;
	private String percentualeVarieta = null;
	private String altriVitigni = null;
	private Date dataFineValidita = null;
	private String matricolaCCIAA = null;
	private String supEleggibile = null;
	private String descTipologiaVino = null;
	private String annoIscrizioneAlbo = null;
	private Date dataImpianto = null;
	private Date dataPrimaProduzione = null;
	private String idIlo = null;
	private String supParcella = null;
	private String tolleranza = null;
	private String supPostAllinea = null;
	private Long idParticella = null;
	private String supParcellaInf = null;
	private String supParcellaSup = null;
	private Integer codTolleranza = null;
	private String  associataParcella = null;
	private Long idCausaleModifica = null;
	private String descCausaleModifica = null;
	private String vigna = null;
	private String vignaElencoReg = null;
	private Long idTipologiaVino = null;
	private Date dataRichiesta = null;
	private Date dataEvasione = null;
	private String descMenzioneGeografica = null;
	private BigDecimal percentualePossesso;
	private BigDecimal percentualeUsoElegg;
	private BigDecimal percentualeFallanza;
	private String flagImproduttiva;
	private Long idParticellaCertificata;
	private Long idVarieta;
	private String inNotifica;
	private Long idCatalogoMatrice;
	
	public String getAssociataParcella()
  {
    return associataParcella;
  }



  public void setAssociataParcella(String associataParcella)
  {
    this.associataParcella = associataParcella;
  }



  public Integer getCodTolleranza()
  {
    return codTolleranza;
  }



  public void setCodTolleranza(Integer codTolleranza)
  {
    this.codTolleranza = codTolleranza;
  }
	
	

	public String getSupParcellaInf()
  {
    return supParcellaInf;
  }

  public void setSupParcellaInf(String supParcellaInf)
  {
    this.supParcellaInf = supParcellaInf;
  }

  public String getSupParcellaSup()
  {
    return supParcellaSup;
  }

  public void setSupParcellaSup(String supParcellaSup)
  {
    this.supParcellaSup = supParcellaSup;
  }	
	
	public Long getIdParticella()
  {
    return idParticella;
  }

  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }

  public String getSupPostAllinea()
  {
    return supPostAllinea;
  }

  public void setSupPostAllinea(String supPostAllinea)
  {
    this.supPostAllinea = supPostAllinea;
  }

  public String getIdIlo()
  {
    return idIlo;
  }

  public void setIdIlo(String idIlo)
  {
    this.idIlo = idIlo;
  }

  public String getSupParcella()
  {
    return supParcella;
  }

  public void setSupParcella(String supParcella)
  {
    this.supParcella = supParcella;
  }

  public String getTolleranza()
  {
    return tolleranza;
  }

  public void setTolleranza(String tolleranza)
  {
    this.tolleranza = tolleranza;
  }

	public String getAltriVitigni() {
		return altriVitigni;
	}

	public void setAltriVitigni(String altriVitigni) {
		this.altriVitigni = altriVitigni;
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

	
	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	
	public String getDescFormaAllevamento() {
		return descFormaAllevamento;
	}

	public void setDescFormaAllevamento(String descFormaAllevamento) {
		this.descFormaAllevamento = descFormaAllevamento;
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
	 * @return the descTipoUnar
	 */
	public String getDescTipoUnar() {
		return descTipoUnar;
	}

	/**
	 * @param descTipoUnar the descTipoUnar to set
	 */
	public void setDescTipoUnar(String descTipoUnar) {
		this.descTipoUnar = descTipoUnar;
	}

	/**
	 * @return the destinazioneProduttiva
	 */
	public String getDestinazioneProduttiva() {
		return destinazioneProduttiva;
	}

	/**
	 * @param destinazioneProduttiva the destinazioneProduttiva to set
	 */
	public void setDestinazioneProduttiva(String destinazioneProduttiva) {
		this.destinazioneProduttiva = destinazioneProduttiva;
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

	public String getNumeroCeppi() {
		return numeroCeppi;
	}

	public void setNumeroCeppi(String numeroCeppi) {
		this.numeroCeppi = numeroCeppi;
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

	
	public String getPercentualeVarieta() {
		return percentualeVarieta;
	}

	public void setPercentualeVarieta(String percentualeVarieta) {
		this.percentualeVarieta = percentualeVarieta;
	}

	/**
	 * @return the progressivo
	 */
	public String getProgressivo() {
		return progressivo;
	}

	/**
	 * @param progressivo the progressivo to set
	 */
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}

	
	public String getSestoSuFila() {
		return sestoSuFila;
	}

	public void setSestoSuFila(String sestoSuFila) {
		this.sestoSuFila = sestoSuFila;
	}

	
	public String getSestoTraFile() {
		return sestoTraFile;
	}

	public void setSestoTraFile(String sestoTraFile) {
		this.sestoTraFile = sestoTraFile;
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

	public String getMatricolaCCIAA()
  {
    return matricolaCCIAA;
  }

  public void setMatricolaCCIAA(String matricolaCCIAA)
  {
    this.matricolaCCIAA = matricolaCCIAA;
  }

  public String getSupEleggibile()
  {
    return supEleggibile;
  }

  public void setSupEleggibile(String supEleggibile)
  {
    this.supEleggibile = supEleggibile;
  }

  public String getDescTipologiaVino()
  {
    return descTipologiaVino;
  }

  public void setDescTipologiaVino(String descTipologiaVino)
  {
    this.descTipologiaVino = descTipologiaVino;
  }

  public String getAnnoIscrizioneAlbo()
  {
    return annoIscrizioneAlbo;
  }

  public void setAnnoIscrizioneAlbo(String annoIscrizioneAlbo)
  {
    this.annoIscrizioneAlbo = annoIscrizioneAlbo;
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

  public String getSuperficieGrafica()
  {
    return superficieGrafica;
  }

  public void setSuperficieGrafica(String superficieGrafica)
  {
    this.superficieGrafica = superficieGrafica;
  }

  public Date getDataImpianto()
  {
    return dataImpianto;
  }

  public void setDataImpianto(Date dataImpianto)
  {
    this.dataImpianto = dataImpianto;
  }

  public Long getIdCausaleModifica()
  {
    return idCausaleModifica;
  }

  public void setIdCausaleModifica(Long idCausaleModifica)
  {
    this.idCausaleModifica = idCausaleModifica;
  }

  public String getVigna()
  {
    return vigna;
  }

  public void setVigna(String vigna)
  {
    this.vigna = vigna;
  }
  
  public String getVignaElencoReg()
  {
    return vignaElencoReg;
  }

  public void setVignaElencoReg(String vignaElencoReg)
  {
    this.vignaElencoReg = vignaElencoReg;
  }
  
  public Long getIdTipologiaVino()
  {
    return idTipologiaVino;
  }

  public void setIdTipologiaVino(Long idTipologiaVino)
  {
    this.idTipologiaVino = idTipologiaVino;
  }

  public String getDescCausaleModifica()
  {
    return descCausaleModifica;
  }

  public void setDescCausaleModifica(String descCausaleModifica)
  {
    this.descCausaleModifica = descCausaleModifica;
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

  public String getDescMenzioneGeografica()
  {
    return descMenzioneGeografica;
  }

  public void setDescMenzioneGeografica(String descMenzioneGeografica)
  {
    this.descMenzioneGeografica = descMenzioneGeografica;
  }
  
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
  
  public BigDecimal getPercentualeUsoElegg()
  {
    return percentualeUsoElegg;
  }

  public void setPercentualeUsoElegg(BigDecimal percentualeUsoElegg)
  {
    this.percentualeUsoElegg = percentualeUsoElegg;
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

  public Long getIdParticellaCertificata()
  {
    return idParticellaCertificata;
  }

  public void setIdParticellaCertificata(Long idParticellaCertificata)
  {
    this.idParticellaCertificata = idParticellaCertificata;
  }

  public Long getIdVarieta()
  {
    return idVarieta;
  }

  public void setIdVarieta(Long idVarieta)
  {
    this.idVarieta = idVarieta;
  }

  public Date getDataPrimaProduzione()
  {
    return dataPrimaProduzione;
  }

  public void setDataPrimaProduzione(Date dataPrimaProduzione)
  {
    this.dataPrimaProduzione = dataPrimaProduzione;
  }

  public String getInNotifica()
  {
    return inNotifica;
  }

  public void setInNotifica(String inNotifica)
  {
    this.inNotifica = inNotifica;
  }

  public Long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }

  public void setIdCatalogoMatrice(Long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }   
  
}
