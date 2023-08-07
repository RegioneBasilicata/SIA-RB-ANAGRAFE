/**
 * DatiUvCCIAA.java
 *
 * VO utilizzato per wrappare l'omologo VO restituito da CCIAA (DatiUv)
 */

package it.csi.smranag.smrgaa.dto.ws.cciaa;

import it.csi.solmr.dto.anag.terreni.UnitaArboreaCCIAAVO;

import java.util.Vector;

public class DatiUvCCIAA  implements java.io.Serializable
{
  /**
   * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
   * compatibili con le versioni precedenti utilizzate da eventuali client
   */
  static final long serialVersionUID = 845673533985195021L;

  private int annoVegetativo;
  private String capResidenza;
  private String codiceMipaf;
  private String codVitigno;
  private String comuneResidenza;
  private String cuaa;
  private String denominazioneAzienda;
  private String descAlbo;
  private String foglio;
  private String indirizzoResidenza;
  private String istatComune;
  private String istatResidenza;
  private int nrMatricola;
  private int numCeppi;
  private String particella;
  private String particellaSearch; // Campo usato dall ricerca, è uguale al campo
                                   // particella con l'unica differenza che omette
                                   // gli eventuali caratteri P presenti al suo interno
  private String foglioSearch; // Campo usato dall ricerca, è uguale al campo
                                // particella con l'unica differenza che omette
                                  // gli eventuali caratteri P presenti al suo interno
  
  
  private String pvResidenza;
  private String sezione;
  private String siglaCciaa;
  private double superficieH;
  private String varieta;
  private String pIva;

  //Gli attributi seguenti non sono ritornati dal CCIAA, sono utilizzati
  //solo dalla view
  private String siglaProvincia;
  private String descComune;
  private boolean presenteFascicolo;
  private String siglaProvinciaRes;
  private String descComuneRes;
  
  private Vector<UnitaArboreaCCIAAVO> vUnitaArboreaCCIAA;
  private boolean stessaProvinciaCompetenza;


  public boolean isStessaProvinciaCompetenza()
  {
    return stessaProvinciaCompetenza;
  }

  public void setStessaProvinciaCompetenza(boolean stessaProvinciaCompetenza)
  {
    this.stessaProvinciaCompetenza = stessaProvinciaCompetenza;
  }

  public int getAnnoVegetativo()
  {
   return annoVegetativo;
  }

  public void setAnnoVegetativo(int annoVegetativo)
  {
   this.annoVegetativo = annoVegetativo;
  }

  public String getCapResidenza()
  {
   return capResidenza;
  }

  public void setCapResidenza(String capResidenza)
  {
   this.capResidenza = capResidenza;
  }

  public String getCodiceMipaf()
  {
   return codiceMipaf;
  }

  public void setCodiceMipaf(String codiceMipaf)
  {
   this.codiceMipaf = codiceMipaf;
  }

  public String getCodVitigno()
  {
   return codVitigno;
  }

  public void setCodVitigno(String codVitigno)
  {
   this.codVitigno = codVitigno;
  }

  public String getComuneResidenza()
  {
   return comuneResidenza;
  }

  public void setComuneResidenza(String comuneResidenza)
  {
   this.comuneResidenza = comuneResidenza;
  }

  public String getCuaa()
  {
   return cuaa;
  }

  public void setCuaa(String cuaa)
  {
   this.cuaa = cuaa;
  }

  public String getDenominazioneAzienda()
  {
   return denominazioneAzienda;
  }

  public void setDenominazioneAzienda(String denominazioneAzienda)
  {
   this.denominazioneAzienda = denominazioneAzienda;
  }

  public String getDescAlbo()
  {
   return descAlbo;
  }

  public void setDescAlbo(String descAlbo)
  {
   this.descAlbo = descAlbo;
  }

  public String getFoglio()
  {
   return foglio;
  }

  public void setFoglio(String foglio)
  {
   this.foglio = foglio;
  }

  public String getIndirizzoResidenza()
  {
   return indirizzoResidenza;
  }

  public void setIndirizzoResidenza(String indirizzoResidenza)
  {
   this.indirizzoResidenza = indirizzoResidenza;
  }

  public String getIstatComune()
  {
   return istatComune;
  }

  public void setIstatComune(String istatComune)
  {
   this.istatComune = istatComune;
  }

  public String getIstatResidenza()
  {
   return istatResidenza;
  }

  public void setIstatResidenza(String istatResidenza)
  {
   this.istatResidenza = istatResidenza;
  }

  public int getNrMatricola()
  {
   return nrMatricola;
  }

  public void setNrMatricola(int nrMatricola)
  {
   this.nrMatricola = nrMatricola;
  }

  public int getNumCeppi()
  {
   return numCeppi;
  }

  public void setNumCeppi(int numCeppi)
  {
   this.numCeppi = numCeppi;
  }

  public String getParticella()
  {
   return particella;
  }

  public void setParticella(String particella)
  {
   this.particella = particella;
  }

  public String getParticellaSearch()
  {
   return particellaSearch;
  }

  public void setParticellaSearch(String particellaSearch)
  {
   this.particellaSearch = particellaSearch;
  }

  public String getPvResidenza()
  {
   return pvResidenza;
  }

  public void setPvResidenza(String pvResidenza)
  {
   this.pvResidenza = pvResidenza;
  }

  public String getSezione()
  {
   return sezione;
  }

  public void setSezione(String sezione)
  {
   this.sezione = sezione;
  }

  public String getSiglaCciaa()
  {
   return siglaCciaa;
  }

  public void setSiglaCciaa(String siglaCciaa)
  {
   this.siglaCciaa = siglaCciaa;
  }

  public double getSuperficieH()
  {
   return superficieH;
  }

  public void setSuperficieH(double superficieH)
  {
   this.superficieH = superficieH;
  }

  public String getVarieta()
  {
   return varieta;
  }

  public void setVarieta(String varieta)
  {
   this.varieta = varieta;
  }

  public String getPIva()
  {
   return pIva;
  }

  public void setPIva(String pIva)
  {
   this.pIva = pIva;
  }

  public String getSiglaProvincia()
  {
    return siglaProvincia;
  }

  public void setSiglaProvincia(String siglaProvincia)
  {
    this.siglaProvincia = siglaProvincia;
  }

  public String getDescComune()
  {
    return descComune;
  }

  public void setDescComune(String descComune)
  {
    this.descComune = descComune;
  }

  public boolean isPresenteFascicolo()
  {
    return presenteFascicolo;
  }

  public void setPresenteFascicolo(boolean presenteFascicolo)
  {
    this.presenteFascicolo = presenteFascicolo;
  }

  public String getSiglaProvinciaRes()
  {
    return siglaProvinciaRes;
  }

  public void setSiglaProvinciaRes(String siglaProvinciaRes)
  {
    this.siglaProvinciaRes = siglaProvinciaRes;
  }

  public String getDescComuneRes()
  {
    return descComuneRes;
  }

  public void setDescComuneRes(String descComuneRes)
  {
    this.descComuneRes = descComuneRes;
  }

  public Vector<UnitaArboreaCCIAAVO> getVUnitaArboreaCCIAA()
  {
    return vUnitaArboreaCCIAA;
  }

  public void setVUnitaArboreaCCIAA(Vector<UnitaArboreaCCIAAVO> unitaArboreaCCIAA)
  {
    vUnitaArboreaCCIAA = unitaArboreaCCIAA;
  }

  public String getFoglioSearch()
  {
    return foglioSearch;
  }

  public void setFoglioSearch(String foglioSearch)
  {
    this.foglioSearch = foglioSearch;
  }
  
  
}
