package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_SETTORE_ABACO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoSettoreAbacoVO implements Serializable {
	
	
  /**
   * 
   */
  private static final long serialVersionUID = 4600934948146808368L;
  
  
  
  private long idSettoreAbaco;
	private long codSettore;
	private String descrizione;
	private boolean flagVisualizza;
	private String desc_breve;
	
	
	
	
  public long getIdSettoreAbaco()
  {
    return idSettoreAbaco;
  }
  public void setIdSettoreAbaco(long idSettoreAbaco)
  {
    this.idSettoreAbaco = idSettoreAbaco;
  }
  public long getCodSettore()
  {
    return codSettore;
  }
  public void setCodSettore(long codSettore)
  {
    this.codSettore = codSettore;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public boolean isFlagVisualizza()
  {
    return flagVisualizza;
  }
  public void setFlagVisualizza(boolean flagVisualizza)
  {
    this.flagVisualizza = flagVisualizza;
  }
  public String getDesc_breve()
  {
    return desc_breve;
  }
  public void setDesc_breve(String desc_breve)
  {
    this.desc_breve = desc_breve;
  }

	
	
}