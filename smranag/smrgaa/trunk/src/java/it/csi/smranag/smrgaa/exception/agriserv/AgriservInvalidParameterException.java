package it.csi.smranag.smrgaa.exception.agriserv;

public class AgriservInvalidParameterException extends AgriservInternalException
{

  /**
   * Serial Version UID
   */
  private static final long  serialVersionUID      = -708415414163690577L;


  /**
   * Messaggio di errore generico (usato dal costruttore con un solo parametro
   * stringa)
   */
  public static final String ERROR_MESSAGE_GENERIC = "Parametri passati al servizio non validi ";

  /**
   * Nome del parametro che ha generato l'errore
   */
  public String              parameterName         = null;

  /**
   * Costruttore della classe che prende un messaggio di errore custom ed il
   * nome del parametro che non è corretto
   * 
   * @param message
   *          Messaggio specifico per il tipo di errore
   * @param parameterName
   *          Nome del parametro che non è corretto
   */
  public AgriservInvalidParameterException(String message, String parameterName)
  {
    super(message,AgriservInvalidParameterException.ERROR_CODE_INVALID_PARAMETER);
    this.parameterName = parameterName;
  }

  /**
   * Costruttore che prende in input solo il nome del parametro!<br>
   * <b>NB:</b>Il messaggio di errore è quello della costante
   * GENERIC_ERROR_MESSAGE a cui viene aggiunto il nome del parametro non è
   * valido
   * 
   * @param parameterName
   *          nome del parametro che non è valido
   */
  public AgriservInvalidParameterException(String parameterName)
  {
    super(getGenericInvalidParameterMessage(parameterName),AgriservInternalException.ERROR_CODE_INVALID_PARAMETER);
    this.parameterName = parameterName;
  }

  /**
   * Restituisce il messaggio di errore generico completo di nome del parametro
   * nel caso esso sia valorizzato
   * 
   * @param parameterName
   *          nome del parametro che non è valido
   * @return messaggio di errore
   */
  private static String getGenericInvalidParameterMessage(String parameterName)
  {
    if (parameterName == null)
    {
      return ERROR_MESSAGE_GENERIC;
    }
    else
    {
      return ERROR_MESSAGE_GENERIC + ". Nome del parametro: " + parameterName;
    }
  }

  /**
   * Restituisce il nome del parametro che ha generato l'errore
   * 
   * @return nome del parametro che è causa dell'errore
   */
  public String getParameterName()
  {
    return parameterName;
  }

}
