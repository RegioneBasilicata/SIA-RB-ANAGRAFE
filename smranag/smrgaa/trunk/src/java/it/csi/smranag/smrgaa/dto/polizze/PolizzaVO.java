package it.csi.smranag.smrgaa.dto.polizze;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

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

public class PolizzaVO implements Serializable
{ 
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -7094905453298927574L;
  
  
  
  
  
  private long idPolizzaAssicurativa;
  private String numeroPolizza;
  private int annoCampagna;
  private String codiceIntervento;
  private String descrizioneIntervento;
  private String codiceCompagnia;
  private String denominazioneCompagnia;
  private String codiceConsorzio;
  private String descrizioneConsorzio;
  private BigDecimal valoreAssicurato;
  private BigDecimal importoPremio;
  private BigDecimal valoreRisarcito;
  private BigDecimal importoPagato;
  private Date dataPolizza;
  private Date dataQuietanza;
  private String descrizionePeriodo;
  private String tipoPolizza;
  private String contributoConsorzio;
  private Vector<String> vMacroUso;
  private String polizzaIntegrativa;
  private String aggiuntiva;
  private Date dataInformatizzazione;
  private BigDecimal giorniFuoriTermine;
  
  
  
  
  public long getIdPolizzaAssicurativa()
  {
    return idPolizzaAssicurativa;
  }
  public void setIdPolizzaAssicurativa(long idPolizzaAssicurativa)
  {
    this.idPolizzaAssicurativa = idPolizzaAssicurativa;
  }
  public String getNumeroPolizza()
  {
    return numeroPolizza;
  }
  public void setNumeroPolizza(String numeroPolizza)
  {
    this.numeroPolizza = numeroPolizza;
  }
  public String getCodiceIntervento()
  {
    return codiceIntervento;
  }
  public void setCodiceIntervento(String codiceIntervento)
  {
    this.codiceIntervento = codiceIntervento;
  }
  public String getDescrizioneIntervento()
  {
    return descrizioneIntervento;
  }
  public void setDescrizioneIntervento(String descrizioneIntervento)
  {
    this.descrizioneIntervento = descrizioneIntervento;
  }
  public String getCodiceCompagnia()
  {
    return codiceCompagnia;
  }
  public void setCodiceCompagnia(String codiceCompagnia)
  {
    this.codiceCompagnia = codiceCompagnia;
  }
  public String getDenominazioneCompagnia()
  {
    return denominazioneCompagnia;
  }
  public void setDenominazioneCompagnia(String denominazioneCompagnia)
  {
    this.denominazioneCompagnia = denominazioneCompagnia;
  }
  public String getCodiceConsorzio()
  {
    return codiceConsorzio;
  }
  public void setCodiceConsorzio(String codiceConsorzio)
  {
    this.codiceConsorzio = codiceConsorzio;
  }
  public String getDescrizioneConsorzio()
  {
    return descrizioneConsorzio;
  }
  public void setDescrizioneConsorzio(String descrizioneConsorzio)
  {
    this.descrizioneConsorzio = descrizioneConsorzio;
  }
  public BigDecimal getValoreAssicurato()
  {
    return valoreAssicurato;
  }
  public void setValoreAssicurato(BigDecimal valoreAssicurato)
  {
    this.valoreAssicurato = valoreAssicurato;
  }
  public BigDecimal getImportoPremio()
  {
    return importoPremio;
  }
  public void setImportoPremio(BigDecimal importoPremio)
  {
    this.importoPremio = importoPremio;
  }
  public BigDecimal getValoreRisarcito()
  {
    return valoreRisarcito;
  }
  public void setValoreRisarcito(BigDecimal valoreRisarcito)
  {
    this.valoreRisarcito = valoreRisarcito;
  }
  public BigDecimal getImportoPagato()
  {
    return importoPagato;
  }
  public void setImportoPagato(BigDecimal importoPagato)
  {
    this.importoPagato = importoPagato;
  }
  public Date getDataPolizza()
  {
    return dataPolizza;
  }
  public void setDataPolizza(Date dataPolizza)
  {
    this.dataPolizza = dataPolizza;
  }
  public Date getDataQuietanza()
  {
    return dataQuietanza;
  }
  public void setDataQuietanza(Date dataQuietanza)
  {
    this.dataQuietanza = dataQuietanza;
  }
  public String getDescrizionePeriodo()
  {
    return descrizionePeriodo;
  }
  public void setDescrizionePeriodo(String descrizionePeriodo)
  {
    this.descrizionePeriodo = descrizionePeriodo;
  }
  public int getAnnoCampagna()
  {
    return annoCampagna;
  }
  public void setAnnoCampagna(int annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
  public String getTipoPolizza()
  {
    return tipoPolizza;
  }
  public void setTipoPolizza(String tipoPolizza)
  {
    this.tipoPolizza = tipoPolizza;
  }
  public String getContributoConsorzio()
  {
    return contributoConsorzio;
  }
  public void setContributoConsorzio(String contributoConsorzio)
  {
    this.contributoConsorzio = contributoConsorzio;
  }
  public Vector<String> getvMacroUso()
  {
    return vMacroUso;
  }
  public void setvMacroUso(Vector<String> vMacroUso)
  {
    this.vMacroUso = vMacroUso;
  }
  public String getPolizzaIntegrativa()
  {
    return polizzaIntegrativa;
  }
  public void setPolizzaIntegrativa(String polizzaIntegrativa)
  {
    this.polizzaIntegrativa = polizzaIntegrativa;
  }
  public String getAggiuntiva()
  {
    return aggiuntiva;
  }
  public void setAggiuntiva(String aggiuntiva)
  {
    this.aggiuntiva = aggiuntiva;
  }
  public Date getDataInformatizzazione()
  {
    return dataInformatizzazione;
  }
  public void setDataInformatizzazione(Date dataInformatizzazione)
  {
    this.dataInformatizzazione = dataInformatizzazione;
  }
  public BigDecimal getGiorniFuoriTermine()
  {
    return giorniFuoriTermine;
  }
  public void setGiorniFuoriTermine(BigDecimal giorniFuoriTermine)
  {
    this.giorniFuoriTermine = giorniFuoriTermine;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  

  

}
