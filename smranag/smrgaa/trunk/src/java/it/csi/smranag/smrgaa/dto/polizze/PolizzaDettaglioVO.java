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

public class PolizzaDettaglioVO implements Serializable
{ 
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -383702023358047905L;
  
  
  
  
  
  private long idPolizzaAssicurativa;
  private long idDettaglioPolizza;
  private String descComune;
  private String siglaProvincia;
  private String indirizzo;
  private String codMacroUso;
  private String descMacroUso;
  private String codProdotto;
  private String descProdotto;
  private String codiceAsl;
  private int percentualeFranchigia;
  private BigDecimal quantitaCopertaFranchigia;
  private BigDecimal parametroMipaaf;
  private BigDecimal spesaParametrata;
  private BigDecimal spesaAmmessa;
  private BigDecimal superficieUtilizzata;
  private BigDecimal superficieAssicurata;
  private BigDecimal quantitaAssicurata;
  private String unitaMisura;
  private BigDecimal valoreAssicurato;
  private BigDecimal tassoApplicato;
  private BigDecimal importoPremio;
  private BigDecimal importoProposto;
  private BigDecimal importoPagato;
  private Vector<String> vDescGaranzia;
  private Vector<String> vDescEpizoozia;
  private BigDecimal superficieDanneggiata;
  private BigDecimal quantitaDanneggiata;
  private BigDecimal superficieRisarcita;
  private BigDecimal quantitaRisarcita;
  private BigDecimal quantitaRitirata;
  private BigDecimal valoreRisarcito;
  private Date dataInizioCopertura;
  private Date dataFineCopertura;
  private Vector<String> vAnomalia;
  private BigDecimal progrRaccolto;
  private String extraResa;
  
  
  
  
  
  public String getDescComune()
  {
    return descComune;
  }
  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }
  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }
  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }
  public String getCodMacroUso()
  {
    return codMacroUso;
  }
  public void setCodMacroUso(String codMacroUso)
  {
    this.codMacroUso = codMacroUso;
  }
  public String getDescMacroUso()
  {
    return descMacroUso;
  }
  public void setDescMacroUso(String descMacroUso)
  {
    this.descMacroUso = descMacroUso;
  }
  public String getCodProdotto()
  {
    return codProdotto;
  }
  public void setCodProdotto(String codProdotto)
  {
    this.codProdotto = codProdotto;
  }
  public String getDescProdotto()
  {
    return descProdotto;
  }
  public void setDescProdotto(String descProdotto)
  {
    this.descProdotto = descProdotto;
  }
  public int getPercentualeFranchigia()
  {
    return percentualeFranchigia;
  }
  public void setPercentualeFranchigia(int percentualeFranchigia)
  {
    this.percentualeFranchigia = percentualeFranchigia;
  }
  public BigDecimal getParametroMipaaf()
  {
    return parametroMipaaf;
  }
  public void setParametroMipaaf(BigDecimal parametroMipaaf)
  {
    this.parametroMipaaf = parametroMipaaf;
  }
  public BigDecimal getSpesaParametrata()
  {
    return spesaParametrata;
  }
  public void setSpesaParametrata(BigDecimal spesaParametrata)
  {
    this.spesaParametrata = spesaParametrata;
  }
  public BigDecimal getSpesaAmmessa()
  {
    return spesaAmmessa;
  }
  public void setSpesaAmmessa(BigDecimal spesaAmmessa)
  {
    this.spesaAmmessa = spesaAmmessa;
  }
  public BigDecimal getSuperficieUtilizzata()
  {
    return superficieUtilizzata;
  }
  public void setSuperficieUtilizzata(BigDecimal superficieUtilizzata)
  {
    this.superficieUtilizzata = superficieUtilizzata;
  }
  public BigDecimal getSuperficieAssicurata()
  {
    return superficieAssicurata;
  }
  public void setSuperficieAssicurata(BigDecimal superficieAssicurata)
  {
    this.superficieAssicurata = superficieAssicurata;
  }
  public BigDecimal getQuantitaAssicurata()
  {
    return quantitaAssicurata;
  }
  public void setQuantitaAssicurata(BigDecimal quantitaAssicurata)
  {
    this.quantitaAssicurata = quantitaAssicurata;
  }
  public String getUnitaMisura()
  {
    return unitaMisura;
  }
  public void setUnitaMisura(String unitaMisura)
  {
    this.unitaMisura = unitaMisura;
  }
  public BigDecimal getValoreAssicurato()
  {
    return valoreAssicurato;
  }
  public void setValoreAssicurato(BigDecimal valoreAssicurato)
  {
    this.valoreAssicurato = valoreAssicurato;
  }
  public BigDecimal getTassoApplicato()
  {
    return tassoApplicato;
  }
  public void setTassoApplicato(BigDecimal tassoApplicato)
  {
    this.tassoApplicato = tassoApplicato;
  }
  public BigDecimal getImportoPremio()
  {
    return importoPremio;
  }
  public void setImportoPremio(BigDecimal importoPremio)
  {
    this.importoPremio = importoPremio;
  }
  public BigDecimal getImportoProposto()
  {
    return importoProposto;
  }
  public void setImportoProposto(BigDecimal importoProposto)
  {
    this.importoProposto = importoProposto;
  }
  public BigDecimal getImportoPagato()
  {
    return importoPagato;
  }
  public void setImportoPagato(BigDecimal importoPagato)
  {
    this.importoPagato = importoPagato;
  }
  public BigDecimal getSuperficieDanneggiata()
  {
    return superficieDanneggiata;
  }
  public void setSuperficieDanneggiata(BigDecimal superficieDanneggiata)
  {
    this.superficieDanneggiata = superficieDanneggiata;
  }
  public BigDecimal getQuantitaDanneggiata()
  {
    return quantitaDanneggiata;
  }
  public void setQuantitaDanneggiata(BigDecimal quantitaDanneggiata)
  {
    this.quantitaDanneggiata = quantitaDanneggiata;
  }
  public BigDecimal getSuperficieRisarcita()
  {
    return superficieRisarcita;
  }
  public void setSuperficieRisarcita(BigDecimal superficieRisarcita)
  {
    this.superficieRisarcita = superficieRisarcita;
  }
  public BigDecimal getQuantitaRisarcita()
  {
    return quantitaRisarcita;
  }
  public void setQuantitaRisarcita(BigDecimal quantitaRisarcita)
  {
    this.quantitaRisarcita = quantitaRisarcita;
  }
  public BigDecimal getValoreRisarcito()
  {
    return valoreRisarcito;
  }
  public void setValoreRisarcito(BigDecimal valoreRisarcito)
  {
    this.valoreRisarcito = valoreRisarcito;
  }
  public Date getDataInizioCopertura()
  {
    return dataInizioCopertura;
  }
  public void setDataInizioCopertura(Date dataInizioCopertura)
  {
    this.dataInizioCopertura = dataInizioCopertura;
  }
  public Date getDataFineCopertura()
  {
    return dataFineCopertura;
  }
  public void setDataFineCopertura(Date dataFineCopertura)
  {
    this.dataFineCopertura = dataFineCopertura;
  }
  /*public String getAnomalia()
  {
    return Anomalia;
  }
  public void setAnomalia(String anomalia)
  {
    Anomalia = anomalia;
  }*/
  public long getIdPolizzaAssicurativa()
  {
    return idPolizzaAssicurativa;
  }
  public Vector<String> getvAnomalia()
  {
    return vAnomalia;
  }
  public void setvAnomalia(Vector<String> vAnomalia)
  {
    this.vAnomalia = vAnomalia;
  }
  public void setIdPolizzaAssicurativa(long idPolizzaAssicurativa)
  {
    this.idPolizzaAssicurativa = idPolizzaAssicurativa;
  }
  public Vector<String> getVDescGaranzia()
  {
    return vDescGaranzia;
  }
  public void setVDescGaranzia(Vector<String> descGaranzia)
  {
    vDescGaranzia = descGaranzia;
  }
  public long getIdDettaglioPolizza()
  {
    return idDettaglioPolizza;
  }
  public void setIdDettaglioPolizza(long idDettaglioPolizza)
  {
    this.idDettaglioPolizza = idDettaglioPolizza;
  }
  public String getCodiceAsl()
  {
    return codiceAsl;
  }
  public void setCodiceAsl(String codiceAsl)
  {
    this.codiceAsl = codiceAsl;
  }
  public BigDecimal getQuantitaCopertaFranchigia()
  {
    return quantitaCopertaFranchigia;
  }
  public void setQuantitaCopertaFranchigia(BigDecimal quantitaCopertaFranchigia)
  {
    this.quantitaCopertaFranchigia = quantitaCopertaFranchigia;
  }
  public BigDecimal getQuantitaRitirata()
  {
    return quantitaRitirata;
  }
  public void setQuantitaRitirata(BigDecimal quantitaRitirata)
  {
    this.quantitaRitirata = quantitaRitirata;
  }
  public Vector<String> getVDescEpizoozia()
  {
    return vDescEpizoozia;
  }
  public void setVDescEpizoozia(Vector<String> descEpizoozia)
  {
    vDescEpizoozia = descEpizoozia;
  }
  public String getIndirizzo()
  {
    return indirizzo;
  }
  public void setIndirizzo(String indirizzo)
  {
    this.indirizzo = indirizzo;
  }
  public BigDecimal getProgrRaccolto()
  {
    return progrRaccolto;
  }
  public void setProgrRaccolto(BigDecimal progrRaccolto)
  {
    this.progrRaccolto = progrRaccolto;
  }
  public String getExtraResa()
  {
    return extraResa;
  }
  public void setExtraResa(String extraResa)
  {
    this.extraResa = extraResa;
  }
  

}
