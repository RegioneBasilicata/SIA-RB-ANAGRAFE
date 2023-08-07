package it.csi.solmr.dto.services;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

public class CommandVO implements java.io.Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -2805879611623367965L;

  private Object[] parameterValues;
  private Class<?>[] parameterTypes;
  private String commandName;
  private String serviceName;

  /**
   * Costruisce un <code>CommandVO</code> necessario ad invocare un metodo con nome uguale a
   * <code>commandName</code> e con parametri <code>parameterValues</code> di tipo
   * <code>parameterTypes</code>. I due array <code>parameterValues</code>
   * e <code>parameterTypes</code> devono ovviamente avere la stessa dimensione.<br><br>
   * Es.: il <code>CommandVO</code> necessario ad invocare il metodo con signature
   * <code>AnagAziendaVO getAziendaById(Long idAnagraficaAzienda)</code> della classe
   * <code>it.csi.solmr.client.anag.AnagFacadeClient</code> verrà istanziato in questo modo (
   * partendo dal presupposto di avere una variabile di tipo <code>Long</code> dal nome idAzienda):<br>
   * <code>CommandVO cVO = new CommandVO("getAziendaById", new Class[] {java.lang.Long.class},
   * new Object[] {idAzienda});</code><br>
   * o, in alternativa:<br>
   * <code>CommandVO cVO = new CommandVO("getAziendaById", new Class[] {idAzienda.getClass()},
   * new Object[] {idAzienda});</code><br>
   * a patto naturalmente di avere la certezza che idAzienda sia diverso da <code>null</code>.
   * <br><br>
   * <b>N.B.:</b> nel caso di metodi che accettino in input parametri di tipo primitivo
   * (es.: <code>int</code>, <code>boolean</code>, ecc. ecc.), questi devono essere wrappati dai corrispettivi oggetti
   * (es.: <code>int</code> in <code>java.lang.Integer</code>, <code>boolean</code> in
   * <code>java.lang.Boolean</code>, ecc. ecc.).
   * @param commandName nome del metodo che si desidera invocare
   * @param parameterTypes array contenente le classi dei parametri da passare al metodo
   * @param parameterValues array contenente i valori dei parametri da passare al metodo
   */
  public CommandVO(String commandName,
		   Class<?>[] parameterTypes,
                   Object[] parameterValues) {
    setCommandName(commandName);
    setParameterTypes(parameterTypes);
    setParameterValues(parameterValues);
  }

  public void setParameterValues(Object[] parameterValues) {
    this.parameterValues = parameterValues;
  }

  public Object[] getParameterValues() {
    return parameterValues;
  }

  public String toString() {
    String stringed = "Command: ";
    stringed += commandName+"(";
    for (int i=0; i<parameterTypes.length; i++) {
      if (parameterValues[i]==null)
        stringed += "null";
      else
        stringed += parameterValues[i]+"("+parameterTypes[i].getName()+")";
      if (i!=parameterTypes.length-1)
	stringed += ", ";
    }
    stringed += ") Invoked on Service: <"+serviceName+">";
    return stringed;
  }
  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }
  public String getCommandName() {
    return commandName;
  }
  public void setCommandName(String commandName) {
    this.commandName = commandName;
  }
  public void setParameterTypes(Class<?>[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  /**
   * Valorizza il nome del servizio su cui viene invocato il <code>CommandVO</code>.
   * @param serviceName nome del servizio
   */
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * Ritorna il nome del servizio su cui viene invocato il <code>CommandVO</code>.
   * @return nome del servizio
   */
  public String getServiceName() {
    return serviceName;
  }
}