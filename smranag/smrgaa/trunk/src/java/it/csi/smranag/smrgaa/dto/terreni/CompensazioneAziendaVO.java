package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * della compensazione azienda
 * 
 * @author TOBECONFIG
 *
 */
public class CompensazioneAziendaVO implements Serializable 
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -3525711731424363222L;
  
  
  private Date dataCalcoloCompensazione;
  private Date dataAllineamentoCompensazione;
  private Date dataDichiarazione;
  private Date dataUltimaElaborazioneIsole;
  private Date dataConsolidamentoGis;
  private Long idIsolaDichiarata;
  private String flagCompensabile;
  
  
  
  
  public Date getDataCalcoloCompensazione()
  {
    return dataCalcoloCompensazione;
  }
  public void setDataCalcoloCompensazione(Date dataCalcoloCompensazione)
  {
    this.dataCalcoloCompensazione = dataCalcoloCompensazione;
  }
  public Date getDataAllineamentoCompensazione()
  {
    return dataAllineamentoCompensazione;
  }
  public void setDataAllineamentoCompensazione(Date dataAllineamentoCompensazione)
  {
    this.dataAllineamentoCompensazione = dataAllineamentoCompensazione;
  }
  public Date getDataDichiarazione()
  {
    return dataDichiarazione;
  }
  public void setDataDichiarazione(Date dataDichiarazione)
  {
    this.dataDichiarazione = dataDichiarazione;
  }
  public Date getDataUltimaElaborazioneIsole()
  {
    return dataUltimaElaborazioneIsole;
  }
  public void setDataUltimaElaborazioneIsole(Date dataUltimaElaborazioneIsole)
  {
    this.dataUltimaElaborazioneIsole = dataUltimaElaborazioneIsole;
  }
  public Long getIdIsolaDichiarata()
  {
    return idIsolaDichiarata;
  }
  public void setIdIsolaDichiarata(Long idIsolaDichiarata)
  {
    this.idIsolaDichiarata = idIsolaDichiarata;
  }
  public String getFlagCompensabile()
  {
    return flagCompensabile;
  }
  public void setFlagCompensabile(String flagCompensabile)
  {
    this.flagCompensabile = flagCompensabile;
  }
  public Date getDataConsolidamentoGis()
  {
    return dataConsolidamentoGis;
  }
  public void setDataConsolidamentoGis(Date dataConsolidamentoGis)
  {
    this.dataConsolidamentoGis = dataConsolidamentoGis;
  }
 
  
  
  
  
	
}
