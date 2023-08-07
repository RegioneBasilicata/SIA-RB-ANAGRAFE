package it.csi.solmr.dto.anag.terreni;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_ALTRO_VITIGNO
 * 
 * @author Mauro Vocale
 *
 */
public class AltroVitignoVO implements Serializable {

	private static final long serialVersionUID = 4957376347660437997L;
	
	private Long idAltroVitigno = null;
	private Long idStoricoUnitaArborea = null;
	private String percentualeVitigno = null;
	private Long idVarieta = null;
	private TipoVarietaVO tipoVarietaVO = null;
	
	/**
	 * Costruttore di default 
	 */
	public AltroVitignoVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idAltroVitigno
	 * @param idStoricoUnitaArborea
	 * @param percentualeVitigno
	 * @param idVarieta
	 * @param tipoVarietaVO
	 */
	public AltroVitignoVO(Long idAltroVitigno, Long idStoricoUnitaArborea, String percentualeVitigno, Long idVarieta, TipoVarietaVO tipoVarietaVO) {
		super();
		this.idAltroVitigno = idAltroVitigno;
		this.idStoricoUnitaArborea = idStoricoUnitaArborea;
		this.percentualeVitigno = percentualeVitigno;
		this.idVarieta = idVarieta;
		this.tipoVarietaVO = tipoVarietaVO;
	}

	/**
	 * @return the idAltroVitigno
	 */
	public Long getIdAltroVitigno() {
		return idAltroVitigno;
	}

	/**
	 * @param idAltroVitigno the idAltroVitigno to set
	 */
	public void setIdAltroVitigno(Long idAltroVitigno) {
		this.idAltroVitigno = idAltroVitigno;
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
	 * @return the idVarieta
	 */
	public Long getIdVarieta() {
		return idVarieta;
	}

	/**
	 * @param idVarieta the idVarieta to set
	 */
	public void setIdVarieta(Long idVarieta) {
		this.idVarieta = idVarieta;
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
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((idAltroVitigno == null) ? 0 : idAltroVitigno.hashCode());
		result = PRIME * result + ((idStoricoUnitaArborea == null) ? 0 : idStoricoUnitaArborea.hashCode());
		result = PRIME * result + ((idVarieta == null) ? 0 : idVarieta.hashCode());
		result = PRIME * result + ((percentualeVitigno == null) ? 0 : percentualeVitigno.hashCode());
		result = PRIME * result + ((tipoVarietaVO == null) ? 0 : tipoVarietaVO.hashCode());
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
		final AltroVitignoVO other = (AltroVitignoVO) obj;
		if (idAltroVitigno == null) {
			if (other.idAltroVitigno != null)
				return false;
		} else if (!idAltroVitigno.equals(other.idAltroVitigno))
			return false;
		if (idStoricoUnitaArborea == null) {
			if (other.idStoricoUnitaArborea != null)
				return false;
		} else if (!idStoricoUnitaArborea.equals(other.idStoricoUnitaArborea))
			return false;
		if (idVarieta == null) {
			if (other.idVarieta != null)
				return false;
		} else if (!idVarieta.equals(other.idVarieta))
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