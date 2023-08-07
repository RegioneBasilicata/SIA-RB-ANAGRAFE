package it.csi.solmr.dto.anag.terreni;

import it.csi.solmr.dto.*;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_ESITO_CONTROLLO_UNAR
 * 
 * @author Mauro Vocale
 *
 */
public class EsitoControlloUnarVO implements Serializable {

	private static final long serialVersionUID = 2832926533640120880L;
	
	private Long idEsitoControlloUnar = null;
	private Long idStoricoUnitaArborea = null;
	private Long idControllo = null;
	private CodeDescription controllo = null;
	private String descrizione = null;
	private String bloccante = null;
	
	/**
	 * Costruttore di default 
	 */
	public EsitoControlloUnarVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idEsitoControlloUnar
	 * @param idStoricoUnitaArborea
	 * @param idControllo
	 * @param controllo
	 * @param descrizione
	 * @param bloccante
	 */
	public EsitoControlloUnarVO(Long idEsitoControlloUnar, Long idStoricoUnitaArborea, Long idControllo, CodeDescription controllo, String descrizione, String bloccante) {
		super();
		this.idEsitoControlloUnar = idEsitoControlloUnar;
		this.idStoricoUnitaArborea = idStoricoUnitaArborea;
		this.idControllo = idControllo;
		this.controllo = controllo;
		this.descrizione = descrizione;
		this.bloccante = bloccante;
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
	 * @return the idEsitoControlloUnar
	 */
	public Long getIdEsitoControlloUnar() {
		return idEsitoControlloUnar;
	}

	/**
	 * @param idEsitoControlloUnar the idEsitoControlloUnar to set
	 */
	public void setIdEsitoControlloUnar(Long idEsitoControlloUnar) {
		this.idEsitoControlloUnar = idEsitoControlloUnar;
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
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((bloccante == null) ? 0 : bloccante.hashCode());
		result = PRIME * result + ((controllo == null) ? 0 : controllo.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idControllo == null) ? 0 : idControllo.hashCode());
		result = PRIME * result + ((idEsitoControlloUnar == null) ? 0 : idEsitoControlloUnar.hashCode());
		result = PRIME * result + ((idStoricoUnitaArborea == null) ? 0 : idStoricoUnitaArborea.hashCode());
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
		final EsitoControlloUnarVO other = (EsitoControlloUnarVO) obj;
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
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idControllo == null) {
			if (other.idControllo != null)
				return false;
		} else if (!idControllo.equals(other.idControllo))
			return false;
		if (idEsitoControlloUnar == null) {
			if (other.idEsitoControlloUnar != null)
				return false;
		} else if (!idEsitoControlloUnar.equals(other.idEsitoControlloUnar))
			return false;
		if (idStoricoUnitaArborea == null) {
			if (other.idStoricoUnitaArborea != null)
				return false;
		} else if (!idStoricoUnitaArborea.equals(other.idStoricoUnitaArborea))
			return false;
		return true;
	}
	
}