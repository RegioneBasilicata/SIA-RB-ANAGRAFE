package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_TIPO_EFA
 * 
 * @author TOBECONFIG
 *
 */
public class TipoEfaVO implements Serializable 
{  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7725262150464164263L;
  
  
  
  private long idTipoEfa;
  private String codiceTipoEfa;
  private String descrizioneTipoEfa;
  private String descrizioneEstesaTipoEfa;
  private Integer idUnitaMisura;
  private BigDecimal fattoreDiConversione;
  private BigDecimal fattoreDiPonderazione;
  private Long idTipoEfaPadre;
  private String descUnitaMisura;
  private boolean padre;
  
  
  
  public long getIdTipoEfa()
  {
    return idTipoEfa;
  }
  public void setIdTipoEfa(long idTipoEfa)
  {
    this.idTipoEfa = idTipoEfa;
  }
  public String getCodiceTipoEfa()
  {
    return codiceTipoEfa;
  }
  public void setCodiceTipoEfa(String codiceTipoEfa)
  {
    this.codiceTipoEfa = codiceTipoEfa;
  }
  public String getDescrizioneTipoEfa()
  {
    return descrizioneTipoEfa;
  }
  public void setDescrizioneTipoEfa(String descrizioneTipoEfa)
  {
    this.descrizioneTipoEfa = descrizioneTipoEfa;
  }
  public String getDescrizioneEstesaTipoEfa()
  {
    return descrizioneEstesaTipoEfa;
  }
  public void setDescrizioneEstesaTipoEfa(String descrizioneEstesaTipoEfa)
  {
    this.descrizioneEstesaTipoEfa = descrizioneEstesaTipoEfa;
  }
  public Integer getIdUnitaMisura()
  {
    return idUnitaMisura;
  }
  public void setIdUnitaMisura(Integer idUnitaMisura)
  {
    this.idUnitaMisura = idUnitaMisura;
  }
  public BigDecimal getFattoreDiConversione()
  {
    return fattoreDiConversione;
  }
  public void setFattoreDiConversione(BigDecimal fattoreDiConversione)
  {
    this.fattoreDiConversione = fattoreDiConversione;
  }
  public BigDecimal getFattoreDiPonderazione()
  {
    return fattoreDiPonderazione;
  }
  public void setFattoreDiPonderazione(BigDecimal fattoreDiPonderazione)
  {
    this.fattoreDiPonderazione = fattoreDiPonderazione;
  }
  public Long getIdTipoEfaPadre()
  {
    return idTipoEfaPadre;
  }
  public void setIdTipoEfaPadre(Long idTipoEfaPadre)
  {
    this.idTipoEfaPadre = idTipoEfaPadre;
  }
  public String getDescUnitaMisura()
  {
    return descUnitaMisura;
  }
  public void setDescUnitaMisura(String descUnitaMisura)
  {
    this.descUnitaMisura = descUnitaMisura;
  }
  public boolean isPadre()
  {
    return padre;
  }
  public void setPadre(boolean padre)
  {
    this.padre = padre;
  }
  
  
  
  
  
}
