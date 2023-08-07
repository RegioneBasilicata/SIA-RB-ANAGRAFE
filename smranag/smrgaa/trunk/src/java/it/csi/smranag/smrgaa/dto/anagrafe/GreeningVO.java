package it.csi.smranag.smrgaa.dto.anagrafe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

public class GreeningVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idTipoGreening;
	private String descTipoGreening;
	private Long idAziendaGreening;
	private String descEsitoGreening;
	private String valoreCalcolato;
	private BigDecimal valoreCalcolatoNumber;
	private Date valoreCalcolatoDate;
	private Vector<String> listaDescTipiEsonero;
	
	public Long getIdTipoGreening() {
		return idTipoGreening;
	}
	public void setIdTipoGreening(Long idTipoGreening) {
		this.idTipoGreening = idTipoGreening;
	}
	public String getDescTipoGreening() {
		return descTipoGreening;
	}
	public void setDescTipoGreening(String descTipoGreening) {
		this.descTipoGreening = descTipoGreening;
	}
	public Long getIdAziendaGreening() {
		return idAziendaGreening;
	}
	public void setIdAziendaGreening(Long idAziendaGreening) {
		this.idAziendaGreening = idAziendaGreening;
	}
	public String getDescEsitoGreening() {
		return descEsitoGreening;
	}
	public void setDescEsitoGreening(String descEsitoGreening) {
		this.descEsitoGreening = descEsitoGreening;
	}
	public String getValoreCalcolato() {
		return valoreCalcolato;
	}
	public void setValoreCalcolato(String valoreCalcolato) {
		this.valoreCalcolato = valoreCalcolato;
	}
	public BigDecimal getValoreCalcolatoNumber() {
		return valoreCalcolatoNumber;
	}
	public void setValoreCalcolatoNumber(BigDecimal valoreCalcolatoNumber) {
		this.valoreCalcolatoNumber = valoreCalcolatoNumber;
	}
	public Date getValoreCalcolatoDate() {
		return valoreCalcolatoDate;
	}
	public void setValoreCalcolatoDate(Date valoreCalcolatoDate) {
		this.valoreCalcolatoDate = valoreCalcolatoDate;
	}
	public Vector<String> getListaDescTipiEsonero() {
		return listaDescTipiEsonero;
	}
	public void setListaDescTipiEsonero(Vector<String> listaDescTipiEsonero) {
		this.listaDescTipiEsonero = listaDescTipiEsonero;
	}
 
}
