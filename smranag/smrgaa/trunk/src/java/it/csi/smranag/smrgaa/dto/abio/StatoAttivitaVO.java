package it.csi.smranag.smrgaa.dto.abio;

import java.io.Serializable;
import java.util.Date;

public class StatoAttivitaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6526505235611042887L;

	private long idStatoAttivita;				//ABIO_D_STATO_ATTIVITA.ID_STATO_ATTIVITA
	private String descrizione;					//ABIO_D_STATO_ATTIVITA.DESCRIZIONE
	private Date dataInizioAttivita;			//ABIO_D_STATO_ATTIVITA.DATA_INIZIO_ATTIVITA
	private Date dataFineValidita;				//ABIO_D_STATO_ATTIVITA.DATA_FINE_VALIDITA
	
	public long getIdStatoAttivita() {
		return idStatoAttivita;
	}
	public void setIdStatoAttivita(long idStatoAttivita) {
		this.idStatoAttivita = idStatoAttivita;
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