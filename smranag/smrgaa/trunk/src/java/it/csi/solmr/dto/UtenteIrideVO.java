package it.csi.solmr.dto;

import java.io.Serializable;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

public class UtenteIrideVO implements Serializable
{
	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = -5111093781137981181L;

	private Long idUtente;
	private String denominazione;
	private String codiceFiscale;
	private String mail;
	private java.util.Date dataUltimoAccesso;
	private Long numeroAccessi;
	private Long idProfilo;
	private String provincia;
	private Long idIntermediario;
	private String cuaa;
	private String descrizioneEnteAppartenenza;
	private Long idProcedimento;
	private String codiceEnte;

	public UtenteIrideVO() {
	}
	public Long getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMail() {
		return mail;
	}
	public void setDataUltimoAccesso(java.util.Date dataUltimoAccesso) {
		this.dataUltimoAccesso = dataUltimoAccesso;
	}
	public java.util.Date getDataUltimoAccesso() {
		return dataUltimoAccesso;
	}
	public void setNumeroAccessi(Long numeroAccessi) {
		this.numeroAccessi = numeroAccessi;
	}
	public Long getNumeroAccessi() {
		return numeroAccessi;
	}
	public void setIdProfilo(Long idProfilo) {
		this.idProfilo = idProfilo;
	}
	public Long getIdProfilo() {
		return idProfilo;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setIdIntermediario(Long idIntermediario) {
		this.idIntermediario = idIntermediario;
	}
	public Long getIdIntermediario() {
		return idIntermediario;
	}
	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}
	public String getCuaa() {
		return cuaa;
	}
	public void setDescrizioneEnteAppartenenza(String descrizioneEnteAppartenenza) {
		this.descrizioneEnteAppartenenza = descrizioneEnteAppartenenza;
	}
	public String getDescrizioneEnteAppartenenza() {
		return descrizioneEnteAppartenenza;
	}
	public void setIdProcedimento(Long idProcedimento) {
		this.idProcedimento = idProcedimento;
	}
	public Long getIdProcedimento() {
		return idProcedimento;
	}
  public String getCodiceEnte()
  {
    return codiceEnte;
  }
  public void setCodiceEnte(String codiceEnte)
  {
    this.codiceEnte = codiceEnte;
  }
	
	
	
	/**
	 * Metodo hashCode()
	 */
	/*public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((codiceFiscale == null) ? 0 : codiceFiscale.hashCode());
		result = PRIME * result + ((cuaa == null) ? 0 : cuaa.hashCode());
		result = PRIME * result + ((dataUltimoAccesso == null) ? 0 : dataUltimoAccesso.hashCode());
		result = PRIME * result + ((denominazione == null) ? 0 : denominazione.hashCode());
		result = PRIME * result + ((descrizioneEnteAppartenenza == null) ? 0 : descrizioneEnteAppartenenza.hashCode());
		result = PRIME * result + ((idIntermediario == null) ? 0 : idIntermediario.hashCode());
		result = PRIME * result + ((idProcedimento == null) ? 0 : idProcedimento.hashCode());
		result = PRIME * result + ((idProfilo == null) ? 0 : idProfilo.hashCode());
		result = PRIME * result + ((idUtente == null) ? 0 : idUtente.hashCode());
		result = PRIME * result + ((mail == null) ? 0 : mail.hashCode());
		result = PRIME * result + ((numeroAccessi == null) ? 0 : numeroAccessi.hashCode());
		result = PRIME * result + ((provincia == null) ? 0 : provincia.hashCode());
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
		final UtenteIrideVO other = (UtenteIrideVO) obj;
		if (codiceFiscale == null) {
			if (other.codiceFiscale != null)
				return false;
		} else if (!codiceFiscale.equals(other.codiceFiscale))
			return false;
		if (cuaa == null) {
			if (other.cuaa != null)
				return false;
		} else if (!cuaa.equals(other.cuaa))
			return false;
		if (dataUltimoAccesso == null) {
			if (other.dataUltimoAccesso != null)
				return false;
		} else if (!dataUltimoAccesso.equals(other.dataUltimoAccesso))
			return false;
		if (denominazione == null) {
			if (other.denominazione != null)
				return false;
		} else if (!denominazione.equals(other.denominazione))
			return false;
		if (descrizioneEnteAppartenenza == null) {
			if (other.descrizioneEnteAppartenenza != null)
				return false;
		} else if (!descrizioneEnteAppartenenza.equals(other.descrizioneEnteAppartenenza))
			return false;
		if (idIntermediario == null) {
			if (other.idIntermediario != null)
				return false;
		} else if (!idIntermediario.equals(other.idIntermediario))
			return false;
		if (idProcedimento == null) {
			if (other.idProcedimento != null)
				return false;
		} else if (!idProcedimento.equals(other.idProcedimento))
			return false;
		if (idProfilo == null) {
			if (other.idProfilo != null)
				return false;
		} else if (!idProfilo.equals(other.idProfilo))
			return false;
		if (idUtente == null) {
			if (other.idUtente != null)
				return false;
		} else if (!idUtente.equals(other.idUtente))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (numeroAccessi == null) {
			if (other.numeroAccessi != null)
				return false;
		} else if (!numeroAccessi.equals(other.numeroAccessi))
			return false;
		if (provincia == null) {
			if (other.provincia != null)
				return false;
		} else if (!provincia.equals(other.provincia))
			return false;
		return true;
	}*/
}