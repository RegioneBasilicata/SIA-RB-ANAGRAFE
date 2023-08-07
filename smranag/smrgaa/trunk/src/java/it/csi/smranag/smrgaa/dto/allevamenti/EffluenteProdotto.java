package it.csi.smranag.smrgaa.dto.allevamenti;

import java.io.Serializable;
import java.math.BigDecimal;

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

public class EffluenteProdotto implements Serializable
{
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 6560430261159804071L;
  
  
  
  
  private long idEffluenteProdotto;            //DB_EFFLUENTEPRODOTTO.ID_EFFLUENTE_PRODOTTO
  private long idStabulazioneTrattamento;      //DB_EFFLUENTEPRODOTTO.ID_STABULAZIONE_TRATTAMENTO
  private BigDecimal volumeProdotto;           //DB_EFFLUENTEPRODOTTO.VOLUME_PRODOTTO
  private BigDecimal azotoProdotto;            //DB_EFFLUENTEPRODOTTO.AZOTO_PRODOTTO
  private String flagPalabile;                 //DB_TIPO_EFFLUENTE.FLAG_PALABILE
  
  
  public long getIdEffluenteProdotto()
  {
    return idEffluenteProdotto;
  }
  public void setIdEffluenteProdotto(long idEffluenteProdotto)
  {
    this.idEffluenteProdotto = idEffluenteProdotto;
  }
  public long getIdStabulazioneTrattamento()
  {
    return idStabulazioneTrattamento;
  }
  public void setIdStabulazioneTrattamento(long idStabulazioneTrattamento)
  {
    this.idStabulazioneTrattamento = idStabulazioneTrattamento;
  }
  public BigDecimal getVolumeProdotto()
  {
    return volumeProdotto;
  }
  public void setVolumeProdotto(BigDecimal volumeProdotto)
  {
    this.volumeProdotto = volumeProdotto;
  }
  public BigDecimal getAzotoProdotto()
  {
    return azotoProdotto;
  }
  public void setAzotoProdotto(BigDecimal azotoProdotto)
  {
    this.azotoProdotto = azotoProdotto;
  }
  public String getFlagPalabile()
  {
    return flagPalabile;
  }
  public void setFlagPalabile(String flagPalabile)
  {
    this.flagPalabile = flagPalabile;
  }
  
  


}
