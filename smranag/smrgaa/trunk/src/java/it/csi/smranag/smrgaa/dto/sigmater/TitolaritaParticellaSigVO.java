package it.csi.smranag.smrgaa.dto.sigmater;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_TITOLARITA_PARTICELLA_SIG
 * 
 * @author TOBECONFIG
 *
 */
public class TitolaritaParticellaSigVO implements Serializable 
{ 
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -1439003952954345020L;
  
  
  
  
  private long idTitolaritaParticellaSig;
  private long idTitolaritaSigmater;
  private long idImmobileSigmater;
  private long idParticellaSigmater;
  
  
  
  
  
  
  
  
  public long getIdTitolaritaParticellaSig()
  {
    return idTitolaritaParticellaSig;
  }
  public void setIdTitolaritaParticellaSig(long idTitolaritaParticellaSig)
  {
    this.idTitolaritaParticellaSig = idTitolaritaParticellaSig;
  }
  public long getIdTitolaritaSigmater()
  {
    return idTitolaritaSigmater;
  }
  public void setIdTitolaritaSigmater(long idTitolaritaSigmater)
  {
    this.idTitolaritaSigmater = idTitolaritaSigmater;
  }
  public long getIdImmobileSigmater()
  {
    return idImmobileSigmater;
  }
  public void setIdImmobileSigmater(long idImmobileSigmater)
  {
    this.idImmobileSigmater = idImmobileSigmater;
  }
  public long getIdParticellaSigmater()
  {
    return idParticellaSigmater;
  }
  public void setIdParticellaSigmater(long idParticellaSigmater)
  {
    this.idParticellaSigmater = idParticellaSigmater;
  }
  
  
  
  
  
  
}
