package it.csi.solmr.dto.anag.terreni;

import it.csi.solmr.dto.CodeDescription;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_DICHIARAZIONE_SEGNALAZIONE
 * @author Mauro Vocale
 *
 */
public class DichiarazioneSegnalazioneVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -594520313849709351L;
	
	private Long idDichiarazioneSegnalazione = null;
	private Long idDichiarazioneConsistenza = null;
	private Long idControllo = null;
	private CodeDescription controllo = null;
	private String descrizione = null;
	private Long idAzienda = null;
	private java.util.Date dataControllo = null;
	private String bloccante = null;
	private Long idStoricoParticella = null;
	private Long idStoricoUnitaArborea = null;
	private String descGruppoControllo;
	
	

	/**
	 * @return the idDichiarazioneSegnalazione
	 */
	public Long getIdDichiarazioneSegnalazione() {
		return idDichiarazioneSegnalazione;
	}

	/**
	 * @param idDichiarazioneSegnalazione the idDichiarazioneSegnalazione to set
	 */
	public void setIdDichiarazioneSegnalazione(Long idDichiarazioneSegnalazione) {
		this.idDichiarazioneSegnalazione = idDichiarazioneSegnalazione;
	}

	/**
	 * @return the idDichiarazioneConsistenza
	 */
	public Long getIdDichiarazioneConsistenza() {
		return idDichiarazioneConsistenza;
	}

	/**
	 * @param idDichiarazioneConsistenza the idDichiarazioneConsistenza to set
	 */
	public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza) {
		this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
	}

	/**
	 * @return the idControllo
	 */
	public Long getIdControllo() {
		return idControllo;
	}

	/**
	 * @param idControllo the idControllo to set
	 */
	public void setIdControllo(Long idControllo) {
		this.idControllo = idControllo;
	}

	/**
	 * @return the controllo
	 */
	public CodeDescription getControllo() {
		return controllo;
	}

	/**
	 * @param controllo the controllo to set
	 */
	public void setControllo(CodeDescription controllo) {
		this.controllo = controllo;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
	 * @return the dataControllo
	 */
	public java.util.Date getDataControllo() {
		return dataControllo;
	}

	/**
	 * @param dataControllo the dataControllo to set
	 */
	public void setDataControllo(java.util.Date dataControllo) {
		this.dataControllo = dataControllo;
	}

	/**
	 * @return the bloccante
	 */
	public String getBloccante() {
		return bloccante;
	}

	/**
	 * @param bloccante the bloccante to set
	 */
	public void setBloccante(String bloccante) {
		this.bloccante = bloccante;
	}

	/**
	 * @return the idStoricoParticella
	 */
	public Long getIdStoricoParticella() {
		return idStoricoParticella;
	}

	/**
	 * @param idStoricoParticella the idStoricoParticella to set
	 */
	public void setIdStoricoParticella(Long idStoricoParticella) {
		this.idStoricoParticella = idStoricoParticella;
	}

	/**
	 * @return the idStoricoUnitaArborea
	 */
	public Long getIdStoricoUnitaArborea() {
		return idStoricoUnitaArborea;
	}

	/**
	 * @param idStoricoUnitaArborea the idStoricoUnitaArborea to set
	 */
	public void setIdStoricoUnitaArborea(Long idStoricoUnitaArborea) {
		this.idStoricoUnitaArborea = idStoricoUnitaArborea;
	}

	/**
	 * Metodo equals()
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DichiarazioneSegnalazioneVO other = (DichiarazioneSegnalazioneVO) obj;
		if (bloccante == null) {
			if (other.bloccante != null)
				return false;
		} else if (!bloccante.equals(other.bloccante))
			return false;
		if (controllo == null) {
			if (other.controllo != null)
				return false;
		} else if (!controllo.equals(other.controllo))
			return false;
		if (dataControllo == null) {
			if (other.dataControllo != null)
				return false;
		} else if (!dataControllo.equals(other.dataControllo))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idAzienda == null) {
			if (other.idAzienda != null)
				return false;
		} else if (!idAzienda.equals(other.idAzienda))
			return false;
		if (idControllo == null) {
			if (other.idControllo != null)
				return false;
		} else if (!idControllo.equals(other.idControllo))
			return false;
		if (idDichiarazioneConsistenza == null) {
			if (other.idDichiarazioneConsistenza != null)
				return false;
		} else if (!idDichiarazioneConsistenza
				.equals(other.idDichiarazioneConsistenza))
			return false;
		if (idDichiarazioneSegnalazione == null) {
			if (other.idDichiarazioneSegnalazione != null)
				return false;
		} else if (!idDichiarazioneSegnalazione
				.equals(other.idDichiarazioneSegnalazione))
			return false;
		if (idStoricoParticella == null) {
			if (other.idStoricoParticella != null)
				return false;
		} else if (!idStoricoParticella.equals(other.idStoricoParticella))
			return false;
		if (idStoricoUnitaArborea == null) {
			if (other.idStoricoUnitaArborea != null)
				return false;
		} else if (!idStoricoUnitaArborea.equals(other.idStoricoUnitaArborea))
			return false;
		return true;
	}

  public String getDescGruppoControllo()
  {
    return descGruppoControllo;
  }

  public void setDescGruppoControllo(String descGruppoControllo)
  {
    this.descGruppoControllo = descGruppoControllo;
  }
	
	
	
				
}