package it.csi.solmr.dto.anag.fabbricati;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * Classe che si occupa di mappare la tabella DB_TIPOLOGIA_FABBRICATO
 * 
 * @author Mauro Vocale
 *
 */
public class TipoTipologiaFabbricatoVO implements Serializable {

	private static final long serialVersionUID = -5547244684973646481L;
	
	private Long idTipologiaFabbricato = null;
	private String descrizione = null;
	private String unitaMisura = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private String            flagStoccaggio;                      // DB_TIPO_TIPOLOGIA_FABBRICATO.FLAG_PER_STOCCAGGIO
  private String            flagPalabile;                        // DB_TIPO_TIPOLOGIA_FABBRICATO.FLAG_PALABILE
  private String            tipoFormula;                        // DB_TIPO_TIPOLOGIA_FABBRICATO.TIPO_FORMULA
  private Vector<String>            vLabel;                             //String array di DB_FABB_DIM_TIPOLOGIA.TESTO_LAYER 
  private String obbligoParticella;
  private String obbligoCoordinate;
	
	
	/**
	 * Costruttore di default
	 */
	public TipoTipologiaFabbricatoVO() {
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idTipologiaFabbricato
	 * @param descrizione
	 * @param unitaMisura
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 */
	public TipoTipologiaFabbricatoVO(Long idTipologiaFabbricato, String descrizione, String unitaMisura, Date dataInizioValidita, Date dataFineValidita) {
		super();
		this.idTipologiaFabbricato = idTipologiaFabbricato;
		this.descrizione = descrizione;
		this.unitaMisura = unitaMisura;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
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

	/**
	 * @return the unitaMisura
	 */
	public String getUnitaMisura() {
		return unitaMisura;
	}

	/**
	 * @param unitaMisura the unitaMisura to set
	 */
	public void setUnitaMisura(String unitaMisura) {
		this.unitaMisura = unitaMisura;
	}

	/**
	 * Metodo hashCode()
	 */
	/*public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((dataFineValidita == null) ? 0 : dataFineValidita.hashCode());
		result = PRIME * result + ((dataInizioValidita == null) ? 0 : dataInizioValidita.hashCode());
		result = PRIME * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = PRIME * result + ((idTipologiaFabbricato == null) ? 0 : idTipologiaFabbricato.hashCode());
		result = PRIME * result + ((unitaMisura == null) ? 0 : unitaMisura.hashCode());
		return result;
	}*/

	/**
	 * Metodo equals()
	 */
	/*public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TipoTipologiaFabbricatoVO other = (TipoTipologiaFabbricatoVO) obj;
		if (dataFineValidita == null) {
			if (other.dataFineValidita != null)
				return false;
		} else if (!dataFineValidita.equals(other.dataFineValidita))
			return false;
		if (dataInizioValidita == null) {
			if (other.dataInizioValidita != null)
				return false;
		} else if (!dataInizioValidita.equals(other.dataInizioValidita))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (idTipologiaFabbricato == null) {
			if (other.idTipologiaFabbricato != null)
				return false;
		} else if (!idTipologiaFabbricato.equals(other.idTipologiaFabbricato))
			return false;
		if (unitaMisura == null) {
			if (other.unitaMisura != null)
				return false;
		} else if (!unitaMisura.equals(other.unitaMisura))
			return false;
		return true;
	}*/

  public String getFlagStoccaggio()
  {
    return flagStoccaggio;
  }

  public void setFlagStoccaggio(String flagStoccaggio)
  {
    this.flagStoccaggio = flagStoccaggio;
  }

  public String getFlagPalabile()
  {
    return flagPalabile;
  }

  public void setFlagPalabile(String flagPalabile)
  {
    this.flagPalabile = flagPalabile;
  }

  public String getTipoFormula()
  {
    return tipoFormula;
  }

  public void setTipoFormula(String tipoFormula)
  {
    this.tipoFormula = tipoFormula;
  }

  public Vector<String> getVLabel()
  {
    return vLabel;
  }

  public void setVLabel(Vector<String> label)
  {
    vLabel = label;
  }

  public String getObbligoParticella()
  {
    return obbligoParticella;
  }

  public void setObbligoParticella(String obbligoParticella)
  {
    this.obbligoParticella = obbligoParticella;
  }

  public String getObbligoCoordinate()
  {
    return obbligoCoordinate;
  }

  public void setObbligoCoordinate(String obbligoCoordinate)
  {
    this.obbligoCoordinate = obbligoCoordinate;
  }
  
  

}
