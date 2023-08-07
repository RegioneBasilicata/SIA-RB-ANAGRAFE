package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value Object per contenere coppie di id/descrizione Oct 22, 2008
 * 
 * @author TOBECONFIG (Matr. 71646)
 */
public class PlSqlCalcoloOteVO implements Serializable
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4859327831080919988L;
  
  
  
  private BigDecimal rls;
  private BigDecimal ulu;
  private Long idUde;
  private Long classeUde;
  private Long idAttivitaOte;
  private long codeResult;
  private String descError;
  private String descOte;
  private String codiceOte;
  
  
  public String getDescOte()
  {
    return descOte;
  }
  public void setDescOte(String descOte)
  {
    this.descOte = descOte;
  }
  public String getCodiceOte()
  {
    return codiceOte;
  }
  public void setCodiceOte(String codiceOte)
  {
    this.codiceOte = codiceOte;
  }
  public BigDecimal getRls()
  {
    return rls;
  }
  public void setRls(BigDecimal rls)
  {
    this.rls = rls;
  }
  public BigDecimal getUlu()
  {
    return ulu;
  }
  public void setUlu(BigDecimal ulu)
  {
    this.ulu = ulu;
  }
  public Long getIdUde()
  {
    return idUde;
  }
  public void setIdUde(Long idUde)
  {
    this.idUde = idUde;
  }
  
  public Long getIdAttivitaOte()
  {
    return idAttivitaOte;
  }
  public void setIdAttivitaOte(Long idAttivitaOte)
  {
    this.idAttivitaOte = idAttivitaOte;
  }
  public long getCodeResult()
  {
    return codeResult;
  }
  public void setCodeResult(long codeResult)
  {
    this.codeResult = codeResult;
  }
  public String getDescError()
  {
    return descError;
  }
  public void setDescError(String descError)
  {
    this.descError = descError;
  }
  public Long getClasseUde()
  {
    return classeUde;
  }
  public void setClasseUde(Long classeUde)
  {
    this.classeUde = classeUde;
  }
 
}
