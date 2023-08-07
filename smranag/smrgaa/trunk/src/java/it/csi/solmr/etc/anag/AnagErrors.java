package it.csi.solmr.etc.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import it.csi.solmr.etc.AbstractSolmrEtc;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.DateUtils;

public abstract class AnagErrors extends AbstractSolmrEtc {
  protected static final String MY_RESOURCE_BUNDLE = "it.csi.solmr.etc.anag.anagErrors";
  protected static final Class<?> THIS_CLASS = AnagErrors.class;

  public static String EXC_DATA_ACCESS;
  public static String EXC_NOT_FOUND;
  public static String EXC_NOT_FOUND_PK;
  public static String EXC_RESOURCE_ACCESS;

  public static String EXC_AUTHENTICATION_FAILED;

  public static String EXC_CONF_FILE_NF;

  public static String RICERCACOMUNI;
  public static String RICERCASTATOESTERO;
  public static String CODICEISTATCOMUNE;
  public static String ERROREPROFILO;
  public static String RICERCAATTIVITAOTE;
  public static String RICERCAATTIVITAATECO;
  public static String RICERCA_TERRENI_UTE;
  public static String ERR_RICERCA_TERRENI;
  public static String RICERCA_TERRENI;
  public static String RICERCA_ANNI_TERRENI;

  public static String DATE_ERRATE_CESS_AZIENDA;
  public static String CAUSALE_NULL;
  public static String LENGTH_CAUSALE_MAX_100;
  public static String DATA_CESSAZIONE_NULL;
  public static String CAMPI_NULL;
  public static String AZIENDA_CESSATA;

  public static String CUAA_GIA_ESISTENTE;
  public static String PIVA_GIA_ESISTENTE;

  public static String DELETE_UTE;
  public static String DELETE_UTE_PARTICELLE;
  public static String DELETE_UTE_FABBRICATI;
  public static String DELETE_UTE_ALLEVAMENTI;
  public static String SELEZIONA_UTE;
  public static String RICERCA_UTE;
  public static String ESISTE_UTE_CON_COMUNE;

  public static String SCELTA_ATTIVITA;
  public static String NB_CF_SBAGLIATO;

  public static String INTERMEDIARIO_SENZA_DELEGA;
  public static String PROVINCIA_NON_VALIDA;

  public static String NESSUNA_ATECO_TROVATA;
  public static String NESSUNA_OTE_TROVATA;
  public static String NESSUNA_AZIENDA_TROVATA;
  public static String CUAA_INESISTENTE;
  public static String NESSUN_RAPPRESENTANTE_LEGALE;

  public static String SOGGETTO_NON_PRESENTE;
  public static String INSERISCI_SOGGETTO;
  public static String MODIFICA_SOGGETTO;
  public static String DATA_INIZIO_RUOLO;
  public static String DATA_FINE_RUOLO;
  public static String RICERCATROPPICOMUNI;

  public static String ERR_DATA_PASSAGGIO_ERRATA;
  public static String ERR_DATA_PASSAGGIO_ERRATA_DATA_ODIERNA;
  public static String ERR_SISTEMA;
  public static String ERR_DENOMINAZIONE_OBBLIGATORIA;
  public static String ERR_CUAA_OBBLIGATORIO;
  public static String ERR_CUAA_ERRATO;
  public static String ERR_CUAA_FORMA_GIURIDICA;
  public static String ERR_PARTITA_IVA_OBBLIGATORIA;
  public static String ERR_PARTITA_IVA_ERRATA;
  public static String ERR_TIPO_AZIENDA_OBBLIGATORIO;
  public static String ERR_PROVINCIA_REA_OBBLIGATORIA;
  public static String ERR_PROVINCIA_REA_ERRATA;
  public static String ERR_NUMERO_REA_OBBLIGATORIO;
  public static String ERR_NUMERO_REA_ERRATO;
  public static String ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_ERRATO;
  public static String ERR_ANNO_ISCRIZIONE_REGISTRO_IMPRESE_OBBLIGATORIO;
  public static String ERR_NUMERO_REGISTRO_IMPRESE_ERRATO;
  public static String ERR_NUMERO_REGISTRO_IMPRESE_OBBLIGATORIO="Il numero di iscrizione registro imprese dell''azienda agricola è obbligatorio";
  public static String ERR_CUAA_INSEDIATO_DUP="Esiste già, fra quelle insediate, una azienda agricola con il CUAA inserito";
  public static String ERR_CODFIS_SU_CODFIS="Non si può cambiare il CUAA con un''altro codice fiscale";
  public static String ERR_CODFISRL_SU_CUAA="Il codice fisacale del titolare deve coincidere con il CUAA";
  public static String ERR_COGNOME_TITOLARE_OBBLIGATORIO;
  public static String ERR_NOME_TITOLARE_OBBLIGATORIO;
  public static String ERR_SESSO_TITOLARE_OBBLIGATORIO;
  public static String ERR_DATA_NASCITA_TITOLARE_OBBLIGATORIA;
  public static String ERR_DATA_NASCITA_TITOLARE_ERRATA;
  public static String ERR_STATO_ESTERO_NASCITA_TITOLARE_OBBLIGATORIO;
  public static String ERR_CITTA_ESTERO_NASCITA_TITOLARE_OBBLIGATORIA;
  public static String ERR_COMUNE_NASCITA_TITOLARE_OBBLIGATORIO;
  public static String ERR_PROVINCIA_NASCITA_TITOLARE_OBBLIGATORIA;
  public static String ERR_CITTA_ESTERO_NASCITA_TITOLARE_ERRATA;
  public static String ERR_STATO_ESTERO_NASCITA_TITOLARE_ERRATO;
  public static String ERR_INDIRIZZO_RESIDENZA_TITOLARE_OBBLIGATORIO;
  public static String ERR_CAP_RESIDENZA_TITOLARE_OBBLIGATORIO;
  public static String ERR_CAP_RESIDENZA_TITOLARE_ERRATO;
  public static String ERR_COMUNE_RESIDENZA_TITOLARE_OBBLIGATORIO;
  public static String ERR_PROVINCIA_RESIDENZA_TITOLARE_OBBLIGATORIO;
  public static String ERR_STATO_ESTERO_RESIDENZA_TITOLARE_OBBLIGATORIO;
  public static String ERR_STATO_ESTERO_RESIDENZA_TITOLARE_ERRATO;
  public static String ERR_CITTA_ESTERA_RESIDENZA_TITOLARE_ERRATA;
  public static String ERR_CAP_RESIDENZA_TITOLARE_NO_VALORIZZABILE;
  public static String ERR_COMUNE_RESIDENZA_TITOLARE_NO_VALORIZZABILE;
  public static String ERR_PROVINCIA_RESIDENZA_TITOLARE_NO_VALORIZZABILE;
  public static String ERR_MAIL_ERRATA;
  public static String ERR_INDIRIZZO_SEDE_OBBLIGATORIO;
  public static String ERR_PROVINCIA_SEDE_OBBLIGATORIA;
  public static String ERR_COMUNE_SEDE_OBBLIGATORIO;
  public static String ERR_CAP_SEDE_OBBLIGATORIO;
  public static String ERR_PROVINCIA_SEDE_NO_VALORIZZABILE;
  public static String ERR_COMUNE_SEDE_NO_VALORIZZABILE;
  public static String ERR_CAP_SEDE_NO_VALORIZZABILE;
  public static String ERR_CITTA_ESTERO_SEDE_ERRATA;
  public static String ERR_CODICEOTE_NON_TROVATO;
  public static String ERR_CODICEATECO_NON_TROVATO;
  public static String ERR_FORMA_GIURIDICA_OBBLIGATORIA;
  public static String ERR_UTENTE_NON_ABILITATO;
  public static String ERR_RECORD_STORICIZZATO;
  public static String ERR_COMUNE_SEDE_INESISTENTE;
  public static String ERR_STATO_ESTERO_SEDE_INESISTENTE;
  public static String ERR_NOME_CODICE_FISCALE;
  public static String ERR_COGNOME_CODICE_FISCALE;
  public static String ERR_ANNO_NASCITA_CODICE_FISCALE;
  public static String ERR_MESE_NASCITA_CODICE_FISCALE;
  public static String ERR_GIORNO_NASCITA_CODICE_FISCALE;
  public static String ERR_COMUNE_NASCITA_CODICE_FISCALE;
  public static String ERR_SESSO_CODICE_FISCALE;
  public static String ERR_CODICE_FISCALE;
  public static String ERR_GENERIC_CODICE_FISCALE;
  public static String ERR_CODICE_FISCALE_OBBLIGATORIO;

  public static final String CONTROLLO_OK = "Controllo con esito positivo";

  public static Object get(String key) {
    return get(THIS_CLASS, key);
  }

  static {
    initClass(THIS_CLASS, MY_RESOURCE_BUNDLE);
  }

  //inizio errori autenticazione
  public static final String MSG_ERRORE_CONTATTARE_ASSISTENZA="Attenzione si è verificato un errore imprevisto. Contattare l'assistenza tecnica";
  public static final String ERRORE_ABILITAZIONE_NO_ABILITAZIONE=" Utente non abilitato ad accedere a tale funzionalità";
  public static final String ERRORE_COMUNE_NON_DISPONIBILE="Servizio di comune non disponibile";
  public static final String ERRORE_ACCESSO_A_COMUNE="Si è verificato un errore nell'accesso al servizio di comune";
  public static final String PROBLEMI_ACCESSO_COMUNE="Problemi di accesso a Comune - Contattare l'assistenza tecnica.";
  public static final String PROBLEMI_ACCESSO_IRIDE2="Problemi di accesso a IRIDE2 - Contattare l'assistenza tecnica.";
  public static final String UTENTE_NON_ABILITATO_IDENTITA_PROVIDER="Il codice di IdentitaProvider che è stato indicato come parte di uno username, oppure la CA che ha emesso un certificato X.509 che è stato usato per l'autenticazione, non corrispondono a nessuno degli IdentitaProvider registrati, afferenti al SAAC.";
  public static final String UTENTE_NON_ABILITATO_CERTIFICATO_REVOCATO="Utente non abilitato, certificato revocato.";
  public static final String UTENTE_NON_ABILITATO_CERTIFICATO_FUORI_VALIDITA="Utente non abilitato, certificato non valido";
  public static final String UTENTE_NON_AUTENTICATO="Username o password non corretti.";
  public static final String UTENTE_NON_ABILITATO_USERNAME_NON_FORMATO="Lo username deve rispettare la sintassi <username>@<codiceProvider>.";
  public static final String UTENTE_SENZA_RUOLI_PROCEDIMENTO="Utente senza ruoli sull'applicativo.";
  public static final String ERRORE_PROBLEMI_ACCESSO_CU_IRIDE2="Errore nell'accesso ai casi d'uso IRIDE2";
  public static final String ERRORE_DI_SISTEMA="Si è verificato un errore di sistema";
  public static final String ERRORE_DI_DELEGA_SOCI="Si è verificato un errore nel reperimento della delega per un socio figlio";
  public static final String ERRORE_AUT_UTENTE_SENZA_MANDATO = "Utente non autorizzato a procedere in quanto privo di mandato per la posizione anagrafica";
  public static final String ERRORE_AUT_AZIENDA_CON_MANDATO = "Utente non autorizzato a procedere in quanto l'azienda ha un mandato attivo per la posizione anagrafica";
  public static final String ERRORE_AUT_NO_PROV_COMPETENZA = "Utente non autorizzato a procedere in quanto l'azienda non appartiene alla propria provincia di competenza";
  public static final String ERRORE_AUT_AZIENDA_NON_PROPRIA = "Utente non autorizzato a procedere su aziende diverse dalla propria";
  public static final String ERRORE_AUT_AZIENDA_CESSATA = "Azienda cessata";
  public static final String ERRORE_AUT_AZIENDA_STORICIZZATA_CESSATA = "Azienda cessata o storicizzata";
  public static final String ERRORE_AUT_AZIENDA_STORICIZZATA = "Azienda storicizzata";
  public static final String ERRORE_AUT_NON_AUTORIZZATO = "Utente non autorizzato a procedere";
  public static final String ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_NO_FORMA_GIURIDICA = "Utente non autorizzato a procedere: azienda priva di forma giuridica";
  public static final String ERRORE_AUT_NON_AUTORIZZATO_AZIENDA_FOR_FORMA_GIURIDICA = "Utente non autorizzato a procedere: operazione non consentita in relazione alla forma giuridica";
  public static final String ERRORE_CUAA_SIAN_NON_VALORIZZATO = "Utente non autorizzato a procedere: azienda priva di CUAA";
  public static final String ERRORE_DI_SISTEMA_AZIENDA_NON_REPERITA = "Utente non autorizzato a procedere: si è verificato un errore di sistema imprevisto che ha impedito di reperire i dati dell'azienda";
  public static final String NO_AZIENDE_ATTIVE_FOR_CUAA = "Non esiste alcuna azienda attiva in anagrafe a fronte del CUAA abilitato";
  public static final String ERRORE_KO_VALIDAZIONE_FOR_COD_FISC_RAPP_LEG = "Impossibile proseguire con la nuova validazione in quanto il rappresentante legale risulta privo di codice fiscale";
  public static final String ERRORE_KO_VERIFICA_FOR_COD_FISC_RAPP_LEG = "Impossibile proseguire con la verifica consistenza in quanto il rappresentante legale risulta privo di codice fiscale";
  public static final String ERRORE_DI_DELEGA_GESTORE_CAA="Si è verificato un errore nel reperimento dei dati dell'intermediario";
  public static final String ERRORE_DI_DELEGA_GESTORE_CAA_SEL="L'intermediario conesso non ha mandato sull'intermediario selezioneto";
  //Fine errori autenticazione
  
  public static final String ERRORE_KO_GIS_INVALID_PARAMETER = "Impossibile accedere al GIS: utente non abilitato";
  //public static final String ERRORE_KO_GIS_CONNECTION = "Impossibile accedere al GIS: errore di connessione";
  public static final String ERRORE_KO_GIS_CONNECTION = "Funzionalita' GIS non disponibile";
  public static final String ERRORE_KO_GIS_FIND_TERRENI_PARAMETER = "Impossibile accedere al GIS: si è verificato un errore imprevisto durante il reperimento dei parametri necessari al richiamo del servizio";
  public static final String ERRORE_KO_GIS_PART_PROVVISORIA = "Impossibile accedere alla fonte GIS in quanto la particella selezionata risulta provvisoria";
  public static final String ERRORE_UTE_DUP_KEY = "Non è possibile inserire due ute aventi lo stesso comune e indirizzo";

  public static final String ERR_TIPOLOGIA_COLTURA_SERRA_ERRATA = "La tipologia coltura serra è obbligatoria";
  public static final String ERR_MESI_RISC_SERRA_ERRATA = "Il numero di mesi di riscaldamento serra inserito è errato";
  public static final String ERR_ORE_RISC_ERRATA = "Le ore annuali di riscaldamento serra inserite sono errate";
  public static final String ERR_ORE_RISC_ERRATA_SUP_ORE_LIMITE = "In base al numero di mesi di riscaldamento serra inseriti il numero di ore non può essere superiore a: ";
  public static final String ERR_MESI_RISC_SERRA_OBBLIGATORIA = "Il numero di mesi di riscaldamento serra è obbligatorio";
  public static final String ERR_ORE_RISC_OBBLIGATORIA = "Le ore annuali di riscaldamento serra sono obbligatorie";
  
  // Inizio Costanti conti correnti
  public static final String ERRORE_KO_PARAMETRO_OCIN = "Si è verificato un errore imprevisto durante il reperimento del parametro OCIN";
  public static final String ERRORE_KO_INSERT_CONTO_CORRENTE_FOR_PARAMETRO_OCIN = "Non è possibile proseguire con l'inserimento del conto corrente in quanto non è stato reperito il parametro OCIN";
  public static final String ERRORE_KO_PARAMETRO_IBAN = "L''IBAN deve ha una lunghezza obbligatoria di 27 caratteri alfanumerici.";
  public static final String ERRORE_CODICE_PAESE_IBAN = "Il codice paese dell''IBAN inserito non è corretto.";
  public static final String ERRORE_MOTIVO_VALIDAZIONE = "Il campo motivo rivalidazione è obbligatorio se il conto corrente è nello stato invalidato";
  public static final String ERRORE_CC_DUP_KEY = "Non si possono inserire due conti correnti aventi lo stesso IBAN";
  public static final String ERRORE_SPORT_NO_GF = "La filiale/sportello selezionati non prevedono la gestione di conti correnti a contabilita'' speciale";
  public static final String ERRORE_KO_DET_CC = "Si è verificato un errore imprevisto durante il reperimento dei dati del conto corrente";
  //Fine Costanti conti correnti
  
  // Inizio Costanti documenti
  public static final String ERRORE_KO_TIPO_TIPOLOGIA_DOCUMENTO = "Si è verificato un errore imprevisto durante il reperimento della tipologia del documento";
  public static final String ERRORE_KO_CATEGORIA_DOCUMENTO = "Si è verificato un errore imprevisto durante il reperimento del tipo categoria documento";
  public static final String ERRORE_KO_TIPO_DOCUMENTO = "Si è verificato un errore imprevisto durante il reperimento del tipo documento";
  public static final String ERR_DOCUMENTO_PRESENTE_FOR_NUMERO_ENTE_RILASCIO = "Esiste già un documento attivo legato all''azienda con lo stesso numero ed ente rilascio";
  public static final String ERR_INSERT_DOCUMENTO_KO_FOR_COMUNE = "Non è possibile inserire il documento in quanto non è stato possibile reperire la sigla dell''amministrazione dell''utente collegato";
  public static final String ERR_PROPRIETARIO_DOCUMENTO_GIA_INSERITO = "Errore: il codice fiscale inserito è già stato indicato come proprietario";
  public static final String ERRORE_PROPRIETARIO_DOCUMENTO_NOT_FOUND = "Proprietario non presente in anagrafe.Procedere con l''inserimento del proprietario";
  public static final String ERRORE_PARTICELLA_DOCUMENTO_GIA_INSERITO = "Attenzione: una o più particelle selezionate erano già presenti in elenco e non sono state aggiunte nuovamente";
  public static final String ERRORE_PROPRIETARIO_DOCUMENTO_KO = "E'' obbligatorio indicare almeno un proprietario";
  public static final String ERRORE_PARTICELLA_DOCUMENTO_KO = "E'' obbligatorio indicare almeno una particella";
  public static final String ERRORE_PARTICELLA_ASS_DOCUMENTO_KO = "E'' obbligatorio associare almeno una particella";
  public static final String ERRORE_PROPRIETARIO_DOCUMENTO_ELIMA_KO = "Selezionare un proprietario";
  public static final String ERRORE_PARTICELLA_DOCUMENTO_ELIMA_KO = "Selezionare una particella";
  public static final String ERRORE_DATA_FINE_VALIDITA_OBBLIGATORIA_FOR_DOCUMENT_SELECTED = "La data fine validità è obbligatoria per il documento selezionato";
  public static final String ERRORE_DATA_INIZIO_POST_DATA_FINE = "La data inizio validita'' deve essere antecedente alla data fine validita''";
  public static final String ERRORE_KO_DOCUMENTO = "Si è verificato un''errore imprevisto durante il reperimento dei dati del documento";
  public static final String ERRORE_MODIFICA_DOCUMENTO_KO = "Non è possibile aggiornare documenti annullati o storicizzati";
  public static final String ERRORE_MODIFICA_DOCUMENTO_KO_FOR_TIPOLOGIA = "Impossibile procedere: la tipologia di documento risulta non più valida";
  public static final String ERRORE_UPDATE_DOCUMENTO_KO_FOR_COMUNE = "Non è possibile modificare il documento in quanto non è stato possibile reperire la sigla dell''amministrazione dell''utente collegato";
  public static final String ERRORE_GENERIC_CODICE_FISCALE_PARTITA_IVA_ERRATA = "Il codice fiscale/partita IVA inserito è errato";
  public static final String ERRORE_NO_PROPRIETARI_FOR_DOCUMENTO = "Non esistono proprietari relativi al documento selezionato";
  public static final String ERRORE_NO_PARTICELLE_FOR_DOCUMENTO_ASS = "Non esistono particelle associate relative al documento selezionato";
  public static final String ERRORE_NO_PARTICELLE_FOR_DOCUMENTO_DA_ASS = "Non esistono particelle da associare relative al documento selezionato";
  public static final String ERRORE_KO_CUAA_DOCUMENTI = "Sì è verificato un errore imprevisto durante il reperimento dei CUAA dell'azienda selezionata";
  public static final String ERRORE_NO_CUAA_FOR_AZIENDA = "Non esistono CUAA relativi all'azienda agricola selezionata";
  public static final String ERRORE_KO_SEARCH_DOCUMENTI = "Si è verificato un'errore imprevisto durante la ricerca dei documenti";
  public static final String ERRORE_KO_SEARCH_NO_DOCUMENTI_FOUND = "Non sono stati trovati documenti che soddisfino i criteri di ricerca impostati";
  public static final String ERRORE_KO_DATA_MAX_ESECUZIONE = "Si è verificato un errore imprevisto durante il reperimento della data esecuzione controlli";
  public static final String ERRORE_KO_CUAA_PROPRIETARI = "Si è verificato un errore imprevisto durante il reperimento dei CUAA dei proprietari dei documenti dell'azienda";
  public static final String ERRORE_KO_PARAMETRO_DOAT = "Si è verificato un errore imprevisto durante il reperimento del parametro DOAT";
  public static final String ERRORE_KO_SIAN_AGGIORNA_DATI_TRIBUTARIA = "Si è verificato un errore imprevisto durante l'aggiornamento dei dati proveniente dall'anagrafe tributaria";
  public static final String ERRORE_KO_ANOMALIE_DOCUMENTI = "Si è verificato un errore imprevisto durante il reperimento delle anomalie relative al documento selezionato";
  public static final String ERRORE_NO_ANOMALIE_DOCUMENTI = "Non sono state rilevate anomalie sul documento selezionato";
  public static final String ERRORE_KO_CUAA_SOCCIDARIO_EQUAL_CUAA_AZIENDA = "Il CUAA soccidario non può essere uguale al CUAA dell''azienda agricola";
  public static final String ERRORE_KO_CUAA_SOCCIDARIO_NOT_EQUAL_AT = "CUAA non congruente con quello presente su anagrafe tributaria";
  public static final String ERRORE_CONDUZIONI_DOCUMENTO_SEARCH = "Si è verificato un problema tecnico durante il reperimento delle conduzioni associate al documento selezionato";
  public static final String ERRORE_KO_ELENCO_ANOMALIE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle anomalie";
  public static final String ERRORE_KO_ELENCO_FASI_DOC = "Si è verificato un errore imprevisto durante il reperimento della fase del documento istanza di riesame";
  public static final String ERR_INSERT_DOCUMENTO_KO_CONTO_CORRENTE = "Selezionare un conto corrente";
  public static final String ERR_DETTAGLIO_DOCUMENTO_CONTO_CORRENTE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al conto corrente";
  public static final String ERRORE_LUNGHEZZA_MAX_CARATTERI = "Superato il limite massimo di caratteri consentiti";
  public static final String ERRORE_DIMENSIONE_MAX_FILE = "Superata la dimensione massima consentita per il file allegato (in MegaBytes)";
  public static final String ERRORE_KO_PARAMETRO_MAX_DIM_FILE_UP = "Si è verificato un errore imprevisto durante il reperimento del parametro MAX_DIM_FILE_UP per la gestione della modifica dei file allegati";
  public static final String ERRORE_PARTICELLA_IST_RIE_LAVORATA = "Modifica non permessa: per la particella selezionata il processo di istanza di riesame e'' in corso";
  public static final String ERRORE_DOC_IST_RIE_LAVORATA = "Modifica del documento non permessa: il processo di istanza di riesame e'' in corso";
  public static final String ERRORE_DOC_ANNULLA_IST_RIE_LAVORATA = "Annullamento del documento non permesso: il processo di istanza di riesame e'' in corso";
  public static final String ERRORE_DOC_ANNUL_MOD_IST_RIE_CONVOCA = "Impossibile modificare o annullare il documento: e'' gia'' stata avviata la fase di contraddittorio dell''istanza";
  public static final String ERRORE_DOC_ANNUL_PARTICELLA_EVASA = "Impossibile annullare il documento: e'' presente almeno una particella evasa";
  public static final String ERRORE_IST_RIE_CAUS_MOD = "E' possibile la selezione della sola correzione anomalia";
  public static final String ERRORE_DOC_PROTOC_IST_RIE_LAVORATA = "Repertoriazione del documento non permesso: il processo di istanza di riesame e'' in corso";
  public static final String ERRORE_DOC_IST_RIE_P26 = "Modifica del documento non permessa: e'' possibile associare al documento solo particelle con anomalia GIS P26";
  public static final String ERRORE_DOC_IST_RIE_PART_OTHER_DOC = "Modifica del documento non permessa: sono presenti una o piu'' particelle gia'' presenti in altri documenti di istanza di riesame.";
  public static final String ERRORE_DOC_IST_RIE_PART_ASSERV = "Modifica del documento non permessa: sono presenti una o piu'' particelle in asservimento.";
  public static final String ERRORE_DOC_IST_RIE_PART_PRIOR_NOTE = "Modifica del documento non permessa: sono presenti, per singola particella, due o piu'' conduzioni associate al documento con note e priorita'' di lavorazione differenti.";
  public static final String ERRORE_DOC_IST_RIE_PART_NO_PIEMO = "Sono consentite solamente particelle TOBECONFIG.";
  public static final String ERRORE_KO_PARAMETRO_GGFOTORIC = "Si è verificato un errore imprevisto durante il reperimento del parametro GGFOTORIC per la gestione della modifica dei documenti";
  public static final String ERRORE_KO_PARAMETRO_GGCONTRORIC = "Si è verificato un errore imprevisto durante il reperimento del parametro GGCONTRORIC per la gestione della modifica dei documenti";
  public static final String ERRORE_DOC_IST_RIE_FASE_PREC = "Almeno una particella selezionata non risulta evasa in una istanza di riesame di fase precedente a quella che si sta'' inserendo";
  public static final String ERRORE_KO_AGGIORNA_DOCUMENTO = "Si è verificato un errore imprevisto durante il salvataggio dei dati relativi al documento";
  public static final String ERRORE_KO_INSERISCI_DOCUMENTO = "Si è verificato un errore imprevisto durante l'inserimento del documento";
  public static final String RICERCADOCUMENTI = "Si è verificato un errore imprevisto durante la ricerca del tipo del documento";
  public static final String ERRORE_DOCUMENTO_NON_ISTANZA_RIESAME = "Impossibile procedere: la tipologia di documento non e'' istanza di riesame";
  public static final String ERRORE_DOCUMENTO_NON_ISTANZA_RIESAME_FOTO = "Impossibile procedere: la tipologia di documento non e'' istanza di riesame - fotointerpretazione";
  public static final String ERRORE_KO_PARTICELLE_ISTANZA_FOTO = "Si è verificato un''errore imprevisto durante il reperimento delle particelle fotointerpretate";
  public static final String ERRORE_DOC_IST_RIE_PART_OTHER_DOC_FOTO = "Modifica del documento non permessa: sono presenti una o piu'' particelle in altri documenti di istanza di riesame di tipo fotointerpretazione.";
  public static final String ERRORE_KO_PARAMETRO_CANC_PART_ISTANZA_F2 = "Si è verificato un errore imprevisto durante il reperimento del parametro CANC_PART_ISTANZA_F2";
  public static final String ERRORE_KO_VALORE_TASTO_ELIMINA = "Si è verificato un errore imprevisto durante il reperimento del valore per fare vedere il tasto elimina particelle nell'istanza di riesame";
  public static final String ERRORE_DOC_IST_RIE_PART_POTENZ = "la particella non e'' presente nell''elenco di istanze inseribili extra sistema";
  public static final String ERRORE_FILE_NON_CORRETTO = "il nome del file non e'' stato inserito nella forma corretta. Non ha l''estensione valorizzata.";
  public static final String ERRORE_DOC_ANNULLA_IMPOSSIBILE = "Il documento selezionato non e'' annullabile";
  //fine costanti documenti
  
  
  // inizio Gestore fascicolo
  public static final String ERRORE_KO_ANAGRAFICA_AZIENDA = "Si è verificato un errore imprevisto durante il reperimento dei dati dell''azienda agricola";
  public static final String ERRORE_KO_DELEGA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla delega dell'azienda agricola";
  public static final String ERRORE_KO_TIPO_SIAN_OPR = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai tipi OPR";
  public static final String ERRORE_KO_OPR_RESP_FASCICOLO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all'OPR detentore del fascicolo";
  public static final String ERRORE_KO_DATA_MIN_ANAG_AZIENDA = "Si è verificato un errore imprevisto durante il reperimento della min data inizio validità dell''azienda agricola selezionata";
  public static final String ERRORE_KO_MODIFICA_GESTORE_FASCICOLO = "Si è verificato un errore imprevisto durante la modifica del gestore del fascicolo";
  public static final String ERRORE_KO_DATA_APERTURA_FASCICOLO = "La data di apertura del fascicolo non può essere antecedente alla data di inizio validità dell'azienda";
  public static final String ERRORE_DATA_APERTURA_FASCICOLO_POST_SYSDATE = "La data di apertura del fascicolo non può essere posteriore alla data odierna";
  public static final String ERRORE_KO_DATA_APERTURA_FASCICOLO_MAX_CHIUSURA = "La data di apertura del fascicolo non può essere posteriore alla data di chiusura";
  public static final String ERRORE_KO_DATA_CHIUSURA_FASCICOLO_MIN_APERTURA = "La data di chiusura del fascicolo non può essere antecedente alla data di apertura";
  public static final String ERRORE_DATA_CHIUSURA_FASCICOLO_POST_SYSDATE = "La data di chiusura del fascicolo non può essere posteriore alla data odierna";
  public static final String ERRORE_DATA_INIZIO_MANDATO_POST_SYSDATE = "La data di inizio mandato non può essere posteriore alla data odierna";
  public static final String ERRORE_KO_MAX_DATA_FINE_MANDATO = "Si è verificato un errore imprevisto durante il reperimento della max data fine mandato";
  public static final String ERRORE_DATA_INIZIO_MANDATO_POST_MAX_DATA_FINE_MANDATO = "La data inizio mandato non può essere inferiore della data di fine mandato precedente";
  public static final String ERRORE_KO_UFFICIO_ZONA_INTERMEDIARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all'ufficio zona intermediario";
  public static final String ERRORE_DATA_FINE_MANDATO_POST_SYSDATE = "La data di fine mandato non può essere posteriore alla data odierna";
  public static final String ERRORE_KO_MAX_DATA_INIZIO_MANDATO = "Si è verificato un errore imprevisto durante il reperimento della max data inizio mandato";
  public static final String ERRORE_DATA_FINE_MANDATO_POST_MAX_DATA_INIZIO_MANDATO = "La data fine mandato non può essere inferiore della data di inizio mandato precedente";
  public static final String ERRORE_KO_STORICO_MANDATI = "Si è verificato un errore imprevisto durante il reperimento dello storico dei mandati";
  public static final String ERRORE_KO_CUAA_AZIENDA = "CUAA dell''azienda non valorizzato";
  public static final String ERRORE_KO_RICHIESTA_DATI_FASCICOLO_SIAN = "Si è verificato un errore imprevisto durante il reperimento dei dati dalal fonte dati SIAN";
  public static final String ERRORE_REG_MANDATO_INTERMEDIARIO = "Non e'' possibile registrare il mandato per un''azienda CAA";
  //fine Gestore fascicolo

  // inizio Anagrafica
  public static final String ERRORE_KO_TIPO_CESSAZIONE = "Si è verificato un errore imprevisto durante il reperimento dei motivi cessazione";
  public static final String ERRORE_NO_AZIENDE_CESSATE_FOR_CUAA = "Non sono state reperite aziende agricole cessate relative al CUAA indicato";
  public static final String ERRORE_DATA_CESSAZIONE_NO_MIN = "La data di cessazione deve essere posteriore al ";
  public static final String ERRORE_KO_DATA_MAX_ANAG_AZIENDA = "Si è verificato un errore imprevisto durante il reperimento della max data inizio validità dell'azienda agricola selezionata";
  public static final String ERRORE_KO_AZIENDA_PROVENIENZA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all''azienda agricola di provenienza";
  public static final String ERRORE_KO_AZIENDA_DESTINAZIONE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all''azienda agricola di destinazione";
  public static final String ERRORE_NO_AVAILABLE_MODIFICA_AZIENDA = "Impossibile modificare la forma giuridica dell''azienda in quanto comporterebbe un cambio di cuaa";
  public static final String ERRORE_NO_AZIENDE_COLLEGATE = "Non e' presente a sistema alcun elemento";
  public static final String ERRORE_PIU_VOCI_SELEZIONATE = "Selezionare una sola voce dall''elenco";
  public static final String ERRORE_NO_AZIENDE_SELEZIONATE = "Non è stata selezionata nessuna azienda dall'elenco";
  public static final String ERRORE_NO_AZIENDA = "Non è stata stata trovata nessuna azienda";
  public static final String ERR_COD_DESC_ATECO_OBBLIGATORI = "Il codice e/o la descrizione ATECO sono obbligatori";
  public static final String ERR_COD_ATECO_GIA_PRESENTE = "Il codice selezionato è lo stesso dell''attività prevalente ATECO";
  public static final String ERR_COD_ATECO_SEC_GIA_PRESENTE = "Il codice selezionato è gia presente tra le attività secondarie ATECO";
  public static final String ERRORE_KO_OBBLIGO_CF = "Si è verificato un errore imprevisto durante il reperimento del dato della forma giuridica obbligo cf";
  public static final String ERR_CAP_DOMICILIO_ERRATO = "Il CAP di domicilio e'' errato";
  public static final String ERR_CAP_SEDE_ERRATO = "Il CAP relativo alla sede legale dell''azienda agricola e'' errato";
  public static final String ERR_NUM_CELLULARE = "Il numero di cellulare inserito e'' errato";
  public static final String ERR_KO_SEARCH_ANAG_GREENING = "Si è verificato un errore imprevisto durante la ricerca dei dati del pagamento ecologico relativi al piano di riferimento selezionato";
  public static final String ERR_NO_COD_ATECO_PRINC_E_ATECO_SEC = "In presenza di ateco secondari valorizzare anche l''ateco principale";
  //fine Anagrafica
  
  //inizio Allevamenti
  public static final String ERRORE_KO_PIANO_RIFERIMENTO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al piano di riferimento";
  public static final String ERRORE_KO_PIANO_RIFERIMENTO_RIPRISTINATO = "Si è verificato un errore imprevisto durante la verifica del ripristino del piano di riferimento";
  public static final String ERRORE_KO_ALLEVAMENTI = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi agli allevamenti";
  public static final String ERRORE_NO_UPDATE_ALLEVAMENTO_STORICIZZATO = "Allevamento storicizzato. Impossibile procedere";
  public static final String ERRORE_NO_DELETE_ALLEVAMENTO = "Impossibile eliminare l''allevamento in quanto risulta presente in una dichiarazione di consistenza.Se l''allevamento non risulta più in carico all''azienda procedere con la cessazione dello stesso.";
  public static final String ERRORE_ALLEVAMENTI_SPECIE_NON_VALIDA = "Impossibile modificare l''allevamento selezionato in quanto la specie non è più valida.";
  public static final String ERRORE_ALLEVAMENTI_NO_BDN = "Impossibile modificare l''allevamento selezionato. Tale allevamento, se variato, deve essere importato dalla BDN.";
  public static final String ERR_ALL_COD_FIS_DEN_PROP_OBB = "I campi codice fiscale e denominazione del proprietario devono essere entrambi valorizzati oppure lascati vuoti";
  public static final String ERR_ALL_COD_FIS_DEN_DET_OBB = "I campi codice fiscale, denominazione e data inizio del detentore devono essere entrambi valorizzati oppure lascati vuoti";
  public static final String ERR_ALL_COD_FIS_DET_PROP_OBB = "I dati del detentore e del proprietario devono essere entrambi valorizzati oppure lascati vuoti";
  public static final String ERR_CAMPO_NON_INTERO = "Il valore inserito deve essere un numero intero.";
  public static final String ERRORE_KO_PARAMETRO_DETT_ALLEVAMENTI = "Si è verificato un errore imprevisto durante il reperimento del parametro DETT_ALLEVAMENTI per la gestione della modifica degli allevamenti";
  public static final String ERR_NESSUNA_SELEZIONE_BDN = "Selezionare una voce dall'elenco!";
  public static final String ERR_COD_AZ_ZOOTECNICO_NULL_BDN = "Codice zootecnico non valorizzato: impossibile consultare il Registro stalla";
  public static final String ERRORE_CAMPO_ERRATO_COORD = "Campo errato: formato concesso 00.000000";
  public static final String ERRORE_KO_FILTRO_ALLEVAMENTI = "Si è verificato un errore imprevisto durante il reperimento dei filtri di ricerca degli allevamenti";
  public static final String ERRORE_NO_DET_PROP = "Non e'' stato possibile identificare se l''allevamento e'' in detenzione o in proprieta''. Impossibile proseguire.";
  public static final String ERRORE_NO_DET_PROP_BDN = "Il cuaa inserito non e'' presente ne'' tra i soggetti collegati ne'' in BDN.";
  public static final String ERRORE_TIPO_PROD_COSMAN = "La tipologia produttiva ai fini Cosman indicata non e'' ammessa per l''allevamento";
  public static final String ERRORE_KO_ANOMALIE_ALLEVAMENTO = "Si è verificato un errore imprevisto durante il reperimento delle anomalie relative all'allevamento selezionato";
  public static final String ERR_NESSUN_CONTROLLO_ALLEVAMENTO_TROVATO = "Non sono state rilevate anomalie sull'allevamento selezionato";
  //fine Allevamenti
  
  public static final String ERRORE_GENERIC_CODICE_FISCALE = "Il codice fiscale inserito è errato";
  public static final String ERRORE_GENERIC_DATA = "La data deve essere posteriore al 1900";
  public static final String ERRORE_DATA_MIN_TODAY = "La data deve essere posteriore o uguale alla data corrente";
  public static final String ERR_SERVIZIO_BDN_NON_DISPONIBILE = "Servizio di consultazione BDN momentaneamente non disponibile";
  public static final String ERR_BDN_NO_REGISTRO_STALLA = "Nessun Registro di Stalla presente in BDN per l'allevamento";
  public static final String ERR_SERVIZIO_BDN = "La consultazione del Registro di Stalla della BDN restituisce: ";
  public static final String ERR_SPECIE_OBB = "Selezionare una specie";
  public static final String ERR_CATEGORIA_OBB = "Selezionare una categoria animale";
  public static final String ERR_SOTTO_CATEGORIA_OBB = "Selezionare una sottocategoria animale";
  public static final String ERR_SOTTO_CATEGORIA_UNIVOCA = "Non è possibile inserire più volte la stessa sottocategoria animale";
  public static final String ERR_NO_CONSISTENZA_DELETE = "Selezionare una voce dall''elenco";
  public static final String ERR_CONSISTENZA_DELETE_USED = "Prima di rimuovere una sottocategoria assicurarsi che non sia usata dalle stabulazioni";
  
  
  public static final String ERR_SOCCIDA_SEL_OBB ="Il codice fiscale del proprietario e quello del detentore devono essere entrambi valorizzati in quanto è stata selezionata la soccida";
  public static final String ERR_SOCCIDA_SEL ="Non e'' possibile selezionare la soccida quando il proprietario coincide con il detentore";
  public static final String ERR_SOCCIDA_NON_SEL ="Proprietario e detentore sono differenti: impostare soccida Si oppure soccida No compilando il campo Motivo";
  public static final String ERR_MOTIVO_SOCCIDA_NO ="Il detentore e il proprietario sono diversi: e'' obbligatorio valorizzare il motivo soccida se viene dichiarato che non si e'' in presenza di soccida";
  public static final String ERR_MOTIVO_SOCCIDA_SI ="Il detentore e il proprietario sono uguali: e'' obbligatorio valorizzare il motivo soccida se viene dichiarato che si e'' in presenza di soccida";
  public static final String ERR_MOTIVO_SOCCIDA_NO_NO_INS ="Il detentore e il proprietario sono uguali: non e'' possibile valorizzare il motivo soccida";
  public static final String ERR_MOTIVO_SOCCIDA_SI_NO_INS ="Il detentore e il proprietario sono diversi: non e''  possibile valorizzare il motivo soccida";

  // inizio Fabbricati
  public static final String ERRORE_KO_FABBRICATI = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai fabbricati";
  public static final String ERRORE_KO_PARTICELLE_FABBRICATO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle particelle su cui insiste il fabbricato";
  public static final String ERR_DIAMETRO_FABBRICATO_OBBLIGATORIO = "Il diametro e'' obbligatorio";
  public static final String ERR_DIAMETRO_FABBRICATO_ERRATO = "Il diametro inserito e'' errato";
  public static final String ERR_POTENZA_FABBRICATO_ERRATO = "la potenza inserita e'' errata";
  public static final String ERR_VOLUME_FABBRICATO_ERRATO = "Il volume inserito e'' errato";
  public static final String ERR_ALTEZZA_FABBRICATO_OBBLIGATORIA = "L''altezza e'' obbligatoria";
  public static final String ERR_ALTEZZA_FABBRICATO_ERRATA = "L''altezza inserita e'' errata";
  public static final String ERR_VOLUME_UTILE_PRESUNTO_OBBLIGATORIO = "Il volume utile presunto e'' obbligatorio";
  public static final String ERR_VOLUME_UTILE_PRESUNTO_ERRATO= "Il volume utile presunto e'' errato";
  public static final String SUP_SCOPERTA_EXTRA_FABBRICATO= "Le altre superfici scoperte impermeabilizzate che convogliano acque di pioggia nella struttura sono errate";
  public static final String ERR_VOLUME_UTILE_PRESUNTO_MAGGIORE_DIMENSIONE = "Il volume utile presunto non può essere maggiore della dimensione";
  public static final String ERR_SUPERFICIE_COPERTA_ERRATA = "La superficie coperta inserita e'' errata";
  public static final String ERR_SUPERFICIE_SCOPERTA_ERRATA = "La superficie scoperta inserita e'' errata";
  public static final String ERR_SUPERFICE_COPERTA_MAGGIORE_SUPERFICIE = "La superficie coperta non può essere maggiore della superficie";
  public static final String ERR_SUPERFICE_SCOPERTA_MAGGIORE_SUPERFICIE = "La superficie scoperta non puo'' essere maggiore della superficie";
  public static final String ERR_SUPERFICE_SCOandCOP_MAGGIORE_SUPERFICIE = "La somma della superficie coperta e scoperta non può essere maggiore della superficie";
  public static final String SUP_SCOP_COP_OBBLIGATORIA_STOCCAGGIO= "La struttura e'' di stoccaggio, almeno una tra le superfici scoperta e coperta deve essere valorizzata";
  public static final String ERR_POTENZA_FABBRICATO_OBBLIGATORIA = "La potenza e'' obbligatoria";
  public static final String ERR_VOLUME_FABBRICATO_OBBLIGATORIA = "Il volume e'' obbligatorio";
  //fine Fabbricati
  
  // inizio Validazioni
  public static final String ERR_NO_CONFERMA_RIPRISTINO_DICHIARAZIONE = "Per proseguire è obbligatorio prendere visione delle informazioni riportate";
  public static final String ERR_KO_PLSQL_PACK_RIPRISTINA_DICHIARAZIONE = "Errore nel ripristino della dichiarazione di consistenza.Contattare l'assistenza tecnica comunicando il seguente errore: ";
  public static final String ERR_GENERIC_KO_PLSQL_PACK_RIPRISTINA_DICHIARAZIONE = "Errore nel ripristino della dichiarazione di consistenza.Contattare l'assistenza tecnica comunicando il seguente errore: TIMEOUT PROCEDURE";
  public static final String ERR_KO_TIPO_MOTIVO_DICHIARAZIONE = "Si è verificato un errore imprevisto durante il reperimento dei tipi motivo dichiarazione";
  public static final String ERR_KO_NEW_DICHIARAZIONE_CONSISTENZA_FOR_CUAA = "Impossibile procedere con la dichiarazione di consistenza in quanto l'azienda selezionata è priva di CUAA";
  public static final String ERR_NEW_DICHIARAZIONE_CONSISTENZA_KO_FOR_COMUNE = "Non è possibile procedere con la dichiarazione di consistenza in quanto non è stato possibile reperire la sigla dell''amministrazione dell''utente collegato";
  public static final String ERRORE_KO_PROTOCOLLA_DICHIARAZIONI_CONSISTENZA = "Si è verificato un errore imprevisto durante la protocollazione delle validazioni selezionate";
  public static final String ERRORE_NO_ANOMALIE_FOR_PARAMETERS = "Non sono state reperite anomalie che soddisfino i criteri di ricerca indicati";
  public static final String ERRORE_TIPO_GRUPPO_CONTROLLO = "Si è verificato un errore imprevisto durante il reperimento del gruppo controllo";
  public static final String ERRORE_TIPO_CONTROLLO = "Si è verificato un errore imprevisto durante il reperimento del tipo controllo";
  public static final String ERRORE_KO_PRATICHE = "Si è verificato un errore imprevisto durante il reperimento delle pratiche relative alla dichiarazione di consistenza";
  public static final String ERRORE_KO_LIST_AMM_COMPETENZA = "Si è verificato un errore imprevisto durante il reperimento delle amministrazioni di competenza";
  public static final String ERRORE_KO_PARAMETRO_ALLD = "Si è verificato un errore imprevisto durante il reperimento del parametro ALLD";
  public static final String ERRORE_KO_FASCICOLI_NAZIONALE_BACKUP = "Si è verificato un errore imprevisto durante il reperimento dei fascicoli nazionali backup legati alla validazione selezionata";
  public static final String ERRORE_KO_DICHIARAZIONE_CONSISTENZA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla dichiarazione di consistenza";
  public static final String ERRORE_RIPRISTINO_DICH_ANT_2007 = "Impossibile ripristinare una dichiarazione antecedente al 2007";
  public static final String ERRORE_RIPRISTINO_DICH = "Impossibile procedere. E' possibile ripristinare l'ultima validazione di ogni anno legata ad una pratica \"Regime di Pagamento Unico\" / \"Piano Sviluppo Rurale\"  oppure una validazione non legata ad alcuna pratica e che la stessa non sia l'ultima dell'anno in corso.";
  public static final String ERRORE_VALIDAZ_NO_PROTOCOL = "Operazione non permessa: la validazione deve essere protocollata.";
  public static final String ERRORE_VALIDAZ_NO_FLAG_SCHEDULA = "Operazione non permessa: il motivo della validazione non rientra tra quelli da inviare a Sian";
  public static final String ERRORE_ESTRAZ_DATI_DICH_CONS = "Si e' verificato un errore imprevisto nell'estrazione dati relativi alla validazione";
  public static final String ERRORE_ESTRAZ_LAST_SCHED = "Si e' verificato un errore imprevisto nell'estrazione dati relativi all'ultima schedulazione della dichiarazione di consistenza";
  public static final String ERRORE_ESTRAZ_AZIENDA_LAST_SCHED = "Si e' verificato un errore imprevisto nell'estrazione dati relativi all'ultima schedulazione delle dichiarazioni di consistenza dell'azienda";
  public static final String ERRORE_INSERT_SCHED = "Si e' verificato un errore imprevisto nell'inserimento della schedulazione";
  public static final String ERRORE_DELETE_SCHED = "Si e' verificato un errore imprevisto nella cancellazione della schedulazione";
  public static final String ERRORE_VALIDAZ_GIA_SCHEDULATO = "Operazione non permessa: esiste una validazione in corso di aggiornamento a Sian";
  public static final String ERRORE_KO_INSERISCI_VALIDAZIONE = "Si è verificato un errore imprevisto durante l'inserimento della validazione";
  public static final String ERRORE_VALIDAZ_GIA_SCHEDULATO_ALTRA_VALID = "Operazione non permessa: esiste una validazione diversa da quella corrente in corso di aggiornamento a Sian";
  public static final String ERRORE_DELETE_SCHED_CONFERMA = "Operazione non permessa: la schedulazione e' in uno stato non modificabile o non esiste nessuna schedulazione.";
  public static final String ERRORE_KO_PARAMETRO_MITT_MAIL_PER_DICH = "Si è verificato un errore imprevisto durante il reperimento del parametro MITT_MAIL_PER_DICH per la generazione della mail nella validazione";
  public static final String ERRORE_KO_PARAMETRO_OGG_MAIL_PER_DICH = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_MAIL_PER_DICH per la generazione della mail nella validazione";
  public static final String ERRORE_KO_PARAMETRO_MAIL_PER_DICH = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_PER_DICH per la generazione della mail nella validazione";
  public static final String ERRORE_KO_PARAMETRO_ABILITA_INVIO_MAIL = "Si è verificato un errore imprevisto durante il reperimento del parametro ABILITA_INVIO_MAIL per la generazione della mail nella validazione";
  public static final String ERRORE_KO_INVIO_MAIL = "La validazione ha avuto esito positivo, ma non è stato possibile inviare la mail per il seguente motivo: ";
  public static final String ERR_ISTANZA_ESAME_ATTIVA = "Non e'' possibile utilizzare il ripristino di validazioni di consistenza in presenza di istanze di riesame attive. Occorre attendere la chiusura dell''istanza o procedere con l''annullamento";
  public static final String ERRORE_KO_PL_PRACARICAMENTO_PG = "Si è verificato un errore imprevisto durante la chiamata al PLSQL precaricamento piano grafico: ";
  //fine Validazioni
  
  // inizio Attestazioni
  public static final String ERR_KO_SEARCH_ATTESTAZIONI = "Si è verificato un errore imprevisto durante la ricerca delle attestazioni relative al piano di riferimento selezionato";
  public static final String ERR_NO_ATTESTAZIONI_FOUND = "Non sono state trovate attestazioni relative al piano di riferimento selezionato";
  public static final String ERR_KO_ATTESTAZIONE_DICHIARATA = "Si è verificato un errore imprevisto durante la verifica della presenza di attestazioni al piano di riferimento selezionato";
  public static final String ERR_KO_AGGIORNA_ATTESTAZIONI = "Si è verificato un errore imprevisto durante l'aggiornamento delle dichiarazioni selezionate";
  public static final String ERR_KO_SEARCH_TIPO_PARAMETRI_ATTESTAZIONI = "Si è verificato un errore imprevisto durante il reperimento dei tipi parametro attestazioni ";
  //fine Attestazioni
  
  //inizio Allegati
  public static final String ERR_KO_SEARCH_ALLEGATI = "Si è verificato un errore imprevisto durante la ricerca degli allegati relativi al piano di riferimento selezionato";
  public static final String ERR_NO_ALLEGATI_FOUND = "Non sono stati trovati allegati relativi al piano di riferimento selezionato";
  public static final String ERR_KO_AGGIORNA_ALLEGATI = "Si è verificato un errore imprevisto durante l'aggiornamento degli allegati associati alla dichiarazione selezionata";
  public static final String ERR_KO_ALLEGATI_PRESENTI_IN_DICHIARAZIONI_ALERT = "La comunicazione è già presente nella sezione Dichiarazioni. Impossibile procedere con la creazione dell''allegato";
  public static final String ERRORE_KO_PARAMETRO_DT_FINE_CONDIZ = "Si è verificato un errore imprevisto durante il reperimento del parametro DT_FINE_CONDIZ per la gestione del dettaglio degli allegati";
  //fine Allegati
  
  //inizio Notifiche
  public static final String ERRORE_KO_DATI_PROFILO_NOTIFICA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all''utente che ha inserito la notifica selezionata";
  public static final String ERRORE_KO_DATI_PROFILO_LOGGATO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all''utente loggato";
  public static final String ERRORE_KO_CHIUSURA_NOTIFICA_FOR_ABILITAZIONE = "Impossibile procedere con la chiusura della notifica in quanto risulta inserita da un altro Ente";
  public static final String ERRORE_KO_CATEGORIA_NOTIFICA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai tipi delle categorie delle notifiche";
  public static final String ERRORE_KO_TIPOLOGIA_NOTIFICA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai tipi delle tipologie delle notifiche";
  public static final String ERR_TROPPE_NOTIFICHE = "Troppi risultati, utilizzare i filtri al fine di restringere il numero di risultati";
  public static final String ERRORE_KO_PARAMETRO_NOTIFI_RMAX = "Si è verificato un errore imprevisto durante il reperimento del parametro NOTIFI_RMAX nella ricerca delle notifiche";
  public static final String ERR_CATEGORIA_NOTIFICA_OBBLIGATORIO = "la categoria della notifica e'' obbligatorio.";
  public static final String ERRORE_UV_NOTIFICA_GIA_INSERITO = "Attenzione: una o più unita'' vitate selezionate erano già presenti in elenco e non sono state aggiunte nuovamente";
  public static final String ERRORE_KO_SEARCH_NO_UV_FOUND = "Non sono state trovate unita' vitate che soddisfino i criteri di ricerca impostati";
  public static final String ERRORE_KO_SEARCH_UV = "Si è verificato un errore imprevisto durante la ricerca delle unita' vitate relative ai filtri di ricerca impostati";
  public static final String ERRORE_UV_NOTIFICA_ELIMA_KO = "Selezionare una unita'' vitata.";
  public static final String ERRORE_PARTICELLE_NOTIFICA_ELIM_KO = "Selezionare una particella.";
  public static final String ERR_AZIENDA_BLOCCOPROCEDIMENTO = "Sono presenti notifiche di blocco dei procedimenti";
  public static final String ERR_AZIENDA_VARIAZIONECATASTALE = "Sono presenti notifiche di variazione catastale";
  public static final String ERR_NO_UV_NOTIFICA = "Per questo tipo di notifica deve essere inserita almeno una unita'' vitata.";
  public static final String ERR_NO_PARTICELLE_NOTIFICA = "Per questo tipo di notifica deve essere inserita almeno una particella.";
  public static final String ERRORE_KO_CHIUSURA_NOTIFICA_RUOLO = "Impossibile procedere: il ruolo dell''utente connesso a sistema non risulta tra i ruoli che possono chiudere questa categoria di notifica.";
  public static final String ERRORE_KO_PARAMETRO_INVIO_MAIL_NOTIF = "Si è verificato un errore imprevisto durante il reperimento del parametro INVIO_MAIL_NOTIF per la generazione della mail nella notifica.";
  public static final String ERRORE_KO_PARAMETRO_MITT_MAIL_NOTIF = "Si è verificato un errore imprevisto durante il reperimento del parametro MITT_MAIL_NOTIF per la generazione della mail nella notifica.";
  public static final String ERRORE_KO_PARAMETRO_MITT_MAIL_NOTIF_M = "Si è verificato un errore imprevisto durante il reperimento del parametro MITT_MAIL_NOTIF_M per la generazione della mail nella notifica.";
  public static final String ERRORE_KO_PARAMETRO_OGG_MAIL_NOTIF = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_MAIL_NOTIF per la generazione della mail nella notifica.";
  public static final String ERRORE_KO_PARAMETRO_OGG_MAIL_NOTIF_M = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_MAIL_NOTIF_M per la generazione della mail nella notifica.";
  public static final String ERRORE_KO_PARAMETRO_MAIL_PER_NOTIF = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_PER_NOTIF per la generazione della mail nella notifica.";
  public static final String ERRORE_KO_PARAMETRO_MAIL_PER_NOTIF_M = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_PER_NOTIF_M per la generazione della mail nella notifica.";
  public static final String ERRORE_NO_MOD_NOTIF = "Impossibile procedere: il ruolo dell''utente connesso a sistema non risulta tra i ruoli che possono modificare questa categoria di notifica.";
  public static final String ERRORE_NO_MOD_NOTIF_CHIUSA = "Impossibile procedere: la notifica e'' stata chiusa.";
  public static final String ERRORE_KO_INVIO_MAIL_NOTIFICA = "La notifica è stata inserita, ma non è stato possibile inviare la mail per il seguente motivo: ";
  public static final String ERR_MAX_4000_CARATTERI = "Campo troppo lungo. Massimo 4000 caratteri";
  public static final String ERR_NO_ENTITA_NOTIFICA_CHIUSURA = "Impossibile procedere: selezionare almeno una voce in elenco";
  public static final String ERR_NO_TUTTE_ENTITA_NOTIFICA_CHIUSURA = "Il motivo di chiusura della notifica deve essere indicata solo se viene chiusa l''intera notifica";
  public static final String ERRORE_PARTICELLA_NOTIFICA_GIA_INSERITO = "Attenzione: una o più particelle selezionate erano già presenti in elenco e non sono state aggiunte nuovamente";
  public static final String ERRORE_KO_RICERCA_NOTIFICHE_PARTICELLA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle notifiche delle particelle";
  public static final String ERRORE_KO_RICERCA_NOTIFICHE_UV = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle notifiche delle unità vitate";
  //fine Notifiche 

  //inizio Ute
  public static String CESSAZIONE_UTE_PARTICELLE = "Non è possibile cessare l''unità produttiva in quanto sono presenti terreni ad essa legati";
  public static String CESSAZIONE_UTE_FABBRICATI = "Non è possibile cessare l''unità produttiva in quanto sono presenti fabbricati ad essa legati";
  public static String CESSAZIONE_UTE_ALLEVAMENTI = "Non è possibile cessare l''unità produttiva in quanto sono presenti allevamenti ad essa legati";
  public static String DELETE_UTE_COMUNICAZIONE_10R = "Non è possibile eliminare l''unità produttiva in quanto sono presenti comunicazioni 10r ad essa legati";
  public static String PROBLEMI_GESTIONE_DELEGA = "Si è verificato un errore nella gestione della delega";
  //fine Ute
  
  public static final String ERRORE_500 = "Si è verificato un'errore imprevisto. Contattare l'assistenza comunicando il seguente errore: ";
  public static final String ERRORE_404 = "Pagina non trovata";

  public static final String ERR_PLSQL_PCK_CARICA_MISURA_H = "Errore nel ribaltamento della consistenza della misura H. Contattare l''assistenza comunicando il seguente messaggio:";

  //inizio Costanti Gestione Utiliti
  public static final String ERR_CAA_IDENTICI = "Selezionare un diverso ufficio per la destinazione";
  public static final String ERRORE_KO_SEARCH_AZIENDE_GU = "Si è verificato un errore imprevisto durante la ricerca delle Aziende relative ai filtri di ricerca impostati";
  public static final String ERRORE_KO_SEARCH_NO_AZIENDE_FOUND_GU = "Non sono state trovate Aziende che soddisfino i criteri di ricerca impostati";
  public static final String ERRORE_KO_PARAMETRO_RTRA = "Si è verificato un errore imprevisto durante il reperimento del parametro RTRA per la gestione del trasferimento ufficio";
  public static final String ERR_CUAA_ERRATO_GU = "Campo formalmente errato";
  //fine Costanti Gestione Utiliti
  
  //inizio Costanti aziende collegate
  public static final String ERRORE_KO_PARAMETRO_RAZA = "Si è verificato un errore imprevisto durante il reperimento del parametro RAZA nella modifica dell'elenco soci";
  public static final String ERRORE_TROPPE_VOCI_SELEZIONATE = "E' possibile selezionare un massimo di 25 elementi dall'elenco";
  public static final String ERRORE_MODIFICA_AZIENDE_STORICIZZATE = "Impossibile modificare un elemento storicizzato";
  public static final String ERRORE_NOAZIENDE_TROVATE = "Nessun record trovato coi filtri impostati";
  public static final String ERRORE_TROPPEAZIENDE_TROVATE = "Troppi record selezionati - Impostare un filtro di ricerca piu' restrittivo";
  public static final String ERRORE_AZIENDA_GIAPRESENTE = "Soggetto già presente nell''elenco";
  public static final String ERRORE_TROPPE_VOCI_INSERITE = "E' possibile inserire un massimo di 25 elementi";
  public static final String ERRORE_ESISTONOANTENATI = "Soggetto già inserito ad un livello superiore a quello in esame";
  public static final String ERRORE_ESISTONODISCENDENTI = "Soggetto già inserito ad un livello superiore a quello in esame e presente a un livello inferiore in quello che si vuole aggiungere";
  public static final String ERRORE_DATA_INGRESSO_POST_DATA_USCITA = "La data ingresso deve essere antecedente alla data uscita";
  public static final String ERRORE_FROMATO_DATA = "La data non è stata inserita nel formato corretto (gg/mm/aaaa) ";
  public static final String ERRORE_COLLEGATE_AZIENDE_STORICIZZATE = "Selezionare un elemento non storicizzato";
  public static final String ERRORE_NO_AZIENDE_INSERITE = "Inserire almeno un soggetto";
  public static final String ERRORE_PIU_VOCI_SELEZIONATE_AC = "Selezionare un solo elemento dall'elenco";
  public static final String ERRORE_NO_AZIENDE_PADRE = "Non esitono aziende padre associate all'azienda su cui si stà operando ";
  public static final String ERRORE_PIU_VOCI_SELEZIONATE_ET = "Selezionare una sola voce dall'elenco";
  public static final String ERRORE_CAP_NON_COERENTE = "Il cap non è coerente col comune inserito";
  public static final String ERRORE_AZIENDA_NON_CENSITA = "L'azienda non è censita nell'anagrafica aziende agricole";
  public static final String ERRORE_ES_CUAA_ERRATO = "Il codice univoco dell''azienda agricola è formalmente errato";
  public static final String ERRORE_ES_PIVA_ERRATO = "La partita iva dell''azienda agricola è formalmente errata";
  public static final String ERRORE_NO_ELENCO_SOCI_SELEZIONATI = "Non è stato selezionato alcun elemento dall'elenco";
  //fine Costanti aziende collegate

  //inizio Costanti web-service CCIAA
  public static final String ERR_SERVIZIO_CCIAA_NON_DISPONIBILE = "Servizio di consultazione CCIAA momentaneamente non disponibile";
  public static final String ERR_SERVIZIO_CCIAA = "Si è verificato un errore nella consultazione dell'albo vigneto: ";
  public static final String ERR_NO_ALBO_VIGNETI_CCIAA = "NON RISULTA REGISTRATO ALCUN ALBO VIGNETO ALLA CCIAA";
  //fine Costanti web-service CCIAA

  //inizio Costanti Terreni
  public static final String ERRORE_KO_FILTRO_PARTICELLARE = "Si è verificato un errore imprevisto durante il reperimento dei filtri di ricerca delle particelle";
  public static final String ERRORE_FILTRO_PARTICELLARE_ERRATO = "I parametri inseriti per la ricerca delle particelle sono errati";
  public static final String ERRORE_KO_SEARCH_ID_CONDUZIONI = "Si è verificato un errore imprevisto durante la ricerca degli id_conduzione relativi ai filtri di ricerca impostati";
  public static final String ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND = "Non sono state trovate particelle territoriali che soddisfino i criteri di ricerca impostati";
  public static final String ERRORE_KO_SEARCH_PARTICELLE = "Si è verificato un errore imprevisto durante la ricerca delle particelle relative ai filtri di ricerca impostati";
  public static final String ERRORE_KO_ESEGUI_CONTROLLI_PARTICELLE = "Si è verificato un errore imprevisto durante i controlli di validità delle particelle ";
  public static final String ERRORE_OBBLIGO_NUM_PAGINAZIONE_PARTICELLE = "Indicare il numero di pagina che si intende visualizzare";
  public static final String ERRORE_KO_NUM_PAGINAZIONE_PARTICELLE = "Il numero di pagina indicato deve essere un numerico positivo";
  public static final String ERRORE_NUM_PAGINAZIONE_PARTICELLE_SUP_NUM_TOT = "Il numero di pagina indicato deve essere minore del numero totale di pagine trovate";
  public static final String ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER = "Si è verificato un errore imprevisto durante il reperimento dei parametri necessari per visualizzare le anomalie relative alla particella selezionata";
  public static final String ERRORE_KO_ANOMALIE_PARTICELLE = "Si è verificato un errore imprevisto durante il reperimento delle anomalie relative alla particella selezionata";
  public static final String ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER = "Si è verificato un errore imprevisto durante il reperimento dei parametri necessari per visualizzare i documenti relativi alla particella selezionata";
  public static final String ERRORE_KO_DOCUMENTI_PARTICELLE = "Si è verificato un errore imprevisto durante il reperimento dei documenti relativi alla particella selezionata";
  public static final String ERRORE_NO_DOCUMENTI_PARTICELLE = "Non sono stati trovati documenti relativi alla particella selezionata";
  public static final String ERRORE_NO_VOCE_SELEZIONATA_ALERT = "Selezionare una voce dall''elenco";
  public static final String ERRORE_NO_TUTTE_UV_ALLINEA_GIS = "Impossibile procedere! L'allineamento a GIS deve essere fatto per tutte le uv delle singole particelle";
  public static final String ERRORE_KO_DETTAGLIO_PARTICELLA = "Si è verificato un errore imprevisto durante il reperimento dei dati della particella selezionata";
  public static final String ERRORE_KO_CONDUZIONE_PARTICELLE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla conduzione delle particelle selezionate";
  public static final String ERRORE_KO_PIANTE_CONSOCIATE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle piante consociate";
  public static final String ERRORE_KO_UTILIZZI = "Non sono stati trovati usi del suolo associati alla particella selezionata";
  public static final String ERRORE_KO_SEARCH_UTILIZZI = "Si è verificato un errore imprevisto durante il reperimento degli usi del suolo relativi alla particella selezionata";
  public static final String ERRORE_KO_PARTICELLA_CERTIFICATA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla particella certificata";
  public static final String ERRORE_KO_FASI_ISTANZA_RIESAME = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all'istanza di riesame";
  public static final String ERRORE_KO_PARAMETRO_RTER = "Si è verificato un errore imprevisto durante il reperimento del parametro RTER per la gestione della modifica multipla";
  public static final String ERRORE_NO_ELEMENTI_SELEZIONATI = "Selezionare una voce dall''elenco";
  public static final String ERRORE_NO_ELEMENTI_MULTIPLI_SELEZIONABILI = "Selezionare una sola voce dall''elenco";
  public static final String ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1 = "E' possibile selezionare un massimo di ";
  public static final String ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2 = " elementi dall''elenco";
  public static final String ERRORE_KO_MODIFICA_MULTIPLA_FOR_CONDUZIONI_STORICIZZATE = "Impossibile modificare la conduzione storicizzata della particella: ";
  public static final String ERRORE_KO_USO_PRIMARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla destinazione produttiva primaria";
  public static final String ERRORE_KO_DESTINAZIONE_PRIMARIA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla destinazione legata all'uso primario";
  public static final String ERRORE_KO_DETT_USO_PRIMARIA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al dettaglio uso legata all'uso primario";
  public static final String ERRORE_KO_QUALITA_USO_PRIMARIA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla qualita uso legata all'uso primario";
  public static final String ERRORE_KO_VARIETA_PRIMARIA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla varieta legata all'uso primario";
  public static final String ERRORE_KO_DEST_USO_PRIMARIA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla destinazione d'uso dell'uso primario";
  public static final String ERRORE_KO_DETTAGLIO_USO_PRIMARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al dettaglio uso dell'uso primario";
  public static final String ERRORE_KO_QUALITA_USO_PRIMARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla qualita uso dell'uso primario";
  public static final String ERRORE_KO_QUALITA_USO_SECONDARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla qualita uso dell'uso secondario";
  public static final String ERRORE_KO_DEST_USO_SECONDARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla destinazione d'uso dell'uso secondario";
  public static final String ERRORE_KO_DETTAGLIO_USO_SECONDARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al dettaglio d'uso dell'uso secondario";
  public static final String ERRORE_KO_USO_SECONDARIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla destinazione produttiva secondaria";
  public static final String ERRORE_KO_VARIETA_SECONDARIA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla varieta legata all'uso secondario";
  public static final String ERRORE_KO_TIPO_EFA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo EFA";
  public static final String ERRORE_KO_TITOLO_POSSESSO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al titolo di possesso";
  public static final String ERRORE_KO_UNITA_PRODUTTIVA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle U.T.E";
  public static final String ERRORE_KO_DOCUMENTI_AZIENDA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai documenti associati all'azienda";
  public static final String ERRORE_SEARCH_PARTICELLE_FOR_MODIFICA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle particelle da modificare";
  public static final String ERRORE_KO_INDIRIZZO_UTILIZZO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi agli indirizzi degli utilizzi";
  public static final String ERRORE_NO_USO_DISP_FOR_INDIRIZZO = "Non sono state reperite destinazioni produttive disponibili per l''azienda selezionata in funzione dell''indirizzo utilizzo scelto.Verificare che non risultino già associati all''azienda";
  public static final String ERRORE_USO_PRIMARIO_OBBLIGATORIO = "Campo obbligatorio in assenza del codice destinazione produttiva";
  public static final String ERRORE_NO_USO_DISP_FOR_CODICE = "Non sono state reperite destinazioni produttive disponibili relative al codice indicato";
  public static final String ERRORE_CODICE_USO_PRESENTE = "La destinazione produttiva selezionata e'' gia'' presente in elenco";
  public static final String ERRORE_CAMPO_OBBLIGATORIO = "Campo obbligatorio";
  public static final String ERRORE_CAMPO_ERRATO = "Campo errato";
  public static final String ERRORE_CAMPO_NON_VALORIZZABILE = "Campo non valorizzabile";
  public static final String ERRORE_CAMPO_PERCENTUALE_ERRATO = "Il valore indicato deve essere maggiore di 0 e minore o uguale a 100";
  public static final String ERRORE_PERCENTUALE_MODMULT_PART_PROV = "Questa funzionalita'' non puo'' avere luogo per le particelle provvisorie";
  public static final String ERRORE_PERCENTUALE_MODMULT_SUPCATGRAF_ZERO = "La superficie catastale / grafica delle particelle selezionate deve essere maggiore di 0";
  public static final String ERRORE_PERCENTUALE_MODMULT_NO_TUTTI_USI = "E'' necessario selezionare tutti gli usi del suolo a parita'' di particella";
  public static final String ERRORE_PERCENTUALE_MODMULT_SUPUTIL_MAG_PERC = "La percentuale di possesso e'' incongruente con la superficie utilizzata e la superficie di riferimento della particella";
  public static final String ERRORE_PERCENTUALE_MODMULT_SUPUTIL_MAG_PERC_ASS = "La percentuale di possesso e'' incongruente con la superficie condotta e la superficie di riferimento della particella";
  public static final String ERRORE_SUPERO_SUP_UTILIZZATE = "Per questa particella non è possibile indicare un totale di superficie utilizzata superiore a";
  public static final String ERRORE_KO_ASSOCIA_USO = "Si è verificato un errore imprevisto durante l'associazione degli usi selezionati";
  public static final String ERRORE_KO_CAMBIA_TITOLO_POSSESSO = "Si è verificato un errore imprevisto durante il cambio titolo possesso";
  public static final String ERRORE_KO_MAX_DATA_INIZIO_CONDUZIONE = "Si è verificato un errore imprevisto durante il reperimento della data massima di inizio conduzione particelle";
  public static final String ERRORE_KO_FABBRICATO_PARTICELLA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai fabbricati della particella";
  public static final String ERRORE_KO_CESSA_PARTICELLE = "Si è verificato un errore imprevisto durante la cessazione delle particelle";
  public static final String ERRORE_KO_CAMBIA_UTE = "Si è verificato un errore imprevisto durante il cambio U.T.E. delle particelle";
  public static final String ERRORE_KO_CAMBIA_PERCENTUALE_POSSESSO = "Si è verificato un errore imprevisto durante il cambio della percentuale di possesso delle particelle";
  public static final String ERRORE_KO_CAMBIA_PERC_POSS_SUP_UTIL = "Si è verificato un errore imprevisto durante l'allinea percentuale di possesso rispetto alla superficie utilizzata delle particelle";
  public static final String ERRORE_KO_ASSOCIA_DOCUMENTO = "Si è verificato un errore imprevisto durante l'associazione del documento alle particelle";
  public static final String ERRORE_KO_MODIFICA_IRRIGAZIONE = "Si è verificato un errore imprevisto durante la modifica dell'irrigazione";
  public static final String ERRORE_KO_PARAMETRO_RTMO = "Si è verificato un errore imprevisto durante il reperimento del parametro RTMO per la gestione della modifica";
  public static final String ERRORE_KO_CASI_PARTICOLARI = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai casi particolari";
  public static final String ERRORE_KO_TIPO_PERIODO_SEMINA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo periodo semina";
  public static final String ERRORE_KO_TIPO_PRATICA_MANTENIMENTO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla pratica mantenimento";
  public static final String ERRORE_KO_TIPO_PERIODO_SEMINA_SEC = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo periodo semina secondario";
  public static final String ERRORE_KO_ZONA_ALTIMETRICA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla zona altimetrica";
  public static final String ERRORE_KO_AREA_A = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo area A";
  public static final String ERRORE_KO_AREA_B = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo area B";
  public static final String ERRORE_KO_AREA_C = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo area C";
  public static final String ERRORE_KO_AREA_D = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo area D";
  public static final String ERRORE_KO_AREA_M = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi allo stato dei corpi idrici confinanti";
  public static final String ERRORE_KO_TIPO_IRRIGAZIONE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo irrigazione";
  public static final String ERRORE_KO_TIPO_POTENZIALITA_IRRIGUA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo potenzilità irrigua";
  public static final String ERRORE_KO_TIPO_ROTAZIONE_COLTURALE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo rotazione colturale";
  public static final String ERRORE_KO_TIPO_TERRAZZAMENTI = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo terrazzamenti";
  public static final String ERRORE_KO_TIPO_METODO_IRRIGUO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo metodo irriguo";
  public static final String ERRORE_KO_TIPO_SEMINA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al tipo semina";
  public static final String ERRORE_KO_CAUSALE_MOD_PARTICELLA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle causali modifica della particella";
  public static final String ERRORE_KO_TIPO_IMPIANTO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai tipi impianto";
  public static final String ERRORE_KO_UTE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all' U.T.E.";
  public static final String ERRORE_KO_SUP_CONDOTTA_MAGGIORE_SUP_CATASTALE = "La superficie condotta non può essere maggiore della superficie catastale";
  public static final String ERRORE_KO_SUP_UTILIZZATA_SECONDARIA_MAGGIORE_SUP_UTILIZZATA = "La superficie secondaria non può essere maggiore della superficie primaria indicata";
  public static final String ERRORE_CAMPO_NON_VALORIZZABILE_FOR_MODIFICA_TERRITORIALE = "Campo non valorizzabile in quanto non è stato modificato nessun dato territoriale";
  public static final String ERRORE_KO_UTILIZZO_CONSOCIATO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi agli utilizzi consociati";
  public static final String ERRORE_KO_MODIFICA = "Si è verificato un errore imprevisto durante la modifica delle particelle selezionate";
  public static final String ERRORE_DATA_CESSAZIONE_MAX_INIZIO_CONDUZIONE = "La data di cessazione non può essere antecedente alla data inizio conduzione della particella più recente";
  public static final String ERRORE_DATA_CESSAZIONE_POST_SYSDATE = "La data di cessazione non può essere posteriore alla data odierna";
  public static final String ERRORE_DETTAGLIO_PARTICELLA_IN_IMPORT_BY_SIAN = "Si è verificato un errore imprevisto durante la ricerca dei dati della particella per l'importazione dal SIAN";
  public static final String ERRORE_COMUNE_INESISTENTE = "Il comune inserito è inesistente";
  public static final String ERRORE_COMUNE_INESISTENTE_FOR_PARTICELLA = "Il comune inserito è inesistente o catastalmente non attivo";
  public static final String ERRORE_KO_COMUNE = "Si è verificato un errore imprevisto durante la ricerca dei dati del comune";
  public static final String ERRORE_KO_PROVINCIA = "Si è verificato un errore imprevisto durante la ricerca dei dati della provincia";
  public static final String ERRORE_STATO_ESTERO_INESISTENTE = "Lo stato estero inserito è inesistente";
  public static final String ERRORE_STATO_ESTERO_INESISTENTE_FOR_PARTICELLA = "Lo stato estero inserito è inesistente o catastalmente non attivo";
  public static final String ERRORE_KO_STATO_ESTERO = "Si è verificato un errore imprevisto durante la ricerca dei dati dello stato estero";
  public static final String ERRORE_KO_FOGLIO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al foglio";
  public static final String ERRORE_KO_TIPI_EVENTO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai tipi evento";
  public static final String ERRORE_KO_SEZIONE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla sezione";
  public static final String ERRORE_SEZIONE_ERRATA = "La sezione inserita è inesistente";
  public static final String ERRORE_FOGLIO_ERRATO = "Il foglio inserito è inesistente";
  public static final String ERRORE_FILTRI_GENERICI = "Troppe occorrenze restituite: restringere i filtri di ricerca";
  public static final String ERRORE_MOTIVAZIONE_PARTICELLE_OBBLIGATORIE = "E' obbligatorio indicare gli estremi della particella";
  public static final String ERRORE_PARTICELLA_NON_IDENTIFICATA_UNIVOCAMENTE = "Non è stato possibile identificare univocamente la particella.Indicare ulteriori filtri di ricerca";
  public static final String ERRORE_PARTICELLA_GIA_INSERITO = "Attenzione: la particella selezionata risulta già presente in elenco";
  public static final String ERRORE_PARTICELLA_EQUALS_PARTICELLA_EVENTO = "Attenzione: si sta cercando di inserire una particella uguale a quella da cui dovrebbe derivare";
  public static final String ERRORE_KO_INSERIMENTO_PARTICELLA = "Sì è verificato un''errore imprevisto durante l''inserimento della particella";
  public static final String ERRORE_MOTIVAZIONE_PARTICELLE_ERRATA = "Impossibile indicare più di una particella";
  public static final String ERRORE_MOTIVAZIONE_PARTICELLE_ERRATA_NO_PARAMETERS = "Impossibile inserire la particella:indicare almeno un parametro";
  public static final String ERRORE_KO_TOT_SUPERFICI_CONDOTTE = "Sì è verificato un''errore imprevisto durante il reperimento del totale delle superfici condotte";
  public static final String ERRORE_KO_INSERIMENTO_PARTICELLA_FOR_SUPERO_CONDOTTA = "Impossibile proseguire con l''inserimento: per la particella indicata è gia stata dichiarata tutta la superficie condotta disponibile";
  public static final String ERRORE_KO_TOT_SUP_CONDOTTA_MAGGIORE_SUP_CATASTALE = "La superficie condotta non può essere maggiore di ";
  public static final String ERRORE_KO_EVENTO_PARTICELLA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi agli eventi della particella";
  public static final String ERRORE_KO_UNITA_ARBOREE_FIND_TERRENI_PARAMETER = "Si è verificato un errore imprevisto durante il reperimento dei parametri necessari per visualizzare le unità arboree relative alla particella selezionata";
  public static final String ERRORE_NO_UNITA_ARBOREE_FOUND = "Non sono state trovate unità arboree relative alla particella selezionata";
  public static final String ERRORE_NO_UNITA_ARBOREE_AZIENDA_FOUND = "Non sono state trovate unità arboree relative all'azienda selezionata";
  public static final String ERRORE_KO_UNITA_ARBOREE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle unità arboree";
  public static final String ERRORE_KO_UNITA_ARBOREA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all' unità arborea";
  public static final String ERRORE_KO_FILTRO_UNITA_ARBOREA = "Si è verificato un errore imprevisto durante il reperimento dei filtri di ricerca delle unità arboree";
  public static final String ERRORE_KO_ESEGUI_CONTROLLI_UNITA_ARBOREE = "Si è verificato un errore imprevisto durante i controlli di validità delle unità arboree";
  public static final String ERRORE_KO_ANOMALIE_FIND_UNAR_PARAMETER = "Si è verificato un errore imprevisto durante il reperimento dei parametri necessari per visualizzare le anomalie relative all'unità arborea selezionata";
  public static final String ERRORE_KO_ANOMALIE_UNAR = "Si è verificato un errore imprevisto durante il reperimento delle anomalie relative all'unità arborea selezionata";
  public static final String ERRORE_NO_ANOMALIE_UNAR = "Non sono state rilevate anomalie sull'unità arborea selezionata";
  public static final String ERRORE_KO_DETTAGLIO_UNAR = "Si è verificato un errore imprevisto durante il reperimento dei dati dell'unità arborea selezionata";
  public static final String ERRORE_KO_ALTRI_VITIGNI = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi agli altri vitigni associati all'unità arborea selezionata";
  public static final String ERRORE_KO_DOC_RICADUTA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi ai documenti di ricaduta/resa associati all'unità arborea selezionata";
  public static final String ERRORE_KO_DESTINAZIONE_PRODUTTIVA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla destinazione produttiva";
  public static final String ERRORE_KO_VITIGNO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi al vitigno legato alla destinazione produttiva selezionata";
  public static final String ERRORE_KO_FORME_ALLEVAMENTO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle forme di allevamento";
  public static final String ERRORE_KO_CAUSALE_MOD_UNITA_ARBOREA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle causali modifica dell'unità arborea";
  public static final String ERRORE_KO_MODIFICA_UNITA_ARBOREA_STORICIZZATA = "Impossibile modificare l''unità arborea storicizzata della particella: ";
  public static final String ERRORE_KO_MODIFICA_UNITA_ARBOREE_VALIDATE_PA = "Tra le occorrenze selezionate vi sono delle UV validate dalla PA. Tali UV non possono essere modificate";
  public static final String ERRORE_KO_MODIFICA_UNITA_ARBOREE_CONSOLIDATE = "Tra le occorrenze selezionate vi sono delle UV consolidate al GIS. Tali UV non possono essere modificate";
  public static final String ERRORE_KO_ELIMINA_CONDUZIONE_UV_VALIDATE_PA = "Tra le occorrenze selezionate vi sono delle UV associate validate dalla PA. Tali occorrenze non possono essere eliminate";
  public static final String ERRORE_KO_ELIMINA_CONDUZIONE_FABBRICATO_ATTIVO = "Non è possibile procedere con l'eliminazione delle particelle in quanto alcune di queste risultano già associate ad uno o più fabbricati";
  public static final String ERRORE_KO_ELIMINA_CONDUZIONE_DOCUMENTO_ATTIVO = "Non è possibile procedere con l'eliminazione delle particelle in quanto alcune di queste risultano già associate a documenti";
  public static final String ERRORE_KO_SUP_VITATA_MAGGIORE_SUP_GRAFICA_CAT = "La superficie vitata non può essere maggiore della superficie grafica o della superfice catastale";
  public static final String ERRORE_KO_DATA_IMPIANTO_UV_MAGGIORE_DATA_CORRENTE = "La data impianto non può essere posteriore alla data corrente";
  public static final String ERRORE_KO_DATA_SOVRAINNESTO_UV_MAGGIORE_DATA_CORRENTE = "La data sovrainnesto non può essere posteriore alla data corrente";
  public static final String ERRORE_KO_DATA_IMPIANTO_UV_MINORE_1900 = "La data impianto non può essere antecedente al 31/12/1900";
  public static final String ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE = "Non è possibile impostare l''idoneità DOC per l''anno di impianto ";
  public static final String ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE_MSG = "Non è possibile impostare l'idoneità DOC per l'anno di impianto ";
  public static final String ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE_JAVASCRIPT = "Non &egrave; possibile impostare l\\'idoneit&agrave; DOC per l\\'anno di impianto ";
  public static final String ERRORE_KO_DATA_PRIMA_PRODUZIONE_UV_MAGGIORE_DATA_CORRENTE = "La data prima produzione non può essere posteriore alla data corrente";
  public static final String ERRORE_KO_DATA_PRIMA_PRODUZIONE_UV_MINORE_1900 = "La data prima produzione non può essere antecedente al 31/12/1900";
  public static final String ERRORE_KO_DATA_SOVRAINNESTO_UV_MINORE_1900 = "La data sovrainnesto non può essere antecedente al 31/12/1900";
  public static final String ERRORE_KO_DATA_SOVRAINNESTO_UV_MINORE_DT_IMPIANTO = "La data sovrainnesto non può essere antecedente alla data impianto";
  public static final String ERRORE_KO_ANNO_RIFERIMENTO_UV_MAGGIORE_ANNO_CORRENTE = "L''anno riferimento non può essere posteriore all''anno corrente";
  public static final String ERRORE_KO_ANNO_RIFERIMENTO_UV_MINORE_ANNO_IMPIANTO = "L''anno riferimento non può essere antecedente all''anno impianto";
  public static final String ERRORE_KO_PRATICHE_ASSOCIATE_UV = "Impossibile procedere in quanto per la particella sono presenti pratiche di \"Estirpazione e impianto vigneti\"";
  public static final String ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT = "Impossibile procedere in quanto per la particella sono presenti pratiche di Estirpazione e impianto vigneti";
  public static final String ERRORE_KO_PRATICHE_ASSOCIATE_UV_MULTIPLE = "Impossibile procedere in quanto per le particelle sono presenti pratiche di \"Estirpazione e impianto vigneti\"";
  public static final String ERRORE_KO_UV_NON_ELIMINABILI_CAA = "Tra le occorrenze selezionate vi sono delle UV la cui destinazione produttiva non permette la cessazione";
  public static final String ERRORE_KO_MOD_PROC_VITI_UV_MULTIPLE = "Impossibile procedere in quanto sono presenti UV modificate o inserite dal procedimento VITI";
  public static final String ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA = "L'unit&agrave; vitata &egrave; stata modificata dal procedimento VITI";
  public static final String ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA_JAVASCRIPT = "L\\'unit&agrave; vitata &egrave; stata modificata dal procedimento VITI";
  public static final String ERRORE_KO_MOD_PROC_VITI_UV_IMPORTA_CCIAA_JAVASCRIPT = "L\\'unit&agrave; vitata in fascicolo &egrave; stata aggiornata dal procedimento VITI";
  public static final String ERRORE_KO_MOD_PROC_VITI_UV_IMPORTA_CCIAA = "L'unit&agrave; vitata in fascicolo &egrave; stata aggiornata dal procedimento VITI";
  public static final String ERRORE_KO_CONSOLIDAMENTO_UV_IMPORTA_CCIAA_JAVASCRIPT = "L\\'unit&agrave; vitata in fascicolo &egrave; stata consolidata a GIS";
  public static final String ERRORE_KO_CONSOLIDAMENTO_UV_IMPORTA_CCIAA = "L'unit&agrave; vitata in fascicolo &egrave; stata consolidata a GIS";
  public static final String ERRORE_KO_MOD_PROC_VITI_UV_ELIMINA = "Impossibile procedere in quanto per la particella sono presenti UV modificate o inserite dal procedimento VITI";
  public static final String ERRORE_KO_MODIFICA_UNITA_ARBOREE = "Si è verificato un errore imprevisto durante la modifica delle unità arboree selezionate";
  public static final String ERRORE_KO_PARAMETRO_RUVM = "Si è verificato un errore imprevisto durante il reperimento del parametro RUVM per la gestione della cessazione delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_ALBO = "Si è verificato un errore imprevisto durante il reperimento del parametro ALBO per la gestione delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_IUVP = "Si è verificato un errore imprevisto durante il reperimento del parametro IUVP per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_BUCV = "Si è verificato un errore imprevisto durante il reperimento del parametro BUCV per la gestione dell'elimina delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_APUV = "Si è verificato un errore imprevisto durante il reperimento del parametro APUV per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_VTUV = "Si è verificato un errore imprevisto durante il reperimento del parametro VTUV per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_UVAG = "Si è verificato un errore imprevisto durante il reperimento del parametro UVAG per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_UV23 = "Si è verificato un errore imprevisto durante il reperimento del parametro UV23 per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_DAID = "Si è verificato un errore imprevisto durante il reperimento del parametro DAID per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_GDEL = "Si è verificato un errore imprevisto durante il reperimento del parametro GDEL per la gestione della revoca delega";
  public static final String ERRORE_KO_PARAMETRO_OTHER_UVP = "Si è verificato un errore imprevisto durante il reperimento del parametro OTHER_UVP per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_LOCK_UV_CONSOLIDATE = "Si è verificato un errore imprevisto durante il reperimento del parametro LOCK_UV_CONSOLIDATE per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_ACCESSO_IDONEITA = "Si è verificato un errore imprevisto durante il reperimento del parametro ACCESSO_IDONEITA per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_ACCESSO_VAR_AREA = "Si è verificato un errore imprevisto durante il reperimento del parametro ACCESSO_VAR_AREA per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_ACCESSO_DT_SOVR = "Si è verificato un errore imprevisto durante il reperimento del parametro ACCESSO_DT_SOVR per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_INSERISCI_NUOVA_UV = "Si è verificato un errore imprevisto durante il reperimento del parametro INSERISCI_NUOVA_UV per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_INSERISCI_NUOVA_PART = "Si è verificato un errore imprevisto durante il reperimento del parametro INSERISCI_NUOVA_PART per la gestione dell'inserimento della particella";
  public static final String ERRORE_KO_PARAMETRO_ACCESSO_ALTRI_DATI = "Si è verificato un errore imprevisto durante il reperimento del parametro ACCESSO_ALTRI_DATI per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_PARAMETRO_ACCESSO_ALLINEA_GIS = "Si è verificato un errore imprevisto durante il reperimento del parametro ACCESSO_ALLINEA_GIS per la gestione della modifica delle unità vitate";
  public static final String ERRORE_KO_GENERE_ISCRIZIONE = "Si è verificato un errore imprevisto durante il reperimento del genere iscrizione di default nella importazione da albo";
  public static final String ERRORE_KO_MODIFICA_MULTIPLA_UNITA_VITATE_COMUNI = "Impossibile modificare unità vitate presenti in comuni diversi";
  public static final String ERRORE_KO_CESSA_UNITA_ARBOREA_STORICIZZATA = "Impossibile cessare l''unità arborea storicizzata della particella: ";
  public static final String ERRORE_SEARCH_PARTICELLE_FOR_CESSAZIONE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle unità arboree da cessare";
  public static final String ERRORE_KO_CESSA_UNITA_ARBOREE = "Si è verificato un errore imprevisto durante la cessazione delle unità arboree";
  public static final String ERRORE_KO_CAUSALE_CESSAZIONE_UNITA_ARBOREA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle causali cessazione dell'unità arborea";
  public static final String ERRORE_KO_SEARCH_UV_IMPORT_BY_SCHEDARIO = "Si è verificato un errore imprevisto durante la ricerca dei dati delle unità arboree importabili da schedario";
  public static final String ERRORE_NO_UV_FOUND_IMPORT_BY_SCHEDARIO = "Non sono state trovate unità vitate importabili da schedario";
  public static final String ERRORE_KO_UV_IMPORT_BY_SCHEDARIO = "Si è verificato un errore imprevisto durante l''importazione delle unità vitate da schedario";
  public static final String ERRORE_PERCENTO_VITIGNO_ALTRI_VITIGNI = "La percentuale vitigno indicata è incongruente con l''informazione di altri vitigni";
  public static final String ERRORE_KO_INSERIMENTO_UV = "Sì è verificato un errore imprevisto durante l''inserimento dell''unità vitata";
  public static final String ERRORE_KO_TIPO_VINO = "Sì è verificato un errore imprevisto durante il reperimento del tipo vino";
  public static final String ERRORE_KO_TIPO_TIPOLOGIA_VINO = "Sì è verificato un errore imprevisto durante il reperimento del tipo tipologia vino";
  public static final String ERRORE_KO_TIPO_GENERE_ISCRIZIONE = "Sì è verificato un errore imprevisto durante il reperimento del tipo genere iscrizione";
  public static final String ERRORE_KO_TIPO_CAUSALE_MODIFICA = "Sì è verificato un errore imprevisto durante il reperimento del tipo causale modifica";
  public static final String ERRORE_KO_TIPO_UTILIZZO = "Sì è verificato un errore imprevisto durante il reperimento del tipo utilizzo";
  public static final String ERRORE_KO_TIPO_VARIETA = "Sì è verificato un errore imprevisto durante il reperimento del tipo varietà";
  public static final String ERRORE_KO_ANNO_ISCRIZIONE_ALBO = "L''anno di iscrizione albo deve essere maggiore del "+SolmrConstants.MIN_ANNO_VALIDITA_ISCRIZIONE_ALBO+" e minore del "+DateUtils.getCurrentYear();
  public static final String ERRORE_ALTRO_VITIGNO_VARIETA_UGUALE_VITIGNO_PRINCIPALE = "La varietà del vitigno deve essere diversa da quella dell''unità vitata principale";
  public static final String ERRORE_VITIGNO_PRINCIPALE_UGUALE_ALTRO_VITIGNO_VARIETA = "La varietà del vitigno dell''unità vitata principale deve essere diversa da quella degli altri vitigni";
  public static final String ERRORE_KO_SUP_DA_ISCRIVERE_ALBO_MAGGIORE_SUP_VITATA = "La superficie da iscrivere albo non può essere maggiore della superficie vitata";
  public static final String ERRORE_PERCENTUALE_ALTRI_VITIGNI_ERRATA = "La somma delle percentuali altro vitigno e quella dell''unità vitata principale non può essere maggiore di 100";
  public static final String ERRORE_PERCENTUALE_ALTRI_VITIGNI_MINORE_100 = "La somma delle percentuali altro vitigno e quella dell''unità vitata principale non può essere minore di 100";
  public static final String ERRORE_PRESENZA_ALTRI_VITIGNI_N = "Campo errato: risultano altri vitigni collegati all''unità vitata";
  public static final String ERRORE_PRESENZA_ALTRI_VITIGNI_S = "Campo errato: non risultano altri vitigni collegati all''unità vitata";
  public static final String ERRORE_ID_UTILIZZO_UV_SU_ALTRI_VIGNETI = "Campo errato: la destinazione produttiva dell''unità vitata non è compatibile con quella degli altri vitigni associati";
  public static final String ERRORE_KO_NUMERO_CEPPI = "Il numero di ceppi calcolato è troppo elevato (max 6 chr)";
  public static final String ERRORE_SUP_AGRONOMICA_MAX_SUP_CONDOTTA = "La superficie agronomica non può essere maggiore della superficie condotta";
  public static final String ERRORE_KO_DICHIARAZIONE_AGRONOMICA = "Si è verificato un errore imprevisto durante la dichiarazione della superficie agronomica";
  public static final String ERRORE_KO_VALIDA_UV = "Selezionare una dichiarazione di consistenza ed effettuare la ricerca";
  public static final String ERRORE_KO_RIEPILOGHI = "Si è verificato un errore imprevisto durante l'elaborazione del riepilogo selezionato";
  public static final String ERRORE_KO_AZIENDE_PARTICELLE_ASSERVITE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle aziende su cui insistono le particelle asservite";
  public static final String ERRORE_KO_AZIENDE_PARTICELLE_CONFERITE = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alle aziende su cui insistono le particelle conferite";
  public static final String ERRORE_UTENTE_PROVINCIALE_NON_AUTORIZZATO = "Tra le occorrenze selezionate vi sono delle UV il cui comune non appartiene alla competenza della ";
  public static final String ERRORE_TROPPI_RECORD_SELEZIONATI = "Troppi record selezionati - Impostare un filtro di ricerca più restrittivo";
  public static final String ERRORE_NO_ELEMENTI_SISTEMA = "Non è presente a sistema alcun elemento";
  public static final String ERRORE_KO_POP_ISTANZA_RIESAME_TERRENI = "Si è verificato un errore imprevisto durante il reperimento dei dati per la visualizzazione della particella selezionata";
  public static final String ERRORE_KO_POP_ISTANZA_RIESAME_UV = "Si è verificato un errore imprevisto durante il reperimento dei dati per la visualizzazione della particella relativa alla uv selezionata";
  public static final String ERRORE_MOTIVAZIONE_PARTICELLA_PROV = "La particella e'' provvisoria per cui la motivazione puo'' essere unicamente nuova particella";
  public static final String ERRORE_KO_COMPENSAZIONE_AZIENDA = "Si è verificato un errore imprevisto durante il reperimento dei dati della compensazione aziendale";
  public static final String ERRORE_KO_MODIFICA_COND_ELEG_UNITA_ARBOREE = "Si è verificato un errore imprevisto durante la modifica nelle unità arboree selezionate della conduzione eleggibile";
  public static final String ERRORE_KO_TIPO_RIEPILOGHI = "Si è verificato un errore imprevisto durante il reperimento dei tipi di riepiloghi";
  public static final String ERRORE_KO_INSERISCI_CON_SCHEDARIO = "Impossibile procedere in quanto per la particella <<PARTICELLA>> sono gia'' presenti unita'' vitate attive in schedario: procedere con l''importazione.";
  public static final String ERRORE_KO_TIPO_ESPORTAZIONE_DATI = "Si è verificato un errore imprevisto durante il reperimento dei tipi di esportazione dati";
  public static final String ERRORE_FRUTTA_GUSCIO_OBBLIGATORIO = "E'' obbligatorio valorizzare i dati specifici dell''impianto di frutta a guscio";
  public static final String ERRORE_FRUTTA_GUSCIO_OBBLIGATORIO_MOD_MUL = "E' obbligatorio valorizzare i dati specifici dell'impianto di frutta a guscio";
  public static final String ERRORE_FRUTTA_GUSCIO_OBBLIGATORIO_MOD_MUL_JSSP = "E'' obbligatorio valorizzare i dati specifici dell''impianto di frutta a guscio";
  public static final String ERRORE_DEST_PROD_CAA = "Destinazione produttiva non utilizzabile dall''utente connesso";
  public static final String ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA = "Non e'' possibile indicare un uso del suolo che non sia un prato permanente: la particella e'' inserita nel registro prati permanenti ed e'' situata in area natura 2000";
  public static final String ERRORE_KO_POP_ISTANZA_LEGENDA_EFA = "Si è verificato un errore imprevisto durante il reperimento dei dati per la visualizzazione della legenda EFA";
  public static final String ERRORE_KO_FILTRO_EFA = "Si è verificato un errore imprevisto durante il reperimento del filtro di ricerca tipo EFA";
  public static final String ERRORE_NON_DICHIARATO_USO = "Funzionalita' non eseguibile su porzioni di particella prive di uso del suolo";
  //fine Costanti Terreni
  
  public static final String DIFF_MANDATO_ATTIVO = "L'azienda ha mandato attivo presso un altro CAA";
  public static final String NO_CONFERIMENTO_FIGLIA = "E' possibile importare particelle in conferimento solo da aziende di cui si e' soci";
  public static final String ABILITAZIONE_UTENTE = "L'utente non è abilitato a proseguire nella sezione";
  public static final String NO_CONDUZIONI = "Può essere che non sia presente alcuna validazione oppure che nella stessa non sono dichiarati terreni";
  public static final String ERRORE_NO_PARTICELLE_SELEZIONATE = "Selezionare almeno una particella";
  public static final String ERRORE_NO_UTE = "Selezionare un''unità produttiva";
  public static final String ERRORE_NO_COMUNE_FOGLIO_PROVINCIA = "Inserire almeno la provincia, il comune e il foglio";
  public static final String ERRORE_NO_PARTICELLE = "Nessuna particella soddisfa i criteri di ricerca impostati.";
  public static final String ERRORE_KO_PARAMETRO_RSAS = "Si è verificato un errore imprevisto durante il reperimento del parametro RSAS nell'importa asservimento terreni";
  public static final String ERR_TIPO_FORMA_FABBRICATO_ERRATA = "La forma è obbligatoria";
  public static final String ERRORE_KO_PERCENTUALE_CONDUZIONE = "La percentuale della conduzione non può essere 0 o maggiore di 100";
  public static final String ERRORE_KO_PERCENTUALE_CONDUZIONE_ELEGG = "La percentuale della conduzione eleggibile non può essere 0 o maggiore di 100";
  public static final String ERRORE_KO_SUPERFICIE_GRAFICA = "La superficie grafica non è maggiore di 0";
  public static final String ERRORE_KO_INSERIMENTO_PARTICELLA_FOR_SUPERO_GRAFICA_CATASTALE = "Impossibile proseguire con l''inserimento: per la particella indicata è gia stata dichiarata tutta la superficie grafica/catastale disponibile";
  public static final String ERRORE_KO_SUP_CONDOTTA_MAGGIORE_SUP_GRAFICA= "La superficie condotta non può essere maggiore della superficie grafica";
  public static final String ERRORE_KO_TOT_SUP_CONDOTTA_MAGGIORE_SUPERFICIE_GRAFICA = "La superficie condotta non può essere maggiore di ";
  public static final String WARNING_CESSAZIONE_TERRENO_CONTENZIOSO_MULTIPLO = "Attenzione! Le particelle selezionate risultano in conduzione ad altre aziende attive. Sei sicuro di voler proseguire?";
  public static final String ERRORE_CESSAZIONE_PARTICELLE_CON_PARTICELLE_GIA_CESSATE = "Non è possibile effettuare questa operazione in quanto alcune particelle risultano essere già cessate";
  public static final String ERRORE_CESSAZIONE_PARTICELLE_CON_PARTICELLA_GIA_CESSATA = "Non è possibile effettuare questa operazione in quanto la particella risulta essere già cessata";
  public static final String ERRORE_SELEZIONARE_ANNO = "Selezionare un anno";
  public static final String ERRORE_ACCESSO_SERVIZIO_AGRISERV = "Errore nell'accesso al servizio ";
  public static final String ERRORE_KO_SUP_COND_ZERO = "La superificie condotta non puo'' essere uguale a 0";
  public static final String ERRORE_KO_SUP_COND_MAX_CAT_GRAF = "La superificie condotta non puo'' essere maggiore della superficie grafica";
  public static final String ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ = "La superificie condotta non puo'' essere minore della superficie utilizzata";
  public static final String ERRORE_KO_SUP_CATASTALE_ZERO = "La superificie catastale non puo'' essere uguale a 0";
  public static final String ERRORE_KO_SUP_CATASTALE_FRAZIONAMENTO = "La superificie catastale non puo'' essere maggiore della medesima della particella frazionata";
  public static final String ERRORE_KO_SUP_CATASTALE_ACCORPAMENTO = "La superificie catastale non puo'' essere maggiore della somma  delle medesime della particelle accorpate";
  public static final String ERRORE_KO_SUM_PERCENTUALE_CONDUZIONE = "La somma delle percentuali delle conduzioni non puo'' essere maggiore di 100";
  public static final String ERRORE_KO_SUM_SUPERFICIE_CONDUZIONE = "La somma delle superifici condotte non puo'' essere maggiore della superficie catastale/grafica o della grafica se il foglio è stabilizzato";
  public static final String ERRORE_PERIODO_SEMINA_ZERO = "Per la coltura indicata non è possibile dichiarare un periodo di semina diverso da annuale";
  public static final String ERRORE_PERIODO_SEMINA_MAGGIORE_ZERO = "Per la coltura indicata deve essere specificato il periodo di semina";
  
  //inizio Cominicazione 10R
  public static final String ERR_CUAA_DEN_OBBLIGATORI = "Il cuaa e la denominazione sono obbligatori";
  public static final String ERRORE_NO_COMUNE_PROVINCIA = "Inserire la provincia e il comune";
  public static final String ERR_VOLUME_SOTTOGRIGLIATO_ERRATO = "Il volume sottogrigliato inserito è errato";
  public static final String ERR_ALTRE_ACQUE_REFLUE_ERRATO = "Il volume di altre acque reflue inserito è errato";
  public static final String ERR_ACQUE_REFLUE_AZIENDA_ERRATO = "Il volume delle acque reflue provenienti da azienda inserito è errato";
  public static final String ERR_QUANTITA_STOC_ERRATO = "Il volume utile disponibile all''azienda è errato";
  public static final String ERR_NO_ELEMENTI = "Nessun elemento selezionato nell''elenco";
  public static final String ERR_TIPO_EFFLUENTE = "Non è possibile dichiarare la cessione di questo affluente in quanto non prodotto";
  public static final String ERR_TIPO_QUANTITA_CESS_ACQU = "Il volume del refluo dell''azienda è minore di quello che si stà cedendo";
  public static final String ERR_NO_VOLUME_PRODOTTO = "Non è stato prodotta nessuna quantità di refluo dall'azienda";
  public static final String ERR_VOLUME_POST_DICH = "Il volume dichiarato inserito è errato";
  public static final String ERR_VOLUME_TRATT = "Il volume trattato inserito è errato";
  public static final String ERR_AZOTO_POST_DICH = "La quantità di azoto dichiarata inserita è errata. Deve essere un numerico intero";
  public static final String ERR_NO_CESS_ACQU = "Non ci sono aziende nell'elenco";
  public static final String ERR_QUANTITA_AZOTO_CESS_ACQU_ERRATO = "Il volume utile dell'azoto è errato";
  public static final String ERR_NO_AZOTO_PRODOTTO = "Non è stato prodotto nessuna quantità di azoto dall'azienda";
  public static final String ERR_NO_COMUNICAZIONE_10R = "Non esiste una comunicazione 10/R. Per creare una nuova comunicazione, scegliere la voce da menu ricalcola.";
  public static final String NOTE_OBBLIGATORIE = "E'' obbligatorio inserire le note in quanto per la ute e'' stato selezionato il trattamento 8.";
  public static final String ERR_VOLUME_REFLUO_ACQUE = "Il volume refluo inserito è errato";
  public static final String ERR_ID_TIPOLOGIA_ACQUE_DOPPIO = "La coppia settore delle acque reflue e unità produttiva deve essere univoca";
  public static final String ERR_RICERCA_UTE = "Si sono verificati problemi nella ricerca dell'ute";
  public static final String ERR_ID_UTE_DOPPIO = "L'unità produttiva deve essere univoca";
  public static final String ERR_QUANTITA_AZOTO_DICHIARATO_CESS_ACQU_ERRATO = "La quantità di azoto dichiarato inserita è errata";
  public static final String ERR_CUAA_DIVERSO_CUAA_SIAN = "Attenzione! Su anagrafe tributaria risulta un altro CUAA:";
  public static final String ERR_PAR_ADESIONE_DEROGA = "Si sono verificati problemi nel recupero del parametro DEROGA";
  public static final String ERR_REFLUO_ADESIONE_DEROGA = "Impossibile selezionare l''adesione alla deroga poiche'' non sono presenti tipologie di refluo ammesse in deroga";
  public static final String ERR_SOMMA_ADESIONE_DEROGA = "Impossibile selezionare l''adesione alla deroga  poiche'' non sono presenti terreni in ZVN";
  public static final String ERR_DIFFERENZA_ADESIONE_DEROGA = "Impossibile selezionare l''adesione alla deroga  poiche'' la capacita'' degli stoccaggi e'' inferiore al minimo previsto";
  public static final String ERR_PAR_MOD_ADESIONE_DEROGA = "Si sono verificati problemi nel recupero del parametro MOD_DEROGA";
  public static final String ERR_SUP_TRATT_CALC = "Il valore del campo non deve superare il valore calcolato";
  public static final String ERR_SUP_TRATT_CALC_SUM = "Il valore del campo non deve superare il valore della somma delle quantita calcolate per gli effluenti non palabili";
  public static final String ERR_CAMPO_VALIDO = "Il valore nel campo inserito non e'' valido";
  public static final String ERR_TRATT_EFF = "Per la stessa ute non può essere inserito due volte un effluente con lo stesso trattamento";
  public static final String ERR_CESS_EFF = "Per la stessa ute non può essere inserito due volte un effluente";
  public static final String ERR_TOT_ACQUE_LAVAGGIO = "Si è verificato un errore nel reperimento delle acque di lavaggio per gli allevamenti collegati";
  //fine Cominicazione 10R
  
  //inizio Unità arboree
  public static final String ERR_MATRICOLA_CCIAA = "Il campo Matrcicola CCIAA non può essere maggiore di 15 caratteri.";
  public static final String ERR_FORMA_ANNO_ISCRIZIONE_ALBO = "Il campo Anno iscrizione albo non è scritto nella forma opportuna.";
  public static final String ERR_ISCRIZIONE_ALBO_ANNI = "Deve essere antecedente o uguale all''anno corrente, posteriore o uguale al 1900 e posteriore o uguale all''anno impianto.";
  public static final String ERR_ISCRIZIONE_ALBI_ANNI = "Deve essere antecedente o uguale all''anno corrente, posteriore o uguale al 1900 e posteriore o uguale all''anno impianto massimo di tutte le unità vitate.";
  public static final String ERR_CAMPO_OBBLIGATORIO_MATRICOLA_VINODOC = "Se il vino e'' DOC il campo Matricola CCIAA e'' obbligatorio.";
  public static final String ERR_CAMPO_OBBLIGATORIO_ANNO_VINODOC = "Se il vino e'' DOC il campo anno idoneita'' e'' obbligatorio.";
  public static final String ERR_CAMPO_OBBLIGATORIO_ANNO_VINODOC_SYSDATE = "Se il vino e'' DOC il campo anno idoneita'' non puo'' essere uguale all''anno corrente";
  public static final String ERR_SUPERFICIE_DA_ISCRIVERE_ALBO_ERRATA = "La superficie iscritta è errata.";
  public static final String ERR_SUPISCRIV_MAGG_SUPVIT_ERRATA = "La superficie iscritta non può essere maggiore della superficie vitata.";
  public static final String ERR_SUPISCRIZ_OBBLIGATORIO = "Se la Tipologia di vino è valorizzata il campo Superficie iscritta è obbligatorio.";
  public static final String ERR_TIPOVINO_OBBLIGATORIO = "Se è stata indicata la Matricola CCIAA o l''Anno iscrizione deve essere selezionata una Tipologia di vino.";
  public static final String ERR_VIGNA = "Il campo Vigna non può essere maggiore di 1000 caratteri.";
  public static final String ERR_VIGNA_NON_INPUT = "Per idoneita'' e particella e'' presente la vigna da elenco regionale.";
  public static final String ERR_ANNOTAZIONE_ETICHETTA = "Il campo annotazione in etichetta non può essere maggiore di 1000 caratteri.";
  public static final String ERR_TIPOLOGIAVINO_NO_VITIGNO = "Deve essere selezionato anche il vitigno";
  public static final String ERR_TIPOLOGIAVINO = "Il vino selezionato non è producibile con tutti i vitigni delle unità vitate in elenco";
  public static final String ERRORE_KO_SUP_VITATA_FUORI_TOLLERANZA = "La superficie vitata non e' in tolleranza rispetto alla superficie della Parcella GIS";
  public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_UVDOPPIE = "L'unità vitata e' ricondotta a due o piu' Parcelle GIS, per procedere e' necessario selezionare la voce di menu' associa 'Parcelle GIS'";
  public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_NO_PARCELLE = "Le ISOLE/Parcelle GIS non sono state calcolate";
  //public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_ERR_PL = "Contattare l'assistenza tecnica:Si e' verificato un errore all'interno della funzione Pck_Smrgaa_Libreria.Uv_In_Tolleranza_Gis";
  public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_NON_PRESENTE = "L'unita' vitata risulta non presente oppure non attiva";
  public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_PARTICELLA_ORFANA = "In seguito al calcolo delle Isole/Parcelle risulta che la particella e' orfana";
  public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_PARCELLE_NO_VITE = "In seguito al calcolo delle Isole/Parcelle risulta che la parcella non e' fotointerpretata a vite";
  public static final String ERRORE_KO_SUP_VITATA_TOLLERANZA_UV_PIU_OCCORR_ATTIVE = "Unita'' vitata duplicata - richiedere bonifica all''assistenza";
  public static final String ERRORE_FLAG_GESTIONE_VIGNA = "Non è possibile inserire il campo vigna in quanto almeno una delle tipologie di vino non lo prevede";
  public static final String ERRORE_FLAG_GESTIONE_IDVIGNA = "Non è possibile inserire il campo vigna in quanto per almeno una delle tipologie di vino e'' presente la vigna nell''elenco provvisorio regionale";
  public static final String ERRORE_FLAG_GESTIONE_ETICHETTA = "Non è possibile inserire il campo annotazione in etichetta in quanto almeno una delle tipologie di vino non lo prevede";
  public static final String ERRORE_NO_SEL_UV = "Deve essere selezionata almeno una unita' vitata";
  public static final String ERRORE_NO_COMPENSAZIONE_AZIENDA = "Per procedere e' necessario esportare i dati relativi alle unita' vitate selezionando lo scarico compensazione aziendale";
  public static final String ERR_ALLIENEA_PIANO_COLTURALE = "Errore nella chiamata del PLQSL PACK_RIBALTA_UV_SU_PCOLTURALE.RIBALTA_UV. ";
  public static final String ERRORE_KO_PART_PERC_COND_ELEGG = "Operazione non permessa: per la stessa particella la percentuale d''uso indicata deve essere uguale per tutte le unita'' vitate";
  public static final String ERRORE_KO_PERCENTUALE_FALLANZA = "La percentuale della fallanza non puo'' essere 0 o maggiore di 100";
  public static final String ERRORE_ANNO_PRIMA_PROD = "L\''anno di prima produzione puo\'' essere valorizzato solo con l\''anno";
  public static final String ERRORE_DATA_SOVR_PRESENTE = "Operazione non permessa: l\''unita\'' vitata ha la data sovrainnesto valorizzata.";
  public static final String ERR_ALLIENEA_PERC_USO = "Errore nella funzione che si occupa di allinare la percentuale uso eleggibile";
  public static final String ERRORE_ALTRO_VITIGNO_PRESENTE = "Impossibile proseguire, almeno una unita\'' vitata ha come altro vitigno lo stesso vino selezionato come vitigno principale";
  //fine Unità arboree
  
  //INIZIO DIRITTI
  public static final String ERR_DIRITTI_IMPIANTO = "Servizio di consultazione diritto impianto viticolo momentaneamente non disponibile"; 
  public static final String ERR_NO_DIRITTI_IMPIANTO = "L'azienda non ha alcun diritto viticolo";
  //FINE DIRITTI
  
  //INIZIO PRATICHE
  public static final String ERR_NO_PAGAMENTI_BENEFICIARI = "Non esistono erogazioni relative all'azienda agricola selezionata.";
  public static final String ERRORE_KO_PARAMETRO_SIGP = "Si è verificato un errore imprevisto durante il reperimento del parametro SIGP nel'erogazione pagamenti.";
  public static final String ERR_NO_PAGAMENTI_BENEFICIARI_FILTRI = "Non esistono erogazioni relative all'azienda agricola selezionata per i parametri di ricerca inseriti.";
  public static final String ERR_NO_REGISTRO_DEBITORI = "Non esiste il registro debitori relativo all'azienda agricola selezionata.";
  public static final String ERR_NO_PREGISTRO_DEBITORI_FILTRI = "Non esiste il registro debitori relativo all'azienda agricola selezionata per i parametri di ricerca inseriti.";
  public static final String ERR_NO_BUONI_CARBURANTE = "Non esistono assegnazioni di carburante relative all'azienda agricola selezionata.";
  public static final String ERR_NO_BUONI_CARBURANTE_FILTRI = "Non esistono assegnazioni di carburante relative all'azienda agricola selezionata per i parametri di ricerca inseriti.";
  public static final String ERR_NO_RECUPERI_PREGRESSI = "Non esistono recuperi pregressi relativi all'azienda agricola selezionata.";
  public static final String ERR_NO_RECUPERI_PREGRESSI_FILTRI = "Non esistono recuperi pregressi relativi all'azienda agricola selezionata per i parametri di ricerca inseriti.";
  //FINE PRATICHE
  
  //INIZIO NUOVA AZIENDA
  public static final String ERR_SIAN_AZIENDA_SUBENTRO_3 = "risulta presente sull'anagrafe tributaria. Vuoi proseguire con l'inserimento? ";
  //FINE NUOVA AZIENDA
  
  //INIZIO UTENTE MONITORAGGIO
  public static String    MONITORAGGIO_OK ="OK";
  public static String    MONITORAGGIO_KO ="KO";
  public static String    MONITORAGGIO_MSG_OK ="L'applicazione risponde correttamente.";
  public static String    MONITORAGGIO_MSG_KO ="L'applicazione non risponde correttamente.";
  public static String    MONITORAGGIO_MSG_KO_DB ="Problemi di accesso alla base dati.  ";
  //FINE UTENTE MONITORAGGIO
  
  //Inizio Importa AAEP
  public static String    ERR_AEEP_NOME_CODICE_FISCALE = "Il nome è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_COGNOME_CODICE_FISCALE = "Il cognome è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_ANNO_NASCITA_CODICE_FISCALE = "L'' anno di nascita è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_MESE_NASCITA_CODICE_FISCALE = "Il mese di nascita è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_GIORNO_NASCITA_CODICE_FISCALE = "Il giorno di nascita è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_COMUNE_NASCITA_CODICE_FISCALE = "Il comune di nascita è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_SESSO_CODICE_FISCALE = "Il sesso è errato in relazione al codice fiscale: ";
  public static String    ERR_AEEP_NO_COD_ATECO_2007_TROVATO = "Il codice Ateco 2007 non puo' essere ricondotto a nessun codice esistente. Contattare l'assistenza.";
  //Fine Importa AAEP
  
  //Inizio Costanti Sian
  public static final String ERR_CODICE_FISCALE_NON_TROVATO = "Non è stato possibile trovare il codice fiscale per le credenziali SIAN";
  public static final String ERR_RESIDENZA_INDIRIZZO_SIAN_NON_VALORIZZATO = "L''indirizzo della residenza non risulta valorizzato in anagrafe tributaria";
  public static final String ERR_RESIDENZA_CAP_SIAN_NON_VALORIZZATO = "Il CAP della residenza non risulta valorizzato in anagrafe tributaria";
  public static final String ERR_DOMICILIO_INDIRIZZO_SIAN_NON_VALORIZZATO = "L''indirizzo del domicilio fiscale non risulta valorizzato in anagrafe tributaria";
  public static final String ERR_DOMICILIO_CAP_SIAN_NON_VALORIZZATO = "Il CAP del domicilio fiscale non risulta valorizzato in anagrafe tributaria";
  public static final String ERR_SEDE_LEGALE_CAP_SIAN_NON_VALORIZZATO = "Il CAP della sede legale non risulta valorizzato in anagrafe tributaria";
  public static final String SIAN_FLAG_NON_PRESENTE_AT_ERRORE = "A";
  public static final String ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE = "Servizio di consultazione SIAN momentaneamente non disponibile";
  public static final String ERRORE_SERVIZIO_SIAN_NO_DATI = "Il servizio di consultazione SIAN non ha restituito nessun dato";
  public static final String ERRORE_GENERIC_CONNECTION_EXCEPTION_SIAN = "Si è verificato un'errore imprevisto durante la connessione al servizio SIAN.Contattare l'assistenza comunicando il seguente errore:";
  public static final String ERRORE_KO_DESCRIZIONE_TIPO_TITOLO_SIAN = "Si è verificato un'errore imprevisto durante la decodifica dei codici tipo titolo provenienti da AGEA-AGRISIAN";
  public static final String ERRORE_KO_DESCRIZIONE_TIPO_ORIGINE_TITOLO_SIAN = "Si è verificato un'errore imprevisto durante la decodifica dei codici origine titolo provenienti da AGEA-AGRISIAN";
  public static final String ERRORE_KO_DESCRIZIONE_TIPO_MOVIMENTO_TITOLO_SIAN = "Si è verificato un'errore imprevisto durante la decodifica dei codici tipo movimento titolo provenienti da AGEA-AGRISIAN";
  public static final String ERRORE_SIAN_NO_TITOLI = "L'AZIENDA NON HA ALCUN TITOLO";
  public static final String ERRORE_CUAA_FOR_SIAN_ERRATO = "Il CUAA indicato è errato";
  public static final String ERRORE_PIVA_FOR_SIAN_ERRATO = "La partita IVA indicata è errata";
  public static final String ERRORE_CUAA_PIVA_FOR_SIAN_ERRATI = "Il CUAA/Partita IVA indicato è errato";
  public static final String ERRORE_CUAA_NOT_FOUND_AT = "Il CUAA indicato non risulta censito presso l'anagrafe tributaria";
  public static final String ERR_ATECO_PRINC_SIAN_NON_VALORIZZATO = "L''ateco 2007 non risulta valorizzato in anagrafe tributaria";
  public static final String ERR_ATECO_SEC_SIAN_NON_VALORIZZATO = "L''ateco secondario non risulta valorizzato in anagrafe tributaria";
  //Fine Costanti Sian
  
  //Inizio Stampe
  public static final String ERRORE_KO_PARAMETRO_SPAL = "Si è verificato un errore imprevisto durante il reperimento del parametro SPAL nelle stampe.";
  //Fine Stampe
  
  //Manodopera
  public static final String ERR_MANODOPERA_NO_SELEZIONATO = "Selezionare la manodopera da visualizzare";
  public static final String ERRORE_KO_DETT_MANODOPERA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi alla manodopera";
  public static final String ERRORE_KO_MAN_FORMA_CONDUZIONE = "Si è verificato un errore imprevisto durante il reperimento dei dati della forma conduzione";
  public static final String ERRORE_KO_FORMA_ISCRIZIONE_INPS = "Si è verificato un errore imprevisto durante il reperimento dei dati del tipo iscrizione INPS";
  public static final String ERRORE_DATA_NULL = "La data puo'' essere inserita solo se valorizzato il campo tipo iscrizione INPS";
  public static final String ERRORE_CODINPS_NULL = "Il numero iscrizione puo'' essere inserita solo se valorizzato il campo tipo iscrizione INPS";
  
  //Inizio Polizze 
  public static final String ERR_POL_ANNO_CAMPAGNA = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all'anno campagna";
  public static final String ERR_POL_INTERVENTO = "Si è verificato un errore imprevisto durante il reperimento dei dati relativi all'intervento";
  //Fine Polizze 
  
  //Inizio Sigmater
  public static final String SIGMATER_CODICE_SERVIZIO_OK = "000";
  public static final String SIGMATER_CODICE_SERVIZIO_KO_GESTITO = "001";
  public static final String SIGMATER_CODICE_SERVIZIO_KO_NON_GESTITO = "002";
  public static final String KO_IMPORT_DIRITTO = "Si è verificato un errore imprevisto durante l'importazione dei diritti sigmater";
  public static final String KO_CODICE_DIRITTO = "Non e' possibile proseguire il codice di diritto restituito da sigmater e' errato. Comunicare il caso all'assistenza";
  public static final String KO_TITOLARITA_PRESENTE = "La titolarita' proposta da Sigmater coincide gia' con quella presente a sistema per la particella";
  public static final String KO_TITOLARITA_CUAA_NON_PRESENTE = "Non e' possibile proseguire: almeno uno dei proprietari ha codice fiscale non valorizzato";
  //Fine Sigmater
  
  //Inizio Isole Parcelle
  public static final String ERRORE_NO_ISOLE_PARCELLE_FOUND = "Non esistono isole e parcelle associate all'azienda";
  public static final String ERRORE_NO_UV_PARCELLE_FOUND = "Non sono presenti a sistema Unità Vitate associate a più Parcelle GIS";
  public static final String ERRORE_NO_ASSOCIA_PARCELLE_FOUND = "Per procedere è necessario selezionare almeno una volta la voce di menù \"associa Parcelle GIS\"";
  //Fine Isole Parcelle
  
  //Inizio Smrgaasrv
  public static final String ERR_SMRGAASRV_TO_CONNECT = "Servizio momentaneamente non disponibile [SMRGAASRV]";
  //Fine Smrgaasrv

  //Inizio revoca mandato
  public static final String ERR_REV_MANDATO_AZIENDA_CESSATA = "Impossibile revocare il mandato in quanto di una azienda cessata";
  public static final String ERR_REV_MANDATO_AZIENDA_STORICIZZATA = "Impossibile revocare la delega su un dato storico";
  public static final String ERR_REV_MANDATO_AZIENDA_DIFF_PROV = "Impossibile revocare la delega di una azienda di un''altra provincia di competenza";
  public static final String ERR_ESTRAZIONE_GEST_FASCICOLO = "Errore nella estrazione dei dati del gestore del fascicolo";
  public static final String ERR_STOR_REVOCA_MAND_TEMP = "Errore nella storicizzazione temporanea del mandato";
  public static final String ERRORE_DATA_RIC_RIT_POST_SYSDATE = "La data ricevuta di ritorno notifica revoca  non può essere posteriore alla data odierna";
  public static final String ERRORE_DATA_RIC_RIT_POST_MAX_DATA_INIZIO_MANDATO = "La data ricevuta di ritorno notifica revoca non può essere inferiore della data di inizio mandato precedente";
  //Fine revoca mandato
  
  //Inizio compensazione
  public static final String ERR_GIA_COMPENSATE = "Impossibile procedere con la compensazione in quanto: le unita' vitate sono gia' state compensate";
  public static final String ERR_NO_SUP_COMPENSABILI = "Impossibile procedere con la compensazione in quanto: su una o piu' unita' vitate sono presenti delle anomalie (vedi scarico compensazione aziendale).";
  public static final String ERR_ISOLE_NO_CALC = "Impossibile procedere con la compensazione in quanto: isole non calcolate";
  public static final String ERR_NO_LAV_ISTANZA = "Impossibile procedere con la compensazione in quanto: l'istanza di riesame non e' stata lavorata";
  public static final String ERR_NO_ISOLA_DOPO_VARIAZIONE = "Impossibile procedere con la compensazione in quanto: generazione isole parcella non avvenuta dopo l'ultima variazione di schedario";
  public static final String ERR_SUP_VIT_DELTA_NEGATIVO = "Impossibile procedere con la compensazione in quanto: la compensazione aziendale ha individuato alcune superfici vitate irregolari sulle quali il delta calcolato e' negativo";
  public static final String ERR_MOD_SUCC_COMPENSZIONE = "Impossibile procedere con la compensazione in quanto: la modifica del piano culturale e' successiva all'esportazione della compensazione aziendale pertanto e' necessario rilanciare l'esportazione";
  public static final String ERR_MOD_FOT_GIS_SUCC_COMPENSZIONE = "Impossibile procedere con la compensazione in quanto: la modifica della fotointerpretazione GIS e' successiva all'esportazione della compensazione aziendale pertanto e' necessario rilanciare l'esportazione";
  public static final String ERR_IMP_ALLIN_COMP = "Impossibile procedere con la compensazione in quanto: il calcolo della compensazione aziendale stessa non permette l'allineamento";
  public static final String ERR_IMP_ALLIN_COMP_MAG_100 = "Impossibile procedere con la compensazione in quanto: la percentuale di possesso relativa alle particelle delle Uv oggetto di compensazione deve essere inferiore o uguale al 100%";
  public static final String ERR_CONS_GIA_EFFETTUATO = "Impossibile procedere con il consolidamento in quanto:le unita' vitate sono gia' state  consolidate";
  //Fine compensazione
  
  //Inizio Registro debitori
  public static final String ERRORE_KO_PARAMETRO_REG_DEB_ARPEA = "Si è verificato un errore imprevisto durante il reperimento del parametro REG_DEB_ARPEA per la gestione del registro debitori ARPEA";
  //Fine Registro debitori
  
  //Nuova iscrizione
  public static final String ERR_CAMPO_OBBLIGATORIO = "Il campo e'' obbligatorio.";
  public static final String ERR_CAMPO_NON_CORRETTO = "Il campo e'' scritto in una forma non corretta.";
  public static final String ERR_PART_NO_CERT = "La particella non è presente a catasto.";
  public static final String ERR_PART_MULT_PERC_POSS = "Esiste gia'' una particella con la stessa chiave catastale e titolo di possesso ma con percentuale di conduzione diversa.";
  public static final String ERRORE_KO_PARAMETRO_UTENTE_RICHIESTA_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro UTENTE_RICHIESTA_AZ per la gestione dell'inserimento nuova richiesta";
  public static final String ERRORE_KO_CUAA_NO_IRIDE = "Non e'' possibile procedere: l''azienda indicata non risulta legata all''utente connesso al sistema";
  public static final String ERR_CAMPO_NON_CORRETTO_INTERO = "Il valore inserito deve essere un numero intero maggiore di zero.";
  public static final String ERRORE_KO_PARAMETRO_INVIO_MAIL_RICH_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro INVIO_MAIL_RICH_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_INVIO_RICH_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro INVIO_RICH_A_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_DEST_MAIL_RICH_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro DEST_MAIL_RICH_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_MAIL_PER_RICH_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_PER_RICH_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_MAIL_RICH_ISC_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_RICH_ISC_A_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_INVIO_MAIL_RICH_AZ = "La richiesta è stata inserita, ma non è stato possibile inviare la mail per il seguente motivo: ";
  public static final String ERRORE_KO_PARAMETRO_MITT_MAIL_RICH_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro MITT_MAIL_RICH_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_REPLY_TO_MAIL = "Si è verificato un errore imprevisto durante il reperimento del parametro REPLY_TO_MAIL per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_OGG_MAIL_PER_RICH_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_MAIL_PER_RICH_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_OGG_RICH_ISC_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_RICH_ISC_A_AZ per la generazione della mail nella richiesta nuova azienda";
  public static final String ERRORE_DUPLICATO_PARTITA_IVA = "Attenzione impossibile proseguire: la partita iva inserita è già presente nei nostri archivi";
  public static final String ERR_PART_NO_FOGLIO = "Per il comune della particella indicata non esiste il foglio, contattare l''assistenza.";
  public static final String ERR_STATO_TIPO_RICHIESTA = "Deve essere selezionato almeno un filtro";
  public static final String ERR_DOC_IDENTITA_OBBLIGATORIO = "E'' obbligatorio inserire il documento di identita''";
  public static final String ERR_DOC_IDENTITA_SPUNTA_OBBLIGATORIO = "E'' obbligatorio inserire l''allegato al documento di identita'' oppure selezionare la dichiarazione di invio via fax del documento";
  public static final String ERR_GENERICO_PART = "Sono presenti anomalie: verificare tutti i dati della particella";
  public static final String ERR_NO_ANNULLA_VALIDATA = "Non e'' possibile annullare una richiesta gia'' presa in carico o annullata dalla Pubblica Amministrazione";
  public static final String ERR_CAMPO_NOTE_MAX = "La lunghezza massima del motivo di annullamento e'' 1000.";
  public static final String ERRORE_KO_PARAMETRO_OGG_ANN_NAP = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_ANN_NAP per la generazione della mail nell'annullamento richiesta nuova azienda";
  public static final String ERRORE_KO_PARAMETRO_TXT_ANN_NAP = "Si è verificato un errore imprevisto durante il reperimento del parametro TXT_ANN_NAP per la generazione della mail nell'annullamento richiesta nuova azienda";
  public static final String ERRORE_NUOVA_AZIENDA_GIA_IN_NUOVA_ISCRIZIONE = "E'' presente una richiesta di nuova iscrizione in Anagrafe in corso per cui non e'' possibile proseguire. Richiedere alla Pubblica Amministrazione verifica di questa richiesta.";
  public static final String ERRORE_KO_PARAMETRO_MAIL_PER_RICH_VAL = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_PER_RICH_VAL per la generazione della mail nella richiesta azienda validazione";
  public static final String ERRORE_KO_PARAMETRO_MAIL_RICH_VAL_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_RICH_VAL_A_AZ per la generazione della mail nella richiesta azienda validazione";
  public static final String ERRORE_KO_PARAMETRO_OGG_MAIL_RICH_VAL = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_MAIL_PER_RICH_VAL per la generazione della mail nella richiesta azienda validazione";
  public static final String ERRORE_KO_PARAMETRO_OGG_RICH_VAL_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_RICH_VAL_A_AZ per la generazione della mail nella richiesta azienda validazione";
  public static final String ERRORE_KO_PARAMETRO_MAIL_PER_RICH_CESS = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_PER_RICH_CESS per la generazione della mail nella richiesta azienda cessazione";
  public static final String ERRORE_KO_PARAMETRO_MAIL_RICH_CESS_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro MAIL_RICH_CESS_A_AZ per la generazione della mail nella richiesta azienda cessazione";
  public static final String ERRORE_KO_PARAMETRO_OGG_MAIL_RICH_CESS = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_MAIL_PER_RICH_CESS per la generazione della mail nella richiesta azienda cessazione";
  public static final String ERRORE_KO_PARAMETRO_OGG_RICH_CESS_A_AZ = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_RICH_CESS_A_AZ per la generazione della mail nella richiesta azienda cessazione";
  public static final String ERR_KO_RICERCA_DATI_RICH_AZ = "Problemi nella ricerca dei dati su richiesta azienda";
  public static final String ERRORE_KO_PARAMETRO_OGG_VAL_RICH_VALID = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_VAL_RICH_VALID per la generazione della mail nella validazione in caso di richiesta azienda in proprio";
  public static final String ERRORE_KO_PARAMETRO_TXT_VAL_RICH_VALID = "Si è verificato un errore imprevisto durante il reperimento del parametro TXT_VAL_RICH_VALID per la generazione della mail nella validazione in caso di richiesta azienda in proprio";
  public static final String ERRORE_KO_PARAMETRO_OGG_VAL_RICH_CESS = "Si è verificato un errore imprevisto durante il reperimento del parametro OGG_VAL_RICH_CESS per la generazione della mail nella cessazione in caso di richiesta azienda in proprio";
  public static final String ERRORE_KO_PARAMETRO_TXT_VAL_RICH_CESS = "Si è verificato un errore imprevisto durante il reperimento del parametro TXT_VAL_RICH_CESS per la generazione della mail nella cessazione in caso di richiesta azienda in proprio";
  public static final String ERRORE_KO_DATA_MAX_DATA_CORRENTE = "La data non puo'' essere posteriore alla data corrente";
  //Messaggistica
  public static final String ERRORE_SERVIZIO_PAPUA_NON_DISPONIBILE = "Servizio di messaggistica momentaneamente non disponibile";  
  //Marcatura Temporale
  public static final String ERRORE_SERVIZIO_MARCATURA_TEMPORALE_NON_DISPONIBILE = "Servizio di marcatura temporale momentaneamente non disponibile";

  //WsBridge
  public static String ERRORE_WSBRIDGE_NON_DISPONIBILE = "Servizio di wsbridge non disponibile";
  public static String ERRORE_ACCESSO_A_WSBRIDGE = "Si è verificato un errore nell'accesso al servizio di wsbridge";
  public static String ERRORE_WSBRIDGE_EXCEPTION = "Rilevata eccezione nella chiamata al servizio di wsbridge";
  
  //AgriWell
  public static String ERRORE_AGRIWELL_NON_DISPONIBILE = "Servizio di agriwell non disponibile";
  public static String ERRORE_ACCESSO_A_AGRIWELL = "Si è verificato un errore nell'accesso al servizio di agriwell";
  public static String ERRORE_AGRIWELL_EXCEPTION = "Rilevata eccezione nella chiamata al servizio di agriwell";
  public static String ERRORE_NOME_FILE_ERRATO = "Il nome del file inserito non e' nella forma corretta";
  public static String ERRORE_NOME_FILE_ERRATO_ESTENSIONE = "Nel nome del file non e' indicata l'estensione";
  public static String ERRORE_KO_PARAMETRO_DATA_STMP_LIVECYCLE = "Si è verificato un errore imprevisto durante il reperimento del parametro DATA_STMP_LIVECYCLE per la protocollazione";
  public static String ERRORE_KO_PARAMETRO_DELAY_NEW_STAMPA = "Si è verificato un errore imprevisto durante il reperimento del parametro DELAY_NEW_STAMPA per la generazione di una nuova stampa della validazione";
  
  //Umaserv
  public static String ERRORE_UMASERV_NON_DISPONIBILE = "Servizio di umaserv non disponibile";
  public static String ERRORE_ACCESSO_A_UMASERV = "Si è verificato un errore nell'accesso al servizio di umaserv";
  public static String ERRORE_UMASERV_EXCEPTION = "Rilevata eccezione nella chiamata al servizio di umaserv";
  
  //Modolsrv
  public static String ERRORE_MODOLSERV_NON_DISPONIBILE = "Servizio di modolserv non disponibile";
  public static String ERRORE_ACCESSO_A_MODOLSERV = "Si è verificato un errore nell'accesso al servizio di modolserv";
  public static String ERRORE_MODOLSERV_EXCEPTION = "Rilevata eccezione nella chiamata al servizio di modolserv";
  

  //macchine agricole
  public static final String ERRORE_KO_PARAMETRO_SCADENZA_IMPO_MACC = "Si è verificato un errore imprevisto durante il reperimento del parametro SCADENZA_IMPO_MACC per la gestione delle macchine agricole";
  public static final String ERRORE_KO_RIC_MACCHINE_AGRICOLE = "Si è verificato un errore imprevisto durante la ricerca delle macchine agricole";
  public static final String ERRORE_KO_GENERE_MACCHINA = "Si è verificato un errore imprevisto durante il reperimento dei dati del tipo genere macchina";
  public static final String ERRORE_KO_IMPORTA_DATI_UMA = "Si è verificato un errore imprevisto durante l'importazione dei dati da uma";
  public static final String ERRORE_NO_MACCHINE_AGRICOLE = "Non e' stata trovata nessuna macchina agricola coi criteri di ricerca impostati";
  public static final String ERRORE_KO_DETTAGLIO_DATI_UMA = "Si è verificato un errore imprevisto durante il prelievo dei dati UMA";
  public static final String ERRORE_KO_INSERISCI_DATI_UMA = "Si è verificato un errore imprevisto durante l'inserimenti dei dati UMA";
  public static final String ERR_CAMPO_OBBLIGATORIO_ANNO_TIPO_MARCA = "E'' obbligatorio valorizzare almeno uno tra anno acquisto o costruzione, marca, modello.";
  public static final String ERR_CAMPO_MACCHINA_PRESENTE_TELAIO = "Esiste gia'' censita su anagrafe una macchina con gli stessi valori di marca, modello, anno e telaio: e'' necessario indicare il n.telaio corretto";
  public static final String ERR_CAMPO_MACCHINA_PRESENTE = "Esiste gia'' censita su anagrafe una macchina con gli stessi valori in marca, modello e anno: e'' necessario dettagliare ulteriormente le informazioni";
  public static final String ERRORE_KO_PERCENTUALE_POSSESSO = "La percentuale del possesso non puo'' essere 0 o maggiore di 100";
  public static final String DELETE_UTE_MACCHINE_AGRICOLE   = "Non e'' possibile eliminare l''unità produttiva in quanto sono presenti delle macchine agricole ad essa legate";
  public static final String CESSAZIONE_UTE_MACCHINE_AGRICOLE = "Non e'' possibile cessare l''unità produttiva in quanto sono presenti delle macchine agricole ad essa legate";
  public static final String ERR_MACCHINA_AGRICOLA_NO_MOD = "la macchina non è modificabile perché importata dal Registro UMA o perché non è in carico all'azienda o perchè l'utente connesso al sistema non è abilitato alla modifica";
  public static final String ERRORE_KO_PARAMETRO_MOD_MACC_LIBERA = "Si è verificato un errore imprevisto durante il reperimento del parametro MOD_MACC_LIBERA per la gestione delle macchine agricole";
  public static final String ERRORE_KO_DATA_CARICO_ANNO = "L''anno della data carico deve essere successivo o uguale all''anno di costruzione";
  public static final String ERRORE_KO_DATA_SCARICO = "La data scarico deve essere successiva alla data di carico";
  public static final String ERRORE_KO_DATA_CARICO = "La data carico deve essere successiva alla data di scarico";
  public static final String ERRORE_KO_DATA_CARICO_SYSDATE = "La data carico non deve essere successiva alla data odierna";
  public static final String ERRORE_KO_MACCH_IMPORTAZIONE = "Impossibile caricare la macchina e'' gia' in carico all''azienda";
}
