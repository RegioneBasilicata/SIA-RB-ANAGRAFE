package it.csi.solmr.dto.anag.terreni;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe che si occupa di mappare la tabella DB_TIPO_TIPOLOGIA_VINO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoTipologiaVinoVO implements Serializable {
	
	private static final long serialVersionUID = 2995866000446073258L;
	
	private Long idTipologiaVino = null;
	private Long idVino = null;
	private String codice = null;
	private String descrizione = null;
	private String codiceMipaf = null;
	private String vinoDoc = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private BigDecimal resa = null;
	private String flagGestioneVigna = null;
	private VignaVO vignaVO = null;
	private String flagGestioneEtichetta = null;
	private Long densitaMinCeppiHa = null;
	private String flagGestioneMenzione = null;
	private String indicazioneVigna = null;


	/**
	 * @return the idTipologiaVino
	 */
	public Long getIdTipologiaVino() {
		return idTipologiaVino;
	}

	/**
	 * @param idTipologiaVino the idTipologiaVino to set
	 */
	public void setIdTipologiaVino(Long idTipologiaVino) {
		this.idTipologiaVino = idTipologiaVino;
	}

	public BigDecimal getResa()
  {
    return resa;
  }


  public void setResa(BigDecimal resa)
  {
    this.resa = resa;
  }


  /**
	 * @return the idVino
	 */
	public Long getIdVino() {
		return idVino;
	}

	/**
	 * @param idVino the idVino to set
	 */
	public void setIdVino(Long idVino) {
		this.idVino = idVino;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
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

  public String getCodiceMipaf()
  {
    return codiceMipaf;
  }

  public void setCodiceMipaf(String codiceMipaf)
  {
    this.codiceMipaf = codiceMipaf;
  }

  public String getVinoDoc()
  {
    return vinoDoc;
  }

  public void setVinoDoc(String vinoDoc)
  {
    this.vinoDoc = vinoDoc;
  }


  public String getFlagGestioneVigna()
  {
    return flagGestioneVigna;
  }


  public void setFlagGestioneVigna(String flagGestioneVigna)
  {
    this.flagGestioneVigna = flagGestioneVigna;
  }

  public VignaVO getVignaVO()
  {
    return vignaVO;
  }

  public void setVignaVO(VignaVO vignaVO)
  {
    this.vignaVO = vignaVO;
  }

  public String getFlagGestioneEtichetta()
  {
    return flagGestioneEtichetta;
  }

  public void setFlagGestioneEtichetta(String flagGestioneEtichetta)
  {
    this.flagGestioneEtichetta = flagGestioneEtichetta;
  }

  public Long getDensitaMinCeppiHa()
  {
    return densitaMinCeppiHa;
  }

  public void setDensitaMinCeppiHa(Long densitaMinCeppiHa)
  {
    this.densitaMinCeppiHa = densitaMinCeppiHa;
  }

  public String getFlagGestioneMenzione()
  {
    return flagGestioneMenzione;
  }

  public void setFlagGestioneMenzione(String flagGestioneMenzione)
  {
    this.flagGestioneMenzione = flagGestioneMenzione;
  }

  public String getIndicazioneVigna()
  {
    return indicazioneVigna;
  }

  public void setIndicazioneVigna(String indicazioneVigna)
  {
    this.indicazioneVigna = indicazioneVigna;
  }
  
  
  
  
  
  

}
