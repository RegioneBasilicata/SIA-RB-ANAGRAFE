package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_PARTICELLA_BIO
 * 
 * @author TOBECONFIG
 *
 */
public class ParticellaBioVO implements Serializable 
{
	
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 7809735654810102118L;
  
  
  
  
  private long idParticellaBio;
  private long idAzienda;
  private long idParticella;
  private BigDecimal supBiologico;
  private BigDecimal supConvenzionale;
  private BigDecimal supInConversione;
  private Date dataInzioConversione;
  private Date dataFineConversione;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  
  
  
  public long getIdParticellaBio()
  {
    return idParticellaBio;
  }
  public void setIdParticellaBio(long idParticellaBio)
  {
    this.idParticellaBio = idParticellaBio;
  }
  public long getIdParticella()
  {
    return idParticella;
  }
  public void setIdParticella(long idParticella)
  {
    this.idParticella = idParticella;
  }
  public BigDecimal getSupBiologico()
  {
    return supBiologico;
  }
  public void setSupBiologico(BigDecimal supBiologico)
  {
    this.supBiologico = supBiologico;
  }
  public BigDecimal getSupConvenzionale()
  {
    return supConvenzionale;
  }
  public void setSupConvenzionale(BigDecimal supConvenzionale)
  {
    this.supConvenzionale = supConvenzionale;
  }
  public BigDecimal getSupInConversione()
  {
    return supInConversione;
  }
  public void setSupInConversione(BigDecimal supInConversione)
  {
    this.supInConversione = supInConversione;
  }
  public Date getDataInzioConversione()
  {
    return dataInzioConversione;
  }
  public void setDataInzioConversione(Date dataInzioConversione)
  {
    this.dataInzioConversione = dataInzioConversione;
  }
  public Date getDataFineConversione()
  {
    return dataFineConversione;
  }
  public void setDataFineConversione(Date dataFineConversione)
  {
    this.dataFineConversione = dataFineConversione;
  }
  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  
  
  
  
  
  
  
	
}
