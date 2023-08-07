package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_MENZIONE_GEOGRAFICA
 * 
 * @author TOBECONFIG
 *
 */
public class TipoMenzioneGeograficaVO implements Serializable {
	
	
	
	
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 3001824180002748710L;
  
  
  
  private Long idMenzioneGeografica = null;
	private String descrizione = null;
	private Long idParticella = null;
	private Long idTipologiaVino = null;
	
	
	/**
	 * Costruttore di default
	 */
	public TipoMenzioneGeograficaVO() {
	}


  public Long getIdMenzioneGeografica()
  {
    return idMenzioneGeografica;
  }


  public void setIdMenzioneGeografica(Long idMenzioneGeografica)
  {
    this.idMenzioneGeografica = idMenzioneGeografica;
  }


  public String getDescrizione()
  {
    return descrizione;
  }


  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }


  public Long getIdParticella()
  {
    return idParticella;
  }


  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }


  public Long getIdTipologiaVino()
  {
    return idTipologiaVino;
  }


  public void setIdTipologiaVino(Long idTipologiaVino)
  {
    this.idTipologiaVino = idTipologiaVino;
  }


  

	
}
