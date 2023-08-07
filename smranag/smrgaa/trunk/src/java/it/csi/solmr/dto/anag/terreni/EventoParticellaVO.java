package it.csi.solmr.dto.anag.terreni;

import java.io.*;
import java.util.Date;
import it.csi.solmr.dto.*;

/**
 * Classe che si occupa di mappare la tabella DB_EVENTO_PARTICELLA
 * @author Mauro Vocale
 *
 */
public class EventoParticellaVO implements Serializable {

	private static final long serialVersionUID = 1718226487441219102L;
	
	private Long idParticellaEvento = null;
	private Long idEvento = null;
	private CodeDescription tipoEvento = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private Long idParticellaNuova = null;
	private Long idParticellaCessata = null;
	private String descParticellaOrigineEvento = null;
	
	/**
	 * Costruttore di default
	 *
	 */
	public EventoParticellaVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idParticellaEvento
	 * @param idEvento
	 * @param tipoEvento
	 * @param dataAggiornamento
	 * @param idUtenteAggiornamento
	 * @param idParticellaNuova
	 * @param idParticellaCessata
	 * @param descParticellaOrigineEvento
	 */
	public EventoParticellaVO(Long idParticellaEvento, Long idEvento, CodeDescription tipoEvento, Date dataAggiornamento, Long idUtenteAggiornamento, Long idParticellaNuova, Long idParticellaCessata, String descParticellaOrigineEvento) {
		super();
		this.idParticellaEvento = idParticellaEvento;
		this.idEvento = idEvento;
		this.tipoEvento = tipoEvento;
		this.dataAggiornamento = dataAggiornamento;
		this.idUtenteAggiornamento = idUtenteAggiornamento;
		this.idParticellaNuova = idParticellaNuova;
		this.idParticellaCessata = idParticellaCessata;
		this.descParticellaOrigineEvento = descParticellaOrigineEvento;
	}

	/**
	 * @return the dataAggiornamento
	 */
	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}

	/**
	 * @param dataAggiornamento the dataAggiornamento to set
	 */
	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}

	/**
	 * @return the idEvento
	 */
	public Long getIdEvento() {
		return idEvento;
	}

	/**
	 * @param idEvento the idEvento to set
	 */
	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}

	/**
	 * @return the tipoEvento
	 */
	public CodeDescription getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(CodeDescription tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	/**
	 * @return the idParticellaCessata
	 */
	public Long getIdParticellaCessata() {
		return idParticellaCessata;
	}

	/**
	 * @param idParticellaCessata the idParticellaCessata to set
	 */
	public void setIdParticellaCessata(Long idParticellaCessata) {
		this.idParticellaCessata = idParticellaCessata;
	}

	/**
	 * @return the idParticellaEvento
	 */
	public Long getIdParticellaEvento() {
		return idParticellaEvento;
	}

	/**
	 * @param idParticellaEvento the idParticellaEvento to set
	 */
	public void setIdParticellaEvento(Long idParticellaEvento) {
		this.idParticellaEvento = idParticellaEvento;
	}

	/**
	 * @return the idParticellaNuova
	 */
	public Long getIdParticellaNuova() {
		return idParticellaNuova;
	}

	/**
	 * @param idParticellaNuova the idParticellaNuova to set
	 */
	public void setIdParticellaNuova(Long idParticellaNuova) {
		this.idParticellaNuova = idParticellaNuova;
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
	
	/**
	 * @return the descParticellaOrigineEvento
	 */
	public String getDescParticellaOrigineEvento() {
		return descParticellaOrigineEvento;
	}

	/**
	 * @param descParticellaOrigineEvento the descParticellaOrigineEvento to set
	 */
	public void setDescParticellaOrigineEvento(String descParticellaOrigineEvento) {
		this.descParticellaOrigineEvento = descParticellaOrigineEvento;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataAggiornamento == null) ? 0 : dataAggiornamento.hashCode());
		result = PRIME * result + ((idEvento == null) ? 0 : idEvento.hashCode());
		result = PRIME * result + ((tipoEvento == null) ? 0 : tipoEvento.hashCode());
		result = PRIME * result + ((idParticellaCessata == null) ? 0 : idParticellaCessata.hashCode());
		result = PRIME * result + ((idParticellaEvento == null) ? 0 : idParticellaEvento.hashCode());
		result = PRIME * result + ((idParticellaNuova == null) ? 0 : idParticellaNuova.hashCode());
		result = PRIME * result + ((idUtenteAggiornamento == null) ? 0 : idUtenteAggiornamento.hashCode());
		result = PRIME * result + ((descParticellaOrigineEvento == null) ? 0 : descParticellaOrigineEvento.hashCode());
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
		final EventoParticellaVO other = (EventoParticellaVO) obj;
		if (dataAggiornamento == null) {
			if (other.dataAggiornamento != null)
				return false;
		} else if (!dataAggiornamento.equals(other.dataAggiornamento))
			return false;
		if (idEvento == null) {
			if (other.idEvento != null)
				return false;
		} else if (!idEvento.equals(other.idEvento))
			return false;
		if (tipoEvento == null) {
			if (other.tipoEvento != null)
				return false;
		} else if (!tipoEvento.equals(other.tipoEvento))
			return false;
		if (idParticellaCessata == null) {
			if (other.idParticellaCessata != null)
				return false;
		} else if (!idParticellaCessata.equals(other.idParticellaCessata))
			return false;
		if (idParticellaEvento == null) {
			if (other.idParticellaEvento != null)
				return false;
		} else if (!idParticellaEvento.equals(other.idParticellaEvento))
			return false;
		if (idParticellaNuova == null) {
			if (other.idParticellaNuova != null)
				return false;
		} else if (!idParticellaNuova.equals(other.idParticellaNuova))
			return false;
		if (idUtenteAggiornamento == null) {
			if (other.idUtenteAggiornamento != null)
				return false;
		} else if (!idUtenteAggiornamento.equals(other.idUtenteAggiornamento))
			return false;
		if (descParticellaOrigineEvento == null) {
			if (other.descParticellaOrigineEvento != null)
				return false;
		} else if (!descParticellaOrigineEvento.equals(other.descParticellaOrigineEvento))
			return false;
		return true;
	}
	
}