package it.csi.smranag.smrgaa.dto.terreni;

import java.io.Serializable;

/**
 * Classe coi i campi per l'allinea uv in tolleranza 
 * della compensazione azienda
 * 
 * @author TOBECONFIG
 *
 */
public class TolleranzaVO implements Serializable 
{
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -2359176557478221184L;
  
  
  
  private long idStoricoUnitaArborea;
  private long idParticellaCertificata;
  private long idVarieta;
  private long idCatalogoMatrice;
  private int tolleranza;
  
  public int getTolleranza()
  {
    return tolleranza;
  }
  public void setTolleranza(int tolleranza)
  {
    this.tolleranza = tolleranza;
  }  
  public long getIdStoricoUnitaArborea()
  {
    return idStoricoUnitaArborea;
  }
  public void setIdStoricoUnitaArborea(long idStoricoUnitaArborea)
  {
    this.idStoricoUnitaArborea = idStoricoUnitaArborea;
  }
  public long getIdParticellaCertificata()
  {
    return idParticellaCertificata;
  }
  public void setIdParticellaCertificata(long idParticellaCertificata)
  {
    this.idParticellaCertificata = idParticellaCertificata;
  }
  public long getIdVarieta()
  {
    return idVarieta;
  }
  public void setIdVarieta(long idVarieta)
  {
    this.idVarieta = idVarieta;
  }
  public long getIdCatalogoMatrice()
  {
    return idCatalogoMatrice;
  }
  public void setIdCatalogoMatrice(long idCatalogoMatrice)
  {
    this.idCatalogoMatrice = idCatalogoMatrice;
  }
  
  
  
  
  
 
  
  
	
}
