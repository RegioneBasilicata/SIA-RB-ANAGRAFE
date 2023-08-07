package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_CAUSALE_MODIFICA
 * 
 * @author Mauro Vocale
 *
 */
public class TipoCausaleModificaVO implements Serializable {
	
	private static final long serialVersionUID = -5746870919501534633L;
	
	private Long idCausaleModifica = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private String altroProcedimento = null;

	

	/**
	 * @return the dataFineValidita
	 */
	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the dataInizioValidita
	 */
	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the idCausaleModifica
	 */
	public Long getIdCausaleModifica() {
		return idCausaleModifica;
	}

	/**
	 * @param idCausaleModifica the idCausaleModifica to set
	 */
	public void setIdCausaleModifica(Long idCausaleModifica) {
		this.idCausaleModifica = idCausaleModifica;
	}

  public String getAltroProcedimento()
  {
    return altroProcedimento;
  }

  public void setAltroProcedimento(String altroProcedimento)
  {
    this.altroProcedimento = altroProcedimento;
  }
	
	
	
	

}
