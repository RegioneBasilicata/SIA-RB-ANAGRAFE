package it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Contiene le informazioni di una pratica di un procedimento sugli utilizzi di
 * una certa particella (la particella è identificata nella classe
 * {@link PraticaProcedimentoVO} che al suo interno contiene un array di
 * PPUtilizzoVO. Sep 10, 2008
 * 
 * @author TOBECONFIG
 * 
 */
public class PPUtilizzoVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 8332231191091547509L;
  private Long              idUtilizzo;
  private Long              idVarieta;
  private BigDecimal        superficie;
  private String            causale;
  private String            descrizioneCausale;

  public String getDescrizioneCausale()
  {
    return descrizioneCausale;
  }

  public void setDescrizioneCausale(String descrizioneCausale)
  {
    this.descrizioneCausale = descrizioneCausale;
  }

  public String getCausale()
  {
    return causale;
  }

  public void setCausale(String causale)
  {
    this.causale = causale;
  }

  public Long getIdUtilizzo()
  {
    return idUtilizzo;
  }

  public void setIdUtilizzo(Long idUtilizzo)
  {
    this.idUtilizzo = idUtilizzo;
  }

  public BigDecimal getSuperficie()
  {
    return superficie;
  }

  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }

  public Long getIdVarieta()
  {
    return idVarieta;
  }

  public void setIdVarieta(Long idVarieta)
  {
    this.idVarieta = idVarieta;
  }
}
