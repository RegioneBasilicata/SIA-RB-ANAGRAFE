package it.csi.solmr.dto.anag.terreni;

import it.csi.solmr.dto.CodeDescription;
import it.csi.solmr.dto.UtenteIrideVO;
import it.csi.solmr.dto.anag.UteVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Vector;

/**
 * Classe che si occupa di mappare la tabella DB_CONDUZIONE_DICHIARATA
 * @author Mauro Vocale
 *
 */
public class ConduzioneDichiarataVO implements Serializable {

	private static final long serialVersionUID = -1434354016473208704L;
	
	private Long idConduzioneDichiarata = null;
	private String codiceFotografiaTerreni = null;
	private Long idUte = null;
	private UteVO uteVO = null;
	private Long idTitoloPossesso = null;
	private CodeDescription titoloPossesso = null;
	private String superficieCondotta = null;
	private String note = null;
	private java.util.Date dataInizioConduzione = null;
	private java.util.Date dataFineConduzione = null;
	private java.util.Date dataAggiornamento = null;
	private Long idUtenteAggiornamento = null;
	private UtenteIrideVO utenteAggiornamento = null;
	private Long idParticella = null;
	private Long idConduzioneParticella = null;
	private Long idStoricoParticella = null;
	private String esitoControllo = null;
	private java.util.Date dataEsecuzione = null;
	private UtilizzoDichiaratoVO[] elencoUtilizzi = null;
	private Vector<UtilizzoDichiaratoVO> vUtilizzi = null;
	private String superficieAgronomica = null;
	private BigDecimal percentualePossesso = null;
	
	/**
	 * Costruttore
	 */
	public ConduzioneDichiarataVO() {
	}

	
	
	public BigDecimal getPercentualePossesso()
  {
    return percentualePossesso;
  }

  public void setPercentualePossesso(BigDecimal percentualePossesso)
  {
    this.percentualePossesso = percentualePossesso;
  }
	
	
	/**
	 * @return the codiceFotografiaTerreni
	 */
	public String getCodiceFotografiaTerreni() {
		return codiceFotografiaTerreni;
	}

  /**
	 * @param codiceFotografiaTerreni the codiceFotografiaTerreni to set
	 */
	public void setCodiceFotografiaTerreni(String codiceFotografiaTerreni) {
		this.codiceFotografiaTerreni = codiceFotografiaTerreni;
	}
	/**
	 * @return the dataAggiornamento
	 */
	public java.util.Date getDataAggiornamento() {
		return dataAggiornamento;
	}
	/**
	 * @param dataAggiornamento the dataAggiornamento to set
	 */
	public void setDataAggiornamento(java.util.Date dataAggiornamento) {
		this.dataAggiornamento = dataAggiornamento;
	}
	/**
	 * @return the dataEsecuzione
	 */
	public java.util.Date getDataEsecuzione() {
		return dataEsecuzione;
	}
	/**
	 * @param dataEsecuzione the dataEsecuzione to set
	 */
	public void setDataEsecuzione(java.util.Date dataEsecuzione) {
		this.dataEsecuzione = dataEsecuzione;
	}
	/**
	 * @return the dataFineConduzione
	 */
	public java.util.Date getDataFineConduzione() {
		return dataFineConduzione;
	}
	/**
	 * @param dataFineConduzione the dataFineConduzione to set
	 */
	public void setDataFineConduzione(java.util.Date dataFineConduzione) {
		this.dataFineConduzione = dataFineConduzione;
	}
	/**
	 * @return the dataInizioConduzione
	 */
	public java.util.Date getDataInizioConduzione() {
		return dataInizioConduzione;
	}
	/**
	 * @param dataInizioConduzione the dataInizioConduzione to set
	 */
	public void setDataInizioConduzione(java.util.Date dataInizioConduzione) {
		this.dataInizioConduzione = dataInizioConduzione;
	}
	/**
	 * @return the esitoControllo
	 */
	public String getEsitoControllo() {
		return esitoControllo;
	}
	/**
	 * @param esitoControllo the esitoControllo to set
	 */
	public void setEsitoControllo(String esitoControllo) {
		this.esitoControllo = esitoControllo;
	}
	/**
	 * @return the idConduzioneDichiarata
	 */
	public Long getIdConduzioneDichiarata() {
		return idConduzioneDichiarata;
	}
	/**
	 * @param idConduzioneDichiarata the idConduzioneDichiarata to set
	 */
	public void setIdConduzioneDichiarata(Long idConduzioneDichiarata) {
		this.idConduzioneDichiarata = idConduzioneDichiarata;
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
	 * @return the idParticella
	 */
	public Long getIdParticella() {
		return idParticella;
	}
	/**
	 * @param idParticella the idParticella to set
	 */
	public void setIdParticella(Long idParticella) {
		this.idParticella = idParticella;
	}
	/**
	 * @return the idStoricoParticella
	 */
	public Long getIdStoricoParticella() {
		return idStoricoParticella;
	}
	/**
	 * @param idStoricoParticella the idStoricoParticella to set
	 */
	public void setIdStoricoParticella(Long idStoricoParticella) {
		this.idStoricoParticella = idStoricoParticella;
	}
	/**
	 * @return the idTitoloPossesso
	 */
	public Long getIdTitoloPossesso() {
		return idTitoloPossesso;
	}
	/**
	 * @param idTitoloPossesso the idTitoloPossesso to set
	 */
	public void setIdTitoloPossesso(Long idTitoloPossesso) {
		this.idTitoloPossesso = idTitoloPossesso;
	}
	/**
	 * @return the idUte
	 */
	public Long getIdUte() {
		return idUte;
	}
	/**
	 * @param idUte the idUte to set
	 */
	public void setIdUte(Long idUte) {
		this.idUte = idUte;
	}
	/**
	 * @return the idUtenteAggiornamento
	 */
	public Long getIdUtenteAggiornamento() {
		return idUtenteAggiornamento;
	}
	/**
	 * @param idUtenteAggiornamento the idUtenteAggiornamento to set
	 */
	public void setIdUtenteAggiornamento(Long idUtenteAggiornamento) {
		this.idUtenteAggiornamento = idUtenteAggiornamento;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the superficieCondotta
	 */
	public String getSuperficieCondotta() {
		return superficieCondotta;
	}
	/**
	 * @param superficieCondotta the superficieCondotta to set
	 */
	public void setSuperficieCondotta(String superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}
	/**
	 * @return the titoloPossesso
	 */
	public CodeDescription getTitoloPossesso() {
		return titoloPossesso;
	}
	/**
	 * @param titoloPossesso the titoloPossesso to set
	 */
	public void setTitoloPossesso(CodeDescription titoloPossesso) {
		this.titoloPossesso = titoloPossesso;
	}
	/**
	 * @return the utenteAggiornamento
	 */
	public UtenteIrideVO getUtenteAggiornamento() {
		return utenteAggiornamento;
	}
	/**
	 * @param utenteAggiornamento the utenteAggiornamento to set
	 */
	public void setUtenteAggiornamento(UtenteIrideVO utenteAggiornamento) {
		this.utenteAggiornamento = utenteAggiornamento;
	}
	/**
	 * @return the uteVO
	 */
	public UteVO getUteVO() {
		return uteVO;
	}
	/**
	 * @param uteVO the uteVO to set
	 */
	public void setUteVO(UteVO uteVO) {
		this.uteVO = uteVO;
	}

	/**
	 * @return the elencoUtilizzi
	 */
	public UtilizzoDichiaratoVO[] getElencoUtilizzi() {
		return elencoUtilizzi;
	}

	/**
	 * @param elencoUtilizzi the elencoUtilizzi to set
	 */
	public void setElencoUtilizzi(UtilizzoDichiaratoVO[] elencoUtilizzi) {
		this.elencoUtilizzi = elencoUtilizzi;
	}
	
	/**
	 * @return the superficieAgronomica
	 */
	public String getSuperficieAgronomica() {
		return superficieAgronomica;
	}

	/**
	 * @param superficieAgronomica the superficieAgronomica to set
	 */
	public void setSuperficieAgronomica(String superficieAgronomica) {
		this.superficieAgronomica = superficieAgronomica;
	}

	

	public Vector<UtilizzoDichiaratoVO> getvUtilizzi()
  {
    return vUtilizzi;
  }



  public void setvUtilizzi(Vector<UtilizzoDichiaratoVO> vUtilizzi)
  {
    this.vUtilizzi = vUtilizzi;
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
		final ConduzioneDichiarataVO other = (ConduzioneDichiarataVO) obj;
		if (codiceFotografiaTerreni == null) {
			if (other.codiceFotografiaTerreni != null)
				return false;
		} else if (!codiceFotografiaTerreni.equals(other.codiceFotografiaTerreni))
			return false;
		if (dataAggiornamento == null) {
			if (other.dataAggiornamento != null)
				return false;
		} else if (!dataAggiornamento.equals(other.dataAggiornamento))
			return false;
		if (dataEsecuzione == null) {
			if (other.dataEsecuzione != null)
				return false;
		} else if (!dataEsecuzione.equals(other.dataEsecuzione))
			return false;
		if (dataFineConduzione == null) {
			if (other.dataFineConduzione != null)
				return false;
		} else if (!dataFineConduzione.equals(other.dataFineConduzione))
			return false;
		if (dataInizioConduzione == null) {
			if (other.dataInizioConduzione != null)
				return false;
		} else if (!dataInizioConduzione.equals(other.dataInizioConduzione))
			return false;
		if (!Arrays.equals(elencoUtilizzi, other.elencoUtilizzi))
			return false;
		if (esitoControllo == null) {
			if (other.esitoControllo != null)
				return false;
		} else if (!esitoControllo.equals(other.esitoControllo))
			return false;
		if (idConduzioneDichiarata == null) {
			if (other.idConduzioneDichiarata != null)
				return false;
		} else if (!idConduzioneDichiarata.equals(other.idConduzioneDichiarata))
			return false;
		if (idConduzioneParticella == null) {
			if (other.idConduzioneParticella != null)
				return false;
		} else if (!idConduzioneParticella.equals(other.idConduzioneParticella))
			return false;
		if (idParticella == null) {
			if (other.idParticella != null)
				return false;
		} else if (!idParticella.equals(other.idParticella))
			return false;
		if (idStoricoParticella == null) {
			if (other.idStoricoParticella != null)
				return false;
		} else if (!idStoricoParticella.equals(other.idStoricoParticella))
			return false;
		if (idTitoloPossesso == null) {
			if (other.idTitoloPossesso != null)
				return false;
		} else if (!idTitoloPossesso.equals(other.idTitoloPossesso))
			return false;
		if (percentualePossesso == null) {
      if (other.percentualePossesso != null)
        return false;
    } else if (!percentualePossesso.equals(other.percentualePossesso))
      return false;
		if (idUte == null) {
			if (other.idUte != null)
				return false;
		} else if (!idUte.equals(other.idUte))
			return false;
		if (idUtenteAggiornamento == null) {
			if (other.idUtenteAggiornamento != null)
				return false;
		} else if (!idUtenteAggiornamento.equals(other.idUtenteAggiornamento))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (superficieCondotta == null) {
			if (other.superficieCondotta != null)
				return false;
		} else if (!superficieCondotta.equals(other.superficieCondotta))
			return false;
		if (titoloPossesso == null) {
			if (other.titoloPossesso != null)
				return false;
		} else if (!titoloPossesso.equals(other.titoloPossesso))
			return false;
		if (uteVO == null) {
			if (other.uteVO != null)
				return false;
		} else if (!uteVO.equals(other.uteVO))
			return false;
		if (utenteAggiornamento == null) {
			if (other.utenteAggiornamento != null)
				return false;
		} else if (!utenteAggiornamento.equals(other.utenteAggiornamento))
			return false;
		if (superficieAgronomica == null) {
			if (other.superficieAgronomica != null)
				return false;
		} else if (!superficieAgronomica.equals(other.superficieAgronomica))
			return false;
		return true;
	}
	
			
}