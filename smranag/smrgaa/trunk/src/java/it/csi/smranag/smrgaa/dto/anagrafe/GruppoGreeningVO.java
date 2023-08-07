package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.util.Vector;

public class GruppoGreeningVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idGruppoGreening;
	private String descGruppoGreening;
	private Vector<GreeningVO> listaGreening;
	
	public Long getIdGruppoGreening() {
		return idGruppoGreening;
	}
	public void setIdGruppoGreening(Long idGruppoGreening) {
		this.idGruppoGreening = idGruppoGreening;
	}
	public String getDescGruppoGreening() {
		return descGruppoGreening;
	}
	public void setDescGruppoGreening(String descGruppoGreening) {
		this.descGruppoGreening = descGruppoGreening;
	}
	public Vector<GreeningVO> getListaGreening() {
		return listaGreening;
	}
	public void setListaGreening(Vector<GreeningVO> listaGreening) {
		this.listaGreening = listaGreening;
	}
	
}
