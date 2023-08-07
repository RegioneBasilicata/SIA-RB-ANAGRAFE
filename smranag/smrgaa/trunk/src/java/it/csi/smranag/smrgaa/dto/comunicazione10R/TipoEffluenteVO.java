package it.csi.smranag.smrgaa.dto.comunicazione10R;

import java.io.Serializable;
import java.math.BigDecimal;
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

public class TipoEffluenteVO implements Serializable
{
  

  /**
   * 
   */
  private static final long serialVersionUID = -6228018906973465035L;
  
  
  private long idEffluente;                                // DB_TIPO_EFFLUENTE.ID_EFFLUENTE
  private String descrizione;                              // DB_TIPO_EFFLUENTE.DESCRIZIONE
  private Date dataInizioValidita;                         // DB_TIPO_EFFLUENTE.DATA_INIZIO_VALIDITA
  private Date dataFineValidita;                           // DB_TIPO_EFFLUENTE.DATA_FINE_VALIDITA
  private String flagPalabile;                             // DB_TIPO_EFFLUENTE.FLAG_PALABILE
  private BigDecimal volumeProdotto;
  private BigDecimal azotoProdotto;
  
  
  
  public long getIdEffluente()
  {
    return idEffluente;
  }
  public void setIdEffluente(long idEffluente)
  {
    this.idEffluente = idEffluente;
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
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
  public String getFlagPalabile()
  {
    return flagPalabile;
  }
  public void setFlagPalabile(String flagPalabile)
  {
    this.flagPalabile = flagPalabile;
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
  
  
  public static TipoEffluenteVO copyNewTipoEffluente(TipoEffluenteVO tipoOldVO)
  {
    TipoEffluenteVO tipoNewVO = new TipoEffluenteVO();
    tipoNewVO.setIdEffluente(tipoOldVO.getIdEffluente());
    tipoNewVO.setDescrizione(tipoOldVO.getDescrizione());
    tipoNewVO.setDataInizioValidita(tipoOldVO.getDataInizioValidita());
    tipoNewVO.setFlagPalabile(tipoOldVO.getFlagPalabile());
    tipoNewVO.setVolumeProdotto(tipoOldVO.getVolumeProdotto());
    tipoNewVO.setAzotoProdotto(tipoOldVO.getAzotoProdotto());
    
    return tipoNewVO;
  }
  

  

}
