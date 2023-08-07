package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.etc.anag.*;
import it.csi.solmr.util.*;
import it.csi.solmr.etc.*;

public class NotificaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -51065868392875182L;
//	Attributi del DB
	private Long idNotifica = null; // Campo ID_NOTIFICA su DB_NOTIFICA
	private Long idAzienda = null; // Campo ID_AZIENDA su DB_NOTIFICA
	private Long idUtenteInserimento = null; // Campo ID_UTENTE_INSERIMENTO su DB_NOTIFICA
	private Long idProcedimentoMittente = null; // Campo ID_PROCEDIMENTO_MITTENTE su DB_NOTIFICA
	private Date dataInserimento = null; // Campo DATA_INSERIMENTO su DB_NOTIFICA
	private String descrizione = null; // Campo DESCRIZIONE su DB_NOTIFICA
	private Date dataChiusura = null; // Campo DATA_CHIUSURA su DB_NOTIFICA
	private Long idUtenteChiusura = null; // Campo ID_UTENTE_CHIUSURA su DB_NOTIFICA
	private Long idTipologiaNotifica = null; // Campo ID_TIPOLOGIA_NOTIFICA su DB_NOTIFICA
	private Long idCategoriaNotifica = null; // Campo ID_CATEGORIA_NOTIFICA su DB_NOTIFICA
	private String descCategoriaNotifica = null; // Campo DESCRIZIONE su DB_TIPO_CATEGORIA_NOTIFICA
	private Long idProcedimentoDestinatario = null; // Campo ID_PROCEDIMENTO_DESTINATARIO su DB_NOTIFICA
	private String noteChisura = null; // Campo NOTE_CHIUSURA su DB_NOTIFICA
	private String cuaa = null; // Campo CUAA su DB_ANAGRAFICA_AZIENDA
	private String denominazione = null; // Campo DENOMINAZIONE su DB_ANAGRAFICA_AZIENDA
	private String descrizioneComuneSedeLegale = null; // Campo DESCRIZIONE su COMUNE in relazione al campo SEDELEG-COMUNE su DB_ANAGRAFICA_AZIENDA
	private Long idAnagraficaAzienda = null; // Campo ID_ANAGRAFICA_AZIENDA su DB_ANAGRAFICA_AZIENDA
	private String istatProvinciaUtente = null; // Campo SEDELEG_COMUNE su DB_ANAGRAFICA_AZIENDA
	private String descTipologiaNotifica = null; // Campo DESCRIZIONE su DB_TIPO_TIPOLOGIA_NOTIFICA
	private String denominazioneUtenteInserimento = null; 
	private String descEnteAppartenenzaUtenteInserimento = null; 
	private String denominazioneUtenteChiusura = null; 
	private String descEnteAppartenenzaUtenteChiusura = null; 

	// Attributi logici usati per la gestione del sistema
	private String dataDal = null; // Attributo che non mappa nessun campo su DB: utilizzato nella ricerca delle notifiche
	private String dataAl = null; // Attributo che non mappa nessun campo su DB: utilizzato nella ricerca delle notifiche
	private String tipiCategoriaNotifica = null; // Attributo che non mappa nessun campo su DB: utilizzato nella ricerca delle notifiche
	private String tipologiaNotifica = null; // Attributo che non mappa nessun campo su DB: utilizzato nella ricerca delle notifiche
	private String strDataInserimento = null; // Attributo usato per formattare il campo DATA_INSERIMENTO su DB_NOTIFICA
	private String strDataChiusura = null; // Attributo usato per formattare il campo DATA_CHIUSURA su DB_NOTIFICA
	private Vector<StoricoParticellaVO> elencoParticelle = null;
	private Vector<NotificaEntitaVO> vNotificaEntita = null;
	private Vector<AllegatoDocumentoVO> vAllegatoDocumento;
	private Integer idTipoEntita;
	private String chiusaDaAltroRuolo;
	private String inviaEmail;
	

	public String getCuaa() {
		return cuaa;
	}

	public Date getDataChiusura() {
		return dataChiusura;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getDescrizioneComuneSedeLegale() {
		return descrizioneComuneSedeLegale;
	}

	public Long getIdAnagraficaAzienda() {
		return idAnagraficaAzienda;
	}

	public Long getIdAzienda() {
		return idAzienda;
	}

	public Long getIdNotifica() {
		return idNotifica;
	}

	public Long getIdProcedimentoDestinatario() {
		return idProcedimentoDestinatario;
	}

	public Long getIdProcedimentoMittente() {
		return idProcedimentoMittente;
	}

	public Long getIdTipologiaNotifica() {
		return idTipologiaNotifica;
	}

	public Long getIdUtenteChiusura() {
		return idUtenteChiusura;
	}

	public Long getIdUtenteInserimento() {
		return idUtenteInserimento;
	}

	public String getNoteChisura() {
		return noteChisura;
	}

	public String getIstatProvinciaUtente() {
		return istatProvinciaUtente;
	}

	public String getDataAl() {
		return dataAl;
	}

	public String getDataDal() {
		return dataDal;
	}

	public String getTipiCategoriaNotifica() {
		return tipiCategoriaNotifica;
	}

	public String getStrDataChiusura() {
		return strDataChiusura;
	}

	public String getStrDataInserimento() {
		return strDataInserimento;
	}

	public String getDescTipologiaNotifica() {
		return descTipologiaNotifica;
	}

	public String getDenominazioneUtenteChiusura() {
		return denominazioneUtenteChiusura;
	}

	public String getDenominazioneUtenteInserimento() {
		return denominazioneUtenteInserimento;
	}

	public String getDescEnteAppartenenzaUtenteChiusura() {
		return descEnteAppartenenzaUtenteChiusura;
	}

	public String getDescEnteAppartenenzaUtenteInserimento() {
		return descEnteAppartenenzaUtenteInserimento;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setDescrizioneComuneSedeLegale(String descrizioneComuneSedeLegale) {
		this.descrizioneComuneSedeLegale = descrizioneComuneSedeLegale;
	}

	public void setIdAnagraficaAzienda(Long idAnagraficaAzienda) {
		this.idAnagraficaAzienda = idAnagraficaAzienda;
	}

	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	public void setIdNotifica(Long idNotifica) {
		this.idNotifica = idNotifica;
	}

	public void setIdProcedimentoDestinatario(Long idProcedimentoDestinatario) {
		this.idProcedimentoDestinatario = idProcedimentoDestinatario;
	}

	public void setIdProcedimentoMittente(Long idProcedimentoMittente) {
		this.idProcedimentoMittente = idProcedimentoMittente;
	}

	public void setIdTipologiaNotifica(Long idTipologiaNotifica) {
		this.idTipologiaNotifica = idTipologiaNotifica;
	}

	public void setIdUtenteChiusura(Long idUtenteChiusura) {
		this.idUtenteChiusura = idUtenteChiusura;
	}

	public void setIdUtenteInserimento(Long idUtenteInserimento) {
		this.idUtenteInserimento = idUtenteInserimento;
	}

	public void setNoteChisura(String noteChisura) {
		this.noteChisura = noteChisura;
	}

	public void setIstatProvinciaUtente(String istatProvinciaUtente) {
		this.istatProvinciaUtente = istatProvinciaUtente;
	}

	public void setDataAl(String dataAl) {
		this.dataAl = dataAl;
	}

	public void setDataDal(String dataDal) {
		this.dataDal = dataDal;
	}

	public void setTipiCategoriaNotifica(String tipiCategoriaNotifica) {
		this.tipiCategoriaNotifica = tipiCategoriaNotifica;
	}

	public void setStrDataChiusura(String strDataChiusura) {
		this.strDataChiusura = strDataChiusura;
	}

	public void setStrDataInserimento(String strDataInserimento) {
		this.strDataInserimento = strDataInserimento;
	}

	public void setDescTipologiaNotifica(String descTipologiaNotifica) {
		this.descTipologiaNotifica = descTipologiaNotifica;
	}

	public void setDenominazioneUtenteChiusura(String denominazioneUtenteChiusura) {
		this.denominazioneUtenteChiusura = denominazioneUtenteChiusura;
	}

	public void setDenominazioneUtenteInserimento(String
			denominazioneUtenteInserimento) {
		this.denominazioneUtenteInserimento = denominazioneUtenteInserimento;
	}

	public void setDescEnteAppartenenzaUtenteChiusura(String
			descEnteAppartenenzaUtenteChiusura) {
		this.descEnteAppartenenzaUtenteChiusura =
			descEnteAppartenenzaUtenteChiusura;
	}

	public void setDescEnteAppartenenzaUtenteInserimento(String
			descEnteAppartenenzaUtenteInserimento) {
		this.descEnteAppartenenzaUtenteInserimento =
			descEnteAppartenenzaUtenteInserimento;
	}

	// Metodo utilizzato per la validazione formale dei dati nella ricerca notifiche
	public ValidationErrors validateRicercaNotifiche() 
	{

		ValidationErrors errors = new ValidationErrors();

		// La data dal è obbligatoria
		if(!Validator.isNotEmpty(dataDal)) {
			errors.add("dataDal", new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_OBBLIGATORIO")));
		}
		// Se è stata valorizzata controllo che sia una data valida
		else {
			if(!Validator.validateDateF(dataDal)) {
				errors.add("dataDal", new ValidationError((String)AnagErrors.get("ERR_DATA_ERRATA")));
			}
		}

		// Se la data al è stata valorizzata...
		if(Validator.isNotEmpty(dataAl)) {
			// ... Controllo che sia valida
			if(!Validator.validateDateF(dataAl)) {
				errors.add("dataAl", new ValidationError((String)AnagErrors.get("ERR_DATA_ERRATA")));
			}
			// Se è valida controllo che non sia antecedente alla data dal
			else {
				if(Validator.isNotEmpty(dataDal)) {
					try {
						if (DateUtils.parseDate(dataAl).before(DateUtils.parseDate(dataDal))) {
							errors.add("dataAl", new ValidationError( (String) AnagErrors.get("ERR_DATA_AL_MINORE_DATA_DAL")));
						}
					}
					catch (Exception e) {}
				}
			}
		}
		return errors;
	}

	// Metodo utilizzato per la validazione formale dei dati nell'inserimento della notifica
	public ValidationErrors validateInsertNotifica() {

		ValidationErrors errors = new ValidationErrors();

	  // La tipologia della notifica è obbligatoria
    if(!Validator.isNotEmpty(tipologiaNotifica)) 
    {
      errors.add("idTipologiaNotifica", new ValidationError((String)AnagErrors.get("ERR_TIPO_TIPOLOGIA_NOTIFICA_OBBLIGATORIO")));
    }
    else 
    {
      idTipologiaNotifica = Long.decode(tipologiaNotifica);
    }
		
		// La tipologia della notifica è obbligatoria
		if(!Validator.isNotEmpty(tipiCategoriaNotifica)) 
		{
			errors.add("idCategoriaNotifica", new ValidationError(AnagErrors.ERR_CATEGORIA_NOTIFICA_OBBLIGATORIO));
		}
		else 
		{
			idCategoriaNotifica = Long.decode(tipiCategoriaNotifica);
		}

		// La descrizione è obbligatoria
		if(!Validator.isNotEmpty(descrizione)) 
		{
			errors.add("descrizione", new ValidationError((String)AnagErrors.get("ERR_DESCRIZIONE_OBBLIGATORIA")));
		}
		// Se è valorizzata controllo che non superi i mille caratteri
		else {
			if(descrizione.length() > 4000) {
				errors.add("descrizione", new ValidationError((String)AnagErrors.ERR_MAX_4000_CARATTERI));
			}
		}

		return errors;
	}
	
//Metodo utilizzato per la validazione formale dei dati nell'inserimento della notifica
 public ValidationErrors validateModificaNotifica() 
 {

   ValidationErrors errors = new ValidationErrors();

   // La descrizione è obbligatoria
   if(!Validator.isNotEmpty(descrizione)) 
   {
     errors.add("descrizione", new ValidationError((String)AnagErrors.get("ERR_DESCRIZIONE_OBBLIGATORIA")));
   }
   // Se è valorizzata controllo che non superi i mille caratteri
   else {
     if(descrizione.length() > 4000) {
       errors.add("descrizione", new ValidationError((String)AnagErrors.ERR_MAX_4000_CARATTERI));
     }
   }

   return errors;
 }

	// Metodo utilizzato per la validazione formale dei dati nella chiusura della notifica
	public ValidationErrors validateCloseNotifica() {

		ValidationErrors errors = new ValidationErrors();

		// Le note chiusura sono obbligatorie se la notifica è di tipo bloccante o warning
		if(idTipologiaNotifica.compareTo((Long)SolmrConstants.get("ID_TIPO_TIPOLOGIA_INFORMAZIONE")) !=0) {
			if(!Validator.isNotEmpty(noteChisura)) {
				errors.add("noteChiusura", new ValidationError( (String) AnagErrors.get("ERR_NOTE_CHIUSURA_OBBLIGATORIE")));
			}
		}

		// Se sono valorizzate controllo che non superino i 1000 caratteri
		if(Validator.isNotEmpty(noteChisura)) {
			if(noteChisura.length() > 1000) {
				errors.add("noteChiusura", new ValidationError( (String) AnagErrors.get("ERR_MAX_1000_CARATTERI")));
			}
		}

		return errors;
	}

  public Long getIdCategoriaNotifica()
  {
    return idCategoriaNotifica;
  }

  public void setIdCategoriaNotifica(Long idCategoriaNotifica)
  {
    this.idCategoriaNotifica = idCategoriaNotifica;
  }

  public String getDescCategoriaNotifica()
  {
    return descCategoriaNotifica;
  }

  public void setDescCategoriaNotifica(String descCategoriaNotifica)
  {
    this.descCategoriaNotifica = descCategoriaNotifica;
  }

  public String getTipologiaNotifica()
  {
    return tipologiaNotifica;
  }

  public void setTipologiaNotifica(String tipologiaNotifica)
  {
    this.tipologiaNotifica = tipologiaNotifica;
  }

  public Vector<StoricoParticellaVO> getElencoParticelle()
  {
    return elencoParticelle;
  }

  public void setElencoParticelle(Vector<StoricoParticellaVO> elencoParticelle)
  {
    this.elencoParticelle = elencoParticelle;
  }

  public Vector<NotificaEntitaVO> getvNotificaEntita()
  {
    return vNotificaEntita;
  }

  public void setvNotificaEntita(Vector<NotificaEntitaVO> vNotificaEntita)
  {
    this.vNotificaEntita = vNotificaEntita;
  }

  public Vector<AllegatoDocumentoVO> getvAllegatoDocumento()
  {
    return vAllegatoDocumento;
  }

  public void setvAllegatoDocumento(Vector<AllegatoDocumentoVO> vAllegatoDocumento)
  {
    this.vAllegatoDocumento = vAllegatoDocumento;
  }

  public Integer getIdTipoEntita()
  {
    return idTipoEntita;
  }

  public void setIdTipoEntita(Integer idTipoEntita)
  {
    this.idTipoEntita = idTipoEntita;
  }

  public String getChiusaDaAltroRuolo()
  {
    return chiusaDaAltroRuolo;
  }

  public void setChiusaDaAltroRuolo(String chiusaDaAltroRuolo)
  {
    this.chiusaDaAltroRuolo = chiusaDaAltroRuolo;
  }

  public String getInviaEmail()
  {
    return inviaEmail;
  }

  public void setInviaEmail(String inviaEmail)
  {
    this.inviaEmail = inviaEmail;
  }
	
	

}
