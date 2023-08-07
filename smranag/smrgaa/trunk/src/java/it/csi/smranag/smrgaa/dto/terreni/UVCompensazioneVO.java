package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe coi i campi per la visualizzazione 
 * delle uv per l'excel compensazione
 * 
 * @author TOBECONFIG
 *
 */
public class UVCompensazioneVO implements Serializable 
{
	
  /**
   * 
   */
  private static final long serialVersionUID = -8338803311043410906L;
  
  
  
  
  private String descComune;
  private String siglaProv;
  private String sezione;
  private String foglio;
  private String particella;
  private String subalterno;
  private String progrUnar;
  private Date dataImpianto;
  private Date dataPrimaProduzione;
  private String destinazioneProduttiva;
  private String vitigno;
  private String idoneita;
  private BigDecimal areaDichiarata;
  private BigDecimal areaGisRiproporzionata;
  private BigDecimal area;
  private BigDecimal areaPostAllinea;
  private String tolleranza;
  private BigDecimal delta;
  private String istanzaRiesame;
  private BigDecimal percentualePossesso;
  
  
  
  
  
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSiglaProv()
  {
    return siglaProv;
  }
  public void setSiglaProv(String siglaProv)
  {
    this.siglaProv = siglaProv;
  }
  public String getSezione()
  {
    return sezione;
  }
  public void setSezione(String sezione)
  {
    this.sezione = sezione;
  }
  public String getFoglio()
  {
    return foglio;
  }
  public void setFoglio(String foglio)
  {
    this.foglio = foglio;
  }
  public String getParticella()
  {
    return particella;
  }
  public void setParticella(String particella)
  {
    this.particella = particella;
  }
  public String getSubalterno()
  {
    return subalterno;
  }
  public void setSubalterno(String subalterno)
  {
    this.subalterno = subalterno;
  }
  public String getProgrUnar()
  {
    return progrUnar;
  }
  public void setProgrUnar(String progrUnar)
  {
    this.progrUnar = progrUnar;
  }
  public Date getDataImpianto()
  {
    return dataImpianto;
  }
  public void setDataImpianto(Date dataImpianto)
  {
    this.dataImpianto = dataImpianto;
  }
  public String getDestinazioneProduttiva()
  {
    return destinazioneProduttiva;
  }
  public void setDestinazioneProduttiva(String destinazioneProduttiva)
  {
    this.destinazioneProduttiva = destinazioneProduttiva;
  }
  public String getVitigno()
  {
    return vitigno;
  }
  public void setVitigno(String vitigno)
  {
    this.vitigno = vitigno;
  }
  public String getIdoneita()
  {
    return idoneita;
  }
  public void setIdoneita(String idoneita)
  {
    this.idoneita = idoneita;
  }
  public BigDecimal getAreaDichiarata()
  {
    return areaDichiarata;
  }
  public void setAreaDichiarata(BigDecimal areaDichiarata)
  {
    this.areaDichiarata = areaDichiarata;
  }
  public BigDecimal getAreaGisRiproporzionata()
  {
    return areaGisRiproporzionata;
  }
  public void setAreaGisRiproporzionata(BigDecimal areaGisRiproporzionata)
  {
    this.areaGisRiproporzionata = areaGisRiproporzionata;
  }
  public BigDecimal getArea()
  {
    return area;
  }
  public void setArea(BigDecimal area)
  {
    this.area = area;
  }
  public BigDecimal getAreaPostAllinea()
  {
    return areaPostAllinea;
  }
  public void setAreaPostAllinea(BigDecimal areaPostAllinea)
  {
    this.areaPostAllinea = areaPostAllinea;
  }
  public BigDecimal getDelta()
  {
    return delta;
  }
  public void setDelta(BigDecimal delta)
  {
    this.delta = delta;
  }
  public String getTolleranza()
  {
    return tolleranza;
  }
  public void setTolleranza(String tolleranza)
  {
    this.tolleranza = tolleranza;
  }
  public String getIstanzaRiesame()
  {
    return istanzaRiesame;
  }
  public void setIstanzaRiesame(String istanzaRiesame)
  {
    this.istanzaRiesame = istanzaRiesame;
  }
  public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }
  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
  public Date getDataPrimaProduzione()
  {
    return dataPrimaProduzione;
  }
  public void setDataPrimaProduzione(Date dataPrimaProduzione)
  {
    this.dataPrimaProduzione = dataPrimaProduzione;
  }
  
  
  
  
  
  
  
	
}
