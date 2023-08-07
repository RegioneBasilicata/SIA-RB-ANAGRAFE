package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;

public class TipoAllegatoVO implements Serializable
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1142050852999786996L;
  
  
  
  private int               idTipoAllegato;
  private String            codiceTipoAllegato;
  private String            descrizioneTipoAllegato;
  private Long              extIdTipoDocumentoIndex;
  private String            codiceReportAllegato;
  private String            flagDaFirmare;
  private String            nomeFileXDP;
  private String            codiceAttestazione;
  private String            flagInseribile;
  
  
  
  
  public int getIdTipoAllegato()
  {
    return idTipoAllegato;
  }
  public void setIdTipoAllegato(int idTipoAllegato)
  {
    this.idTipoAllegato = idTipoAllegato;
  }
  public String getCodiceTipoAllegato()
  {
    return codiceTipoAllegato;
  }
  public void setCodiceTipoAllegato(String codiceTipoAllegato)
  {
    this.codiceTipoAllegato = codiceTipoAllegato;
  }
  public String getDescrizioneTipoAllegato()
  {
    return descrizioneTipoAllegato;
  }
  public void setDescrizioneTipoAllegato(String descrizioneTipoAllegato)
  {
    this.descrizioneTipoAllegato = descrizioneTipoAllegato;
  }
  public Long getExtIdTipoDocumentoIndex()
  {
    return extIdTipoDocumentoIndex;
  }
  public void setExtIdTipoDocumentoIndex(Long extIdTipoDocumentoIndex)
  {
    this.extIdTipoDocumentoIndex = extIdTipoDocumentoIndex;
  }
  public String getCodiceReportAllegato()
  {
    return codiceReportAllegato;
  }
  public void setCodiceReportAllegato(String codiceReportAllegato)
  {
    this.codiceReportAllegato = codiceReportAllegato;
  }
  public String getFlagDaFirmare()
  {
    return flagDaFirmare;
  }
  public void setFlagDaFirmare(String flagDaFirmare)
  {
    this.flagDaFirmare = flagDaFirmare;
  }
  public String getNomeFileXDP()
  {
    return nomeFileXDP;
  }
  public void setNomeFileXDP(String nomeFileXDP)
  {
    this.nomeFileXDP = nomeFileXDP;
  }
  public String getCodiceAttestazione()
  {
    return codiceAttestazione;
  }
  public void setCodiceAttestazione(String codiceAttestazione)
  {
    this.codiceAttestazione = codiceAttestazione;
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
