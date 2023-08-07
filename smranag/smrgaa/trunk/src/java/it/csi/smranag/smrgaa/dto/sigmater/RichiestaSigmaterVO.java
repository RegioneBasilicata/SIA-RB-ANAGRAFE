package it.csi.smranag.smrgaa.dto.sigmater;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_RICHIESTA_SIGMATER
 * 
 * @author TOBECONFIG
 *
 */
public class RichiestaSigmaterVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3372339191798276555L;
  
  
  
  
  private Long idRichiestaSigmater;
  private String nomeServizio;
  private Long idAzienda;  
  private String codIstat;
  private String sezione;
  private Date dataUltimaRichiesta;
  private Date dataAggiornamento;
  private BigDecimal supConvenzionale;
  private BigDecimal supInConversione;
  private String codiceEsito;
  private String descrizioneEsito;
  
  
  
  
  
  
  public Long getIdRichiestaSigmater()
  {
    return idRichiestaSigmater;
  }
  public void setIdRichiestaSigmater(Long idRichiestaSigmater)
  {
    this.idRichiestaSigmater = idRichiestaSigmater;
  }
  public String getNomeServizio()
  {
    return nomeServizio;
  }
  public void setNomeServizio(String nomeServizio)
  {
    this.nomeServizio = nomeServizio;
  }
  public Long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public String getCodIstat()
  {
    return codIstat;
  }
  public void setCodIstat(String codIstat)
  {
    this.codIstat = codIstat;
  }
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public Date getDataUltimaRichiesta()
  {
    return dataUltimaRichiesta;
  }
  public void setDataUltimaRichiesta(Date dataUltimaRichiesta)
  {
    this.dataUltimaRichiesta = dataUltimaRichiesta;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
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
  public String getCodiceEsito()
  {
    return codiceEsito;
  }
  public void setCodiceEsito(String codiceEsito)
  {
    this.codiceEsito = codiceEsito;
  }
  public String getDescrizioneEsito()
  {
    return descrizioneEsito;
  }
  public void setDescrizioneEsito(String descrizioneEsito)
  {
    this.descrizioneEsito = descrizioneEsito;
  }
  
	
}
