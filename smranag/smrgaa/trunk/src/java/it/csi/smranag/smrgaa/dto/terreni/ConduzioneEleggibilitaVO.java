package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_CONDUZIONE_ELEGGIBILITA
 * 
 * @author TOBECONFIG
 *
 */
public class ConduzioneEleggibilitaVO implements Serializable 
{  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -176627500204449972L;
  
  
  
  private long idConduzioneEleggibilita;
  private BigDecimal percentualeUtilizzo;
  private long idUtenteAggiornamento;
  private long idParticella;
  private int idEleggibilitaFit;
  private long idAzienda;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  private Date dataAggiornamento;
  
  
  public long getIdConduzioneEleggibilita()
  {
    return idConduzioneEleggibilita;
  }
  public void setIdConduzioneEleggibilita(long idConduzioneEleggibilita)
  {
    this.idConduzioneEleggibilita = idConduzioneEleggibilita;
  }
  public BigDecimal getPercentualeUtilizzo()
  {
    return percentualeUtilizzo;
  }
  public void setPercentualeUtilizzo(BigDecimal percentualeUtilizzo)
  {
    this.percentualeUtilizzo = percentualeUtilizzo;
  }
  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public long getidParticella()
  {
    return idParticella;
  }
  public void setidParticella(long idParticella)
  {
    this.idParticella = idParticella;
  }
  public int getIdEleggibilitaFit()
  {
    return idEleggibilitaFit;
  }
  public void setIdEleggibilitaFit(int idEleggibilitaFit)
  {
    this.idEleggibilitaFit = idEleggibilitaFit;
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
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  
  
  
  
  
  
  
}
