package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_INTERVENTO_VITICOLO
 * @author Mauro Vocale
 *
 */
public class TipoInterventoViticoloVO implements Serializable {

	
	
	/**
   * 
   */
  private static final long serialVersionUID = 4057759849058891859L;
  
  
  
  private Long idTipoInterventoViticolo = null;
	private String codiceInterventoViticolo = null;
	private String descrizione = null;
	private String flagVideo = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	
	
  public Long getIdTipoInterventoViticolo()
  {
    return idTipoInterventoViticolo;
  }
  public void setIdTipoInterventoViticolo(Long idTipoInterventoViticolo)
  {
    this.idTipoInterventoViticolo = idTipoInterventoViticolo;
  }
  public String getCodiceInterventoViticolo()
  {
    return codiceInterventoViticolo;
  }
  public void setCodiceInterventoViticolo(String codiceInterventoViticolo)
  {
    this.codiceInterventoViticolo = codiceInterventoViticolo;
  }
  public String getDescrizione()
  {
    return descrizione;
  }
  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }
  public String getFlagVideo()
  {
    return flagVideo;
  }
  public void setFlagVideo(String flagVideo)
  {
    this.flagVideo = flagVideo;
  }
  public java.util.Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(java.util.Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }
  public java.util.Date getDataFineValidita()
  {
    return dataFineValidita;
  }
  public void setDataFineValidita(java.util.Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }
	
	
	
}