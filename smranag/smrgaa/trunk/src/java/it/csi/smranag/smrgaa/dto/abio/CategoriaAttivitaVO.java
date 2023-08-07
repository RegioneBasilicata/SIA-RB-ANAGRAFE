package it.csi.smranag.smrgaa.dto.abio;

import java.io.Serializable;
import java.util.Date;

public class CategoriaAttivitaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4125154896488736611L;

	private long idCategoriaAttivita;				//ABIO_D_CATEGORIA_ATTIVITA.ID_CATEGORIA_ATTIVITA
	private String descrizione;						//ABIO_D_CATEGORIA_ATTIVITA.DESCRIZIONE
	private Date dataInizioAttivita;				//ABIO_D_CATEGORIA_ATTIVITA.DATA_INIZIO_ATTIVITA
	private Date dataFineValidita;					//ABIO_D_CATEGORIA_ATTIVITA.DATA_FINE_VALIDITA
	
	public long getIdCategoriaAttivita() {
		return idCategoriaAttivita;
	}
	public void setIdCategoriaAttivita(long idCategoriaAttivita) {
		this.idCategoriaAttivita = idCategoriaAttivita;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Date getDataInizioAttivita() {
		return dataInizioAttivita;
	}
	public void setDataInizioAttivita(Date dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}
	public Date getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
}