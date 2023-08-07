package it.csi.solmr.dto;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * <br>
 *
 * Value Object per il trasporto di dati pertinenti per lo più le tabelle di look-up
 *
 * @author Luca Romanello
 * @version 1.0
 */

import java.io.Serializable;

public class StringcodeDescription implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 1650116721695968204L;

  /**
  * Il codice del record corrente
  */
  private String code        = null;
  /**
  * La descrizione del record corrente
  */
  private String  description = null;
  /**
   * Il secondo codice del record corrente
   */
  private Long secondaryCode = null;

  /**
   *
   */
  public StringcodeDescription() {
  }

  /**
   *
   * @param code il codice del record corrente
   * @param description la descrizione del record corrente
   */
  public StringcodeDescription(String code,
			       String  description) {
    setCode(code);
    this.description = description;
  }

  /**
   *
   * @return il codice del record corrente
   */
  public String getCode() {
    return this.code;
  }

  /**
   *
   * @return la descrizione del record corrente
   */
  public String getDescription() {
    return this.description;
  }

  /**
   *
   * @param code il codice del record corrente
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   *
   * @param description la descrizione del record corrente
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   *
   * @return il codice secondario del record corrente
   */
  public Long getSecondaryCode() {
    return this.secondaryCode;
  }

  /**
   *
   * @param secondaryCode Long
   */
  public void setSecondaryCode(Long secondaryCode) {
    this.secondaryCode = secondaryCode;
  }

  /**
   *
   * @return una rappresentazione del record corrente sotto forma di stringa formattata
   */
  public String toString() {
    return "Code: "+this.code+" Description: "+this.description+" Secondary code: "+this.secondaryCode;
  }

  /**
   *
   * @param o l'oggetto da confrontare con l'istanza corrente
   * @return un boolean che indica se o e l'istanza corrente si equivalgono. Ritorna false se
   *         o non è un'istanza di CodeDescription, altrimenti procede al confronto
   */
  public boolean equals(Object o) {
    if (o instanceof CodeDescription) {
      CodeDescription other = (CodeDescription)o;
      return ((other.getCode() == null && this.getCode() == null ||
	      other.getCode().equals(this.getCode())) &&
	      (other.getDescription() == null && this.getDescription() == null ||
	      other.getDescription().equals(this.getDescription())) &&
              (other.getSecondaryCode() == null && this.getSecondaryCode() == null ||
	      other.getSecondaryCode().equals(this.getSecondaryCode())));
    } else
      return false;
  }

  /**
   *
   * @return l'hashCode dell'istanza corrente
   */
  public int hashCode() {
    return this.getCode()==null?0:this.getCode().hashCode();
  }
}
