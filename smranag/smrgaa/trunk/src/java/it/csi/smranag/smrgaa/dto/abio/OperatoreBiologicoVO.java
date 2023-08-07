package it.csi.smranag.smrgaa.dto.abio;

import java.io.Serializable;
import java.util.Date;

public class OperatoreBiologicoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3904092065511948065L;

	private long idOperatoreBiologico;            // ABIO_T_OPERATORE_BIOLOGICO.ID_OPERATORE_BIOLOGICO
	private Date dataInizioAttivita;              // ABIO_T_OPERATORE_BIOLOGICO.DATA_INIZIO_ATTIVITA
	private Date dataFineAttivita;                // ABIO_T_OPERATORE_BIOLOGICO.DATA_FINE_ATTIVITA
	private long extIdAzienda;                    // ABIO_T_OPERATORE_BIOLOGICO.EXT_ID_AZIENDA
	
	public long getIdOperatoreBiologico() {
		return idOperatoreBiologico;
	}
	
	public void setIdOperatoreBiologico(long idOperatoreBiologico) {
		this.idOperatoreBiologico = idOperatoreBiologico;
	}
	
	public Date getDataInizioAttivita() {
		return dataInizioAttivita;
	}
	
	public void setDataInizioAttivita(Date dataInizioAttivita) {
		this.dataInizioAttivita = dataInizioAttivita;
	}
	
	public Date getDataFineAttivita() {
		return dataFineAttivita;
	}
	
	public void setDataFineAttivita(Date dataFineAttivita) {
		this.dataFineAttivita = dataFineAttivita;
	}
	
	public long getExtIdAzienda() {
		return extIdAzienda;
	}
	
	public void setExtIdAzienda(long extIdAzienda) {
		this.extIdAzienda = extIdAzienda;
	}

	
}