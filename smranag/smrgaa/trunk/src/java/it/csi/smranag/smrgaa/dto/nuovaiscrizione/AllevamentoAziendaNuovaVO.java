package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

import java.io.Serializable;

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

public class AllevamentoAziendaNuovaVO implements Serializable
{  
 
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 8481292259140025234L;
  
  
  private Long idAllevamentoAziendaNuova;
  private Long idUteAziendaNuova;
  private String denominazUte;
  private String codiceAziendaZootecnica;
  private Integer idAsl;
  private Integer idCategoriaAnimale;
  private Long numeroCapi;
  private String strNumeroCapi;
  private String note;
  private Long idSpecieAnimale;
  private String descSpecie;
  private String descCategoria;
  private String descUte;
  private String unitaMisura;
  
  
  public Long getIdAllevamentoAziendaNuova()
  {
    return idAllevamentoAziendaNuova;
  }
  public void setIdAllevamentoAziendaNuova(Long idAllevamentoAziendaNuova)
  {
    this.idAllevamentoAziendaNuova = idAllevamentoAziendaNuova;
  }
  public Long getIdUteAziendaNuova()
  {
    return idUteAziendaNuova;
  }
  public void setIdUteAziendaNuova(Long idUteAziendaNuova)
  {
    this.idUteAziendaNuova = idUteAziendaNuova;
  }
  public String getDenominazUte()
  {
    return denominazUte;
  }
  public void setDenominazUte(String denominazUte)
  {
    this.denominazUte = denominazUte;
  }
  public Integer getIdAsl()
  {
    return idAsl;
  }
  public void setIdAsl(Integer idAsl)
  {
    this.idAsl = idAsl;
  }
  public Integer getIdCategoriaAnimale()
  {
    return idCategoriaAnimale;
  }
  public void setIdCategoriaAnimale(Integer idCategoriaAnimale)
  {
    this.idCategoriaAnimale = idCategoriaAnimale;
  }
  public Long getNumeroCapi()
  {
    return numeroCapi;
  }
  public void setNumeroCapi(Long numeroCapi)
  {
    this.numeroCapi = numeroCapi;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public String getCodiceAziendaZootecnica()
  {
    return codiceAziendaZootecnica;
  }
  public void setCodiceAziendaZootecnica(String codiceAziendaZootecnica)
  {
    this.codiceAziendaZootecnica = codiceAziendaZootecnica;
  }
  public Long getIdSpecieAnimale()
  {
    return idSpecieAnimale;
  }
  public void setIdSpecieAnimale(Long idSpecieAnimale)
  {
    this.idSpecieAnimale = idSpecieAnimale;
  }
  public String getStrNumeroCapi()
  {
    return strNumeroCapi;
  }
  public void setStrNumeroCapi(String strNumeroCapi)
  {
    this.strNumeroCapi = strNumeroCapi;
  }
  public String getDescSpecie()
  {
    return descSpecie;
  }
  public void setDescSpecie(String descSpecie)
  {
    this.descSpecie = descSpecie;
  }
  public String getDescCategoria()
  {
    return descCategoria;
  }
  public void setDescCategoria(String descCategoria)
  {
    this.descCategoria = descCategoria;
  }
  public String getDescUte()
  {
    return descUte;
  }
  public void setDescUte(String descUte)
  {
    this.descUte = descUte;
  }
  public String getUnitaMisura()
  {
    return unitaMisura;
  }
  public void setUnitaMisura(String unitaMisura)
  {
    this.unitaMisura = unitaMisura;
  }
  
  
}
