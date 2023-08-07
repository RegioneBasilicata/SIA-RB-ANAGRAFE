package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;

import java.io.Serializable;
import java.util.Date;

public class NotificaEntitaVO implements Serializable {

	
  /**
   * 
   */
  private static final long serialVersionUID = -4921792738985006674L;
  
  
  //	Attributi del DB
	private Long idNotificaEntita; 
	private Long idNotifica;
	private Integer idTipoEntita;
	private Long identificativo;
	private String note;
	private Date dataInizioValidita;
	private Date dataFineValidita;
	private Long idUtenteAggiornamento;
	private Long idUtenteInserimento;
	private Date dataAggiornamento;
	private Long idDichiarazioneConsistenza;
	private String noteChiusuraEntita;
	private String denUtente;
	private String denEnteUtente;
	private String denUtenteChiusura;
  private String denEnteUtenteChiusura;
	//Property che mi dice se l'entità è ancora presente nell'inserimento
	private boolean isInserimento;
	
	
	private StoricoParticellaVO storicoParticellaVO;
	
  public Long getIdNotificaEntita()
  {
    return idNotificaEntita;
  }
  public void setIdNotificaEntita(Long idNotificaEntita)
  {
    this.idNotificaEntita = idNotificaEntita;
  }
  public Long getIdNotifica()
  {
    return idNotifica;
  }
  public void setIdNotifica(Long idNotifica)
  {
    this.idNotifica = idNotifica;
  }
  public Integer getIdTipoEntita()
  {
    return idTipoEntita;
  }
  public void setIdTipoEntita(Integer idTipoEntita)
  {
    this.idTipoEntita = idTipoEntita;
  }
  public Long getIdentificativo()
  {
    return identificativo;
  }
  public void setIdentificativo(Long identificativo)
  {
    this.identificativo = identificativo;
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
  public Long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }
  public void setIdUtenteAggiornamento(Long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }
  public Date getDataAggiornamento()
  {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(Date dataAggiornamento)
  {
    this.dataAggiornamento = dataAggiornamento;
  }
  public Long getIdDichiarazioneConsistenza()
  {
    return idDichiarazioneConsistenza;
  }
  public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza)
  {
    this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
  }
  public StoricoParticellaVO getStoricoParticellaVO()
  {
    return storicoParticellaVO;
  }
  public void setStoricoParticellaVO(StoricoParticellaVO storicoParticellaVO)
  {
    this.storicoParticellaVO = storicoParticellaVO;
  }
  public String getNoteChiusuraEntita()
  {
    return noteChiusuraEntita;
  }
  public void setNoteChiusuraEntita(String noteChiusuraEntita)
  {
    this.noteChiusuraEntita = noteChiusuraEntita;
  }
  public String getDenUtente()
  {
    return denUtente;
  }
  public void setDenUtente(String denUtente)
  {
    this.denUtente = denUtente;
  }
  public String getDenEnteUtente()
  {
    return denEnteUtente;
  }
  public void setDenEnteUtente(String denEnteUtente)
  {
    this.denEnteUtente = denEnteUtente;
  }
  public boolean isInserimento()
  {
    return isInserimento;
  }
  public void setInserimento(boolean isInserimento)
  {
    this.isInserimento = isInserimento;
  }
  public Long getIdUtenteInserimento()
  {
    return idUtenteInserimento;
  }
  public void setIdUtenteInserimento(Long idUtenteInserimento)
  {
    this.idUtenteInserimento = idUtenteInserimento;
  }
  public String getDenUtenteChiusura()
  {
    return denUtenteChiusura;
  }
  public void setDenUtenteChiusura(String denUtenteChiusura)
  {
    this.denUtenteChiusura = denUtenteChiusura;
  }
  public String getDenEnteUtenteChiusura()
  {
    return denEnteUtenteChiusura;
  }
  public void setDenEnteUtenteChiusura(String denEnteUtenteChiusura)
  {
    this.denEnteUtenteChiusura = denEnteUtenteChiusura;
  }
  
	
}
