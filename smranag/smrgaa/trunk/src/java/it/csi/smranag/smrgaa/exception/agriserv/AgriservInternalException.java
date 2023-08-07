package it.csi.smranag.smrgaa.exception.agriserv;

import it.csi.csi.wrapper.UserException;

/**
 * Eccezione generica per indicare un errore interno non applicativo, ad esempio
 * problemi di accesso al database. Definisce una proprietà errorCode che indica
 * quale è il tipo di eccezione. Per i possibili valori di errorCode vedere le
 * constanti della classe che iniziano per "ERROR_CODE_".
 * 
 * @author TOBECONFIG
 * @since 1.0
 * 
 */
public class AgriservInternalException extends UserException
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 368031714889615637L;
  /**
   * Errore interno non identificato 
   */
  public static final int    ERROR_CODE_UNKNOWN    = 0x00000001;
  /**
   * Errore interno di accesso al database
   */
  public static final int    ERROR_CODE_DATABASE   = 0x00000002;
  /**
   * Errore interno non dipendente dall'applicativo (out of memory o simili)
   */
  public static final int    ERROR_CODE_SYSTEM    = 0x00000003;
  
  /* QueryTimeOutException BEGIN */
  
  /**
   * Errore interno che dipende dal timeout dell'eleaborazione della query
   */
  public static final int    ERROR_CODE_QUERY_TIMEOUT    = 0x00001001;
  
  /* QueryTimeOutException END */
  /**
   * Errore che indica che i parametri passati al servizio sono errati 
   */
  public static final int ERROR_CODE_INVALID_PARAMETER = 0x00002001; 
  
  /**
   * Errore interno di accesso al database
   */
  /**
   * Errore generico utilizzato dal costruttore
   */
  public static final String ERROR_MESSAGE_GENERIC = "Si è verificata una eccezione interna. Codice di errore: ";
  private int                errorCode;

  /**
   * Costruisce l'eccezione settando un messaggio di errore e un
   * codice di errore
   * @param message Messaggio di errore
   * @param errorCode Codice di errore
   */
  public AgriservInternalException(String message, int errorCode)
  {
    super(message);
    this.errorCode = errorCode;
  }

  /**
   * Costruisce l'eccezione utilizzando il messaggio di errore di default
   * (e accodandogli il codice di errore) e settando l'errorCode
   * @param errorCode Codice di errore
   */
  public AgriservInternalException(int errorCode)
  {
    super(ERROR_MESSAGE_GENERIC + errorCode);
    this.errorCode = errorCode;
  }

  /**
   * Restituisce il codice di errore
   * @return codice di errore
   */
  public int getErrorCode()
  {
    return errorCode;
  }
  
  
}
