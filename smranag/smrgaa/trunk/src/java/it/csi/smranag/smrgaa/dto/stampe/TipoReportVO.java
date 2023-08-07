package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;

public class TipoReportVO implements Serializable
{
  
  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2400527236086842075L;
  
  
  
  
  private int               idTipoReport;
  private String            codiceReport;
  private String            descrizione;
  private String            flagSceltaPropietari;
  private String            nomeFileXDP;
  private Long              idTipoAllegato;
  private String            flagInseribile;
  
  
  public int getIdTipoReport()
  {
    return idTipoReport;
  }
  public void setIdTipoReport(int idTipoReport)
  {
    this.idTipoReport = idTipoReport;
  }
  public String getCodiceReport()
  {
    return codiceReport;
  }
  public void setCodiceReport(String codiceReport)
  {
    this.codiceReport = codiceReport;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagSceltaPropietari()
  {
    return flagSceltaPropietari;
  }
  public void setFlagSceltaPropietari(String flagSceltaPropietari)
  {
    this.flagSceltaPropietari = flagSceltaPropietari;
  }
  public String getNomeFileXDP()
  {
    return nomeFileXDP;
  }
  public void setNomeFileXDP(String nomeFileXDP)
  {
    this.nomeFileXDP = nomeFileXDP;
  }
  public Long getIdTipoAllegato()
  {
    return idTipoAllegato;
  }
  public void setIdTipoAllegato(Long idTipoAllegato)
  {
    this.idTipoAllegato = idTipoAllegato;
  }
  public String getFlagInseribile()
  {
    return flagInseribile;
  }
  public void setFlagInseribile(String flagInseribile)
  {
    this.flagInseribile = flagInseribile;
  }
  
  
  
  
  

  
}
