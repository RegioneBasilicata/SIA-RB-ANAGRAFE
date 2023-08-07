package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.AbstractValueObject;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class AnagAziendaVO extends AbstractValueObject implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 7716247305772588262L;

  private Long idAnagAzienda = null;
  private Long idAzienda = null;
  private Long idAziendaSubentro;
  private Date dataInizioVal = null;
  private Date dataFineVal = null;
  private Date dataIngresso = null;
  private Date dataUscita = null;
  private Long idAziendaCollegata = null;
  private String CUAA = null;
  private String CUAAAnagrafeTributaria = null;
  private String[] CUAACollegati = null;
  private String oldCUAA = null;
  private String CUAASubentro;
  private String partitaIVA = null;
  private String denominazione = null;
  private String provCompetenza = null;
  private String siglaProvCompetenza = null;
  private String CCIAAprovREA = null;
  private Long CCIAAnumeroREA = null;
  private String sedelegIstatProv = null;
  private String sedelegIstatComune = null;
  private String sedelegProv = null;
  private String sedelegComune = null;
  private String sedelegIndirizzo = null;
  private String sedelegCAP = null;
  private String telefono = null;
  private String fax = null;
  private String mail = null;
  private String pec = null;
  private String sitoWEB = null;
  private String note = null;
  private java.util.Date dataCessazione = null;
  private String causaleCessazione = null;
  private java.util.Date dataAggiornamento = null;
  private Long idUtenteAggiornamento = null;
  private String motivoModifica = null;
  private CodeDescription tipoFormaGiuridica = null;
  private CodeDescription tipoAttivitaATECO = null;
  private CodeDescription tipoAttivitaOTE = null;
  private CodeDescription tipoTipologiaAzienda = null;
  private String flagFormaAssociata;
  private boolean flagEnteAppartenenza;
  private String codiceComune = null;
  private String descComune = null;
  private String dataSituazioneAlStr = null;
  private String ultimaModifica = null;
  private String sedelegIstatEstero = null;
  private String sedelegEstero = null;
  private String sedelegCittaEstero = null;
  private String CCIAAnumRegImprese = null;
  private String CCIAAannoIscrizione = null;
  private String variazioneUtilizziAmmessa = null; // Valore relativo al campo
                                                    // VARIAZIONE_UTILIZZI_AMMESSA
                                                    // sulla tabella DB_AZIENDA
  // Variabile di appoggio necessaria solo per la gestione degli errori
  private String statoEstero = null;
  private String unitaProduttiva = null;;
  private String strCCIAAnumeroREA;
  private String tipiFormaGiuridica;
  private String tipiFormaGiuridicaNonIndividuale;
  private String tipiAzienda;
  private String strAttivitaATECO;
  private String strAttivitaOTE;
  private String flagCCIAA;
  private String dataCessazioneStr;
  private DelegaVO delegaVO = null;

  // attributi che servono solo per la gestione della validazione dei dati sulle
  // pagine jsp
  private String dataPuntuale = null;
  private String dataAvanzata = null;
  private String dataAvvenutoPassaggio = null;
  private String intermediarioDelegato;
  private String idIntermediarioDelegato = null;
  private String flagPartitaIva = null;
  private String codFiscale = null;
  private String cognome = null;
  private String nome = null;
  // Variabili utilizzate solo per la gestione della ricerca per persona che usa
  // AnagAziendaVO invece di
  // PersonaFisicaVO
  private String dataNascitaPersonaFisica = null;
  private String provinciaNascitaPersonaFisica = null;
  private String comuneNascitaPersonaFisica = null;
  private String statoEsteroNascitaPersonaFisica = null;
  private String provinciaResidenzaPersonaFisica = null;
  private String comuneResidenzaPersonaFisica = null;
  private String statoEsteroResidenzaPersonaFisica = null;
  // attributi che servono per visualizzare correttamente il codice e la
  // descrizione di ote e ateco
  String codeOte = null;
  String codeAteco = null;
  String descOte = null;
  String descAteco = null;
  String idOte = null;
  String idAteco = null;
  private String rappresentanteLegale;
  private Long idFormaGiuridica;
  private String provincePiemonte;
  private String descrizioneProvCompetenza;
  private String descrizioneUtenteModifica = null;
  private String descrizioneEnteUtenteModifica = null;
  private boolean flagAziendaProvvisoria;
  private Long idOpr = null;
  private Date dataAperturaFascicolo = null;
  private Date dataChiusuraFascicolo = null;
  private Date dataAggiornamentoOpr = null;
  private Long idUtenteAggiornamentoOpr = null;
  private String modificaIntermediario = null;
  private String numeroAgea = null;
  private Long idCessazione = null;
  private TipoCessazioneVO tipoCessazioneVO = null;
  private Long idAziendaProvenienza = null;
  private Long idTipoFormaAssociata = null;
  private String labelElencoAssociati = null;
  private String labelSubAssociati = null;
  
  /**
   * Attributo per vitivinicolo
   */
  private String posizioneSchedario;
  /** ********************************************* */

  /**
   * Questo atributo mi dice se esiste una delega (data_fine in null) per
   * l'azienda
   */
  private boolean possiedeDelegaAttiva;

  private String intestazionePartitaIva;

  // Campi ultima modifica spezzata
  private Date dataUltimaModifica = null;
  private String utenteUltimaModifica = null;
  private String enteUltimaModifica = null;
  
  //Campi introdotti per l'OTE
  private Long idDimensioneAzienda; //utilizzato per controllo uguaglianza
  private String descDimensioneAzienda;
  private Long idUde; //utilizzato per controllo uguaglianza
  private Long classeUde; 
  private BigDecimal rls; //utilizzato per controllo uguaglianza
  private BigDecimal ulu;
  
  //Per agriturismo
  private String codiceAgriturismo;
 
  @SuppressWarnings("rawtypes")
  private Vector vAziendaATECOSec;
  //Aggiunto solo per il metodo equalsForUpdateNew poiche nel caso di solo questa modifica
  //non si fà l'update di DB_ANAGRAFICA AZIENDA
  private boolean modificaAtecoSec;
  
  //CC speciali
  private String esoneroPagamentoGF;
  
  //Nuova iscrizione
  private String fascicoloDematerializzato;
  
  private Date dataControlliAllevamenti;
  private Date dataAggiornamentoUma;
  private String flagIap;
  private Date dataIscrizioneRea;
  private Date dataCessazioneRea;
  private Date dataIscrizioneRi;
  private Date dataCessazioneRi;
  private Date dataInizioAteco;
  
  @SuppressWarnings("rawtypes")
  private Vector vAziendaSezioni;

  public AnagAziendaVO()
  {
    tipoFormaGiuridica = new CodeDescription();
    tipoAttivitaATECO = new CodeDescription();
    tipoAttivitaOTE = new CodeDescription();
    tipoTipologiaAzienda = new CodeDescription();
  }

  public boolean equalsForUpdateNewNoAtecoSec(Object o)
  {
    if (o instanceof AnagAziendaVO)
    {
      AnagAziendaVO other = (AnagAziendaVO) o;
      return (confronta(tipoAttivitaATECO, other.tipoAttivitaATECO) && 
    		  confronta(tipoAttivitaOTE, other.tipoAttivitaOTE) &&
    		  confronta(this.strCCIAAnumeroREA,other.strCCIAAnumeroREA) &&
	          confronta(this.CCIAAprovREA,other.CCIAAprovREA) &&
		      confronta(this.CCIAAannoIscrizione,other.CCIAAannoIscrizione) &&
		      confronta(this.CCIAAnumRegImprese,other.CCIAAnumRegImprese) &&
		      confronta(this.flagIap,other.flagIap));
    }
    else
    {
      return false;
    }
  }
  
  
  //Usato nel metodo updateAZienda di AnagAziendaBean
  public boolean equalsForUpdateNew(Object o)
  {
    if (o instanceof AnagAziendaVO)
    {
      AnagAziendaVO other = (AnagAziendaVO) o;
      return (this.CUAA == null && other.CUAA == null || this.CUAA
          .equals(other.CUAA)
          && (this.partitaIVA == null && other.partitaIVA == null || this.partitaIVA
              .equals(other.partitaIVA))
          && (this.denominazione == null && other.denominazione == null || this.denominazione
              .trim().equals(other.denominazione.trim()))
          && (this.tipoFormaGiuridica.getCode() == null
              && other.tipoFormaGiuridica.getCode() == null || this.tipoFormaGiuridica
              .getCode().equals(other.tipoFormaGiuridica.getCode()))
          && (this.tipoTipologiaAzienda.getCode() == null
              && other.tipoTipologiaAzienda.getCode() == null || this.tipoTipologiaAzienda
              .getCode().equals(other.tipoTipologiaAzienda.getCode()))
          && (this.CCIAAprovREA == null && other.CCIAAprovREA == null
              || this.CCIAAprovREA.equals(other.CCIAAprovREA) || this.CCIAAprovREA
              .equals("")
              && other.CCIAAprovREA == null)
          && (this.strCCIAAnumeroREA == null && other.strCCIAAnumeroREA == null
              || this.strCCIAAnumeroREA.equals(other.strCCIAAnumeroREA) || this.strCCIAAnumeroREA
              .equals("")
              && other.strCCIAAnumeroREA == null)
          && (this.CCIAAannoIscrizione == null
              && other.CCIAAannoIscrizione == null
              || this.CCIAAannoIscrizione.equals(other.CCIAAannoIscrizione) || this.CCIAAannoIscrizione
              .equals("")
              && other.CCIAAannoIscrizione == null)
          && (this.CCIAAnumRegImprese == null
              && other.CCIAAnumRegImprese == null
              || this.CCIAAnumRegImprese.equals(other.CCIAAnumRegImprese) || this.CCIAAnumRegImprese
              .equals("")
              && other.CCIAAnumRegImprese == null)
          && (this.provCompetenza == null && other.provCompetenza == null
              || this.provCompetenza.equals(other.provCompetenza) || this.provCompetenza
              .equals("")
              && other.provCompetenza == null)
          && confronta(tipoAttivitaATECO, other.tipoAttivitaATECO)
          && confronta(tipoAttivitaOTE, other.tipoAttivitaOTE)
          && (this.note == null && other.note == null
              || this.note.equals(other.note) || this.note.equals("")
              && other.note == null)
          && (this.idDimensioneAzienda == null && other.idDimensioneAzienda == null || this.idDimensioneAzienda
              .equals(other.idDimensioneAzienda))
          && (this.idUde == null && other.idUde == null || this.idUde
              .equals(other.idUde))
          && (this.rls == null && other.rls == null || this.rls
              .equals(other.rls))
          && (this.ulu == null && other.ulu == null || this.ulu
              .equals(other.ulu))
          && (!this.modificaAtecoSec)
          && confronta(this.esoneroPagamentoGF, other.esoneroPagamentoGF));
    }
    else
    {
      return false;
    }
  }
  
  
  //Restituisce true se i due oggetti sono uguali
  public boolean equalsForUpdateTabAnagrafica(Object o, boolean all)
  {
    if (o instanceof AnagAziendaVO)
    {
      AnagAziendaVO other = (AnagAziendaVO) o;
      if(all)
      {
        return 
            confronta(this.CUAA,other.CUAA) &&
            confronta(this.partitaIVA,other.partitaIVA) &&
            confronta(this.denominazione,other.denominazione) &&
            confronta(this.intestazionePartitaIva,other.intestazionePartitaIva) &&
            confrontaSenzaDescrizione(this.tipoFormaGiuridica, other.tipoFormaGiuridica) &&
            confrontaSenzaDescrizione(this.tipoTipologiaAzienda, other.tipoTipologiaAzienda) &&
            confronta(this.provCompetenza, other.provCompetenza) &&
            confronta(this.pec, other.pec) &&
            confronta(this.telefono, other.telefono) &&
            confronta(this.fax, other.fax) &&
            confronta(this.sitoWEB, other.sitoWEB) &&
            confronta(this.mail, other.mail);
            
            
      }
      else
      {
        return 
        	confronta(this.CUAA,other.CUAA) &&
        	confronta(this.partitaIVA,other.partitaIVA) &&
          confronta(this.denominazione,other.denominazione) &&
          confronta(this.intestazionePartitaIva,other.intestazionePartitaIva) &&
          confrontaSenzaDescrizione(this.tipoFormaGiuridica, other.tipoFormaGiuridica) &&
          confrontaSenzaDescrizione(this.tipoTipologiaAzienda, other.tipoTipologiaAzienda) &&
          confronta(this.provCompetenza, other.provCompetenza) &&
          confronta(this.pec, other.pec);
      }
    }
    else
    {
      return false;
    }
  }
  
  //Restituisce true se due stringhe sono uguali
  private boolean confronta(String s1, String s2)
  {
    if (s1 == null && s2 == null)
      return true;
    if (s1 == null && "".equals(s2))
      return true;
    if (s2 == null && "".equals(s1))
      return true;
    if (s1 == null || s2 == null)
      return false;
    return s1.trim().equals(s2.trim());
  }
  
  public boolean confrontaSenzaDescrizione(CodeDescription c1, CodeDescription c2)
  {
    if (c1 != null ^ c2 != null)
      return false;
    if (c1 == c2)
      return true;
    return c1.confrontaSenzaDescrizione(c2);
  }
  

  public boolean confronta(CodeDescription c1, CodeDescription c2)
  {
    if (c1 != null ^ c2 != null)
      return false;
    if (c1 == c2)
      return true;
    return c1.confronta(c2);
  }

  public String getCUAA()
  {
    return CUAA;
  }

  public void setCUAA(String CUAA)
  {
    this.CUAA = CUAA;
  }

  public Date getDataFineVal()
  {
    return dataFineVal;
  }

  public void setDataFineVal(Date dataFineVal)
  {
    this.dataFineVal = dataFineVal;
  }

  public Date getDataInizioVal()
  {
    return dataInizioVal;
  }

  public void setDataInizioVal(Date dataInizioVal)
  {
    this.dataInizioVal = dataInizioVal;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public Long getIdAnagAzienda()
  {
    return idAnagAzienda;
  }

  public void setIdAnagAzienda(Long idAnagAzienda)
  {
    this.idAnagAzienda = idAnagAzienda;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public String getPartitaIVA()
  {
    return partitaIVA;
  }

  public void setPartitaIVA(String partitaIVA)
  {
    this.partitaIVA = partitaIVA;
  }

  public void setProvCompetenza(String provCompetenza)
  {
    this.provCompetenza = provCompetenza;
  }

  public String getProvCompetenza()
  {
    return provCompetenza;
  }

  public void setSiglaProvCompetenza(String siglaProvCompetenza)
  {
    this.siglaProvCompetenza = siglaProvCompetenza;
  }

  public String getSiglaProvCompetenza()
  {
    return siglaProvCompetenza;
  }

  public void setCCIAAprovREA(String CCIAAprovREA)
  {
    this.CCIAAprovREA = CCIAAprovREA;
  }

  public String getCCIAAprovREA()
  {
    return CCIAAprovREA;
  }

  public void setCCIAAnumeroREA(Long CCIAAnumeroREA)
  {
    this.CCIAAnumeroREA = CCIAAnumeroREA;
  }

  public Long getCCIAAnumeroREA()
  {
    return CCIAAnumeroREA;
  }

  public void setSedelegProv(String sedelegProv)
  {
    this.sedelegProv = sedelegProv;
  }

  public String getSedelegProv()
  {
    return sedelegProv;
  }

  public void setSedelegComune(String sedelegComune)
  {
    this.sedelegComune = sedelegComune;
  }

  public String getSedelegComune()
  {
    return sedelegComune;
  }

  public void setSedelegIndirizzo(String sedelegIndirizzo)
  {
    this.sedelegIndirizzo = sedelegIndirizzo;
  }

  public String getSedelegIndirizzo()
  {
    return sedelegIndirizzo;
  }

  public void setSedelegCAP(String sedelegCAP)
  {
    this.sedelegCAP = sedelegCAP;
  }

  public String getSedelegCAP()
  {
    return sedelegCAP;
  }

  public void setMail(String mail)
  {
    this.mail = mail;
  }

  public String getMail()
  {
    return mail;
  }
  
  public String getPec() {
    return pec;
  }
  
  public void setPec(String pec) {
    this.pec = pec;
  }

  public void setSitoWEB(String sitoWEB)
  {
    this.sitoWEB = sitoWEB;
  }

  public String getSitoWEB()
  {
    return sitoWEB;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getNote()
  {
    return note;
  }

  public void setDataCessazione(java.util.Date dataCessazione)
  {
    this.dataCessazione = dataCessazione;
  }

  public java.util.Date getDataCessazione()
  {
    return dataCessazione;
  }

  public void setCausaleCessazione(String causaleCessazione)
  {
    this.causaleCessazione = causaleCessazione;
  }

  public String getCausaleCessazione()
  {
    return causaleCessazione;
  }

  public void setDataAggiornamento(java.util.Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }

  public java.util.Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }

  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setMotivoModifica(String motivoModifica)
  {
    this.motivoModifica = motivoModifica;
  }

  public String getMotivoModifica()
  {
    return motivoModifica;
  }

  public boolean isDenominazioneInitialized()
  {
    return (denominazione != null && !denominazione.equals("")) ? true : false;
  }

  public boolean isSedelegProvInitialized()
  {
    return (sedelegProv != null && !sedelegProv.equals("")) ? true : false;
  }

  public boolean isDescComuneInitialized()
  {
    return (descComune != null && !descComune.equals("")) ? true : false;
  }

  public boolean isTipoFormaGiuridicaInitialized()
  {
    return (tipoFormaGiuridica.getCode() != null) ? true : false;
  }

  public boolean isTipoAziendaInitialized()
  {
    return (tipiAzienda != null) ? true : false;
  }

  public void setTipoFormaGiuridica(CodeDescription tipoFormaGiuridica)
  {
    this.tipoFormaGiuridica = tipoFormaGiuridica;
  }

  public CodeDescription getTipoFormaGiuridica()
  {
    return tipoFormaGiuridica;
  }

  public void setTipoAttivitaATECO(CodeDescription tipoAttivitaATECO)
  {
    this.tipoAttivitaATECO = tipoAttivitaATECO;
  }

  public CodeDescription getTipoAttivitaATECO()
  {
    return tipoAttivitaATECO;
  }

  public void setTipoAttivitaOTE(CodeDescription tipoAttivitaOTE)
  {
    this.tipoAttivitaOTE = tipoAttivitaOTE;
  }

  public CodeDescription getTipoAttivitaOTE()
  {
    return tipoAttivitaOTE;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public String getCodiceComune()
  {
    return codiceComune;
  }

  public void setCodiceComune(String codiceComune)
  {
    this.codiceComune = codiceComune;
  }

  public String getDataSituazioneAlStr()
  {
    return dataSituazioneAlStr;
  }

  public void setDataSituazioneAlStr(String dataSituazioneAlStr)
  {
    this.dataSituazioneAlStr = dataSituazioneAlStr;
  }

  public String getUltimaModifica()
  {
    return ultimaModifica;
  }

  public void setUltimaModifica(String ultimaModifica)
  {
    this.ultimaModifica = ultimaModifica;
  }

  public String getSedelegEstero()
  {
    return sedelegEstero;
  }

  public void setSedelegEstero(String sedelegEstero)
  {
    this.sedelegEstero = sedelegEstero;
  }

  public void setStatoEstero(String statoEstero)
  {
    this.statoEstero = statoEstero;
  }

  public String getStatoEstero()
  {
    return statoEstero;
  }

  public void setDataAvvenutoPassaggio(String dataAvvenutoPassaggio)
  {
    this.dataAvvenutoPassaggio = dataAvvenutoPassaggio;
  }

  public String getDataAvvenutoPassaggio()
  {
    return dataAvvenutoPassaggio;
  }

  public void setVariazioneUtilizziAmmessa(String variazioneUtilizziAmmessa)
  {
    this.variazioneUtilizziAmmessa = variazioneUtilizziAmmessa;
  }

  public String getVariazioneUtilizziAmmessa()
  {
    return variazioneUtilizziAmmessa;
  }

  /**
   * @return the modificaIntermediario
   */
  public String getModificaIntermediario()
  {
    return modificaIntermediario;
  }

  /**
   * @param modificaIntermediario
   *          the modificaIntermediario to set
   */
  public void setModificaIntermediario(String modificaIntermediario)
  {
    this.modificaIntermediario = modificaIntermediario;
  }

  /**
   * @return the numeroAgea
   */
  public String getNumeroAgea()
  {
    return numeroAgea;
  }

  /**
   * @param numeroAgea
   *          the numeroAgea to set
   */
  public void setNumeroAgea(String numeroAgea)
  {
    this.numeroAgea = numeroAgea;
  }

  /**
   * @return the idCessazione
   */
  public Long getIdCessazione()
  {
    return idCessazione;
  }

  /**
   * @param idCessazione
   *          the idCessazione to set
   */
  public void setIdCessazione(Long idCessazione)
  {
    this.idCessazione = idCessazione;
  }

  /**
   * @return the tipoCessazioneVO
   */
  public TipoCessazioneVO getTipoCessazioneVO()
  {
    return tipoCessazioneVO;
  }

  /**
   * @param tipoCessazioneVO
   *          the tipoCessazioneVO to set
   */
  public void setTipoCessazioneVO(TipoCessazioneVO tipoCessazioneVO)
  {
    this.tipoCessazioneVO = tipoCessazioneVO;
  }

  /**
   * @return the idAziendaProvenienza
   */
  public Long getIdAziendaProvenienza()
  {
    return idAziendaProvenienza;
  }

  /**
   * @param idAziendaProvenienza
   *          the idAziendaProvenienza to set
   */
  public void setIdAziendaProvenienza(Long idAziendaProvenienza)
  {
    this.idAziendaProvenienza = idAziendaProvenienza;
  }



  public boolean equalsSedeLegale(Object o)
  {
    AnagAziendaVO other = (AnagAziendaVO) o;
    // Indirizzo
    if (!Validator.isNotEmpty(this.sedelegIndirizzo))
    {
      if (Validator.isNotEmpty(other.sedelegIndirizzo))
      {
        return false;
      }
    }
    else if (!Validator.isNotEmpty(other.sedelegIndirizzo))
    {
      if (Validator.isNotEmpty(this.sedelegIndirizzo))
      {
        return false;
      }
    }
    else
    {
      if (!this.sedelegIndirizzo.equalsIgnoreCase(other.sedelegIndirizzo))
      {
        return false;
      }
    }
    // Comune
    if (!Validator.isNotEmpty(this.sedelegComune))
    {
      if (Validator.isNotEmpty(other.sedelegComune))
      {
        return false;
      }
    }
    else if (!Validator.isNotEmpty(other.sedelegComune))
    {
      if (Validator.isNotEmpty(this.sedelegComune))
      {
        return false;
      }
    }
    else
    {
      if (!this.sedelegComune.equalsIgnoreCase(other.sedelegComune))
      {
        return false;
      }
    }
    // CAP
    if (!Validator.isNotEmpty(this.sedelegCAP))
    {
      if (Validator.isNotEmpty(other.sedelegCAP))
      {
        return false;
      }
    }
    else if (!Validator.isNotEmpty(other.sedelegCAP))
    {
      if (Validator.isNotEmpty(this.sedelegCAP))
      {
        return false;
      }
    }
    else
    {
      if (!this.sedelegCAP.equalsIgnoreCase(other.sedelegCAP))
      {
        return false;
      }
    }
    // Città estera
    if (!Validator.isNotEmpty(this.sedelegCittaEstero))
    {
      if (Validator.isNotEmpty(other.sedelegCittaEstero))
      {
        return false;
      }
    }
    else if (!Validator.isNotEmpty(other.sedelegCittaEstero))
    {
      if (Validator.isNotEmpty(this.sedelegCittaEstero))
      {
        return false;
      }
    }
    else
    {
      if (!this.sedelegCittaEstero.equalsIgnoreCase(other.sedelegCittaEstero))
      {
        return false;
      }
    }
    //Controllo l'email ed il sito web se all è true in quanto per la storicizzazione
    //la modifica di questi campi non è importante
    /*if (all)
    {
    	// Sito web
    	if (!Validator.isNotEmpty(this.sitoWEB))
    	{
    	  if (Validator.isNotEmpty(other.sitoWEB))
    	  {
    	    return false;
    	  }
    	}
    	else if (!Validator.isNotEmpty(other.sitoWEB))
    	{
    	  if (Validator.isNotEmpty(this.sitoWEB))
    	  {
    	    return false;
    	  }
    	}
    	else
    	{
    	  if (!this.sitoWEB.equalsIgnoreCase(other.sitoWEB))
    	  {
    	    return false;
    	  }
    	}
    	// E-mail
    	if (!Validator.isNotEmpty(this.mail))
    	{
    	  if (Validator.isNotEmpty(other.mail))
    	  {
    	    return false;
    	  }
    	}
    	else if (!Validator.isNotEmpty(other.mail))
    	{
    	  if (Validator.isNotEmpty(this.mail))
    	  {
    	    return false;
    	  }
    	}
    	else
    	{
    	  if (!this.mail.equalsIgnoreCase(other.mail))
    	  {
    	    return false;
    	  }
    	}
    	//pec
    	if (!Validator.isNotEmpty(this.pec))
    	{
    	  if (Validator.isNotEmpty(other.pec))
    	  {
    	    return false;
    	  }
    	}
    	else if (!Validator.isNotEmpty(other.pec))
    	{
    	  if (Validator.isNotEmpty(this.pec))
    	  {
    	    return false;
    	  }
    	}
    	else
    	{
    	  if (!this.pec.equalsIgnoreCase(other.pec))
    	  {
    	    return false;
    	  }
    	}
    	// Telefono
      if (!Validator.isNotEmpty(this.telefono))
      {
        if (Validator.isNotEmpty(other.telefono))
        {
          return false;
        }
      }
      else if (!Validator.isNotEmpty(other.telefono))
      {
        if (Validator.isNotEmpty(this.telefono))
        {
          return false;
        }
      }
      else
      {
        if (!this.telefono.equalsIgnoreCase(other.telefono))
        {
          return false;
        }
      }
      // Fax
      if (!Validator.isNotEmpty(this.fax))
      {
        if (Validator.isNotEmpty(other.fax))
        {
          return false;
        }
      }
      else if (!Validator.isNotEmpty(other.fax))
      {
        if (Validator.isNotEmpty(this.fax))
        {
          return false;
        }
      }
      else
      {
        if (!this.fax.equalsIgnoreCase(other.fax))
        {
          return false;
        }
      }
    }*/
    return true;
  }

  public ValidationErrors validate()
  {
    ValidationErrors errors = new ValidationErrors();
    // L'inidirizzo è obbligatorio
    if (!Validator.isNotEmpty(sedelegIndirizzo))
    {
      errors.add("sedelegIndirizzo", new ValidationError(
          "Inserire l''indirizzo dell''azienda!"));
    }
    // Per la modifica/storicizzazione della sede legale occorre inserire la
    // provincia,il comune
    // il cap oppure lo stato estero
    if (!Validator.isNotEmpty(sedelegProv) && !Validator.isNotEmpty(descComune)
        && !Validator.isNotEmpty(sedelegCAP))
    {
      if (!Validator.isNotEmpty(statoEstero))
      {
        errors
            .add(
                "statoEstero",
                new ValidationError(
                    "Inserire lo stato estero o eventualmente provincia,comune e cap!"));
      }
    }
    if (!Validator.isNotEmpty(sedelegProv))
    {
      errors.add("sedelegProv", new ValidationError(
          "Inserire la provincia dell''azienda!"));
    }
    if (!Validator.isNotEmpty(descComune))
    {
      errors.add("sedelegComune", new ValidationError(
          "Inserire il comune dell''azienda!"));
    }
    if (!Validator.isNotEmpty(sedelegCAP))
    {
      errors.add("sedelegCAP", new ValidationError(
          "Inserire il cap dell''azienda!"));
    }
    // controllo la correttezza del campo e-mail
    if (Validator.isNotEmpty(mail))
    {
      if (!Validator.isValidEmail(mail))
      {
        errors.add("mail", new ValidationError(
            "L''indirizzo di posta inserito non è valido!"));
      }
    }
    // controllo la correttezza del campo pec
    if (Validator.isNotEmpty(pec))
    {
      if (!Validator.isValidEmail(pec))
      {
        errors.add("pec", new ValidationError(
            "L''indirizzo di posta pec inserito non è valido!"));
      }
    }
    return errors;
  }

  public ValidationErrors validateInsSedeLegale()
  {
    ValidationErrors errors = new ValidationErrors();
    // L'inidirizzo è obbligatorio
    if (!Validator.isNotEmpty(sedelegIndirizzo))
    {
      errors.add("sedelegIndirizzo", new ValidationError(
          "Inserire l''indirizzo dell''azienda!"));
    }
    // Per l'inserimento della sede legale occorre inserire la provincia,il
    // comune
    // il cap oppure lo stato estero
    if (!Validator.isNotEmpty(sedelegEstero))
    {
      if (!Validator.isNotEmpty(sedelegProv))
      {
        errors.add("sedelegProv", new ValidationError(
            "Inserire la provincia dell''azienda!"));
      }
      if (!Validator.isNotEmpty(descComune))
      {
        errors.add("sedelegComune", new ValidationError(
            "Inserire il comune dell''azienda!"));
      }
      if (Validator.isEmpty(sedelegCAP))
      {
        errors.add("sedelegCAP", new ValidationError(
            "Inserire il cap dell''azienda!"));
      }
      else
      {
        if(!Validator.isCapOk(sedelegCAP)) 
        {
          errors.add("sedelegCAP", new ValidationError(AnagErrors.ERR_CAP_SEDE_ERRATO));
        }
      }
      if (Validator.isNotEmpty(sedelegEstero))
      {
        errors
            .add(
                "sedelegEstero",
                new ValidationError(
                    "Se la provincia, il comune o il cap della sede legale sono valorizzati non è possibile valorizzare lo stato estero!"));
      }
    }
    else
    {
      if (Validator.isNotEmpty(sedelegProv))
      {
        errors
            .add(
                "sedelegProv",
                new ValidationError(
                    "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      }
      if (Validator.isNotEmpty(descComune))
      {
        errors
            .add(
                "sedelegComune",
                new ValidationError(
                    "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      }
      if (Validator.isNotEmpty(sedelegCAP))
      {
        errors
            .add(
                "sedelegCAP",
                new ValidationError(
                    "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      }
    }
    if (!Validator.isNotEmpty(sedelegEstero))
    {
      if (Validator.isNotEmpty(sedelegCittaEstero))
      {
        errors
            .add(
                "sedelegCittaEstero",
                new ValidationError(
                    "Se la sede legale non è in uno stato estero non è possibile valorizzare il campo città!"));
      }
    }
    // L'utente deve specificare se si tratta di un'unità produttiva oppure no
    if (!Validator.isNotEmpty(unitaProduttiva))
    {
      errors.add("unitaProduttiva", new ValidationError(
          "Specificare se si tratta o meno di un''unità produttiva!"));
    }
    if (unitaProduttiva != null)
    {
      if (Validator.isNotEmpty(sedelegEstero)
          && unitaProduttiva.equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        errors
            .add(
                "unitaProduttiva",
                new ValidationError(
                    "Se la sede legale è ubicata all''estero l''azienda non può essere un''unità produttiva!"));
      }
    }
    return errors;
  }

  public ValidationErrors validateInsertUnProd()
  {
    ValidationErrors errors = new ValidationErrors();

    if (!Validator.isNotEmpty(statoEstero))
    {
      if (!Validator.isNotEmpty(sedelegCAP))
        errors.add("sedelegCAP", new ValidationError(
            "Se lo Stato Estero non è valorizzato, il CAP è obbligatorio!"));
      if (!Validator.isNotEmpty(descComune))
        errors.add("descComune", new ValidationError(
            "Se lo Stato Estero non è valorizzato, il Comune è obbligatorio!"));
      if (!Validator.isNotEmpty(sedelegProv))
        errors
            .add(
                "descComune",
                new ValidationError(
                    "Se lo Stato Estero non è valorizzato, la Provincia è obbligatoria!"));
      if (!Validator.isNotEmpty(sedelegCAP)
          && !Validator.isNotEmpty(descComune)
          && !Validator.isNotEmpty(sedelegProv))
        errors
            .add(
                "statoEstero",
                new ValidationError(
                    "Se Provincia, Comune e CAP non sono valorizzati, lo Stato Estero è obbligatorio!"));
    }
    else
    {
      if (Validator.isNotEmpty(sedelegCAP))
        errors
            .add(
                "sedelegCAP",
                new ValidationError(
                    "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      if (Validator.isNotEmpty(descComune))
        errors
            .add(
                "descComune",
                new ValidationError(
                    "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
      if (Validator.isNotEmpty(sedelegProv))
        errors
            .add(
                "descComune",
                new ValidationError(
                    "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));

      errors
          .add(
              "statoEstero",
              new ValidationError(
                  "Se lo Stato Estero è valorizzato, Provincia, Comune e CAP devono essere lasciati vuoti e viceversa!"));
    }

    return errors;
  }

  // Metodo per la validazione dei dati durante l'inserimento di una nuova
  // azienda agricola
  public ValidationErrors validateInsertAnag()
  {
    ValidationErrors errors = new ValidationErrors();

    // La forma giuridica dell'azienda agricola è obbligatoria.
    if (!Validator.isNotEmpty(tipiFormaGiuridica))
    {
      errors.add("tipiFormaGiuridica", new ValidationError(
          AnagErrors.ERR_FORMA_GIURIDICA_OBBLIGATORIA));
    }
    // Il tipo azienda deve essere valorizzato
    if (!Validator.isNotEmpty(tipiAzienda))
    {
      errors.add("tipiAzienda", new ValidationError(
          AnagErrors.ERR_TIPO_AZIENDA_OBBLIGATORIO));
    }
    // Il codice fiscale dell'azienda agricola è obbligatorio.
    if (!Validator.isNotEmpty(CUAA))
    {
      errors.add("CUAA", new ValidationError(AnagErrors.ERR_CUAA_OBBLIGATORIO));
    }
    // La denominazione dell'azienda agricola è obbligatoria
    if (!Validator.isNotEmpty(denominazione))
    {
      errors.add("denominazione", new ValidationError(
          AnagErrors.ERR_DENOMINAZIONE_OBBLIGATORIA));
    }
    // La partita iva dell'azienda agricola è obbligatoria solo se l'azienda non
    // è provvisoria
    if (!flagAziendaProvvisoria)
    {
      // La partita iva dell'azienda agricola è obbligatoria in relazione alla
      // forma giuridica selezionata
      if (flagPartitaIva != null && !flagPartitaIva.equals(""))
      {
        if (flagPartitaIva.equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          if (!Validator.isNotEmpty(partitaIVA))
          {
            errors.add("partitaIVA", new ValidationError(
                AnagErrors.ERR_PARTITA_IVA_OBBLIGATORIA));
          }
          else
          {
            if (!Validator.controlloPIVA(partitaIVA))
            {
              errors.add("partitaIVA", new ValidationError(
                  AnagErrors.ERR_PARTITA_IVA_ERRATA));
            }
          }
        }
      }
    }
    if (tipiFormaGiuridica != null)
    {
      if (tipiFormaGiuridica.equals(String
          .valueOf(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE))
          || tipiFormaGiuridica
              .equals(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))
      {
        if (!Validator.controlloCf(CUAA))
        {
          errors.add("CUAA", new ValidationError(AnagErrors.ERR_CUAA_ERRATO));
        }
      }
      else
      {
        if (!Validator.controlloPIVA(CUAA))
        {
          errors.add("CUAA", new ValidationError(AnagErrors.ERR_CUAA_ERRATO));
        }
      }
    }
    else
    {
      if (!Validator.controlloPIVA(CUAA))
      {
        errors.add("CUAA", new ValidationError(
            AnagErrors.ERR_CUAA_FORMA_GIURIDICA));
      }
    }
    if (Validator.isNotEmpty(partitaIVA))
    {
      // La partita iva inserita deve essere valida.
      if (!Validator.controlloPIVA(partitaIVA))
      {
        errors.add("partitaIVA", new ValidationError(
            AnagErrors.ERR_PARTITA_IVA_ERRATA));
      }
    }
    
    // La provincia di competenza è obbligatoria
    if (!Validator.isNotEmpty(provCompetenza))
    {
      errors.add("provincePiemonte", new ValidationError((String) AnagErrors
          .get("ERR_PROVINCIA_COMPETENZA_OBBLIGATORIA")));
    }   
    
    // controllo la correttezza del campo e-mail
    if (Validator.isNotEmpty(mail))
    {
      if (!Validator.isValidEmail(mail))
      {
        errors.add("mail", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
      }
    }
    // controllo la correttezza del campo pec
    if (Validator.isNotEmpty(pec))
    {
      if (!Validator.isValidEmail(pec))
      {
        errors.add("pec", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
      }
    }

    // Le note, se valorizzate, non possono essere più lunghe di 300 caratteri
    if (Validator.isNotEmpty(note))
    {
      if (note.length() > 300)
      {
        errors.add("note", new ValidationError((String) AnagErrors
            .get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
      }
    }
    
    
    return errors;
  }
  
  
  
  public ValidationErrors validateInsertIndicatori()
  {
    ValidationErrors errors = new ValidationErrors();
    
    if (tipiFormaGiuridica != null)
    {
      // La provincia ed il numero REA sono obbligatori solo se l'azienda
      // non è provvisoria
      /*if (!flagAziendaProvvisoria)
      {
        // La provincia ed il numero REA sono obbligatori solo nel caso in cui
        // la forma giuridica
        // selezionata dall'utente abbia flagCCIAA = S.
        if (flagCCIAA != null && flagCCIAA.equals(SolmrConstants.FLAG_S))
        {
          if (!Validator.isNotEmpty(CCIAAprovREA))
          {
            errors.add("CCIAAprovREA", new ValidationError(
                AnagErrors.ERR_PROVINCIA_REA_OBBLIGATORIA));
          }
          if (!Validator.isNotEmpty(strCCIAAnumeroREA))
          {
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_OBBLIGATORIO));
          }
          else if (!Validator.isNumericInteger(strCCIAAnumeroREA)
              || strCCIAAnumeroREA.length() > 9)
          {
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_ERRATO));
          }
          else
          {
            CCIAAnumeroREA = new Long(strCCIAAnumeroREA);
          }
          if (!Validator.isNotEmpty(CCIAAannoIscrizione))
          {
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_OBBLIGATORIO));
          }
          else
          {
            if (!Validator.isNumericInteger(CCIAAannoIscrizione))
            {
              errors.add("CCIAAannoIscrizione", new ValidationError(
                  AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
            }
            else
            {
              int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
              if (annoIscrizione < SolmrConstants.ANNO_REGISTRO_MINIMO
                  || annoIscrizione > SolmrConstants.ANNO_REGISTRO_MASSIMO)
              {
                errors.add("CCIAAannoIscrizione", new ValidationError(
                    AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
              }
            }
          }
        }
      }*/
      if (Validator.isNotEmpty(strCCIAAnumeroREA))
      {
        if (!Validator.isNumericInteger(strCCIAAnumeroREA)
            || strCCIAAnumeroREA.length() > 9)
        {
          errors.add("strCCIAAnumeroREA", new ValidationError(
              AnagErrors.ERR_NUMERO_REA_ERRATO));
        }
        else
        {
          if (!Validator.validateMinimumValue(strCCIAAnumeroREA, 0))
          {
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_ERRATO));
          }
        }
      }
      if (Validator.isNotEmpty(CCIAAannoIscrizione))
      {
        if (!Validator.isNumericInteger(CCIAAannoIscrizione))
        {
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        }
        else
        {
          int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
          if (annoIscrizione < SolmrConstants.ANNO_REGISTRO_MINIMO
              || annoIscrizione > SolmrConstants.ANNO_REGISTRO_MASSIMO)
          {
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
          }
        }
      }
    }
    
    if (Validator.isNotEmpty(CCIAAannoIscrizione))
    {
      if (!Validator.isNumericInteger(CCIAAannoIscrizione))
      {
        errors.add("CCIAAannoIscrizione", new ValidationError(
            AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
      }
      else
      {
        int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
        if (annoIscrizione < SolmrConstants.ANNO_MIN
            || annoIscrizione > SolmrConstants.ANNO_MAX)
        {
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        }
      }
    }
    
    return errors;
  }

  // Metodo per la validazione dei dati durante la modifica/storicizzazione di
  // una azienda agricola
  public ValidationErrors validateUpdateAnag()
  {
    ValidationErrors errors = new ValidationErrors();

    // La forma giuridica dell'azienda agricola è obbligatoria.
    if (!Validator.isNotEmpty(tipiFormaGiuridica))
    {
      errors.add("tipiFormaGiuridica", new ValidationError(
          "La forma giuridica deve essere valorizzata"));
    }
    // Il tipo azienda deve essere valorizzato
    if (!Validator.isNotEmpty(tipiAzienda))
    {
      errors.add("tipiAzienda", new ValidationError(
          "Il tipo azienda deve essere valorizzato!"));
    }
    // La denominazione dell'azienda agricola è obbligatoria
    if (!Validator.isNotEmpty(denominazione))
    {
      errors.add("denominazione", new ValidationError(
          "La denominazione dell''azienda agricola deve essere valorizzata!"));
    }

    // Il cuaa è obbligatorio
    if (!Validator.isNotEmpty(CUAA))
    {
      errors.add("cuaa", new ValidationError((String) AnagErrors
          .get("ERR_CUAA_OBBLIGATORIO")));
    }

    if (tipiFormaGiuridica != null)
    {
      // Se la forma giuridica corrisponde a ditta individuale o a persona che
      // non esercita attività
      // d'impresa controllo che il cuaa corrisponda ad un codice fiscale
      // corretto
      if (tipiFormaGiuridica.equals(String
          .valueOf(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE))
          || tipiFormaGiuridica
              .equals(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))
      {
        if (Validator.isNotEmpty(CUAA))
        {
          if (!Validator.controlloCf(CUAA))
          {
            errors.add("cuaa", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRATO")));
          }
        }
      }
      else
      {
        // Il cuaa è obbligatorio
        if (Validator.isNotEmpty(CUAA))
        {
          if (!Validator.controlloPIVA(CUAA))
          {
            errors.add("cuaa", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRATO")));
          }
        }
      }
      
    }

    // La partita iva dell'azienda agricola è obbligatoria solo se l'azienda non
    // è provvisoria
    if (!flagAziendaProvvisoria)
    {
      // La partita iva dell'azienda agricola è obbligatoria in relazione alla
      // forma giuridica selezionata
      if (flagPartitaIva != null && !flagPartitaIva.equals(""))
      {
        if (flagPartitaIva.equalsIgnoreCase(SolmrConstants.FLAG_S))
        {
          if (!Validator.isNotEmpty(partitaIVA))
          {
            errors.add("partitaIVA", new ValidationError(
                AnagErrors.ERR_PARTITA_IVA_OBBLIGATORIA));
          }
        }
      }
    }
    // se viene inserita la partita iva deve essere corretta in ogni caso
    if (Validator.isNotEmpty(partitaIVA))
    {
      // La partita iva inserita deve essere valida.
      if (!Validator.controlloPIVA(partitaIVA))
      {
        errors.add("partitaIVA", new ValidationError(
            AnagErrors.ERR_PARTITA_IVA_ERRATA));
      }
    }
    // La provincia di competenza è obbligatoria
    if (!Validator.isNotEmpty(provCompetenza))
    {
      errors.add("provincePiemonte", new ValidationError((String) AnagErrors
          .get("ERR_PROVINCIA_COMPETENZA_OBBLIGATORIA")));
    }
    
    
    // controllo la correttezza del campo e-mail
    if (Validator.isNotEmpty(mail))
    {
      if (!Validator.isValidEmail(mail))
      {
        errors.add("mail", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
      }
    }
    // controllo la correttezza del campo pec
    if (Validator.isNotEmpty(pec))
    {
      if (!Validator.isValidEmail(pec))
      {
        errors.add("pec", new ValidationError(AnagErrors.ERR_MAIL_ERRATA));
      }
    }

    // Le note, se valorizzate, non possono essere più lunghe di 300 caratteri
    if (Validator.isNotEmpty(note))
    {
      if (note.length() > 300)
      {
        errors.add("note", new ValidationError((String) AnagErrors
            .get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
      }
    }

    return errors;
  }
  
  
  
  
  public ValidationErrors validateUpdateAnagIndicatori()
  {
    ValidationErrors errors = new ValidationErrors();

    if (Validator.isNotEmpty(tipoFormaGiuridica) 
    	&& Validator.isNotEmpty(tipoFormaGiuridica.getCode()))
    {
     
      // La provincia ed il numero REA sono obbligatori solo se l'azienda
      // non è provvisoria
      /*if (!flagAziendaProvvisoria)
      {
        // La provincia ed il numero REA sono obbligatori solo nel caso in cui
        // la forma giuridica
        // selezionata dall'utente abbia flagCCIAA = S.
        if (flagCCIAA != null && flagCCIAA.equals(SolmrConstants.FLAG_S))
        {
          if (Validator.isEmpty(CCIAAprovREA))
          {
            errors.add("CCIAAprovREA", new ValidationError(
                AnagErrors.ERR_PROVINCIA_REA_OBBLIGATORIA));
          }
          if (Validator.isEmpty(strCCIAAnumeroREA))
          {
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_OBBLIGATORIO));
          }
          if (!Validator.isNumericInteger(strCCIAAnumeroREA)
              || strCCIAAnumeroREA.length() > 9)
          {
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_ERRATO));
          }
          else
          {
            CCIAAnumeroREA = new Long(strCCIAAnumeroREA);
          }
          if (!Validator.isNotEmpty(CCIAAannoIscrizione))
          {
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_OBBLIGATORIO));
          }
          else
          {
            if (!Validator.isNumericInteger(CCIAAannoIscrizione))
            {
              errors.add("CCIAAannoIscrizione", new ValidationError(
                  AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
            }
            else
            {
              int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
              if (annoIscrizione < SolmrConstants.ANNO_REGISTRO_MINIMO
                  || annoIscrizione > SolmrConstants.ANNO_REGISTRO_MASSIMO)
              {
                errors.add("CCIAAannoIscrizione", new ValidationError(
                    AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
              }
            }
          }
        }
      }*/
      if (Validator.isNotEmpty(strCCIAAnumeroREA))
      {
        if (!Validator.isNumericInteger(strCCIAAnumeroREA)
            || strCCIAAnumeroREA.length() > 9)
        {
          errors.add("strCCIAAnumeroREA", new ValidationError(
              AnagErrors.ERR_NUMERO_REA_ERRATO));
        }
        else
        {
          if (!Validator.validateMinimumValue(strCCIAAnumeroREA, 0))
          {
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_ERRATO));
          }
        }
      }
      if (Validator.isNotEmpty(CCIAAannoIscrizione))
      {
        if (!Validator.isNumericInteger(CCIAAannoIscrizione))
        {
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        }
        else
        {
          int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
          if (annoIscrizione < SolmrConstants.ANNO_REGISTRO_MINIMO
              || annoIscrizione > SolmrConstants.ANNO_REGISTRO_MASSIMO
              || annoIscrizione < SolmrConstants.ANNO_MIN
              || annoIscrizione > SolmrConstants.ANNO_MAX)
          {
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
          }
        }
      }
    }

    return errors;
  }

  // Metodo per la validazione dei dati durante la modifica/storicizzazione di
  // una azienda agricola
  public ValidationErrors validateInsediamentoGiovani()
  {
    ValidationErrors errors = new ValidationErrors();

    // La forma giuridica dell'azienda agricola è obbligatoria.
    if (!Validator.isNotEmpty(tipiFormaGiuridica))
      errors.add("tipiFormaGiuridica", new ValidationError(
          "La forma giuridica deve essere valorizzata"));

    // Il tipo azienda deve essere valorizzato
    if (!Validator.isNotEmpty(tipiAzienda))
      errors.add("tipiAzienda", new ValidationError(
          "Il tipo azienda deve essere valorizzato!"));

    // La denominazione dell'azienda agricola è obbligatoria
    if (!Validator.isNotEmpty(denominazione))
      errors.add("denominazione", new ValidationError(
          "La denominazione dell''azienda agricola deve essere valorizzata!"));

    // Il cuaa è obbligatorio
    if (!Validator.isNotEmpty(CUAA))
      errors.add("cuaa", new ValidationError((String) AnagErrors
          .get("ERR_CUAA_OBBLIGATORIO")));

    if (tipiFormaGiuridica != null)
    {
      // Se la forma giuridica corrisponde a ditta individuale o a persona che
      // non esercita attività
      // d'impresa controllo che il cuaa corrisponda ad un codice fiscale
      // corretto
      if (tipiFormaGiuridica.equals(String
          .valueOf(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE))
          || tipiFormaGiuridica
              .equals(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))
      {
        if (Validator.isNotEmpty(CUAA))
          if (!Validator.controlloCf(CUAA))
            errors.add("cuaa", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRATO")));
      }
      else
      {
        if (Validator.isNotEmpty(CUAA))
          if (!Validator.controlloPIVA(CUAA))
            errors.add("cuaa", new ValidationError((String) AnagErrors
                .get("ERR_CUAA_ERRATO")));
      }

      // La provincia ed il numero REA sono obbligatori solo nel caso in cui la
      // forma giuridica
      // selezionata dall'utente abbia flagCCIAA = S.
      /*if (flagCCIAA != null && flagCCIAA.equals(SolmrConstants.FLAG_S))
      {
        if (!Validator.isNotEmpty(CCIAAprovREA))
          errors.add("CCIAAprovREA", new ValidationError(
              AnagErrors.ERR_PROVINCIA_REA_OBBLIGATORIA));
        if (!Validator.isNotEmpty(strCCIAAnumeroREA))
          errors.add("strCCIAAnumeroREA", new ValidationError(
              AnagErrors.ERR_NUMERO_REA_OBBLIGATORIO));
        else if (!Validator.isNumericInteger(strCCIAAnumeroREA)
            || strCCIAAnumeroREA.length() > 9)
          errors.add("strCCIAAnumeroREA", new ValidationError(
              AnagErrors.ERR_NUMERO_REA_ERRATO));
        else
          CCIAAnumeroREA = new Long(strCCIAAnumeroREA);

        if (!Validator.isNotEmpty(CCIAAannoIscrizione))
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_OBBLIGATORIO));
        else
        {
          if (!Validator.isNumericInteger(CCIAAannoIscrizione))
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
          else
          {
            int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
            if (annoIscrizione < SolmrConstants.ANNO_REGISTRO_MINIMO
                || annoIscrizione > SolmrConstants.ANNO_REGISTRO_MASSIMO)
              errors.add("CCIAAannoIscrizione", new ValidationError(
                  AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
          }
        }
        if (!Validator.isNotEmpty(CCIAAnumRegImprese))
          errors.add("CCIAAnumRegImprese", new ValidationError(
              AnagErrors.ERR_NUMERO_REGISTRO_IMPRESE_OBBLIGATORIO));
      }*/

      if (Validator.isNotEmpty(strCCIAAnumeroREA))
      {
        if (!Validator.isNumericInteger(strCCIAAnumeroREA)
            || strCCIAAnumeroREA.length() > 9)
          errors.add("strCCIAAnumeroREA", new ValidationError(
              AnagErrors.ERR_NUMERO_REA_ERRATO));
        else
        {
          if (!Validator.validateMinimumValue(strCCIAAnumeroREA, 0))
            errors.add("strCCIAAnumeroREA", new ValidationError(
                AnagErrors.ERR_NUMERO_REA_ERRATO));
        }
      }
      if (Validator.isNotEmpty(CCIAAannoIscrizione))
      {
        if (!Validator.isNumericInteger(CCIAAannoIscrizione))
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        else
        {
          int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
          if (annoIscrizione < SolmrConstants.ANNO_REGISTRO_MINIMO
              || annoIscrizione > SolmrConstants.ANNO_REGISTRO_MASSIMO)
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        }
      }
    }

    if (Validator.isNotEmpty(CCIAAannoIscrizione))
    {
      if (!Validator.isNumericInteger(CCIAAannoIscrizione))
        errors.add("CCIAAannoIscrizione", new ValidationError(
            AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
      else
      {
        int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
        if (annoIscrizione < SolmrConstants.ANNO_MIN
            || annoIscrizione > SolmrConstants.ANNO_MAX)
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
      }
    }

    // La partita iva dell'azienda agricola è obbligatoria in relazione alla
    // forma giuridica selezionata
    if (flagPartitaIva != null && !flagPartitaIva.equals(""))
    {
      if (flagPartitaIva.equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        if (!Validator.isNotEmpty(partitaIVA))
        {
          errors.add("partitaIVA", new ValidationError(
              AnagErrors.ERR_PARTITA_IVA_OBBLIGATORIA));
        }
      }
    }
    // se viene inserita la partita iva deve essere corretta in ogni caso
    if (Validator.isNotEmpty(partitaIVA))
    {
      // La partita iva inserita deve essere valida.
      if (!Validator.controlloPIVA(partitaIVA))
      {
        errors.add("partitaIVA", new ValidationError(
            AnagErrors.ERR_PARTITA_IVA_ERRATA));
      }
    }

    // La provincia di competenza è obbligatoria
    if (!Validator.isNotEmpty(provCompetenza))
      errors.add("provincePiemonte", new ValidationError((String) AnagErrors
          .get("ERR_PROVINCIA_COMPETENZA_OBBLIGATORIA")));

    // Le note, se valorizzate, non possono essere più lunghe di 300 caratteri
    if (Validator.isNotEmpty(note))
    {
      if (note.length() > 300)
        errors.add("note", new ValidationError((String) AnagErrors
            .get("ERR_NOTE_CONDUZIONE_PARTICELLA")));
    }

    return errors;
  }

  public ValidationErrors validateRicAAEP(boolean CUAASubentroNonPresente,
      boolean CUAAPresente)
  {
    ValidationErrors errors = new ValidationErrors();

    if (!Validator.isNotEmpty(CUAA))
      errors.add("CUAA", new ValidationError((String) AnagErrors
          .get("ERR_INSERIRE_CUAA")));
    else
    {
      CUAA = CUAA.trim();
      if (CUAA.length() != 11 && CUAA.length() != 16)
        errors.add("CUAA", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      else if (CUAA.length() == 11 && !Validator.controlloPIVA(CUAA))
        errors.add("CUAA", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      // parte in sostituzione alla parte commentata sopra
      // parte commentata per evitare i controlli sul cuaa e sulla partita iva,
      // richiesta effettuata dal dominio il 5/11/03
      else if (CUAA.length() == 16 && !Validator.controlloCf(CUAA))
        errors.add("CUAA", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
    }
    if (CUAASubentroNonPresente)
      errors.add("CUAASubentro", new ValidationError((String) AnagErrors
          .get("ERR_CUAA_SUBENTRO")));

    if (CUAAPresente)
      errors.add("CUAA", new ValidationError((String) AnagErrors
          .get("ERR_CUAA_E_CUAA_SUBENTRO")));

    return errors;
  }

  public ValidationErrors validateRicPuntuale()
  {
    ValidationErrors errors = new ValidationErrors();

    if (Validator.isNotEmpty(CUAA) && Validator.isNotEmpty(partitaIVA))
    {
      errors.add("CUAA", new ValidationError(
          "Inserire solo CUAA o solo Partita IVA"));
    }
    if (!Validator.isNotEmpty(CUAA) && !Validator.isNotEmpty(partitaIVA))
    {
      errors.add("partitaIVA", new ValidationError(
          "Inserire CUAA o Partita IVA"));
    }
    if (Validator.isNotEmpty(CUAA))
    {
      CUAA = CUAA.trim();
      if (CUAA.length() != 11 && CUAA.length() != 16)
        errors.add("CUAA", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      // parte commentata per evitare i controlli sul cuaa e sulla partita iva,
      // richiesta effettuata dal dominio il 5/11/03
      // else if(CUAA.length()==11 && !Validator.controlloPIVA(CUAA))
      // parte in sostituzione alla parte commentata sopra
      else if (CUAA.length() == 11 && !Validator.isNumericInteger(CUAA))
        errors.add("CUAA", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      // parte commentata per evitare i controlli sul cuaa e sulla partita iva,
      // richiesta effettuata dal dominio il 5/11/03
      /*
       * else if(CUAA.length()==16 && !Validator.controlloCf(CUAA))
       * errors.add("CUAA",new
       * ValidationError((String)AnagErrors.get("ERR_CUAA_ERRORE")));
       */
    }
    // if(Validator.isNotEmpty(partitaIVA) &&
    // !Validator.controlloPIVA(partitaIVA)) {
    if (Validator.isNotEmpty(partitaIVA)
        && !Validator.isNumericInteger(partitaIVA))
    {
      errors.add("partitaIVA", new ValidationError("Partita IVA errata"));
    }
    if (!Validator.isNotEmpty(dataPuntuale))
    {
      errors.add("dataPuntuale", new ValidationError(
          "Inserire la data situazione al"));
    }
    else
    {
      if (!Validator.validateDateF(dataPuntuale))
      {
        errors.add("dataPuntuale", new ValidationError(
            "Inserire la data situazione al correttamente"));
      }
    }
    return errors;
  }

  public ValidationErrors validateRicAvanzata()
  {
    ValidationErrors errors = new ValidationErrors();

    if (Validator.isNotEmpty(descComune) && Validator.isNotEmpty(sedelegEstero))
    {
      errors.add("sedelegProv", new ValidationError((String) AnagErrors
          .get("ERR_SEDELEGALE_PROVINCIA_NO_VALORIZZABILE2")));
      errors.add("descComune", new ValidationError((String) AnagErrors
          .get("ERR_SEDELEGALE_COMUNE_NO_VALORIZZABILE")));
      errors.add("sedelegEstero", new ValidationError((String) AnagErrors
          .get("ERR_SEDELEGALE_ESTERO_NO_VALORIZZABILE")));
    }
    if (!Validator.isNotEmpty(denominazione))
    {
      if (!Validator.isNotEmpty(descComune))
      {
        if (!Validator.isNotEmpty(sedelegEstero)
            && !Validator.isNotEmpty(tipiAzienda))
        {
          errors.add("sedelegEstero", new ValidationError((String) AnagErrors
              .get("ERR_PARAMETRI_RICERCA_AVANZATA_NO_COMUNE_OBBLIGATORI")));
          errors.add("tipiAzienda", new ValidationError((String) AnagErrors
              .get("ERR_PARAMETRI_RICERCA_AVANZATA_NO_COMUNE_OBBLIGATORI")));
        }
        else if (Validator.isNotEmpty(sedelegEstero))
        {
          if (Validator.isNotEmpty(sedelegProv))
          {
            errors.add("sedelegProv", new ValidationError((String) AnagErrors
                .get("ERR_SEDELEGALE_PROVINCIA_NO_VALORIZZABILE")));
          }
        }
      }
      else
      {
        if (!Validator.isNotEmpty(sedelegProv))
        {
          errors.add("sedelegProv", new ValidationError((String) AnagErrors
              .get("ERR_SEDELEGALE_PROVINCIA_OBBLIGATORIA")));
        }
      }
      /*
       * else if(!Validator.isNotEmpty(sedelegEstero)) {
       * if(!Validator.isNotEmpty(descComune)) { errors.add("descComune",new
       * ValidationError((String)AnagErrors.get("ERR_SEDELEGALE_COMUNE_OBBLIGATORIO"))); }
       * if(!Validator.isNotEmpty(sedelegProv)) { errors.add("sedelegProv",new
       * ValidationError((String)AnagErrors.get("ERR_SEDELEGALE_PROVINCIA_OBBLIGATORIA"))); } } }
       */
    }
    if (Validator.isNotEmpty(descComune))
    {
      if (!Validator.isNotEmpty(sedelegProv))
      {
        errors.add("sedelegProv", new ValidationError((String) AnagErrors
            .get("ERR_SEDELEGALE_PROVINCIA_OBBLIGATORIA")));
      }
    }
    if (!Validator.isNotEmpty(dataAvanzata))
    {
      errors.add("dataAvanzata", new ValidationError(
          "Inserire la data situazione al"));
    }
    else
    {
      if (!Validator.validateDateF(dataAvanzata))
      {
        errors.add("dataAvanzata", new ValidationError(
            "Inserire la data situazione al correttamente"));
      }
    }
    return errors;
  }

  public ValidationErrors validateCessaAzienda()
  {
    ValidationErrors errors = new ValidationErrors();
    if (Validator.isNotEmpty(dataCessazioneStr))
    {
      if (!Validator.validateDateF(this.dataCessazioneStr))
      {
        errors.add("dataCessazioneStr", new ValidationError(
            "Inserire la data inizio cessazione correttamente!"));
      }
      // la data cessazione deve essere minore della data di sistema
      else
        try
        {
          dataCessazione = DateUtils.parseDate(dataCessazioneStr);
          if (!dataCessazione.equals((DateUtils.parseDate(DateUtils
              .getCurrent("dd/MM/yyyy")))))
          {
            if (dataCessazione.after((DateUtils.parseDate(DateUtils
                .getCurrent("dd/MM/yyyy")))))
            {
              errors.add("dataCessazioneStr", new ValidationError(
                  AnagErrors.DATE_ERRATE_CESS_AZIENDA));
            }
          }
        }
        catch (Exception ex)
        {
        }
    }
    else
      errors.add("dataCessazioneStr", new ValidationError(
          "La data cessazione deve essere valorizzata"));
    // Il tipo cessazione è obbligatorio
    if (idCessazione == null)
    {
      errors.add("idCessazione", new ValidationError(
          AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
    }

    if (Validator.isNotEmpty(causaleCessazione))
    {
      if (causaleCessazione.length() > 100)
      {
        errors.add("causaleCessazione", new ValidationError(
            AnagErrors.LENGTH_CAUSALE_MAX_100));
      }
    }

    return errors;
  }

  public ValidationErrors validateSedeLegale()
  {
    ValidationErrors errors = new ValidationErrors();
    // L'inidirizzo è obbligatorio
    if (!Validator.isNotEmpty(sedelegIndirizzo))
    {
      errors.add("sedelegIndirizzo", new ValidationError(
          AnagErrors.ERR_INDIRIZZO_SEDE_OBBLIGATORIO));
    }
    // Per l'inserimento della sede legale occorre inserire la provincia,il
    // comune
    // il cap oppure lo stato estero
    if (!Validator.isNotEmpty(statoEstero))
    {
      if (!Validator.isNotEmpty(sedelegProv))
      {
        errors.add("sedelegProv", new ValidationError(
            AnagErrors.ERR_PROVINCIA_SEDE_OBBLIGATORIA));
      }
      if (!Validator.isNotEmpty(descComune))
      {
        errors.add("sedelegComune", new ValidationError(
            AnagErrors.ERR_COMUNE_SEDE_OBBLIGATORIO));
      }
      if (Validator.isEmpty(sedelegCAP))
      {
        errors.add("sedelegCAP", new ValidationError(
            AnagErrors.ERR_CAP_SEDE_OBBLIGATORIO));
      }
      else
      {
        if(!Validator.isCapOk(sedelegCAP)) 
        {
          errors.add("sedelegCAP", new ValidationError(AnagErrors.ERR_CAP_SEDE_ERRATO));
        }
      }
    }
    else
    {
      if (Validator.isNotEmpty(sedelegProv))
      {
        errors.add("sedelegProv", new ValidationError(
            AnagErrors.ERR_PROVINCIA_SEDE_NO_VALORIZZABILE));
      }
      if (Validator.isNotEmpty(descComune))
      {
        errors.add("sedelegComune", new ValidationError(
            AnagErrors.ERR_COMUNE_SEDE_NO_VALORIZZABILE));
      }
      if (Validator.isNotEmpty(sedelegCAP))
      {
        errors.add("sedelegCAP", new ValidationError(
            AnagErrors.ERR_CAP_SEDE_NO_VALORIZZABILE));
      }
    }
    if (!Validator.isNotEmpty(statoEstero))
    {
      if (Validator.isNotEmpty(sedelegCittaEstero))
      {
        errors.add("sedelegCittaEstero", new ValidationError(
            AnagErrors.ERR_CITTA_ESTERO_SEDE_ERRATA));
      }
    }
    
    return errors;
  }

  // Usato dall'insediamento giovani
  public ValidationErrors validateSedeLegale(ValidationErrors errors)
  {
    // L'inidirizzo è obbligatorio
    if (!Validator.isNotEmpty(sedelegIndirizzo))
    {
      errors.add("sedelegIndirizzo", new ValidationError(
          AnagErrors.ERR_INDIRIZZO_SEDE_OBBLIGATORIO));
    }
    // Per l'inserimento della sede legale occorre inserire la provincia,il
    // comune
    // il cap oppure lo stato estero
    if (!Validator.isNotEmpty(statoEstero))
    {
      if (!Validator.isNotEmpty(sedelegProv))
      {
        errors.add("sedelegProv", new ValidationError(
            AnagErrors.ERR_PROVINCIA_SEDE_OBBLIGATORIA));
      }
      if (!Validator.isNotEmpty(descComune))
      {
        errors.add("sedelegComune", new ValidationError(
            AnagErrors.ERR_COMUNE_SEDE_OBBLIGATORIO));
      }
      if (Validator.isEmpty(sedelegCAP))
      {
        errors.add("sedelegCAP", new ValidationError(
            AnagErrors.ERR_CAP_SEDE_OBBLIGATORIO));
      }
      else
      {
        if(!Validator.isCapOk(sedelegCAP)) 
        {
          errors.add("sedelegCAP", new ValidationError(AnagErrors.ERR_CAP_SEDE_ERRATO));
        }
      }
    }
    else
    {
      if (Validator.isNotEmpty(sedelegProv))
      {
        errors.add("sedelegProv", new ValidationError(
            AnagErrors.ERR_PROVINCIA_SEDE_NO_VALORIZZABILE));
      }
      if (Validator.isNotEmpty(descComune))
      {
        errors.add("sedelegComune", new ValidationError(
            AnagErrors.ERR_COMUNE_SEDE_NO_VALORIZZABILE));
      }
      if (Validator.isNotEmpty(sedelegCAP))
      {
        errors.add("sedelegCAP", new ValidationError(
            AnagErrors.ERR_CAP_SEDE_NO_VALORIZZABILE));
      }
    }
    if (!Validator.isNotEmpty(statoEstero))
    {
      if (Validator.isNotEmpty(sedelegCittaEstero))
      {
        errors.add("sedelegCittaEstero", new ValidationError(
            AnagErrors.ERR_CITTA_ESTERO_SEDE_ERRATA));
      }
    }
    return errors;
  }

  // Metodo per effettuare i controlli relativi al cambio di titolare di
  // un'azienda
  public ValidationErrors validateCambioTitolare()
  {
    ValidationErrors errors = new ValidationErrors();
    // Controllo che la data di avvenuto passaggio, se valorizzata, sia valida e
    // non sia maggiore
    // della data di sistema.
    if (Validator.isNotEmpty(dataAvvenutoPassaggio))
    {
      if (!Validator.validateDateF(dataAvvenutoPassaggio))
      {
        errors.add("dataAvvenutoPassaggio", new ValidationError(
            AnagErrors.ERR_DATA_PASSAGGIO_ERRATA));
      }
      try
      {
        if (DateUtils.parseDate(dataAvvenutoPassaggio).after(
            new Date(System.currentTimeMillis())))
        {
          errors.add("dataAvvenutoPassaggio", new ValidationError(
              AnagErrors.ERR_DATA_PASSAGGIO_ERRATA_DATA_ODIERNA));
        }
      }
      catch (Exception e)
      {
        errors.add("error", new ValidationError(AnagErrors.ERR_SISTEMA));
      }
    }
    // La denominazione dell'azienda agricola è obbligatoria
    if (!Validator.isNotEmpty(denominazione))
    {
      errors.add("denominazione", new ValidationError(
          AnagErrors.ERR_DENOMINAZIONE_OBBLIGATORIA));
    }
    // Il codice fiscale dell'azienda è obbligatorio
    if (!Validator.isNotEmpty(CUAA))
    {
      errors.add("CUAA", new ValidationError(AnagErrors.ERR_CUAA_OBBLIGATORIO));
    }
    else
    {
      if (!Validator.controlloCf(CUAA))
      {
        errors.add("CUAA", new ValidationError(AnagErrors.ERR_CUAA_ERRATO));
      }
    }
    // La partita iva dell'azienda agricola è obbligatoria in relazione alla
    // forma giuridica selezionata
    if (flagPartitaIva != null && !flagPartitaIva.equals(""))
    {
      if (flagPartitaIva.equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        if (!Validator.isNotEmpty(partitaIVA))
        {
          errors.add("partitaIVA", new ValidationError(
              AnagErrors.ERR_PARTITA_IVA_OBBLIGATORIA));
        }
        else
        {
          if (!Validator.controlloPIVA(partitaIVA))
          {
            errors.add("partitaIVA", new ValidationError(
                AnagErrors.ERR_PARTITA_IVA_ERRATA));
          }
        }
      }
    }
    // Il tipo azienda deve essere valorizzato
    if (!Validator.isNotEmpty(tipiAzienda))
    {
      errors.add("tipiAzienda", new ValidationError(
          AnagErrors.ERR_TIPO_AZIENDA_OBBLIGATORIO));
    }
    /*if (flagCCIAA != null && flagCCIAA.equals(SolmrConstants.FLAG_S))
    {
      // La provincia ed il numero REA sono obbligatori
      if (!Validator.isNotEmpty(CCIAAprovREA))
      {
        errors.add("CCIAAprovREA", new ValidationError(
            AnagErrors.ERR_PROVINCIA_REA_OBBLIGATORIA));
      }
      if (!Validator.isNotEmpty(strCCIAAnumeroREA))
      {
        errors.add("strCCIAAnumeroREA", new ValidationError(
            AnagErrors.ERR_NUMERO_REA_OBBLIGATORIO));
      }
      else if (!Validator.isNumericInteger(strCCIAAnumeroREA)
          || strCCIAAnumeroREA.length() > 9)
      {
        errors.add("strCCIAAnumeroREA", new ValidationError(
            AnagErrors.ERR_NUMERO_REA_ERRATO));
      }
      else
      {
        CCIAAnumeroREA = new Long(strCCIAAnumeroREA);
      }
      if (!Validator.isNotEmpty(CCIAAannoIscrizione))
      {
        errors.add("CCIAAannoIscrizione", new ValidationError(
            AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_OBBLIGATORIO));
      }
      else
      {
        if (!Validator.isNumericInteger(CCIAAannoIscrizione))
        {
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        }
        else
        {
          int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
          if (annoIscrizione < SolmrConstants.ANNO_MIN
              || annoIscrizione > SolmrConstants.ANNO_MAX)
          {
            errors.add("CCIAAannoIscrizione", new ValidationError(
                AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
          }
        }
      }
    }*/
    // Se l'anno di iscrizione è valorizzato controllo che abbia un valore
    // numerico
    if (Validator.isNotEmpty(CCIAAannoIscrizione))
    {
      if (!Validator.isNumericInteger(CCIAAannoIscrizione))
      {
        errors.add("CCIAAannoIscrizione", new ValidationError(
            AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
      }
      else
      {
        int annoIscrizione = Integer.parseInt(CCIAAannoIscrizione);
        if (annoIscrizione < SolmrConstants.ANNO_MIN
            || annoIscrizione > SolmrConstants.ANNO_MAX)
        {
          errors.add("CCIAAannoIscrizione", new ValidationError(
              AnagErrors.ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO));
        }
      }
    }
    return errors;
  }

  public ValidationErrors validateRicercaPersona()
  {
    ValidationErrors errors = new ValidationErrors();

    if (!Validator.isNotEmpty(codFiscale) && !Validator.isNotEmpty(cognome))
    {
      errors.add("codFiscale", new ValidationError(
          "Inserire almeno un parametro di ricerca"));
      errors.add("nome", new ValidationError(
          "Inserire almeno un parametro di ricerca"));
      errors.add("cognome", new ValidationError(
          "Inserire almeno un parametro di ricerca"));
    }
    if (Validator.isNotEmpty(codFiscale))
    {
      if (codFiscale.length() != 16)
        errors.add("codFiscale", new ValidationError(
            AnagErrors.ERR_GENERIC_CODICE_FISCALE));
      /*
       * else if(!Validator.controlloCf(codFiscale)) errors.add("codFiscale",new
       * ValidationError(AnagErrors.ERR_GENERIC_CODICE_FISCALE));
       */
    }
    // Se la data di nascita è valorizzata controllo che sia corretta
    if (Validator.isNotEmpty(dataNascitaPersonaFisica))
    {
      if (!Validator.validateDateF(dataNascitaPersonaFisica))
      {
        errors.add("dataNascita", new ValidationError((String) AnagErrors
            .get("ERR_DATA_NASCITA")));
      }
    }
    // Se valorizzo il comune e la provincia di nascita non devo valorizzare lo
    // stato estero di nascita e viceversa
    if (Validator.isNotEmpty(statoEsteroNascitaPersonaFisica))
    {
      if (Validator.isNotEmpty(provinciaNascitaPersonaFisica))
      {
        errors.add("nascitaProv", new ValidationError((String) AnagErrors
            .get("ERR_PROVINCIA_NASCITA_NO_VALORIZZABILE")));
      }
      if (Validator.isNotEmpty(comuneNascitaPersonaFisica))
      {
        errors.add("descNascitaComune", new ValidationError((String) AnagErrors
            .get("ERR_COMUNE_NASCITA_NO_VALORIZZABILE")));
      }
    }
    else
    {
      if (Validator.isNotEmpty(provinciaNascitaPersonaFisica)
          || Validator.isNotEmpty(comuneNascitaPersonaFisica))
      {
        if (Validator.isNotEmpty(provinciaNascitaPersonaFisica))
        {
          if (!Validator.isNotEmpty(comuneNascitaPersonaFisica))
          {
            errors.add("descNascitaComune", new ValidationError(
                (String) AnagErrors.get("ERR_COMUNE_NASCITA_OBBLIGATORIO")));
          }
        }
        else
        {
          if (!Validator.isNotEmpty(provinciaNascitaPersonaFisica))
          {
            errors.add("nascitaProv", new ValidationError((String) AnagErrors
                .get("ERR_PROVINCIA_NASCITA_OBBLIGATORIA")));
          }
        }
      }
    }
    // Se valorizzo il comune e la provincia di residenza non devo valorizzare
    // lo stato estero di residenza e viceversa
    if (Validator.isNotEmpty(statoEsteroResidenzaPersonaFisica))
    {
      if (Validator.isNotEmpty(provinciaResidenzaPersonaFisica))
      {
        errors.add("resProvincia", new ValidationError((String) AnagErrors
            .get("ERR_PROVINCIA_RESIDENZA_NO_VALORIZZABILE")));
      }
      if (Validator.isNotEmpty(comuneResidenzaPersonaFisica))
      {
        errors.add("descResComune", new ValidationError((String) AnagErrors
            .get("ERR_COMUNE_RESIDENZA_NO_VALORIZZABILE")));
      }
    }
    else
    {
      if (Validator.isNotEmpty(provinciaResidenzaPersonaFisica)
          || Validator.isNotEmpty(comuneResidenzaPersonaFisica))
      {
        if (Validator.isNotEmpty(provinciaResidenzaPersonaFisica))
        {
          if (!Validator.isNotEmpty(comuneResidenzaPersonaFisica))
          {
            errors.add("descResComune", new ValidationError((String) AnagErrors
                .get("ERR_COMUNE_RESIDENZA_OBBLIGATORIO")));
          }
        }
        if (Validator.isNotEmpty(comuneResidenzaPersonaFisica))
        {
          if (!Validator.isNotEmpty(provinciaResidenzaPersonaFisica))
          {
            errors.add("resProvincia", new ValidationError((String) AnagErrors
                .get("ERR_PROVINCIA_RESIDENZA_OBBLIGATORIA")));
          }
        }
      }
    }
    return errors;
  }

  public ValidationErrors validateRicAziendeCollegate(HttpServletRequest request)
  {
    ValidationErrors errors = null;

    String cuaa = request.getParameter("cuaa");
    // String denominazione = request.getParameter("denominazione");

    if (Validator.isNotEmpty(cuaa))
    {
      cuaa = cuaa.trim();
      if (cuaa.length() != 11 && cuaa.length() != 16)
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("cuaa", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      }
      // parte commentata per evitare i controlli sul cuaa e sulla partita iva,
      // richiesta effettuata dal dominio il 5/11/03
      // else if(CUAA.length()==11 && !Validator.controlloPIVA(CUAA))
      // parte in sostituzione alla parte commentata sopra
      else if (cuaa.length() == 11 && !Validator.isNumericInteger(cuaa))
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        
        errors.add("cuaa", new ValidationError((String) AnagErrors
            .get("ERR_CUAA_ERRORE")));
      }
      // parte commentata per evitare i controlli sul cuaa e sulla partita iva,
      // richiesta effettuata dal dominio il 5/11/03
      /*
       * else if(CUAA.length()==16 && !Validator.controlloCf(CUAA))
       * errors.add("CUAA",new
       * ValidationError((String)AnagErrors.get("ERR_CUAA_ERRORE")));
       */
    }
    return errors;
  }

  /**
   * Metodo che mi restituisce la stringa che dovrò visualizzare come tooltip
   * durante la paginazione dei terreni
   * 
   * @return java.lang.String
   */
  public String getDescForTooltip()
  {
    String toolitip = "";
    if (Validator.isNotEmpty(this.getCUAA()))
    {
      toolitip = "CUAA: " + this.getCUAA();
    }
    if (Validator.isNotEmpty(this.getDenominazione()))
    {
      toolitip += " Az. " + this.getDenominazione();
    }
    return toolitip;
  }
  
  /* BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END */

  // Campi utilizzati per l'ultima modifica spezzata
  public Date getDataUltimaModifica()
  {
    return dataUltimaModifica;
  }

  public void setDataUltimaModifica(Date dataUltimaModifica)
  {
    this.dataUltimaModifica = dataUltimaModifica;
  }

  public String getUtenteUltimaModifica()
  {
    return utenteUltimaModifica;
  }

  public void setUtenteUltimaModifica(String utenteUltimaModifica)
  {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public String getEnteUltimaModifica()
  {
    return enteUltimaModifica;
  }

  public void setEnteUltimaModifica(String enteUltimaModifica)
  {
    this.enteUltimaModifica = enteUltimaModifica;
  }

  public String getFlagFormaAssociata()
  {
    return flagFormaAssociata;
  }

  // Aggiunto per gestione aziende collegate
  public void setFlagFormaAssociata(String flagFormaAssociata)
  {
    this.flagFormaAssociata = flagFormaAssociata;
  }
  
  public boolean isFlagEnteAppartenenza()
  {
    return flagEnteAppartenenza;
  }

  public void setFlagEnteAppartenenza(boolean flagEnteAppartenenza)
  {
    this.flagEnteAppartenenza = flagEnteAppartenenza;
  }
  
  

  // Aggiunto per la visualizzazione aziende associate
  public Date getDataIngresso()
  {
    return dataIngresso;
  }

  public void setDataIngresso(Date dataIngresso)
  {
    this.dataIngresso = dataIngresso;
  }

  public Date getDataUscita()
  {
    return dataUscita;
  }

  public void setDataUscita(Date dataUscita)
  {
    this.dataUscita = dataUscita;
  }

  public Long getIdAziendaCollegata()
  {
    return idAziendaCollegata;
  }

  public void setIdAziendaCollegata(Long idAziendaCollegata)
  {
    this.idAziendaCollegata = idAziendaCollegata;
  }

  public void setDataCessazioneStr(String dataCessazioneStr)
  {
    this.dataCessazioneStr = dataCessazioneStr;
  }

  public String getDataCessazioneStr()
  {
    return dataCessazioneStr;
  }

  public void setUnitaProduttiva(String unitaProduttiva)
  {
    this.unitaProduttiva = unitaProduttiva;
  }

  public String getUnitaProduttiva()
  {
    return unitaProduttiva;
  }

  public void setStrCCIAAnumeroREA(String strCCIAAnumeroREA)
  {
    this.strCCIAAnumeroREA = strCCIAAnumeroREA;
  }

  public String getStrCCIAAnumeroREA()
  {
    return strCCIAAnumeroREA;
  }

  public void setTipiFormaGiuridica(String tipiFormaGiuridica)
  {
    this.tipiFormaGiuridica = tipiFormaGiuridica;
  }

  public String getTipiFormaGiuridica()
  {
    return tipiFormaGiuridica;
  }

  public void setStrAttivitaATECO(String strAttivitaATECO)
  {
    this.strAttivitaATECO = strAttivitaATECO;
  }

  public String getStrAttivitaATECO()
  {
    return strAttivitaATECO;
  }

  public void setStrAttivitaOTE(String strAttivitaOTE)
  {
    this.strAttivitaOTE = strAttivitaOTE;
  }

  public String getStrAttivitaOTE()
  {
    return strAttivitaOTE;
  }

  public void setFlagCCIAA(String flagCCIAA)
  {
    this.flagCCIAA = flagCCIAA;
  }

  public String getFlagCCIAA()
  {
    return flagCCIAA;
  }

  public String getDataAvanzata()
  {
    return dataAvanzata;
  }

  public void setDataAvanzata(String dataAvanzata)
  {
    this.dataAvanzata = dataAvanzata;
  }

  public String getDataPuntuale()
  {
    return dataPuntuale;
  }

  public void setDataPuntuale(String dataPuntuale)
  {
    this.dataPuntuale = dataPuntuale;
  }

  public String getSedelegIstatComune()
  {
    return sedelegIstatComune;
  }

  public void setSedelegIstatComune(String sedelegIstatComune)
  {
    this.sedelegIstatComune = sedelegIstatComune;
  }
  
  public String getSedelegIstatProv()
  {
    return sedelegIstatProv;
  }

  public void setSedelegIstatProv(String sedelegIstatProv)
  {
    this.sedelegIstatProv = sedelegIstatProv;
  }

  public void setTipiFormaGiuridicaNonIndividuale(
      String tipiFormaGiuridicaNonIndividuale)
  {
    this.tipiFormaGiuridicaNonIndividuale = tipiFormaGiuridicaNonIndividuale;
  }

  public String getTipiFormaGiuridicaNonIndividuale()
  {
    return tipiFormaGiuridicaNonIndividuale;
  }

  public void setOldCUAA(String oldCUAA)
  {
    this.oldCUAA = oldCUAA;
  }

  public String getOldCUAA()
  {
    return oldCUAA;
  }

  public CodeDescription getTipoTipologiaAzienda()
  {
    return tipoTipologiaAzienda;
  }

  public void setTipoTipologiaAzienda(CodeDescription tipoTipologiaAzienda)
  {
    this.tipoTipologiaAzienda = tipoTipologiaAzienda;
  }

  public String getCCIAAnumRegImprese()
  {
    return CCIAAnumRegImprese;
  }

  public void setCCIAAnumRegImprese(String CCIAAnumRegImprese)
  {
    this.CCIAAnumRegImprese = CCIAAnumRegImprese;
  }

  public String getCCIAAannoIscrizione()
  {
    return CCIAAannoIscrizione;
  }

  public void setCCIAAannoIscrizione(String CCIAAannoIscrizione)
  {
    this.CCIAAannoIscrizione = CCIAAannoIscrizione;
  }

  public String getSedelegCittaEstero()
  {
    return sedelegCittaEstero;
  }

  public void setSedelegCittaEstero(String sedelegCittaEstero)
  {
    this.sedelegCittaEstero = sedelegCittaEstero;
  }

  public DelegaVO getDelegaVO()
  {
    return delegaVO;
  }

  public void setDelegaVO(DelegaVO delegaVO)
  {
    this.delegaVO = delegaVO;
    if (this.delegaVO != null)
      setIntermediarioDelegato(delegaVO.getDenomIntermediario());
  }

  public String getTipiAzienda()
  {
    return tipiAzienda;
  }

  public void setTipiAzienda(String tipiAzienda)
  {
    this.tipiAzienda = tipiAzienda;
  }

  public void setIntermediarioDelegato(String intermediarioDelegato)
  {
    this.intermediarioDelegato = intermediarioDelegato;
  }

  public String getIntermediarioDelegato()
  {
    return intermediarioDelegato;
  }

  public void setFlagPartitaIva(String flagPartitaIva)
  {
    this.flagPartitaIva = flagPartitaIva;
  }

  public String getFlagPartitaIva()
  {
    return flagPartitaIva;
  }

  public void setCodFiscale(String codFiscale)
  {
    this.codFiscale = codFiscale;
  }

  public String getCodFiscale()
  {
    return codFiscale;
  }

  public void setCognome(String cognome)
  {
    this.cognome = cognome;
  }

  public String getCognome()
  {
    return cognome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getNome()
  {
    return nome;
  }

  public void setCodeOte(String codeOte)
  {
    this.codeOte = codeOte;
  }

  public void setIdOte(String idOte)
  {
    this.idOte = idOte;
  }

  public String getIdOte()
  {
    return idOte;
  }

  public String getCodeOte()
  {
    return codeOte;
  }

  public void setDescOte(String descOte)
  {
    this.descOte = descOte;
  }

  public String getDescOte()
  {
    return descOte;
  }

  public void setCodeAteco(String codeAteco)
  {
    this.codeAteco = codeAteco;
  }

  public String getCodeAteco()
  {
    return codeAteco;
  }

  public void setIdAteco(String idAteco)
  {
    this.idAteco = idAteco;
  }

  public String getIdAteco()
  {
    return idAteco;
  }

  public void setDescAteco(String descAteco)
  {
    this.descAteco = descAteco;
  }

  public String getDescAteco()
  {
    return descAteco;
  }

  public void setRappresentanteLegale(String rappresentanteLegale)
  {
    this.rappresentanteLegale = rappresentanteLegale;
  }

  public String getRappresentanteLegale()
  {
    return rappresentanteLegale;
  }

  public void setIdIntermediarioDelegato(String idIntermediarioDelegato)
  {
    this.idIntermediarioDelegato = idIntermediarioDelegato;
  }

  public String getIdIntermediarioDelegato()
  {
    return idIntermediarioDelegato;
  }

  public void setIdFormaGiuridica(Long idFormaGiuridica)
  {
    this.idFormaGiuridica = idFormaGiuridica;
  }

  public Long getIdFormaGiuridica()
  {
    return idFormaGiuridica;
  }

  public String getComuneNascitaPersonaFisica()
  {
    return comuneNascitaPersonaFisica;
  }

  public String getComuneResidenzaPersonaFisica()
  {
    return comuneResidenzaPersonaFisica;
  }

  public void setComuneNascitaPersonaFisica(String comuneNascitaPersonaFisica)
  {
    this.comuneNascitaPersonaFisica = comuneNascitaPersonaFisica;
  }

  public void setComuneResidenzaPersonaFisica(
      String comuneResidenzaPersonaFisica)
  {
    this.comuneResidenzaPersonaFisica = comuneResidenzaPersonaFisica;
  }

  public String getDataNascitaPersonaFisica()
  {
    return dataNascitaPersonaFisica;
  }

  public void setDataNascitaPersonaFisica(String dataNascitaPersonaFisica)
  {
    this.dataNascitaPersonaFisica = dataNascitaPersonaFisica;
  }

  public String getProvinciaNascitaPersonaFisica()
  {
    return provinciaNascitaPersonaFisica;
  }

  public void setProvinciaNascitaPersonaFisica(
      String provinciaNascitaPersonaFisica)
  {
    this.provinciaNascitaPersonaFisica = provinciaNascitaPersonaFisica;
  }

  public String getProvinciaResidenzaPersonaFisica()
  {
    return provinciaResidenzaPersonaFisica;
  }

  public void setProvinciaResidenzaPersonaFisica(
      String provinciaResidenzaPersonaFisica)
  {
    this.provinciaResidenzaPersonaFisica = provinciaResidenzaPersonaFisica;
  }

  public String getStatoEsteroNascitaPersonaFisica()
  {
    return statoEsteroNascitaPersonaFisica;
  }

  public void setStatoEsteroNascitaPersonaFisica(
      String statoEsteroNascitaPersonaFisica)
  {
    this.statoEsteroNascitaPersonaFisica = statoEsteroNascitaPersonaFisica;
  }

  public String getStatoEsteroResidenzaPersonaFisica()
  {
    return statoEsteroResidenzaPersonaFisica;
  }

  public void setStatoEsteroResidenzaPersonaFisica(
      String statoEsteroResidenzaPersonaFisica)
  {
    this.statoEsteroResidenzaPersonaFisica = statoEsteroResidenzaPersonaFisica;
  }

  public String getProvincePiemonte()
  {
    return provincePiemonte;
  }

  public void setProvincePiemonte(String provincePiemonte)
  {
    this.provincePiemonte = provincePiemonte;
  }

  public String getDescrizioneProvCompetenza()
  {
    return descrizioneProvCompetenza;
  }

  public void setDescrizioneProvCompetenza(String descrizioneProvCompetenza)
  {
    this.descrizioneProvCompetenza = descrizioneProvCompetenza;
  }

  public String getDescrizioneUtenteModifica()
  {
    return descrizioneUtenteModifica;
  }

  public void setDescrizioneUtenteModifica(String descrizioneUtenteModifica)
  {
    this.descrizioneUtenteModifica = descrizioneUtenteModifica;
  }

  public String getDescrizioneEnteUtenteModifica()
  {
    return descrizioneEnteUtenteModifica;
  }

  public void setDescrizioneEnteUtenteModifica(
      String descrizioneEnteUtenteModifica)
  {
    this.descrizioneEnteUtenteModifica = descrizioneEnteUtenteModifica;
  }

  public void setIdAziendaSubentro(Long idAziendaSubentro)
  {
    this.idAziendaSubentro = idAziendaSubentro;
  }

  public Long getIdAziendaSubentro()
  {
    return idAziendaSubentro;
  }

  public void setCUAASubentro(String CUAASubentro)
  {
    this.CUAASubentro = CUAASubentro;
  }

  public String getCUAASubentro()
  {
    return CUAASubentro;
  }

  public void setFlagAziendaProvvisoria(boolean flagAziendaProvvisoria)
  {
    this.flagAziendaProvvisoria = flagAziendaProvvisoria;
  }

  public boolean isFlagAziendaProvvisoria()
  {
    return flagAziendaProvvisoria;
  }

  /**
   * @return the idOpr
   */
  public Long getIdOpr()
  {
    return idOpr;
  }

  /**
   * @param idOpr
   *          the idOpr to set
   */
  public void setIdOpr(Long idOpr)
  {
    this.idOpr = idOpr;
  }

  /**
   * @return the dataAggiornamentoOpr
   */
  public Date getDataAggiornamentoOpr()
  {
    return dataAggiornamentoOpr;
  }

  /**
   * @param dataAggiornamentoOpr
   *          the dataAggiornamentoOpr to set
   */
  public void setDataAggiornamentoOpr(Date dataAggiornamentoOpr)
  {
    this.dataAggiornamentoOpr = dataAggiornamentoOpr;
  }

  /**
   * @return the dataAperturaFascicolo
   */
  public Date getDataAperturaFascicolo()
  {
    return dataAperturaFascicolo;
  }

  /**
   * @param dataAperturaFascicolo
   *          the dataAperturaFascicolo to set
   */
  public void setDataAperturaFascicolo(Date dataAperturaFascicolo)
  {
    this.dataAperturaFascicolo = dataAperturaFascicolo;
  }

  /**
   * @return the dataChiusuraFascicolo
   */
  public Date getDataChiusuraFascicolo()
  {
    return dataChiusuraFascicolo;
  }

  /**
   * @param dataChiusuraFascicolo
   *          the dataChiusuraFascicolo to set
   */
  public void setDataChiusuraFascicolo(Date dataChiusuraFascicolo)
  {
    this.dataChiusuraFascicolo = dataChiusuraFascicolo;
  }

  /**
   * @return the idUtenteAggiornamentoOpr
   */
  public Long getIdUtenteAggiornamentoOpr()
  {
    return idUtenteAggiornamentoOpr;
  }

  /**
   * @param idUtenteAggiornamentoOpr
   *          the idUtenteAggiornamentoOpr to set
   */
  public void setIdUtenteAggiornamentoOpr(Long idUtenteAggiornamentoOpr)
  {
    this.idUtenteAggiornamentoOpr = idUtenteAggiornamentoOpr;
  }

  /**
   * Attributo per vitivinicolo
   */
  public void setPosizioneSchedario(String posizioneSchedario)
  {
    this.posizioneSchedario = posizioneSchedario;
  }

  public String getPosizioneSchedario()
  {
    return posizioneSchedario;
  }

  public void setPossiedeDelegaAttiva(boolean possiedeDelegaAttiva)
  {
    this.possiedeDelegaAttiva = possiedeDelegaAttiva;
  }

  public boolean isPossiedeDelegaAttiva()
  {
    return possiedeDelegaAttiva;
  }

  /** *************************************************************** */

  public void setIntestazionePartitaIva(String intestazionePartitaIva)
  {
    this.intestazionePartitaIva = intestazionePartitaIva;
  }

  public String getIntestazionePartitaIva()
  {
    return intestazionePartitaIva;
  }

  public String getCUAAAnagrafeTributaria()
  {
    return CUAAAnagrafeTributaria;
  }

  public void setCUAAAnagrafeTributaria(String anagrafeTributaria)
  {
    this.CUAAAnagrafeTributaria = anagrafeTributaria;
  }
  
  public String[] getCUAACollegati()
  {
    return CUAACollegati;
  }

  public void setCUAACollegati(String[] CUAACollegati)
  {
    this.CUAACollegati = CUAACollegati;
  }
  
  public Long getIdDimensioneAzienda()
  {
    return idDimensioneAzienda;
  }

  public void setIdDimensioneAzienda(Long idDimensioneAzienda)
  {
    this.idDimensioneAzienda = idDimensioneAzienda;
  }
  
  public Long getIdUde()
  {
    return idUde;
  }

  public void setIdUde(Long idUde)
  {
    this.idUde = idUde;
  }

  public Long getClasseUde()
  {
    return classeUde;
  }

  public void setClasseUde(Long classeUde)
  {
    this.classeUde = classeUde;
  }

  public BigDecimal getRls()
  {
    return rls;
  }

  public void setRls(BigDecimal rls)
  {
    this.rls = rls;
  }
  
  public BigDecimal getUlu() 
  {
	return ulu;
  }
	
  public void setUlu(BigDecimal ulu) 
  {
	this.ulu = ulu;
  }

  public String getDescDimensioneAzienda()
  {
    return descDimensioneAzienda;
  }

  public void setDescDimensioneAzienda(String descDimensioneAzienda)
  {
    this.descDimensioneAzienda = descDimensioneAzienda;
  }

  
  
  @SuppressWarnings("rawtypes")
  public Vector getVAziendaATECOSec()
  {
    return vAziendaATECOSec;
  }

  
  @SuppressWarnings("rawtypes")
  public void setVAziendaATECOSec(Vector aziendaATECOSec)
  {
    vAziendaATECOSec = aziendaATECOSec;
  }
  
  public boolean isModificaAtecoSec()
  {
    return modificaAtecoSec;
  }

  public void setModificaAtecoSec(boolean modificaAtecoSec)
  {
    this.modificaAtecoSec = modificaAtecoSec;
  }

  public String getLabelElencoAssociati()
  {
    if(labelElencoAssociati == null)
    {
      labelElencoAssociati = "Elenco associati";
    }
    return labelElencoAssociati;
  }

  public void setLabelElencoAssociati(String labelElencoAssociati)
  {
    this.labelElencoAssociati = labelElencoAssociati;
  }

  public String getLabelSubAssociati()
  {
    if(labelSubAssociati == null)
    {
      labelSubAssociati = "soci collegati";
    }
    return labelSubAssociati;
  }

  public void setLabelSubAssociati(String labelSubAssociati)
  {
    this.labelSubAssociati = labelSubAssociati;
  }

  public Long getIdTipoFormaAssociata()
  {
    return idTipoFormaAssociata;
  }

  public void setIdTipoFormaAssociata(Long idTipoFormaAssociata)
  {
    this.idTipoFormaAssociata = idTipoFormaAssociata;
  }

  public String getTelefono()
  {
    return telefono;
  }

  public void setTelefono(String telefono)
  {
    this.telefono = telefono;
  }

  public String getFax()
  {
    return fax;
  }

  public void setFax(String fax)
  {
    this.fax = fax;
  }

  public String getCodiceAgriturismo()
  {
    return codiceAgriturismo;
  }

  public void setCodiceAgriturismo(String codiceAgriturismo)
  {
    this.codiceAgriturismo = codiceAgriturismo;
  }


  public String getSedelegIstatEstero()
  {
    return sedelegIstatEstero;
  }


  public void setSedelegIstatEstero(String sedelegIstatEstero)
  {
    this.sedelegIstatEstero = sedelegIstatEstero;
  }


  public String getEsoneroPagamentoGF()
  {
    return esoneroPagamentoGF;
  }


  public void setEsoneroPagamentoGF(String esoneroPagamentoGF)
  {
    this.esoneroPagamentoGF = esoneroPagamentoGF;
  }

  public String getFascicoloDematerializzato()
  {
    return fascicoloDematerializzato;
  }

  public void setFascicoloDematerializzato(String fascicoloDematerializzato)
  {
    this.fascicoloDematerializzato = fascicoloDematerializzato;
  }

  public Date getDataControlliAllevamenti()
  {
    return dataControlliAllevamenti;
  }

  public void setDataControlliAllevamenti(Date dataControlliAllevamenti)
  {
    this.dataControlliAllevamenti = dataControlliAllevamenti;
  }

  public Date getDataAggiornamentoUma()
  {
    return dataAggiornamentoUma;
  }

  public void setDataAggiornamentoUma(Date dataAggiornamentoUma)
  {
    this.dataAggiornamentoUma = dataAggiornamentoUma;
  }

  public String getFlagIap()
  {
    return flagIap;
  }

  public void setFlagIap(String flagIap)
  {
    this.flagIap = flagIap;
  }

  public Date getDataIscrizioneRea()
  {
    return dataIscrizioneRea;
  }

  public void setDataIscrizioneRea(Date dataIscrizioneRea)
  {
    this.dataIscrizioneRea = dataIscrizioneRea;
  }

  public Date getDataCessazioneRea()
  {
    return dataCessazioneRea;
  }

  public void setDataCessazioneRea(Date dataCessazioneRea)
  {
    this.dataCessazioneRea = dataCessazioneRea;
  }

  public Date getDataIscrizioneRi()
  {
    return dataIscrizioneRi;
  }

  public void setDataIscrizioneRi(Date dataIscrizioneRi)
  {
    this.dataIscrizioneRi = dataIscrizioneRi;
  }

  public Date getDataCessazioneRi()
  {
    return dataCessazioneRi;
  }

  public void setDataCessazioneRi(Date dataCessazioneRi)
  {
    this.dataCessazioneRi = dataCessazioneRi;
  }

  public Date getDataInizioAteco()
  {
    return dataInizioAteco;
  }

  public void setDataInizioAteco(Date dataInizioAteco)
  {
    this.dataInizioAteco = dataInizioAteco;
  }

  @SuppressWarnings("rawtypes")
  public Vector getvAziendaSezioni()
  {
    return vAziendaSezioni;
  }

  @SuppressWarnings("rawtypes")
  public void setvAziendaSezioni(Vector vAziendaSezioni)
  {
    this.vAziendaSezioni = vAziendaSezioni;
  }
  
  

  

}
