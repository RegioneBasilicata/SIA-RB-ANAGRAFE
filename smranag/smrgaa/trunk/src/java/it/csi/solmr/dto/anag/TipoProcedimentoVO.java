package it.csi.solmr.dto.anag;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Mauro Vocale
 * @version 1.0
 */

import java.io.*;

public class TipoProcedimentoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5066827360436568181L;
	private Long idProcedimento = null;              // DB_TIPO_PROCEDIMENTO.ID_PROCEDIMENTO
	private String descrizione = null;               // DB_TIPO_PROCEDIMENTO.DESCRIZIONE
	private String descrizioneEstesa = null;         // DB_TIPO_PROCEDIMENTO.DESCRIZIONE_ESTESA
	private String flagControlliAbilitazioni = null; // DB_TIPO_PROCEDIMENTO.FLAG_CONTROLLI_ABILITAZIONI

	public TipoProcedimentoVO() {
	}

	public int hashCode() {
		return  (idProcedimento == null ? 0 : idProcedimento.hashCode())+
		(descrizione == null ? 0 : descrizione.hashCode())+
		(descrizioneEstesa == null ? 0 : descrizioneEstesa.hashCode())+
		(flagControlliAbilitazioni == null ? 0 : flagControlliAbilitazioni.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof TipoProcedimentoVO) {
			TipoProcedimentoVO other = (TipoProcedimentoVO)o;
			return (this.idProcedimento == null && other.idProcedimento == null || this.idProcedimento.equals(other.idProcedimento) &&
					this.descrizione == null && other.descrizione == null || this.descrizione.equals(other.descrizione) &&
					this.descrizioneEstesa == null && other.descrizioneEstesa == null || this.descrizioneEstesa.equals(other.descrizioneEstesa) &&
					this.flagControlliAbilitazioni == null && other.flagControlliAbilitazioni == null || this.flagControlliAbilitazioni.equals(other.flagControlliAbilitazioni));
		} else
			return false;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getDescrizioneEstesa() {
		return descrizioneEstesa;
	}

	public Long getIdProcedimento() {
		return idProcedimento;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setDescrizioneEstesa(String descrizioneEstesa) {
		this.descrizioneEstesa = descrizioneEstesa;
	}

	public void setFlagControlliAbilitazioni(String flagControlliAbilitazioni) {
		this.flagControlliAbilitazioni = flagControlliAbilitazioni;
	}

	public void setIdProcedimento(Long idProcedimento) {
		this.idProcedimento = idProcedimento;
	}

}
