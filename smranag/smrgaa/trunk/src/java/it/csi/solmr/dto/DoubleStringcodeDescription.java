package it.csi.solmr.dto;

import java.io.Serializable;

public class DoubleStringcodeDescription implements Serializable, Comparable<Object>
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 6154649407202637056L;

  private String firstCode        = null;
  private String  firstDescription = null;
  private String secondCode;
  private String secondDescription;

  public DoubleStringcodeDescription() {
  }

  public String getFirstCode() {
    return firstCode;
  }

  public String getFirstDescription() {
    return firstDescription;
  }

  public void setFirstCode(String firstCode) {
    this.firstCode = firstCode;
  }

  public void setFirstDescription(String firstDescription) {
    this.firstDescription = firstDescription;
  }

  public String toString() {
    return "First Code: "+this.firstCode+" Description: "+this.firstDescription;
  }

  public boolean equals(Object o) {
    if (o instanceof DoubleStringcodeDescription) {
      DoubleStringcodeDescription other = (DoubleStringcodeDescription)o;
      return ((other.getFirstCode() == null && this.getFirstCode() == null ||
	      other.getFirstCode().equals(this.getFirstCode()))&&
	      (other.getSecondCode() == null && this.getSecondCode() == null ||
	      other.getSecondCode().equals(this.getSecondCode())));
    } else
      return false;
  }

  public int hashCode() {
    return this.getFirstCode()==null?0:this.getFirstCode().hashCode()+
	   this.getSecondCode()==null?0:this.getSecondCode().hashCode();
  }

  public int compareTo(Object obj) {
    DoubleStringcodeDescription code = (DoubleStringcodeDescription)obj;
    int result = this.getFirstDescription().compareTo(code.getFirstDescription());
    return result;
  }


  public void setSecondCode(String secondCode) {
    this.secondCode = secondCode;
  }
  public String getSecondCode() {
    return secondCode;
  }
  public void setSecondDescription(String secondDescription) {
    this.secondDescription = secondDescription;
  }
  public String getSecondDescription() {
    return secondDescription;
  }
}
