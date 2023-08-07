package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_TERRAZZAMENTO
 * @author Mauro Vocale
 *
 */
public class TipoTerrazzamentoVO implements Serializable {

	
	
	/**
   * 
   */
  private static final long serialVersionUID = -1301495977221911517L;

  private Long idTerrazzamento = null;
  private String codice = null;
	private String descrizione = null;
	private java.util.Date dataInizioValidita = null;
	private java.util.Date dataFineValidita = null;
	
	

	/**
	 * @return the dataFineValidita
	 */
	public java.util.Date getDataFineValidita() {
		return dataFineValidita;
	}

	/**
	 * @param dataFineValidita the dataFineValidita to set
	 */
	public void setDataFineValidita(java.util.Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	/**
	 * @return the dataInizioValidita
	 */
	public java.util.Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	/**
	 * @param dataInizioValidita the dataInizioValidita to set
	 */
	public void setDataInizioValidita(java.util.Date dataInizioValidita) {
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
	
	
	public Long getIdTerrazzamento()
  {
    return idTerrazzamento;
  }

  public void setIdTerrazzamento(Long idTerrazzamento)
  {
    this.idTerrazzamento = idTerrazzamento;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }
  
  
  

	
	
}