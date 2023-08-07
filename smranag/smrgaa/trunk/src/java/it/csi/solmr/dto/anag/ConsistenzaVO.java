package it.csi.solmr.dto.anag;

import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.solmr.dto.anag.consistenza.TipoMotivoDichiarazioneVO;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class ConsistenzaVO implements Serializable {
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -5716655199945051357L;

	private String anno;
	private Long codiceFotografiaTerreni;
	private String data;
	private Date dataDichiarazione = null;
	private Long idAzienda;
	private String idDichiarazioneConsistenza;
	private String idProcedimento;
	private String idUtente;
	private String motivo;
	private String note;
	private String tipoConvalida;
	private String descProcedimento;
	private boolean anomalie;
	private String utenteAggiornamento;
	private String idMotivo;
	private String responsabile;
	private Boolean esistePratica;
	private Date dataAggiornamentoFascicolo = null;
	private String cuaaValidato = null;
	private String flagAnomalia = null;
	private Date dataInserimentoDichiarazione = null;
	private String flagInviaUso = null;
	private String annoCampagna = null;
	private TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO = null;
  private String numeroProtocollo;
  private Date dataProtocollo;
  private String dataProtocolloStr;
  private String flagProtocollo;
  private boolean possibileProtocollo;
  private Date dataAggiornamentoUV = null;
  private String flagAnomaliaUV = null;
  private Date dataAggiornamentoColtura = null;
  private String flagAnomaliaColtura = null;
  private Date dataAggiornamentoFabbricati = null;
  private String flagAnomaliaFabbricati = null;
  private Date dataAggiornamentoCC = null;
  private String flagAnomaliaCC = null;
  private Date dataConnessioneUnarParcella = null;
  private String schedulaFascicoli = null;
  private InvioFascicoliVO invioFascicoliVO = null;
  private String flagInvioMail = null;
  private Long extIdTipoDocumentoIndex;
  private Long idAllegato;
  private Long idTipoFirma;
  private String stileFirma;
  private String descrizioneTipoFirma;
  private Long idAllegatoDichiarazione;
  private Date dataRichiestaStampa;
  private Vector vAllegatoDichiarazioneVO = null;
  private Long extIdDocumentoIndex;
  
  //Piano Grafico
  private String codiceUtilita;
  private Long idAccessoPianoGrafico;
  
  
  public String getFlagInvioMail()
  {
    return flagInvioMail;
  }
  public void setFlagInvioMail(String flagInvioMail)
  {
    this.flagInvioMail = flagInvioMail;
  }
  public java.util.Date getDataConnessioneUnarParcella()
  {
    return dataConnessioneUnarParcella;
  }
  public void setDataConnessioneUnarParcella(
      java.util.Date dataConnessioneUnarParcella)
  {
    this.dataConnessioneUnarParcella = dataConnessioneUnarParcella;
  }

	public String getAnno()
	{
		return anno;
	}
	public void setAnno(String anno)
	{
		this.anno = anno;
	}

	public void setCodiceFotografiaTerreni(Long codiceFotografiaTerreni)
	{
		this.codiceFotografiaTerreni = codiceFotografiaTerreni;
	}
	public Long getCodiceFotografiaTerreni()
	{
		return codiceFotografiaTerreni;
	}

	public void setData(String data)
	{
		this.data = data;
	}
	public String getData()
	{
		return data;
	}

	public Timestamp getDataTimestamp()
	{
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");
		try
		{
			return new Timestamp(format.parse(data).getTime());
		}
		catch (ParseException ex)
		{
			return null;
		}
	}

	public void setIdAzienda(Long idAzienda)
	{
		this.idAzienda = idAzienda;
	}
	public Long getIdAzienda()
	{
		return idAzienda;
	}

	public void setIdDichiarazioneConsistenza(String idDichiarazioneConsistenza)
	{
		this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
	}
	public String getIdDichiarazioneConsistenza()
	{
		return idDichiarazioneConsistenza;
	}

	public void setIdProcedimento(String idProcedimento)
	{
		this.idProcedimento = idProcedimento;
	}
	public String getIdProcedimento()
	{
		return idProcedimento;
	}

	public void setIdUtente(String idUtente)
	{
		this.idUtente = idUtente;
	}
	public String getIdUtente()
	{
		return idUtente;
	}

	public void setMotivo(String motivo)
	{
		this.motivo = motivo;
	}
	public String getMotivo()
	{
		return motivo;
	}

	public void setNote(String note)
	{
		this.note = note;
	}
	public String getNote()
	{
		return note;
	}

	public void setTipoConvalida(String tipoConvalida)
	{
		this.tipoConvalida = tipoConvalida;
	}
	public String getTipoConvalida()
	{
		return tipoConvalida;
	}

	public void setDescProcedimento(String descProcedimento)
	{
		this.descProcedimento = descProcedimento;
	}
	public String getDescProcedimento()
	{
		return descProcedimento;
	}

	public void setAnomalie(boolean anomalie)
	{
		this.anomalie = anomalie;
	}
	public boolean isAnomalie()
	{
		return anomalie;
	}

	public void setUtenteAggiornamento(String utenteAggiornamento)
	{
		this.utenteAggiornamento = utenteAggiornamento;
	}
	public String getUtenteAggiornamento()
	{
		return utenteAggiornamento;
	}

	/**
	 * @return the dataAggiornamentoFascicolo
	 */
	 public java.util.Date getDataAggiornamentoFascicolo() {
		 return dataAggiornamentoFascicolo;
	 }

	 /**
	  * @param dataAggiornamentoFascicolo the dataAggiornamentoFascicolo to set
	  */
	 public void setDataAggiornamentoFascicolo(java.util.Date dataAggiornamentoFascicolo) {
		 this.dataAggiornamentoFascicolo = dataAggiornamentoFascicolo;
	 }

	 /**
	  * @return the cuaaValidato
	  */
	 public String getCuaaValidato() {
		 return cuaaValidato;
	 }

	 /**
	  * @param cuaaValidato the cuaaValidato to set
	  */
	 public void setCuaaValidato(String cuaaValidato) {
		 this.cuaaValidato = cuaaValidato;
	 }

	 /**
	  * @return the flagAnomalia
	  */
	 public String getFlagAnomalia() {
		 return flagAnomalia;
	 }

	 /**
	  * @param flagAnomalia the flagAnomalia to set
	  */
	 public void setFlagAnomalia(String flagAnomalia) {
		 this.flagAnomalia = flagAnomalia;
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
	 public void setDataInserimentoDichiarazione(java.util.Date dataInserimentoDichiarazione) {
		 this.dataInserimentoDichiarazione = dataInserimentoDichiarazione;
	 }

	 /**
	  * @return the flagInviaUso
	  */
	 public String getFlagInviaUso() {
		 return flagInviaUso;
	 }

	 /**
	  * @param flagInviaUso the flagInviaUso to set
	  */
	 public void setFlagInviaUso(String flagInviaUso) {
		 this.flagInviaUso = flagInviaUso;
	 }

	 /**
	  * @return the annoCampagna
	  */
	 public String getAnnoCampagna() {
		 return annoCampagna;
	 }

	 /**
	  * @param annoCampagna the annoCampagna to set
	  */
	 public void setAnnoCampagna(String annoCampagna) {
		 this.annoCampagna = annoCampagna;
	 }

	 /**
	  * @return the tipoMotivoDichiarazioneVO
	  */
	 public TipoMotivoDichiarazioneVO getTipoMotivoDichiarazioneVO() {
		 return tipoMotivoDichiarazioneVO;
	 }

	 /**
	  * @param tipoMotivoDichiarazioneVO the tipoMotivoDichiarazioneVO to set
	  */
	 public void setTipoMotivoDichiarazioneVO(TipoMotivoDichiarazioneVO tipoMotivoDichiarazioneVO) {
		 this.tipoMotivoDichiarazioneVO = tipoMotivoDichiarazioneVO;
	 }


  public void setIdMotivo(String idMotivo)
  {
          this.idMotivo = idMotivo;
  }
  public String getIdMotivo()
  {
          return idMotivo;
  }
  public String getResponsabile() {
          return responsabile;
  }
  public void setResponsabile(String responsabile) {
          this.responsabile = responsabile;
  }
  public Boolean getEsistePratica() {
          return esistePratica;
  }
  public void setEsistePratica(Boolean esistePratica) {
          this.esistePratica = esistePratica;
  }
  public String getNumeroProtocollo() {
  return numeroProtocollo;
  }
  public void setNumeroProtocollo(String numeroProtocollo) {
  this.numeroProtocollo = numeroProtocollo;
  }
  public java.util.Date getDataProtocollo() {
  return dataProtocollo;
  }
  public void setDataProtocollo(java.util.Date dataProtocollo) {
  this.dataProtocollo = dataProtocollo;
  }
  public String getDataProtocolloStr() {
  return dataProtocolloStr;
  }
  public void setDataProtocolloStr(String dataProtocolloStr) {
  this.dataProtocolloStr = dataProtocolloStr;
  }
  public String getFlagProtocollo() {
  return flagProtocollo;
  }
  public void setFlagProtocollo(String flagProtocollo) {
  this.flagProtocollo = flagProtocollo;
  }
        
    /**
	 * @return the dataDichiarazione
	 */
    public java.util.Date getDataDichiarazione() {
		return dataDichiarazione;
	}
    
	/**
	 * @param dataDichiarazione the dataDichiarazione to set
	 */
	public void setDataDichiarazione(java.util.Date dataDichiarazione) {
		this.dataDichiarazione = dataDichiarazione;
	}
	
	
	public ValidationErrors validate()
	{
	  ValidationErrors errors = new ValidationErrors();
	  if (getIdMotivo()==null || "".equals(getIdMotivo()))
		{
		  errors.add("motivo",new ValidationError((String)AnagErrors.get("ERR_CONSISTENZAVO_MOTIVO_OBBLIGATORIO")));
		}
    if (isPossibileProtocollo())
    {
      //la selezione del radioButton diventa obbligatoria
      if (this.getFlagProtocollo()==null)
        errors.add("flagProtocollo",new ValidationError("Selezionare una voce"));
    }
		if (getNote()!=null && getNote().length()>1000)
			errors.add("note",new ValidationError((String)AnagErrors.get("ERR_MAX_1000_CARATTERI")));
		return errors;
	}
  public boolean isPossibileProtocollo() {
    return possibileProtocollo;
  }
  public void setPossibileProtocollo(boolean possibileProtocollo) {
    this.possibileProtocollo = possibileProtocollo;
  }
	 
  public java.util.Date getDataAggiornamentoUV()
  {
    return dataAggiornamentoUV;
  }
  public void setDataAggiornamentoUV(java.util.Date dataAggiornamentoUV)
  {
    this.dataAggiornamentoUV = dataAggiornamentoUV;
  }
  public String getFlagAnomaliaUV()
  {
    return flagAnomaliaUV;
  }
  public void setFlagAnomaliaUV(String flagAnomaliaUV)
  {
    this.flagAnomaliaUV = flagAnomaliaUV;
  }
  public java.util.Date getDataAggiornamentoColtura()
  {
    return dataAggiornamentoColtura;
  }
  public void setDataAggiornamentoColtura(java.util.Date dataAggiornamentoColtura)
  {
    this.dataAggiornamentoColtura = dataAggiornamentoColtura;
  }
  public String getFlagAnomaliaColtura()
  {
    return flagAnomaliaColtura;
  }
  public void setFlagAnomaliaColtura(String flagAnomaliaColtura)
  {
    this.flagAnomaliaColtura = flagAnomaliaColtura;
  }
  public java.util.Date getDataAggiornamentoFabbricati()
  {
    return dataAggiornamentoFabbricati;
  }
  public void setDataAggiornamentoFabbricati(
      java.util.Date dataAggiornamentoFabbricati)
  {
    this.dataAggiornamentoFabbricati = dataAggiornamentoFabbricati;
  }
  public String getFlagAnomaliaFabbricati()
  {
    return flagAnomaliaFabbricati;
  }
  public void setFlagAnomaliaFabbricati(String flagAnomaliaFabbricati)
  {
    this.flagAnomaliaFabbricati = flagAnomaliaFabbricati;
  }
  public java.util.Date getDataAggiornamentoCC()
  {
    return dataAggiornamentoCC;
  }
  public void setDataAggiornamentoCC(java.util.Date dataAggiornamentoCC)
  {
    this.dataAggiornamentoCC = dataAggiornamentoCC;
  }
  public String getFlagAnomaliaCC()
  {
    return flagAnomaliaCC;
  }
  public void setFlagAnomaliaCC(String flagAnomaliaCC)
  {
    this.flagAnomaliaCC = flagAnomaliaCC;
  }
  public String getSchedulaFascicoli()
  {
    return schedulaFascicoli;
  }
  public void setSchedulaFascicoli(String schedulaFascicoli)
  {
    this.schedulaFascicoli = schedulaFascicoli;
  }
  public InvioFascicoliVO getInvioFascicoliVO()
  {
    return invioFascicoliVO;
  }
  public void setInvioFascicoliVO(InvioFascicoliVO invioFascicoliVO)
  {
    this.invioFascicoliVO = invioFascicoliVO;
  }
  public Long getExtIdTipoDocumentoIndex()
  {
    return extIdTipoDocumentoIndex;
  }
  public void setExtIdTipoDocumentoIndex(Long extIdTipoDocumentoIndex)
  {
    this.extIdTipoDocumentoIndex = extIdTipoDocumentoIndex;
  }
  public Long getIdAllegato()
  {
    return idAllegato;
  }
  public void setIdAllegato(Long idAllegato)
  {
    this.idAllegato = idAllegato;
  }
  public Long getIdTipoFirma()
  {
    return idTipoFirma;
  }
  public void setIdTipoFirma(Long idTipoFirma)
  {
    this.idTipoFirma = idTipoFirma;
  }
  public Long getIdAllegatoDichiarazione()
  {
    return idAllegatoDichiarazione;
  }
  public void setIdAllegatoDichiarazione(Long idAllegatoDichiarazione)
  {
    this.idAllegatoDichiarazione = idAllegatoDichiarazione;
  }
  public Date getDataRichiestaStampa()
  {
    return dataRichiestaStampa;
  }
  public void setDataRichiestaStampa(Date dataRichiestaStampa)
  {
    this.dataRichiestaStampa = dataRichiestaStampa;
  }
  public Vector getvAllegatoDichiarazioneVO()
  {
    return vAllegatoDichiarazioneVO;
  }
  public void setvAllegatoDichiarazioneVO(
      Vector vAllegatoDichiarazioneVO)
  {
    this.vAllegatoDichiarazioneVO = vAllegatoDichiarazioneVO;
  }
  public Long getExtIdDocumentoIndex()
  {
    return extIdDocumentoIndex;
  }
  public void setExtIdDocumentoIndex(Long extIdDocumentoIndex)
  {
    this.extIdDocumentoIndex = extIdDocumentoIndex;
  }
  public String getStileFirma()
  {
    return stileFirma;
  }
  public void setStileFirma(String stileFirma)
  {
    this.stileFirma = stileFirma;
  }
  public String getDescrizioneTipoFirma()
  {
    return descrizioneTipoFirma;
  }
  public void setDescrizioneTipoFirma(String descrizioneTipoFirma)
  {
    this.descrizioneTipoFirma = descrizioneTipoFirma;
  }
  public String getCodiceUtilita()
  {
    return codiceUtilita;
  }
  public void setCodiceUtilita(String codiceUtilita)
  {
    this.codiceUtilita = codiceUtilita;
  }
  public Long getIdAccessoPianoGrafico()
  {
    return idAccessoPianoGrafico;
  }
  public void setIdAccessoPianoGrafico(Long idAccessoPianoGrafico)
  {
    this.idAccessoPianoGrafico = idAccessoPianoGrafico;
  }
  
  
  
  
}
