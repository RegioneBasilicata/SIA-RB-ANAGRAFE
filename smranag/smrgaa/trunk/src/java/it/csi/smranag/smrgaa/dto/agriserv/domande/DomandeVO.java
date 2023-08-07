package it.csi.smranag.smrgaa.dto.agriserv.domande;

import java.io.*;

/**
 * Value object utilizzato da agriservSearchDomande
 *
 * @author TOBECONFIG
 * @since 2.5.0
 */
public class DomandeVO implements Serializable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -2688667146260244211L;

  private DomandaVO[] domande; //Array di domande corrispondenti ai parametri di input
  private boolean flagOverflow;  //True = il numero di record corrispondenti alla ricerca ha superato il numero
                                 //massimo di risultati richiesti o consentiti; i record restituiti pertanto non
                                 //sono tutti quelli corrispondenti alla ricerca
                                 //False =  il numero di record corrispondenti alla ricerca non ha superato
                                 //il numero massimo di risultati richiesti o consentiti;
                                 //i record restituiti pertanto sono tutti quelli corrispondenti alla ricerca

  public DomandeVO()
  {
  }

  public DomandaVO[] getDomande()
  {
    return domande;
  }
  public void setDomande(DomandaVO[] domande)
  {
    this.domande = domande;
  }

  public boolean isFlagOverflow()
  {
    return flagOverflow;
  }
  public void setFlagOverflow(boolean flagOverflow)
  {
    this.flagOverflow = flagOverflow;
  }
}
