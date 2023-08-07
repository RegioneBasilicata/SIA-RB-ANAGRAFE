package it.csi.solmr.dto.anag.terreni;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_ALTRO_VITIGNO_DICHIARATO
 * 
 * @author Mauro Vocale
 *
 */
public class AltroVitignoDichiaratoVO implements Serializable {

	private static final long serialVersionUID = -5430450155154619489L;
	
	private Long idAltroVitignoDichiarato = null;
	private Long idUnitaArboreaDichiarata = null;
	private Long idVarietaUnar = null;
	private TipoVarietaVO tipoVarietaVO = null;
	private String percentualeVitigno = null;
	
	/**
	 * Costruttore di default 
	 */
	public AltroVitignoDichiaratoVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idAltroVitignoDichiarato
	 * @param idUnitaArboreaDichiarata
	 * @param idVarietaUnar
	 * @param tipoVarietaVO
	 * @param percentualeVitigno
	 */
	public AltroVitignoDichiaratoVO(Long idAltroVitignoDichiarato,
			Long idUnitaArboreaDichiarata, Long idVarietaUnar,
			TipoVarietaVO tipoVarietaVO, String percentualeVitigno) {
		super();
		this.idAltroVitignoDichiarato = idAltroVitignoDichiarato;
		this.idUnitaArboreaDichiarata = idUnitaArboreaDichiarata;
		this.idVarietaUnar = idVarietaUnar;
		this.tipoVarietaVO = tipoVarietaVO;
		this.percentualeVitigno = percentualeVitigno;
	}

	/**
	 * @return the idAltroVitignoDichiarato
	 */
	public Long getIdAltroVitignoDichiarato() {
		return idAltroVitignoDichiarato;
	}

	/**
	 * @param idAltroVitignoDichiarato the idAltroVitignoDichiarato to set
	 */
	public void setIdAltroVitignoDichiarato(Long idAltroVitignoDichiarato) {
		this.idAltroVitignoDichiarato = idAltroVitignoDichiarato;
	}

	/**
	 * @return the idUnitaArboreaDichiarata
	 */
	public Long getIdUnitaArboreaDichiarata() {
		return idUnitaArboreaDichiarata;
	}

	/**
	 * @param idUnitaArboreaDichiarata the idUnitaArboreaDichiarata to set
	 */
	public void setIdUnitaArboreaDichiarata(Long idUnitaArboreaDichiarata) {
		this.idUnitaArboreaDichiarata = idUnitaArboreaDichiarata;
	}

	/**
	 * @return the idVarietaUnar
	 */
	public Long getIdVarietaUnar() {
		return idVarietaUnar;
	}

	/**
	 * @param idVarietaUnar the idVarietaUnar to set
	 */
	public void setIdVarietaUnar(Long idVarietaUnar) {
		this.idVarietaUnar = idVarietaUnar;
	}

	/**
	 * @return the tipoVarietaVO
	 */
	public TipoVarietaVO getTipoVarietaVO() {
		return tipoVarietaVO;
	}

	/**
	 * @param tipoVarietaVO the tipoVarietaVO to set
	 */
	public void setTipoVarietaVO(TipoVarietaVO tipoVarietaVO) {
		this.tipoVarietaVO = tipoVarietaVO;
	}

	/**
	 * @return the percentualeVitigno
	 */
	public String getPercentualeVitigno() {
		return percentualeVitigno;
	}

	/**
	 * @param percentualeVitigno the percentualeVitigno to set
	 */
	public void setPercentualeVitigno(String percentualeVitigno) {
		this.percentualeVitigno = percentualeVitigno;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idAltroVitignoDichiarato == null) ? 0
						: idAltroVitignoDichiarato.hashCode());
		result = prime
				* result
				+ ((idUnitaArboreaDichiarata == null) ? 0
						: idUnitaArboreaDichiarata.hashCode());
		result = prime * result
				+ ((idVarietaUnar == null) ? 0 : idVarietaUnar.hashCode());
		result = prime
				* result
				+ ((percentualeVitigno == null) ? 0 : percentualeVitigno
						.hashCode());
		result = prime * result
				+ ((tipoVarietaVO == null) ? 0 : tipoVarietaVO.hashCode());
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
		final AltroVitignoDichiaratoVO other = (AltroVitignoDichiaratoVO) obj;
		if (idAltroVitignoDichiarato == null) {
			if (other.idAltroVitignoDichiarato != null)
				return false;
		} else if (!idAltroVitignoDichiarato
				.equals(other.idAltroVitignoDichiarato))
			return false;
		if (idUnitaArboreaDichiarata == null) {
			if (other.idUnitaArboreaDichiarata != null)
				return false;
		} else if (!idUnitaArboreaDichiarata
				.equals(other.idUnitaArboreaDichiarata))
			return false;
		if (idVarietaUnar == null) {
			if (other.idVarietaUnar != null)
				return false;
		} else if (!idVarietaUnar.equals(other.idVarietaUnar))
			return false;
		if (percentualeVitigno == null) {
			if (other.percentualeVitigno != null)
				return false;
		} else if (!percentualeVitigno.equals(other.percentualeVitigno))
			return false;
		if (tipoVarietaVO == null) {
			if (other.tipoVarietaVO != null)
				return false;
		} else if (!tipoVarietaVO.equals(other.tipoVarietaVO))
			return false;
		return true;
	}
	
}