package it.csi.solmr.dto.anag.terreni;

import java.io.*;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_VINO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoVinoVO implements Serializable {
	
	private static final long serialVersionUID = -640196407603594282L;
	
	private Long idVino = null;
	private String descrizione = null;
	private String altriVitigni = null;
	private String ricaduta = null;
	private String resa = null;
	private String varietaParticolare = null;
	private String resaDiRicaduta = null;
	private String flagConsideraVitigno = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	private String codice = null;
	
	/**
	 * Costruttore di default
	 */
	public TipoVinoVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idVino
	 * @param descrizione
	 * @param altriVitigni
	 * @param ricaduta
	 * @param resa
	 * @param varietaParticolare
	 * @param resaDiRicaduta
	 * @param flagConsideraVitigno
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 * @param codice
	 */
	public TipoVinoVO(Long idVino, String descrizione, String altriVitigni, String ricaduta, String resa, String varietaParticolare, String resaDiRicaduta, String flagConsideraVitigno, Date dataInizioValidita, Date dataFineValidita, String codice) {
		super();
		this.idVino = idVino;
		this.descrizione = descrizione;
		this.altriVitigni = altriVitigni;
		this.ricaduta = ricaduta;
		this.resa = resa;
		this.varietaParticolare = varietaParticolare;
		this.resaDiRicaduta = resaDiRicaduta;
		this.flagConsideraVitigno = flagConsideraVitigno;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
		this.codice = codice;
	}

	/**
	 * @return the idVino
	 */
	public Long getIdVino() {
		return idVino;
	}

	/**
	 * @param idVino the idVino to set
	 */
	public void setIdVino(Long idVino) {
		this.idVino = idVino;
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
	 * @return the altriVitigni
	 */
	public String getAltriVitigni() {
		return altriVitigni;
	}

	/**
	 * @param altriVitigni the altriVitigni to set
	 */
	public void setAltriVitigni(String altriVitigni) {
		this.altriVitigni = altriVitigni;
	}

	/**
	 * @return the ricaduta
	 */
	public String getRicaduta() {
		return ricaduta;
	}

	/**
	 * @param ricaduta the ricaduta to set
	 */
	public void setRicaduta(String ricaduta) {
		this.ricaduta = ricaduta;
	}

	/**
	 * @return the resa
	 */
	public String getResa() {
		return resa;
	}

	/**
	 * @param resa the resa to set
	 */
	public void setResa(String resa) {
		this.resa = resa;
	}

	/**
	 * @return the varietaParticolare
	 */
	public String getVarietaParticolare() {
		return varietaParticolare;
	}

	/**
	 * @param varietaParticolare the varietaParticolare to set
	 */
	public void setVarietaParticolare(String varietaParticolare) {
		this.varietaParticolare = varietaParticolare;
	}

	/**
	 * @return the resaDiRicaduta
	 */
	public String getResaDiRicaduta() {
		return resaDiRicaduta;
	}

	/**
	 * @param resaDiRicaduta the resaDiRicaduta to set
	 */
	public void setResaDiRicaduta(String resaDiRicaduta) {
		this.resaDiRicaduta = resaDiRicaduta;
	}

	/**
	 * @return the flagConsideraVitigno
	 */
	public String getFlagConsideraVitigno() {
		return flagConsideraVitigno;
	}

	/**
	 * @param flagConsideraVitigno the flagConsideraVitigno to set
	 */
	public void setFlagConsideraVitigno(String flagConsideraVitigno) {
		this.flagConsideraVitigno = flagConsideraVitigno;
	}

	/**
	 * @return the dataInizioValidita
	 */
	public java.util.Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(java.util.Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the dataFineValidita
	 */
	public java.util.Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(java.util.Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altriVitigni == null) ? 0 : altriVitigni.hashCode());
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = prime * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((flagConsideraVitigno == null) ? 0 : flagConsideraVitigno.hashCode());
		result = prime * result + ((idVino == null) ? 0 : idVino.hashCode());
		result = prime * result + ((resa == null) ? 0 : resa.hashCode());
		result = prime * result + ((resaDiRicaduta == null) ? 0 : resaDiRicaduta.hashCode());
		result = prime * result + ((ricaduta == null) ? 0 : ricaduta.hashCode());
		result = prime * result + ((varietaParticolare == null) ? 0 : varietaParticolare.hashCode());
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
		final TipoVinoVO other = (TipoVinoVO) obj;
		if (altriVitigni == null) {
			if (other.altriVitigni != null)
				return false;
		} else if (!altriVitigni.equals(other.altriVitigni))
			return false;
		if (codice == null) {
			if (other.codice != null)
				return false;
		} else if (!codice.equals(other.codice))
			return false;
		if (dataFineValidita == null) {
			if (other.dataFineValidita != null)
				return false;
		} else if (!dataFineValidita.equals(other.dataFineValidita))
			return false;
		if (dataInizioValidita == null) {
			if (other.dataInizioValidita != null)
				return false;
		} else if (!dataInizioValidita.equals(other.dataInizioValidita))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (flagConsideraVitigno == null) {
			if (other.flagConsideraVitigno != null)
				return false;
		} else if (!flagConsideraVitigno.equals(other.flagConsideraVitigno))
			return false;
		if (idVino == null) {
			if (other.idVino != null)
				return false;
		} else if (!idVino.equals(other.idVino))
			return false;
		if (resa == null) {
			if (other.resa != null)
				return false;
		} else if (!resa.equals(other.resa))
			return false;
		if (resaDiRicaduta == null) {
			if (other.resaDiRicaduta != null)
				return false;
		} else if (!resaDiRicaduta.equals(other.resaDiRicaduta))
			return false;
		if (ricaduta == null) {
			if (other.ricaduta != null)
				return false;
		} else if (!ricaduta.equals(other.ricaduta))
			return false;
		if (varietaParticolare == null) {
			if (other.varietaParticolare != null)
				return false;
		} else if (!varietaParticolare.equals(other.varietaParticolare))
			return false;
		return true;
	}
}
