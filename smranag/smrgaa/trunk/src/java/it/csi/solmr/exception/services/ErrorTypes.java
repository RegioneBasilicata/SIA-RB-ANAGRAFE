package it.csi.solmr.exception.services;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG & TOBECONFIG
 * @version 1.0
 *
 * Questa classe contiene le costanti dei tipi di errori contenuti nelle
 * eccezioni restituite dai servizi che l'anagrafe espone tramite soap al
 * mondo esterno
 */

public class ErrorTypes
{
  public final static int NUM_MAX_RECORD = 500;
  /*Errore inaspettato */
  public final static int UNKNOWN_ERROR = -1;
  public final static String STR_UNKNOWN_ERROR = "Errore imprevisto";

  /*Uno o più parametri passati al servizio non sono validi */
  public final static int INVALID_PARAMETER = 1;
  public final static String STR_INVALID_PARAMETER = "I parametri passati non sono validi";

  /*Il numero di elementi restituiti da una ricerca ha superato il limite
  massimo: rieseguire la ricerca con ulteriori filtri*/
  public final static int MAX_RECORD = 10;
  public final static String STR_MAX_RECORD = "Il numero di elementi restituiti dalla ricerca ha superato il limite massimo";

  public final static int SERVICE_AAEP_EXCEPTION = 20;
  public final static String STR_SERVICE_AAEP_EXCEPTION = "Servizio Infocamere attualmente non disponibile";

  public final static int SERVICE_SIAN_EXCEPTION = 40;
  public final static String STR_SERVICE_SIAN_EXCEPTION = "Servizio di consultazione SIAN momentaneamente non disponibile";

  /*Nessun elemento trovato*/
  public final static int NO_RECORD = 30;
  public final static String STR_NO_RECORD = "Non è stato trovato nessun record";

  public final static int CODE_SERVICE_COMUNE_EXCEPTION = 2;
  public final static String STR_SERVICE_COMUNE_EXCEPTION = "Servizio COMUNE attualmente non disponibile";
  public final static int CODE_SERVICE_SIGLA_AMMINISTRAZIONE_EXCEPTION = 3;
  public final static String STR_SERVICE_SIGLA_AMMINISTRAZIONE_EXCEPTION = "Impossibile calcolare il numero repertorio: non è stata reperita la sigla amministrazione dell'utente collegato";
  public final static int INVALID_PARAMETER_DOCUMENTALE = 4;
  public final static int INVALID_PARAMETER_TIPO_DOCUMENTO = 5;
  public final static String STR_INVALID_PARAMETER_TIPO_DOCUMENTO = "Il tipo documento indicato risulta inesistente o scaduto";
  public final static int INVALID_PARAMETER_DOCUMENTO_CENSITO = 6;
  public final static String STR_INVALID_PARAMETER_DOCUMENTO_CENSITO = "Il documento indicato risultà già censito in anagrafe";
  

  public final static String STR_SERVICE_CAMPO_OBBLIGATORIO = "Campo obbligatorio";
  public final static String STR_SERVICE_OUTPUT_INASPETTATO = "L'oggetto ritornato dal servizio non è del tipo atteso";
  public final static String STR_SERVICE_OUTPUT_NULLO = "L'oggetto ritornato dal servizio è nullo";
  public final static String STR_SERVICE_LUNGHEZZA_CAMPO = "La lunghezza del campo è errata";
  public final static String STR_SERVICE_CAMPO_NUMERICO_INTERO = "Il campo non contiene un numero intero";
  public final static String STR_SERVICE_FORMATO_NUMERICO_DECIMALE = "Il numero non ha un numero di cifre intere o decimali corretto";
  public final static String STR_SERVICE_FORMATO_STRINGA = "Il formato della stringa non è corretto";
  public final static String STR_SERVICE_PARAMETRO_OBBLIGATORIO = "Parametro obbligatorio";

}