package it.csi.solmr.dto.anag.attestazioni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_ATTESTAZIONE_AZIENDA
 * 
 * @author Mauro Vocale
 *
 */
public class AttestazioneAziendaVO implements Serializable {

	private static final long serialVersionUID = -1703237338403160969L;
	
	private Long idAttestazioneAzienda = null;
	private Long idAzienda = null;
	private Long idAttestazione = null;
	private Date dataUltimoAggiornamento = null;
	private Long idUtenteAggiornamento = null;
  //Campi ultima modifica spezzata
  private String utenteUltimaModifica = null;
  private String enteUltimaModifica = null;
	

	/**
	 * @return the idAttestazioneAzienda
	 */
	public Long getIdAttestazioneAzienda() {
		return idAttestazioneAzienda;
	}

	/**
	 * @param idAttestazioneAzienda the idAttestazioneAzienda to set
	 */
	public void setIdAttestazioneAzienda(Long idAttestazioneAzienda) {
		this.idAttestazioneAzienda = idAttestazioneAzienda;
	}

	/**
	 * @return the idAzienda
	 */
	public Long getIdAzienda() {
		return idAzienda;
	}

	/**
	 * @param idAzienda the idAzienda to set
	 */
	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	/**
	 * @return the idAttestazione
	 */
	public Long getIdAttestazione() {
		return idAttestazione;
	}

	/**
	 * @param idAttestazione the idAttestazione to set
	 */
	public void setIdAttestazione(Long idAttestazione) {
		this.idAttestazione = idAttestazione;
	}

	/**
	 * @return the dataUltimoAggiornamento
	 */
	public Date getDataUltimoAggiornamento() {
		return dataUltimoAggiornamento;
	}

	/**
	 * @param dataUltimoAggiornamento the dataUltimoAggiornamento to set
	 */
	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	/**
	 * @return the idUtenteAggiornamento
	 */
	public Long getIdUtenteAggiornamento() {
		return idUtenteAggiornamento;
	}

	/**
	 * @param idUtenteAggiornamento the idUtenteAggiornamento to set
	 */
	public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
		this.idUtenteAggiornamento = idUtenteAggiornamento;
	}

  public String getUtenteUltimaModifica()
  {
    return utenteUltimaModifica;
  }

  public void setUtenteUltimaModifica(String utenteUltimaModifica)
  {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public String getEnteUltimaModifica()
  {
    return enteUltimaModifica;
  }

  public void setEnteUltimaModifica(String enteUltimaModifica)
  {
    this.enteUltimaModifica = enteUltimaModifica;
  }

}