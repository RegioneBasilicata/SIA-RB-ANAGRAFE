package it.csi.smranag.smrgaa.dto.comunicazione10R;

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

public class RefluoEffluenteVO implements Serializable
{
  

  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 4324297051266861462L;
  
  
  
  private long idEffluente10R;                                // DB_EFFLUENTE_10R.ID_EFFLUENTE_10R
  private long idComunicazione10R;                            // DB_EFFLUENTE_10R.ID_COMUNICAZIONE_10R
  private long idEffluente;                                   // DB_EFFLUENTE_10R.ID_EFFLUENTE
  private String tipoEffluente;                               // DB_TIPO_EFFLUENTE.FLAG_PALABILE (palabile/non palabile)
  private String descrizione;                                 // DB_TIPO_EFFLUENTE.DESCRIZIONE
  private BigDecimal volumePostTrattamentoCalc;                   
  private BigDecimal azotoPostTrattamentoCalc;                
  private BigDecimal volumePostTrattamentoDich;               
  private BigDecimal azotoPostTrattamentoDich;
  private BigDecimal volumeEffProdStalla;
  private BigDecimal azotoEffProdStalla;
  private BigDecimal volumeAcquisito;
  private BigDecimal azotoAcquisito;
  private BigDecimal volumeCeduto;
  private BigDecimal azotoCeduto;
  private BigDecimal volumeUtilizzoAgronomico;
  private BigDecimal azotoUtilizzoAgronomico;
  private Long idTrattamento;
  private BigDecimal volumeIniziale;
  private BigDecimal azotoIniziale;
  private BigDecimal volumeTrattato;
  private BigDecimal azotoTrattato;
  private String comuneUte;
  private String sglProvUte;
  private String indirizzoUte;
  
  
  
  
  public long getIdEffluente10R()
  {
    return idEffluente10R;
  }
  public void setIdEffluente10R(long idEffluente10R)
  {
    this.idEffluente10R = idEffluente10R;
  }
  public long getIdComunicazione10R()
  {
    return idComunicazione10R;
  }
  public void setIdComunicazione10R(long idComunicazione10R)
  {
    this.idComunicazione10R = idComunicazione10R;
  }
  public long getIdEffluente()
  {
    return idEffluente;
  }
  public void setIdEffluente(long idEffluente)
  {
    this.idEffluente = idEffluente;
  }
  public String getTipoEffluente()
  {
    return tipoEffluente;
  }
  public void setTipoEffluente(String tipoEffluente)
  {
    this.tipoEffluente = tipoEffluente;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public BigDecimal getVolumePostTrattamentoCalc()
  {
    return volumePostTrattamentoCalc;
  }
  public void setVolumePostTrattamentoCalc(BigDecimal volumePostTrattamentoCalc)
  {
    this.volumePostTrattamentoCalc = volumePostTrattamentoCalc;
  }
  public BigDecimal getAzotoPostTrattamentoCalc()
  {
    return azotoPostTrattamentoCalc;
  }
  public void setAzotoPostTrattamentoCalc(BigDecimal azotoPostTrattamentoCalc)
  {
    this.azotoPostTrattamentoCalc = azotoPostTrattamentoCalc;
  }
  public BigDecimal getVolumePostTrattamentoDich()
  {
    return volumePostTrattamentoDich;
  }
  public void setVolumePostTrattamentoDich(BigDecimal volumePostTrattamentoDich)
  {
    this.volumePostTrattamentoDich = volumePostTrattamentoDich;
  }
  public BigDecimal getAzotoPostTrattamentoDich()
  {
    return azotoPostTrattamentoDich;
  }
  public void setAzotoPostTrattamentoDich(BigDecimal azotoPostTrattamentoDich)
  {
    this.azotoPostTrattamentoDich = azotoPostTrattamentoDich;
  }
  public BigDecimal getVolumeEffProdStalla()
  {
    return volumeEffProdStalla;
  }
  public void setVolumeEffProdStalla(BigDecimal volumeEffProdStalla)
  {
    this.volumeEffProdStalla = volumeEffProdStalla;
  }
  public BigDecimal getAzotoEffProdStalla()
  {
    return azotoEffProdStalla;
  }
  public void setAzotoEffProdStalla(BigDecimal azotoEffProdStalla)
  {
    this.azotoEffProdStalla = azotoEffProdStalla;
  }
  public BigDecimal getVolumeAcquisito()
  {
    return volumeAcquisito;
  }
  public void setVolumeAcquisito(BigDecimal volumeAcquisito)
  {
    this.volumeAcquisito = volumeAcquisito;
  }
  public BigDecimal getAzotoAcquisito()
  {
    return azotoAcquisito;
  }
  public void setAzotoAcquisito(BigDecimal azotoAcquisito)
  {
    this.azotoAcquisito = azotoAcquisito;
  }
  public BigDecimal getVolumeCeduto()
  {
    return volumeCeduto;
  }
  public void setVolumeCeduto(BigDecimal volumeCeduto)
  {
    this.volumeCeduto = volumeCeduto;
  }
  public BigDecimal getAzotoCeduto()
  {
    return azotoCeduto;
  }
  public void setAzotoCeduto(BigDecimal azotoCeduto)
  {
    this.azotoCeduto = azotoCeduto;
  }
  public BigDecimal getVolumeUtilizzoAgronomico()
  {
    return volumeUtilizzoAgronomico;
  }
  public void setVolumeUtilizzoAgronomico(BigDecimal volumeUtilizzoAgronomico)
  {
    this.volumeUtilizzoAgronomico = volumeUtilizzoAgronomico;
  }
  public BigDecimal getAzotoUtilizzoAgronomico()
  {
    return azotoUtilizzoAgronomico;
  }
  public void setAzotoUtilizzoAgronomico(BigDecimal azotoUtilizzoAgronomico)
  {
    this.azotoUtilizzoAgronomico = azotoUtilizzoAgronomico;
  }
  public Long getIdTrattamento()
  {
    return idTrattamento;
  }
  public void setIdTrattamento(Long idTrattamento)
  {
    this.idTrattamento = idTrattamento;
  }
  public BigDecimal getVolumeIniziale()
  {
    return volumeIniziale;
  }
  public void setVolumeIniziale(BigDecimal volumeIniziale)
  {
    this.volumeIniziale = volumeIniziale;
  }
  public BigDecimal getAzotoIniziale()
  {
    return azotoIniziale;
  }
  public void setAzotoIniziale(BigDecimal azotoIniziale)
  {
    this.azotoIniziale = azotoIniziale;
  }
  public BigDecimal getVolumeTrattato()
  {
    return volumeTrattato;
  }
  public void setVolumeTrattato(BigDecimal volumeTrattato)
  {
    this.volumeTrattato = volumeTrattato;
  }
  public BigDecimal getAzotoTrattato()
  {
    return azotoTrattato;
  }
  public void setAzotoTrattato(BigDecimal azotoTrattato)
  {
    this.azotoTrattato = azotoTrattato;
  }
  public String getComuneUte()
  {
    return comuneUte;
  }
  public void setComuneUte(String comuneUte)
  {
    this.comuneUte = comuneUte;
  }
  public String getSglProvUte()
  {
    return sglProvUte;
  }
  public void setSglProvUte(String sglProvUte)
  {
    this.sglProvUte = sglProvUte;
  }
  public String getIndirizzoUte()
  {
    return indirizzoUte;
  }
  public void setIndirizzoUte(String indirizzoUte)
  {
    this.indirizzoUte = indirizzoUte;
  }
  
  
  
  
  
  
  
  

}
