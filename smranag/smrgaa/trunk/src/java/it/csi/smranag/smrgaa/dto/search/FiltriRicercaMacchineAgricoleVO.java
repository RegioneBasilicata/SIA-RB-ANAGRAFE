package it.csi.smranag.smrgaa.dto.search;

import java.io.Serializable;

public class FiltriRicercaMacchineAgricoleVO implements Serializable
{
 
 
  /**
   * 
   */
  private static final long serialVersionUID = 2226490031500239145L;
  
  
  
  private int               paginaCorrente;
  private Long              idGenereMacchina;
  private boolean           storico;
  private Long              idAzienda;
  private String            cuaa;
  private String            codiceRuolo;
  
  
  public int getPaginaCorrente()
  {
    return paginaCorrente;
  }
  public void setPaginaCorrente(int paginaCorrente)
  {
    this.paginaCorrente = paginaCorrente;
  }
  public Long getIdGenereMacchina()
  {
    return idGenereMacchina;
  }
  public void setIdGenereMacchina(Long idGenereMacchina)
  {
    this.idGenereMacchina = idGenereMacchina;
  }
  public boolean isStorico()
  {
    return storico;
  }
  public void setStorico(boolean storico)
  {
    this.storico = storico;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public String getCuaa()
  {
    return cuaa;
  }
  public void setCuaa(String cuaa)
  {
    this.cuaa = cuaa;
  }
  public String getCodiceRuolo()
  {
    return codiceRuolo;
  }
  public void setCodiceRuolo(String codiceRuolo)
  {
    this.codiceRuolo = codiceRuolo;
  }
  

  
  
}
