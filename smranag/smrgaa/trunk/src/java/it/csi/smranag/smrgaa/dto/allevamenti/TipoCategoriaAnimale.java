package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */

public class TipoCategoriaAnimale implements Serializable
{
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -5271862524233818780L;
  
  
  
  private long idCategoriaAnimale;
  private String descrizione;
  private BigDecimal consumoAnnuoUF;
  private BigDecimal coefficienteUBA;
  private BigDecimal pesoVivoMedio;
  private BigDecimal pesoVivoMin;
  private BigDecimal pesoVivoMax;
  private Long idAttivitaInea;
  
  
  
  public long getIdCategoriaAnimale()
  {
    return idCategoriaAnimale;
  }
  public void setIdCategoriaAnimale(long idCategoriaAnimale)
  {
    this.idCategoriaAnimale = idCategoriaAnimale;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public BigDecimal getConsumoAnnuoUF()
  {
    return consumoAnnuoUF;
  }
  public void setConsumoAnnuoUF(BigDecimal consumoAnnuoUF)
  {
    this.consumoAnnuoUF = consumoAnnuoUF;
  }
  public BigDecimal getCoefficienteUBA()
  {
    return coefficienteUBA;
  }
  public void setCoefficienteUBA(BigDecimal coefficienteUBA)
  {
    this.coefficienteUBA = coefficienteUBA;
  }
  public BigDecimal getPesoVivoMedio()
  {
    return pesoVivoMedio;
  }
  public void setPesoVivoMedio(BigDecimal pesoVivoMedio)
  {
    this.pesoVivoMedio = pesoVivoMedio;
  }
  public BigDecimal getPesoVivoMin()
  {
    return pesoVivoMin;
  }
  public void setPesoVivoMin(BigDecimal pesoVivoMin)
  {
    this.pesoVivoMin = pesoVivoMin;
  }
  public BigDecimal getPesoVivoMax()
  {
    return pesoVivoMax;
  }
  public void setPesoVivoMax(BigDecimal pesoVivoMax)
  {
    this.pesoVivoMax = pesoVivoMax;
  }
  public Long getIdAttivitaInea()
  {
    return idAttivitaInea;
  }
  public void setIdAttivitaInea(Long idAttivitaInea)
  {
    this.idAttivitaInea = idAttivitaInea;
  }

}
