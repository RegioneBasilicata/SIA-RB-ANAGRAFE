package it.csi.smranag.smrgaa.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO;

/**
 * Rappresenta i dati di una riga del dettaglio ricerca particella per tab
 * pratiche Oct 22, 2008
 * 
 * @author TOBECONFIG
 * 
 */

public class RigaPraticaParticellaVO implements Serializable
{
  /** serialVersionUID */
  private static final long     serialVersionUID = 1477935812561217894L;
  private String                cuaa; // CUAA
  private String                denominazione; // Denominazione
  private BigDecimal            supCatastale; // Superficie catastale
  private PraticaProcedimentoVO praticaProcedimentoVO; // VO ritornato dal servizio agriservSearchPraticheProcedimento
  private BaseCodeDescription conduzioni[];

  public BaseCodeDescription[] getConduzioni()
  {
    return conduzioni;
  }

  public void setConduzioni(BaseCodeDescription[] conduzioni)
  {
    this.conduzioni = conduzioni;
  }

  public String getCuaa()
  {
    return cuaa;
  }

  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public PraticaProcedimentoVO getPraticaProcedimentoVO()
  {
    return praticaProcedimentoVO;
  }

  public void setPraticaProcedimentoVO(
      PraticaProcedimentoVO praticaProcedimentoVO)
  {
    this.praticaProcedimentoVO = praticaProcedimentoVO;
  }

  public BigDecimal getSupCatastale()
  {
    return supCatastale;
  }

  public void setSupCatastale(BigDecimal supCatastale)
  {
    this.supCatastale = supCatastale;
  }
}
