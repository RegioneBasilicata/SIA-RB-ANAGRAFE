package it.csi.smranag.smrgaa.dto.uma;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati del tipo forma possesso macchina
 * 
 * @author TOBECONFIG
 *
 */
public class TipoFormaPossessoGaaVO implements Serializable 
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 1503216111704601348L;
  
  
  
  private long idTipoFormaPossesso;
  private String codiceFormaPossesso;
  private String descrizione;
  private String abilitaPercentualePossesso;
  private Date dataInizioValidita;
  
  
  public long getIdTipoFormaPossesso()
  {
    return idTipoFormaPossesso;
  }
  public void setIdTipoFormaPossesso(long idTipoFormaPossesso)
  {
    this.idTipoFormaPossesso = idTipoFormaPossesso;
  }
  public String getCodiceFormaPossesso()
  {
    return codiceFormaPossesso;
  }
  public void setCodiceFormaPossesso(String codiceFormaPossesso)
  {
    this.codiceFormaPossesso = codiceFormaPossesso;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getAbilitaPercentualePossesso()
  {
    return abilitaPercentualePossesso;
  }
  public void setAbilitaPercentualePossesso(String abilitaPercentualePossesso)
  {
    this.abilitaPercentualePossesso = abilitaPercentualePossesso;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  
  
  
  
  
	
}
