package it.csi.solmr.dto.anag;

import it.csi.solmr.dto.anag.terreni.ConduzioneParticellaVO;
import it.csi.solmr.dto.anag.terreni.StoricoParticellaVO;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Nadia B.
 * @version 1.0
 */

public class ErrAnomaliaDicConsistenzaVO
implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -2173231678878497778L;
	private String idGruppoControllo;
	private String descGruppoControllo;
	private String tipoAnomaliaErrore;
	private String descAnomaliaErrore;
	private boolean bloccante;
	private String bloccanteStr;
	private String idDichiarazioneCorrezione;
	private String risoluzione;
	private String idDichiarazioneSegnalazione;
	private String dataEsecuzione;
	private String codiceControllo;
	private String tipoDocumento;
	private String riferimentoDocumento;
	private String ultimaModifica;
	private String flagDocumentoGiustificativo;
	private String idControllo;
	private Long idAzienda;
	private String idStoricoParticella;
	private String extIdTipoDocumento;
	private boolean errExtIdTipoDocumento;
	private boolean errRiferimentoDocumento;
	private boolean noError;
	private Long idDocumento = null;
	private boolean errIdDocumento;
	private String tipoAnomaliaBloccante = null;
	private String tipoAnomaliaWarning = null;
	private String tipoAnomaliaOk = null;
	private StoricoParticellaVO storicoParticellaVO = null;
	private ConduzioneParticellaVO conduzioneParticellaVO = null;
	
	//Campi ultima modifica spezzata
  private Date dataUltimaModifica = null;
  private String utenteUltimaModifica = null;
  private String enteUltimaModifica = null;
	
	/**
	 * Costruttore con i campi
	 */
	public ErrAnomaliaDicConsistenzaVO() {
		super();
	}

	/**
	 * Costruttore con i campi
	 * 
	 * @param idGruppoControllo
	 * @param descGruppoControllo
	 * @param tipoAnomaliaErrore
	 * @param descAnomaliaErrore
	 * @param bloccante
	 * @param bloccanteStr
	 * @param idDichiarazioneCorrezione
	 * @param risoluzione
	 * @param idDichiarazioneSegnalazione
	 * @param dataEsecuzione
	 * @param codiceControllo
	 * @param tipoDocumento
	 * @param riferimentoDocumento
	 * @param ultimaModifica
	 * @param flagDocumentoGiustificativo
	 * @param idControllo
	 * @param idAzienda
	 * @param idStoricoParticella
	 * @param extIdTipoDocumento
	 * @param errExtIdTipoDocumento
	 * @param errRiferimentoDocumento
	 * @param noError
	 * @param idDocumento
	 * @param errIdDocumento
	 * @param tipoAnomaliaBloccante
	 * @param tipoAnomaliaWarning
	 * @param tipoAnomaliaOk
	 */
	public ErrAnomaliaDicConsistenzaVO(String idGruppoControllo, String descGruppoControllo, String tipoAnomaliaErrore, String descAnomaliaErrore, boolean bloccante, String bloccanteStr,String idDichiarazioneCorrezione, String risoluzione, String idDichiarazioneSegnalazione, String dataEsecuzione, String codiceControllo, String tipoDocumento, String riferimentoDocumento, String ultimaModifica, String flagDocumentoGiustificativo, String idControllo, Long idAzienda, String idStoricoParticella, String extIdTipoDocumento, boolean errExtIdTipoDocumento, boolean errRiferimentoDocumento, boolean noError, Long idDocumento, boolean errIdDocumento, String tipoAnomaliaBloccante, String tipoAnomaliaWarning, String tipoAnomaliaOk) {
		super();
		this.idGruppoControllo = idGruppoControllo;
		this.descGruppoControllo = descGruppoControllo;
		this.tipoAnomaliaErrore = tipoAnomaliaErrore;
		this.descAnomaliaErrore = descAnomaliaErrore;
		this.bloccante = bloccante;
		this.bloccanteStr = bloccanteStr;
		this.idDichiarazioneCorrezione = idDichiarazioneCorrezione;
		this.risoluzione = risoluzione;
		this.idDichiarazioneSegnalazione = idDichiarazioneSegnalazione;
		this.dataEsecuzione = dataEsecuzione;
		this.codiceControllo = codiceControllo;
		this.tipoDocumento = tipoDocumento;
		this.riferimentoDocumento = riferimentoDocumento;
		this.ultimaModifica = ultimaModifica;
		this.flagDocumentoGiustificativo = flagDocumentoGiustificativo;
		this.idControllo = idControllo;
		this.idAzienda = idAzienda;
		this.idStoricoParticella = idStoricoParticella;
		this.extIdTipoDocumento = extIdTipoDocumento;
		this.errExtIdTipoDocumento = errExtIdTipoDocumento;
		this.errRiferimentoDocumento = errRiferimentoDocumento;
		this.noError = noError;
		this.idDocumento = idDocumento;
		this.errIdDocumento = errIdDocumento;
		this.tipoAnomaliaBloccante = tipoAnomaliaBloccante;
		this.tipoAnomaliaWarning = tipoAnomaliaWarning;
		this.tipoAnomaliaOk = tipoAnomaliaOk;
	}

	/**
	 * @return the idGruppoControllo
	 */
	public String getIdGruppoControllo() {
		return idGruppoControllo;
	}

	/**
	 * @param idGruppoControllo the idGruppoControllo to set
	 */
	public void setIdGruppoControllo(String idGruppoControllo) {
		this.idGruppoControllo = idGruppoControllo;
	}

	/**
	 * @return the descGruppoControllo
	 */
	public String getDescGruppoControllo() {
		return descGruppoControllo;
	}

	/**
	 * @param descGruppoControllo the descGruppoControllo to set
	 */
	public void setDescGruppoControllo(String descGruppoControllo) {
		this.descGruppoControllo = descGruppoControllo;
	}

	/**
	 * @return the tipoAnomaliaErrore
	 */
	public String getTipoAnomaliaErrore() {
		return tipoAnomaliaErrore;
	}

	/**
	 * @param tipoAnomaliaErrore the tipoAnomaliaErrore to set
	 */
	public void setTipoAnomaliaErrore(String tipoAnomaliaErrore) {
		this.tipoAnomaliaErrore = tipoAnomaliaErrore;
	}

	/**
	 * @return the descAnomaliaErrore
	 */
	public String getDescAnomaliaErrore() {
		return descAnomaliaErrore;
	}

	/**
	 * @param descAnomaliaErrore the descAnomaliaErrore to set
	 */
	public void setDescAnomaliaErrore(String descAnomaliaErrore) {
		this.descAnomaliaErrore = descAnomaliaErrore;
	}

	/**
	 * @return the bloccante
	 */
	public boolean isBloccante() {
		return bloccante;
	}

	/**
	 * @param bloccante the bloccante to set
	 */
	public void setBloccante(boolean bloccante) {
		this.bloccante = bloccante;
	}

	/**
	 * @return the bloccanteStr
	 */
	public String getBloccanteStr() {
		return bloccanteStr;
	}

	/**
	 * @param bloccanteStr the bloccanteStr to set
	 */
	public void setBloccanteStr(String bloccanteStr) {
		this.bloccanteStr = bloccanteStr;
	}

	/**
	 * @return the idDichiarazioneCorrezione
	 */
	public String getIdDichiarazioneCorrezione() {
		return idDichiarazioneCorrezione;
	}

	/**
	 * @param idDichiarazioneCorrezione the idDichiarazioneCorrezione to set
	 */
	public void setIdDichiarazioneCorrezione(String idDichiarazioneCorrezione) {
		this.idDichiarazioneCorrezione = idDichiarazioneCorrezione;
	}

	/**
	 * @return the risoluzione
	 */
	public String getRisoluzione() {
		return risoluzione;
	}

	/**
	 * @param risoluzione the risoluzione to set
	 */
	public void setRisoluzione(String risoluzione) {
		this.risoluzione = risoluzione;
	}

	/**
	 * @return the idDichiarazioneSegnalazione
	 */
	public String getIdDichiarazioneSegnalazione() {
		return idDichiarazioneSegnalazione;
	}

	/**
	 * @param idDichiarazioneSegnalazione the idDichiarazioneSegnalazione to set
	 */
	public void setIdDichiarazioneSegnalazione(String idDichiarazioneSegnalazione) {
		this.idDichiarazioneSegnalazione = idDichiarazioneSegnalazione;
	}

	/**
	 * @return the dataEsecuzione
	 */
	public String getDataEsecuzione() {
		return dataEsecuzione;
	}

	/**
	 * @param dataEsecuzione the dataEsecuzione to set
	 */
	public void setDataEsecuzione(String dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}

	/**
	 * @return the codiceControllo
	 */
	public String getCodiceControllo() {
		return codiceControllo;
	}

	/**
	 * @param codiceControllo the codiceControllo to set
	 */
	public void setCodiceControllo(String codiceControllo) {
		this.codiceControllo = codiceControllo;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the riferimentoDocumento
	 */
	public String getRiferimentoDocumento() {
		return riferimentoDocumento;
	}

	/**
	 * @param riferimentoDocumento the riferimentoDocumento to set
	 */
	public void setRiferimentoDocumento(String riferimentoDocumento) {
		this.riferimentoDocumento = riferimentoDocumento;
	}

	/**
	 * @return the ultimaModifica
	 */
	public String getUltimaModifica() {
		return ultimaModifica;
	}

	/**
	 * @param ultimaModifica the ultimaModifica to set
	 */
	public void setUltimaModifica(String ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}

	/**
	 * @return the flagDocumentoGiustificativo
	 */
	public String getFlagDocumentoGiustificativo() {
		return flagDocumentoGiustificativo;
	}

	/**
	 * @param flagDocumentoGiustificativo the flagDocumentoGiustificativo to set
	 */
	public void setFlagDocumentoGiustificativo(String flagDocumentoGiustificativo) {
		this.flagDocumentoGiustificativo = flagDocumentoGiustificativo;
	}

	/**
	 * @return the idControllo
	 */
	public String getIdControllo() {
		return idControllo;
	}

	/**
	 * @param idControllo the idControllo to set
	 */
	public void setIdControllo(String idControllo) {
		this.idControllo = idControllo;
	}

	/**
	 * @return the idAzienda
	 */
	public Long getIdAzienda() {
		return idAzienda;
	}

	/**
	 * @param idAzienda the idAzienda to set
	 */
	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	/**
	 * @return the idStoricoParticella
	 */
	public String getIdStoricoParticella() {
		return idStoricoParticella;
	}

	/**
	 * @param idStoricoParticella the idStoricoParticella to set
	 */
	public void setIdStoricoParticella(String idStoricoParticella) {
		this.idStoricoParticella = idStoricoParticella;
	}

	/**
	 * @return the extIdTipoDocumento
	 */
	public String getExtIdTipoDocumento() {
		return extIdTipoDocumento;
	}

	/**
	 * @param extIdTipoDocumento the extIdTipoDocumento to set
	 */
	public void setExtIdTipoDocumento(String extIdTipoDocumento) {
		this.extIdTipoDocumento = extIdTipoDocumento;
	}

	/**
	 * @return the errExtIdTipoDocumento
	 */
	public boolean isErrExtIdTipoDocumento() {
		return errExtIdTipoDocumento;
	}

	/**
	 * @param errExtIdTipoDocumento the errExtIdTipoDocumento to set
	 */
	public void setErrExtIdTipoDocumento(boolean errExtIdTipoDocumento) {
		this.errExtIdTipoDocumento = errExtIdTipoDocumento;
	}

	/**
	 * @return the errRiferimentoDocumento
	 */
	public boolean isErrRiferimentoDocumento() {
		return errRiferimentoDocumento;
	}

	/**
	 * @param errRiferimentoDocumento the errRiferimentoDocumento to set
	 */
	public void setErrRiferimentoDocumento(boolean errRiferimentoDocumento) {
		this.errRiferimentoDocumento = errRiferimentoDocumento;
	}

	/**
	 * @return the noError
	 */
	public boolean isNoError() {
		return noError;
	}

	/**
	 * @param noError the noError to set
	 */
	public void setNoError(boolean noError) {
		this.noError = noError;
	}

	/**
	 * @return the idDocumento
	 */
	public Long getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @param idDocumento the idDocumento to set
	 */
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	/**
	 * @return the errIdDocumento
	 */
	public boolean isErrIdDocumento() {
		return errIdDocumento;
	}

	/**
	 * @param errIdDocumento the errIdDocumento to set
	 */
	public void setErrIdDocumento(boolean errIdDocumento) {
		this.errIdDocumento = errIdDocumento;
	}

	/**
	 * @return the tipoAnomaliaBloccante
	 */
	public String getTipoAnomaliaBloccante() {
		return tipoAnomaliaBloccante;
	}

	/**
	 * @param tipoAnomaliaBloccante the tipoAnomaliaBloccante to set
	 */
	public void setTipoAnomaliaBloccante(String tipoAnomaliaBloccante) {
		this.tipoAnomaliaBloccante = tipoAnomaliaBloccante;
	}

	/**
	 * @return the tipoAnomaliaWarning
	 */
	public String getTipoAnomaliaWarning() {
		return tipoAnomaliaWarning;
	}

	/**
	 * @param tipoAnomaliaWarning the tipoAnomaliaWarning to set
	 */
	public void setTipoAnomaliaWarning(String tipoAnomaliaWarning) {
		this.tipoAnomaliaWarning = tipoAnomaliaWarning;
	}

	/**
	 * @return the tipoAnomaliaOk
	 */
	public String getTipoAnomaliaOk() {
		return tipoAnomaliaOk;
	}

	/**
	 * @param tipoAnomaliaOk the tipoAnomaliaOk to set
	 */
	public void setTipoAnomaliaOk(String tipoAnomaliaOk) {
		this.tipoAnomaliaOk = tipoAnomaliaOk;
	}

	/**
	 * Metodo hashCode();
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bloccante ? 1231 : 1237);
		result = prime * result + ((bloccanteStr == null) ? 0 : bloccanteStr.hashCode());
		result = prime * result + ((codiceControllo == null) ? 0 : codiceControllo.hashCode());
		result = prime * result + ((dataEsecuzione == null) ? 0 : dataEsecuzione.hashCode());
		result = prime * result + ((descAnomaliaErrore == null) ? 0 : descAnomaliaErrore.hashCode());
		result = prime * result + ((descGruppoControllo == null) ? 0 : descGruppoControllo.hashCode());
		result = prime * result + (errExtIdTipoDocumento ? 1231 : 1237);
		result = prime * result + (errIdDocumento ? 1231 : 1237);
		result = prime * result + (errRiferimentoDocumento ? 1231 : 1237);
		result = prime * result + ((extIdTipoDocumento == null) ? 0 : extIdTipoDocumento.hashCode());
		result = prime * result + ((flagDocumentoGiustificativo == null) ? 0 : flagDocumentoGiustificativo.hashCode());
		result = prime * result + ((idAzienda == null) ? 0 : idAzienda.hashCode());
		result = prime * result + ((idControllo == null) ? 0 : idControllo.hashCode());
		result = prime * result + ((idDichiarazioneCorrezione == null) ? 0 : idDichiarazioneCorrezione.hashCode());
		result = prime * result + ((idDichiarazioneSegnalazione == null) ? 0 : idDichiarazioneSegnalazione.hashCode());
		result = prime * result + ((idDocumento == null) ? 0 : idDocumento.hashCode());
		result = prime * result + ((idGruppoControllo == null) ? 0 : idGruppoControllo.hashCode());
		result = prime * result + ((idStoricoParticella == null) ? 0 : idStoricoParticella.hashCode());
		result = prime * result + (noError ? 1231 : 1237);
		result = prime * result + ((riferimentoDocumento == null) ? 0 : riferimentoDocumento.hashCode());
		result = prime * result + ((risoluzione == null) ? 0 : risoluzione.hashCode());
		result = prime * result + ((tipoAnomaliaBloccante == null) ? 0 : tipoAnomaliaBloccante.hashCode());
		result = prime * result + ((tipoAnomaliaErrore == null) ? 0 : tipoAnomaliaErrore.hashCode());
		result = prime * result + ((tipoAnomaliaOk == null) ? 0 : tipoAnomaliaOk.hashCode());
		result = prime * result + ((tipoAnomaliaWarning == null) ? 0 : tipoAnomaliaWarning.hashCode());
		result = prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
		result = prime * result + ((ultimaModifica == null) ? 0 : ultimaModifica.hashCode());
		return result;
	}

	/**
	 * Metodo equals()
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ErrAnomaliaDicConsistenzaVO other = (ErrAnomaliaDicConsistenzaVO) obj;
		if (bloccante != other.bloccante)
			return false;
		if (bloccanteStr == null) {
			if (other.bloccanteStr != null)
				return false;
		} else if (!bloccanteStr.equals(other.bloccanteStr))
			return false;
		if (codiceControllo == null) {
			if (other.codiceControllo != null)
				return false;
		} else if (!codiceControllo.equals(other.codiceControllo))
			return false;
		if (dataEsecuzione == null) {
			if (other.dataEsecuzione != null)
				return false;
		} else if (!dataEsecuzione.equals(other.dataEsecuzione))
			return false;
		if (descAnomaliaErrore == null) {
			if (other.descAnomaliaErrore != null)
				return false;
		} else if (!descAnomaliaErrore.equals(other.descAnomaliaErrore))
			return false;
		if (descGruppoControllo == null) {
			if (other.descGruppoControllo != null)
				return false;
		} else if (!descGruppoControllo.equals(other.descGruppoControllo))
			return false;
		if (errExtIdTipoDocumento != other.errExtIdTipoDocumento)
			return false;
		if (errIdDocumento != other.errIdDocumento)
			return false;
		if (errRiferimentoDocumento != other.errRiferimentoDocumento)
			return false;
		if (extIdTipoDocumento == null) {
			if (other.extIdTipoDocumento != null)
				return false;
		} else if (!extIdTipoDocumento.equals(other.extIdTipoDocumento))
			return false;
		if (flagDocumentoGiustificativo == null) {
			if (other.flagDocumentoGiustificativo != null)
				return false;
		} else if (!flagDocumentoGiustificativo
				.equals(other.flagDocumentoGiustificativo))
			return false;
		if (idAzienda == null) {
			if (other.idAzienda != null)
				return false;
		} else if (!idAzienda.equals(other.idAzienda))
			return false;
		if (idControllo == null) {
			if (other.idControllo != null)
				return false;
		} else if (!idControllo.equals(other.idControllo))
			return false;
		if (idDichiarazioneCorrezione == null) {
			if (other.idDichiarazioneCorrezione != null)
				return false;
		} else if (!idDichiarazioneCorrezione
				.equals(other.idDichiarazioneCorrezione))
			return false;
		if (idDichiarazioneSegnalazione == null) {
			if (other.idDichiarazioneSegnalazione != null)
				return false;
		} else if (!idDichiarazioneSegnalazione
				.equals(other.idDichiarazioneSegnalazione))
			return false;
		if (idDocumento == null) {
			if (other.idDocumento != null)
				return false;
		} else if (!idDocumento.equals(other.idDocumento))
			return false;
		if (idGruppoControllo == null) {
			if (other.idGruppoControllo != null)
				return false;
		} else if (!idGruppoControllo.equals(other.idGruppoControllo))
			return false;
		if (idStoricoParticella == null) {
			if (other.idStoricoParticella != null)
				return false;
		} else if (!idStoricoParticella.equals(other.idStoricoParticella))
			return false;
		if (noError != other.noError)
			return false;
		if (riferimentoDocumento == null) {
			if (other.riferimentoDocumento != null)
				return false;
		} else if (!riferimentoDocumento.equals(other.riferimentoDocumento))
			return false;
		if (risoluzione == null) {
			if (other.risoluzione != null)
				return false;
		} else if (!risoluzione.equals(other.risoluzione))
			return false;
		if (tipoAnomaliaBloccante == null) {
			if (other.tipoAnomaliaBloccante != null)
				return false;
		} else if (!tipoAnomaliaBloccante.equals(other.tipoAnomaliaBloccante))
			return false;
		if (tipoAnomaliaErrore == null) {
			if (other.tipoAnomaliaErrore != null)
				return false;
		} else if (!tipoAnomaliaErrore.equals(other.tipoAnomaliaErrore))
			return false;
		if (tipoAnomaliaOk == null) {
			if (other.tipoAnomaliaOk != null)
				return false;
		} else if (!tipoAnomaliaOk.equals(other.tipoAnomaliaOk))
			return false;
		if (tipoAnomaliaWarning == null) {
			if (other.tipoAnomaliaWarning != null)
				return false;
		} else if (!tipoAnomaliaWarning.equals(other.tipoAnomaliaWarning))
			return false;
		if (tipoDocumento == null) {
			if (other.tipoDocumento != null)
				return false;
		} else if (!tipoDocumento.equals(other.tipoDocumento))
			return false;
		if (ultimaModifica == null) {
			if (other.ultimaModifica != null)
				return false;
		} else if (!ultimaModifica.equals(other.ultimaModifica))
			return false;
		return true;
	}

	public boolean validateCorrezioneDichiarazione() {
		boolean result = false;
		// Se flagDocumentoGiustificativo non è vuoto devo controllare
		// i campi obbligatori
		if(Validator.isNotEmpty(this.getFlagDocumentoGiustificativo())) {
			if (!Validator.isNotEmpty(this.getIdDocumento())) {
				result = true;
				errIdDocumento=true;
			}
		}
		return result;
	}
	
	//Campi ultima modifica spezzata
  public Date getDataUltimaModifica()
  {
    return dataUltimaModifica;
  }

  public void setDataUltimaModifica(Date dataUltimaModifica)
  {
    this.dataUltimaModifica = dataUltimaModifica;
  }

  public String getUtenteUltimaModifica()
  {
    return utenteUltimaModifica;
  }

  public void setUtenteUltimaModifica(String utenteUltimaModifica)
  {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public String getEnteUltimaModifica()
  {
    return enteUltimaModifica;
  }

  public void setEnteUltimaModifica(String enteUltimaModifica)
  {
    this.enteUltimaModifica = enteUltimaModifica;
  }

  public StoricoParticellaVO getStoricoParticellaVO()
  {
    return storicoParticellaVO;
  }

  public void setStoricoParticellaVO(StoricoParticellaVO storicoParticellaVO)
  {
    this.storicoParticellaVO = storicoParticellaVO;
  }

  public ConduzioneParticellaVO getConduzioneParticellaVO()
  {
    return conduzioneParticellaVO;
  }

  public void setConduzioneParticellaVO(
      ConduzioneParticellaVO conduzioneParticellaVO)
  {
    this.conduzioneParticellaVO = conduzioneParticellaVO;
  }

  

}
