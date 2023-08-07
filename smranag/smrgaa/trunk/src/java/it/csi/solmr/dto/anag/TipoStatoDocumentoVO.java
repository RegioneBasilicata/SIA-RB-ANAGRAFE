package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

import it.csi.solmr.util.*;

/**
 * Value Object che mappa la tabella DB_TIPO_STATO_DOCUMENTO
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
public class TipoStatoDocumentoVO extends AbstractValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6624051925994972954L;
	private Long idStatoDocumento = null;
	private String descrizione = null;
	private Date dataInizioValidita = null;
	private Date dataFineValidita = null;

	public TipoStatoDocumentoVO() {
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Long getIdStatoDocumento() {
		return idStatoDocumento;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setIdStatoDocumento(Long idStatoDocumento) {
		this.idStatoDocumento = idStatoDocumento;
	}

	public int hashCode() {
		return (idStatoDocumento == null ? 0 : idStatoDocumento.hashCode())+
		(descrizione == null ? 0 : descrizione.hashCode())+
		(dataInizioValidita == null ? 0 : dataInizioValidita.hashCode())+
		(dataFineValidita == null ? 0 : dataFineValidita.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof TipoStatoDocumentoVO) {
			TipoStatoDocumentoVO other = (TipoStatoDocumentoVO)o;
			return (this.idStatoDocumento == null && other.idStatoDocumento == null || this.idStatoDocumento.equals(other.idStatoDocumento) &&
					this.descrizione == null && other.descrizione == null || this.descrizione.equals(other.descrizione) &&
					this.dataInizioValidita == null && other.dataInizioValidita == null || this.dataInizioValidita.equals(other.dataInizioValidita) &&
					this.dataFineValidita == null && other.dataFineValidita == null || this.dataFineValidita.equals(other.dataFineValidita));
		} else
			return false;
	}

}
