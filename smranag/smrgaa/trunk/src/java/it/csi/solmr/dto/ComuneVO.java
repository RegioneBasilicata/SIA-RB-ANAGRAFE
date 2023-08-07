package it.csi.solmr.dto;

// <p>Title: S.O.L.M.R.</p>
// <p>Description: Servizi On-Line per il Mondo Rurale</p>
// <p>Copyright: Copyright (c) 2003</p>
// <p>Company: TOBECONFIG</p>
// @author
// @version 1.0

import java.io.*;

public class ComuneVO implements Serializable
{

	/**
	 * L'attributo serialVersionUID è stato aggiunto a tutti i VO per renderli
	 * compatibili con le versioni precedenti utilizzate da eventuali client
	 */
	static final long serialVersionUID = 4232875639745484232L;

	private String istatComune = null;
	private String istatProvincia = null;
	private String descom = null;
	private String cap = null;
	private String flagEstero = null;
	private String codfisc = null;
	private String siglaProv = null;
	private String descrProv = null;
	private Long zonaAlt;
	private String zonaAltS1;
	private String zonaAltS2;
	private String regAgri;
	private String provOld;
	private String comuneOld;
	private String uslOld;
	private String prefisso;
	private String provNew;
	private ProvinciaVO provinciaVO = null;
	private String montana = null;
	private CodeDescription tipoZonaAltimetrica = null;
	private String flagEstinto = null;
	private String siglaEstero = null;
	private String ussl = null;
	private String flagCatastoAttivo = null;
	private CodeDescription areaPsn = null;
	private String gestioneSezione = null;

  /**
	 * @return the cap
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * @param cap the cap to set
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}

	/**
	 * @return the codfisc
	 */
	public String getCodfisc() {
		return codfisc;
	}

	/**
	 * @param codfisc the codfisc to set
	 */
	public void setCodfisc(String codfisc) {
		this.codfisc = codfisc;
	}

	/**
	 * @return the comuneOld
	 */
	public String getComuneOld() {
		return comuneOld;
	}

	/**
	 * @param comuneOld the comuneOld to set
	 */
	public void setComuneOld(String comuneOld) {
		this.comuneOld = comuneOld;
	}

	/**
	 * @return the descom
	 */
	public String getDescom() {
		return descom;
	}

	/**
	 * @param descom the descom to set
	 */
	public void setDescom(String descom) {
		this.descom = descom;
	}

	/**
	 * @return the descrProv
	 */
	public String getDescrProv() {
		return descrProv;
	}

	/**
	 * @param descrProv the descrProv to set
	 */
	public void setDescrProv(String descrProv) {
		this.descrProv = descrProv;
	}

	/**
	 * @return the flagCatastoAttivo
	 */
	public String getFlagCatastoAttivo() {
		return flagCatastoAttivo;
	}

	/**
	 * @param flagCatastoAttivo the flagCatastoAttivo to set
	 */
	public void setFlagCatastoAttivo(String flagCatastoAttivo) {
		this.flagCatastoAttivo = flagCatastoAttivo;
	}

	/**
	 * @return the flagEstero
	 */
	public String getFlagEstero() {
		return flagEstero;
	}

	/**
	 * @param flagEstero the flagEstero to set
	 */
	public void setFlagEstero(String flagEstero) {
		this.flagEstero = flagEstero;
	}

	/**
	 * @return the flagEstinto
	 */
	public String getFlagEstinto() {
		return flagEstinto;
	}

	/**
	 * @param flagEstinto the flagEstinto to set
	 */
	public void setFlagEstinto(String flagEstinto) {
		this.flagEstinto = flagEstinto;
	}

	/**
	 * @return the istatComune
	 */
	public String getIstatComune() {
		return istatComune;
	}

	/**
	 * @param istatComune the istatComune to set
	 */
	public void setIstatComune(String istatComune) {
		this.istatComune = istatComune;
	}

	/**
	 * @return the istatProvincia
	 */
	public String getIstatProvincia() {
		return istatProvincia;
	}

	/**
	 * @param istatProvincia the istatProvincia to set
	 */
	public void setIstatProvincia(String istatProvincia) {
		this.istatProvincia = istatProvincia;
	}

	/**
	 * @return the montana
	 */
	public String getMontana() {
		return montana;
	}

	/**
	 * @param montana the montana to set
	 */
	public void setMontana(String montana) {
		this.montana = montana;
	}

	/**
	 * @return the prefisso
	 */
	public String getPrefisso() {
		return prefisso;
	}

	/**
	 * @param prefisso the prefisso to set
	 */
	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}

	/**
	 * @return the provinciaVO
	 */
	public ProvinciaVO getProvinciaVO() {
		return provinciaVO;
	}

	/**
	 * @param provinciaVO the provinciaVO to set
	 */
	public void setProvinciaVO(ProvinciaVO provinciaVO) {
		this.provinciaVO = provinciaVO;
	}

	/**
	 * @return the provNew
	 */
	public String getProvNew() {
		return provNew;
	}

	/**
	 * @param provNew the provNew to set
	 */
	public void setProvNew(String provNew) {
		this.provNew = provNew;
	}

	/**
	 * @return the provOld
	 */
	public String getProvOld() {
		return provOld;
	}

	/**
	 * @param provOld the provOld to set
	 */
	public void setProvOld(String provOld) {
		this.provOld = provOld;
	}

	/**
	 * @return the regAgri
	 */
	public String getRegAgri() {
		return regAgri;
	}

	/**
	 * @param regAgri the regAgri to set
	 */
	public void setRegAgri(String regAgri) {
		this.regAgri = regAgri;
	}

	/**
	 * @return the siglaEstero
	 */
	public String getSiglaEstero() {
		return siglaEstero;
	}

	/**
	 * @param siglaEstero the siglaEstero to set
	 */
	public void setSiglaEstero(String siglaEstero) {
		this.siglaEstero = siglaEstero;
	}

	/**
	 * @return the siglaProv
	 */
	public String getSiglaProv() {
		return siglaProv;
	}

	/**
	 * @param siglaProv the siglaProv to set
	 */
	public void setSiglaProv(String siglaProv) {
		this.siglaProv = siglaProv;
	}

	/**
	 * @return the tipoZonaAltimetrica
	 */
	public CodeDescription getTipoZonaAltimetrica() {
		return tipoZonaAltimetrica;
	}

	/**
	 * @param tipoZonaAltimetrica the tipoZonaAltimetrica to set
	 */
	public void setTipoZonaAltimetrica(CodeDescription tipoZonaAltimetrica) {
		this.tipoZonaAltimetrica = tipoZonaAltimetrica;
	}

	/**
	 * @return the uslOld
	 */
	public String getUslOld() {
		return uslOld;
	}

	/**
	 * @param uslOld the uslOld to set
	 */
	public void setUslOld(String uslOld) {
		this.uslOld = uslOld;
	}

	/**
	 * @return the ussl
	 */
	public String getUssl() {
		return ussl;
	}

	/**
	 * @param ussl the ussl to set
	 */
	public void setUssl(String ussl) {
		this.ussl = ussl;
	}

	/**
	 * @return the zonaAlt
	 */
	public Long getZonaAlt() {
		return zonaAlt;
	}

	/**
	 * @param zonaAlt the zonaAlt to set
	 */
	public void setZonaAlt(Long zonaAlt) {
		this.zonaAlt = zonaAlt;
	}

	/**
	 * @return the zonaAltS1
	 */
	public String getZonaAltS1() {
		return zonaAltS1;
	}

	/**
	 * @param zonaAltS1 the zonaAltS1 to set
	 */
	public void setZonaAltS1(String zonaAltS1) {
		this.zonaAltS1 = zonaAltS1;
	}

	/**
	 * @return the zonaAltS2
	 */
	public String getZonaAltS2() {
		return zonaAltS2;
	}

	/**
	 * @param zonaAltS2 the zonaAltS2 to set
	 */
	public void setZonaAltS2(String zonaAltS2) {
		this.zonaAltS2 = zonaAltS2;
	}

	/**
	 * @return the areaPsn
	 */
	public CodeDescription getAreaPsn() {
		return areaPsn;
	}

	/**
	 * @param areaPsn the areaPsn to set
	 */
	public void setAreaPsn(CodeDescription areaPsn) {
		this.areaPsn = areaPsn;
	}
	
	public String getGestioneSezione()
  {
    return gestioneSezione;
  }

  public void setGestioneSezione(String gestioneSezione)
  {
    this.gestioneSezione = gestioneSezione;
  }

	
		
}