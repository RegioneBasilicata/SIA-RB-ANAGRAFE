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

public class CategorieAllevamento implements Serializable
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3202100623596937736L;
  
  
  
  
  private long idCategorieAllevamento;            //DB_CATEGORIE_ALLEVAMENTO.ID_CATEGORIE_ALLEVAMENTO
  private long idCategoriaAnimale;                //DB_CATEGORIE_ALLEVAMENTO.ID_CATEGORIA_ANIMALE
  private long idAllevamento;                     //DB_CATEGORIE_ALLEVAMENTO.ID_ALLEVAMENTO 
  private BigDecimal Quantita;                    //DB_CATEGORIE_ALLEVAMENTO.QUANTITA
  private BigDecimal pesoVivoUnitario;            //DB_CATEGORIE_ALLEVAMENTO.PESO_VIVO_UNITARIO
  
  
  
  
  
  
  
  
  public long getIdCategorieAllevamento()
  {
    return idCategorieAllevamento;
  }
  public void setIdCategorieAllevamento(long idCategorieAllevamento)
  {
    this.idCategorieAllevamento = idCategorieAllevamento;
  }
  public long getIdCategoriaAnimale()
  {
    return idCategoriaAnimale;
  }
  public void setIdCategoriaAnimale(long idCategoriaAnimale)
  {
    this.idCategoriaAnimale = idCategoriaAnimale;
  }
  public long getIdAllevamento()
  {
    return idAllevamento;
  }
  public void setIdAllevamento(long idAllevamento)
  {
    this.idAllevamento = idAllevamento;
  }
  public BigDecimal getQuantita()
  {
    return Quantita;
  }
  public void setQuantita(BigDecimal quantita)
  {
    Quantita = quantita;
  }
  public BigDecimal getPesoVivoUnitario()
  {
    return pesoVivoUnitario;
  }
  public void setPesoVivoUnitario(BigDecimal pesoVivoUnitario)
  {
    this.pesoVivoUnitario = pesoVivoUnitario;
  }

}
