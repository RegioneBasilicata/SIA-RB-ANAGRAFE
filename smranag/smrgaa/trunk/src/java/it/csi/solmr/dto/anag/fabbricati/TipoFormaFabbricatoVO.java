package it.csi.solmr.dto.anag.fabbricati;

import java.io.Serializable;

/**
 * Classe che si occupa di mappare la tabella DB_FORMA_FABBRICATO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoFormaFabbricatoVO implements Serializable {
	
	private static final long serialVersionUID = -1194988125210947135L;
	
	private Long idFormaFabbricato = null;
	private String descrizione = null;
	private Double fattoreCubatura = null;
	private Long idTipologiaFabbricato = null;
	private Double coeffImpilabile = null;
	
	/**
	 * Costruttore di default
	 */
	public TipoFormaFabbricatoVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idFormaFabbricato
	 * @param descrizione
	 * @param fattoreCubatura
	 * @param idTipologiaFabbricato
	 * @param coeffImpilabile
	 */
	public TipoFormaFabbricatoVO(Long idFormaFabbricato, String descrizione, 
	      Double fattoreCubatura, Long idTipologiaFabbricato, Double coeffImpilabile) {
		super();
		this.idFormaFabbricato = idFormaFabbricato;
		this.descrizione = descrizione;
		this.fattoreCubatura = fattoreCubatura;
		this.idTipologiaFabbricato = idTipologiaFabbricato;
		this.coeffImpilabile = coeffImpilabile;
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
	 * @return the idFormaFabbricato
	 */
	public Long getIdFormaFabbricato() {
		return idFormaFabbricato;
	}

	/**
	 * @param idFormaFabbricato the idFormaFabbricato to set
	 */
	public void setIdFormaFabbricato(Long idFormaFabbricato) {
		this.idFormaFabbricato = idFormaFabbricato;
	}

	/**
	 * @return the idTipologiaFabbricato
	 */
	public Long getIdTipologiaFabbricato() {
		return idTipologiaFabbricato;
	}

	/**
	 * @param idTipologiaFabbricato the idTipologiaFabbricato to set
	 */
	public void setIdTipologiaFabbricato(Long idTipologiaFabbricato) {
		this.idTipologiaFabbricato = idTipologiaFabbricato;
	}

  public Double getFattoreCubatura()
  {
    return fattoreCubatura;
  }

  public void setFattoreCubatura(Double fattoreCubatura)
  {
    this.fattoreCubatura = fattoreCubatura;
  }

  public Double getCoeffImpilabile()
  {
    return coeffImpilabile;
  }

  public void setCoeffImpilabile(Double coeffImpilabile)
  {
    this.coeffImpilabile = coeffImpilabile;
  }

  

	

  

}
