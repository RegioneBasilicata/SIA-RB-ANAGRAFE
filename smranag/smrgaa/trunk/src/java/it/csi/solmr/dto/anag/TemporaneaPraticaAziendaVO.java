package it.csi.solmr.dto.anag;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Piergiorgio C.
 * @version 1.0
 */

public class TemporaneaPraticaAziendaVO implements Serializable {
	
	static final long serialVersionUID = -543766943672483216L;

	private String idTemporaneaPraticaAzienda;  //DB_PROCEDIMENTO_AZIENDA.ID_TEMPORANEA_PRATICA_AZIENDA
	private String idAzienda;                   //DB_PROCEDIMENTO_AZIENDA.ID_AZIENDA
	private String idProcedimento;              //DB_PROCEDIMENTO_AZIENDA.ID_PROCEDIMENTO
	private String numeroPratica;               //DB_PROCEDIMENTO_AZIENDA.NUMERO_PRATICA
	private String descrizione;                 //DB_PROCEDIMENTO_AZIENDA.DESCRIZIONE
	private String stato;                       //DB_PROCEDIMENTO_AZIENDA.STATO
	private String descrizioneStato;            //DB_PROCEDIMENTO_AZIENDA.DESCRIZIONE_STATO
	private String dataValiditaStato;           //DB_PROCEDIMENTO_AZIENDA.DATA_VALIDITA_STATO
	private String idDichiarazioneConsistenza;  //DB_PROCEDIMENTO_AZIENDA.ID_DICHIARAZIONE_CONSISTENZA
	private String flagCessazioneAzAmmessa;     //DB_PROCEDIMENTO_AZIENDA.FLAG_CESSAZIONE_AZ_AMMESSA
	private String descProcedimento;            //DB_TIPO_PROCEDIMENTO.DESCRIZIONE
	private Long extIdAmmCompetenza = null;
	
	/**
	 * Costruttore di default 
	 */
	public TemporaneaPraticaAziendaVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idTemporaneaPraticaAzienda
	 * @param idAzienda
	 * @param idProcedimento
	 * @param numeroPratica
	 * @param descrizione
	 * @param stato
	 * @param descrizioneStato
	 * @param dataValiditaStato
	 * @param idDichiarazioneConsistenza
	 * @param flagCessazioneAzAmmessa
	 * @param descProcedimento
	 * @param extIdAmmCompetenza
	 */
	public TemporaneaPraticaAziendaVO(String idTemporaneaPraticaAzienda, String idAzienda, String idProcedimento, String numeroPratica, String descrizione, String stato, String descrizioneStato,String dataValiditaStato, String idDichiarazioneConsistenza, String flagCessazioneAzAmmessa, String descProcedimento, Long extIdAmmCompetenza) {
		super();
		this.idTemporaneaPraticaAzienda = idTemporaneaPraticaAzienda;
		this.idAzienda = idAzienda;
		this.idProcedimento = idProcedimento;
		this.numeroPratica = numeroPratica;
		this.descrizione = descrizione;
		this.stato = stato;
		this.descrizioneStato = descrizioneStato;
		this.dataValiditaStato = dataValiditaStato;
		this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
		this.flagCessazioneAzAmmessa = flagCessazioneAzAmmessa;
		this.descProcedimento = descProcedimento;
		this.extIdAmmCompetenza = extIdAmmCompetenza;
	}

	/**
	 * @return the idTemporaneaPraticaAzienda
	 */
	public String getIdTemporaneaPraticaAzienda() {
		return idTemporaneaPraticaAzienda;
	}

	/**
	 * @param idTemporaneaPraticaAzienda the idTemporaneaPraticaAzienda to set
	 */
	public void setIdTemporaneaPraticaAzienda(String idTemporaneaPraticaAzienda) {
		this.idTemporaneaPraticaAzienda = idTemporaneaPraticaAzienda;
	}

	/**
	 * @return the idAzienda
	 */
	public String getIdAzienda() {
		return idAzienda;
	}

	/**
	 * @param idAzienda the idAzienda to set
	 */
	public void setIdAzienda(String idAzienda) {
		this.idAzienda = idAzienda;
	}

	/**
	 * @return the idProcedimento
	 */
	public String getIdProcedimento() {
		return idProcedimento;
	}

	/**
	 * @param idProcedimento the idProcedimento to set
	 */
	public void setIdProcedimento(String idProcedimento) {
		this.idProcedimento = idProcedimento;
	}

	/**
	 * @return the numeroPratica
	 */
	public String getNumeroPratica() {
		return numeroPratica;
	}

	/**
	 * @param numeroPratica the numeroPratica to set
	 */
	public void setNumeroPratica(String numeroPratica) {
		this.numeroPratica = numeroPratica;
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
	 * @return the stato
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * @param stato the stato to set
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * @return the descrizioneStato
	 */
	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	/**
	 * @param descrizioneStato the descrizioneStato to set
	 */
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	/**
	 * @return the dataValiditaStato
	 */
	public String getDataValiditaStato() {
		return dataValiditaStato;
	}

	/**
	 * @param dataValiditaStato the dataValiditaStato to set
	 */
	public void setDataValiditaStato(String dataValiditaStato) {
		this.dataValiditaStato = dataValiditaStato;
	}

	/**
	 * @return the idDichiarazioneConsistenza
	 */
	public String getIdDichiarazioneConsistenza() {
		return idDichiarazioneConsistenza;
	}

	/**
	 * @param idDichiarazioneConsistenza the idDichiarazioneConsistenza to set
	 */
	public void setIdDichiarazioneConsistenza(String idDichiarazioneConsistenza) {
		this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
	}

	/**
	 * @return the flagCessazioneAzAmmessa
	 */
	public String getFlagCessazioneAzAmmessa() {
		return flagCessazioneAzAmmessa;
	}

	/**
	 * @param flagCessazioneAzAmmessa the flagCessazioneAzAmmessa to set
	 */
	public void setFlagCessazioneAzAmmessa(String flagCessazioneAzAmmessa) {
		this.flagCessazioneAzAmmessa = flagCessazioneAzAmmessa;
	}

	/**
	 * @return the descProcedimento
	 */
	public String getDescProcedimento() {
		return descProcedimento;
	}

	/**
	 * @param descProcedimento the descProcedimento to set
	 */
	public void setDescProcedimento(String descProcedimento) {
		this.descProcedimento = descProcedimento;
	}

	/**
	 * @return the extIdAmmCompetenza
	 */
	public Long getExtIdAmmCompetenza() {
		return extIdAmmCompetenza;
	}

	/**
	 * @param extIdAmmCompetenza the extIdAmmCompetenza to set
	 */
	public void setExtIdAmmCompetenza(Long extIdAmmCompetenza) {
		this.extIdAmmCompetenza = extIdAmmCompetenza;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataValiditaStato == null) ? 0 : dataValiditaStato.hashCode());
		result = prime * result + ((descProcedimento == null) ? 0 : descProcedimento.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((descrizioneStato == null) ? 0 : descrizioneStato.hashCode());
		result = prime * result + ((extIdAmmCompetenza == null) ? 0 : extIdAmmCompetenza.hashCode());
		result = prime * result + ((flagCessazioneAzAmmessa == null) ? 0 : flagCessazioneAzAmmessa.hashCode());
		result = prime * result + ((idAzienda == null) ? 0 : idAzienda.hashCode());
		result = prime * result + ((idDichiarazioneConsistenza == null) ? 0 : idDichiarazioneConsistenza.hashCode());
		result = prime * result + ((idProcedimento == null) ? 0 : idProcedimento.hashCode());
		result = prime * result + ((idTemporaneaPraticaAzienda == null) ? 0 : idTemporaneaPraticaAzienda.hashCode());
		result = prime * result + ((numeroPratica == null) ? 0 : numeroPratica.hashCode());
		result = prime * result + ((stato == null) ? 0 : stato.hashCode());
		return result;
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
		final TemporaneaPraticaAziendaVO other = (TemporaneaPraticaAziendaVO) obj;
		if (dataValiditaStato == null) {
			if (other.dataValiditaStato != null)
				return false;
		} else if (!dataValiditaStato.equals(other.dataValiditaStato))
			return false;
		if (descProcedimento == null) {
			if (other.descProcedimento != null)
				return false;
		} else if (!descProcedimento.equals(other.descProcedimento))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (descrizioneStato == null) {
			if (other.descrizioneStato != null)
				return false;
		} else if (!descrizioneStato.equals(other.descrizioneStato))
			return false;
		if (extIdAmmCompetenza == null) {
			if (other.extIdAmmCompetenza != null)
				return false;
		} else if (!extIdAmmCompetenza.equals(other.extIdAmmCompetenza))
			return false;
		if (flagCessazioneAzAmmessa == null) {
			if (other.flagCessazioneAzAmmessa != null)
				return false;
		} else if (!flagCessazioneAzAmmessa
				.equals(other.flagCessazioneAzAmmessa))
			return false;
		if (idAzienda == null) {
			if (other.idAzienda != null)
				return false;
		} else if (!idAzienda.equals(other.idAzienda))
			return false;
		if (idDichiarazioneConsistenza == null) {
			if (other.idDichiarazioneConsistenza != null)
				return false;
		} else if (!idDichiarazioneConsistenza
				.equals(other.idDichiarazioneConsistenza))
			return false;
		if (idProcedimento == null) {
			if (other.idProcedimento != null)
				return false;
		} else if (!idProcedimento.equals(other.idProcedimento))
			return false;
		if (idTemporaneaPraticaAzienda == null) {
			if (other.idTemporaneaPraticaAzienda != null)
				return false;
		} else if (!idTemporaneaPraticaAzienda
				.equals(other.idTemporaneaPraticaAzienda))
			return false;
		if (numeroPratica == null) {
			if (other.numeroPratica != null)
				return false;
		} else if (!numeroPratica.equals(other.numeroPratica))
			return false;
		if (stato == null) {
			if (other.stato != null)
				return false;
		} else if (!stato.equals(other.stato))
			return false;
		return true;
	}
}