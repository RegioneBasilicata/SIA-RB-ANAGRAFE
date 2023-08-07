package it.csi.solmr.dto.anag;

import java.io.Serializable;

/**
 * Qiesto VO è utilizzato solamente dai servizi di anagrafe nel caricamento
 * dei dati di AAEP. Non deve essere utilizzato all'interno di anagrafe.
 * Presenta tutti gli attributi di AnagAziendaVO dato che li eredita ed in
 * più presneta un puntatore a PersonaFisicaVO che gli permette di mantenere
 * memorizzati i dati relativi al rappresentantelegale
 * */
public class AnagAziendaAndPersonaFisicaVO extends AnagAziendaVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 3429720082405202758L;

  private PersonaFisicaVO personaFisica;

  public PersonaFisicaVO getPersonaFisica()
  {
    return personaFisica;
  }
  public void setPersonaFisica(PersonaFisicaVO personaFisica)
  {
    this.personaFisica = personaFisica;
  }
}
