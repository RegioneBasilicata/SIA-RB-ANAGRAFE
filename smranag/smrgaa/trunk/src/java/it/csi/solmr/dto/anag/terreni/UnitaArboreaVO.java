package it.csi.solmr.dto.anag.terreni;

import java.io.*;
import java.util.*;

/**
 * Classe che si occupa di mappare la tabella DB_UNITA_ARBOREA
 * 
 * @author Mauro Vocale
 *
 */
public class UnitaArboreaVO implements Serializable {
	
	private static final long serialVersionUID = -4768781154229612597L;
	
	private Long idUnitaArborea = null;
	private Date dataInizioValidita = null;
	
	/**
	 * Costruttore di default 
	 */
	public UnitaArboreaVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idUnitaArborea
	 * @param dataInizioValidita
	 */
	public UnitaArboreaVO(Long idUnitaArborea, Date dataInizioValidita) {
		super();
		this.idUnitaArborea = idUnitaArborea;
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the dataInizioValidita
	 */
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the idUnitaArborea
	 */
	public Long getIdUnitaArborea() {
		return idUnitaArborea;
	}

	/**
	 * @param idUnitaArborea the idUnitaArborea to set
	 */
	public void setIdUnitaArborea(Long idUnitaArborea) {
		this.idUnitaArborea = idUnitaArborea;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((idUnitaArborea == null) ? 0 : idUnitaArborea.hashCode());
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
		final UnitaArboreaVO other = (UnitaArboreaVO) obj;
		if (dataInizioValidita == null) {
			if (other.dataInizioValidita != null)
				return false;
		} else if (!dataInizioValidita.equals(other.dataInizioValidita))
			return false;
		if (idUnitaArborea == null) {
			if (other.idUnitaArborea != null)
				return false;
		} else if (!idUnitaArborea.equals(other.idUnitaArborea))
			return false;
		return true;
	}
	
}