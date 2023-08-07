package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.ValidationError;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class ManodoperaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 7469993333204467771L;

  private String idManodopera = null;
  private String idAzienda = null;
  private String codiceInps = null;
  private String matricolaInail = null;
  private TipoFormaConduzioneVO tipoFormaConduzioneVO = null;
  private Date dataInizioValiditaDate = null;
  private String dataInizioValidita = null;
  private Date dataFineValiditaDate = null;
  private String dataFineValidita = null;
  private Date dataAggiornamentoDate = null;
  private String dataAggiornamento = null;
  private UtenteIrideVO utenteAggiornamento = null;

  private String denominazioneIntermediario = null;
  private Vector<DettaglioManodoperaVO> vDettaglioManodopera = null;
  private Vector<DettaglioAttivitaVO> vDettaglioAttivita = null;
  private String tipoFormaConduzione = null; //tiene il valore selezionato nella Combo
  private Date dataSituazioneAlDate = null;
  private String dataSituazioneAl = null;
  private Integer idTipoIscrizioneINPS = null;
  private Date dataInizioIscrizioneDate;
  private String dataInizioIscrizione;
  private Date dataCessazioneIscrizioneDate;
  private String dataCessazioneIscrizione;
  private String descTipoIscrizioneINPS;
  

  public ManodoperaVO() {
  }

  public String getDump() {
    return null;
  }

  public ValidationErrors validate() {
    ValidationErrors errors = new ValidationErrors();

    return errors;
  }

  /**
   * Controllo correttezza formale DettaglioManodoperaVO
   * @param dettaglioManodoperaVO
   * @param err_uomini
   * @param err_donne
   * @param err_giornateAnnue
   * @return ValidationErrors
   */
  private ValidationErrors validateDettaglioManodoperaVO(ValidationErrors errors, DettaglioManodoperaVO dettaglioManodoperaVO, String err_uomini, String err_donne, String err_giornateAnnue) {
    if (errors == null) errors = new ValidationErrors();

    //Controllo correttezza formale

    errors = validateIntNumber(errors, dettaglioManodoperaVO.getUomini(), 3, err_uomini);

    errors = validateIntNumber(errors, dettaglioManodoperaVO.getDonne(), 3, err_donne);

    if (err_giornateAnnue != null)
      errors = validateIntNumber(errors, dettaglioManodoperaVO.getGiornateAnnue(), 7, err_giornateAnnue);

    return errors;
  }

  /**
   * Controllo tipo e intervallo numerico
   * @param sValue
   * @param iLength
   * @param errPlaceHolder
   * @return ValidationErrors
   */
  private ValidationErrors validateIntNumber(ValidationErrors errors, String sValue, int iLength, String errPlaceHolder) {
    if (errors == null) errors = new ValidationErrors();

    SolmrLogger.debug(this, "sValue: " + sValue + " iLength: " + iLength + " errPlaceHolder: " + errPlaceHolder);

    Long lValue = new Long(0);
    try {
      lValue = new Long(sValue);
    }
    catch (Exception ex) {}

    String sMaxNumber = "";
    for (int i = 0; i < iLength; i++) {
      sMaxNumber = sMaxNumber + "9";
    }

    if (Validator.isNotEmpty(sValue) & ! Validator.isNumericInteger(sValue))
    {
      SolmrLogger.debug(this, "\n\n\n#### Found error: Inserire un valore numerico intero.\n\n");
      errors.add(errPlaceHolder, new ValidationError("Inserire un valore numerico intero."));
    }
    else if (sValue.length() > iLength || lValue.longValue() < 0)
    {
      SolmrLogger.debug(this, "\n\n\n#### Found error: Inserire un dato compreso tra 0 e " + sMaxNumber + "\n\n");
      errors.add(errPlaceHolder, new ValidationError("Inserire un dato compreso tra 0 " + sMaxNumber));
    }

    return errors;
  }

  /**
   * Controllo obbligatorietà e correttezza formale
   * dei dati appartenenti al primo step di inserimento/modifica manodopera
   * @return ValidationErrors
   */
  public ValidationErrors validateManodopera() 
  {
    ValidationErrors errors = new ValidationErrors();

    //Lunghezza Codice Inps
    if(codiceInps != null)
    {
      if (codiceInps.length() > 15)
      {
        SolmrLogger.debug(this, "\n\n\n#### Found error: Il N° iscrizione INPS non può essere più lungo di 15 caratteri.\n\n");
        errors.add("codiceInps", new ValidationError("Il N° iscrizione INPS non può essere più lunga di 15 caratteri."));
      }
    }
    
    
    
    
    if(idTipoIscrizioneINPS != null)
    {      
      if(Validator.isNotEmpty(dataInizioIscrizione))
      {
        try
        {
          if(!Validator.validateDateF(dataInizioIscrizione))
            errors.add("dataInizioIscrizione", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
          else
            dataInizioIscrizioneDate = DateUtils.parse(dataInizioIscrizione, "dd/MM/yyyy");
        }
        catch(Exception ex)
        {
          errors.add("dataInizioIscrizione", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
        }
      }
      
      
      if(Validator.isNotEmpty(dataCessazioneIscrizione))
      {
        try
        {
          if(!Validator.validateDateF(dataCessazioneIscrizione))
            errors.add("dataCessazioneIscrizione", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
          else
            dataCessazioneIscrizioneDate = DateUtils.parse(dataCessazioneIscrizione, "dd/MM/yyyy");
        }
        catch(Exception ex)
        {
          errors.add("dataCessazioneIscrizione", new ValidationError(AnagErrors.ERRORE_FROMATO_DATA));
        }
        
      }
      
      //if(Validator.isEmpty(codiceInps))
        //errors.add("codiceInps", new ValidationError(AnagErrors.ERR_CAMPO_OBBLIGATORIO));
      
    }
    else
    {
      
      if(Validator.isNotEmpty(dataInizioIscrizione))
      {
        errors.add("dataInizioIscrizione", new ValidationError(AnagErrors.ERRORE_DATA_NULL));
      }
      
      if(Validator.isNotEmpty(dataCessazioneIscrizione))
      {
        errors.add("dataCessazioneIscrizione", new ValidationError(AnagErrors.ERRORE_DATA_NULL));
      }
      
      if(Validator.isNotEmpty(codiceInps))
      {
        errors.add("codiceInps", new ValidationError(AnagErrors.ERRORE_CODINPS_NULL));
      }
      
    }

    //Controllo obbligatorietà Dettaglio Manodopera

    String errDettaglioManodopera = "Inserire almeno un valore significativo relativo ad una delle classi di manodopera richieste";

    //E' richieso l'inserimento di almeno un valore significativo relativo ad
    //una delle classi di manodopera richieste
    /*DettaglioManodoperaVO dettaglioManodoperaVO = null;
    for (Iterator<DettaglioManodoperaVO> iter = vDettaglioManodopera.iterator(); iter.hasNext(); ) 
    {
      dettaglioManodoperaVO = (DettaglioManodoperaVO) iter.next();
      SolmrLogger.debug(this, "validateInsertNew dettaglioManodoperaVO.getCodTipoClasseManodopera(): " + dettaglioManodoperaVO.getCodTipoClasseManodopera());
      if (Validator.isNotEmpty(dettaglioManodoperaVO.getUomini()) |
      Validator.isNotEmpty(dettaglioManodoperaVO.getDonne()))
      {
        errDettaglioManodopera = "";
        break;
      }
    }

    if (! errDettaglioManodopera.equals(""))
    {
      SolmrLogger.debug(this, "\n\n\n#### Found error: " + errDettaglioManodopera + "\n\n");
      errors.add("famTempoPienoUomini", new ValidationError(errDettaglioManodopera));
      errors.add("famTempoPienoDonne", new ValidationError(errDettaglioManodopera));
      errors.add("famTempoParzUomini", new ValidationError(errDettaglioManodopera));
      errors.add("famTempoParzDonne", new ValidationError(errDettaglioManodopera));
      errors.add("salFisTempoPienoUomini", new ValidationError(errDettaglioManodopera));
      errors.add("salFisTempoPienoDonne", new ValidationError(errDettaglioManodopera));
      errors.add("salFisTempoParzUomini", new ValidationError(errDettaglioManodopera));
      errors.add("salFisTempoParzDonne", new ValidationError(errDettaglioManodopera));
      errors.add("salAvvUomini", new ValidationError(errDettaglioManodopera));
      errors.add("salAvvDonne", new ValidationError(errDettaglioManodopera));

      return errors;
    }*/

    //Controllo correttezza formale Dettaglio Manodopera

    if(vDettaglioManodopera != null)
    {
      //indice 0 del Vector: tipo classe codice 10    
      errors = validateDettaglioManodoperaVO(errors, (DettaglioManodoperaVO) vDettaglioManodopera.get(0), "famTempoPienoUomini", "famTempoPienoDonne", null);
  
      //indice 1 del Vector: tipo classe codice 20
      errors = validateDettaglioManodoperaVO(errors, (DettaglioManodoperaVO) vDettaglioManodopera.get(1), "famTempoParzUomini", "famTempoParzDonne", null);
  
      //indice 2 del Vector: tipo classe codice 30
      errors = validateDettaglioManodoperaVO(errors, (DettaglioManodoperaVO) vDettaglioManodopera.get(2), "salFisTempoPienoUomini", "salFisTempoPienoDonne", null);
  
      //indice 3 del Vector: tipo classe codice 40
      errors = validateDettaglioManodoperaVO(errors, (DettaglioManodoperaVO) vDettaglioManodopera.get(3), "salFisTempoParzUomini", "salFisTempoParzDonne", null);
  
      //indice 4 del Vector: tipo classe codice 50
      errors = validateDettaglioManodoperaVO(errors, (DettaglioManodoperaVO) vDettaglioManodopera.get(4), "salAvvUomini", "salAvvDonne", "salAvvGiornateAnnue");
    }
    
    return errors;
  }

  /**
   * Controllo obbligatorietà e correttezza formale
   * dei dati appartenenti al secondo step di inserimento/modifica manodopera
   * @return ValidationErrors
   */

  public ValidationErrors validateConduzione(HttpServletRequest request, String dataFineValiditaLastManodopera)
  {
    //nuove specifiche Teresa 02/05/2016 verifico che sia almeno valorizzato un valore....
    boolean flagAlmenoUnValore = false;
    
    SolmrLogger.debug(this, "dataFineValiditaLastManodopera: " + dataFineValiditaLastManodopera);

    ValidationErrors errors = new ValidationErrors();

    //Controllo obbligatorietà
    /*if (! Validator.isNotEmpty(tipoFormaConduzione))
    {
      SolmrLogger.debug(this, "\n\n\n#### Found error: Il campo Tipo Forma Conduzione è un dato obbligatorio\n\n");
      errors.add("tipoFormaConduzione", new ValidationError("Il campo Tipo Forma Conduzione è un dato obbligatorio"));
    }*/
    
    if (Validator.isNotEmpty(tipoFormaConduzione))
    {
      flagAlmenoUnValore = true;
    }

    //Attività complementari
    if (vDettaglioAttivita != null && vDettaglioAttivita.size() > 0)
    {
      DettaglioAttivitaVO dettaglioAttivitaVO = null;
      for (Iterator<DettaglioAttivitaVO> iter = vDettaglioAttivita.iterator(); iter.hasNext(); ) {
        dettaglioAttivitaVO = (DettaglioAttivitaVO) iter.next();
        if (Validator.isNotEmpty(dettaglioAttivitaVO.getDescrizione()) &&
        dettaglioAttivitaVO.getDescrizione().length() > 100)
        {
          SolmrLogger.debug(this, "\n\n\n#### Found error: La descrizione Attività Complementari non può essere più lunga di 100 caratteri.\n\n");
          errors.add("descrizione", new ValidationError("La descrizione Attività Complementari non può essere più lunga di 100 caratteri."));
        }
      }
      
      flagAlmenoUnValore = true;
    }
    
    if(Validator.isNotEmpty(codiceInps))
    {
      flagAlmenoUnValore = true;
    }   
    
    if(Validator.isNotEmpty(idTipoIscrizioneINPS))
    { 
      flagAlmenoUnValore = true;
    }
    
    if(Validator.isNotEmpty(dataInizioIscrizione))
    {
      flagAlmenoUnValore = true;
    }
      
    if(Validator.isNotEmpty(dataCessazioneIscrizione))
    {
      flagAlmenoUnValore = true;
    }
    
    if(vDettaglioManodopera != null)
    {
      flagAlmenoUnValore = true;
    }

    if(!flagAlmenoUnValore)
    {
      request.setAttribute("messaggioErrore", "Non e' stato impostato nessun campo tra questa pagina e quella precedente");      
    }
    if (errors.size() == 0)
    {
      //Sostituire i null di dettaglio manodopera con gli zeri
      DettaglioManodoperaVO dettaglioManodoperaVO = null;
      if((vDettaglioManodopera != null) && (vDettaglioManodopera.size() > 0))
      {
        for (Iterator<DettaglioManodoperaVO> iter = vDettaglioManodopera.iterator(); iter.hasNext(); ) 
        {
          dettaglioManodoperaVO = (DettaglioManodoperaVO) iter.next();
          SolmrLogger.debug(this, "validateInsertNew dettaglioManodoperaVO.getCodTipoClasseManodopera(): " + dettaglioManodoperaVO.getCodTipoClasseManodopera());
          if (! Validator.isNotEmpty(dettaglioManodoperaVO.getUomini()))
          {
            dettaglioManodoperaVO.setUomini("0");
          }
          if (! Validator.isNotEmpty(dettaglioManodoperaVO.getDonne()))
          {
            dettaglioManodoperaVO.setDonne("0");
          }
          //Qualora per una classe di manodopera venga indicato uno dei due valori n.uomini
          //oppure n.donne, il corrispondente valore n.giornate/anno deve essere valorizzato.
          //Per i campi non valorizzati impostare il valore 0.
          SolmrLogger.debug(this, "prima dettaglioManodoperaVO.getGiornateAnnue(): " + dettaglioManodoperaVO.getGiornateAnnue());
          SolmrLogger.debug(this, "dettaglioManodoperaVO.getUominiLong(): " + dettaglioManodoperaVO.getUominiLong());
          SolmrLogger.debug(this, "dettaglioManodoperaVO.getDonneLong(): " + dettaglioManodoperaVO.getDonneLong());
          if (dettaglioManodoperaVO.getCodTipoClasseManodopera().equals(SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI")) &&
          (dettaglioManodoperaVO.getUominiLong().longValue() > 0 |
          dettaglioManodoperaVO.getDonneLong().longValue() > 0) &&
          ! Validator.isNotEmpty(dettaglioManodoperaVO.getGiornateAnnue()))
          {
            dettaglioManodoperaVO.setGiornateAnnue("0");
          }
          SolmrLogger.debug(this, "dopo dettaglioManodoperaVO.getGiornateAnnue(): " + dettaglioManodoperaVO.getGiornateAnnue());
        }
      }
    }
    return errors;
  }

  public String calcoloIdTipoFormaConduzione(Vector<CodeDescription> vTipoFormaConduzione)
  {
    String codeTipoFormaConduzione = null;
    String idTipoFormaConduzione = null;

    long numFamTempoPieno = sommaPersone((DettaglioManodoperaVO) vDettaglioManodopera.get(0));
    long numFamTempoParz = sommaPersone((DettaglioManodoperaVO) vDettaglioManodopera.get(1));
    long numSalFisTempoPieno = sommaPersone((DettaglioManodoperaVO) vDettaglioManodopera.get(2));
    long numSalFisTempoParz = sommaPersone((DettaglioManodoperaVO) vDettaglioManodopera.get(3));
    long numSalAvv = sommaPersone((DettaglioManodoperaVO) vDettaglioManodopera.get(4));

    //tipo forma conduzione = 10
    if ( (numFamTempoPieno + numFamTempoParz > 0) &
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv == 0) )
    {
      codeTipoFormaConduzione = (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PIENO");
    }

    //tipo forma conduzione = 20
    if ( (numFamTempoPieno + numFamTempoParz > 0) &
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv > 0) &
        ( (numFamTempoPieno + numFamTempoParz) >=
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv) ) )
    {
      codeTipoFormaConduzione = (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_FAMIL_T_PARZIALE");
    }

    //tipo forma conduzione = 30
    if ( (numFamTempoPieno + numFamTempoParz > 0) &
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv > 0) &
        ( (numFamTempoPieno + numFamTempoParz) <
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv) ) )
    {
      codeTipoFormaConduzione = (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PIENO");
    }

    //tipo forma conduzione = 40
    if ( (numFamTempoPieno + numFamTempoParz == 0) &
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv > 0) )
    {
      codeTipoFormaConduzione = (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_FISSI_T_PARZIALE");
    }

    //tipo forma conduzione = 50
    if ( (numFamTempoPieno + numFamTempoParz == 0) &
        (numSalFisTempoPieno + numSalFisTempoParz + numSalAvv == 0) )
    {
      codeTipoFormaConduzione = (String) SolmrConstants.get("CODE_TIPO_CL_MANODOPERA_SALAR_AVVENTIZI");
    }

    CodeDescription codDesc = null;
    for (Iterator<CodeDescription> iter = vTipoFormaConduzione.iterator(); iter.hasNext(); ) {
      codDesc = (CodeDescription) iter.next();
      if (codDesc.getSecondaryCode().equals(codeTipoFormaConduzione))
      {
        idTipoFormaConduzione = codDesc.getCode().toString();
        SolmrLogger.debug(this, "idTipoFormaConduzione proposto: " + idTipoFormaConduzione);
      }
    }
    SolmrLogger.debug(this, "codeTipoFormaConduzione proposto: " + codeTipoFormaConduzione);
    return idTipoFormaConduzione;
  }

  private long sommaPersone(DettaglioManodoperaVO dettaglioManodoperaVO)
  {
    long uomini = dettaglioManodoperaVO.getUominiLong() == null ? 0 :
        dettaglioManodoperaVO.getUominiLong().longValue();
    long donne = dettaglioManodoperaVO.getDonneLong() == null ? 0 :
        dettaglioManodoperaVO.getDonneLong().longValue();
    return uomini + donne;
  }

  public void setIdManodopera(String idManodopera) {
    this.idManodopera = idManodopera;
  }

  public String getIdManodopera() {
    return idManodopera;
  }

  public Long getIdManodoperaLong() {
    try {
      return new Long(idManodopera);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdManodoperaLong(Long idManodopera) {
    this.idManodopera = idManodopera == null ? null : idManodopera.toString();
  }

  public String getIdAzienda() {
    return idAzienda;
  }

  public void setIdAzienda(String idAzienda) {
    this.idAzienda = idAzienda;
  }

  public Long getIdAziendaLong() {
    try {
      return new Long(idAzienda);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setIdAziendaLong(Long idAzienda) {
    this.idAzienda = idAzienda == null ? null : idAzienda.toString();
  }

  public String getCodiceInps() {
    return codiceInps;
  }

  public void setCodiceInps(String codiceInps) {
    this.codiceInps = codiceInps;
  }

  public TipoFormaConduzioneVO getTipoFormaConduzioneVO() {
    return tipoFormaConduzioneVO;
  }

  public void setTipoFormaConduzioneVO(TipoFormaConduzioneVO
                                       tipoFormaConduzioneVO) {
    this.tipoFormaConduzioneVO = tipoFormaConduzioneVO;
  }

  public String getDataInizioValidita() {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(String dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
    try
    {
      this.dataInizioValiditaDate = DateUtils.parseDate(dataInizioValidita);
    }
    catch (Exception ex)
    {
      this.dataInizioValiditaDate=null;
    }
  }

  public Date getDataInizioValiditaDate() {
    return dataInizioValiditaDate;
  }

  public void setDataInizioValiditaDate(Date dataInizioValiditaDate) {
    this.dataInizioValiditaDate = dataInizioValiditaDate;
    try {
      this.dataInizioValidita = DateUtils.formatDate(dataInizioValiditaDate);
    }
    catch (Exception ex) {
    }
  }

  public String getDataFineValidita() {
    return dataFineValidita;
  }

  public void setDataFineValidita(String dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
    try {
      this.dataFineValiditaDate = DateUtils.parseDate(dataFineValidita);
    }
    catch (Exception ex)
    {
      this.dataFineValiditaDate=null;
    }
  }

  public Date getDataFineValiditaDate() {
    return dataFineValiditaDate;
  }

  public void setDataFineValiditaDate(Date dataFineValiditaDate) {
    this.dataFineValiditaDate = dataFineValiditaDate;
    try {
      this.dataFineValidita = DateUtils.formatDate(dataFineValiditaDate);
    }
    catch (Exception ex) {
    }
  }

  public String getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(String dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
    try
    {
      this.dataAggiornamentoDate = DateUtils.parseDate(dataAggiornamento);
    }
    catch (Exception ex)
    {
      this.dataAggiornamentoDate =null;
    }
  }

  public Date getDataAggiornamentoDate() {
    return dataAggiornamentoDate;
  }

  public void setDataAggiornamentoDate(Date dataAggiornamentoDate) {
    this.dataAggiornamentoDate = dataAggiornamentoDate;
    try {
      this.dataAggiornamento = DateUtils.formatDate(dataAggiornamentoDate);
    }
    catch (Exception ex) {
    }
  }

  public UtenteIrideVO getUtenteAggiornamento() {
    return utenteAggiornamento;
  }

  public void setUtenteAggiornamento(UtenteIrideVO utenteAggiornamento) {
    this.utenteAggiornamento = utenteAggiornamento;
  }

  public String getDenominazioneIntermediario() {
    return denominazioneIntermediario;
  }

  public void setDenominazioneIntermediario(String denominazioneIntermediario) {
    this.denominazioneIntermediario = denominazioneIntermediario;
  }

  public Vector<DettaglioManodoperaVO> getVDettaglioManodopera() {
    return vDettaglioManodopera;
  }

  public void setVDettaglioManodopera(Vector<DettaglioManodoperaVO> vDettaglioManodopera) {
    this.vDettaglioManodopera = vDettaglioManodopera;
  }

  public Vector<DettaglioAttivitaVO> getVDettaglioAttivita() {
    return vDettaglioAttivita;
  }

  public void setVDettaglioAttivita(Vector<DettaglioAttivitaVO> vDettaglioAttivita) {
    this.vDettaglioAttivita = vDettaglioAttivita;
  }

  public String getTipoFormaConduzione() {
    return tipoFormaConduzione;
  }

  public void setTipoFormaConduzione(String tipoFormaConduzione) {
    this.tipoFormaConduzione = tipoFormaConduzione;
  }

  public Long getTipoFormaConduzioneLong() {
    try {
      return new Long(tipoFormaConduzione);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void setTipoFormaConduzioneLong(Long tipoFormaConduzione) {
    this.tipoFormaConduzione = tipoFormaConduzione == null ? null :
        tipoFormaConduzione.toString();
  }

  public String getDataSituazioneAl() {
    return dataSituazioneAl;
  }

  public void setDataSituazioneAl(String dataSituazioneAl)
  {
    this.dataSituazioneAl = dataSituazioneAl;
    try
    {
      this.dataSituazioneAlDate = DateUtils.parseDate(dataSituazioneAl);
    }
    catch (Exception ex)
    {
      this.dataSituazioneAlDate = null;
    }
  }

  public Date getDataSituazioneAlDate() {
    return dataSituazioneAlDate;
  }

  public void setDataSituazioneAlDate(Date dataSituazioneAlDate) {
    this.dataSituazioneAlDate = dataSituazioneAlDate;
    try {
      this.dataSituazioneAl = DateUtils.formatDate(dataSituazioneAlDate);
    }
    catch (Exception ex) {
    }
  }

  public String getMatricolaInail()
  {
    return matricolaInail;
  }

  public void setMatricolaInail(String matricolaInail)
  {
    this.matricolaInail = matricolaInail;
  }

  public Integer getIdTipoIscrizioneINPS()
  {
    return idTipoIscrizioneINPS;
  }

  public void setIdTipoIscrizioneINPS(Integer idTipoIscrizioneINPS)
  {
    this.idTipoIscrizioneINPS = idTipoIscrizioneINPS;
  }

  public Date getDataInizioIscrizioneDate()
  {
    return dataInizioIscrizioneDate;
  }

  public void setDataInizioIscrizioneDate(Date dataInizioIscrizioneDate)
  {
    this.dataInizioIscrizioneDate = dataInizioIscrizioneDate;
  }

  public String getDataInizioIscrizione()
  {
    return dataInizioIscrizione;
  }

  public void setDataInizioIscrizione(String dataInizioIscrizione)
  {
    this.dataInizioIscrizione = dataInizioIscrizione;
  }

  public Date getDataCessazioneIscrizioneDate()
  {
    return dataCessazioneIscrizioneDate;
  }

  public void setDataCessazioneIscrizioneDate(Date dataCessazioneIscrizioneDate)
  {
    this.dataCessazioneIscrizioneDate = dataCessazioneIscrizioneDate;
  }

  public String getDataCessazioneIscrizione()
  {
    return dataCessazioneIscrizione;
  }

  public void setDataCessazioneIscrizione(String dataCessazioneIscrizione)
  {
    this.dataCessazioneIscrizione = dataCessazioneIscrizione;
  }

  public String getDescTipoIscrizioneINPS()
  {
    return descTipoIscrizioneINPS;
  }

  public void setDescTipoIscrizioneINPS(String descTipoIscrizioneINPS)
  {
    this.descTipoIscrizioneINPS = descTipoIscrizioneINPS;
  }
  
  
}
