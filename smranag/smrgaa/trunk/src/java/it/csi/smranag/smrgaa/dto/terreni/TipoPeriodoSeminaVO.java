package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_PERIODO_SEMINA
 * 
 * @author TOBECONFIG
 *
 */
public class TipoPeriodoSeminaVO implements Serializable 
{  
   
  
  /**
   * 
   */
  private static final long serialVersionUID = -2017039796972740354L;
  
  
  
  private long idTipoPeriodoSemina;
  private String codice;
  private String descrizione;
  private String flagDefault;
  private Long idCatalogoMatrice;
  
  
  public long getIdTipoPeriodoSemina()
  {
    return idTipoPeriodoSemina;
  }
  public void setIdTipoPeriodoSemina(long idTipoPeriodoSemina)
  {
    this.idTipoPeriodoSemina = idTipoPeriodoSemina;
  }
  public String getCodice()
  {
    return codice;
  }
  public void setCodice(String codice)
  {
    this.codice = codice;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagDefault()
  {
    return flagDefault;
  }
  public void setFlagDefault(String flagDefault)
  {
    this.flagDefault = flagDefault;
  }
  public Long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }
  public void setIdCatalogoMatrice(Long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }
 
  
  
  
  
  
}
