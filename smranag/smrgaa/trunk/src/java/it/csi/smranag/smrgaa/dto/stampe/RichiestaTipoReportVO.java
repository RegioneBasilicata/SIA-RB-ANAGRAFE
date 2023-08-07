package it.csi.smranag.smrgaa.dto.stampe;

import java.io.Serializable;
import java.math.BigDecimal;

public class RichiestaTipoReportVO implements Serializable
{
  
  
  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4391202615039662447L;
  
  
  
  private Long              idReportSubReport;
  private int               idTipoReport;
  private Long              idTipoSubReport;
  private String            quadro;
  private String            sezione;
  private boolean           saltoPaginaIniziale;
  private boolean           visibile;
  private boolean           selezionabile;
  private String            descrizioneSelezione;
  private Long              idReportSubReportPadre;
  private String            codice;
  private String            obbligatorio;
  private int               ordinamento;
  private String            testo;
  private String            note;
  private Integer           idTipoFirmatario;
  private Integer           idTipoAllegato;
  private BigDecimal        posFirmaGrafoXDestra;
  private BigDecimal        posFirmaGrafoXSinistra;
  private BigDecimal        posFirmaGrafoYAlto;
  private BigDecimal        posFirmaGrafoYBasso;
  private String            codiceFirmatario;
  
  
  
  
  public int getIdTipoReport()
  {
    return idTipoReport;
  }
  public void setIdTipoReport(int idTipoReport)
  {
    this.idTipoReport = idTipoReport;
  }
  public Long getIdTipoSubReport()
  {
    return idTipoSubReport;
  }
  public void setIdTipoSubReport(Long idTipoSubReport)
  {
    this.idTipoSubReport = idTipoSubReport;
  }
  public String getQuadro()
  {
    return quadro;
  }
  public void setQuadro(String quadro)
  {
    this.quadro = quadro;
  }
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public boolean isSaltoPaginaIniziale()
  {
    return saltoPaginaIniziale;
  }
  public void setSaltoPaginaIniziale(boolean saltoPaginaIniziale)
  {
    this.saltoPaginaIniziale = saltoPaginaIniziale;
  }
  public boolean isVisibile()
  {
    return visibile;
  }
  public void setVisibile(boolean visibile)
  {
    this.visibile = visibile;
  }
  public boolean isSelezionabile()
  {
    return selezionabile;
  }
  public void setSelezionabile(boolean selezionabile)
  {
    this.selezionabile = selezionabile;
  }
  public String getDescrizioneSelezione()
  {
    return descrizioneSelezione;
  }
  public void setDescrizioneSelezione(String descrizioneSelezione)
  {
    this.descrizioneSelezione = descrizioneSelezione;
  }
  public Long getIdReportSubReportPadre()
  {
    return idReportSubReportPadre;
  }
  public void setIdReportSubReportPadre(Long idReportSubReportPadre)
  {
    this.idReportSubReportPadre = idReportSubReportPadre;
  }
  public String getCodice()
  {
    return codice;
  }
  public void setCodice(String codice)
  {
    this.codice = codice;
  }
  public int getOrdinamento()
  {
    return ordinamento;
  }
  public void setOrdinamento(int ordinamento)
  {
    this.ordinamento = ordinamento;
  }
  public String getObbligatorio()
  {
    return obbligatorio;
  }
  public void setObbligatorio(String obbligatorio)
  {
    this.obbligatorio = obbligatorio;
  }
  public Long getIdReportSubReport()
  {
    return idReportSubReport;
  }
  public void setIdReportSubReport(Long idReportSubReport)
  {
    this.idReportSubReport = idReportSubReport;
  }
  public String getTesto()
  {
    return testo;
  }
  public void setTesto(String testo)
  {
    this.testo = testo;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public Integer getIdTipoFirmatario()
  {
    return idTipoFirmatario;
  }
  public void setIdTipoFirmatario(Integer idTipoFirmatario)
  {
    this.idTipoFirmatario = idTipoFirmatario;
  }
  public Integer getIdTipoAllegato()
  {
    return idTipoAllegato;
  }
  public void setIdTipoAllegato(Integer idTipoAllegato)
  {
    this.idTipoAllegato = idTipoAllegato;
  }
  public BigDecimal getPosFirmaGrafoXDestra()
  {
    return posFirmaGrafoXDestra;
  }
  public void setPosFirmaGrafoXDestra(BigDecimal posFirmaGrafoXDestra)
  {
    this.posFirmaGrafoXDestra = posFirmaGrafoXDestra;
  }
  public BigDecimal getPosFirmaGrafoXSinistra()
  {
    return posFirmaGrafoXSinistra;
  }
  public void setPosFirmaGrafoXSinistra(BigDecimal posFirmaGrafoXSinistra)
  {
    this.posFirmaGrafoXSinistra = posFirmaGrafoXSinistra;
  }
  public BigDecimal getPosFirmaGrafoYAlto()
  {
    return posFirmaGrafoYAlto;
  }
  public void setPosFirmaGrafoYAlto(BigDecimal posFirmaGrafoYAlto)
  {
    this.posFirmaGrafoYAlto = posFirmaGrafoYAlto;
  }
  public BigDecimal getPosFirmaGrafoYBasso()
  {
    return posFirmaGrafoYBasso;
  }
  public void setPosFirmaGrafoYBasso(BigDecimal posFirmaGrafoYBasso)
  {
    this.posFirmaGrafoYBasso = posFirmaGrafoYBasso;
  }
  public String getCodiceFirmatario()
  {
    return codiceFirmatario;
  }
  public void setCodiceFirmatario(String codiceFirmatario)
  {
    this.codiceFirmatario = codiceFirmatario;
  }

  
}
