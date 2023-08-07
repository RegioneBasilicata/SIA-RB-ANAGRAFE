package it.csi.solmr.dto.anag.services;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author unascribed
 * @version 1.0
 */

public class DelegaAnagrafeVO implements Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */	
  static final long serialVersionUID = 5152282099336734738L;	
	
  private String idIntermediario;
  private String denominazione;
  private String codiceFiscIntermediario;
  private String dataInizioDelega;
  private String dataFineDelega;
  private String codiceUfficioZona;
  private String denominazioneUfficioZona;
  private String indirizzoUfficioZona;
  private String comuneUfficioZona;
  private String capUfficioZona;
  private String recapitoUfficioZona;
  private String flagFiglio;
  private String istatComuneUfficioZona;
  private String responsabile;
  public DelegaAnagrafeVO()
  {
  }

  public String getIdIntermediario()
  {
    return idIntermediario;
  }

  public void setIdIntermediario(String idIntermediario)
  {
    this.idIntermediario = idIntermediario;
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public String getDenominazione()
  {
    return denominazione;
  }

  public void setCodiceFiscIntermediario(String codiceFiscIntermediario)
  {
    this.codiceFiscIntermediario = codiceFiscIntermediario;
  }

  public String getCodiceFiscIntermediario()
  {
    return codiceFiscIntermediario;
  }

  public void setDataInizioDelega(String dataInizioDelega)
  {
    this.dataInizioDelega = dataInizioDelega;
  }

  public String getDataInizioDelega()
  {
    return dataInizioDelega;
  }

  public void setDataFineDelega(String dataFineDelega)
  {
    this.dataFineDelega = dataFineDelega;
  }

  public String getDataFineDelega()
  {
    return dataFineDelega;
  }

  public void setCodiceUfficioZona(String codiceUfficioZona)
  {
    this.codiceUfficioZona = codiceUfficioZona;
  }

  public String getCodiceUfficioZona()
  {
    return codiceUfficioZona;
  }

  public void setDenominazioneUfficioZona(String denominazioneUfficioZona)
  {
    this.denominazioneUfficioZona = denominazioneUfficioZona;
  }

  public String getDenominazioneUfficioZona()
  {
    return denominazioneUfficioZona;
  }

  public void setIndirizzoUfficioZona(String indirizzoUfficioZona)
  {
    this.indirizzoUfficioZona = indirizzoUfficioZona;
  }

  public String getIndirizzoUfficioZona()
  {
    return indirizzoUfficioZona;
  }

  public void setComuneUfficioZona(String comuneUfficioZona)
  {
    this.comuneUfficioZona = comuneUfficioZona;
  }

  public String getComuneUfficioZona()
  {
    return comuneUfficioZona;
  }

  public void setCapUfficioZona(String capUfficioZona)
  {
    this.capUfficioZona = capUfficioZona;
  }

  public String getCapUfficioZona()
  {
    return capUfficioZona;
  }

  public void setRecapitoUfficioZona(String recapitoUfficioZona)
  {
    this.recapitoUfficioZona = recapitoUfficioZona;
  }

  public String getRecapitoUfficioZona()
  {
    return recapitoUfficioZona;
  }

  public void setFlagFiglio(String flagFiglio)
  {
    this.flagFiglio = flagFiglio;
  }

  public String getFlagFiglio()
  {
    return flagFiglio;
  }

  public void setIstatComuneUfficioZona(String istatComuneUfficioZona)
  {
    this.istatComuneUfficioZona = istatComuneUfficioZona;
  }

  public String getIstatComuneUfficioZona()
  {
    return istatComuneUfficioZona;
  }

  public void setResponsabile(String responsabile)
  {
    this.responsabile = responsabile;
  }

  public String getResponsabile()
  {
    return responsabile;
  }
}