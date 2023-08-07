package it.csi.smranag.smrgaa.dto.sigmater;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delal tabella DB_TITOLARITA_SIGMATER
 * 
 * @author TOBECONFIG
 *
 */
public class TitolaritaSigmaterVO implements Serializable 
{
	
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3795290812279287852L;
  
  
  
  
  
  private Long idTitolaritaSigmater;
  private Long idRichiestaSigmater;
  private Long idSoggettoSigmater;
  private Long idDirittoSigmater;
  private String quotaNumeratore;  
  private String quotaDenominatore;
  private String codiceRegime;
  private String descrizioneRegime;
  
  
  
  
  
  
  
  public Long getIdTitolaritaSigmater()
  {
    return idTitolaritaSigmater;
  }
  public void setIdTitolaritaSigmater(Long idTitolaritaSigmater)
  {
    this.idTitolaritaSigmater = idTitolaritaSigmater;
  }
  public Long getIdRichiestaSigmater()
  {
    return idRichiestaSigmater;
  }
  public void setIdRichiestaSigmater(Long idRichiestaSigmater)
  {
    this.idRichiestaSigmater = idRichiestaSigmater;
  }
  public Long getIdSoggettoSigmater()
  {
    return idSoggettoSigmater;
  }
  public void setIdSoggettoSigmater(Long idSoggettoSigmater)
  {
    this.idSoggettoSigmater = idSoggettoSigmater;
  }
  public Long getIdDirittoSigmater()
  {
    return idDirittoSigmater;
  }
  public void setIdDirittoSigmater(Long idDirittoSigmater)
  {
    this.idDirittoSigmater = idDirittoSigmater;
  }
  public String getQuotaNumeratore()
  {
    return quotaNumeratore;
  }
  public void setQuotaNumeratore(String quotaNumeratore)
  {
    this.quotaNumeratore = quotaNumeratore;
  }
  public String getQuotaDenominatore()
  {
    return quotaDenominatore;
  }
  public void setQuotaDenominatore(String quotaDenominatore)
  {
    this.quotaDenominatore = quotaDenominatore;
  }
  public String getCodiceRegime()
  {
    return codiceRegime;
  }
  public void setCodiceRegime(String codiceRegime)
  {
    this.codiceRegime = codiceRegime;
  }
  public String getDescrizioneRegime()
  {
    return descrizioneRegime;
  }
  public void setDescrizioneRegime(String descrizioneRegime)
  {
    this.descrizioneRegime = descrizioneRegime;
  }
  
  
  
  
  
  
}
