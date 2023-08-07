package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_R_MENZIONE_PARTICELLA
 * 
 * @author TOBECONFIG
 *
 */
public class MenzioneParticellaVO implements Serializable {
	
	
	
	
  
  
  
  
  
  
  /**
   * 
   */
  private static final long serialVersionUID = -4405867804730538103L;
  
  
  
  
  private Long idMenzioneParticella = null;
  private Long idParticella = null;
  private Long idMenzioneGeografica = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	
	
	
  public Long getIdMenzioneParticella()
  {
    return idMenzioneParticella;
  }
  public void setIdMenzioneParticella(Long idMenzioneParticella)
  {
    this.idMenzioneParticella = idMenzioneParticella;
  }
  public Long getIdParticella()
  {
    return idParticella;
  }
  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }
  public Long getIdMenzioneGeografica()
  {
    return idMenzioneGeografica;
  }
  public void setIdMenzioneGeografica(Long idMenzioneGeografica)
  {
    this.idMenzioneGeografica = idMenzioneGeografica;
  }
  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
	
	
}
