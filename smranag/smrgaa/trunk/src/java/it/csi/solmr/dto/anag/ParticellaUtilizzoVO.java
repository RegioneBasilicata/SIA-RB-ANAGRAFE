package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

import it.csi.solmr.etc.anag.*;
import it.csi.solmr.util.*;

public class ParticellaUtilizzoVO implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = 8604122269453854131L;

	private String tipiUtilizzoAttivi = null;
	private String descTipiUtilizzoAttivi = null;
	private String superficieUtilizzata = null;
	private String indirizziTipiUtilizzoAttivi;
	private String indirizziDescTipiUtilizzoAttivi;
	private String note;
	private Long idTipoIndirizzoUtilizzo = null; // ID_INDIRIZZO_UTILIZZO su DB_TIPO_INDIRIZZO_UTILIZZO
	private Long idTipoUtilizzo = null; // ID_UTILIZZO su DB_TIPO_UTILIZZO
	private String descTipoUtilizzo = null; // DESCRIZIONE su DB_TIPO_UTILIZZO
	private String annoUtilizzo = null; // ANNO su DB_UTILIZZO_PARTICELLA
	private Long idUtilizzoParticella = null; // ID_UTILIZZO_PARTICELLA su DB_UTILIZZO_PARTICELLA
	private Long idUtilizzoDichiarato = null; // ID_UTILIZZO_DICHIARATO su DB_UTILIZZO_DICHIARATO
	private String descTipoUtilizzoSecondario = null; // DESCRIZIONE su DB_TIPO_UTILIZZO in relazione a ID_UTILIZZO_SECONDARIO presente su DB_UTILIZZO_PARTICELLA
	private String superficieUtilizzataSecondaria = null; // SUP_UTILIZZATA_SECONDARIA su DB_UTILIZZO_PARTICELLA
	private Long idUtilizzoSecondario = null; // ID_UTILIZZO_SECONDARIO su DB_UTILIZZO_PARTICELLA

	private String flagSAU = null; // FLAG_SAU su DB_TIPO_UTILIZZO
	private String flagAlimentazioneAnimale; // FLAG_ALIMENTAZIONE_ANIMALE su DB_TIPO_UTILIZZO
	private String flagArboreo; // FLAG_ARBOREO su DB_TIPO_UTILIZZO
	private String flagColturaSecondaria; // FLAG_COLTURA_SECONDARIA su DB_TIPO_UTILIZZO
	private String flagForaggera; // FLAG_FORAGGERA su DB_TIPO_UTILIZZO
	private String flagPascolo; // FLAG_PASCOLO su DB_TIPO_UTILIZZO
	private String flagSerra; // FLAG_SERRA su DB_TIPO_UTILIZZO
	private String flagUba_Sostenibile;// FLAG_UBA_SOSTENIBILE su DB_TIPO_UTILIZZO
	private String tipo;// TIPO su DB_TIPO_UTILIZZO
	private String flagNidi;// FLAG_NIDI su DB_TIPO_UTILIZZO
	private String flagPratoPascolo;// FLAG_PRATO_PASCOLO su DB_TIPO_UTILIZZO

	private String flagSAU_Sec = null; // FLAG_SAU su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagAlimentazioneAnimale_Sec; // FLAG_ALIMENTAZIONE_ANIMALE su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagArboreo_Sec; // FLAG_ARBOREO su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagColturaSecondaria_Sec; // FLAG_COLTURA_SECONDARIA su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagForaggera_Sec; // FLAG_FORAGGERA su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagPascolo_Sec; // FLAG_PASCOLO su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagSerra_Sec; // FLAG_SERRA su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagUba_Sostenibile_Sec;// FLAG_UBA_SOSTENIBILE su DB_TIPO_UTILIZZO per coltura secondaria
	private String tipo_Sec;// TIPO su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagNidi_Sec;// FLAG_NIDI su DB_TIPO_UTILIZZO per coltura secondaria
	private String flagPratoPascolo_Sec;// FLAG_PRATO_PASCOLO su DB_TIPO_UTILIZZO per coltura secondaria

	private Long idConduzioneParticella = null; // ID_CONDUZIONE_PARTICELLA su DB_UTILIZZO_PARTICELLA
	private Date dataAggiornamento = null; // DATA_AGGIORNAMENTO su DB_UTILIZZO_PARTICELLA
	private Long idUtenteAggiornamento = null; // ID_UTENTE_AGGIORNAMENTO su DB_UTILIZZO_PARTICELLA

	public ParticellaUtilizzoVO() {
	}

	public int hashCode(){
		return (tipiUtilizzoAttivi == null ? 0 : tipiUtilizzoAttivi.hashCode()) +
		(descTipiUtilizzoAttivi == null ? 0 : descTipiUtilizzoAttivi.hashCode()) +
		(superficieUtilizzata == null ? 0 : superficieUtilizzata.hashCode())+
		(indirizziTipiUtilizzoAttivi == null ? 0 : indirizziTipiUtilizzoAttivi.hashCode())+
		(indirizziDescTipiUtilizzoAttivi == null ? 0 : indirizziDescTipiUtilizzoAttivi.hashCode())+
		(note == null ? 0 : note.hashCode())+
		(idTipoIndirizzoUtilizzo == null ? 0 : idTipoIndirizzoUtilizzo.hashCode())+
		(idTipoUtilizzo == null ? 0 : idTipoUtilizzo.hashCode())+
		(descTipoUtilizzo == null ? 0 : descTipoUtilizzo.hashCode())+
		(annoUtilizzo == null ? 0 : annoUtilizzo.hashCode())+
		(idUtilizzoParticella == null ? 0 : idUtilizzoParticella.hashCode())+
		(idUtilizzoDichiarato == null ? 0 : idUtilizzoDichiarato.hashCode())+
		(descTipoUtilizzoSecondario == null ? 0 : descTipoUtilizzoSecondario.hashCode())+
		(superficieUtilizzataSecondaria == null ? 0 : superficieUtilizzataSecondaria.hashCode())+
		(idUtilizzoSecondario == null ? 0 : idUtilizzoSecondario.hashCode())+
		(idConduzioneParticella == null ? 0 : idConduzioneParticella.hashCode())+
		(dataAggiornamento == null ? 0 : dataAggiornamento.hashCode())+
		(idUtenteAggiornamento == null ? 0 : idUtenteAggiornamento.hashCode())+
		(flagSAU == null ? 0 : flagSAU.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof ParticellaUtilizzoVO) {
			ParticellaUtilizzoVO other = (ParticellaUtilizzoVO)o;
			return (this.tipiUtilizzoAttivi == null && other.tipiUtilizzoAttivi == null || this.tipiUtilizzoAttivi == other.tipiUtilizzoAttivi) &&
			(this.descTipiUtilizzoAttivi == null && other.descTipiUtilizzoAttivi == null || this.descTipiUtilizzoAttivi == other.descTipiUtilizzoAttivi) &&
			(this.superficieUtilizzata == null && other.superficieUtilizzata == null || this.superficieUtilizzata == other.superficieUtilizzata) &&
			(this.indirizziTipiUtilizzoAttivi == null && other.indirizziTipiUtilizzoAttivi == null || this.indirizziTipiUtilizzoAttivi == other.indirizziTipiUtilizzoAttivi) &&
			(this.indirizziDescTipiUtilizzoAttivi == null && other.indirizziDescTipiUtilizzoAttivi == null || this.indirizziDescTipiUtilizzoAttivi == other.indirizziDescTipiUtilizzoAttivi) &&
			(this.note == null && other.note == null || this.note == other.note) &&
			(this.idTipoIndirizzoUtilizzo == null && other.idTipoIndirizzoUtilizzo == null || this.idTipoIndirizzoUtilizzo == other.idTipoIndirizzoUtilizzo) &&
			(this.idTipoUtilizzo == null && other.idTipoUtilizzo == null || this.idTipoUtilizzo == other.idTipoUtilizzo) &&
			(this.descTipoUtilizzo == null && other.descTipoUtilizzo == null || this.descTipoUtilizzo == other.descTipoUtilizzo) &&
			(this.annoUtilizzo == null && other.annoUtilizzo == null || this.annoUtilizzo == other.annoUtilizzo) &&
			(this.idUtilizzoParticella == null && other.idUtilizzoParticella == null || this.idUtilizzoParticella == other.idUtilizzoParticella) &&
			(this.idUtilizzoDichiarato == null && other.idUtilizzoDichiarato == null || this.idUtilizzoDichiarato == other.idUtilizzoDichiarato) &&
			(this.descTipoUtilizzoSecondario == null && other.descTipoUtilizzoSecondario == null || this.descTipoUtilizzoSecondario == other.descTipoUtilizzoSecondario) &&
			(this.superficieUtilizzataSecondaria == null && other.superficieUtilizzataSecondaria == null || this.superficieUtilizzataSecondaria == other.superficieUtilizzataSecondaria) &&
			(this.idUtilizzoSecondario == null && other.idUtilizzoSecondario == null || this.idUtilizzoSecondario == other.idUtilizzoSecondario) &&
			(this.idConduzioneParticella == null && other.idConduzioneParticella == null || this.idConduzioneParticella == other.idConduzioneParticella) &&
			(this.dataAggiornamento == null && other.dataAggiornamento == null || this.dataAggiornamento == other.dataAggiornamento) &&
			(this.idUtenteAggiornamento == null && other.idUtenteAggiornamento == null || this.idUtenteAggiornamento == other.idUtenteAggiornamento) &&
			(this.flagSAU == null && other.flagSAU == null || this.flagSAU == other.flagSAU);

		} else
			return false;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note = note;
	}
	public String getSuperficieUtilizzata()
	{
		return superficieUtilizzata;
	}
	public void setSuperficieUtilizzata(String superficieUtilizzata)
	{
		this.superficieUtilizzata = superficieUtilizzata;
	}
	public void setTipiUtilizzoAttivi(String tipiUtilizzoAttivi)
	{
		this.tipiUtilizzoAttivi = tipiUtilizzoAttivi;
	}
	public String getTipiUtilizzoAttivi()
	{
		return tipiUtilizzoAttivi;
	}
	public void setDescTipiUtilizzoAttivi(String descTipiUtilizzoAttivi)
	{
		this.descTipiUtilizzoAttivi = descTipiUtilizzoAttivi;
	}
	public String getDescTipiUtilizzoAttivi()
	{
		return descTipiUtilizzoAttivi;
	}
	public void setIndirizziTipiUtilizzoAttivi(String indirizziTipiUtilizzoAttivi)
	{
		this.indirizziTipiUtilizzoAttivi = indirizziTipiUtilizzoAttivi;
	}
	public String getIndirizziTipiUtilizzoAttivi()
	{
		return indirizziTipiUtilizzoAttivi;
	}
	public void setIndirizziDescTipiUtilizzoAttivi(String indirizziDescTipiUtilizzoAttivi)
	{
		this.indirizziDescTipiUtilizzoAttivi = indirizziDescTipiUtilizzoAttivi;
	}
	public String getIndirizziDescTipiUtilizzoAttivi()
	{
		return indirizziDescTipiUtilizzoAttivi;
	}
	public Long getIdTipoIndirizzoUtilizzo() {
		return idTipoIndirizzoUtilizzo;
	}
	public void setIdTipoIndirizzoUtilizzo(Long idTipoIndirizzoUtilizzo) {
		this.idTipoIndirizzoUtilizzo = idTipoIndirizzoUtilizzo;
	}
	public Long getIdTipoUtilizzo() {
		return idTipoUtilizzo;
	}
	public void setIdTipoUtilizzo(Long idTipoUtilizzo) {
		this.idTipoUtilizzo = idTipoUtilizzo;
	}
	public String getDescTipoUtilizzo() {
		return descTipoUtilizzo;
	}
	public void setDescTipoUtilizzo(String descTipoUtilizzo) {
		this.descTipoUtilizzo = descTipoUtilizzo;
	}
	public String getAnnoUtilizzo() {
		return annoUtilizzo;
	}
	public void setAnnoUtilizzo(String annoUtilizzo) {
		this.annoUtilizzo = annoUtilizzo;
	}
	public Long getIdUtilizzoParticella() {
		return idUtilizzoParticella;
	}
	public void setIdUtilizzoParticella(Long idUtilizzoParticella) {
		this.idUtilizzoParticella = idUtilizzoParticella;
	}
	public Long getIdUtilizzoDichiarato() {
		return idUtilizzoDichiarato;
	}

	public String getDescTipoUtilizzoSecondario() {
		return descTipoUtilizzoSecondario;
	}

	public String getSuperficieUtilizzataSecondaria() {
		return superficieUtilizzataSecondaria;
	}

	public Long getIdUtilizzoSecondario() {
		return idUtilizzoSecondario;
	}

	public String getFlagSAU() {
		return flagSAU;
	}

	public String getFlagSAU_Sec() {
		return flagSAU_Sec;
	}

	public void setIdUtilizzoDichiarato(Long idUtilizzoDichiarato) {
		this.idUtilizzoDichiarato = idUtilizzoDichiarato;
	}

	public void setDescTipoUtilizzoSecondario(String descTipoUtilizzoSecondario) {
		this.descTipoUtilizzoSecondario = descTipoUtilizzoSecondario;
	}

	public void setSuperficieUtilizzataSecondaria(String
			superficieUtilizzataSecondaria) {
		this.superficieUtilizzataSecondaria = superficieUtilizzataSecondaria;
	}

	public void setIdUtilizzoSecondario(Long idUtilizzoSecondario) {
		this.idUtilizzoSecondario = idUtilizzoSecondario;
	}

	public void setFlagSAU(String flagSAU) {
		this.flagSAU = flagSAU;
	}

	public void setFlagSAU_Sec(String flagSAU_Sec) {
		this.flagSAU_Sec = flagSAU_Sec;
	}

	public void setFlagAlimentazioneAnimale(String flagAlimentazioneAnimale)
	{
		this.flagAlimentazioneAnimale = flagAlimentazioneAnimale;
	}
	public String getFlagAlimentazioneAnimale()
	{
		return flagAlimentazioneAnimale;
	}

	public void setFlagAlimentazioneAnimale_Sec(String flagAlimentazioneAnimale_Sec)
	{
		this.flagAlimentazioneAnimale_Sec = flagAlimentazioneAnimale_Sec;
	}
	public String getFlagAlimentazioneAnimale_Sec()
	{
		return flagAlimentazioneAnimale_Sec;
	}

	public void setFlagArboreo(String flagArboreo)
	{
		this.flagArboreo = flagArboreo;
	}
	public String getFlagArboreo()
	{
		return flagArboreo;
	}

	public void setFlagArboreo_Sec(String flagArboreo_Sec)
	{
		this.flagArboreo_Sec = flagArboreo_Sec;
	}
	public String getFlagArboreo_Sec()
	{
		return flagArboreo_Sec;
	}

	public void setFlagColturaSecondaria(String flagColturaSecondaria)
	{
		this.flagColturaSecondaria = flagColturaSecondaria;
	}
	public String getFlagColturaSecondaria()
	{
		return flagColturaSecondaria;
	}

	public void setFlagColturaSecondaria_Sec(String flagColturaSecondaria_Sec)
	{
		this.flagColturaSecondaria_Sec = flagColturaSecondaria_Sec;
	}
	public String getFlagColturaSecondaria_Sec()
	{
		return flagColturaSecondaria_Sec;
	}

	public void setFlagForaggera(String flagForaggera)
	{
		this.flagForaggera = flagForaggera;
	}
	public String getFlagForaggera()
	{
		return flagForaggera;
	}

	public void setFlagForaggera_Sec(String flagForaggera_Sec)
	{
		this.flagForaggera_Sec = flagForaggera_Sec;
	}
	public String getFlagForaggera_Sec()
	{
		return flagForaggera_Sec;
	}

	public void setFlagPascolo(String flagPascolo)
	{
		this.flagPascolo = flagPascolo;
	}
	public String getFlagPascolo()
	{
		return flagPascolo;
	}

	public void setFlagPascolo_Sec(String flagPascolo_Sec)
	{
		this.flagPascolo_Sec = flagPascolo_Sec;
	}
	public String getFlagPascolo_Sec()
	{
		return flagPascolo_Sec;
	}

	public void setFlagSerra(String flagSerra)
	{
		this.flagSerra = flagSerra;
	}
	public String getFlagSerra()
	{
		return flagSerra;
	}

	public void setFlagSerra_Sec(String flagSerra_Sec)
	{
		this.flagSerra_Sec = flagSerra_Sec;
	}
	public String getFlagSerra_Sec()
	{
		return flagSerra_Sec;
	}

	public void setFlagUba_Sostenibile(String flagUba_Sostenibile)
	{
		this.flagUba_Sostenibile = flagUba_Sostenibile;
	}
	public String getFlagUba_Sostenibile()
	{
		return flagUba_Sostenibile;
	}

	public void setFlagUba_Sostenibile_Sec(String flagUba_Sostenibile_Sec)
	{
		this.flagUba_Sostenibile_Sec = flagUba_Sostenibile_Sec;
	}
	public String getFlagUba_Sostenibile_Sec()
	{
		return flagUba_Sostenibile_Sec;
	}

	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	public String getTipo()
	{
		return tipo;
	}

	public void setTipo_Sec(String tipo_Sec)
	{
		this.tipo_Sec = tipo_Sec;
	}
	public String getTipo_Sec()
	{
		return tipo_Sec;
	}

	public void setFlagNidi(String flagNidi)
	{
		this.flagNidi = flagNidi;
	}
	public String getFlagNidi()
	{
		return flagNidi;
	}

	public void setFlagNidi_Sec(String flagNidi_Sec)
	{
		this.flagNidi_Sec = flagNidi_Sec;
	}
	public String getFlagNidi_Sec()
	{
		return flagNidi_Sec;
	}

	public void setFlagPratoPascolo(String flagPratoPascolo)
	{
		this.flagPratoPascolo = flagPratoPascolo;
	}
	public String getFlagPratoPascolo()
	{
		return flagPratoPascolo;
	}

	public void setFlagPratoPascolo_Sec(String flagPratoPascolo_Sec)
	{
		this.flagPratoPascolo_Sec = flagPratoPascolo_Sec;
	}
	public String getFlagPratoPascolo_Sec()
	{
		return flagPratoPascolo_Sec;
	}


	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
	}

	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
		this.idUtenteAggiornamento = idUtenteAggiornamento;
	}

	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}

	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	public Long getIdUtenteAggiornamento() {
		return idUtenteAggiornamento;
	}

	/*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_BEGIN*/
	public ValidationErrors validateInsertUtilizzo() {
		ValidationErrors errors = new ValidationErrors();

		// Se  uno dei dati è stato valorizzato, i valori diventano tutti obbligatori
		if(Validator.isNotEmpty(tipiUtilizzoAttivi) || Validator.isNotEmpty(superficieUtilizzata)
				|| Validator.isNotEmpty(indirizziTipiUtilizzoAttivi))
		{
			if(!Validator.isNotEmpty(tipiUtilizzoAttivi)) {
				errors.add("tipiUtilizzoAttivi",new ValidationError((String)AnagErrors.get("ERR_TIPO_UTILIZZO_OBBLIGATORIO")));
			}
			if(!Validator.isNotEmpty(indirizziTipiUtilizzoAttivi)) {
				errors.add("indirizziTipiUtilizzoAttivi",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_TIPO_UTILIZZO_OBBLIGATORIO")));
			}
			if(!Validator.isNotEmpty(superficieUtilizzata)) {
				errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUPERFICIE_UTILIZZATA_OBBLIGATORIA")));
			}
		}

		//verifico che le note, se valorizzate, non siano più lunghe di 100 caratteri
		if(Validator.isNotEmpty(note) && note.length() > 100)
		{
			errors.add("note", new ValidationError("Le note non possono essere più lunghe di 100 caratteri"));
		}

		// Controllo che la superficie utilizzata inserita sia corretta
		if(Validator.isNotEmpty(superficieUtilizzata))
		{
			if(Validator.validateDouble(superficieUtilizzata, 9999999999.9999) == null)
			{
				errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
			}
			else {
				int valoreInizio = superficieUtilizzata.indexOf(",");
				if((superficieUtilizzata.length() - valoreInizio) > 5) {
					errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
				}
				else {
					superficieUtilizzata = StringUtils.parseSuperficieField(Validator.validateDouble(superficieUtilizzata,9999999999.9999));
					/**
					 * Controllo che la superficie sia maggiore di 0
					 */
					try {
						if(Double.parseDouble(superficieUtilizzata.replace(',','.')) == 0)
							errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
					}
					catch(Exception e) {
						errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
					}
				}
			}
		}
		return errors;
	}

	// Metodo per effettuare la validazione formale dei dati in modifica superficie
	public ValidationErrors validateModificaSuperficie() {
		ValidationErrors errors = new ValidationErrors();

		if(!Validator.isNotEmpty(idTipoUtilizzo)) {
			errors.add("idTipoUtilizzo",new ValidationError((String)AnagErrors.get("ERR_TIPO_UTILIZZO_OBBLIGATORIO")));
		}
		if(!Validator.isNotEmpty(idTipoIndirizzoUtilizzo)) {
			errors.add("idIndirizzoUtilizzo",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_TIPO_UTILIZZO_OBBLIGATORIO")));
		}
		if(!Validator.isNotEmpty(superficieUtilizzata)) {
			errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUPERFICIE_UTILIZZATA_OBBLIGATORIA")));
		}

		//verifico che le note, se valorizzate, non siano più lunghe di 100 caratteri
		if(Validator.isNotEmpty(note) && note.length() > 100) {
			errors.add("note", new ValidationError((String)AnagErrors.get("ERR_NOTE_UTILIZZO_PARTICELLA")));
		}

		// Controllo che la superficie utilizzata inserita sia corretta
		if(Validator.isNotEmpty(superficieUtilizzata)) {
			if(Validator.validateDouble(superficieUtilizzata, 999999.9999) == null) {
				errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
			}
			else {
				int valoreInizio = superficieUtilizzata.indexOf(",");
				if((superficieUtilizzata.length() - valoreInizio) > 5) {
					errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
				}
				else {
					superficieUtilizzata = StringUtils.parseSuperficieField(Validator.validateDouble(superficieUtilizzata,999999.9999));
					/**
					 * Controllo che la superficie sia maggiore di 0
					 */
					try {
						if(Double.parseDouble(superficieUtilizzata.replace(',','.')) == 0) {
							errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
						}
					}
					catch(Exception e) {
						errors.add("superficieUtilizzata",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_ERRATA")));
					}
				}
			}
		}

		// La superficie utilizzata secondaria può essere valorizzata solo se è stato specificato
		// il tipo utilizzo secondario
		if(!Validator.isNotEmpty(idUtilizzoSecondario)) {
			if(Validator.isNotEmpty(superficieUtilizzataSecondaria)) {
				errors.add("superficieUtilizzataSecondaria",new ValidationError((String)AnagErrors.get("ERR_SUP_SECONDARIA_NO_VALORIZZABILE")));
			}
		}
		// Se invece è stato indicato l'uso secondario effettuo i controlli di validità sulla superficie
		// secondaria
		else {
			// La superficie secondaria deve essere valorizzata
			if(!Validator.isNotEmpty(superficieUtilizzataSecondaria)) {
				errors.add("superficieUtilizzataSecondaria",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_SECONDARIA_OBBLIGATORIA")));
			}
			// se lo è controllo che sia valida
			else {
				if(Validator.validateDouble(superficieUtilizzataSecondaria, 999999.9999) == null) {
					errors.add("superficieUtilizzataSecondaria", new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_SECONDARIA_ERRATA")));
				}
				else {
					int valoreInizio = superficieUtilizzataSecondaria.indexOf(",");
					if((superficieUtilizzataSecondaria.length() - valoreInizio) > 5) {
						errors.add("superficieUtilizzataSecondaria", new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_SECONDARIA_ERRATA")));
					}
					else {
						superficieUtilizzataSecondaria = StringUtils.parseSuperficieField(Validator.validateDouble(superficieUtilizzataSecondaria,999999.9999));
						/**
						 * Controllo che la superficie sia maggiore di 0
						 */
						try {
							if(Double.parseDouble(superficieUtilizzataSecondaria.replace(',', '.')) == 0) {
								errors.add("superficieUtilizzataSecondaria",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_SECONDARIA_ERRATA")));
							}
						}
						catch(Exception e) {
							errors.add("superficieUtilizzataSecondaria", new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_SECONDARIA_ERRATA")));
						}
						// Controllo che la superficie secondaria utilizzata non sia maggiore della superficie
						// utilizzata
						if(Validator.isNotEmpty(superficieUtilizzata)) {
							if(Double.parseDouble(Validator.validateDouble(superficieUtilizzata, 999999.9999)) <
									Double.parseDouble(Validator.validateDouble(superficieUtilizzataSecondaria,999999.9999))) {
								errors.add("superficieUtilizzataSecondaria",new ValidationError((String)AnagErrors.get("ERR_SUP_UTILIZZATA_SECONDARIA_ERRATA2")));
							}
						}
					}
				}
			}
		}
		return errors;
	}
	/*BLOCK_TO_BE_REMOVED_FOR_LIBRARY_CREATION_END*/
}

