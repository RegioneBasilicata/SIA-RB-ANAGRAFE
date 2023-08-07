package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per la visualizzazione 
 * delle tabelle coi dati DB_R_CATALOGO_MATRICE_SEMINA
 * 
 * @author TOBECONFIG
 *
 */
public class CatalogoMatriceSeminaVO implements Serializable 
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7678631436216209136L;
  
  
  
  private long idCatalogoMatrice;
  private Long idTipoPeriodoSemina;
  private String inizioDestinazioneDefault;
  private String fineDestinazioneDefault;
  private int annoDecodificaPreData;
  private int annoDecodificaPostData;
  
  
  
  public long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }
  public void setIdCatalogoMatrice(long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }
  public Long getIdTipoPeriodoSemina()
  {
    return idTipoPeriodoSemina;
  }
  public void setIdTipoPeriodoSemina(Long idTipoPeriodoSemina)
  {
    this.idTipoPeriodoSemina = idTipoPeriodoSemina;
  }
  public String getInizioDestinazioneDefault()
  {
    return inizioDestinazioneDefault;
  }
  public void setInizioDestinazioneDefault(String inizioDestinazioneDefault)
  {
    this.inizioDestinazioneDefault = inizioDestinazioneDefault;
  }
  public String getFineDestinazioneDefault()
  {
    return fineDestinazioneDefault;
  }
  public void setFineDestinazioneDefault(String fineDestinazioneDefault)
  {
    this.fineDestinazioneDefault = fineDestinazioneDefault;
  }
  public int getAnnoDecodificaPreData()
  {
    return annoDecodificaPreData;
  }
  public void setAnnoDecodificaPreData(int annoDecodificaPreData)
  {
    this.annoDecodificaPreData = annoDecodificaPreData;
  }
  public int getAnnoDecodificaPostData()
  {
    return annoDecodificaPostData;
  }
  public void setAnnoDecodificaPostData(int annoDecodificaPostData)
  {
    this.annoDecodificaPostData = annoDecodificaPostData;
  }
  
  
  
  
  
  
  
  
}
