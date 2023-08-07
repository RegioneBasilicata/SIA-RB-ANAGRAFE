package it.csi.smranag.smrgaa.dto.nuovaiscrizione;

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

public class StatoRichiestaVO implements Serializable
{  
 
  
  
 
  
  /**
   * 
   */
  private static final long serialVersionUID = 1137890586416691774L;
  
  
  private Integer idStatoRichiesta;
  private String descrizione;
  private Date dataInizioValidita;
  private String descrizioneEstesa;
  private String intestazionePasso;
  private String descrizionePasso;
  private String flagVisualizzaStep;
  private Integer idStatoRichiestaPrecedente;
  
  
  
  public Integer getIdStatoRichiesta()
  {
    return idStatoRichiesta;
  }
  public void setIdStatoRichiesta(Integer idStatoRichiesta)
  {
    this.idStatoRichiesta = idStatoRichiesta;
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
  public String getDescrizioneEstesa()
  {
    return descrizioneEstesa;
  }
  public void setDescrizioneEstesa(String descrizioneEstesa)
  {
    this.descrizioneEstesa = descrizioneEstesa;
  }
  public String getIntestazionePasso()
  {
    return intestazionePasso;
  }
  public void setIntestazionePasso(String intestazionePasso)
  {
    this.intestazionePasso = intestazionePasso;
  }
  public String getDescrizionePasso()
  {
    return descrizionePasso;
  }
  public void setDescrizionePasso(String descrizionePasso)
  {
    this.descrizionePasso = descrizionePasso;
  }
  public String getFlagVisualizzaStep()
  {
    return flagVisualizzaStep;
  }
  public void setFlagVisualizzaStep(String flagVisualizzaStep)
  {
    this.flagVisualizzaStep = flagVisualizzaStep;
  }
  public Integer getIdStatoRichiestaPrecedente()
  {
    return idStatoRichiestaPrecedente;
  }
  public void setIdStatoRichiestaPrecedente(Integer idStatoRichiestaPrecedente)
  {
    this.idStatoRichiestaPrecedente = idStatoRichiestaPrecedente;
  }
  
  
  
  
}
