package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

import it.csi.solmr.dto.*;

/**
 * Value Object che mappa la tabella DB_TIPO_DOCUMENTO
 *
 * <p>Title: SMRGAA</p>
 *
 * <p>Description: Anagrafe delle Imprese Agricole e Agro-Alimentari</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: CSI - PIEMONTE</p>
 *
 * @author Mauro Vocale
 * @version 1.0
 */
public class TipoDocumentoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1089644861758474154L;
	private Long idDocumento = null;
	private Long idTipologiaDocumento = null;
	private String descrizione = null;
	private String codiceDocumento = null;
	private String flagAnagTerr = null;
	private String flagObbligoFascicolo = null;
	private String flagObbligoDataFine = null;
	private String flagObbligoProtocollo = null;
	private String flagObbligoParticella = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private CodeDescription tipoTipologiaDocumento = null;
	private String flagObbligoProprietario = null;
	private String flagObbligoEnteNumero = null;
	private String flagUnivocita = null;
	private Long extIdTipoDocumentoIndex;
	private String flagAnnullabile;
	
	/**
	 * Costruttore di default
	 *
	 */
	public TipoDocumentoVO() {
		super();
	}
	
	/**
	 * Costruttore con i campi
	 * 
	 * @param idDocumento
	 * @param idTipologiaDocumento
	 * @param descrizione
	 * @param codiceDocumento
	 * @param flagAnagTerr
	 * @param flagObbligoFascicolo
	 * @param flagObbligoDataFine
	 * @param flagObbligoProtocollo
	 * @param flagObbligoParticella
	 * @param dataInizioValidita
	 * @param dataFineValidita
	 * @param tipoTipologiaDocumento
	 * @param flagObbligoProprietario
	 * @param flagObbligoEnteNumero
	 * @param flagUnivocita
	 */
	/*public TipoDocumentoVO(Long idDocumento, Long idTipologiaDocumento, String descrizione, String codiceDocumento, String flagAnagTerr, String flagObbligoFascicolo, String flagObbligoDataFine, String flagObbligoProtocollo, String flagObbligoParticella, Date dataInizioValidita, Date dataFineValidita, CodeDescription tipoTipologiaDocumento, String flagObbligoProprietario, String flagObbligoEnteNumero, String flagUnivocita) {
		super();
		this.idDocumento = idDocumento;
		this.idTipologiaDocumento = idTipologiaDocumento;
		this.descrizione = descrizione;
		this.codiceDocumento = codiceDocumento;
		this.flagAnagTerr = flagAnagTerr;
		this.flagObbligoFascicolo = flagObbligoFascicolo;
		this.flagObbligoDataFine = flagObbligoDataFine;
		this.flagObbligoProtocollo = flagObbligoProtocollo;
		this.flagObbligoParticella = flagObbligoParticella;
		this.dataInizioValidita = dataInizioValidita;
		this.dataFineValidita = dataFineValidita;
		this.tipoTipologiaDocumento = tipoTipologiaDocumento;
		this.flagObbligoProprietario = flagObbligoProprietario;
		this.flagObbligoEnteNumero = flagObbligoEnteNumero;
		this.flagUnivocita = flagUnivocita;
	}*/

	/**
	 * @return the codiceDocumento
	 */
	public String getCodiceDocumento() {
		return codiceDocumento;
	}

	/**
	 * @param codiceDocumento the codiceDocumento to set
	 */
	public void setCodiceDocumento(String codiceDocumento) {
		this.codiceDocumento = codiceDocumento;
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
	 * @return the flagAnagTerr
	 */
	public String getFlagAnagTerr() {
		return flagAnagTerr;
	}

	/**
	 * @param flagAnagTerr the flagAnagTerr to set
	 */
	public void setFlagAnagTerr(String flagAnagTerr) {
		this.flagAnagTerr = flagAnagTerr;
	}

	/**
	 * @return the flagObbligoDataFine
	 */
	public String getFlagObbligoDataFine() {
		return flagObbligoDataFine;
	}

	/**
	 * @param flagObbligoDataFine the flagObbligoDataFine to set
	 */
	public void setFlagObbligoDataFine(String flagObbligoDataFine) {
		this.flagObbligoDataFine = flagObbligoDataFine;
	}

	/**
	 * @return the flagObbligoEnteNumero
	 */
	public String getFlagObbligoEnteNumero() {
		return flagObbligoEnteNumero;
	}

	/**
	 * @param flagObbligoEnteNumero the flagObbligoEnteNumero to set
	 */
	public void setFlagObbligoEnteNumero(String flagObbligoEnteNumero) {
		this.flagObbligoEnteNumero = flagObbligoEnteNumero;
	}

	/**
	 * @return the flagObbligoFascicolo
	 */
	public String getFlagObbligoFascicolo() {
		return flagObbligoFascicolo;
	}

	/**
	 * @param flagObbligoFascicolo the flagObbligoFascicolo to set
	 */
	public void setFlagObbligoFascicolo(String flagObbligoFascicolo) {
		this.flagObbligoFascicolo = flagObbligoFascicolo;
	}

	/**
	 * @return the flagObbligoParticella
	 */
	public String getFlagObbligoParticella() {
		return flagObbligoParticella;
	}

	/**
	 * @param flagObbligoParticella the flagObbligoParticella to set
	 */
	public void setFlagObbligoParticella(String flagObbligoParticella) {
		this.flagObbligoParticella = flagObbligoParticella;
	}

	/**
	 * @return the flagObbligoProprietario
	 */
	public String getFlagObbligoProprietario() {
		return flagObbligoProprietario;
	}

	/**
	 * @param flagObbligoProprietario the flagObbligoProprietario to set
	 */
	public void setFlagObbligoProprietario(String flagObbligoProprietario) {
		this.flagObbligoProprietario = flagObbligoProprietario;
	}

	/**
	 * @return the flagObbligoProtocollo
	 */
	public String getFlagObbligoProtocollo() {
		return flagObbligoProtocollo;
	}

	/**
	 * @param flagObbligoProtocollo the flagObbligoProtocollo to set
	 */
	public void setFlagObbligoProtocollo(String flagObbligoProtocollo) {
		this.flagObbligoProtocollo = flagObbligoProtocollo;
	}

	/**
	 * @return the flagUnivocita
	 */
	public String getFlagUnivocita() {
		return flagUnivocita;
	}

	/**
	 * @param flagUnivocita the flagUnivocita to set
	 */
	public void setFlagUnivocita(String flagUnivocita) {
		this.flagUnivocita = flagUnivocita;
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
	 * @return the idTipologiaDocumento
	 */
	public Long getIdTipologiaDocumento() {
		return idTipologiaDocumento;
	}

	/**
	 * @param idTipologiaDocumento the idTipologiaDocumento to set
	 */
	public void setIdTipologiaDocumento(Long idTipologiaDocumento) {
		this.idTipologiaDocumento = idTipologiaDocumento;
	}

	/**
	 * @return the tipoTipologiaDocumento
	 */
	public CodeDescription getTipoTipologiaDocumento() {
		return tipoTipologiaDocumento;
	}

	/**
	 * @param tipoTipologiaDocumento the tipoTipologiaDocumento to set
	 */
	public void setTipoTipologiaDocumento(CodeDescription tipoTipologiaDocumento) {
		this.tipoTipologiaDocumento = tipoTipologiaDocumento;
	}

  public Long getExtIdTipoDocumentoIndex()
  {
    return extIdTipoDocumentoIndex;
  }

  public void setExtIdTipoDocumentoIndex(Long extIdTipoDocumentoIndex)
  {
    this.extIdTipoDocumentoIndex = extIdTipoDocumentoIndex;
  }

  public String getFlagAnnullabile()
  {
    return flagAnnullabile;
  }

  public void setFlagAnnullabile(String flagAnnullabile)
  {
    this.flagAnnullabile = flagAnnullabile;
  }
	
	
	

	
}
