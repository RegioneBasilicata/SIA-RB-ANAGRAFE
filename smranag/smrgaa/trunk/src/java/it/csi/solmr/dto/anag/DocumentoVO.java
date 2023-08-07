package it.csi.solmr.dto.anag;

import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Value Object che mappa la tabella DB_DOCUMENTO
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
public class DocumentoVO implements Serializable {

	private static final long serialVersionUID = 4562189970856356817L;
	
	private Long idDocumento = null;
	private Long extIdDocumento = null;
	private CodeDescription tipoTipologiaDocumento = null;
	private TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = null;
	private Long idStatoDocumento = null;
//non usato nei confronti poichè si usa id_stato_DOCUMENTO
	private String descStatoDocumento = null; 
	private Long idAzienda = null;
	private String cuaa = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private String numeroProtocollo = null;
	private Date dataProtocollo = null;
	private String numeroDocumento = null;
	private String enteRilascioDocumento = null;
	private Long idDocumentoPrecedente = null;
	private String note = null;
	private Date dataUltimoAggiornamento = null;
	private Long utenteUltimoAggiornamento = null;
	private Date dataInserimento = null;
	private Date dataVariazioneStato = null;
	private Long idUtenteAggiornamentoSrv = null;
	private String numeroProtocolloEsterno = null;
	private String cuaaSoccidario = null;
	private String esitoControllo = null;
	private Date dataEsecuzione = null;
	private String flagCuaaSoccidarioValidato = null;
	private TipoDocumentoVO tipoDocumentoVO = null;
	private TipoStatoDocumentoVO tipoStatoDocumentoVO = null;
	private Vector<DocumentoProprietarioVO> elencoProprietari = null;
	private Vector<?> elencoParticelle = null;
	private UtenteIrideVO utenteAggiornamento = null;
	private DocumentoConduzioneVO documentoConduzioneVO = new DocumentoConduzioneVO();
	private DocumentoConduzioneVO[] elencoDocumentoConduzione = null;
	//Usato nell'elenco documento per discriminare il documento istanza di riesame dagli altri
	//non usato nei vari controlli di egualianza
	private String flagIstanzaRiesame = null;
	private String flagIstanzaRiesameNoModTotale = null;
	private String flagTastoParticelle = null;
	private int faseIstanzaRiesame = 0;
	private String extraSistemaIstanzaRiesame = null;

	// Variabile di appoggio
	private String protocolla = null;
	private String checkScaduti = null;
	private java.util.Date minDataFineValidita = null;
	private java.util.Date maxDataFineValidita = null;
	
	private Vector<?> particelleAssociate;
	
	private Long idContoCorrente;
	
	private Long idCausaleModificaDocumento;
	//Non usato nei vari confronti al suo posto usato l'idCausaleModificaDocumento
	private String descCausaleModificaDocumento;
	
	private Vector<AllegatoDocumentoVO> vAllegatoDocumento;
	
	public Vector<?> getParticelleAssociate()
  {
    return particelleAssociate;
  }

  public void setParticelleAssociate(Vector<?> particelleAssociate)
  {
    this.particelleAssociate = particelleAssociate;
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
	 * @return the extIdDocumento
	 */
	public Long getExtIdDocumento() {
		return extIdDocumento;
	}

	/**
	 * @param extIdDocumento the extIdDocumento to set
	 */
	public void setExtIdDocumento(Long extIdDocumento) {
		this.extIdDocumento = extIdDocumento;
	}

	/**
	 * @return the tipoTipologiaDocumento
	 */
	public CodeDescription getTipoTipologiaDocumento() {
		return tipoTipologiaDocumento;
	}

	/**
	 * @param tipoTipologiaDocumento the tipoTipologiaDocumento to set
	 */
	public void setTipoTipologiaDocumento(CodeDescription tipoTipologiaDocumento) {
		this.tipoTipologiaDocumento = tipoTipologiaDocumento;
	}

	/**
	 * @return the tipoCategoriaDocumentoVO
	 */
	public TipoCategoriaDocumentoVO getTipoCategoriaDocumentoVO() {
		return tipoCategoriaDocumentoVO;
	}

	/**
	 * @param tipoCategoriaDocumentoVO the tipoCategoriaDocumentoVO to set
	 */
	public void setTipoCategoriaDocumentoVO(
			TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO) {
		this.tipoCategoriaDocumentoVO = tipoCategoriaDocumentoVO;
	}

	/**
	 * @return the idStatoDocumento
	 */
	public Long getIdStatoDocumento() {
		return idStatoDocumento;
	}

	/**
	 * @param idStatoDocumento the idStatoDocumento to set
	 */
	public void setIdStatoDocumento(Long idStatoDocumento) {
		this.idStatoDocumento = idStatoDocumento;
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
	 * @return the numeroProtocollo
	 */
	public String getNumeroProtocollo() {
		return numeroProtocollo;
	}

	/**
	 * @param numeroProtocollo the numeroProtocollo to set
	 */
	public void setNumeroProtocollo(String numeroProtocollo) {
		this.numeroProtocollo = numeroProtocollo;
	}

	/**
	 * @return the dataProtocollo
	 */
	public Date getDataProtocollo() {
		return dataProtocollo;
	}

	/**
	 * @param dataProtocollo the dataProtocollo to set
	 */
	public void setDataProtocollo(Date dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the enteRilascioDocumento
	 */
	public String getEnteRilascioDocumento() {
		return enteRilascioDocumento;
	}

	/**
	 * @param enteRilascioDocumento the enteRilascioDocumento to set
	 */
	public void setEnteRilascioDocumento(String enteRilascioDocumento) {
		this.enteRilascioDocumento = enteRilascioDocumento;
	}

	/**
	 * @return the idDocumentoPrecedente
	 */
	public Long getIdDocumentoPrecedente() {
		return idDocumentoPrecedente;
	}

	/**
	 * @param idDocumentoPrecedente the idDocumentoPrecedente to set
	 */
	public void setIdDocumentoPrecedente(Long idDocumentoPrecedente) {
		this.idDocumentoPrecedente = idDocumentoPrecedente;
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
	 * @return the dataUltimoAggiornamento
	 */
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	/**
	 * @param dataUltimoAggiornamento the dataUltimoAggiornamento to set
	 */
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	/**
	 * @return the utenteUltimoAggiornamento
	 */
	public Long getUtenteUltimoAggiornamento() {
		return utenteUltimoAggiornamento;
	}

	/**
	 * @param utenteUltimoAggiornamento the utenteUltimoAggiornamento to set
	 */
	public void setUtenteUltimoAggiornamento(Long utenteUltimoAggiornamento) {
		this.utenteUltimoAggiornamento = utenteUltimoAggiornamento;
	}

	/**
	 * @return the dataInserimento
	 */
	public Date getDataInserimento() {
		return dataInserimento;
	}

	/**
	 * @param dataInserimento the dataInserimento to set
	 */
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	/**
	 * @return the dataVariazioneStato
	 */
	public Date getDataVariazioneStato() {
		return dataVariazioneStato;
	}

	/**
	 * @param dataVariazioneStato the dataVariazioneStato to set
	 */
	public void setDataVariazioneStato(Date dataVariazioneStato) {
		this.dataVariazioneStato = dataVariazioneStato;
	}

	/**
	 * @return the idUtenteAggiornamentoSrv
	 */
	public Long getIdUtenteAggiornamentoSrv() {
		return idUtenteAggiornamentoSrv;
	}

	/**
	 * @param idUtenteAggiornamentoSrv the idUtenteAggiornamentoSrv to set
	 */
	public void setIdUtenteAggiornamentoSrv(Long idUtenteAggiornamentoSrv) {
		this.idUtenteAggiornamentoSrv = idUtenteAggiornamentoSrv;
	}

	/**
	 * @return the numeroProtocolloEsterno
	 */
	public String getNumeroProtocolloEsterno() {
		return numeroProtocolloEsterno;
	}

	/**
	 * @param numeroProtocolloEsterno the numeroProtocolloEsterno to set
	 */
	public void setNumeroProtocolloEsterno(String numeroProtocolloEsterno) {
		this.numeroProtocolloEsterno = numeroProtocolloEsterno;
	}

	/**
	 * @return the cuaaSoccidario
	 */
	public String getCuaaSoccidario() {
		return cuaaSoccidario;
	}

	/**
	 * @param cuaaSoccidario the cuaaSoccidario to set
	 */
	public void setCuaaSoccidario(String cuaaSoccidario) {
		this.cuaaSoccidario = cuaaSoccidario;
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
	 * @return the flagCuaaSoccidarioValidato
	 */
	public String getFlagCuaaSoccidarioValidato() {
		return flagCuaaSoccidarioValidato;
	}

	/**
	 * @param flagCuaaSoccidarioValidato the flagCuaaSoccidarioValidato to set
	 */
	public void setFlagCuaaSoccidarioValidato(String flagCuaaSoccidarioValidato) {
		this.flagCuaaSoccidarioValidato = flagCuaaSoccidarioValidato;
	}

	/**
	 * @return the tipoDocumentoVO
	 */
	public TipoDocumentoVO getTipoDocumentoVO() {
		return tipoDocumentoVO;
	}

	/**
	 * @param tipoDocumentoVO the tipoDocumentoVO to set
	 */
	public void setTipoDocumentoVO(TipoDocumentoVO tipoDocumentoVO) {
		this.tipoDocumentoVO = tipoDocumentoVO;
	}

	/**
	 * @return the tipoStatoDocumentoVO
	 */
	public TipoStatoDocumentoVO getTipoStatoDocumentoVO() {
		return tipoStatoDocumentoVO;
	}

	/**
	 * @param tipoStatoDocumentoVO the tipoStatoDocumentoVO to set
	 */
	public void setTipoStatoDocumentoVO(TipoStatoDocumentoVO tipoStatoDocumentoVO) {
		this.tipoStatoDocumentoVO = tipoStatoDocumentoVO;
	}

	/**
	 * @return the elencoProprietari
	 */
	public Vector<DocumentoProprietarioVO> getElencoProprietari() {
		return elencoProprietari;
	}

	/**
	 * @param elencoProprietari the elencoProprietari to set
	 */
	public void setElencoProprietari(Vector<DocumentoProprietarioVO> elencoProprietari) {
		this.elencoProprietari = elencoProprietari;
	}

	/**
	 * @return the elencoParticelle
	 */
	public Vector<?> getElencoParticelle() {
		return elencoParticelle;
	}

	/**
	 * @param elencoParticelle the elencoParticelle to set
	 */
	public void setElencoParticelle(Vector<?> elencoParticelle) {
		this.elencoParticelle = elencoParticelle;
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
	 * @return the documentoConduzioneVO
	 */
	public DocumentoConduzioneVO getDocumentoConduzioneVO() {
		return documentoConduzioneVO;
	}

	/**
	 * @param documentoConduzioneVO the documentoConduzioneVO to set
	 */
	public void setDocumentoConduzioneVO(DocumentoConduzioneVO documentoConduzioneVO) {
		this.documentoConduzioneVO = documentoConduzioneVO;
	}

	/**
	 * @return the elencoDocumentoConduzione
	 */
	public DocumentoConduzioneVO[] getElencoDocumentoConduzione() {
		return elencoDocumentoConduzione;
	}

	/**
	 * @param elencoDocumentoConduzione the elencoDocumentoConduzione to set
	 */
	public void setElencoDocumentoConduzione(
			DocumentoConduzioneVO[] elencoDocumentoConduzione) {
		this.elencoDocumentoConduzione = elencoDocumentoConduzione;
	}

	/**
	 * @return the protocolla
	 */
	public String getProtocolla() {
		return protocolla;
	}

	/**
	 * @param protocolla the protocolla to set
	 */
	public void setProtocolla(String protocolla) {
		this.protocolla = protocolla;
	}

	/**
	 * @return the checkScaduti
	 */
	public String getCheckScaduti() {
		return checkScaduti;
	}

	/**
	 * @param checkScaduti the checkScaduti to set
	 */
	public void setCheckScaduti(String checkScaduti) {
		this.checkScaduti = checkScaduti;
	}

	/**
	 * @return the minDataFineValidita
	 */
	public java.util.Date getMinDataFineValidita() {
		return minDataFineValidita;
	}

	/**
	 * @param minDataFineValidita the minDataFineValidita to set
	 */
	public void setMinDataFineValidita(java.util.Date minDataFineValidita) {
		this.minDataFineValidita = minDataFineValidita;
	}

	/**
	 * @return the maxDataFineValidita
	 */
	public java.util.Date getMaxDataFineValidita() {
		return maxDataFineValidita;
	}

	/**
	 * @param maxDataFineValidita the maxDataFineValidita to set
	 */
	public void setMaxDataFineValidita(java.util.Date maxDataFineValidita) {
		this.maxDataFineValidita = maxDataFineValidita;
	}
	
  
		
		
	/**
   * Restituisce null se i due oggetti sono uguali
   * size == 0 se uno dei due oggetti è null
   * > 0 altrimenti
   */
  public Vector<Object> equalsVect(Object obj) {
    Vector<Object> vect = new Vector<Object>();
    
    if (this == obj)
      return null;
    if (obj == null)
      return vect;
    if (getClass() != obj.getClass())
      return vect;
    final DocumentoVO other = (DocumentoVO) obj;
    //checkScaduti
    if (!Validator.isNotEmpty(checkScaduti)) {
      if (Validator.isNotEmpty(other.checkScaduti)) {
        vect.add("checkScaduti");
      }
    } else if (!checkScaduti.equals(other.checkScaduti)) {        
      vect.add("checkScaduti");
    }
    //Cuaa
    if (!Validator.isNotEmpty(cuaa)) {
      if (Validator.isNotEmpty(other.cuaa)) {
        vect.add("cuaa");
      }
    } else if (!cuaa.equals(other.cuaa)) {
      vect.add("cuaa");
    }
    // Cuaa soccidario
    if(!Validator.isNotEmpty(this.cuaaSoccidario)) {
      if(Validator.isNotEmpty(other.cuaaSoccidario)) {
        vect.add("cuaaSoccidario");
      }
    }
    else if(!Validator.isNotEmpty(other.cuaaSoccidario)) {
      if(Validator.isNotEmpty(this.cuaaSoccidario)) {
        vect.add("cuaaSoccidario");
      }
    }
    else if(!this.cuaaSoccidario.equalsIgnoreCase(other.cuaaSoccidario)) {
      vect.add("cuaaSoccidario");
    }
    //dataEsecuzione
    compareDate(vect, dataEsecuzione, other.dataEsecuzione, "dataEsecuzione");
    //dataFineValidita
    compareDate(vect, dataFineValidita, other.dataFineValidita, "dataFineValidita");
    // dataInizioValidita
    compareDate(vect, dataInizioValidita, other.dataInizioValidita, "dataInizioValidita");
    // dataInserimento
    compareDate(vect, dataInserimento, other.dataInserimento, "dataInserimento");
    // dataProtocollo
    compareDate(vect, dataProtocollo, other.dataProtocollo, "dataProtocollo");
    //dataUltimoAggiornamento
    compareDate(vect, dataUltimoAggiornamento, other.dataUltimoAggiornamento, "dataUltimoAggiornamento");
    // dataVariazioneStato
    compareDate(vect, dataVariazioneStato, other.dataVariazioneStato, "dataVariazioneStato");
    // Elenco particelle
    if(!Validator.isNotEmpty(elencoParticelle)) {
      if(Validator.isNotEmpty(other.elencoParticelle)) {
        vect.add("elencoParticelle");
      }
    }
    else if(!Validator.isNotEmpty(other.elencoParticelle)) {
      if(Validator.isNotEmpty(this.elencoParticelle)) {
        vect.add("elencoParticelle");
      }
    }
    else if(this.elencoParticelle.size() != other.elencoParticelle.size()) {
      vect.add("elencoParticelle");
    }
    else 
    {
      for(int i = 0; i < this.elencoParticelle.size(); i++) 
      {
        StoricoParticellaVO thisAltro = (StoricoParticellaVO)this.elencoParticelle.elementAt(i);
        StoricoParticellaVO otherAltro = (StoricoParticellaVO)other.elencoParticelle.elementAt(i);
        // Conduzione particella
        if(thisAltro.getElencoConduzioni()[0].getIdConduzioneParticella().compareTo(otherAltro.getElencoConduzioni()[0].getIdConduzioneParticella()) != 0) 
        {
          if(!vect.contains("elencoParticelle"))
          {
            vect.add("elencoParticelle");
          }
        }
        
        //controllo differenza note/lavorazione prioritaria
        if(Validator.isNotEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione())
          && Validator.isEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
        {
          vect.add("elencoParticelle");          
        }
        else if(Validator.isEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione())
            && Validator.isNotEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
        {
          vect.add("elencoParticelle");          
        }
        else if(Validator.isNotEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione())
            && Validator.isNotEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
        {
          if(Validator.isNotEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote())
             && Validator.isEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()))
          {
            vect.add("elencoParticelle");     
          }
          else if (Validator.isEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote())
              && Validator.isNotEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()))
          {
            vect.add("elencoParticelle");  
          }
          else if (Validator.isNotEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote())
              && Validator.isNotEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()))
          {
            if(!thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote().equals(
                otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getNote()))
            {
              vect.add("elencoParticelle");  
            }
          }
          
          
          if(Validator.isNotEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria())
              && Validator.isEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria()))
           {
             vect.add("elencoParticelle");     
           }
           else if (Validator.isEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria())
               && Validator.isNotEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria()))
           {
             vect.add("elencoParticelle");  
           }
           else if (Validator.isNotEmpty(thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria())
               && Validator.isNotEmpty(otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria()))
           {
             if(!thisAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria().equals(
                 otherAltro.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria()))
             {
               vect.add("elencoParticelle");  
             }
           }
          
          
             
          
        }
      }
    }
    
    
    
    //Particelle associate
    if(!Validator.isNotEmpty(particelleAssociate)) 
    {
      if(Validator.isNotEmpty(other.particelleAssociate)) 
      {
        vect.add("particelleAssociate");
      }
    }
    else 
      if(!Validator.isNotEmpty(other.particelleAssociate)) 
      {
        if(Validator.isNotEmpty(this.particelleAssociate)) 
        {
          vect.add("particelleAssociate");
        }
      }
      else 
        if(this.particelleAssociate.size() != other.particelleAssociate.size()) 
        {
          vect.add("particelleAssociate");
        }
        else 
        {
          for(int i = 0; i < this.particelleAssociate.size(); i++) 
          {
            ParticellaAssVO thisAltro = (ParticellaAssVO)this.particelleAssociate.elementAt(i);
            ParticellaAssVO otherAltro = (ParticellaAssVO)other.particelleAssociate.elementAt(i);
            // Conduzione particella
            if(!thisAltro.equalsForUpadate(otherAltro)) 
            {
                vect.add("particelleAssociate");
                break;
            }
          }
        }
    
    
    
    // Elenco proprietari
    if(!Validator.isNotEmpty(this.elencoProprietari)) {
      if(Validator.isNotEmpty(other.elencoProprietari)) {
        vect.add("elencoProprietari");
      }
    }
    else if(!Validator.isNotEmpty(other.elencoProprietari)) {
      if(Validator.isNotEmpty(this.elencoProprietari)) {
        vect.add("elencoProprietari");
      }
    }
    else if(this.elencoProprietari.size() != other.elencoProprietari.size()) {
      vect.add("elencoProprietari");
    }
    else {
      for(int i = 0; i < this.elencoProprietari.size(); i++) 
      {
        DocumentoProprietarioVO thisAltro = (DocumentoProprietarioVO)this.elencoProprietari.elementAt(i);
        DocumentoProprietarioVO otherAltro = (DocumentoProprietarioVO)other.elencoProprietari.elementAt(i);
        // Cuaa
        if(!thisAltro.getCuaa().equalsIgnoreCase(otherAltro.getCuaa())) 
        {
          if(!vect.contains("elencoProprietari"))
          {
            vect.add("elencoProprietari");
          }
        }
        // Denominazione
        if(!Validator.isNotEmpty(thisAltro.getDenominazione())) 
        {
          if(Validator.isNotEmpty(otherAltro.getDenominazione())) 
          {
            if(!vect.contains("elencoProprietari"))
            {
              vect.add("elencoProprietari");
            }
          }
        }
        else if(!Validator.isNotEmpty(otherAltro.getDenominazione())) 
        {
          if(Validator.isNotEmpty(thisAltro.getDenominazione())) 
          {
            if(!vect.contains("elencoProprietari"))
            {
              vect.add("elencoProprietari");
            }
          }
        }
        else if(!thisAltro.getDenominazione().equalsIgnoreCase(otherAltro.getDenominazione())) 
        {
          if(!vect.contains("elencoProprietari"))
          {
            vect.add("elencoProprietari");
          }
        }
      }
    }
    // enteRilascioDocumento
    if (!Validator.isNotEmpty(enteRilascioDocumento)) {
      if (Validator.isNotEmpty(other.enteRilascioDocumento)) {
        vect.add("enteRilascioDocumento");
      }
    } else if (!enteRilascioDocumento.equals(other.enteRilascioDocumento)) {
      vect.add("enteRilascioDocumento");
    }
    //esitoControllo
    if (!Validator.isNotEmpty(esitoControllo)) {
      if (Validator.isNotEmpty(other.esitoControllo)) {
        vect.add("esitoControllo");
      }
    } else if (!esitoControllo.equals(other.esitoControllo)) {
      vect.add("esitoControllo");
    }
    //extIdDocumento
    if (!Validator.isNotEmpty(extIdDocumento)) {
      if (Validator.isNotEmpty(other.extIdDocumento)) {
        vect.add("extIdDocumento");
      }
    } else if (!extIdDocumento.equals(other.extIdDocumento)) {
      vect.add("extIdDocumento");
    }
    //flagCuaaSoccidarioValidato
    if (!Validator.isNotEmpty(flagCuaaSoccidarioValidato)) {
      if (Validator.isNotEmpty(other.flagCuaaSoccidarioValidato)) {
        vect.add("flagCuaaSoccidarioValidato");
      }
    } else if (!flagCuaaSoccidarioValidato.equals(other.flagCuaaSoccidarioValidato)) {
      vect.add("flagCuaaSoccidarioValidato");
    }
    //idAzienda
    if (!Validator.isNotEmpty(idAzienda)) {
      if (Validator.isNotEmpty(other.idAzienda)) {
        vect.add("idAzienda");
      }
    } else if (!idAzienda.equals(other.idAzienda)) {
      vect.add("idAzienda");
    }
    //idDocumento
    if (!Validator.isNotEmpty(idDocumento)) {
      if (Validator.isNotEmpty(other.idDocumento)) {
        vect.add("idDocumento");
      }
    } else if (!idDocumento.equals(other.idDocumento)) {
      vect.add("idDocumento");
    }
    //idDocumentoPrecedente
    if (!Validator.isNotEmpty(idDocumentoPrecedente)) {
      if (Validator.isNotEmpty(other.idDocumentoPrecedente)) {
        vect.add("idDocumentoPrecedente");
      }
    } else if (!idDocumentoPrecedente.equals(other.idDocumentoPrecedente)) {
      vect.add("idDocumentoPrecedente");
    }
    //idStatoDocumento
    if (!Validator.isNotEmpty(idStatoDocumento)) {
      if (Validator.isNotEmpty(other.idStatoDocumento)) {
        vect.add("idStatoDocumento");
      }
    } else if (!idStatoDocumento.equals(other.idStatoDocumento)) {
      vect.add("idStatoDocumento");
    }
    //idUtenteAggiornamentoSrv
    if (!Validator.isNotEmpty(idUtenteAggiornamentoSrv)) {
      if (Validator.isNotEmpty(other.idUtenteAggiornamentoSrv)) {
        vect.add("idUtenteAggiornamentoSrv");
      }
    } else if (!idUtenteAggiornamentoSrv.equals(other.idUtenteAggiornamentoSrv)) {
      vect.add("idUtenteAggiornamentoSrv");
    }
    //maxDataFineValidita
    compareDate(vect, maxDataFineValidita, other.maxDataFineValidita, "maxDataFineValidita");
    //minDataFineValidita
    compareDate(vect, minDataFineValidita, other.minDataFineValidita, "minDataFineValidita");
    //note
    if (!Validator.isNotEmpty(note)) {
      if (Validator.isNotEmpty(other.note)) {
        vect.add("note");
      }
    } else if (!note.equals(other.note)) {
      vect.add("note");
    }
    //numeroDocumento
    if (!Validator.isNotEmpty(numeroDocumento)) {
      if (Validator.isNotEmpty(other.numeroDocumento)) {
        vect.add("numeroDocumento");
      }
    } else if (!numeroDocumento.equals(other.numeroDocumento)) {
      vect.add("numeroDocumento");
    }
    //numeroProtocollo
    if (!Validator.isNotEmpty(numeroProtocollo)) {
      if (Validator.isNotEmpty(other.numeroProtocollo)) {
        vect.add("numeroProtocollo");
      }
    } else if (!numeroProtocollo.equals(other.numeroProtocollo)) {
      vect.add("numeroProtocollo");
    }
    //numeroProtocolloEsterno
    if (!Validator.isNotEmpty(numeroProtocolloEsterno)) {
      if (Validator.isNotEmpty(other.numeroProtocolloEsterno)) {
        vect.add("numeroProtocolloEsterno");
      }
    } else if (!numeroProtocolloEsterno.equals(other.numeroProtocolloEsterno)) {
      vect.add("numeroProtocolloEsterno");
    }
    //protocolla
    if (!Validator.isNotEmpty(protocolla)) {
      if (Validator.isNotEmpty(other.protocolla)) {
        vect.add("protocolla");
      }
    } else if (!protocolla.equals(other.protocolla)) {
      vect.add("protocolla");
    }
    //tipoCategoriaDocumentoVO
    if (!Validator.isNotEmpty(tipoCategoriaDocumentoVO)) {
      if (Validator.isNotEmpty(other.tipoCategoriaDocumentoVO)) {
        vect.add("tipoCategoriaDocumentoVO");
      }
    } else if (!tipoCategoriaDocumentoVO.equals(other.tipoCategoriaDocumentoVO)) {
      vect.add("tipoCategoriaDocumentoVO");
    }
    //tipoDocumentoVO
    if (!Validator.isNotEmpty(tipoDocumentoVO)) {
      if (Validator.isNotEmpty(other.tipoDocumentoVO)) {
        vect.add("tipoDocumentoVO");
      }
    } else if (!tipoDocumentoVO.equals(other.tipoDocumentoVO)) {
      vect.add("tipoDocumentoVO");
    }
    // tipoStatoDocumentoVO
    if (!Validator.isNotEmpty(tipoStatoDocumentoVO)) {
      if (Validator.isNotEmpty(other.tipoStatoDocumentoVO)) {
        vect.add("tipoStatoDocumentoVO");
      }
    } else if (!tipoStatoDocumentoVO.equals(other.tipoStatoDocumentoVO)) {
      vect.add("tipoStatoDocumentoVO");
    }
    //tipoTipologiaDocumento
    if (!Validator.isNotEmpty(tipoTipologiaDocumento)) {
      if (Validator.isNotEmpty(other.tipoTipologiaDocumento)) {
        vect.add("tipoTipologiaDocumento");
      }
    } else if (!tipoTipologiaDocumento.equals(other.tipoTipologiaDocumento)) {
      vect.add("tipoTipologiaDocumento");
    }
    //utenteAggiornamento
    if (!Validator.isNotEmpty(utenteAggiornamento)) {
      if (Validator.isNotEmpty(other.utenteAggiornamento)) {
        vect.add("utenteAggiornamento");
      }
    } else if (!utenteAggiornamento.equals(other.utenteAggiornamento)) {
      vect.add("utenteAggiornamento");
    }
    //utenteUltimoAggiornamento
    if (!Validator.isNotEmpty(utenteUltimoAggiornamento)) {
      if (Validator.isNotEmpty(other.utenteUltimoAggiornamento)) {
        vect.add("utenteUltimoAggiornamento");
      }
    } else if (!utenteUltimoAggiornamento.equals(other.utenteUltimoAggiornamento)) {
      vect.add("utenteUltimoAggiornamento");
    }
    //documentoConduzioneVO
    /*if(!Validator.isNotEmpty(documentoConduzioneVO)) {
      if (Validator.isNotEmpty(other.documentoConduzioneVO)) {
        vect.add("documentoConduzioneVO");
      }
    }
    if(!Validator.isNotEmpty(other.documentoConduzioneVO)) {
      if(Validator.isNotEmpty(documentoConduzioneVO)) {
        vect.add("documentoConduzioneVO");
      }
    }
    else if(!documentoConduzioneVO.equals(other.documentoConduzioneVO)) {
      vect.add("documentoConduzioneVO");
    }*/
    //idContoCorrente
    if (!Validator.isNotEmpty(idContoCorrente)) {
      if (Validator.isNotEmpty(other.idContoCorrente)) {
        vect.add("idContoCorrente");
      }
    } else if (!idContoCorrente.equals(other.idContoCorrente)) {
      vect.add("idContoCorrente");
    }
    
    //idCausaleModificaDocumento
    if (!Validator.isNotEmpty(idCausaleModificaDocumento)) {
      if (Validator.isNotEmpty(other.idCausaleModificaDocumento)) {
        vect.add("idCausaleModificaDocumento");
      }
    } else if (!idCausaleModificaDocumento.equals(other.idCausaleModificaDocumento)) {
      vect.add("idCausaleModificaDocumento");
    }
    
    //Ho passato i primi controlli sugli oggetti
    //ma non ho riscontrato differenze sulle singole property di essi
    if(vect.size() == 0)
      return null;
    
    return vect;
  }

	/**
	 * Metodo che si occupa di effettuare la validazione formale dei dati relativi all'inserimento del documento
	 * 
	 * @param request HttpServletRequest
	 * @return ValidationErrors
	 */
 	public ValidationErrors validateInsertDocumento(HttpServletRequest request) 
 	{
 		ValidationErrors errors = new ValidationErrors();
 		// La data inizio validità è obbligatoria
 		String strDataInizioValidita = request.getParameter("dataInizioValidita");
 		String strDataFineValidita = request.getParameter("dataFineValidita");
 		protocolla = request.getParameter("protocolla");
 		numeroProtocolloEsterno = request.getParameter("numeroProtocolloEsterno");
 		note = request.getParameter("note");
 		numeroDocumento = request.getParameter("numeroDocumento");
 		enteRilascioDocumento = request.getParameter("enteRilascioDocumento");
 		cuaaSoccidario = request.getParameter("cuaaSoccidario");
 		try 
 		{
 			if(!Validator.isNotEmpty(strDataInizioValidita)) 
 			{
 				errors.add("dataInizioValidita", new ValidationError((String)AnagErrors.get("ERR_CAMPO_OBBLIGATORIO")));
 			}
 			else 
 			{
 				if(!Validator.validateDateF(strDataInizioValidita)) 
 				{
 					errors.add("dataInizioValidita", new ValidationError((String)AnagErrors.get("ERR_DATA_NON_CORRETTA")));
 				}
 				else 
 				{
 					dataInizioValidita = DateUtils.parseDate(strDataInizioValidita);
 				}
 				if(Validator.isNotEmpty(strDataFineValidita) && Validator.validateDateF(strDataFineValidita)) 
 				{
 					if(dataInizioValidita.after(DateUtils.parseDate(strDataFineValidita))) 
 					{
 						errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
 					}
 				}
 			}
 			// Se flag_obbligo data fine = S la data fine è obbligatoria
 			if(tipoDocumentoVO != null && Validator.isNotEmpty(tipoDocumentoVO.getFlagObbligoDataFine())) 
 			{
 				if(tipoDocumentoVO.getFlagObbligoDataFine().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
 				{
 					if(!Validator.isNotEmpty(strDataFineValidita)) 
 					{
 						errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_DATA_FINE_VALIDITA_OBBLIGATORIA_FOR_DOCUMENT_SELECTED));
 					}
 				}
 			}
 			// Se è stata valorizzata controllo che sia una data valida
 			if(Validator.isNotEmpty(strDataFineValidita)) 
 			{ 			  
 				if(!Validator.validateDateF(strDataFineValidita)) 
 				{
 					errors.add("dataFineValidita", new ValidationError((String)AnagErrors.get("ERR_DATA_NON_CORRETTA")));
 				}
 				else 
 				{
 					dataFineValidita = DateUtils.parseDate(strDataFineValidita);
 				}
 			}
 			else 
 			{
 				dataFineValidita = null;
 			}
 			// Se il numero protocollo esterno è valorizzato...
 			if(Validator.isNotEmpty(numeroProtocolloEsterno)) 
 			{
 				// ... controllo che non sia più lungo di 100 caratteri
 				if(numeroProtocolloEsterno.length() > 100) 
 				{
 					errors.add("numeroDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 				}
 			}
 			// Se le note sono valorizzate...
 			if(Validator.isNotEmpty(note)) 
 			{
 				// ... controllo che non siano più lunghe di 500 caratteri
 				if(note.length() > 500) 
 				{
 					errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 				}
 			}
 			if(tipoDocumentoVO != null && Validator.isNotEmpty(tipoDocumentoVO.getFlagAnagTerr())) 
 			{
 				// Se è stato scelto un tipo documento anagrafico
 				if(tipoDocumentoVO.getFlagAnagTerr()
 				    .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI)) 
 				{
 					// Se FLAG_OBBLIGO_ENTE_NUMERO del tipo documento selezionato è uguale a S
 					if(tipoDocumentoVO.getFlagObbligoEnteNumero().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
 					{
 						// Il numero documento è obbligatorio
 						if(!Validator.isNotEmpty(numeroDocumento)) 
 						{
 							errors.add("numeroDocumento", new ValidationError((String)AnagErrors.get("ERR_CAMPO_OBBLIGATORIO")));
 						}
 						else 
 						{
 							if(numeroDocumento.length() > 20) 
 							{
 								errors.add("numeroDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 							}
 						}
 						// L'ente rilascio documento è obbligatorio
 						if(!Validator.isNotEmpty(enteRilascioDocumento)) 
 						{
 							errors.add("enteRilascioDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 						}
 						else 
 						{	 							
 							if(enteRilascioDocumento.length() > 100) 
 							{
 								errors.add("enteRilascioDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 							}
 						}
 					}
 				}
 				// Se invece è stato scelto un documento di tipo zootecnico
     		else if(tipoDocumentoVO.getFlagAnagTerr()
     		    .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI)) 
     		{
     			// Controllo che il campo CUAA soccidario sia stato valorizzato, solo nel caso in cui flag_obbligo proprietario
     			// sia uguale a S
     			if(tipoDocumentoVO.getFlagObbligoProprietario()
     			    .equalsIgnoreCase(SolmrConstants.FLAG_S)) 
     			{
     				if(!Validator.isNotEmpty(cuaaSoccidario)) 
     				{
     					errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
     				}
     			}
     			// Se valorizzato...
     			if(Validator.isNotEmpty(cuaaSoccidario)) 
     			{
     				// ... controllo che sia un CUAA valido
     				if(cuaaSoccidario.length() != 11 && cuaaSoccidario.length() != 16) 
     				{
     					errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
     				}
     				else 
     				{
     					if(cuaaSoccidario.length() == 11) 
     					{
     						if(!Validator.controlloPIVA(cuaaSoccidario)) 
     						{
     							errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
     						}
     					}
     					else 
     					{
     						if(!Validator.controlloCf(cuaaSoccidario)) 
     						{
     							errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
     						}
     					}
     				}
     			}
     		}
 			}
 		}
 		catch (Exception e) {
 		}
 		return errors;
 	}

 	/**
 	 * Metodo che si occupa di valorizzare e controllare i valori inseriti dall'utente durante la modifica del documento
 	 * @param request HttpServletRequest
 	 * @return ValidationErrors
 	 */
 	public ValidationErrors validateUpdateDocumento(HttpServletRequest request) 
 	{
 		ValidationErrors errors = new ValidationErrors();
 		numeroProtocolloEsterno = request.getParameter("numeroProtocolloEsterno");
 		numeroDocumento = request.getParameter("numeroDocumento");
 		enteRilascioDocumento = request.getParameter("enteRilascioDocumento");
 		cuaaSoccidario = request.getParameter("cuaaSoccidario");
 		String idCausaleModificaDocumentoStr = request.getParameter("idCausaleModificaDocumento"); 
 		note = request.getParameter("note");
 		try 
 		{
 			// DATA INIZIO VALIDITA'
 			if(!Validator.isNotEmpty(request.getParameter("dataInizioValidita"))) 
 			{
 				errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 				dataInizioValidita = null;
 			}
 			else 
 			{
 				if((!Validator.validateDateF(request.getParameter("dataInizioValidita")))) 
 				{
 					errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 					dataInizioValidita = null;
 				}
 				else 
 				{
 					dataInizioValidita = DateUtils.parseDate(request.getParameter("dataInizioValidita"));
 					if(Validator.isNotEmpty(request.getParameter("dataFineValidita")) && Validator.validateDateF(request.getParameter("dataFineValidita"))) {
 						if(dataInizioValidita.after(DateUtils.parseDate(request.getParameter("dataFineValidita")))) {
 							errors.add("dataInizioValidita", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
 						}
 					}
 				}
 			}
 			// DATA FINE VALIDITA'
 			if(Validator.isNotEmpty(this.getTipoDocumentoVO().getFlagObbligoDataFine()) && this.getTipoDocumentoVO().getFlagObbligoDataFine().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 				if(!Validator.isNotEmpty(request.getParameter("dataFineValidita"))) {
 					errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_DATA_FINE_VALIDITA_OBBLIGATORIA_FOR_DOCUMENT_SELECTED));
 					dataFineValidita = null;
 				}
 			}
 			if(Validator.isNotEmpty(request.getParameter("dataFineValidita"))) 
 			{
 			  if(Validator.isNotEmpty(flagIstanzaRiesame)
          && "S".equalsIgnoreCase(flagIstanzaRiesame))
        {
 			    if(Validator.isNotEmpty(request.getParameter("dataFineValidita"))) 
          {
 			      errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE));
          }
        }
 			  else
 			  {
   			  if((!Validator.validateDateF(request.getParameter("dataFineValidita")))) 
          {
            errors.add("dataFineValidita", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            dataFineValidita = null;
          }
          else 
          {
            dataFineValidita = DateUtils.parseDate(request.getParameter("dataFineValidita"));
          }
 			  }
 			}
 			else 
 			{
 				dataFineValidita = null;
 			}
 			// Se il numero protocollo esterno è valorizzato...
 			if(Validator.isNotEmpty(numeroProtocolloEsterno)) {
 				// ... controllo che non sia più lungo di 100 caratteri
 				if(numeroProtocolloEsterno.length() > 100) {
 					errors.add("numeroDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 				}
 			}
 			else {
 				numeroProtocolloEsterno = null;
 			}
 			// Se la causale modifica è valorizzata...
 			if(Validator.isEmpty(idCausaleModificaDocumentoStr))
 			{
 			  errors.add("idCausaleModificaDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 			}
 			else
 			{
 			  if("S".equalsIgnoreCase(flagIstanzaRiesameNoModTotale))
 			  {
 			    if(!idCausaleModificaDocumentoStr.equalsIgnoreCase(SolmrConstants.CAUSALE_MOD_CORR_ANOMALIA))
 			    {
 			     errors.add("idCausaleModificaDocumento", new ValidationError(AnagErrors.ERRORE_IST_RIE_CAUS_MOD));
 			    }
 			  }
 			  idCausaleModificaDocumento = new Long(idCausaleModificaDocumentoStr);
 			}
 			
 	    //Se le note sono valorizzate...
 			if(Validator.isEmpty(note))
      {
        errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
      }
 			else
 			{
   			if(Validator.isNotEmpty(note)) 
        {
          // ... controllo che non siano più lunghe di 500 caratteri
          if(note.length() > 500) 
          {
            errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
          }
        }
        else 
        {
          note = null;
        }
 			}
 			
 			
 			/*if(Validator.isEmpty(note) && Validator.isEmpty(idCausaleModificaDocumentoStr))
 			{
 			  errors.add("note", new ValidationError(AnagErrors.ERR_OBB_NOTE_CAUSALE_MODIFICA));
 			  errors.add("idCausaleModificaDocumento", new ValidationError(AnagErrors.ERR_OBB_NOTE_CAUSALE_MODIFICA));
 			}
 			else
 			{
 			  
 			  if(Validator.isNotEmpty(idCausaleModificaDocumentoStr)) 
        {
 			    idCausaleModificaDocumento = new Long(idCausaleModificaDocumentoStr);
        }
        else 
        {
          idCausaleModificaDocumento = null;
        }
 			  
 			  if(Validator.isNotEmpty(note)) 
   			{
   				// ... controllo che non siano più lunghe di 500 caratteri
   				if(note.length() > 500) 
   				{
   					errors.add("note", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
   				}
   			}
   			else 
   			{
   				note = null;
   			}
 			}*/
 			if(Validator.isNotEmpty(this.getTipoDocumentoVO().getFlagAnagTerr())) {
 				// Se è stato scelto un tipo documento anagrafico
 				if(tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI)) {
 					// Se FLAG_OBBLIGO_ENTE_NUMERO del tipo documento selezionato è uguale a S
 					if(tipoDocumentoVO.getFlagObbligoEnteNumero().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 						// Il numero documento è obbligatorio
 						if(!Validator.isNotEmpty(numeroDocumento)) {
 							errors.add("numeroDocumento", new ValidationError((String)AnagErrors.get("ERR_CAMPO_OBBLIGATORIO")));
 						}
 						else {
 							if(numeroDocumento.length() > 20) {
 								errors.add("numeroDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 							}
 						}
 						// L'ente rilascio documento è obbligatorio
 						if(!Validator.isNotEmpty(enteRilascioDocumento)) {
 							errors.add("enteRilascioDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
 						}
 						else {	 							
 							if(enteRilascioDocumento.length() > 100) {
 								errors.add("enteRilascioDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 							}
 						}
 					}
 					if(Validator.isNotEmpty(numeroDocumento) && numeroDocumento.length() > 20) {
 						errors.add("numeroDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
 					}
 					if(Validator.isNotEmpty(enteRilascioDocumento) && enteRilascioDocumento.length() > 100) {
						errors.add("enteRilascioDocumento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
					}
 				}
 				// Se invece è stato scelto un documento di tipo zootecnico
     		else if(tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI)) {
     			// Controllo che il campo CUAA soccidario sia stato valorizzato solo se flag_obbligo_proprietario = S
     			if(tipoDocumentoVO.getFlagObbligoProprietario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
     				if(!Validator.isNotEmpty(cuaaSoccidario)) {
     					errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
     				}
     			}
     			// Se valorizzato...
     			if(Validator.isNotEmpty(cuaaSoccidario)) {
     				// ... controllo che sia un CUAA valido
     				if(cuaaSoccidario.length() != 11 && cuaaSoccidario.length() != 16) {
     					errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
     				}
     				else {
     					if(cuaaSoccidario.length() == 11) {
     						if(!Validator.controlloPIVA(cuaaSoccidario)) {
     							errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
     						}
     					}
     					else {
     						if(!Validator.controlloCf(cuaaSoccidario)) {
     							errors.add("cuaaSoccidario", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
     						}
     					}
     				}
     			}
     		}
 			}
 		}
 		catch(Exception e) {
 		}
 		return errors;
 	}
	 
 
	 	
 	//Controllo solo se sono state modificate unicamente le note...
 	//nel caso faccio l'update altrimenti storicizzo!
  public boolean equalsForUpdate(Object obj, Vector<Object> vect) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final DocumentoVO other = (DocumentoVO) obj;
    // Faccio l'update solo se sono state modificate esclusivamente le note
    // e/o la causale modifica 
    if((vect.size() == 1) 
        && (vect.contains("note")
            || vect.contains("idCausaleModificaDocumento")))
    {
      //idDocumento
      if (!Validator.isNotEmpty(idCausaleModificaDocumento)) {
        if (Validator.isNotEmpty(other.idCausaleModificaDocumento)) {
          return false;
        }
      } 
      else if(!Validator.isNotEmpty(other.idCausaleModificaDocumento)) {
        if(Validator.isNotEmpty(this.idCausaleModificaDocumento)) {
          return false;
        }
      }
      else if (!idCausaleModificaDocumento.equals(other.idCausaleModificaDocumento)) {
        return false;
      }
      
      if(!Validator.isNotEmpty(this.note)) {
        if(Validator.isNotEmpty(other.note)) {
          return false;
        }
      }
      else if(!Validator.isNotEmpty(other.note)) {
        if(Validator.isNotEmpty(this.note)) {
          return false;
        }
      }
      else if(!this.note.equalsIgnoreCase(other.note)) {
        return false;
      }
    }
    
    if((vect.size() == 2) 
        && vect.contains("note")
        && vect.contains("idCausaleModificaDocumento"))
    {
      //idDocumento
      if (!Validator.isNotEmpty(idCausaleModificaDocumento)) {
        if (Validator.isNotEmpty(other.idCausaleModificaDocumento)) {
          return false;
        }
      } 
      else if(!Validator.isNotEmpty(other.idCausaleModificaDocumento)) {
        if(Validator.isNotEmpty(this.idCausaleModificaDocumento)) {
          return false;
        }
      }
      else if (!idCausaleModificaDocumento.equals(other.idCausaleModificaDocumento)) {
        return false;
      }
      
      if(!Validator.isNotEmpty(this.note)) {
        if(Validator.isNotEmpty(other.note)) {
          return false;
        }
      }
      else if(!Validator.isNotEmpty(other.note)) {
        if(Validator.isNotEmpty(this.note)) {
          return false;
        }
      }
      else if(!this.note.equalsIgnoreCase(other.note)) {
        return false;
      }
    }
    
    return true;
  }

  public String getFlagIstanzaRiesame()
  {
    return flagIstanzaRiesame;
  }

  public void setFlagIstanzaRiesame(String flagIstanzaRiesame)
  {
    this.flagIstanzaRiesame = flagIstanzaRiesame;
  }

  public Long getIdContoCorrente()
  {
    return idContoCorrente;
  }

  public void setIdContoCorrente(Long idContoCorrente)
  {
    this.idContoCorrente = idContoCorrente;
  }

  public Long getIdCausaleModificaDocumento()
  {
    return idCausaleModificaDocumento;
  }

  public void setIdCausaleModificaDocumento(Long idCausaleModificaDocumento)
  {
    this.idCausaleModificaDocumento = idCausaleModificaDocumento;
  }

  public String getDescCausaleModificaDocumento()
  {
    return descCausaleModificaDocumento;
  }

  public void setDescCausaleModificaDocumento(String descCausaleModificaDocumento)
  {
    this.descCausaleModificaDocumento = descCausaleModificaDocumento;
  }

  public String getDescStatoDocumento()
  {
    return descStatoDocumento;
  }

  public void setDescStatoDocumento(String descStatoDocumento)
  {
    this.descStatoDocumento = descStatoDocumento;
  }

  public String getFlagIstanzaRiesameNoModTotale()
  {
    return flagIstanzaRiesameNoModTotale;
  }

  public void setFlagIstanzaRiesameNoModTotale(
      String flagIstanzaRiesameNoModTotale)
  {
    this.flagIstanzaRiesameNoModTotale = flagIstanzaRiesameNoModTotale;
  }

  public Vector<AllegatoDocumentoVO> getvAllegatoDocumento()
  {
    return vAllegatoDocumento;
  }

  public void setvAllegatoDocumento(Vector<AllegatoDocumentoVO> vAllegatoDocumento)
  {
    this.vAllegatoDocumento = vAllegatoDocumento;
  }
  
  
  
  public int getFaseIstanzaRiesame()
  {
    return faseIstanzaRiesame;
  }

  public void setFaseIstanzaRiesame(int faseIstanzaRiesame)
  {
    this.faseIstanzaRiesame = faseIstanzaRiesame;
  }

  private void compareDate(Vector<Object> vect, Date data1, Date data2, String strCampo)
  {
    if (Validator.isEmpty(data1)) 
    {
      if (Validator.isNotEmpty(data2)) 
      {
        vect.add(strCampo);
      }
    } 
    else
    {
      if(Validator.isEmpty(data2)) 
      {
        vect.add(strCampo);
      }
      else if(data1.compareTo(data2) !=0)
      {
        vect.add(strCampo);
      }
    }
  }

  public String getFlagTastoParticelle()
  {
    return flagTastoParticelle;
  }

  public void setFlagTastoParticelle(String flagTastoParticelle)
  {
    this.flagTastoParticelle = flagTastoParticelle;
  }

  public String getExtraSistemaIstanzaRiesame()
  {
    return extraSistemaIstanzaRiesame;
  }

  public void setExtraSistemaIstanzaRiesame(String extraSistemaIstanzaRiesame)
  {
    this.extraSistemaIstanzaRiesame = extraSistemaIstanzaRiesame;
  }
  
  
}
