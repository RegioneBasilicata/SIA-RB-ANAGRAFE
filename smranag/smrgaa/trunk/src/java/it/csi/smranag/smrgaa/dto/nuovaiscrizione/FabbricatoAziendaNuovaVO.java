package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;

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

public class FabbricatoAziendaNuovaVO implements Serializable
{  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -3061343711971949147L;
  
  
  private Long idFabbricatoAzNuova;
  private Long idUteAziendaNuova;
  private String denominazUte;
  private Long idTipologiaFabbricato;
  private String descFabbricato;
  private String unitaMisura;
  private BigDecimal lunghezza;
  private String strLunghezza;
  private BigDecimal larghezza;
  private String strLarghezza;
  private BigDecimal altezza;
  private String strAltezza;
  private Integer annoCostruzione;
  private String strAnnoCostruzione; 
  private BigDecimal dimensione;
  private String strDimensione;
  private BigDecimal superficie;
  private String strSuperficie;
  private BigDecimal superficieCoperta;
  private String strSuperficieCoperta;
  private BigDecimal superficieScoperta;
  private String strSuperficieScoperta;
  private Vector<PartFabbrAziendaNuovaVO> vPartFabbrAziendaNuova;
  TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO;
  
  
  
  public Long getIdFabbricatoAzNuova()
  {
    return idFabbricatoAzNuova;
  }
  public void setIdFabbricatoAzNuova(Long idFabbricatoAzNuova)
  {
    this.idFabbricatoAzNuova = idFabbricatoAzNuova;
  }
  public Long getIdUteAziendaNuova()
  {
    return idUteAziendaNuova;
  }
  public void setIdUteAziendaNuova(Long idUteAziendaNuova)
  {
    this.idUteAziendaNuova = idUteAziendaNuova;
  }
  public Long getIdTipologiaFabbricato()
  {
    return idTipologiaFabbricato;
  }
  public void setIdTipologiaFabbricato(Long idTipologiaFabbricato)
  {
    this.idTipologiaFabbricato = idTipologiaFabbricato;
  }
  public BigDecimal getLunghezza()
  {
    return lunghezza;
  }
  public void setLunghezza(BigDecimal lunghezza)
  {
    this.lunghezza = lunghezza;
  }
  public BigDecimal getLarghezza()
  {
    return larghezza;
  }
  public void setLarghezza(BigDecimal larghezza)
  {
    this.larghezza = larghezza;
  }
  public BigDecimal getAltezza()
  {
    return altezza;
  }
  public void setAltezza(BigDecimal altezza)
  {
    this.altezza = altezza;
  }
  public Integer getAnnoCostruzione()
  {
    return annoCostruzione;
  }
  public void setAnnoCostruzione(Integer annoCostruzione)
  {
    this.annoCostruzione = annoCostruzione;
  }
  public BigDecimal getDimensione()
  {
    return dimensione;
  }
  public void setDimensione(BigDecimal dimensione)
  {
    this.dimensione = dimensione;
  }
  public BigDecimal getSuperficieCoperta()
  {
    return superficieCoperta;
  }
  public void setSuperficieCoperta(BigDecimal superficieCoperta)
  {
    this.superficieCoperta = superficieCoperta;
  }
  public BigDecimal getSuperficieScoperta()
  {
    return superficieScoperta;
  }
  public void setSuperficieScoperta(BigDecimal superficieScoperta)
  {
    this.superficieScoperta = superficieScoperta;
  }
  public Vector<PartFabbrAziendaNuovaVO> getvPartFabbrAziendaNuova()
  {
    return vPartFabbrAziendaNuova;
  }
  public void setvPartFabbrAziendaNuova(
      Vector<PartFabbrAziendaNuovaVO> vPartFabbrAziendaNuova)
  {
    this.vPartFabbrAziendaNuova = vPartFabbrAziendaNuova;
  }
  public String getDenominazUte()
  {
    return denominazUte;
  }
  public void setDenominazUte(String denominazUte)
  {
    this.denominazUte = denominazUte;
  }
  public String getDescFabbricato()
  {
    return descFabbricato;
  }
  public void setDescFabbricato(String descFabbricato)
  {
    this.descFabbricato = descFabbricato;
  }
  public String getUnitaMisura()
  {
    return unitaMisura;
  }
  public void setUnitaMisura(String unitaMisura)
  {
    this.unitaMisura = unitaMisura;
  }
  public BigDecimal getSuperficie()
  {
    return superficie;
  }
  public void setSuperficie(BigDecimal superficie)
  {
    this.superficie = superficie;
  }
  public String getStrLunghezza()
  {
    return strLunghezza;
  }
  public void setStrLunghezza(String strLunghezza)
  {
    this.strLunghezza = strLunghezza;
  }
  public String getStrLarghezza()
  {
    return strLarghezza;
  }
  public void setStrLarghezza(String strLarghezza)
  {
    this.strLarghezza = strLarghezza;
  }
  public String getStrAltezza()
  {
    return strAltezza;
  }
  public void setStrAltezza(String strAltezza)
  {
    this.strAltezza = strAltezza;
  }
  public String getStrDimensione()
  {
    return strDimensione;
  }
  public void setStrDimensione(String strDimensione)
  {
    this.strDimensione = strDimensione;
  }
  public String getStrSuperficie()
  {
    return strSuperficie;
  }
  public void setStrSuperficie(String strSuperficie)
  {
    this.strSuperficie = strSuperficie;
  }
  public String getStrSuperficieCoperta()
  {
    return strSuperficieCoperta;
  }
  public void setStrSuperficieCoperta(String strSuperficieCoperta)
  {
    this.strSuperficieCoperta = strSuperficieCoperta;
  }
  public String getStrSuperficieScoperta()
  {
    return strSuperficieScoperta;
  }
  public void setStrSuperficieScoperta(String strSuperficieScoperta)
  {
    this.strSuperficieScoperta = strSuperficieScoperta;
  }
  public String getStrAnnoCostruzione()
  {
    return strAnnoCostruzione;
  }
  public void setStrAnnoCostruzione(String strAnnoCostruzione)
  {
    this.strAnnoCostruzione = strAnnoCostruzione;
  }
  public TipoTipologiaFabbricatoVO getTipoTipologiaFabbricatoVO()
  {
    return tipoTipologiaFabbricatoVO;
  }
  public void setTipoTipologiaFabbricatoVO(
      TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO)
  {
    this.tipoTipologiaFabbricatoVO = tipoTipologiaFabbricatoVO;
  }
  
  
  
  
  
 
  
}
