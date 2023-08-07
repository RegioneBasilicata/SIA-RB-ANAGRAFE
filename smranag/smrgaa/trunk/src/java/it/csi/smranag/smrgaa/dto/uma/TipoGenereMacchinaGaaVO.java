package it.csi.smranag.smrgaa.dto.uma;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati del genere macchina
 * 
 * @author TOBECONFIG
 *
 */
public class TipoGenereMacchinaGaaVO implements Serializable 
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -8691780046713786016L;
  
  
  
  private long idGenereMacchina;
  private long idTipoMacchina;
  private String codiceGenere;
  private String descrizione;
  private String flagMatrice;
  private String flagImportabile;
  private Date dataInizioValidita;
  private TipoMacchinaGaaVO tipoMacchinaVO;
  
  
  
  
  public long getIdGenereMacchina()
  {
    return idGenereMacchina;
  }
  public void setIdGenereMacchina(long idGenereMacchina)
  {
    this.idGenereMacchina = idGenereMacchina;
  }
  public long getIdTipoMacchina()
  {
    return idTipoMacchina;
  }
  public void setIdTipoMacchina(long idTipoMacchina)
  {
    this.idTipoMacchina = idTipoMacchina;
  }
  public String getCodiceGenere()
  {
    return codiceGenere;
  }
  public void setCodiceGenere(String codiceGenere)
  {
    this.codiceGenere = codiceGenere;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagMatrice()
  {
    return flagMatrice;
  }
  public void setFlagMatrice(String flagMatrice)
  {
    this.flagMatrice = flagMatrice;
  }
  public String getFlagImportabile()
  {
    return flagImportabile;
  }
  public void setFlagImportabile(String flagImportabile)
  {
    this.flagImportabile = flagImportabile;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public TipoMacchinaGaaVO getTipoMacchinaVO()
  {
    return tipoMacchinaVO;
  }
  public void setTipoMacchinaVO(TipoMacchinaGaaVO tipoMacchinaVO)
  {
    this.tipoMacchinaVO = tipoMacchinaVO;
  }
  
 
  
  
  
	
}
