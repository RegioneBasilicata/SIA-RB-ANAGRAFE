package it.csi.smranag.smrgaa.dto.fabbricati;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_FABBRICATO_BIO
 * 
 * @author TOBECONFIG
 *
 */
public class FabbricatoBioVO implements Serializable 
{
	
  
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -8460636600083996100L;
  
  
  
  
  
  
  private long idFabbricatoBio;
  private long idAzienda;
  private long idFabbricato;
  private BigDecimal dimensione;
  private BigDecimal dimensioneConvenzionale;
  private BigDecimal dimensioneBiologico;
  private Date dataInizioValidita;
  private Date dataFineValidita;
  
  
  
  
  
  public long getIdFabbricatoBio()
  {
    return idFabbricatoBio;
  }
  public void setIdFabbricatoBio(long idFabbricatoBio)
  {
    this.idFabbricatoBio = idFabbricatoBio;
  }
  public long getIdAzienda()
  {
    return idAzienda;
  }
  public void setIdAzienda(long idAzienda)
  {
    this.idAzienda = idAzienda;
  }
  public long getIdFabbricato()
  {
    return idFabbricato;
  }
  public void setIdFabbricato(long idFabbricato)
  {
    this.idFabbricato = idFabbricato;
  }
  public BigDecimal getDimensione()
  {
    return dimensione;
  }
  public void setDimensione(BigDecimal dimensione)
  {
    this.dimensione = dimensione;
  }
  public BigDecimal getDimensioneConvenzionale()
  {
    return dimensioneConvenzionale;
  }
  public void setDimensioneConvenzionale(BigDecimal dimensioneConvenzionale)
  {
    this.dimensioneConvenzionale = dimensioneConvenzionale;
  }
  public BigDecimal getDimensioneBiologico()
  {
    return dimensioneBiologico;
  }
  public void setDimensioneBiologico(BigDecimal dimensioneBiologico)
  {
    this.dimensioneBiologico = dimensioneBiologico;
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
