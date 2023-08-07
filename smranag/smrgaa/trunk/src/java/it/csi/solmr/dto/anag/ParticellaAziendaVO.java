package it.csi.solmr.dto.anag;

import java.io.*;
import java.util.*;

public class ParticellaAziendaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6104904138974591826L;
	private Long idAnagraficaAzienda = null; // Campo ID_ANAGRAFICA_AZIENDA su DB_ANAGRAFICA_AZIENDA
	private Date dataInizioConduzione = null; // Campo DATA_INIZIO_CONDUZIONE su DB_CONDUZIONE_PARTICELLA
	private Date dataFineConduzione = null; // Campo DATA_FINE_CONDUZIONE su DB_CONDUZIONE_PARTICELLA
	private Long idTitoloPossesso = null; // Campo ID_TITOLO_POSSESSO su DB_CONDUZIONE_PARTICELLA
	private String descTitoloPossesso = null; // Campo DESCRIZIONE su DB_TIPO_TITOLO_POSSESSO
	private String superficieCondotta = null; // Campo SUPERFICIE_CONDOTTA su DB_CONDUZIONE_PARTICELLA
	private String denominazione = null; // Campo DENOMINAZIONE su DB_ANAGRAFICA_AZIENDA
	private String cuaa = null; // Campo CUAA su DB_ANAGRAFICA_AZIENDA
	private String istatSedeLegale = null; // Campo SEDELEG_COMUNE su DB_ANAGRAFICA_AZIENDA
	private String descComuneSedeLegale = null; // Campo DESCRIZIONE su COMUNE se comune italiano
	private String descStatoEsteroSedeLegale = null; // Campo DESCRIZIONE su COMUNE se stato estero
	private String istatProvinciaSedeLegale = null; // Campo ISTAT_PROVINCIA su PROVINCIA
	private String siglaProvinciaSedeLegale = null; // Campo SIGLA_PROVINCIA su PROVINCIA
	private Long idConduzioneParticella = null; // Campo ID_CONDUZIONE_PARTICELLA su DB_CONDUZIONE_PARTICELLA

	public ParticellaAziendaVO() {
	}



	public int hashCode() {
		return (idAnagraficaAzienda == null ? 0 : idAnagraficaAzienda.hashCode())+
		(dataInizioConduzione == null ? 0 : dataInizioConduzione.hashCode())+
		(dataFineConduzione == null ? 0 : dataFineConduzione.hashCode())+
		(idTitoloPossesso == null ? 0 : idTitoloPossesso.hashCode())+
		(descTitoloPossesso == null ? 0 : descTitoloPossesso.hashCode())+
		(superficieCondotta == null ? 0 : superficieCondotta.hashCode())+
		(denominazione == null ? 0 : denominazione.hashCode())+
		(cuaa == null ? 0 : cuaa.hashCode())+
		(istatSedeLegale == null ? 0 : istatSedeLegale.hashCode())+
		(descComuneSedeLegale == null ? 0 : descComuneSedeLegale.hashCode())+
		(istatProvinciaSedeLegale == null ? 0 : istatProvinciaSedeLegale.hashCode())+
		(siglaProvinciaSedeLegale == null ? 0 : siglaProvinciaSedeLegale.hashCode())+
		(descStatoEsteroSedeLegale == null ? 0 : descStatoEsteroSedeLegale.hashCode())+
		(idConduzioneParticella == null ? 0 : idConduzioneParticella.hashCode());
	}

	public boolean equals(Object o) {
		if (o instanceof ParticellaAziendaVO) {
			ParticellaAziendaVO other = (ParticellaAziendaVO)o;
			return (this.idAnagraficaAzienda == null && other.idAnagraficaAzienda == null || this.idAnagraficaAzienda.equals(other.idAnagraficaAzienda) &&
					this.dataInizioConduzione == null && other.dataInizioConduzione == null || this.dataInizioConduzione.equals(other.dataInizioConduzione) &&
					this.dataFineConduzione == null && other.dataFineConduzione == null || this.dataFineConduzione.equals(other.dataFineConduzione) &&
					this.idTitoloPossesso == null && other.idTitoloPossesso == null || this.idTitoloPossesso.equals(other.idTitoloPossesso) &&
					this.descTitoloPossesso == null && other.descTitoloPossesso == null || this.descTitoloPossesso.equals(other.descTitoloPossesso) &&
					this.superficieCondotta == null && other.superficieCondotta == null || this.superficieCondotta.equals(other.superficieCondotta) &&
					this.denominazione == null && other.denominazione == null || this.denominazione.equals(other.denominazione) &&
					this.cuaa == null && other.cuaa == null || this.cuaa.equals(other.cuaa) &&
					this.istatSedeLegale == null && other.istatSedeLegale == null || this.istatSedeLegale.equals(other.istatSedeLegale) &&
					this.descComuneSedeLegale == null && other.descComuneSedeLegale == null || this.descComuneSedeLegale.equals(other.descComuneSedeLegale) &&
					this.istatProvinciaSedeLegale == null && other.istatProvinciaSedeLegale == null || this.istatProvinciaSedeLegale.equals(other.istatProvinciaSedeLegale) &&
					this.siglaProvinciaSedeLegale == null && other.siglaProvinciaSedeLegale == null || this.siglaProvinciaSedeLegale.equals(other.siglaProvinciaSedeLegale) &&
					this.descStatoEsteroSedeLegale == null && other.descStatoEsteroSedeLegale == null || this.descStatoEsteroSedeLegale.equals(other.descStatoEsteroSedeLegale) &&
					this.idConduzioneParticella == null && other.idConduzioneParticella == null || this.idConduzioneParticella.equals(other.idConduzioneParticella));

		}
		else {
			return false;
		}
	}

	public String getCuaa() {
		return cuaa;
	}

	public Date getDataFineConduzione() {
		return dataFineConduzione;
	}

	public Date getDataInizioConduzione() {
		return dataInizioConduzione;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public String getDescComuneSedeLegale() {
		return descComuneSedeLegale;
	}

	public String getDescTitoloPossesso() {
		return descTitoloPossesso;
	}

	public Long getIdAnagraficaAzienda() {
		return idAnagraficaAzienda;
	}

	public Long getIdTitoloPossesso() {
		return idTitoloPossesso;
	}

	public String getIstatProvinciaSedeLegale() {
		return istatProvinciaSedeLegale;
	}

	public String getIstatSedeLegale() {
		return istatSedeLegale;
	}

	public String getSiglaProvinciaSedeLegale() {
		return siglaProvinciaSedeLegale;
	}

	public String getSuperficieCondotta() {
		return superficieCondotta;
	}

	public String getDescStatoEsteroSedeLegale() {
		return descStatoEsteroSedeLegale;
	}

	public Long getIdConduzioneParticella() {
		return idConduzioneParticella;
	}

	public void setCuaa(String cuaa) {
		this.cuaa = cuaa;
	}

	public void setDataFineConduzione(Date dataFineConduzione) {
		this.dataFineConduzione = dataFineConduzione;
	}

	public void setDataInizioConduzione(Date dataInizioConduzione) {
		this.dataInizioConduzione = dataInizioConduzione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public void setDescComuneSedeLegale(String descComuneSedeLegale) {
		this.descComuneSedeLegale = descComuneSedeLegale;
	}

	public void setDescTitoloPossesso(String descTitoloPossesso) {
		this.descTitoloPossesso = descTitoloPossesso;
	}

	public void setIdAnagraficaAzienda(Long idAnagraficaAzienda) {
		this.idAnagraficaAzienda = idAnagraficaAzienda;
	}

	public void setIdTitoloPossesso(Long idTitoloPossesso) {
		this.idTitoloPossesso = idTitoloPossesso;
	}

	public void setIstatProvinciaSedeLegale(String istatProvinciaSedeLegale) {
		this.istatProvinciaSedeLegale = istatProvinciaSedeLegale;
	}

	public void setSiglaProvinciaSedeLegale(String siglaProvinciaSedeLegale) {
		this.siglaProvinciaSedeLegale = siglaProvinciaSedeLegale;
	}

	public void setSuperficieCondotta(String superficieCondotta) {
		this.superficieCondotta = superficieCondotta;
	}

	public void setIstatSedeLegale(String istatSedeLegale) {
		this.istatSedeLegale = istatSedeLegale;
	}

	public void setDescStatoEsteroSedeLegale(String descStatoEsteroSedeLegale) {
		this.descStatoEsteroSedeLegale = descStatoEsteroSedeLegale;
	}

	public void setIdConduzioneParticella(Long idConduzioneParticella) {
		this.idConduzioneParticella = idConduzioneParticella;
	}
}
