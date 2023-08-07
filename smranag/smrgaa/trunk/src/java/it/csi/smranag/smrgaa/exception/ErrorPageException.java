package it.csi.smranag.smrgaa.exception;

/**
 * Eccezione specifica che richiama la pagina di errore (errorPage500.jsp) e che
 * serve a far visualizzare il messaggio di errore senza variazioni: negli altri
 * casi viene visualizzato un pre-messaggio che invita a contattare l'assistenza
 * tecnica. Contiene anche l'informazione di quante pagine tornare indietro
 * nell'history per il pulsante indietro. Sep 29, 2008
 * 
 * @author TOBECONFIG
 * 
 */
public class ErrorPageException extends Exception
{
  /** serialVersionUID */
  private static final long serialVersionUID = -158413956704126565L;
  /*
   * valore da passare al javascript histroy.go($$gobackCount)
   */
  private int gobackCount = -1;
  private String message=null;

  /**
   * Costruttore
   * 
   * @param message
   *          Messaggio di errore
   * @param gobackCount
   *          numero di pagine di cui fare il back per il pulsante indietro
   */
  public ErrorPageException(String message, int gobackCount)
  {
    super(message);
    this.message=message;
    this.gobackCount = gobackCount;
  }

  /**
   * Costruttore
   * 
   * @param message
   *          Messaggio di errore, la proprietà gobackCount resta impostata a -1
   */
  public ErrorPageException(String message)
  {
    super(message);
    this.message=message;
    // E' implicito gobackCount==-1
  }

  public int getGobackCount()
  {
    return gobackCount;
  }

  public void setGobackCount(int gobackCount)
  {
    this.gobackCount = gobackCount;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }
}