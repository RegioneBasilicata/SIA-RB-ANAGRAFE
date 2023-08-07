package it.csi.smranag.smrgaa.exception.agriserv;

import java.io.Serializable;
/**
 * Rappresenta l'eccezione rilanciata nel caso l'elaborazione della query 
 * superi il timeout impostato.
 * il range di errori parte da 0x00001001!
 * 
 * @author TOBECONFIG
 * @since 1.0
 */
public class AgriservQueryTimeOutException extends AgriservInternalException implements Serializable
{
  /**
   * Serial Version uid
   */
  private static final long serialVersionUID = 2321071049022796820L;
  
  
  /**
   * Numero massimo di elementi accettati in un array di input per un servizio
   */
  public static final int ORACLE_PREPARE_STATEMENT_TIME_OUT = 1013;
  
  /**
   * Numero massimo di elementi accettati in un array di input per un servizio
   */
  public static final int MIN_TIME_WAIT = 1;
  /**
   * Numero massimo di elementi accettati in un array di input per un servizio
   */
  public static final int MID_TIME_WAIT = 5;
  /**
   * Numero massimo di elementi accettati in un array di input per un servizio
   */
  public static final int MAX_TIME_WAIT = 30;
  
  
  
  /**
   * Messaggio di errore di default
   */
  public static final String DEFAULT_ERROR_MESSAGE = "Timeout superato nell'elaborazione della query";
/**
 * Costruisce una eccezione con un messaggio specifico
 * @param message messaggio dell'eccezione
 */
  public AgriservQueryTimeOutException(String message)
  {
    super(message,ERROR_CODE_QUERY_TIMEOUT);
  }

  /**
   * Costruisce l'eccezione con il messaggio di default 
   */
  public AgriservQueryTimeOutException()
  {
    super(DEFAULT_ERROR_MESSAGE,ERROR_CODE_QUERY_TIMEOUT);
  }
}
