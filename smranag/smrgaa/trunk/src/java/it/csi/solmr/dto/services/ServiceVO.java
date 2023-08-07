package it.csi.solmr.dto.services;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

public class ServiceVO implements java.io.Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 38514084539639970L;

  /**
   * Nome del servizio
   */
  private String serviceName;

  /**
   * File di configurazione della porta delegata associato al servizio nel caso di servizi
   * esterni o uno tra i valori "EJB" e "PA" nel caso di classi client del SOLMR (
   * <code>it.csi.solmr.client.anag.AnagFacadeClient</code>,
   * <code>it.csi.solmr.client.profile.ProfileClient</code>,
   * <code>it.csi.solmr.client.uma.UmaFacadeClient</code>)
   */
  private String configurationFile;

  /**
   * Tipo di servizio. I valori possibili sono:
   * <ul>
   * <li><code>ServiceVO.INTERNAL</code> - accesso alle classi client del SOLMR (
   * <code>it.csi.solmr.client.anag.AnagFacadeClient</code>,
   * <code>it.csi.solmr.client.profile.ProfileClient</code>,
   * <code>it.csi.solmr.client.uma.UmaFacadeClient</code>)</li>
   * <li><code>ServiceVO.EXTERNAL</code> - servizio esterno</li>
   * <li><code>ServiceVO.UNINITIALIZED</code> - servizio non inizializzato</li>
   * </ul>
   */
  private int serviceType = UNINITIALIZED;

  /**
   * Classe client del servizio o interfaccia CSI.
   */
  private String serviceClass;

  public static final int EXTERNAL = 1;
  public static final int INTERNAL = 0;
  public static final int UNINITIALIZED = -1;

  public ServiceVO() {
  }
  public String getServiceName() {
    return serviceName;
  }
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
  public void setServiceType(int serviceType) {
    this.serviceType = serviceType;
  }
  public int getServiceType() {
    return serviceType;
  }
  public void setConfigurationFile(String configurationFile) {
    this.configurationFile = configurationFile;
  }
  public String getConfigurationFile() {
    return configurationFile;
  }
  public void setServiceClass(String serviceClass) {
    this.serviceClass = serviceClass;
  }
  public String getServiceClass() {
    return serviceClass;
  }
  public boolean equals(Object obj) {
    if (obj instanceof ServiceVO) {
      if (obj==null)
	return false;
      ServiceVO sVO = (ServiceVO)obj;
      if (this.serviceName!=null&&sVO.serviceName!=null&&
	  this.serviceName.equals(sVO.serviceName)||
	  this.serviceName==null&&sVO.serviceName==null)
	return true;
      else
	return false;
    } else
      return false;
  }

  public String toString() {
    String stringed = "Service: ";
    stringed += "\n  Name:       "+(serviceName!=null?serviceName:"<none>");
    stringed += "\n  Class:      "+(serviceClass!=null?serviceClass:"<none>");
    stringed += "\n  Type:       "+(serviceType>0?String.valueOf(serviceType):"<none>");
    stringed += "\n  Conf. File: "+(configurationFile!=null?configurationFile:"<none>");
    return stringed;
  }
}