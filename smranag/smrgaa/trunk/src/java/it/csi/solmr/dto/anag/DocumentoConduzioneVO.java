package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

/**
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
public class DocumentoConduzioneVO implements Serializable {

	private static final long serialVersionUID = -1902869673535715439L;
	
	private Long idDocumentoConduzione = null;
	private Long idConduzioneParticella = null;
	private Long idDocumento = null;
	private Long idParticella = null;
	private Date dataInserimento = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;
	private Date minDataInizioValidita = null;
	private Date minDataFineValidita = null;
	private Date maxDataFineValidita = null;
	private String note = null;
	private String lavorazionePrioritaria = null;

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
	 * @return the dataInserimento
	 */
	public Date getDataInserimento() {
		return dataInserimento;
	}

	/**
	 * @param dataInserimento the dataInserimento to set
	 */
	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	/**
	 * @return the idConduzioneParticella
	 */
	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}

	/**
	 * @param idConduzioneParticella the idConduzioneParticella to set
	 */
	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
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
	 * @return the idDocumentoConduzione
	 */
	public Long getIdDocumentoConduzione() {
		return idDocumentoConduzione;
	}

	/**
	 * @param idDocumentoConduzione the idDocumentoConduzione to set
	 */
	public void setIdDocumentoConduzione(Long idDocumentoConduzione) {
		this.idDocumentoConduzione = idDocumentoConduzione;
	}

	/**
	 * @return the minDataInizioValidita
	 */
	public Date getMinDataInizioValidita() {
		return minDataInizioValidita;
	}

	/**
	 * @param minDataInizioValidita the minDataInizioValidita to set
	 */
	public void setMinDataInizioValidita(Date minDataInizioValidita) {
		this.minDataInizioValidita = minDataInizioValidita;
	}

	/**
	 * @return the minDataFineValidita
	 */
	public Date getMinDataFineValidita() {
		return minDataFineValidita;
	}

	/**
	 * @param minDataFineValidita the minDataFineValidita to set
	 */
	public void setMinDataFineValidita(Date minDataFineValidita) {
		this.minDataFineValidita = minDataFineValidita;
	}

	/**
	 * @return the maxDataFineValidita
	 */
	public Date getMaxDataFineValidita() {
		return maxDataFineValidita;
	}

	/**
	 * @param maxDataFineValidita the maxDataFineValidita to set
	 */
	public void setMaxDataFineValidita(Date maxDataFineValidita) {
		this.maxDataFineValidita = maxDataFineValidita;
	}

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getLavorazionePrioritaria()
  {
    return lavorazionePrioritaria;
  }

  public void setLavorazionePrioritaria(String lavorazionePrioritaria)
  {
    this.lavorazionePrioritaria = lavorazionePrioritaria;
  }

  public Long getIdParticella()
  {
    return idParticella;
  }

  public void setIdParticella(Long idParticella)
  {
    this.idParticella = idParticella;
  }
	
	
	
}
