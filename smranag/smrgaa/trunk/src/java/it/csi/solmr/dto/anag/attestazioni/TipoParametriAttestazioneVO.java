package it.csi.solmr.dto.anag.attestazioni;

import java.io.*;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_PARAMETRI_ATTESTAZIONE
 * 
 * @author Mauro Vocale
 *
 */
public class TipoParametriAttestazioneVO implements Serializable {
	
	private static final long serialVersionUID = -5612003353170741512L;
	
	private Long idParametriAttestazione = null;
	private Long idAttestazione = null;
	private String parametro1 = null;
	private String parametro2 = null;
	private String parametro3 = null;
	private String parametro4 = null;
	private String parametro5 = null;
	private String obbligatorio1 = null;
	private String obbligatorio2 = null;
	private String obbligatorio3 = null;
	private String obbligatorio4 = null;
	private String obbligatorio5 = null;
	
	/**
	 * Costruttore di default 
	 */
	public TipoParametriAttestazioneVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idParametriAttestazione
	 * @param idAttestazione
	 * @param parametro1
	 * @param parametro2
	 * @param parametro3
	 * @param parametro4
	 * @param parametro5
	 * @param obbligatorio1
	 * @param obbligatorio2
	 * @param obbligatorio3
	 * @param obbligatorio4
	 * @param obbligatorio5
	 */
	public TipoParametriAttestazioneVO(Long idParametriAttestazione, Long idAttestazione, String parametro1, String parametro2, String parametro3, String parametro4, String parametro5, String obbligatorio1, String obbligatorio2, String obbligatorio3, String obbligatorio4, String obbligatorio5) {
		super();
		this.idParametriAttestazione = idParametriAttestazione;
		this.idAttestazione = idAttestazione;
		this.parametro1 = parametro1;
		this.parametro2 = parametro2;
		this.parametro3 = parametro3;
		this.parametro4 = parametro4;
		this.parametro5 = parametro5;
		this.obbligatorio1 = obbligatorio1;
		this.obbligatorio2 = obbligatorio2;
		this.obbligatorio3 = obbligatorio3;
		this.obbligatorio4 = obbligatorio4;
		this.obbligatorio5 = obbligatorio5;
	}

	/**
	 * @return the idParametriAttestazione
	 */
	public Long getIdParametriAttestazione() {
		return idParametriAttestazione;
	}

	/**
	 * @param idParametriAttestazione the idParametriAttestazione to set
	 */
	public void setIdParametriAttestazione(Long idParametriAttestazione) {
		this.idParametriAttestazione = idParametriAttestazione;
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
	 * @return the obbligatorio1
	 */
	public String getObbligatorio1() {
		return obbligatorio1;
	}

	/**
	 * @param obbligatorio1 the obbligatorio1 to set
	 */
	public void setObbligatorio1(String obbligatorio1) {
		this.obbligatorio1 = obbligatorio1;
	}

	/**
	 * @return the obbligatorio2
	 */
	public String getObbligatorio2() {
		return obbligatorio2;
	}

	/**
	 * @param obbligatorio2 the obbligatorio2 to set
	 */
	public void setObbligatorio2(String obbligatorio2) {
		this.obbligatorio2 = obbligatorio2;
	}

	/**
	 * @return the obbligatorio3
	 */
	public String getObbligatorio3() {
		return obbligatorio3;
	}

	/**
	 * @param obbligatorio3 the obbligatorio3 to set
	 */
	public void setObbligatorio3(String obbligatorio3) {
		this.obbligatorio3 = obbligatorio3;
	}

	/**
	 * @return the obbligatorio4
	 */
	public String getObbligatorio4() {
		return obbligatorio4;
	}

	/**
	 * @param obbligatorio4 the obbligatorio4 to set
	 */
	public void setObbligatorio4(String obbligatorio4) {
		this.obbligatorio4 = obbligatorio4;
	}

	/**
	 * @return the obbligatorio5
	 */
	public String getObbligatorio5() {
		return obbligatorio5;
	}

	/**
	 * @param obbligatorio5 the obbligatorio5 to set
	 */
	public void setObbligatorio5(String obbligatorio5) {
		this.obbligatorio5 = obbligatorio5;
	}

	/**
	 * Metodo hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAttestazione == null) ? 0 : idAttestazione.hashCode());
		result = prime * result + ((idParametriAttestazione == null) ? 0 : idParametriAttestazione.hashCode());
		result = prime * result + ((obbligatorio1 == null) ? 0 : obbligatorio1.hashCode());
		result = prime * result + ((obbligatorio2 == null) ? 0 : obbligatorio2.hashCode());
		result = prime * result + ((obbligatorio3 == null) ? 0 : obbligatorio3.hashCode());
		result = prime * result + ((obbligatorio4 == null) ? 0 : obbligatorio4.hashCode());
		result = prime * result + ((obbligatorio5 == null) ? 0 : obbligatorio5.hashCode());
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
		final TipoParametriAttestazioneVO other = (TipoParametriAttestazioneVO) obj;
		if (idAttestazione == null) {
			if (other.idAttestazione != null)
				return false;
		} else if (!idAttestazione.equals(other.idAttestazione))
			return false;
		if (idParametriAttestazione == null) {
			if (other.idParametriAttestazione != null)
				return false;
		} else if (!idParametriAttestazione
				.equals(other.idParametriAttestazione))
			return false;
		if (obbligatorio1 == null) {
			if (other.obbligatorio1 != null)
				return false;
		} else if (!obbligatorio1.equals(other.obbligatorio1))
			return false;
		if (obbligatorio2 == null) {
			if (other.obbligatorio2 != null)
				return false;
		} else if (!obbligatorio2.equals(other.obbligatorio2))
			return false;
		if (obbligatorio3 == null) {
			if (other.obbligatorio3 != null)
				return false;
		} else if (!obbligatorio3.equals(other.obbligatorio3))
			return false;
		if (obbligatorio4 == null) {
			if (other.obbligatorio4 != null)
				return false;
		} else if (!obbligatorio4.equals(other.obbligatorio4))
			return false;
		if (obbligatorio5 == null) {
			if (other.obbligatorio5 != null)
				return false;
		} else if (!obbligatorio5.equals(other.obbligatorio5))
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