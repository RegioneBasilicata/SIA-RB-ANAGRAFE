package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_VIGNA
 * 
 * @author TOBECONFIG
 *
 */
public class VignaVO implements Serializable {
	
	
	
	/**
   * 
   */
  private static final long serialVersionUID = -8813688769469971590L;
  
  
  
  private Long idTipologiaVino = null;
	private Long idVigna = null;
	private String menzione = null;
	private String note = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	
	
	
  public Long getIdTipologiaVino()
  {
    return idTipologiaVino;
  }
  public void setIdTipologiaVino(Long idTipologiaVino)
  {
    this.idTipologiaVino = idTipologiaVino;
  }
  public Long getIdVigna()
  {
    return idVigna;
  }
  public void setIdVigna(Long idVigna)
  {
    this.idVigna = idVigna;
  }
  public String getMenzione()
  {
    return menzione;
  }
  public void setMenzione(String menzione)
  {
    this.menzione = menzione;
  }
  public String getNote()
  {
    return note;
  }
  public void setNote(String note)
  {
    this.note = note;
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
