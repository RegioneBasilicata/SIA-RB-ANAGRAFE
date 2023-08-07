package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

public class MacchinaAziendaNuovaVO implements Serializable
{  
 
 
  
 
  
  /**
   * 
   */
  private static final long serialVersionUID = -4813819540751586494L;
  
  
  
  
  private Long idMacchineAzNuova;
  private Long idRichiestaAzienda;
  private Long idMacchina;
  private Long idMacchinaNuova;
  private Long idUte;
  private String descUte;
  private Long idGenereMacchina;
  private String descGenereMacchina;
  private Long idCategoria;
  private String descCategoria;
  private Long idMarca;
  private String descMarca;
  private Integer annoCostruzione;
  private String matricolaTelaio;
  private String tipoMacchina;
  private Long idTipoFormaPossesso;
  private String descTipoFormaPossesso;
  private BigDecimal percentualePossesso;
  private Date dataCarico;
  private Date dataScarico;
  
  
  public Long getIdMacchineAzNuova()
  {
    return idMacchineAzNuova;
  }
  public void setIdMacchineAzNuova(Long idMacchineAzNuova)
  {
    this.idMacchineAzNuova = idMacchineAzNuova;
  }
  public Long getIdRichiestaAzienda()
  {
    return idRichiestaAzienda;
  }
  public void setIdRichiestaAzienda(Long idRichiestaAzienda)
  {
    this.idRichiestaAzienda = idRichiestaAzienda;
  }
  public Long getIdMacchina()
  {
    return idMacchina;
  }
  public void setIdMacchina(Long idMacchina)
  {
    this.idMacchina = idMacchina;
  }
  public Long getIdMacchinaNuova()
  {
    return idMacchinaNuova;
  }
  public void setIdMacchinaNuova(Long idMacchinaNuova)
  {
    this.idMacchinaNuova = idMacchinaNuova;
  }
  public Long getIdUte()
  {
    return idUte;
  }
  public void setIdUte(Long idUte)
  {
    this.idUte = idUte;
  }
  public Long getIdGenereMacchina()
  {
    return idGenereMacchina;
  }
  public void setIdGenereMacchina(Long idGenereMacchina)
  {
    this.idGenereMacchina = idGenereMacchina;
  }
  public Long getIdCategoria()
  {
    return idCategoria;
  }
  public void setIdCategoria(Long idCategoria)
  {
    this.idCategoria = idCategoria;
  }
  public Long getIdMarca()
  {
    return idMarca;
  }
  public void setIdMarca(Long idMarca)
  {
    this.idMarca = idMarca;
  }
  public Integer getAnnoCostruzione()
  {
    return annoCostruzione;
  }
  public void setAnnoCostruzione(Integer annoCostruzione)
  {
    this.annoCostruzione = annoCostruzione;
  }
  public String getMatricolaTelaio()
  {
    return matricolaTelaio;
  }
  public void setMatricolaTelaio(String matricolaTelaio)
  {
    this.matricolaTelaio = matricolaTelaio;
  }
  public String getTipoMacchina()
  {
    return tipoMacchina;
  }
  public void setTipoMacchina(String tipoMacchina)
  {
    this.tipoMacchina = tipoMacchina;
  }
  public Long getIdTipoFormaPossesso()
  {
    return idTipoFormaPossesso;
  }
  public void setIdTipoFormaPossesso(Long idTipoFormaPossesso)
  {
    this.idTipoFormaPossesso = idTipoFormaPossesso;
  }
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }
  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
  public Date getDataCarico()
  {
    return dataCarico;
  }
  public void setDataCarico(Date dataCarico)
  {
    this.dataCarico = dataCarico;
  }
  public Date getDataScarico()
  {
    return dataScarico;
  }
  public void setDataScarico(Date dataScarico)
  {
    this.dataScarico = dataScarico;
  }
  public String getDescUte()
  {
    return descUte;
  }
  public void setDescUte(String descUte)
  {
    this.descUte = descUte;
  }
  public String getDescGenereMacchina()
  {
    return descGenereMacchina;
  }
  public void setDescGenereMacchina(String descGenereMacchina)
  {
    this.descGenereMacchina = descGenereMacchina;
  }
  public String getDescCategoria()
  {
    return descCategoria;
  }
  public void setDescCategoria(String descCategoria)
  {
    this.descCategoria = descCategoria;
  }
  public String getDescMarca()
  {
    return descMarca;
  }
  public void setDescMarca(String descMarca)
  {
    this.descMarca = descMarca;
  }
  public String getDescTipoFormaPossesso()
  {
    return descTipoFormaPossesso;
  }
  public void setDescTipoFormaPossesso(String descTipoFormaPossesso)
  {
    this.descTipoFormaPossesso = descTipoFormaPossesso;
  }
  
  
  
  
  
}
