package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_GENERE_ISCRIZIONE
 * 
 * @author TOBECONFIG
 *
 */
public class TipoGenereIscrizioneVO implements Serializable {
	
	
	
	/**
   * 
   */
  private static final long serialVersionUID = 7527238196836489207L;
  
  
  
  private Long idGenereIscrizione = null;
	private String descrizione = null;
	private String flagDefinitiva = null;
	
	
	/**
	 * Costruttore di default
	 */
	public TipoGenereIscrizioneVO() {
	}


  public Long getIdGenereIscrizione()
  {
    return idGenereIscrizione;
  }


  public void setIdGenereIscrizione(Long idGenereIscrizione)
  {
    this.idGenereIscrizione = idGenereIscrizione;
  }


  public String getDescrizione()
  {
    return descrizione;
  }


  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }


  public String getFlagDefinitiva()
  {
    return flagDefinitiva;
  }


  public void setFlagDefinitiva(String flagDefinitiva)
  {
    this.flagDefinitiva = flagDefinitiva;
  }

	
}
