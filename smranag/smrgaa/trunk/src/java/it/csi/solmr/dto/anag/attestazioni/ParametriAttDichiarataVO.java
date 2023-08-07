package it.csi.solmr.dto.anag.attestazioni;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_PARAMETRI_ATT_DICHIARATA
 * 
 * @author Mauro Vocale
 *
 */
public class ParametriAttDichiarataVO implements Serializable {
	
	private static final long serialVersionUID = 8896579273321443003L;
	
	private Long idParametriAttDichiarata = null;
	private Long idAttestazioneDichiarata = null;
	private String parametro1 = null;
	private String parametro2 = null;
	private String parametro3 = null;
	private String parametro4 = null;
	private String parametro5 = null;
	
	/**
	 * Costruttore di default 
	 */
	public ParametriAttDichiarataVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idParametriAttDichiarata
	 * @param idAttestazioneDichiarata
	 * @param parametro1
	 * @param parametro2
	 * @param parametro3
	 * @param parametro4
	 * @param parametro5
	 */
	public ParametriAttDichiarataVO(Long idParametriAttDichiarata, Long idAttestazioneDichiarata, String parametro1, String parametro2, String parametro3, String parametro4, String parametro5) {
		super();
		this.idParametriAttDichiarata = idParametriAttDichiarata;
		this.idAttestazioneDichiarata = idAttestazioneDichiarata;
		this.parametro1 = parametro1;
		this.parametro2 = parametro2;
		this.parametro3 = parametro3;
		this.parametro4 = parametro4;
		this.parametro5 = parametro5;
	}

	/**
	 * @return the idParametriAttDichiarata
	 */
	public Long getIdParametriAttDichiarata() {
		return idParametriAttDichiarata;
	}

	/**
	 * @param idParametriAttDichiarata the idParametriAttDichiarata to set
	 */
	public void setIdParametriAttDichiarata(Long idParametriAttDichiarata) {
		this.idParametriAttDichiarata = idParametriAttDichiarata;
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
	 * @return the parametro1
	 */
	public String getParametro1() {
		return parametro1;
	}

	/**
	 * @param parametro1 the parametro1 to set
	 */
	public void setParametro1(String parametro1) {
		this.parametro1 = parametro1;
	}

	/**
	 * @return the parametro2
	 */
	public String getParametro2() {
		return parametro2;
	}

	/**
	 * @param parametro2 the parametro2 to set
	 */
	public void setParametro2(String parametro2) {
		this.parametro2 = parametro2;
	}

	/**
	 * @return the parametro3
	 */
	public String getParametro3() {
		return parametro3;
	}

	/**
	 * @param parametro3 the parametro3 to set
	 */
	public void setParametro3(String parametro3) {
		this.parametro3 = parametro3;
	}

	/**
	 * @return the parametro4
	 */
	public String getParametro4() {
		return parametro4;
	}

	/**
	 * @param parametro4 the parametro4 to set
	 */
	public void setParametro4(String parametro4) {
		this.parametro4 = parametro4;
	}

	/**
	 * @return the parametro5
	 */
	public String getParametro5() {
		return parametro5;
	}

	/**
	 * @param parametro5 the parametro5 to set
	 */
	public void setParametro5(String parametro5) {
		this.parametro5 = parametro5;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAttestazioneDichiarata == null) ? 0 : idAttestazioneDichiarata.hashCode());
		result = prime * result + ((idParametriAttDichiarata == null) ? 0 : idParametriAttDichiarata.hashCode());
		result = prime * result + ((parametro1 == null) ? 0 : parametro1.hashCode());
		result = prime * result + ((parametro2 == null) ? 0 : parametro2.hashCode());
		result = prime * result + ((parametro3 == null) ? 0 : parametro3.hashCode());
		result = prime * result + ((parametro4 == null) ? 0 : parametro4.hashCode());
		result = prime * result + ((parametro5 == null) ? 0 : parametro5.hashCode());
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
		final ParametriAttDichiarataVO other = (ParametriAttDichiarataVO) obj;
		if (idAttestazioneDichiarata == null) {
			if (other.idAttestazioneDichiarata != null)
				return false;
		} else if (!idAttestazioneDichiarata.equals(other.idAttestazioneDichiarata))
			return false;
		if (idParametriAttDichiarata == null) {
			if (other.idParametriAttDichiarata != null)
				return false;
		} else if (!idParametriAttDichiarata
				.equals(other.idParametriAttDichiarata))
			return false;
		if (parametro1 == null) {
			if (other.parametro1 != null)
				return false;
		} else if (!parametro1.equals(other.parametro1))
			return false;
		if (parametro2 == null) {
			if (other.parametro2 != null)
				return false;
		} else if (!parametro2.equals(other.parametro2))
			return false;
		if (parametro3 == null) {
			if (other.parametro3 != null)
				return false;
		} else if (!parametro3.equals(other.parametro3))
			return false;
		if (parametro4 == null) {
			if (other.parametro4 != null)
				return false;
		} else if (!parametro4.equals(other.parametro4))
			return false;
		if (parametro5 == null) {
			if (other.parametro5 != null)
				return false;
		} else if (!parametro5.equals(other.parametro5))
			return false;
		return true;
	}

}