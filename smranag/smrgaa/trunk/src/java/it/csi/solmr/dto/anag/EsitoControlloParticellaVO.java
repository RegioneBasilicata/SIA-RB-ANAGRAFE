package it.csi.solmr.dto.anag;

import java.io.*;

public class EsitoControlloParticellaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6231051035318207117L;
	//	Attributi del DB
	public Long idEsitoControlloParticella = null; // DB_ESITO_CONTROLLO_PARTICELLA.ID_ESITO_CONTROLLO_PARTICELLA
	public Long idConduzioneParticella = null; // DB_ESITO_CONTROLLO_PARTICELLA.ID_CONDUZIONE_PARTICELLA
	public Long idControllo = null; // DB_ESITO_CONTROLLO_PARTICELLA.ID_CONTROLLO
	public String bloccante = null; // DB_ESITO_CONTROLLO_PARTICELLA.BLOCCANTE
	public String descrizione = null; // DB_ESITO_CONTROLLO_PARTICELLA.DESCRIZIONE
	public String descrizioneControllo = null; // DB_TIPO_CONTROLLO.DESCRIZIONE

	public EsitoControlloParticellaVO() {
	}



	public int hashCode() {
		return (idEsitoControlloParticella == null ? 0 : idEsitoControlloParticella.hashCode())+
		(idConduzioneParticella == null ? 0 : idConduzioneParticella.hashCode())+
		(idControllo == null ? 0 : idControllo.hashCode())+
		(bloccante == null ? 0 : bloccante.hashCode())+
		(descrizione == null ? 0 : descrizione.hashCode())+
		(descrizioneControllo == null ? 0 : descrizioneControllo.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof EsitoControlloParticellaVO) {
			EsitoControlloParticellaVO other = (EsitoControlloParticellaVO)o;
			return (this.idEsitoControlloParticella == null && other.idEsitoControlloParticella == null || this.idEsitoControlloParticella.equals(other.idEsitoControlloParticella) &&
					this.idConduzioneParticella == null && other.idConduzioneParticella == null || this.idConduzioneParticella.equals(other.idConduzioneParticella) &&
					this.idControllo == null && other.idControllo == null || this.idControllo.equals(other.idControllo) &&
					this.bloccante == null && other.bloccante == null || this.bloccante.equals(other.bloccante) &&
					this.descrizione == null && other.descrizione == null || this.descrizione.equals(other.descrizione) &&
					this.descrizioneControllo == null && other.descrizioneControllo == null || this.descrizioneControllo.equals(other.descrizioneControllo));

		}
		else {
			return false;
		}
	}

	public String getBloccante() {
		return bloccante;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}

	public Long getIdControllo() {
		return idControllo;
	}

	public Long getIdEsitoControlloParticella() {
		return idEsitoControlloParticella;
	}

	public String getDescrizioneControllo() {
		return descrizioneControllo;
	}

	public void setBloccante(String bloccante) {
		this.bloccante = bloccante;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
	}

	public void setIdControllo(Long idControllo) {
		this.idControllo = idControllo;
	}

	public void setIdEsitoControlloParticella(Long idEsitoControlloParticella) {
		this.idEsitoControlloParticella = idEsitoControlloParticella;
	}

	public void setDescrizioneControllo(String descrizioneControllo) {
		this.descrizioneControllo = descrizioneControllo;
	}
}
