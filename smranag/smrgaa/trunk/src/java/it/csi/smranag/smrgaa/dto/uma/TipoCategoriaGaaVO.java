package it.csi.smranag.smrgaa.dto.uma;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati del tipo categoria macchina
 * 
 * @author TOBECONFIG
 *
 */
public class TipoCategoriaGaaVO implements Serializable 
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -6712081144202445594L;
  
  
  
  private long idCategoria;
  private long idGenereMacchina;
  private String codiceCategoria;
  private String descrizione;
  private Date dataInizioValidita;
  private String flagControlliUnivocita;
  
  
  
  public long getIdCategoria()
  {
    return idCategoria;
  }
  public void setIdCategoria(long idCategoria)
  {
    this.idCategoria = idCategoria;
  }
  public long getIdGenereMacchina()
  {
    return idGenereMacchina;
  }
  public void setIdGenereMacchina(long idGenereMacchina)
  {
    this.idGenereMacchina = idGenereMacchina;
  }
  public String getCodiceCategoria()
  {
    return codiceCategoria;
  }
  public void setCodiceCategoria(String codiceCategoria)
  {
    this.codiceCategoria = codiceCategoria;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public String getFlagControlliUnivocita()
  {
    return flagControlliUnivocita;
  }
  public void setFlagControlliUnivocita(String flagControlliUnivocita)
  {
    this.flagControlliUnivocita = flagControlliUnivocita;
  }
  
  
  
  
  
  
	
}
