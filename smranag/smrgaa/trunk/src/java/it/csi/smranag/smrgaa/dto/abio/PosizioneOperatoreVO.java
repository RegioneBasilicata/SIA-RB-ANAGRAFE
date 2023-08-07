package it.csi.smranag.smrgaa.dto.abio;

import java.io.Serializable;
import java.util.Date;

public class PosizioneOperatoreVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6711701622434833665L;
	
	private long idPosizioneOperatore;				//ABIO_T_POSIZIONE_OPERATORE.ID_POSIZIONE_OPERATORE
	private long idOperatoreBiologico;				//ABIO_T_POSIZIONE_OPERATORE.ID_OPERATORE_BIOLOGICO
	private long idCategoriaAttivita;				//ABIO_T_POSIZIONE_OPERATORE.ID_CATEGORIA_ATTIVITA
	private String descCategoria;					//ABIO_D_CATEGORIA_ATTIVITA.DESCRIZIONE
	private long idStatoAttivita;					//ABIO_T_POSIZIONE_OPERATORE.ID_STATO_ATTIVITA
	private String descStatoAttivita;				//ABIO_D_STATO_ATTIVITA.DESCRIZIONE
	private Date dataInizioValidita;				//ABIO_T_POSIZIONE_OPERATORE.DATA_INIZIO_ATTIVITA
	private Date dataFineValidita;					//ABIO_T_POSIZIONE_OPERATORE.DATA_FINE_ATTIVITA
	
	public long getIdPosizioneOperatore() {
		return idPosizioneOperatore;
	}
	public void setIdPosizioneOperatore(long idPosizioneOperatore) {
		this.idPosizioneOperatore = idPosizioneOperatore;
	}
	public long getIdOperatoreBiologico() {
		return idOperatoreBiologico;
	}
	public void setIdOperatoreBiologico(long idOperatoreBiologico) {
		this.idOperatoreBiologico = idOperatoreBiologico;
	}
	public long getIdCategoriaAttivita() {
		return idCategoriaAttivita;
	}
	public void setIdCategoriaAttivita(long idCategoriaAttivita) {
		this.idCategoriaAttivita = idCategoriaAttivita;
	}
	public String getDescCategoria() {
		return descCategoria;
	}
	public void setDescCategoria(String descCategoria) {
		this.descCategoria = descCategoria;
	}
	public long getIdStatoAttivita() {
		return idStatoAttivita;
	}
	public void setIdStatoAttivita(long idStatoAttivita) {
		this.idStatoAttivita = idStatoAttivita;
	}
	public String getDescStatoAttivita() {
		return descStatoAttivita;
	}
	public void setDescStatoAttivita(String descStatoAttivita) {
		this.descStatoAttivita = descStatoAttivita;
	}
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
	public Date getDataFineValidita() {
		return dataFineValidita;
	}
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	
}
