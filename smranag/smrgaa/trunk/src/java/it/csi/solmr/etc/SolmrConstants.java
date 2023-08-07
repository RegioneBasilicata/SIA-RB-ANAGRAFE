package it.csi.solmr.etc;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */
import java.util.Calendar;
import it.csi.solmr.util.*;

public abstract class SolmrConstants extends AbstractSolmrEtc 
{
  protected static final String MY_RESOURCE_BUNDLE = "it.csi.solmr.etc.generalString";
  protected static final String MY_RESOURCE_BUNDLE_INT = "it.csi.solmr.etc.generalInt";
  protected static final Class<?> THIS_CLASS = SolmrConstants.class;

  public static String IRIDE_PASS_PHRASE;

  public static String PROFILE_P1; // PA - Utente provinciale abilitato a funzionalità anagrafiche e tutti i procedimenti
  public static String PROFILE_P2; // PA - Utente provinciale abilitato alle sole funzionalità anagrafiche
  public static String PROFILE_P3; // PA - Utente provinciale abilitato al solo procedimento UMA
  public static String PROFILE_P4; // Intermediario - Utente abilitato ad operare per conto di un intermediario
  public static String PROFILE_P5; // Azienda - Utente azienda agricola

  public static String PROF_APA1; // Gestione anagrafica - PA - RW
  public static String PROF_APA2; // Gestione anagrafica - PA - RO
  public static String PROF_APA3; // Gestione anagrafica - PA - Con funzionalità estese
  public static String PROF_ASP1; // Gestione anagrafica - Intermediario - RW
  public static String PROF_ASP2; // Gestione anagrafica - Intermediario - RO

  public static String PROF_UPA1; // Gestione UMA - PA - RW
  public static String PROF_UPA2; // Gestione UMA - PA - RO
  public static String PROF_UPA3; // Gestione UMA - PA - Con funzionalità estese
  public static String PROF_USP1; // Gestione UMA - Intermediario - RW
  public static String PROF_USP2; // Gestione UMA - Intermediario - RO

  public static String PROF_PAWR; // PA - RW su Anagrafica / RO su UMA
  public static String PROF_PARW; // PA - RO su Anagrafica / RW su UMA
  public static String PROF_PAWN; // PA - RW su Anagrafica / No UMA
  public static String PROF_PARN; // PA - RO su Anagrafica / No UMA
  public static String PROF_SPRW; // Intermediario - RO su Anagrafica / RW su UMA
  public static String PROF_SPNW; // Intermediario - No Anagrafica / RW su UMA
  public static String PROF_SPNR; // Intermediario - No Anagrafica / RO su UMA

  public static String APP_NAME_GENERIC;
  public static String APP_NAME_BUSINESS;
  public static String APP_NAME_CLIENT;
  public static String APP_NAME_INTEGRATION;
  public static String APP_NAME_BUSINESS_UMA;
  public static String APP_NAME_CLIENT_UMA;
  public static String APP_NAME_INTEGRATION_UMA;
  public static String APP_NAME_BUSINESS_ANAG;
  public static String APP_NAME_CLIENT_ANAG;
  public static String APP_NAME_INTEGRATION_ANAG;
  public static String APP_NAME_BUSINESS_PROF;
  public static String APP_NAME_CLIENT_PROF;
  public static String APP_NAME_INTEGRATION_PROF;

  public static String FILE_PA_SOLMR;
  public static String FILE_PD_SOLMR;
  public static String FILE_PA_UMA;
  public static String FILE_PD_UMA;
  public static String FILE_PA_ANAG;
  public static String FILE_PD_ANAG;
  public static String FILE_PA_PROF;
  public static String FILE_PD_PROF;
  public static String FILE_CONF_CLIENT;

  public static String I_SOLMR_FACADE;
  public static String I_UMA_FACADE;
  public static String I_ANAG_FACADE;
  public static String I_PROF_FACADE;
  public static String I_STAMPE_FACADE;
  public static String I_SOLMR_CSI;
  public static String I_UMA_CSI;
  public static String I_PROF_CSI;
  public static String I_SOLMR_CLIENT;
  public static String I_UMA_CLIENT;
  public static String I_ANAG_CLIENT;
  public static String I_PROF_CLIENT;

  public static String CONF_EJB;
  public static String CONF_PA;

  public static String JNDI_RESOURCE_REFERENCE;
  public static String JNDI_ANAG_RESOURCE_REFERENCE;
  public static String JNDI_UMA_RESOURCE_REFERENCE;
  public static String JNDI_SLSB_ANAG_FACADE;
  public static String JNDI_SLSB_FACADE;
  public static String JNDI_SLSB_UMA_FACADE;
  public static String JNDI_SLSB_PROF_FACADE;

  public static String ORACLE_DATE_FORMAT;
  public static String ORACLE_FULL_DATE_FORMAT;
  public static String ORACLE_TIME_FORMAT;

  public static String ID_REG_PIEMONTE;

  public static int NUM_MAX_ROWS_ANAG_AZIENDA;
  public static int NUM_MAX_ROWS_PAG;
  public static int NUM_MAX_ROWS_RESULT;

  public static String CD_CODE;
  public static String CD_DESCRIPTION;

  public static String SEQ_NUMERO_TARGA;
  public static String SEQ_NUMERAZIONE_BLOCCO;
  public static String SEQ_COMUNI_TERRENI;
  public static String SEQ_ALLEVAMENTO;
  public static String SEQ_ANAG_AZIENDA;
  public static String SEQ_ASSEGNAZIONE_CARBURANTE;
  public static String SEQ_AZIENDA;
  public static String SEQ_BLOCCO_DITTA;
  public static String SEQ_BUONO_PRELIEVO;
  public static String SEQ_COLTURA_PRATICATA;
  public static String SEQ_CONSUMO_RIMANENZA;
  public static String SEQ_CONTITOLARE;
  public static String SEQ_DITTA_UMA;
  public static String SEQ_DOMANDA_ASSEGNAZIONE;
  public static String SEQ_BUONO_CARBURANTE;
  public static String SEQ_LAVORAZIONI_PRATICATE;
  public static String SEQ_PERSONA_FISICA;
  public static String SEQ_PERSONA_GIURIDICA;
  public static String SEQ_PRELIEVO;
  public static String SEQ_QUANTITA_ASSEGNATA;
  public static String SEQ_SERRA ;
  public static String SEQ_SOGGETTO;
  public static String SEQ_SUPERFICIE_AZIENDA;
  public static String SEQ_UTE ;
  public static final String SEQ_DB_UTE_ATECO_SECONDARI = "SEQ_DB_UTE_ATECO_SECONDARI";
  public static String SEQ_UTENTE ;
  public static String SEQ_NUMERAZIONE_FOGLIO;
  public static String SEQ_FOGLIO_RIGA;

  public static String SEQ_DATI_MACCHINA;
  public static String SEQ_MACCHINA;
  public static String SEQ_MATRICE;
  public static String SEQ_TARGA ;
  public static String SEQ_MOVIMENTI_TARGA;
  public static String SEQ_UTILIZZO;
  public static String SEQ_POSSESSO;
  public static String SEQ_ATTESTATO_PROPRIETA;
  public static String SEQ_ROTTAMAZIONE;
  public static String SEQ_DATI_DITTA;
  public static String SEQ_TARGA_ANNULLATA;
  public static String SEQ_DELEGA ;
  public static String SEQ_SOTTOCATEGORIA_ALLEVAMENTO="SEQ_SOTTOCATEGORIA_ALLEVAMENTO";
  public static String SEQ_STABULAZIONE_TRATTAMENTO="SEQ_STABULAZIONE_TRATTAMENTO";
  public static String SEQ_EFFLUENTE_PRODOTTO="SEQ_EFFLUENTE_PRODOTTO";
  

  
  public static String STATO_DOMANDA_CREATA; //
  public static String ID_STATO_DOMANDA_RESTITUZIONE_COMPLETATA;

  public static String STATO_DITTA_ATTIVA; //ATTIVA

  public static String NUMERO_BLOCCO;
  public static String NUMERO_BUONO;
  public static String CARBURANTE_PER_SERRA;
  public static String DESC_CARBURANTE_PER_SERRA;
  public static String ANNULLATO;
  public static String FLAG_ANNULLATO;

  public static String QTA_CONCESSA_RISC_SERRA;
  public static String QTA_CONCESSA_AGRICOLTURA;

  public static String ID_STATO_DOMANDA_ANNULLATA;
  public static String ID_STATO_DOMANDA_VALIDATA;
  public static String ID_STATO_DOMANDA_RESPINTA;
  public static String ID_STATO_DOMANDA_ATTESA_VAL_PA;
  public static String ID_STATO_DOMANDA_BOZZA;

  public static String DESC_STATO_DOMANDA_ANNULLATA;
  public static String DESC_STATO_DOMANDA_VALIDATA;
  public static String DESC_STATO_DOMANDA_RESPINTA;
  public static String DESC_STATO_DOMANDA_ATTESA_VAL_PA;
  public static String DESC_STATO_DOMANDA_BOZZA;

  public static String ID_TIPO_ASSEGNAZIONE;
  public static String NUMERO_SUPPLEMENTO;
  public static String ID_BENZINA;
  public static String BENZINA;
  public static String ID_GASOLIO;
  public static String GASOLIO;
  public static String SCENARIO_UNO;

  public static String NUMERO_BLOCCO_UNO;
  public static String NUMERO_BUONO_UNO;

  public static String TIPO_CATEGORIA_005;
  public static String TIPO_GENERE_MACCHINA_ASM;

  public static Integer ID_GENERE_MACCHINA_ASM;
  public static Integer ID_GENERE_MACCHINA_R;

  public static String ORDERBY_ANNO_RIFERIMENTO;

  public static String FLAGPRINCIPALE;
  public static String TIPODITTAUMA;

  public static Long IDCONDUZIONECONTOPROPRIO;
  public static Long IDCONDUZIONECONTOPROPRIOETERZI;

  public static int ANNO_REGISTRO_MINIMO;
  public static int ANNO_REGISTRO_MASSIMO;

  public static String TIPORUOLO_TITOL_RAPPR_LEG;
  public static String TIPORUOLO_SOCIO;
  public static Integer TIPO_FORMA_GIURIDICA_INDIVIDUALE;
  public static String ISTAT_STATO_ESTERO;

  public static String CRITERIO_CUAA;
  public static String CRITERIO_PARTITA_IVA;

  public static String CODE_FITTIZIO_UFF_UMA_PROV;
  public static String DESC_FITTIZIO_UFF_UMA_PROV;

  public static String RICERCA_TERRENI_PER_TIPO_CONDUZIONE;
  public static String RICERCA_TERRENI_PER_COMUNE;
  public static String RICERCA_TERRENI_PER_UTILIZZO;

  public static String TAB_PROCEDIMENTO;
  public static String TAB_TIPO_GENERE_MACCHINA;
  public static String TAB_TIPO_ALIMENTAZIONE;
  public static String TAB_TIPO_ATTIVITA;
  public static String TAB_TIPO_ATTIVITA_ATECO;
  public static String TAB_TIPO_ATTIVITA_OTE;
  public static String TAB_TIPO_AZIENDA;
  public static String TAB_TIPO_CARBURANTE;
  public static String TAB_TIPO_CONTRATTO;
  public static String TAB_TIPO_CONDUZIONE;
  public static String TAB_TIPO_FABBRICATO;
  public static String TAB_TIPO_FORMA_GIURIDICA;
  public static String TAB_TIPO_FORMA_POSSESSO;
  public static String TAB_TIPO_IMMATRICOLAZIONE;
  public static String TAB_TIPO_INTERMEDIARIO;
  public static String TAB_TIPO_MARCA;
  public static String TAB_TIPO_NAZIONALITA;
  public static String TAB_TIPO_RUOLO;
  public static String TAB_TIPO_SCADENZA;
  public static String TAB_TIPO_SCARICO;
  public static String TAB_TIPO_STATO_DITTA;
  public static String TAB_TIPO_STATO_DOMANDA;
  public static String TAB_TIPO_TARGA;
  public static String TAB_TIPO_TRAZIONE;
  public static String TAB_TIPO_UTILIZZO;
  public static String TAB_TIPO_LAVORAZIONI;

  public static String MAX_NRIHE_XFOGLIO;
  public static String PRIMO_FOGLIO;
  public static String PRIMA_RIGA;

  public static String TAB_TIPO_TIPOLOGIA_AZIENDA;

  public static String PRIMO_UTILIZZO;
  public static long ID_MACCHINA_RUBATA;
  public static String MACCHINA_RUBATA;
  public static String NON_ULTIMA_TARGA;
  public static String DITTA_NON_APPARTENENTE_ALLA_REGIONE;
  public static String MACCHINA_IN_CARICO_ALTRE_DITTE;
  public static String MACCHINA_NON_DI_PROPRIETA;
  public static String TARGA_NON_TROVATA;
  public static String MATRICE_NON_TROVATA;
  public static String DATI_MACCHINA_NON_TROVATI;
  public static String MACCHINA_NON_TROVATA;
  public static String MACCHINA_IN_CARICO;
  public static String MACCHINA_NON_STATA_IN_CARICO;
  public static String MACCHINA_IN_CARICO_DITTA;

  public static String DESC_CATEGORIA_NON_TROVATA;
  public static String DESC_GENERE_NON_TROVATA;
  public static String TIPO_RIMORCHIO;
  public static String TIPO_ASM;

  public static String STAMPA_TEMPLATE_MODELLO25;
  public static String STAMPA_REPORT_MODELLO25;

  public static String FORMATO_NUMERIC_1INT_4DEC;
  public static String FORMATO_NUMERIC_1INT_2DEC;
  public static String MSG_EXISTS_BUONI_PRELIEVO_DOMASS;

  public static int ANNO_MIN;
  public static int ANNO_MAX = Calendar.getInstance().get(Calendar.YEAR);
  public static String ID_TIPO_PROCEDIMENTO_UMA;
  public static String ID_TIPO_PROCEDIMENTO_ANAG;

  public static String ID_SOGGETTO_NON_COSTITUITO;

  public static String DITTA_LEASING_NON_TROVATA;
  public static String LEASING;
  public static String NOLEGGIO;
  public static String DITTA_LEASING_NON_UNIVOCA;
  public static String GENERE_MACCHINA_MC;
  public static String GENERE_MACCHINA_MF;
  public static String GENERE_MACCHINA_MZ;
  public static String TARGA_UMA;
  public static String TARGA_STRADALE_RA;
  public static String TARGA_STRADALE_MA;
  public static String TARGA_MAO;
  public static String MOVIMENTAZIONE_USATO;
  public static String MOVIMENTAZIONE_USATO_CON_NUOVA_TARGA;
  public static String COD_BREVE_GENERE_MACCHINA_T;
  public static String COD_BREVE_GENERE_MACCHINA_D;
  public static String COD_BREVE_GENERE_MACCHINA_MAO;
  public static String COD_BREVE_GENERE_MACCHINA_MTS;
  public static String COD_BREVE_GENERE_MACCHINA_MTA;
  public static String COD_BREVE_GENERE_MACCHINA_MC;
  public static String COD_BREVE_GENERE_MACCHINA_MF;
  public static String COD_BREVE_GENERE_MACCHINA_MZ;
  public static String COD_BREVE_GENERE_MACCHINA_V;
  public static int COD_TARGA_UMA;
  public static int COD_TARGA_STRADALE_RA;
  public static int COD_TARGA_STRADALE_MA;
  public static int COD_TARGA_MAO;

  public static Object get(String key) {
    return get(THIS_CLASS, key);
  }

  static {
    initClass(THIS_CLASS, MY_RESOURCE_BUNDLE);
    initClassNumeric(THIS_CLASS, MY_RESOURCE_BUNDLE_INT);
  }

  /**
   * Torniamo a fare le costanti con delle costanti  16/08/2006
   * */
  /*LOGGER_STOPWATCH_BEGIN*/
  public static String LOGGER_STOPWATCH = "smrgaa";
  /*LOGGER_STOPWATCH_END*/
  public static String IDENTITA = "identita";
  public static final String FILE_XML_IRIDE2="/it/csi/solmr/etc/iride2Config.xml";
  public static final String FILE_PD_SMRCOMSV="/it/csi/solmr/etc/services/pdComuneService.xml";
  public static final String FILE_PD_SMRCOMSRV_NP ="/it/csi/solmr/etc/services/pdComuneServiceNP.xml";
  public static final String SIGNATURE_NAME_NOT_FOUND_EXCEPTION="javax.naming.NameNotFoundException";
  public static final String SIGNATURE_COMMUNICATION_EXCEPTION="javax.naming.CommunicationException";
  public static final int CODICE_ERRORE_COMUNE_NON_DISPONIBILE= 0x10000001;
  public static final int CODICE_ERRORE_DI_ACCESSO_A_COMUNE= 0x10000002;
  public static final String FILE_DEF_PD_PEP_EJB="/it/csi/solmr/etc/services/defPDPEPEJB.xml";
  public static final String NOME_PORTALE_RUPAR="ruparpiemonte";
  public static final String URL_LOGIN_RUPAR="/auth/INTERNET_RUPAR/autenticazione.shtml";
  public static final String NOME_PORTALE_SISPIE="sistemapiemonte";
  public static final String URL_LOGIN_SISPIE="/_auth/autenticazione.shtml";
  public static final String NOME_PORTALE_RAS="ras";
  public static final String NOME_PORTALE_TOBECONFIG = "applicazioni";
  public static final String PATH_TO_FOLLOW_RUPAR="rupar";
  public static final String PATH_TO_FOLLOW_SISTEMAPIEMONTE ="sispie";
  public static final String PATH_TO_FOLLOW_TOBECONFIG="TOBECONFIG";
  /*AUTENTICAZIONE_RAS_BEGIN*/
  public static final String URL_LOGIN_RAS="/_auth/autenticazione.shtml";
  /*AUTENTICAZIONE_RAS_END*/
  public static final String DOMINIO_DEFAULT_RUPAR = "@IPA";
  public static final String DOMINIO_DEFAULT_SISPIE = "@SISTEMAPIEMONTE";
  public static final String DOMINIO_DEFAULT_RAS = "@SISTEMAPIEMONTE";
  /*IRIDE2_APPLICATION_NAME_BEGIN*/
  public static String APP_NAME_IRIDE2_SMRGAA = "SMRGAA";
  /*IRIDE2_APPLICATION_NAME_END*/
  public static final String PAGINA_SESSIONE_SCADUTA ="/messaggi_error/sessione_scaduta.html";
  public static final String JSP_ERROR_PAGE ="/error/errorPage.jsp";
  public static final String ABACO_CONTEXT = "SITI";
  public static final String TIPO_INTERMEDIARIO_CAA = "C";
  public static final String TIPO_RUOLO_TITOLARE_RAPPRESENTANTE_LEGALE = "1";
  public static final String FORMA_GIURIDICA_INDIVIDUALE = "1";
  public static final String FORMA_GIURIDICA_PERSONA_FISICA_NO_IMPRESA = "52";
  public static final String LABEL_PROV_COMP_PIEMONTE = "Provincia competenza";
  public static final String LABEL_PROV_COMP_SARDEGNA = "Ufficio ripartimentale";
  public static final String CAPOLUOGO_PIEMONTE = "TORINO";
  public static final String CAPOLUOGO_SARDEGNA = "CAGLIARI";
  public static final String ISTAT_CAPOLUOGO_PIEMONTE = "001";
  public static final String ISTAT_CAPOLUOGO_SARDEGNA = "092";
  /*ISTAT_REGIONE_ATTIVA_BEGIN*/
  public static final String ISTAT_REGIONE_ATTIVA = "17";
  /*ISTAT_REGIONE_ATTIVA_END*/


  // INIZIO Costanti nuovo territoriale
  public static final String ORDER_BY_DESC_COMUNE = "C.DESCOM";
  public static final String ORDER_BY_CHAR_COMUNE_DESC = "C.DESCOM DESC";
  public static final String ORDER_BY_DESC_COMUNE_DESC_PIANO_ATTUALE = "DESCOM DESC";
  public static final String ORDER_BY_CHAR_COMUNE_ASC = "C.DESCOM ASC";
  public static final String ORDER_BY_DESC_COMUNE_ASC_PIANO_ATTUALE = "DESCOM ASC";
  public static final String ORDER_BY_DESC_COMUNE_UNAR_ASC = "C.DESCOM ASC";
  public static final String ORDER_BY_DESC_COMUNE_UNAR_DESC = "C.DESCOM DESC";
  public static final String ORDER_BY_UTE_INDIRIZZO = "U.INDIRIZZO";
  public static final String ORDER_BY_UTE_DATA_INIZIO_ATTIVITA_ASC = "U.DATA_INIZIO_ATTIVITA ASC";
  public static final String ORDER_BY_UTE_DATA_INIZIO_ATTIVITA_DESC = "U.DATA_INIZIO_ATTIVITA DESC";
  public static final String ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE = "DESC_TIPO_UTI";
  public static final String ORDER_BY_TIPO_UTILIZZO_CODICE_ASC = "CODICE ASC";
  public static final String ORDER_BY_TIPO_UTILIZZO_CODICE_DESC = "CODICE DESC";
  public static final String ORDER_BY_CHAR_ID_TITOLO_POSSESSO_DESC = "TTP.ID_TITOLO_POSSESSO DESC";
  public static final String ORDER_BY_CHAR_ID_TITOLO_POSSESSO_ASC = "TTP.ID_TITOLO_POSSESSO ASC";
  public static final String ORDER_BY_ID_TITOLO_POSSESSO_DESC_PIANO_ATTUALE = "ID_TITOLO_POSSESSO DESC";
  public static final String ORDER_BY_ID_TITOLO_POSSESSO_ASC_PIANO_ATTUALE = "ID_TITOLO_POSSESSO ASC";
  public static final String ORDER_BY_CHAR_DESC_UTILIZZO_DESC = "COD_PRIMARIO DESC, DESC_PRIMARIO DESC ";
  public static final String ORDER_BY_CHAR_DESC_UTILIZZO_ASC = "COD_PRIMARIO ASC, DESC_PRIMARIO ASC";
  public static final String ORDER_BY_DESC_UTILIZZO_DESC_PIANO_ATTUALE = "COD_PRIMARIO DESC, DESC_PRIMARIO DESC ";
  public static final String ORDER_BY_DESC_UTILIZZO_ASC_PIANO_ATTUALE = "COD_PRIMARIO ASC, DESC_PRIMARIO ASC";
  public static final String ORDER_BY_CHAR_VARIETA_DESC = "COD_VARIETA DESC, DESC_VARIETA DESC ";
  public static final String ORDER_BY_CHAR_VARIETA_ASC = "COD_VARIETA ASC, DESC_VARIETA ASC";
  public static final String ORDER_BY_DESC_VARIETA_DESC_PIANO_ATTUALE = "COD_VARIETA DESC, DESC_VARIETA DESC ";
  public static final String ORDER_BY_DESC_VARIETA_ASC_PIANO_ATTUALE = "COD_VARIETA ASC, DESC_VARIETA ASC";
  public static final String ORDER_BY_CHAR_UTILIZZO_SECONDARIO_DESC = "COD_SECONDARIO DESC, DESC_SECONDARIO DESC";
  public static final String ORDER_BY_CHAR_UTILIZZO_SECONDARIO_ASC = "COD_SECONDARIO ASC, DESC_SECONDARIO ASC";
  public static final String ORDER_BY_DESC_UTILIZZO_SECONDARIO_DESC_PIANO_ATTUALE = "COD_SECONDARIO DESC, DESC_SECONDARIO DESC";
  public static final String ORDER_BY_DESC_UTILIZZO_SECONDARIO_ASC_PIANO_ATTUALE = "COD_SECONDARIO ASC, DESC_SECONDARIO ASC";
  public static final String ORDER_BY_CHAR_VARIETA_SECONDARIA_DESC = "COD_VAR_SECONDARIA DESC, VAR_SECONDARIA DESC";
  public static final String ORDER_BY_CHAR_VARIETA_SECONDARIA_ASC = "COD_VAR_SECONDARIA ASC, VAR_SECONDARIA ASC";
  public static final String ORDER_BY_DESC_VARIETA_SECONDARIA_DESC_PIANO_ATTUALE = "COD_VAR_SECONDARIA DESC, VAR_SECONDARIA DESC";
  public static final String ORDER_BY_DESC_VARIETA_SECONDARIA_ASC_PIANO_ATTUALE = "COD_VAR_SECONDARIA ASC, VAR_SECONDARIA ASC";
  public static final String ORDER_BY_CHAR_ID_CASO_PARTICOLARE_DESC = "SP.ID_CASO_PARTICOLARE DESC";
  public static final String ORDER_BY_CHAR_ID_CASO_PARTICOLARE_ASC = "SP.ID_CASO_PARTICOLARE ASC";
  public static final String ORDER_BY_ID_CASO_PARTICOLARE_DESC_PIANO_ATTUALE = "ID_CASO_PARTICOLARE DESC";
  public static final String ORDER_BY_ID_CASO_PARTICOLARE_ASC_PIANO_ATTUALE = "ID_CASO_PARTICOLARE ASC";
  public static final String ORDER_BY_GENERIC_DESCRIPTION = "DESCRIZIONE ASC";
  public static final String ORDER_BY_GENERIC_CODE = "1";
  public static final String ORDER_BY_TIPO_DOCUMENTO_DESCRIPTION = "TD.DESCRIZIONE ASC";
  public static final String ORDER_BY_TIPO_DOCUMENTO_DESCRIPTION_DATA = "TD.DESCRIZIONE ASC, TD.DATA_INIZIO_VALIDITA DESC";
  public static final String ORDER_BY_DATA_PROTOCOLLO = "D.DATA_PROTOCOLLO ASC";
  public static final String ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY = "C.DESCOM, SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, SP.SUBALTERNO";
  public static final String ORDER_BY_PARTIAL_STORICO_PARTICELLA_LOGIC_KEY = "SP.SEZIONE, SP.FOGLIO, SP.PARTICELLA, SP.SUBALTERNO";
  public static final String ORDER_BY_ID_GRUPPO_CONTROLLO_ASC = "ID_GRUPPO_CONTROLLO ASC";
  public static final String ORDER_BY_ID_GRUPPO_CONTROLLO_DESC = "ID_GRUPPO_CONTROLLO DESC";
  public static final String ORDER_BY_GENERIC_DATA_INIZIO_ASC = "DATA_INIZIO ASC";
  public static final String ORDER_BY_GENERIC_DATA_INIZIO_DESC = "DATA_INIZIO DESC";
  public static final String ORDER_BY_GENERIC_DATA_FINE_ASC = "DATA_FINE ASC";
  public static final String ORDER_BY_GENERIC_DATA_FINE_DESC = "DATA_FINE DESC";
  public static final String ORDER_BY_DATA_AGGIORNAMENTO_EVENTO_PARTICELLA_DESC = "DATA_AGGIORNAMENTO DESC";
  public static final String ORDER_BY_DATA_AGGIORNAMENTO_EVENTO_PARTICELLA_ASC = "DATA_AGGIORNAMENTO ASC";
  public static final String ORDER_BY_PROGR_UNAR_ASC = "PROGR_UNAR ASC";
  public static final String ORDER_BY_PROGR_UNAR_DESC = "PROGR_UNAR DESC";
  public static final String ORDER_BY_DATA_IMPIANTO_ASC = "DATA_IMPIANTO ASC";
  public static final String ORDER_BY_DATA_IMPIANTO_DESC = "DATA_IMPIANTO DESC";
  public static final String ORDER_BY_ID_TIPOLOGIA_UNAR_ASC = "ID_TIPOLOGIA_UNAR ASC";
  public static final String ORDER_BY_ID_TIPOLOGIA_UNAR_DESC = "ID_TIPOLOGIA_UNAR DESC";
  public static final String ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC = "TTU.DESCRIZIONE ASC";
  public static final String ORDER_BY_DESC_TIPOLOGIA_UNAR_DESC = "TTU.DESCRIZIONE DESC";
  public static final String ORDER_BY_DESC_UTILIZZO_UNAR_DESC = "TU.DESCRIZIONE DESC, TVAR.DESCRIZIONE DESC ";
  public static final String ORDER_BY_DESC_UTILIZZO_UNAR_ASC = "TU.DESCRIZIONE ASC, TVAR.DESCRIZIONE ASC";
  public static final String ORDER_BY_DESC_VARIETA_UNAR_DESC = "TVAR.DESCRIZIONE DESC, TU.DESCRIZIONE DESC";
  public static final String ORDER_BY_DESC_VARIETA_UNAR_ASC = "TVAR.DESCRIZIONE ASC, TU.DESCRIZIONE ASC";
  public static final String ORDER_BY_ID_TITOLO_POSSESSO_UNAR_DESC = "TTP.ID_TITOLO_POSSESSO DESC";
  public static final String ORDER_BY_ID_TITOLO_POSSESSO_UNAR_ASC = "TTP.ID_TITOLO_POSSESSO ASC";
  public static final String ORDER_BY_BLOCCANTE_UNAR_ASC = "ECU.BLOCCANTE ASC";
  public static final String ORDER_BY_BLOCCANTE_UNAR_DESC = "ECU.BLOCCANTE DESC";
  public static final String ORDER_BY_GENERIC_BLOCCANTE_ASC = "BLOCCANTE ASC";
  public static final String ORDER_BY_GENERIC_BLOCCANTE_DESC = "BLOCCANTE DESC";
  public static final String ORDER_BY_DESCRIZIONE_VARIETA_ASC = "TV.DESCRIZIONE ASC";
  public static final String ORDER_BY_DESCRIZIONE_VARIETA_DESC = "TV.DESCRIZIONE DESC";
  public static final String ORDER_BY_CODICE_TIPO_MACRO_USO_ASC = "41 ASC";
  public static final String ORDER_BY_CHAR_CODICE_TIPO_MACRO_USO_ASC = "TMU.CODICE ASC";
  public static final String ORDER_BY_CODICE_TIPO_MACRO_USO_DESC = "41 DESC";
  public static final String ORDER_BY_CHAR_CODICE_TIPO_MACRO_USO_DESC = "TMU.CODICE DESC";
  public static final String ORDER_BY_CODICE_TIPO_MACRO_USO_ASC_PIANO_ATTUALE = "CODICE ASC";
  public static final String ORDER_BY_CODICE_TIPO_MACRO_USO_DESC_PIANO_ATTUALE = "CODICE DESC";
  public static final String ORDER_BY_GENERIC_CODICE_TIPO_MACRO_USO_ASC = "TMU.CODICE ASC";
  public static final String ORDER_BY_GENERIC_CODICE_TIPO_MACRO_USO_DESC = "TMU.CODICE DESC";
  public static final String ORDER_BY_GENERIC_DESCRIZIONE_TIPO_MACRO_USO_ASC = "TMU.DESCRIZIONE ASC";
  public static final String ORDER_BY_GENERIC_DESCRIZIONE_TIPO_MACRO_USO_DESC = "TMU.DESCRIZIONE DESC";
  public static final String ORDER_BY_TIPO_UTILIZZO_FLAG_SAU_DESC = "TU.FLAG_SAU DESC";
  public static final String ORDER_BY_TIPO_UTILIZZO_FLAG_SAU_ASC = "TU.FLAG_SAU ASC";
  public static final String ORDER_BY_TIPO_UTILIZZO_FLAG_SAU_NUMBER_DESC = "3 DESC";
  public static final String ORDER_BY_TIPO_UTILIZZO_FLAG_SAU_NUMBER_ASC = "3 ASC";
  public static final String ORDER_BY_CONTROLLI_DESC_COMUNE_DESC = "DESCOM DESC";
  public static final String ORDER_BY_CONTROLLI_DESC_COMUNE_ASC = "DESCOM ASC";
  public static final String ORDER_BY_CONTROLLI_ORDINAMENTO_DESC = "ORDINAMENTO DESC";
  public static final String ORDER_BY_CONTROLLI_ORDINAMENTO_ASC = "ORDINAMENTO ASC";
  public static final String ORDER_BY_TIPOLOGIA_VINO_ASC = "TTV.DESCRIZIONE ASC";
  public static final String ORDER_BY_TIPOLOGIA_VINO_DESC = "TTV.DESCRIZIONE DESC";
  public static final String NUMBER_RECORDS_FOR_PAGE_TERRENI = "25";
  public static final String NUM_PAGINE_PER_GRUPPO = "7";
  public static final String NUM_GRUPPI_SX = "2";
  public static final String NUM_GRUPPI_DX = "2";
  public static final String COLOR_PARTICELLE_MODIFICATE = "giallo";
  public static final String ESITO_CONTROLLO_BLOCCANTE = "B";
  public static final String ESITO_CONTROLLO_WARNING = "W";
  public static final String ESITO_CONTROLLO_POSITIVO = "P";
  public static final String IMMAGINE_ESITO_BLOCCANTE = "bloccante";
  public static final String IMMAGINE_ESITO_WARNING = "anomalia";
  public static final String IMMAGINE_ESITO_POSITIVO = "eseguito";
  public static final String IMMAGINE_GIS_GIS = "gis";
  public static final String IMMAGINE_GIS_GIS_STAB = "gis_stabilizzato";
  public static final String IMMAGINE_GIS_GIS_STAB_CORSO = "gis_stabInCorso";
  public static final String IMMAGINE_GIS_P30 = "p30";
  public static final String IMMAGINE_GIS_P30_STAB = "p30_stabilizzato";
  public static final String IMMAGINE_GIS_P30_STAB_CORSO = "p30_stabInCorso";
  public static final String IMMAGINE_GIS_P25 = "p25";
  public static final String IMMAGINE_GIS_P25_STAB = "p25_stabilizzato";
  public static final String IMMAGINE_GIS_P25_STAB_CORSO = "p25_stabInCorso";
  public static final String IMMAGINE_GIS_P26 = "p26";
  public static final String IMMAGINE_GIS_P26_STAB = "p26_stabilizzato";
  public static final String IMMAGINE_GIS_P26_STAB_CORSO = "p26_stabInCorso";
  public static final String IMMAGINE_SOSPESA_GIS = "lavSosp";
  public static final String IMMAGINE_SOSPESA_GIS_STAB = "lavSosp_stabilizzato";
  public static final String IMMAGINE_SOSPESA_GIS_STAB_CORSO = "lavSosp_stabInCorso";
  public static final Integer FOGLIO_STABILIZZATO = new Integer(0);
  public static final Integer FOGLIO_STAB_IN_CORSO = new Integer(1);
  public static final String IMMAGINE_ISTANZA_INIZIO = "istanza_inizio";
  public static final String IMMAGINE_ISTANZA_FINE = "istanza_fine";
  public static final String DESC_TITLE_BLOCCANTE = "Anomalia bloccante";
  public static final String DESC_TITLE_WARNING = "Segnalazione di warning";
  public static final String DESC_TITLE_POSITIVO = "esito controllo positivo";
  public static final String CLASS_STATO_DOCUMENTO_OK = "docOk";
  public static final String TITLE_STATO_DOCUMENTO_OK = "documento attivo";
  public static final String CLASS_SCHEDARIO = "schedario";
  public static final String DESC_TITLE_SCHEDARIO = "schedario vitivinicolo";
  public static final String CLASS_BIOLOGICO = "biologico";
  public static final String DESC_TITLE_BIOLOGICO = "biologico";
  public static final String ORACLE_FINAL_DATE = "31/12/9999";
  public static final String ID_CASO_PARTICOLARE = "99";
  public static final String DATE_AMERICAN_FORMAT = "yyyyMMdd";
  public static final String DATE_EUROPEAN_STANDARD_FORMAT = "dd/MM/yyyy";
  public static final String FONTE_DATI_PARTICELLA_CERTIFICATA_DEFAULT = "AGEA";
  public static final String PARTICELLA_CERTIFICATA_NON_CENSITA = "non presente";
  public static final String PARTICELLA_CERTIFICATA_NON_UNIVOCA = "non identificata univocamente";
  public static final String FLAG_SI = "SI";
  public static final String FLAG_NO = "NO";
  public static final String FLAG_S = "S";
  public static final String FLAG_N = "N";
  public static final String FLAG_S_BINARIO = "1";
  public static final String FLAG_N_BINARIO = "0";
  public static final String PARAMETRO_RTER = "RTER";
  public static final String PARAMETRO_RTMO = "RTMO";
  public static final String PARAMETRO_SRPU = "SRPU";
  public static final String PARAMETRO_DGTF = "DGTF";
  public static final String PARAMETRO_VRPU = "VRPU";
  public static final String PARAMETRO_MDIC = "MDIC";
  public static final String PARAMETRO_CRPU = "CRPU";
  public static final String PARAMETRO_DCMP = "DCMP";
  public static final String PARAMETRO_GGAT = "GGAT";
  public static final String PARAMETRO_ALLD = "ALLD";
  public static final String PARAMETRO_CCAV = "CCAV";
  public static final String PARAMETRO_RTRA = "RTRA";
  public static final String PARAMETRO_RAZA = "RAZA";
  public static final String PARAMETRO_RSAS = "RSAS";
  public static final String PARAMETRO_ABCV = "ABCV";
  public static final String PARAMETRO_ABIS = "ABIS";
  public static final String PARAMETRO_AB3D = "AB3D";
  public static final String PARAMETRO_SIGP = "SIGP";
  public static final String PARAMETRO_CATE = "CATE";
  public static final String PARAMETRO_P26P = "P26P";
  public static final String PARAMETRO_SPAL = "SPAL";
  public static final String PARAMETRO_SPA2 = "SPA2";
  public static final String PARAMETRO_CCSI = "CCSI";
  public static final String GENERIC_CODICE_VARIETA = "000";
  public static final String DEFAULT_SUPERFICIE = "0,0000";
  public static final String TAB_TIPO_TITOLO_POSSESSO = "Titolo_Possesso";
  public static final String TAB_TIPO_CASO_PARTICOLARE = "Caso_Particolare";
  public static final String TAB_TIPO_ZONA_ALTIMETRICA = "Zona_Altimetrica";
  public static final String TAB_TIPO_AREA_A = "Area_A";
  public static final String TAB_TIPO_AREA_B = "Area_B";
  public static final String TAB_TIPO_AREA_C = "Area_C";
  public static final String TAB_TIPO_AREA_D = "Area_D";
  public static final String TAB_TIPO_AREA_E = "Area_E";
  public static final String TAB_TIPO_AREA_F = "AREA_F";
  public static final String TAB_TIPO_AREA_G = "AREA_G";
  public static final String TAB_TIPO_AREA_H = "AREA_H";
  public static final String TAB_TIPO_AREA_M = "AREA_M";
  public static final String TAB_TIPO_AREA_PSN = "AREA_PSN";
  public static final String TAB_TIPO_FASCIA_FLUVIALE = "FASCIA_FLUVIALE";
  public static final String TAB_TIPO_POTENZIALITA_IRRIGUA = "POTENZIALITA_IRRIGUA";
  public static final String TAB_TIPO_ROTAZIONE_COLTURALE = "ROTAZIONE_COLTURALE";
  public static final String TAB_TIPO_TERRAZZAMENTO = "TERRAZZAMENTO";
  public static final String TAB_TIPO_CAUSALE_MOD_PARTICELLA = "Causale_Mod_Particella";
  public static final String TAB_TIPO_EVENTO = "EVENTO";
  public static final String SEQ_UTILIZZO_CONSOCIATO = "SEQ_UTILIZZO_CONSOCIATO";
  public static final String SEQ_EVENTO_PARTICELLA = "SEQ_EVENTO_PARTICELLA";
  public static final String SEQ_ALTRO_VITIGNO = "SEQ_ALTRO_VITIGNO";
  public static final String SEQ_DOC_COR_PARTICELLA = "SEQ_DOCUMENTO_CORR_PARTICELLA";
  public static final String SEQ_DB_CCIAA_ALBO_VIGNETI = "SEQ_DB_CCIAA_ALBO_VIGNETI";
  public static final String SEQ_DB_R_PARTICELLA_AREA = "SEQ_DB_R_PARTICELLA_AREA";
  public static final String ID_PARAMETRO_ALBO =  "ALBO";
  public static final long ID_CAUSALE_MODIFICA_MULTIPLA_UNI_ARBOREE =  3;
  public static final String SEQ_DB_R_MENZIONE_PARTICELLA = "SEQ_DB_R_MENZIONE_PARTICELLA";  
  public static final String SEQ_SMRGAA_WRK_DELEGA_CON_PADR = "SEQ_SMRGAA_WRK_DELEGA_CON_PADR";
  public static final String SEQ_SMRGAA_WRK_LIST_SPORTELLI = "SEQ_SMRGAA_WRK_LIST_SPORTELLI";
  public static final String SEQ_SMRGAA_W_JAVA_INSERT = "SEQ_SMRGAA_W_JAVA_INSERT";
  public static final Long ID_PIANTA_CONSOCIATA_OLIVETO = new Long(2);
  public static final Long ID_AREA_E_ZVN = new Long(2);
  public static final Long ID_AREA_C_NO_NATURA_2000 = new Long(1);
  public static final Long ID_AREA_A_DEFAULT = new Long(1);
  public static final Long ID_AREA_B_DEFAULT = new Long(1);
  public static final Long ID_AREA_C_DEFAULT = new Long(1);
  public static final Long ID_AREA_D_DEFAULT = new Long(1);
  public static final String SEQ_DB_ACCESSO_PIANO_GRAFICO = "SEQ_DB_ACCESSO_PIANO_GRAFICO";
  /*MIN_TIME_WAIT_BEGIN*/
  public static final int MIN_TIME_WAIT = 1;
  /*MIN_TIME_WAIT_END*/
  /*MEDIUM_TIME_WAIT_BEGIN*/
  public static final int MEDIUM_TIME_WAIT = 3;
  /*MEDIUM_TIME_WAIT_END*/
  /*MAX_TIME_WAIT_BEGIN*/
  public static final int MAX_TIME_WAIT = 5;
  /*MAX_TIME_WAIT_END*/
  public static final int MAX2_TIME_WAIT = 10;
  public static final int TIME_WAIT_QUERY_RANGE_INSERT = 30;
  public static final int TIME_WAIT_QUERY_RANGE_SELECT = 240;
  public static final int ORACLE_PREPARE_STATEMENT_TIME_OUT = 1013;
  public static final Long ID_EVENTO_NUOVA_PARTICELLA = new Long(0);
  public static final Long ID_EVENTO_ACCORPAMENTO_PARTICELLA = new Long(2);
  public static final double FORMAT_SUP_CATASTALE = 999999.9999;
  public static final double FORMAT_SUP_CONDOTTA = 999999.9999;
  public static final double FORMAT_SUP_UTILIZZATA = 999999.9999;
  public static final double FORMAT_SUP_ONEDECIMAL = 999999999.9;
  public static final double FORMAT_FIVE_SUP_TWODECIMAL = 999.99;
  public static final double FORMAT_FIVE_SUP_ONEDECIMAL = 999.9;
  public static final double FORMAT_SUP_UTILIZZATA_SECONDARIA = 999999.9999;
  public static final String NO_CONTROLLI_PROCEDURA_UNITA_ARBOREE_ESEGUITI = "Non sono stati eseguiti controlli di validit&agrave sulle unit&agrave arboree.";
  public static final Long ID_GRUPPO_CONTROLLO_UNITA_ARBOREA = new Long(12);
  public static final Long ID_GRUPPO_CONTROLLO_PARTICELLARE = new Long(4);
  public static final String SEQ_STORICO_UNITA_ARBOREA = "SEQ_STORICO_UNITA_ARBOREA";
  public static final String TIPO_UTILIZZO_VIGNETO = "V";
  public static final Long ID_TIPOLOGIA_UNAR_VINO = new Long(2);
  public static final String FLAG_SCHEDARIO_MODIFICA = "M";
  public static final String CLASS_SCHEDARIO_MODIFICA = "schedarioRosso";
  public static final String DESC_TITLE_SCHEDARIO_MODIFICATO = "UV modificata";
  public static final String COLOR_UNITA_ARBOREE_STORICIZZATE = "grigio";
  public static final String PARAMETRO_RUVM = "RUVM";
  public static final String PARAMETRO_IUVP = "IUVP";
  public static final String PARAMETRO_BUCV = "BUCV";
  public static final String PARAMETRO_APUV = "APUV";
  public static final String PARAMETRO_FAPP = "FAPP";
  public static final String PARAMETRO_VTUV = "VTUV";
  public static final String PARAMETRO_UVAG = "UVAG";
  public static final String PARAMETRO_PTUV = "PTUV";
  public static final String PARAMETRO_MTUV = "MTUV";
  public static final String PARAMETRO_UV23 = "UV23";
  public static final String PARAMETRO_DAID = "DAID";
  public static final String PARAMETRO_GDEL = "GDEL";
  public static final String PARAMETRO_OTHER_UVP = "OTHER_UVP";
  public static final String PARAMETRO_LOCK_UV_CONSOLIDATE = "LOCK_UV_CONSOLIDATE";
  public static final String PARAMETRO_ACCESSO_IDONEITA = "ACCESSO_IDONEITA";
  public static final String PARAMETRO_URL_PIANO_GRAFICO = "URL_PIANO_GRAFICO";
  public static final String PARAMETRO_ACCESSO_VAR_AREA = "ACCESSO_VAR_AREA";
  public static final String PARAMETRO_ACCESSO_DT_SOVR = "ACCESSO_DT_SOVR";
  public static final String PARAMETRO_ACCESSO_ALTRI_DATI = "ACCESSO_ALTRI_DATI";
  public static final String PARAMETRO_ACCESSO_ALLINEA_GIS = "ACCESSO_ALLINEA_GIS";
  public static final String PARAMETRO_INSERISCI_NUOVA_UV = "INSERISCI_NUOVA_UV";
  public static final String PARAMETRO_INSERISCI_NUOVA_PART = "INSERISCI_NUOVA_PART";
  public static final String MODIFICA_UV_PARAMETRO_TUTTO = "ST";
  public static final String MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE = "SNV";
  public static final String MODIFICA_UV_PARAMETRO_NON_PERMESSA = "N";
  public static final Long ID_CAUSALE_MODIFICA_UNAR_IMPORTAZIONE_SCHEDARIO = new Long(1);
  public static final String SEQ_UNITA_ARBOREA = "SEQ_UNITA_ARBOREA";
  public static final Long ID_FONTE_SIAP = new Long(3);
  public static final Long ID_CAUSALE_CORREZIONE_ANOMALIE = new Long(3);
  public static final String IMMAGINE_BLOCCANTE = "Bloccante.gif";
  public static final String IMMAGINE_WARNING = "Warning.gif";
  public static final String IMMAGINE_OK = "ok.gif";
  public static final String ORDER_BY_DEFAULT_UV_IMPORTABILI = " 7, 9, 10, 11, 12";
  public static final int MIN_ANNO_VALIDITA_ISCRIZIONE_ALBO = 1900;
  public static final String STATO_UV_PROPOSTO_CAA = "P";
  public static final String DESC_STATO_UV_PROPOSTO_CAA = "Proposto";
  public static final String STATO_UV_VALIDATO_PA = "V";
  public static final String DESC_STATO_UV_VALIDATO_PA = "Validato";
  public static final String CLASS_STATO_UV_VALIDATO_PA = "validata";
  public static final Long RIEPILOGO_TITOLO_POSSESSO_COMUNE = new Long(2);
  public static final Long RIEPILOGO_TITOLO_POSSESSO = new Long(1);
  public static final Long RIEPILOGO_COMUNE = new Long(3);
  public static final Long RIEPILOGO_DESTINAZIONE_USO = new Long(4);
  public static final Long RIEPILOGO_USO_SECONDARIO = new Long(5);
  //public static final Long RIEPILOGO_ZVN = new Long(6);
  public static final Long RIEPILOGO_MACRO_USO = new Long(17);
  public static final Long RIEPILOGO_TIPO_AREA = new Long(26);
  //public static final Long RIEPILOGO_AREA_A = new Long(9);
  //public static final Long RIEPILOGO_AREA_B = new Long(10);
  //public static final Long RIEPILOGO_AREA_C = new Long(11);
  //public static final Long RIEPILOGO_AREA_D = new Long(12);
  //public static final Long RIEPILOGO_AREA_F = new Long(13);
  //public static final Long RIEPILOGO_AREA_PSN = new Long(16);
  //public static final Long RIEPILOGO_FASCIA_FLUVIALE = new Long(7);
  //public static final Long RIEPILOGO_ZVN_FASCIA_FLUVIALE = new Long(8);
  //public static final Long RIEPILOGO_AREA_G = new Long(14);
  public static final Long RIEPILOGO_ZONA_ALTIMETRICA = new Long(18);
  public static final Long RIEPILOGO_CASO_PARTICOLARE = new Long(19);
  public static final Long RIEPILOGO_TIPO_EFA = new Long(24);
  public static final Long RIEPILOGO_TIPO_GREENING = new Long(25);
  //public static final Long RIEPILOGO_AREA_H = new Long(15);
  //public static final String TIPO_RIEPILOGO_AREA_A = "A";
  //public static final String TIPO_RIEPILOGO_AREA_B = "B";
  //public static final String TIPO_RIEPILOGO_AREA_C = "C";
  //public static final String TIPO_RIEPILOGO_AREA_D = "D";
  public static final Long ID_TITOLO_POSSESSO_ASSERVIMENTO = new Long(5);
  public static final Long ID_TITOLO_POSSESSO_CONFERIMENTO = new Long(6);
  public static final String IMPORTA_ASSERVIMENTO_PARTICELLARE = "importaAsservimentoParticellare";
  public static final String IMPORTA_ASSERVIMENTO_CUAA = "importaAsservimentoCuaa";
  public static final String MAX_RECORD = "1000";
  public static final String RICERCA_IMPORTA_ASSERVIMENO_CUAA = "ricercaAzienda";
  public static final String RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE = "ricercaParticelle";
  public static final int    NUM_RIGHE_PER_PAGINA_RICERCA_TERRENI_IMPORTA_ASSERVIMENTO   = 25;
  public static final String DESC_IN_ZVN_FASCIEFLUVIALI = "Compresa nelle Zone Vulnerabili da Nitrati comprensive di Fascie fluviali";
  public static final String DESC_OUT_ZVN_FASCIEFLUVIALI = "Esterna alle Zone Vulnerabili da Nitrati comprensive di Fascie fluviali";
  public static final Long RIEPILOGO_DESTIONAZIONE_PRODUTTIVA_COMUNE = new Long(20);
  public static final Long RIEPILOGO_DESTINAZIONE_PRODUTTIVA_UVA_DA_VINO = new Long(21);
  public static final Long RIEPILOGO_VINO_DOP = new Long(22);
  public static final Long RIEPILOGO_PROVINCIA_VINO_DOP = new Long(23);
  public static final Long UVA_DA_VINO = new Long(487);
  public static final String COD_UTILIZZO_UVA_DA_VINO = "410";
  public static final String COD_DESTINAZIONE_UVA_DA_VINO = "009";
  public static final String FLAG_UTILIZZO_UVA_DA_VINO = "V";
  public static final String COD_DESTINAZIONE_UVA_DA_MENSA = "005";
  public static final String FLAG_UTILIZZO_UVA_DA_MENSA = "M";
  public static final Long UVA_DA_MENSA = new Long(180);
  public static final String NO_BIOLOGICO = "Non sono presenti indicazioni sui metodi di produzione adottati"; 
  public static final String CONFERMA_PA = "CONFERMA_PA";
  public static final Long INSERIMENTO_FRAZIONAMENTO = new Long(1);
  public static final Long INSERIMENTO_ACCORPAMENTO = new Long(2);
  public static final Integer ISTANZA_RIESAME_IN_LAVORAZIONE = new Integer(1);
  public static final Integer ISTANZA_RIESAME_LAVORATA = new Integer(2);
  public static final int FASE_CONTROLLI_CONSOLIDA = 8;
  public static final String SEQ_DB_CONDUZIONE_ELEGGIBILITA = "SEQ_DB_CONDUZIONE_ELEGGIBILITA";
  public static final int ELEGGIBILITA_FIT_VINO = 26;
  public static final String RIEPILOGO_TERRITORIALE = "T";
  public static final String RIEPILOGO_UNITA_VITATE = "U";
  public static final String ESPORTAZIONE_DATI_TERRENI = "TERRENI";
  public static final String ESP_DATI_TERRENI_BROGLIACCIO = "BROG";
  public static final String ESP_DATI_TERRENI_STAB_GIS = "REF";
  public static final String ESP_DATI_TERRENI_AVVICENDAMENTO = "AVV";
  public static final String MODIFICA_MULTIPLA = "MODIFICA_MULTIPLA";
  public static final int    NUM_RIGHE_PER_PAGINA_RICERCA_TERRENI                                 = 15;
  public static final String HTML_SELECTED                                                        = "selected=\"selected\"";
  public static final String CLASSE_ORDINAMENTO_DESCRESCENTE                                      = "giu";
  public static final String CLASSE_ORDINAMENTO_ASCENDENTE                                        = "su";
  public static final int    ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_ORDINE_DEFAULT             = 0;
  public static final int    ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_PER_CUAA                   = 1;
  public static final int    ORDINAMENTO_DETTAGLIO_TERRENI_VALIDAZIONI_PER_VALIDAZIONI            = 2;
  public static final String CODICE_CHIAMATA_AGRISERV_DETTAGLIO_RICERCA_PARTICELLA                = "PRATICHE_PARTICELLA";
  public static final String CODICE_CHIAMATA_AGRISERV_DETTAGLIO_RICERCA_PARTICELLA_IDAZIENDA      = "PRATICHE_PARTICELLA_NEW";
  public static final String CODICE_CHIAMATA_AGRISERV_PRATICHE_CONTO_CORRENTE                     = "PRATICHE_CONTO_CORRENTE";
  public static final String FILE_PD_AGRISERV_GENERIC                                             = "/it/csi/smranag/smrgaa/presentation/pdAgriserv.xml";
  public static final String CODICE_CHIAMATA_AGRISERV_RICERCA_PRATICHE_UV_IDAZIENDA               = "PRATICHE_UV";
  public static final String SEQ_DB_ESITO_PASCOLO_MAGRO = "SEQ_DB_ESITO_PASCOLO_MAGRO";
  public static final String PARAMETRO_DATA_SWAP_SEMINA = "DATA_SWAP_SEMINA";
  // Fine costanti nuovo territoriale
  
  
  //Inizio costanti stampe
  public static final int STAMPA_ATTUALE = 0;
  public static final int STAMPA_PRE_PERC_POSSESSO = 1;
  public static final int STAMPA_PRE_CORPO_IDRICO = 2;
  public static final String PARAMETRO_STAMPA_SCI = "STAMPA_SCI";
  public static final String PARAMETRO_NOTA_STAMPA_DOC = "NOTA_STAMPA_DOC";
  public static final String   COD_STAMPA_VARIAZIONI_CATASTALI = "VARIAZIONI_CATASTALI";
  //Fine costanti stampe

  // Inizio costanti conti correnti
  public static final String PARAMETRO_OCIN = "OCIN";
  public static final String CIN_DEFAULT = "*";
  public static final String MSG_ERR_CIFRA_CONTROLLO = "La cifra di controllo deve essere composta da 2 numeri";
  public static final String MSG_ERR_IBAN = "La Cifra controllo del codice IBAN non risulta corretta";
  public static final String MSG_VALIDAZIONE_CC_OK = "Conto corrente validato";
  public static final String MSG_VALIDAZIONE_CC_KO = "Conto corrente invalidato";
  // Fine costanti conti correnti

  // Inizio costanti documenti
  public static final Long ID_TIPOLOGIA_DOCUMENTO_TERRITORIALE = new Long(2);
  public static final Long ID_TIPOLOGIA_DOCUMENTO_ANAGRAFICO = new Long(1);
  public static final String FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI = "A";
  public static final String FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI = "Z";
  public static final String FLAG_ANAG_TERR_DOCUMENTI_TERRENI = "T";
  public static final String FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR = "C";
  public static final String FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI = "B";
  public static final String PROTOCOLLAZIONE_EFFETTUATA = "1";
  public static final String PROTOCOLLAZIONE_DA_EFFETTUARE = "2";
  public static final String ID_STATO_DOCUMENTO_ANNULLATO = "1";
  public static final String ID_STATO_DOCUMENTO_STORICIZZATO = "2";
  public static final String CLASS_STATO_DOCUMENTO_ANNULLATO = "docAnnullato";
  public static final String CLASS_STATO_DOCUMENTO_STORICIZZATO = "docStoricizzato";
  public static final String CLASS_STATO_DOCUMENTO_SCADUTO = "docScaduto";
  public static final String TITLE_STATO_DOCUMENTO_ANNULLATO = "documento annullato";
  public static final String TITLE_STATO_DOCUMENTO_STORICIZZATO = "documento storicizzato";
  public static final String TITLE_STATO_DOCUMENTO_SCADUTO = "documento scaduto";
  public static final String TITLE_STATO_DOCUMENTO_ATTIVO = "documento attivo";
  public static final String TAB_TIPO_TIPOLOGIA_DOCUMENTO = "TIPOLOGIA_DOCUMENTO";
  public static final String ID_STATO_DOCUMENTO_ATTIVO = "-1";
  public static final String DESCRIZIONE_DOCUMENTO_ATTIVO = "Attivo";
  public static final String MAX_DATA_ESECUZIONE_NOT_EXECUTED = "Non sono mai stati eseguiti i controlli sui documenti";
  public static final String PARAMETRO_DOAT = "DOAT";
  public static final Long ID_GRUPPO_CONTROLLO_DOCUMENTALE = new Long(11);
  public static final String DATA_INIZIO_ANNO_AGRICOLO = "11/11/"+String.valueOf((DateUtils.getCurrentYear()).intValue()-1);
  public static final String PARAMETRO_MAX_DIM_FILE_UP = "MAX_DIM_FILE_UP";
  public static final String SEQ_DB_ALLEGATO = "SEQ_DB_ALLEGATO";
  public static final String SEQ_DB_ALLEGATO_DOCUMENTO = "SEQ_DB_ALLEGATO_DOCUMENTO";
  public static final String CAUSALE_MOD_CORR_ANOMALIA = "1";
  public static final long   DOC_IST_RIES_FOTOINT = 283;
  public static final long   DOC_IST_RIES_CONTROL_CONTRAD = 340;
  public static final long   DOC_IST_RIES_SOP_CAMPO = 341;
  public static final String PARAMETRO_GGFOTORIC = "GGFOTORIC";
  public static final String PARAMETRO_GGCONTRORIC = "GGCONTRORIC";
  public static final String PARAMETRO_GGFOTO = "GGFOTO";
  public static final String PARAMETRO_GGCONTRO = "GGCONTRO";
  public static final int FASE_IST_RIESAM_FOTO = 1;
  public static final int FASE_IST_RIESAM_CONTRO = 2;
  public static final int FASE_IST_RIESAM_SOPRA = 3;
  public static final String SEQ_DB_ISTANZA_RIESAME = "SEQ_DB_ISTANZA_RIESAME";
  public static final String SEQ_DB_ISTANZA_RIESAME_AZIENDA = "SEQ_DB_ISTANZA_RIESAME_AZIENDA";
  public static final String PARAMETRO_CANC_PART_ISTANZA_F2 = "CANC_PART_ISTANZA_F2";
  // Fine costanti documenti

  // Inizio costanti SIAN
  public static final String TAB_SIAN_TIPO_ORIGINE_TITOLO = "ORIGINE_TITOLO";
  public static final String TAB_SIAN_TIPO_MOVIMENTO_TITOLO = "MOVIMENTO_TITOLO";
  public static final String TAB_SIAN_TIPO_TITOLO = "TITOLO";
  public static final int DEFAULT_DIVISION_SIAN_SUPERFICIE = 10000;
  public static final Long CODICE_ATTESA_VALIDAZIONE_SIAN = new Long(1);
  public static final Long CODICE_VALIDATO_SIAN = new Long(2);
  public static final Long CODICE_RESPINTO_SIAN = new Long(3);
  public static final String DESCRIZIONE_ATTESA_VALIDAZIONE_SIAN = "in attesa di validzione";
  public static final String DESCRIZIONE_VALIDATO_SIAN = "validato";
  public static final String DESCRIZIONE_RESPINTO_SIAN = "respinto";
  public static final Long SAL_CODICE_TITOLO_PROVVISORIO_SIAN = new Long(1);
  public static final Long SAL_CODICE_TITOLO_DEFINITIVO_SIAN = new Long(2);
  public static final String SAL_DESCRIZIONE_TITOLO_PROVVISORIO_SIAN = "provvisorio";
  public static final String SAL_DESCRIZIONE_TITOLO_DEFINITIVO_SIAN = "definitivo";
  public static final long ID_OPR_PIEMONTE = 5;
  //Elenco ruoli per cui non usare il suo codice fiscale in SIAN
  public static final String RUOLI_ESCLUSI = "AZIENDA_AGRICOLA@AGRICOLTURA,"+
   "TITOLARE_CF@UTENTI_IRIDE2,"+"LEGALE_RAPPRESENTANTE@ALI,"+"GAL@AGRICOLTURA,"+"AMBITO_CACCIA@AGRICOLTURA,"+
   "COMPRENSORIO_ALPINO@AGRICOLTURA,"+"BATCH@AGRICOLTURA";
  public static final String SIAN_CODICE_NO_FASCICOLI = "031";
  public static final String SIAN_TRUE = "true";
  public static final String SIAN_FALSE = "false";

  public static final String SEQ_CF_COLLEGATI_SIAN = "SEQ_CF_COLLEGATI_SIAN";
  public static final String SEQ_DOMICILIO_FISC_VARIATO_SIA = "SEQ_DOMICILIO_FISC_VARIATO_SIA";
  public static final String SEQ_RESIDENZA_VARIATA_SIAN = "SEQ_RESIDENZA_VARIATA_SIAN";
  public static final String SEQ_RAPPRESENTANTI_SOCIETA_SIA = "SEQ_RAPPRESENTANTI_SOCIETA_SIA";
  public static final String SEQ_PARTITE_IVA_ATTRIBUITE_SIA = "SEQ_PARTITE_IVA_ATTRIBUITE_SIA";
  public static final String SEQ_SOCIETA_RAPPRESENTATE_SIAN = "SEQ_SOCIETA_RAPPRESENTATE_SIAN";
  public static final String SEQ_ALLEVAMENTI_SIAN = "SEQ_ALLEVAMENTI_SIAN";
  public static final String SEQ_PARTICELLA_SIAN = "SEQ_PARTICELLA_SIAN";
  public static final String SEQ_UNITA_ARBOREA_SIAN = "SEQ_UNITA_ARBOREA_SIAN";
  public static final String SEQ_PARTICELLA_POLIGONO_SIAN = "SEQ_PARTICELLA_POLIGONO_SIAN";
  public static final String SEQ_POLIGONO_UNITA_ARBOREA_SIA = "SEQ_POLIGONO_UNITA_ARBOREA_SIA";
  public static final String SIAN_DATA_NULL = "31/12/9999";
  public static final int DIMENSIONE_CAMPO_PARTICELLA_SIAN = 5;
  public static final String SIAN_CODICE_SERVIZIO_OK = "012";
  public static final String SIAN_CODICE_SERVIZIO_ALLEVAMENTI_NO_RECORD = "BDN";
  public static final String SIAN_CUAA_NON_PRESENTE = "XX02";
  public static final String SIAN_SOGGETTO_NON_PRESENTE = "XX01";
  public static final String SIAN_CUAA_ASSOCIATO_A_PIU_PERSONE = "XX03";
  public static final String SIAN_FLAG_PRESENTE_AT_ERRORE = "E";
  public static final String SIAN_FLAG_PRESENTE_AT_INDISPONIBILE = "I";
  public static final String SIAN_FLAG_NON_PRESENTE_AT = "N";
  public static final String SIAN_FLAG_PRESENTE_AT = "S";
  public static final String SIAN_CODICE_SERVIZIO_NO_TERRENI = "016";
  public static final String SEQ_DB_ATECO_SEC_TRIBUTARIA = "SEQ_DB_ATECO_SEC_TRIBUTARIA";
  // Fine costanti SIAN

   // Fine costanti Teramo
  public static final String TERAMO_CODICE_NO_ALLEVAMENTI = "E043";
  public static final String MODIFICA_BDN = "modificaBdn";
  // Inizio costanti Teramo

  // Inizio costanti ANAGRAFICA
  public static final String ORDER_BY_DATA_CESSAZIONE_ASC = "DATA_CESSAZIONE ASC";
  public static final String ORDER_BY_DATA_CESSAZIONE_DESC = "DATA_CESSAZIONE DESC";
  public static final String SEQ_AZIENDA_DESTINAZIONE = "SEQ_AZIENDA_DESTINAZIONE";
  public static final String SEQ_DB_AZIENDA_ATECO_SEC = "SEQ_AZIENDA_ATECO_SEC";
  public static final String SEQ_DB_AZIENDA_SEZIONI = "SEQ_DB_AZIENDA_SEZIONI";
  // Fine costanti ANAGRAFICA
  
  // Inizio costanti VALIDAZIONI
  public static final Long ID_FASE_DICHIARAZIONE = new Long("1");
  public static final String ESITO_AGGIORNAMENTO_SIAN_OK = "Fascicolo aggiornato correttamente su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_WAIT = "Fascicolo in attesa del controllo su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_CT_OK = "Consistenza aggiornata correttamente su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_UV_OK = "Unit&agrave; vitate aggiornate correttamente su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_FABBRICATI_OK = "Fabbricati aggiornati correttamente su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_CC_OK = "Conti correnti aggiornati correttamente su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_KO = "Si sono verificate delle anomalie nell'aggiornamento del fascicolo su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_CT_KO = "Si sono verificate delle anomalie nell'aggiornamento della consistenza su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_UV_KO = "Si sono verificate delle anomalie nell'aggiornamento delle unit&agrave; vitate su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_FABBRICATI_KO = "Si sono verificate delle anomalie nell'aggiornamento dei fabbricati su SIAN";
  public static final String ESITO_AGGIORNAMENTO_SIAN_CC_KO = "Si sono verificate delle anomalie nell'aggiornamento dei conti correnti su SIAN";
  public static final String DATA_AGGIORNAMENTO_SIAN_KO = "Fascicolo non aggiornato su SIAN";
  public static final String DATA_AGGIORNAMENTO_SIAN_CT_KO = "Consistenza non aggiornata su SIAN";
  public static final String DATA_AGGIORNAMENTO_SIAN_UV_KO = "Unità vitate non aggiornate su SIAN";
  public static final String DATA_AGGIORNAMENTO_SIAN_FABBRICATI_KO = "Fabbricati non aggiornati su SIAN";
  public static final String DATA_AGGIORNAMENTO_SIAN_CC_KO = "Conti correnti non aggiornati su SIAN";
  public static final String ORDER_BY_ORDINAMENTO_ASC = "ORDINAMENTO ASC";
  public static final String ORDER_BY_ORDINAMENTO_DESC = "ORDINAMENTO DESC";
  public static final String ORDER_BY_ANNO_CONSISTENZA_ASC = "DC.ANNO ASC";
  public static final String ORDER_BY_ANNO_CONSISTENZA_DESC = "DC.ANNO DESC";
  public static final String ORDER_BY_DATA_CONSISTENZA_ASC = "DC.DATA ASC";
  public static final String ORDER_BY_DATA_CONSISTENZA_DESC = "DC.DATA DESC";
  public static final String ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY_FOR_ANOMALIE = " 13, 14, 15, 16, 17";
  public static final String TIPO_DICHIARAZIONE_CORRETTIVA = "C";
  public static final String TIPO_DICHIARAZIONE_COMUNICAZIONE_10R = "R";
  public static final String LABEL_DICHIARAZIONE_COMUNICAZIONE_10R = "COM. 10/R";
  public static final int MAX_TIME_WAIT_PACK_RIPRISTINA_DICHIARAZIONE = 180;
  public static final String RICHIESTA_CONFERMA_DELETE_VALIDAZIONE_CON_FASCICOLI_NAZIONALE_BACKUP = "Attenzione: la validazione che si sta eliminando è relativa ad un fascicolo già aggiornato su SIAN.Eliminare solo in caso sia stata fatta una dichiarazione errata. Sicuri di voler proseguire?";
  public static final String NUOVA_DICHIARAZIONE_CONSISTENZA = "N";
  public static final String NO_NUOVA_DICHIARAZIONE_CONSISTENZA = "Impossibile procedere nella validazione in quanto è stata selezionata"+
    " una motivazione correttiva il cui anno campagna non corrisponde a quello " +
    "presente nella validazione ripristinata sul piano in lavorazione all''azienda";
  public static final String SCHED_STATO_SCHEDULATO = "S";
  public static final String SCHED_STATO_INVIATO = "I";
  public static final String SCHED_STATO_CHIUSO = "C";
  public static final String SEQ_DB_INVIO_FASCICOLI = "SEQ_DB_INVIO_FASCICOLI";
  public static final String PARAMETRO_MITT_MAIL_PER_DICH = "MITT_MAIL_PER_DICH";
  public static final String PARAMETRO_OGG_MAIL_PER_DICH = "OGG_MAIL_PER_DICH";
  public static final String PARAMETRO_MAIL_PER_DICH = "MAIL_PER_DICH";
  public static final String PARAMETRO_ABILITA_INVIO_MAIL = "ABILITA_INVIO_MAIL";
  public static final String MSG_SCHEDELAZIONE_NO_ULTIMA_PROTO = "Attenzione: su SIAN verranno inviati i dati relativi ad una consistenza che non coincide con l'ultima valida per questa azienda. " + 
    "Se necessario avere l'ultima situazione aggiornata e' possibile rivalidare il fascicolo aziendale oppure schedulare anche i dati dell'ultima consistenza valida non appena risultera' concluso l'invio appena inserito.";
  public static final String MSG_SCHEDELAZIONE_OK = "La schedulazione e' stata inserita con successo.";
  public static final String MSG_SCHEDELAZIONE_OK_ANNULLATA = "La schedulazione e' stata annullata con successo.";
  public static final String IMMAGINE_PIANO_GRAFICO = "pianoGrafico";
  // Fine costanti VALIDAZIONI

  // Inizio costanti FABBRICATI
  public static final String ORDER_BY_DATA_INIZIO_VALIDITA_ASC = "F.DATA_INIZIO_VALIDITA ASC";
  public static final String ORDER_BY_DATA_INIZIO_VALIDITA_DESC = "F.DATA_INIZIO_VALIDITA DESC";
  public static final String ORDER_BY_DATA_FINE_VALIDITA_ASC = "F.DATA_FINE_VALIDITA ASC";
  public static final String ORDER_BY_DATA_FINE_VALIDITA_DESC = "F.DATA_FINE_VALIDITA DESC";
  public static final String ORDER_BY_DESC_TIPO_TIPOLOGIA_FABBICATO_ASC = "TTF.DESCRIZIONE ASC";
  public static final String ORDER_BY_DESC_TIPO_TIPOLOGIA_FABBICATO_DESC = "TTF.DESCRIZIONE DESC";
  public static final String ORDER_BY_UV_DATA_FINE_VALIDITA_ASC = "SUA.DATA_FINE_VALIDITA ASC";
  public static final String ORDER_BY_UV_DATA_FINE_VALIDITA_DESC = "SUA.DATA_FINE_VALIDITA DESC";
  public static final String ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC = "UAD.DATA_FINE_VALIDITA DESC";
  public static final String NO_BIOLOGICO_FABBRICATI = "Non sono presenti indicazioni sulla dimensione";
  public static final String STRUTTURA_STANDARD = "S";
  public static final String STRUTTURA_VASCA_CIRCOLARE = "C";
  public static final String STRUTTURA_POTENZA = "N";
  public static final String STRUTTURA_VASCA_RETTANGOLARE = "R";
  public static final String STRUTTURA_LAGONE = "L";
  public static final String STRUTTURA_PLATEA = "P";
  // Fine costanti FABBRICATI

  // Inizio costanti ATTESTAZIONI
  public static final String ORDER_BY_CODICE_ATTESTAZIONE_ASC = "TA.CODICE_ATTESTAZIONE ASC";
  public static final String ORDER_BY_TIPO_ATTESTAZIONE_GRUPPO_ASC = "TA.GRUPPO ASC";
  public static final String ORDER_BY_TIPO_ATTESTAZIONE_GRUPPO_DESC = "TA.GRUPPO DESC";
  public static final String ORDER_BY_TIPO_ATTESTAZIONE_ORDINAMENTO_ASC = "TA.ORDINAMENTO ASC";
  public static final String ORDER_BY_TIPO_ATTESTAZIONE_ORDINAMENTO_DESC = "TA.ORDINAMENTO DESC";
  public static final String NUMERO_RIGHE_INTESTAZIONE_ATTESTAZIONE = "0";
  public static final String NUMERO_RIGHE_MONO_ATTESTAZIONE = "1";
  public static final String TIPO_RIGHE_ATTESTAZIONE_CHECKBOX = "C";
  public static final String NUMERO_RIGHE_DOUBLE_ATTESTAZIONE = "2";
  public static final Long ID_ATTESTAZIONE_PADRE_INTESTAZIONE = new Long(0);
  public static final String TIPO_ATTESTAZIONE_CARATTERE_BOLD = "B";
  public static final String SEQ_ATTESTAZIONE_AZIENDA = "SEQ_ATTESTAZIONE_AZIENDA";
  public static final String SEQ_ATTESTAZIONE_DICHIARATA = "SEQ_ATTESTAZIONE_DICHIARATA";
  public static final String TIPO_PARAMETRO_ATTESTAZIONE_DATA = "D";
  public static final String TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO = "N";
  public static final String TIPO_PARAMETRO_ATTESTAZIONE_TESTO = "T";
  public static final String TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE = "M";
  public static final String TIPO_PARAMETRO_ATTESTAZIONE_TESTO_READ_ONLY = "F";
  public static final String SEQ_PARAMETRI_ATT_AZIENDA = "SEQ_PARAMETRI_ATT_AZIENDA";
  public static final String SEQ_PARAMETRI_ATT_DICHIARATA = "SEQ_PARAMETRI_ATT_DICHIARATA";
  public static final String VOCE_MENU_ATTESTAZIONI_DICHIARAZIONI = "DICHIARAZIONI";
  public static final String VOCE_MENU_ATTESTAZIONI_ALLEGATI = "ALLEGATI";
  public static final String TIPO_ATTESTAZIONE_NPUA = "NPUA";
  public static final String PARAMETRO_DT_FINE_CONDIZ = "DT_FINE_CONDIZ";
  public static final String TIPO_ATTESTAZIONE_COND = "COND";
  public static final int ATTESTAZIONI_DATA_MINORE_DATAMIN = -1;
  public static final int ATTESTAZIONI_DATA_COMPRESA_DATAMIN_DATAMAX = 0;
  public static final int ATTESTAZIONI_DATA_MAGGIORE_DATAMAX = 1;
  // Fine costanti ATTESTAZIONI

  public static final int ID_PROCEDIMENTO_ANAGRAFE = 7;
  public static final String ID_UTENTE_DEFAULT_INSERIMENTO = "9999999997";
  public static final Long ID_UTENTE_DEFAULT = new Long("9999999999");
  public static final String TIPORUOLO_COADIUVANTE = "3";
  public static final String NUMBER_FORMAT_INTEGER = "#######0";
  public static final String NUMBER_FORMAT_7 = "#######";
  public static final String NUMBER_FORMAT_5 = "#####";
  public static final String NUMBER_FORMAT_4 = "####";
  public static final String NUMBER_EURO_FORMAT = "###0.00";
  public static final String NUMBER_DOUBLE_FORMAT = "###0.0#";
  public static final String PARAMETRO_COMUNE_MAPR = "MAPR";
  public static final String PARAMETRO_COMUNE_DCNP = "DCNP";
  public static final Long ID_TIPO_DOCUMENTO_REVOCA_MANDATO_ASSISTENZA = new Long(17);
  public static final Long ID_TIPO_DOCUMENTO_MANDATO_ASSISTENZA = new Long(16);
  public static final String DATE_EUROPE_FORMAT = "dd/MM/yyyy";
  public static final String FULL_DATE_ORACLE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String FULL_DATE_EUROPE_FORMAT = "dd/MM/yyyy HH:mm:ss";
  public static final String ORACLE_DEFAULT_YEAR = "9999";

  public static final String UNITA_MISURA_METRI_QUADRI = "m2";
  public static final int MAX_NUMERO_GIORNI_MESE = 31;
  public static final int MAX_NUMERO_ORE_GIORNO = 24;

  public static final String ISTAT_COLCAVAGNO = "005043";
  public static final String ISTAT_SOSTITUTIVO_COLCAVAGNO = "005121";
  public static final String SEZIONE_SOSTITUTIVO_COLCAVAGNO = "A";

  public static final String ISTAT_MONTIGLIO = "005078";
  public static final String ISTAT_SOSTITUTIVO_MONTIGLIO = "005121";
  public static final String SEZIONE_SOSTITUTIVO_MONTIGLIO = "B";

  public static final String ISTAT_SCANDELUZZA = "005102";
  public static final String ISTAT_SOSTITUTIVO_SCANDELUZZA = "005121";
  public static final String SEZIONE_SOSTITUTIVO_SCANDELUZZA = "C";

  public static final String ISTAT_MOSSO_SANTA_MARIA = "096036";
  public static final String ISTAT_SOSTITUTIVO_MOSSO_SANTA_MARIA = "096084";
  public static final String SEZIONE_SOSTITUTIVO_MOSSO_SANTA_MARIA = "A";

  public static final String ISTAT_PISTOLESA = "096045";
  public static final String ISTAT_SOSTITUTIVO_PISTOLESA = "096084";
  public static final String SEZIONE_SOSTITUTIVO_PISTOLESA = "B";


  public static final String ID_CORREZIONE_DOMANDA_UNICA = "8";
  public static final String PRATICA_IN_BOZZA = "In Bozza";
  public static final int ID_PROCEDIMENTO_PSR = 2;
  public static final int ID_PROCEDIMENTO_RPU = 12;

  public static final String PRATICA_PSR_2007_NO_DIC_CONS ="Impossibile variare i dati di consistenza in quanto la dichiarazione risulta legata ad una pratica di Piano di Sviluppo Rurale";
  
  //Inizio Reportistica
  public static final String LEGENDA_REPORT_MANDATI_VALIDAZIONI = "* vengono conteggiate solo le validazioni nel periodo @dal@al. La stessa azienda con più validazioni viene conteggiata una sola volta.";
  public static final String DATE_FORMAT_4REPORT = "dd/MM/yyyy";
  public static final String TIME_OUT_QUERY_REPORTDIN = "Il report selezionato genera un errore di time-out.";
  //Fine Reportistica
  
  //Inizio Soggetti Collegati
  public static final String SRC_IMG_CELLULARE = "cellulare.gif";
  public static final String DESC_IMG_CELLULARE = "Il soggetto @@c@@n possiede recapito cellulare";
  //Fine Soggetti Collegati
  
  // Inizio Notifiche
  public static final Long ID_PROFILO_PA_RW = new Long(1);
  public static final String PARAMETRO_NOTIFI_RMAX = "NOTIFI_RMAX";
  public static final String PARAMETRO_CAT_NOTIFICA_REVOCA = "CAT_NOTIFICA_REVOCA";
  public static final String TIPO_ENTITA_UV = "1";
  public static final String TIPO_ENTITA_PARTICELLE = "2";
  public static final String SEQ_DB_NOTIFICA_ENTITA = "SEQ_DB_NOTIFICA_ENTITA";
  public static final String SEQ_DB_ALLEGATO_NOTIFICA = "SEQ_DB_ALLEGATO_NOTIFICA";
  public static final Long   ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI = new Long(15);
  public static final Long   ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE = new Long(40);
  public static final String STATO_NOTIFICHE_PARTICELLARI = "notificheparticellari";
  public static final String DESC_NOTIFICHE_PARTICELLARI = "Notifica variazioni catastali";
  public static final String STATO_BLOCCO_PROCEDIMENTI = "bloccoprocedimenti";
  public static final String DESC_BLOCCO_PROCEDIMENTI = "Notifica di blocco procedimenti vitivinicoli";
  public static final String IMMAGINE_BLOCCOPROCEDIMENTI = "BloccoProcedimenti.gif";
  public static final String IMMAGINE_VARIAZIONICATASTALI = "notifiche_particellari.gif";
  public static final String STATO_FASCICOLO_VIRTUALE = "fascicolovirtuale";
  public static final String DESC_FASCICOLO_VIRTUALE = "fascicolo dematerializzato";
  public static final String PARAMETRO_INVIO_MAIL_NOTIF = "INVIO_MAIL_NOTIF";
  public static final String PARAMETRO_MITT_MAIL_NOTIF = "MITT_MAIL_NOTIF";
  public static final String PARAMETRO_MITT_MAIL_NOTIF_M = "MITT_MAIL_NOTIF_M";
  public static final String PARAMETRO_OGG_MAIL_NOTIF = "OGG_MAIL_NOTIF";
  public static final String PARAMETRO_OGG_MAIL_NOTIF_M = "OGG_MAIL_NOTIF_M";
  public static final String PARAMETRO_MAIL_PER_NOTIF = "MAIL_PER_NOTIF";
  public static final String PARAMETRO_MAIL_PER_NOTIF_M = "MAIL_PER_NOTIF_M";
  public static final String PARAMETRO_MAIL_PER_CH_NOTIF = "MAIL_PER_CH_NOTIF";
  public static final String COD_TIPO_ENTITA_UV = "U";
  public static final String COD_TIPO_ENTITA_PARTICELLE = "P";
  //Fine Notifiche  

  //Inizio Trasferimento ufficio
  public static final String SERVIZI_TRASFERIMENTO_UFFICIO = "T";
  public static final String NUMBER_RECORDS_FOR_PAGE_TRASFERIMENTO_AZIENDA = "25";
  public static final String UFFICIO_ZONA = "Z";
  public static final String UFFICIO_PROVINCIALE = "P";
  public static final String UFFICIO_REGIONALE = "R";
  //Fine Trasferimento ufficio  

  //Inizio Aziende Collegate
  public static final String SEQ_DB_AZIENDA_COLLEGATA = "SEQ_DB_AZIENDA_COLLEGATA";
  public static final String SEQ_DB_SOGGETTO_ASSOCIATO = "SEQ_SOGGETTO_ASSOCIATO";
  public static final String DICHIARAZIONE_INSEDIAMENTO = "DICHIARAZIONE INSEDIAMENTO";
  public static final int    NUM_RIGHE_PER_PAGINA_AZIENDE_COLLEGATE = 15;
  public static final int    MAX_NUMERO_SOCI_PER_TOTALE = 2500;
  //Fine Aziende Collegate

  //Inizio WEB SERVICE CCIAA
  public static final String CCIAA_CODICE_SERVIZIO_OK = "012";
  public static final String CCIAA_CODICE_SERVIZIO_NO_RECORD = "016";
  public static final String IMPORTA_CCIAA = "IMPORTA_CCIAA";
  //Fine WEB SERVICE CCIAA
  
  //Inizio Comunicazione10R
  public static final String SEQ_DB_COMUNICAZIONE_10R = "SEQ_COMUNICAZIONE_10R";
  public static final String SEQ_DB_EFFLUENTE_10R = "SEQ_EFFLUENTE_10R";
  public static final String SEQ_DB_EFFLUENTE_CES_ACQ_10R = "SEQ_EFFLUENTE_CES_ACQ_10R";
  public static final String SEQ_DB_EFFLUENTE_STOC_EXT_10R = "SEQ_EFFLUENTE_STOC_EXT_10R";
  public static final String SEQ_DB_ACQUA_EXTRA_10R = "SEQ_ACQUA_EXTRA_10R";
  public static final String LUNGHEZZA_MAX_NOTE_COMUNICAZIONE10R = "300";
  public static final Long   ID_TIPO_CAUS_EFF_ACQUISIZIONE = new Long(2);
  public static final Long   ID_TIPO_CAUS_EFF_CESSIONE = new Long(1);
  public static final Long   TRATTAMENTO_ALTRI = new Long(8);
  //Fine Comunicazione10R
  
  //Inizio Variazioni aziendali
  public static final String DB_TIPO_TIPOLOGIA_VARIAZIONE = "TIPO_TIPOLOGIA_VARIAZIONE";
  public static final String ID_TIPO_TIPOLOGIA_VARIAZIONE = "ID_TIPO_TIPOLOGIA_VARIAZIONE";
  public static final String DB_TIPO_VARIAZIONE_AZIENDA = "TIPO_VARIAZIONE_AZIENDA";
  public static final String ID_PARAMETRO_RVAR="RVAR";
  public static final String ID_PARAMETRO_RVAP="RVAP";
  public static final String ID_PARAMETRO_RVMX="RVMX";
  public static final String SEQ_DB_VISIONE_VARIAZIONE_AZI = "SEQ_VISIONE_VARIAZIONE_AZI";
  //Fine Variazioni aziendali
  
  //Inizio Sigmater
  public static final String FILE_PD_SIGMATER = "/it/csi/solmr/etc/services/defpd_daticatastali.xml";
  public static final String NOME_SERVIZIO_SIGMATER = "SERVIZIO_SIGMATER";
  public static final String SIGMATER_CERCASOGGETTOGIURIDICO= "CERCA_SOGGETTO_GIURIDICO";
  public static final String SIGMATER_CERCASOGGETTOFISICO= "CERCA_SOGGETTO_FISICO";
  public static final String SIGMATER_CERCATITOLARITASOGGETTOCATASTALE= "CERCA_TITOLARITA_SOGGETTO_CATASTALE";
  public static final String SEQ_DB_RICHIESTA_SIGMATER = "SEQ_DB_RICHIESTA_SIGMATER";
  public static final String SEQ_DB_SOGGETTO_SIGMATER = "SEQ_DB_SOGGETTO_SIGMATER";
  public static final String SEQ_DB_TITOLARITA_SIGMATER = "SEQ_DB_TITOLARITA_SIGMATER";
  public static final String SEQ_DB_TITOL_PARTICELLA_SIG = "SEQ_DB_TITOL_PARTICELLA_SIG";
  public static final String SEQ_DB_PARTICELLA_SIGMATER = "SEQ_DB_PARTICELLA_SIGMATER";
  public static final String SEQ_DB_PROPRIETA_CERTIFICATA = "SEQ_DB_PROPRIETA_CERTIFICATA";
  //Fine Sigmater
  
  
  //Smrgaasrv
  public static final String FILE_PD_SMRGAASRV = "/it/csi/solmr/etc/services/pdSmrgaasrv.xml";
  
  
  public static final String SOTTO_PROCEDIMENTO = "ANAG";
  public static final Integer EXT_ID_PROCEDIMENTO = new Integer("7");
  
  //Piano Lavorazione
  public static final String PIANO_LAVORAZIONE_NORMALE = "NORMALE";
  public static final String PIANO_LAVORAZIONE_TERRENI = "TERRENI";
  public static final String PIANO_LAVORAZIONE_UV = "UNITA_VITATE";
  
  //Inizio Polizze
  public static final String NO_POLIZZE = "Non esistono polizze relative all'azienda agricola selezionata";
  public static final String TIPO_POLIZZA_COLTURA = "C";
  public static final String TIPO_POLIZZA_STRUTTURA = "S";
  public static final String TIPO_POLIZZA_ZOOTECNIA = "Z";
  //Fine Polizze
  
  //Inizio Isole parcelle
  public static final String ISO_TOLLERANZA_OK = "La parcella e' in tolleranza";
  public static final String ISO_TOLLERANZA_FUORI_KO = "La parcella e' fuori tolleranza";
  public static final String ISO_TOLLERANZA_UVDOPPIE_KO = "Le unita' vitate della parcella appartengono anche ad altre parcelle";
  public static final String ISO_TOLLERANZA_NO_PARCELLE_KO = "Isole non calcolate";
  public static final String ISO_TOLLERANZA_PLSQL_KO = "Errore nell'esecuzione del plsql relativo alla tolleranza";
  public static final String SEQ_DB_UNAR_PARCELLA = "SEQ_DB_UNAR_PARCELLA";
  public static final Integer IN_TOLLERANZA = new Integer(0);
  public static final Integer FUORI_TOLLERANZA = new Integer(1);
  public static final Integer UVDOPPIE_TOLLERANZA = new Integer(2);
  public static final Integer NO_PARCELLE_TOLLERANZA = new Integer(3);
  public static final Integer ERR_PL_TOLLERANZA = new Integer(4);
  public static final Integer UV_NON_PRESENTE = new Integer(5);
  public static final Integer PARTICELLA_ORFANA = new Integer(6);
  public static final Integer PARCELLE_NO_VITE = new Integer(7);
  public static final Integer UV_PIU_OCCORR_ATTIVE = new Integer(8);
  public static final Long CAUSALE_ALLINEA_GIS = new Long(12);
  public static final String UV_TOLLERANZA_OK = "L'unita' vitata e' in tolleranza";
  public static final String UV_TOLLERANZA_FUORI_KO = "L'unita' vitata e' fuori tolleranza";
  public static final String UV_TOLLERANZA_UVDOPPIE_KO = "L'unita' vitata appartiene a piu' parcelle";
  public static final String UV_NON_PRESENTE_KO = "Unita' vitata non presente o non attiva";
  public static final String UV_TOLLERANZA_PARTICELLA_ORFANA_KO = "Particella orfana";
  public static final String UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO = "Unità vitata duplicata";
  public static final String UV_TOLLERANZA_PARCELLE_NO_VITE_KO = "Parcelle non a vite";
  //Fine Isole parcelle
  
  
  //Inizio AAEP
  public static final String FONTE_DATO_INFOCAMERE_STR = "3";
  public static String SEQ_DB_PROC_CONCORSUALE_AAEP = "SEQ_DB_PROC_CONCORSUALE_AAEP";
  //Fine AAEP
  
  //Inizio Revoca
  public static final int NO_RICHIESTE_REVOCA_ATTIVE = 0;
  public static final int SI_RICHIESTE_REVOCA_ATTIVE = 1;
  //Fine Revoca
  
  //Variabili sian vecchie poi da togliere
  public static final String SIAN_CODICE_SERVIZIO_ANAGRAFE_TRIBUTARIA_NO_RECORD = "013";
  
  //Registro Debitori
  public static final String PARAMETRO_REG_DEB_ARPEA = "REG_DEB_ARPEA";
  
  //Inizio Azienda in proprio
  public static final Integer NESSUNA_AZIENDA_CENSITA = new Integer(1);
  public static final long RICHIESTA_NUOVA_ISCRIZIONE = 1;
  public static final long RICHIESTA_NI_AZIENDA_OBSOLETA = 4;
  public static final long RICHIESTA_VALIDAZIONE = 5;
  public static final long RICHIESTA_CESSAZIONE = 3;
  public static final Long RICHIESTA_STATO_BOZZA = new Long(10);
  public static final Long RICHIESTA_STATO_STAMPATA = new Long(20);
  public static final Long RICHIESTA_STATO_FIRMA_DIGITALE = new Long(30);
  public static final Long RICHIESTA_STATO_TRASMESSA_PA = new Long(40);
  public static final Long RICHIESTA_STATO_VALIDATA = new Long(50);
  public static final Long RICHIESTA_STATO_ANNULLAMENTO = new Long(60);
  public static final String SEQ_SMRGAA_W_AZIENDA_NUOVA = "SEQ_SMRGAA_W_AZIENDA_NUOVA";
  public static final String SEQ_DB_RICHIESTA_AZIENDA = "SEQ_DB_RICHIESTA_AZIENDA";
  public static final String SEQ_DB_ITER_RICHIESTA_AZIENDA = "SEQ_DB_ITER_RICHIESTA_AZIENDA";
  public static final String SEQ_SMRGAA_W_UTE_AZIENDA_NUOVA = "SEQ_SMRGAA_W_UTE_AZIENDA_NUOVA";
  public static final String TIPO_UTILIZZO_FABBRICATO = "F";
  public static final String SEQ_SMRGAA_W_FABBR_AZ_NUOVA = "SEQ_SMRGAA_W_FABBR_AZ_NUOVA";
  public static final String SEQ_SMRGAA_W_PART_FAB_AZ_NUOVA = "SEQ_SMRGAA_W_PART_FAB_AZ_NUOVA";
  public static final String SEQ_SMRGAA_W_UTILIZZO_AZ_NUOVA = "SEQ_SMRGAA_W_UTILIZZO_AZ_NUOVA";
  public static final String SEQ_SMRGAA_W_ALLEV_AZ_NUOVA = "SEQ_SMRGAA_W_ALLEV_AZ_NUOVA";
  public static final String SEQ_SMRGAA_W_CC_AZ_NUOVA = "SEQ_SMRGAA_W_CC_AZ_NUOVA";
  public static final String SEQ_SMRGAA_W_SOGGETTI_AZ_NUOVA = "SEQ_SMRGAA_W_SOGGETTI_AZ_NUOVA";
  public static final String SEQ_SMRGAA_W_ASSOCIATE_AZ_NUOV = "SEQ_SMRGAA_W_ASSOCIATE_AZ_NUOV";
  public static final String SEQ_SMRGAA_W_SOGG_ASS_AZ_NUOVA = "SEQ_SMRGAA_W_SOGG_ASS_AZ_NUOVA";
  public static final String SEQ_DB_RICHIESTA_AZIENDA_DOC = "SEQ_DB_RICHIESTA_AZIENDA_DOC";
  public static final String SEQ_DB_ALLEGATO_RICHIESTA = "SEQ_DB_ALLEGATO_RICHIESTA";
  public static final String SEQ_DB_ALLEGATO_DICHIARAZIONE = "SEQ_DB_ALLEGATO_DICHIARAZIONE";
  public static final String SEQ_DB_MACCHINE_AZ_NUOVA = "SEQ_DB_MACCHINE_AZ_NUOVA";
  public static final String PARAMETRO_UTENTE_RICHIESTA_AZ = "UTENTE_RICHIESTA_AZ";
  public static final Integer UM_ETTARI = new Integer(1);
  public static final Integer UM_METRIQUADRI = new Integer(2);
  public static final String PARAMETRO_INVIO_MAIL_RICH_AZ = "INVIO_MAIL_RICH_AZ";
  public static final String PARAMETRO_INVIO_RICH_A_AZ = "INVIO_RICH_A_AZ";
  public static final String PARAMETRO_DEST_MAIL_RICH_AZ = "DEST_MAIL_RICH_AZ";
  public static final String PARAMETRO_MAIL_PER_RICH_AZ = "MAIL_PER_RICH_AZ";
  public static final String PARAMETRO_MAIL_RICH_ISC_A_AZ = "MAIL_RICH_ISC_A_AZ";
  public static final String PARAMETRO_MITT_MAIL_RICH_AZ = "MITT_MAIL_RICH_AZ";
  public static final String PARAMETRO_REPLY_TO_MAIL = "REPLY_TO_MAIL";
  public static final String PARAMETRO_OGG_MAIL_PER_RICH_AZ = "OGG_MAIL_PER_RICH_AZ";
  public static final String PARAMETRO_OGG_RICH_ISC_A_AZ = "OGG_RICH_ISC_A_AZ";
  public static final String PARAMETRO_OLDEST_VAL_ALLOWED = "OLDEST_VAL_ALLOWED";
  public static final String PARAMETRO_ET_UT_NAP_UTE = "ET_UT_NAP_UTE";
  public static final String PARAMETRO_ET_UT_NAP_ANA1 = "ET_UT_NAP_ANA1";
  public static final String PARAMETRO_ET_UT_NAP_ANA2 = "ET_UT_NAP_ANA2";
  public static final String PARAMETRO_ET_UT_NAP_ANA3 = "ET_UT_NAP_ANA3";
  public static final String PARAMETRO_ET_UT_NAP_TER = "ET_UT_NAP_TER";
  public static final String PARAMETRO_ET_UT_NAP_FAB = "ET_UT_NAP_FAB";
  public static final String PARAMETRO_ET_UT_NAP_ALL = "ET_UT_NAP_ALL";
  public static final String PARAMETRO_ET_UT_NAP_CC = "ET_UT_NAP_CC";
  public static final String PARAMETRO_ET_UT_NAP_DOC = "ET_UT_NAP_DOC";
  public static final String PARAMETRO_CK_ALL_OBB_NAP = "CK_ALL_OBB_NAP";
  public static final String PARAMETRO_ID_DOC_OBB_NAP = "ID_DOC_OBB_NAP";
  public static final String PARAMETRO_ET_UT_NAP_AA = "ET_UT_NAP_AA";
  public static final String PARAMETRO_PAG_FINE_NAP_1 = "PAG_FINE_NAP_1";
  public static final String PARAMETRO_PAG_FINE_NAP_2 = "PAG_FINE_NAP_2";
  public static final String PARAMETRO_MSG_NAP_ANNULLA = "MSG_NAP_ANNULLA";
  public static final String PARAMETRO_MSG_NAP_PAG1_CAA = "MSG_NAP_PAG1_CAA"; 
  public static final String PARAMETRO_MSG_NAP_PAG1_NOCAA = "MSG_NAP_PAG1_NOCAA";
  public static final String PARAMETRO_DV_AP_ANA1 = "DV_AP_ANA1";
  public static final String PARAMETRO_DV_ET_PAG1 = "DV_ET_PAG1";
  public static final String PARAMETRO_DICH_RICH_VAL_PAG = "DICH_RICH_VAL_PAG";
  public static final String PARAMETRO_DC_AP_ANA1 = "DC_AP_ANA1";
  public static final String PARAMETRO_ET_UT_NAP_SC = "ET_UT_NAP_SC";
  public static final String COD_TIPO_UTILIZZO_FABBRICATO = "157";
  public static final String COD_TIPO_VARIETA_GEN = "000";
  public static final Long COD_TIPOLOGIA_AZIENDA_PRIVATA = new Long(2);
  public static final Long COD_TIPOLOGIA_AZIENDA_ENTE = new Long(1);
  public static final Long COD_TIPO_SUB_REPORT_RAPP_LEGALE = new Long(54);
  public static final Long COD_TIPO_SUB_REPORT_STAMPA_IMG = new Long(65);
  public static final String PARAMETRO_STM_INTRO_NAP = "STM_INTRO_NAP";
  public static final String PARAMETRO_OGG_ANN_NAP = "OGG_ANN_NAP";
  public static final String PARAMETRO_TXT_ANN_NAP = "TXT_ANN_NAP";
  public static final String PARAMETRO_STM_INTRO_DV = "STM_INTRO_DV";
  public static final String PARAMETRO_DICH_RICH_VAL_STMP = "DICH_RICH_VAL_STMP";
  public static final String PARAMETRO_MAIL_PER_RICH_VAL = "MAIL_PER_RICH_VAL";
  public static final String PARAMETRO_MAIL_RICH_VAL_A_AZ = "MAIL_RICH_VAL_A_AZ";
  public static final String PARAMETRO_OGG_MAIL_RICH_VAL = "OGG_MAIL_RICH_VAL";
  public static final String PARAMETRO_OGG_RICH_VAL_A_AZ = "OGG_RICH_VAL_A_AZ";
  public static final String PARAMETRO_MAIL_PER_RICH_CESS = "MAIL_PER_RICH_CESS";
  public static final String PARAMETRO_MAIL_RICH_CESS_A_AZ = "MAIL_RICH_CESS_A_AZ";
  public static final String PARAMETRO_OGG_MAIL_RICH_CESS = "OGG_MAIL_RICH_CESS";
  public static final String PARAMETRO_OGG_RICH_CESS_A_AZ = "OGG_RICH_CESS_A_AZ";
  public static final String PARAMETRO_STM_INTRO_DC = "STM_INTRO_DC";
  public static final String PARAMETRO_OGG_VAL_RICH_VALID = "OGG_VAL_RICH_VALID";
  public static final String PARAMETRO_TXT_VAL_RICH_VALID = "TXT_VAL_RICH_VALID";
  public static final String PARAMETRO_OGG_VAL_RICH_CESS = "OGG_VAL_RICH_CESS";
  public static final String PARAMETRO_TXT_VAL_RICH_CESS = "TXT_VAL_RICH_CESS";
  public static final String PARAMETRO_STR_DICH_SOST_AN = "STR_DICH_SOST_AN";
  public static final String GESTORE_CAA = "GESTORE_CAA";
  public static final String CAA_SCRITTURA = "CAA_RW";
  public static final String ENTE_DELEGATO = "ENTE_DELEGATO";
  public static final int  RICHIESTA_VAR_IRRORATRICI = 8;
  public static final int  RICHIESTA_VAR_SOCI = 6;
  public static final int  RICHIESTA_VAR_SOGGETTI = 7;
  public static final String PARAMETRO_ET_UT_VAP_IR = "ET_UT_VAP_IR";
  public static final String PARAMETRO_ET_UT_VAP_AA = "ET_UT_VAP_AA";
  //Fine Azienda in proprio
  
  //Inzio messaggistica
  public static final  String PARAMETRO_MESSAGGI_REFRESH = "MESR";
  public static final  String PARAMETRO_MESSAGGI_ELAPSED = "MESE";
  public static final  String PARAMETRO_MESSAGGI_LOGOUT  = "MESL";
  public static final  String NAME_NOT_FOUND_EXCEPTION = "javax.naming.NameNotFoundException";
  public static final  String COMMUNICATION_EXCEPTION = "javax.naming.CommunicationException";
  public static final  String SESSION_MESSAGGI_TESTATA = "SESSION_MESSAGGI_TESTATA";
  public static final  String SESSION_ERRORI_PAGINA = "SolmrConstants.SESSION_MESSAGGIO_ERRORE";
  public static final  String OPERATION_CONFIRM = "OPERATION_CONFIRM";
  //Fine Messagistica
  
  
  //Inizio allevamenti
  public static final String SEQ_DB_ALLEVAMENTO_ACQUA_LAVAG = "SEQ_DB_ALLEVAMENTO_ACQUA_LAVAG";
  public static final String PARAMETRO_DETT_ALLEVAMENTI = "DETT_ALLEVAMENTI";
  public static final String PROPRIETARIO = "P";
  public static final String DETENTORE = "D";
  public static final String DESC_PROPRIETARIO = "Proprietario";
  public static final String DESC_DETENTORE = "Detentore";
  public static final String ORDER_BY_DESC_TIPO_SPECIE_ASC = "TSA.DESCRIZIONE ASC";
  public static final String ORDER_BY_DESC_TIPO_SPECIE_DESC = "TSA.DESCRIZIONE DESC";
  public static final String NO_BIOLOGICO_ALLEVAMENTI = "Non sono presenti indicazioni sui metodi biologici adottati";
  public static final Long  SPECIE_BOVINI_ALLEVAMENTO = new Long(1);
  public static final Long  SPECIE_BOVINI_CARNE = new Long(2);
  public static final Long  SPECIE_BUFALINI = new Long(13);
  public static final Long  SPECIE_OVINI = new Long(5);
  public static final Long  SPECIE_CAPRINI = new Long(6);
  public static final Integer ID_GRUPPO_CONTROLLO_ALLEVAMENTI = new Integer(6);
  public static final String PARAMETRO_CTRL_ALLEVAMENTI = "CTRL_ALLEVAMENTI";
  public static final String PARAMETRO_ALL_SU_GNPS = "ALL_SU_GNPS";
  // Fine costanti ALLEVAMENTI
  
  //Inzio AgriWell
  public static final String FILE_PD_AGRIWELL = "/it/csi/solmr/etc/services/pdAgriWellService.xml";
  public static final int ESITO_AGRIWELL_OK = 0;
  public static final int ESITO_AGRIWELL_KO = 1;
  public static final String NO_DOC_DEM = "Non sono presenti documenti per l'azienda";
  public static final int    NUM_RIGHE_PER_PAGINA_INDEX = 10;
  public static final int    VALIDAZIONE_ALLEGATO = 10;
  public static final int    ALLEGATI_VALIDAZIONE = 30;
  public static final String PARAMETRO_DATA_STMP_LIVECYCLE = "DATA_STMP_LIVECYCLE";
  public static final String PARAMETRO_CO_ST_FA_XS = "CO_ST_FA_XS";
  public static final String PARAMETRO_CO_ST_FA_XD = "CO_ST_FA_XD";
  public static final String PARAMETRO_CO_ST_FA_YA = "CO_ST_FA_YA";
  public static final String PARAMETRO_CO_ST_FA_YB = "CO_ST_FA_YB";
  public static final String PARAMETRO_DELAY_NEW_STAMPA = "DELAY_NEW_STAMPA";
  public static final String DA_FIRMARE = "S";
  public static final String FIRMATA_ELETTRONICAMENTE = "E";
  public static final String FIRMATA_TABLET = "N";
  public static final String FIRMATA_CARTA = "C";
  //Fine AgriWell
  
  //inizio Umaserv
  public static final String FILE_PD_UMASERV = "/it/csi/solmr/etc/services/pdUmaServ.xml";
  public static final String NO_BUONI_CARBURANTE = "Non esistono buoni carburante relativi all'azienda agricola selezionata";
  public static final String FILE_PD_UMA_SERVICE = "/it/csi/solmr/etc/uma/pdUmaService.xml";
  //Fine Umaserv
  
  //inizio macchine agricole
  public static final String PARAMETRO_SCADENZA_IMPO_MACC = "SCADENZA_IMPO_MACC";
  public static final String SEQ_DB_MACCHINA = "SEQ_DB_MACCHINA";
  public static final String SEQ_DB_NUMERO_TARGA = "SEQ_DB_NUMERO_TARGA";
  public static final String SEQ_DB_POSSESSO_MACCHINA = "SEQ_DB_POSSESSO_MACCHINA";
  public static final String PARAMETRO_MOD_MACCH_LIBERA = "MOD_MACCH_LIBERA";
  //fine macchine agricole

  
  //inizio Modolserv
  public static final String FILE_PD_MODOLSERV = "/it/csi/solmr/etc/services/pdModolsrv.xml";
  public static final String FILE_PD_MODOLPDFGENER = "/it/csi/solmr/etc/services/pdModolpdfgeneratorsrv.xml";
  public static final String CODICE_APPLICAZIONE_MODOL = "SMRANAG";
  public static final String DESCRIZIONE_APPLICAZIONE_MODOL = "SIAPSIGN - Firma grafometrica SIAP";
  //public static final String CODICE_MODULO_MODOL = "fascicolo";
  //public static final String CODICE_MODULO_MODOL_BOZZA = "fascicolo_bozza";
  //public static final String CODICE_MODELLO_MODOL = "fascicolo";
  //public static final String CODICE_MODELLO_MODOL_BOZZA = "fascicolo_bozza";
  public static final String RIF_ADOBE_MODOL = "/smranag/int-01/smrgaa/";
  //public static final String RIF_ADOBE_MODOL_BOZZA = "/smranag/int-01/smrgaa/ascicolo_bozza.xdp";
  public static final String ENCODING_MODOL = "utf-8";
  public static final Long MODOL_STAMPA_FIRMATA = new Long(1);
  public static final Long MODOL_STAMPA_NON_FIRMATA = new Long(4);
  //Fine Modolserv

  public static final String AMBIENTE_JBOSS_CSI = "J";
  public static final String AMBIENTE_JBOSS_WILDFLY = "W";
  
  
  //Mail
  /*PEC_USERNAME_BEGIN*/
  //public static final String PEC_USERNAME = "K5800989";
  /*PEC_USERNAME_END*/
  /*PEC_PASSWORD_BEGIN*/
  //public static final String PEC_PASSWORD = "8TJPAMYP";
  /*PEC_PASSWORD_END*/
  
  
  //public static final String PEC_PROTOCOL = "smtps";
  //public static final String PEC_SMTPHOST = "sendm.cert.legalmail.it";
  
  
  

}

