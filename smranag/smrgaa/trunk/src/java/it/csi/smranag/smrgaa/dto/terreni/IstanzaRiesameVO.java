package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati isola parcella
 * 
 * @author TOBECONFIG
 *
 */
public class IstanzaRiesameVO implements Serializable 
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2990858019153781142L;
  
  
  private Long idIstanzaRiesame;
  private Long idFaseIstanzaRiesame;
  private String descFaseIstanzaRiesame;
  private Integer anno;
  private long idAzienda;
  private long idParticella;
  private Date dataRichiesta;
  private Date dataAnnullamento;
  private Date dataEvasione;
  private Date dataAggiornamento;
  private Long idListaCampione;
  private String descListaCampione;
  private String lavorazionePrioritaria;
  private String note;
  private Date dataChiusuraIstanza;
  private Date dataSiticonvoca;
  private String protocolloIstanza;
  private Date dataInvioGis;
  private Date dataSospensioneScaduta;
  private Long idStatoSitiConvoca;
  
  
  public Long getIdIstanzaRiesame()
  {
    return idIstanzaRiesame;
  }
  public void setIdIstanzaRiesame(Long idIstanzaRiesame)
  {
    this.idIstanzaRiesame = idIstanzaRiesame;
  }
  public Long getIdFaseIstanzaRiesame()
  {
    return idFaseIstanzaRiesame;
  }
  public void setIdFaseIstanzaRiesame(Long idFaseIstanzaRiesame)
  {
    this.idFaseIstanzaRiesame = idFaseIstanzaRiesame;
  }
  public Integer getAnno()
  {
    return anno;
  }
  public void setAnno(Integer anno)
  {
    this.anno = anno;
  }
  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public long getIdParticella()
  {
    return idParticella;
  }
  public void setIdParticella(long idParticella)
  {
    this.idParticella = idParticella;
  }
  public Date getDataRichiesta()
  {
    return dataRichiesta;
  }
  public void setDataRichiesta(Date dataRichiesta)
  {
    this.dataRichiesta = dataRichiesta;
  }
  public Date getDataAnnullamento()
  {
    return dataAnnullamento;
  }
  public void setDataAnnullamento(Date dataAnnullamento)
  {
    this.dataAnnullamento = dataAnnullamento;
  }
  public Date getDataEvasione()
  {
    return dataEvasione;
  }
  public void setDataEvasione(Date dataEvasione)
  {
    this.dataEvasione = dataEvasione;
  }
  public String getDescFaseIstanzaRiesame()
  {
    return descFaseIstanzaRiesame;
  }
  public void setDescFaseIstanzaRiesame(String descFaseIstanzaRiesame)
  {
    this.descFaseIstanzaRiesame = descFaseIstanzaRiesame;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public Long getIdListaCampione()
  {
    return idListaCampione;
  }
  public void setIdListaCampione(Long idListaCampione)
  {
    this.idListaCampione = idListaCampione;
  }
  public String getDescListaCampione()
  {
    return descListaCampione;
  }
  public void setDescListaCampione(String descListaCampione)
  {
    this.descListaCampione = descListaCampione;
  }
  public String getLavorazionePrioritaria()
  {
    return lavorazionePrioritaria;
  }
  public void setLavorazionePrioritaria(String lavorazionePrioritaria)
  {
    this.lavorazionePrioritaria = lavorazionePrioritaria;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
  }
  public Date getDataChiusuraIstanza()
  {
    return dataChiusuraIstanza;
  }
  public void setDataChiusuraIstanza(Date dataChiusuraIstanza)
  {
    this.dataChiusuraIstanza = dataChiusuraIstanza;
  }
  public Date getDataSiticonvoca()
  {
    return dataSiticonvoca;
  }
  public void setDataSiticonvoca(Date dataSiticonvoca)
  {
    this.dataSiticonvoca = dataSiticonvoca;
  }
  public String getProtocolloIstanza()
  {
    return protocolloIstanza;
  }
  public void setProtocolloIstanza(String protocolloIstanza)
  {
    this.protocolloIstanza = protocolloIstanza;
  }
  public Date getDataInvioGis()
  {
    return dataInvioGis;
  }
  public void setDataInvioGis(Date dataInvioGis)
  {
    this.dataInvioGis = dataInvioGis;
  }
  public Date getDataSospensioneScaduta()
  {
    return dataSospensioneScaduta;
  }
  public void setDataSospensioneScaduta(Date dataSospensioneScaduta)
  {
    this.dataSospensioneScaduta = dataSospensioneScaduta;
  }
  public Long getIdStatoSitiConvoca()
  {
    return idStatoSitiConvoca;
  }
  public void setIdStatoSitiConvoca(Long idStatoSitiConvoca)
  {
    this.idStatoSitiConvoca = idStatoSitiConvoca;
  }
  
  
  
  
  
  
	
}
