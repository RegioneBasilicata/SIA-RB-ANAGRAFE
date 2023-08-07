package it.csi.smranag.smrgaa.dto.fascicoli;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 0.1
 */

public class InvioFascicoliVO implements Serializable
{ 
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3204794252765497582L;
  
  
  private Long idInvioFascicoli;
  private Long idDichiarazioneConsistenza;
  private Date dataRichiesta;
  private Date dataInvio;
  private Date dataConclusione;
  private String statoInvio;
  private String flagDatiAnagrafici;
  private String flagTerreni;
  private String flagFabbricati;
  private String flagCc;
  private String flagUv;
  private int numTentativiInvio;
  private long idUtenteAggiornamento;
  private Date dataAggiornamento;
  
  
  public Long getIdInvioFascicoli()
  {
    return idInvioFascicoli;
  }
  public void setIdInvioFascicoli(Long idInvioFascicoli)
  {
    this.idInvioFascicoli = idInvioFascicoli;
  }
  public Long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }
  public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }
  public Date getDataRichiesta()
  {
    return dataRichiesta;
  }
  public void setDataRichiesta(Date dataRichiesta)
  {
    this.dataRichiesta = dataRichiesta;
  }
  public Date getDataInvio()
  {
    return dataInvio;
  }
  public void setDataInvio(Date dataInvio)
  {
    this.dataInvio = dataInvio;
  }
  public Date getDataConclusione()
  {
    return dataConclusione;
  }
  public void setDataConclusione(Date dataConclusione)
  {
    this.dataConclusione = dataConclusione;
  }
  public String getStatoInvio()
  {
    return statoInvio;
  }
  public void setStatoInvio(String statoInvio)
  {
    this.statoInvio = statoInvio;
  }
  public String getFlagDatiAnagrafici()
  {
    return flagDatiAnagrafici;
  }
  public void setFlagDatiAnagrafici(String flagDatiAnagrafici)
  {
    this.flagDatiAnagrafici = flagDatiAnagrafici;
  }
  public String getFlagTerreni()
  {
    return flagTerreni;
  }
  public void setFlagTerreni(String flagTerreni)
  {
    this.flagTerreni = flagTerreni;
  }
  public String getFlagFabbricati()
  {
    return flagFabbricati;
  }
  public void setFlagFabbricati(String flagFabbricati)
  {
    this.flagFabbricati = flagFabbricati;
  }
  public String getFlagCc()
  {
    return flagCc;
  }
  public void setFlagCc(String flagCc)
  {
    this.flagCc = flagCc;
  }
  public String getFlagUv()
  {
    return flagUv;
  }
  public void setFlagUv(String flagUv)
  {
    this.flagUv = flagUv;
  }
  public int getNumTentativiInvio()
  {
    return numTentativiInvio;
  }
  public void setNumTentativiInvio(int numTentativiInvio)
  {
    this.numTentativiInvio = numTentativiInvio;
  }
  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  

}
