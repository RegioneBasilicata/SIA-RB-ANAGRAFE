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

public class CodeDescr implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = -8129265727489273009L;

  /**
  * Il codice del record corrente
  */
  private Integer code        = null;
  /**
  * La descrizione del record corrente
  */
  private String description = null;
  private String secondaryCode;
  private String codeFlag;

  /**
   *
   */
  public CodeDescr() {
  }

  /**
   *
   * @param code il codice del record corrente
   * @param description la descrizione del record corrente
   */
  public CodeDescr(Integer code,
			 String  description) {
    setCode(code);
    this.description = description;
  }

  /**
   *
   * @return il codice del record corrente
   */
  public Integer getCode() {
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
  public void setCode(Integer code) {
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
   * @return una rappresentazione del record corrente sotto forma di stringa formattata
   */
  public String toString() {
    return "Code: "+this.code+" Description: "+this.description;
  }

  /**
   *
   * @param o l'oggetto da confrontare con l'istanza corrente
   * @return un boolean che indica se o e l'istanza corrente si equivalgono. Ritorna false se
   *         o non è un'istanza di CodeDescr, altrimenti procede al confronto
   */
  public boolean equals(Object o) {
    if (o instanceof CodeDescr) {
      CodeDescr other = (CodeDescr)o;
      return ((other.getCode() == null && this.getCode() == null ||
	      other.getCode().equals(this.getCode())) &&
	      (other.getDescription() == null && this.getDescription() == null ||
	      other.getDescription().equals(this.getDescription())));
    } else
      return false;
  }


  public boolean confronta(CodeDescr other)
  {
    if (other==null) return false;
    //if (this.getCode()!=other.getCode()&&!(this.getCode()!=null^other.getCode()!=null)&&this.getCode().compareTo(other.getCode())!=0)
    if (this.getCode()!=other.getCode()&&((this.getCode()!=null^other.getCode()!=null)||this.getCode().compareTo(other.getCode())!=0))
      return false;

    //if (this.getDescription()!=other.getDescription()&&!(this.getDescription()!=null^other.getDescription()!=null)&&this.getDescription().compareTo(other.getDescription())!=0)
    if (this.getDescription()!=other.getDescription()&&((this.getDescription()!=null^other.getDescription()!=null)||this.getDescription().compareTo(other.getDescription())!=0))
      return false;

    return true;
  }
  
  public boolean confrontaSenzaDescrizione(CodeDescr other)
  {
    if (other==null) return false;
    if (this.getCode()!=other.getCode()&&((this.getCode()!=null^other.getCode()!=null)||this.getCode().compareTo(other.getCode())!=0))
      return false;

    return true;
  }



  /**
   *
   * @return l'hashCode dell'istanza corrente
   */
  public int hashCode() {
    return this.getCode()==null?0:this.getCode().hashCode();
  }
  public void setSecondaryCode(String secondaryCode) {
    this.secondaryCode = secondaryCode;
  }
  public String getSecondaryCode() {
    return secondaryCode;
  }
  public String getCodeFlag() {
    return codeFlag;
  }
  public void setCodeFlag(String codeFlag) {
    this.codeFlag = codeFlag;
  }
}
