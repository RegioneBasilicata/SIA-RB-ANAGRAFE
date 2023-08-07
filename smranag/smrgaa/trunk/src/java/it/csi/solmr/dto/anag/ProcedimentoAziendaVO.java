package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Mauro Vocale
 * @version 1.0
 */

public class ProcedimentoAziendaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1956055874265054077L;
	private Long idProcedimentoAzienda = null;            // DB_PROCEDIMENTO_AZIENDA.ID_PROCEDIMENTO_AZIENDA
	private Long idAzienda = null;                        // DB_PROCEDIMENTO_AZIENDA.ID_AZIENDA
	private TipoProcedimentoVO tipoProcedimentoVO = null; // VO che mappa i dati della tabella DB_TIPO_PROCEDIMENTO a partire da DB_PROCEDIMENTO_AZIENDA.ID_PROCEDIMENTO
	private String numeroPratica = null;                  // DB_PROCEDIMENTO_AZIENDA.NUMERO_PRATICA
	private String descrizione = null;                    // DB_PROCEDIMENTO_AZIENDA.DESCRIZIONE
	private String stato = null;                          // DB_PROCEDIMENTO_AZIENDA.STATO
	private String descrizioneStato = null;               // DB_PROCEDIMENTO_AZIENDA.DESCRIZIONE_STATO
	private String note = null;                           // DB_PROCEDIMENTO_AZIENDA.NOTE
	private Date dataValiditaStato = null;                // DB_PROCEDIMENTO_AZIENDA.DATA_VALIDITA_STATO
	private Long idDichiarazioneConsistenza = null;       //DB_PROCEDIMENTO_AZIENDA.ID_DICHIARAZIONE_CONSISTENZA
	private String flagCessazioneAzAmmessa = null;        //DB_PROCEDIMENTO_AZIENDA.FLAG_CESSAZIONE_AZ_AMMESSA
	private Long exIdAmmCompetenza = null;                //DB_PROCEDIMENTO_AZIENDA.EX_ID_AMM_COMPETENZA
	private Long annoCampagna = null;                     // DB_PROCEDIMENTO_AZIENDA.ANNO_CAMPAGNA

	public ProcedimentoAziendaVO() {
	}

	public int hashCode() {
		return  (idProcedimentoAzienda == null ? 0 : idProcedimentoAzienda.hashCode())+
		(idAzienda == null ? 0 : idAzienda.hashCode())+
		(tipoProcedimentoVO == null ? 0 : tipoProcedimentoVO.hashCode())+
		(numeroPratica == null ? 0 : numeroPratica.hashCode())+
		(descrizione == null ? 0 : descrizione.hashCode())+
		(stato == null ? 0 : stato.hashCode())+
		(descrizioneStato == null ? 0 : descrizioneStato.hashCode())+
		(note == null ? 0 : note.hashCode())+
		(dataValiditaStato == null ? 0 : dataValiditaStato.hashCode())+
		(idDichiarazioneConsistenza == null ? 0 : idDichiarazioneConsistenza.hashCode())+
		(flagCessazioneAzAmmessa == null ? 0 : flagCessazioneAzAmmessa.hashCode())+
		(exIdAmmCompetenza == null ? 0 : exIdAmmCompetenza.hashCode())+
		(annoCampagna == null ? 0 : annoCampagna.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof ProcedimentoAziendaVO) {
			ProcedimentoAziendaVO other = (ProcedimentoAziendaVO)o;
			return (this.idProcedimentoAzienda == null && other.idProcedimentoAzienda == null || this.idProcedimentoAzienda.equals(other.idProcedimentoAzienda) &&
					this.idAzienda == null && other.idAzienda == null || this.idAzienda.equals(other.idAzienda) &&
					this.tipoProcedimentoVO == null && other.tipoProcedimentoVO == null || this.tipoProcedimentoVO.equals(other.tipoProcedimentoVO) &&
					this.numeroPratica == null && other.numeroPratica == null || this.numeroPratica.equals(other.numeroPratica) &&
					this.descrizione == null && other.descrizione == null || this.descrizione.equals(other.descrizione) &&
					this.stato == null && other.stato == null || this.stato.equals(other.stato) &&
					this.descrizioneStato == null && other.descrizioneStato == null || this.descrizioneStato.equals(other.descrizioneStato) &&
					this.note == null && other.note == null || this.note.equals(other.note) &&
					this.dataValiditaStato == null && other.dataValiditaStato == null || this.dataValiditaStato.equals(other.dataValiditaStato) &&
					this.idDichiarazioneConsistenza == null && other.idDichiarazioneConsistenza == null || this.idDichiarazioneConsistenza.equals(other.idDichiarazioneConsistenza) &&
					this.flagCessazioneAzAmmessa == null && other.flagCessazioneAzAmmessa == null || this.flagCessazioneAzAmmessa.equals(other.flagCessazioneAzAmmessa) &&
					this.exIdAmmCompetenza == null && other.exIdAmmCompetenza == null || this.exIdAmmCompetenza.equals(other.exIdAmmCompetenza) &&
					this.annoCampagna == null && other.annoCampagna == null || this.annoCampagna.equals(other.annoCampagna));
		} else
			return false;
	}

	public Date getDataValiditaStato() {
		return dataValiditaStato;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getDescrizioneStato() {
		return descrizioneStato;
	}

	public String getFlagCessazioneAzAmmessa() {
		return flagCessazioneAzAmmessa;
	}

	public Long getIdAzienda() {
		return idAzienda;
	}

	public Long getIdDichiarazioneConsistenza() {
		return idDichiarazioneConsistenza;
	}

	public Long getIdProcedimentoAzienda() {
		return idProcedimentoAzienda;
	}

	public String getNote() {
		return note;
	}

	public String getNumeroPratica() {
		return numeroPratica;
	}

	public String getStato() {
		return stato;
	}
	
	public Long getExIdAmmCompetenza()
  {
    return exIdAmmCompetenza;
  }

	public TipoProcedimentoVO getTipoProcedimentoVO() {
		return tipoProcedimentoVO;
	}

	public void setDataValiditaStato(Date dataValiditaStato) {
		this.dataValiditaStato = dataValiditaStato;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}

	public void setFlagCessazioneAzAmmessa(String flagCessazioneAzAmmessa) {
		this.flagCessazioneAzAmmessa = flagCessazioneAzAmmessa;
	}

	public void setIdAzienda(Long idAzienda) {
		this.idAzienda = idAzienda;
	}

	public void setIdDichiarazioneConsistenza(Long idDichiarazioneConsistenza) {
		this.idDichiarazioneConsistenza = idDichiarazioneConsistenza;
	}

	public void setIdProcedimentoAzienda(Long idProcedimentoAzienda) {
		this.idProcedimentoAzienda = idProcedimentoAzienda;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setNumeroPratica(String numeroPratica) {
		this.numeroPratica = numeroPratica;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	public void setExIdAmmCompetenza(Long exIdAmmCompetenza)
  {
    this.exIdAmmCompetenza = exIdAmmCompetenza;
  }
  

	public void setTipoProcedimentoVO(TipoProcedimentoVO tipoProcedimentoVO) {
		this.tipoProcedimentoVO = tipoProcedimentoVO;
	}

  public Long getAnnoCampagna()
  {
    return annoCampagna;
  }

  public void setAnnoCampagna(Long annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }
	

}
