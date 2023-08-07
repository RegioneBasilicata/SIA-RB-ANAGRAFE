package it.csi.solmr.dto.anag;

import java.io.*;

import it.csi.solmr.util.*;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Mauro Vocale
 * @version 1.0
 */

public class ConduzioneContrattoVO  extends AbstractValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1887954650146401612L;
	private Long idConduzioneContratto = null;
	private Long idContratto = null;
	private Long idConduzioneParticella = null;

	public ConduzioneContrattoVO() {
	}

	public int hashCode() {
		return (idConduzioneContratto == null ? 0 : idConduzioneContratto.hashCode())+
		(idContratto == null ? 0 : idContratto.hashCode())+
		(idConduzioneParticella == null ? 0 : idConduzioneParticella.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof ConduzioneContrattoVO) {
			ConduzioneContrattoVO other = (ConduzioneContrattoVO) o;
			return (this.idConduzioneContratto == null && other.idConduzioneContratto == null || this.idConduzioneContratto.equals(other.idConduzioneContratto) &&
					this.idContratto == null && other.idContratto == null || this.idContratto.equals(other.idContratto) &&
					this.idConduzioneParticella == null && other.idConduzioneParticella == null || this.idConduzioneParticella.equals(other.idConduzioneParticella));

		}
		else {
			return false;
		}
	}

	public Long getIdConduzioneContratto() {
		return idConduzioneContratto;
	}

	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}

	public Long getIdContratto() {
		return idContratto;
	}

	public void setIdConduzioneContratto(Long idConduzioneContratto) {
		this.idConduzioneContratto = idConduzioneContratto;
	}

	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
	}

	public void setIdContratto(Long idContratto) {
		this.idContratto = idContratto;
	}

}
