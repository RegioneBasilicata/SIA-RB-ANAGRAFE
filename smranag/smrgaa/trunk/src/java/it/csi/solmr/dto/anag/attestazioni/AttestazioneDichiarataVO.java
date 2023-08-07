package it.csi.solmr.dto.anag.attestazioni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_ATTESTAZIONE_DICHIARATA
 * 
 * @author Mauro Vocale
 *
 */
public class AttestazioneDichiarataVO implements Serializable {

	private static final long serialVersionUID = -6830054905903501680L;
	
	private Long idAttestazioneDichiarata = null;
	private String codiceFotografiaTerreni = null;
	private Long idAttestazione = null;
	private Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
//Campi ultima modifica spezzata
  private String utenteUltimaModifica = null;
  private String enteUltimaModifica = null;
  private Date dataInizioValidita = null;
	
	

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

  /**
	 * @return the idAttestazioneDichiarata
	 */
	public Long getIdAttestazioneDichiarata() {
		return idAttestazioneDichiarata;
	}

	/**
	 * @param idAttestazioneDichiarata the idAttestazioneDichiarata to set
	 */
	public void setIdAttestazioneDichiarata(Long idAttestazioneDichiarata) {
		this.idAttestazioneDichiarata = idAttestazioneDichiarata;
	}

	/**
	 * @return the codiceFotografiaTerreni
	 */
	public String getCodiceFotografiaTerreni() {
		return codiceFotografiaTerreni;
	}

	/**
	 * @param codiceFotografiaTerreni the codiceFotografiaTerreni to set
	 */
	public void setCodiceFotografiaTerreni(String codiceFotografiaTerreni) {
		this.codiceFotografiaTerreni = codiceFotografiaTerreni;
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
	 * @return the dataAggiornamento
	 */
	public Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	/**
	 * @param dataAggiornamento the dataAggiornamento to set
	 */
	public void setDataAggiornamento(Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
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

  public Date getDataInizioValidita()
  {
    return dataInizioValidita;
  }

  public void setDataInizioValidita(Date dataInizioValidita)
  {
    this.dataInizioValidita = dataInizioValidita;
  }

	

}