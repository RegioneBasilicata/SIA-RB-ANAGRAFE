package it.csi.solmr.dto.anag.terreni;

import it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.util.ValidationErrors;
import it.csi.solmr.util.Validator;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe che si occupa di mappare gli elementi relativi ai filtri di ricerca dei terreni
 * @author Mauro Vocale
 *
 */
public class FiltriParticellareRicercaVO implements Serializable {

	private static final long serialVersionUID = 8692183121105254450L;
	
	private Long idPianoRiferimento = null;
	private Long idUte = null;
	private String istatComune = null;
	private String sezione = null;
	private String foglio = null;
	private String particella = null;
	private String subalterno = null;
	private Long idUtilizzo = null;
	private String checkUsoPrimario = null;
	private String checkUsoSecondario = null;
	private Long idTitoloPossesso = null;
	private String tipoSegnalazioneBloccante = null;
	private String tipoSegnalazioneWarning = null;
	private String tipoSegnalazioneOk = null;
	private String dataEsecuzioneControlli = null;
	private String dataEsecuzioneControlliDichiarati = null;
	private String orderBy = null;
	private Long idControllo = null;
	private String checkEscludiAsservimento = null;
	private String checkSoloAsservite = null;
	private String checkSoloConferite = "N";
	private String checkEscludiConferimento = "N";
	//private String checkUsoAgronomico = null;
	private java.util.Date dataInserimentoDichiarazione = null;
	private Long idAreaE = null;
	private Long idMacroUso = null;
	private Long idAreaA = null;
	private Long idAreaB = null;
	private Long idAreaC = null;
	private Long idAreaD = null;
	private Long idAreaM = null;
	private Long idAreaF = null;
	private Long idAreaPSN = null;
	private Long idFasciaFluviale = null;
	private Long idAreaEFasciaFluviale = null;
	private Long idAreaG = null;
	private Long idAreaH = null;
	private Long idZonaAltimetrica = null;
	private Long idCasoParticolare = null;
	private Long idTipoDocumento = null;
	private Long idDocumento = null;
	private Long idProtocolloDocumento = null;
	private int paginaCorrente;
	private Long idTipologiaNotifica;
  private Long idCategoriaNotifica;
  private String flagNotificheChiuse;
  private Long idTipoEfa;
  private Long idTipoValoreArea;
  private String flagFoglio;
  private Vector<TipoAreaVO> vTipoArea;
	

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the idUtilizzo
	 */
	public Long getIdUtilizzo() {
		return idUtilizzo;
	}

	/**
	 * @param idUtilizzo the idUtilizzo to set
	 */
	public void setIdUtilizzo(Long idUtilizzo) {
		this.idUtilizzo = idUtilizzo;
	}

	/**
	 * @return the dataEsecuzioneControlli
	 */
	public String getDataEsecuzioneControlli() {
		return dataEsecuzioneControlli;
	}

	/**
	 * @param dataEsecuzioneControlli the dataEsecuzioneControlli to set
	 */
	public void setDataEsecuzioneControlli(String dataEsecuzioneControlli) {
		this.dataEsecuzioneControlli = dataEsecuzioneControlli;
	}

	/**
	 * @return the checkUsoPrimario
	 */
	public String getCheckUsoPrimario() {
		return checkUsoPrimario;
	}

	/**
	 * @param checkUsoPrimario the checkUsoPrimario to set
	 */
	public void setCheckUsoPrimario(String checkUsoPrimario) {
		this.checkUsoPrimario = checkUsoPrimario;
	}

	/**
	 * @return the idPianoRiferimento
	 */
	public Long getIdPianoRiferimento() {
		return idPianoRiferimento;
	}

	/**
	 * @param idPianoRiferimento the idPianoRiferimento to set
	 */
	public void setIdPianoRiferimento(Long idPianoRiferimento) {
		this.idPianoRiferimento = idPianoRiferimento;
	}
	
	/**
	 * @return the checkUsoSecondario
	 */
	public String getCheckUsoSecondario() {
		return checkUsoSecondario;
	}

	/**
	 * @param checkUsoSecondario the checkUsoSecondario to set
	 */
	public void setCheckUsoSecondario(String checkUsoSecondario) {
		this.checkUsoSecondario = checkUsoSecondario;
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
	 * @return the foglio
	 */
	public String getFoglio() {
		return foglio;
	}

	/**
	 * @param foglio the foglio to set
	 */
	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	/**
	 * @return the particella
	 */
	public String getParticella() {
		return particella;
	}

	/**
	 * @param particella the particella to set
	 */
	public void setParticella(String particella) {
		this.particella = particella;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @param sezione the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	/**
	 * @return the subalterno
	 */
	public String getSubalterno() {
		return subalterno;
	}

	/**
	 * @param subalterno the subalterno to set
	 */
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
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
	 * @return the tipoSegnalazioneBloccante
	 */
	public String getTipoSegnalazioneBloccante() {
		return tipoSegnalazioneBloccante;
	}

	/**
	 * @param tipoSegnalazioneBloccante the tipoSegnalazioneBloccante to set
	 */
	public void setTipoSegnalazioneBloccante(String tipoSegnalazioneBloccante) {
		this.tipoSegnalazioneBloccante = tipoSegnalazioneBloccante;
	}

	/**
	 * @return the tipoSegnalazioneOk
	 */
	public String getTipoSegnalazioneOk() {
		return tipoSegnalazioneOk;
	}

	/**
	 * @param tipoSegnalazioneOk the tipoSegnalazioneOk to set
	 */
	public void setTipoSegnalazioneOk(String tipoSegnalazioneOk) {
		this.tipoSegnalazioneOk = tipoSegnalazioneOk;
	}

	/**
	 * @return the tipoSegnalazioneWarning
	 */
	public String getTipoSegnalazioneWarning() {
		return tipoSegnalazioneWarning;
	}

	/**
	 * @param tipoSegnalazioneWarning the tipoSegnalazioneWarning to set
	 */
	public void setTipoSegnalazioneWarning(String tipoSegnalazioneWarning) {
		this.tipoSegnalazioneWarning = tipoSegnalazioneWarning;
	}
	
	/**
	 * @return the dataEsecuzioneControlliDichiarati
	 */
	public String getDataEsecuzioneControlliDichiarati() {
		return dataEsecuzioneControlliDichiarati;
	}

	/**
	 * @param dataEsecuzioneControlliDichiarati the dataEsecuzioneControlliDichiarati to set
	 */
	public void setDataEsecuzioneControlliDichiarati(String dataEsecuzioneControlliDichiarati) {
		this.dataEsecuzioneControlliDichiarati = dataEsecuzioneControlliDichiarati;
	}
	
	/**
	 * @return the idControllo
	 */
	public Long getIdControllo() {
		return idControllo;
	}

	/**
	 * @param idControllo the idControllo to set
	 */
	public void setIdControllo(Long idControllo) {
		this.idControllo = idControllo;
	}

	/**
	 * @return the checkUsoAgronomico
	 */
	/*public String getCheckUsoAgronomico() {
		return checkUsoAgronomico;
	}*/

	/**
	 * @param checkUsoAgronomico the checkUsoAgronomico to set
	 */
	/*public void setCheckUsoAgronomico(String checkUsoAgronomico) {
		this.checkUsoAgronomico = checkUsoAgronomico;
	}*/

	/**
	 * @return the dataInserimentoDichiarazione
	 */
	public java.util.Date getDataInserimentoDichiarazione() {
		return dataInserimentoDichiarazione;
	}

	/**
	 * @param dataInserimentoDichiarazione the dataInserimentoDichiarazione to set
	 */
	public void setDataInserimentoDichiarazione(java.util.Date dataInserimentoDichiarazione) {
		this.dataInserimentoDichiarazione = dataInserimentoDichiarazione;
	}

	/**
	 * @return the idAreaE
	 */
	public Long getIdAreaE() {
		return idAreaE;
	}

	/**
	 * @param idAreaE the idAreaE to set
	 */
	public void setIdAreaE(Long idAreaE) {
		this.idAreaE = idAreaE;
	}

	/**
	 * @return the idMacroUso
	 */
	public Long getIdMacroUso() {
		return idMacroUso;
	}

	/**
	 * @param idMacroUso the idMacroUso to set
	 */
	public void setIdMacroUso(Long idMacroUso) {
		this.idMacroUso = idMacroUso;
	}

	/**
	 * @return the idAreaA
	 */
	public Long getIdAreaA() {
		return idAreaA;
	}

	/**
	 * @param idAreaA the idAreaA to set
	 */
	public void setIdAreaA(Long idAreaA) {
		this.idAreaA = idAreaA;
	}

	/**
	 * @return the idAreaB
	 */
	public Long getIdAreaB() {
		return idAreaB;
	}

	/**
	 * @param idAreaB the idAreaB to set
	 */
	public void setIdAreaB(Long idAreaB) {
		this.idAreaB = idAreaB;
	}

	/**
	 * @return the idAreaC
	 */
	public Long getIdAreaC() {
		return idAreaC;
	}

	/**
	 * @param idAreaC the idAreaC to set
	 */
	public void setIdAreaC(Long idAreaC) {
		this.idAreaC = idAreaC;
	}

	/**
	 * @return the idAreaD
	 */
	public Long getIdAreaD() {
		return idAreaD;
	}

	/**
	 * @param idAreaD the idAreaD to set
	 */
	public void setIdAreaD(Long idAreaD) {
		this.idAreaD = idAreaD;
	}
	
	/**
   * @return the idAreaM
   */
  public Long getIdAreaM() {
    return idAreaM;
  }

  /**
   * @param idAreaM the idAreaM to set
   */
  public void setIdAreaM(Long idAreaM) {
    this.idAreaM = idAreaM;
  }

	/**
	 * @return the idAreaF
	 */
	public Long getIdAreaF() {
		return idAreaF;
	}

	/**
	 * @param idAreaF the idAreaF to set
	 */
	public void setIdAreaF(Long idAreaF) {
		this.idAreaF = idAreaF;
	}
	
	/**
   * @return the idAreaPSN
   */
  public Long getIdAreaPSN() {
    return idAreaPSN;
  }

  /**
   * @param idAreaPSN the idAreaPSN to set
   */
  public void setIdAreaPSN(Long idAreaPSN) {
    this.idAreaPSN = idAreaPSN;
  }
  
  public Long getIdFasciaFluviale()
  {
    return idFasciaFluviale;
  }

  public void setIdFasciaFluviale(Long idFasciaFluviale)
  {
    this.idFasciaFluviale = idFasciaFluviale;
  }
  
  public Long getIdAreaEFasciaFluviale()
  {
    return idAreaEFasciaFluviale;
  }

  public void setIdAreaEFasciaFluviale(Long idAreaEFasciaFluviale)
  {
    this.idAreaEFasciaFluviale = idAreaEFasciaFluviale;
  }
  
  public Long getIdAreaG()
  {
    return idAreaG;
  }

  public void setIdAreaG(Long idAreaG)
  {
    this.idAreaG = idAreaG;
  }

  public Long getIdZonaAltimetrica()
  {
    return idZonaAltimetrica;
  }

  public void setIdZonaAltimetrica(Long idZonaAltimetrica)
  {
    this.idZonaAltimetrica = idZonaAltimetrica;
  }

  public Long getIdCasoParticolare()
  {
    return idCasoParticolare;
  }

  public void setIdCasoParticolare(Long idCasoParticolare)
  {
    this.idCasoParticolare = idCasoParticolare;
  }

	/**
	 * @return the checkEscludiAsservimento
	 */
	public String getCheckEscludiAsservimento() {
		return checkEscludiAsservimento;
	}

	/**
	 * @param checkEscludiAsservimento the checkEscludiAsservimento to set
	 */
	public void setCheckEscludiAsservimento(String checkEscludiAsservimento) {
		this.checkEscludiAsservimento = checkEscludiAsservimento;
	}

	/**
	 * @return the checkSoloAsservite
	 */
	public String getCheckSoloAsservite() {
		return checkSoloAsservite;
	}

	/**
	 * @param checkSoloAsservite the checkSoloAsservite to set
	 */
	public void setCheckSoloAsservite(String checkSoloAsservite) {
		this.checkSoloAsservite = checkSoloAsservite;
	}
	
	public String getCheckSoloConferite()
	{
		return checkSoloConferite;
	}

	public void setCheckSoloConferite(String checkSoloConferite)
	{
		this.checkSoloConferite = checkSoloConferite;
	}

	public String getCheckEscludiConferimento()
	{
		return checkEscludiConferimento;
	}

	public void setCheckEscludiConferimento(String checkEscludiConferimento)
	{
		this.checkEscludiConferimento = checkEscludiConferimento;
	}


	/** 
	 * Metodo equals()
	 */
	/*public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FiltriParticellareRicercaVO other = (FiltriParticellareRicercaVO) obj;
		if (checkUsoPrimario == null) {
			if (other.checkUsoPrimario != null)
				return false;
		} else if (!checkUsoPrimario.equals(other.checkUsoPrimario))
			return false;
		if (checkUsoSecondario == null) {
			if (other.checkUsoSecondario != null)
				return false;
		} else if (!checkUsoSecondario.equals(other.checkUsoSecondario))
			return false;
		if (dataEsecuzioneControlli == null) {
			if (other.dataEsecuzioneControlli != null)
				return false;
		} else if (!dataEsecuzioneControlli.equals(other.dataEsecuzioneControlli))
			return false;
		if (dataEsecuzioneControlliDichiarati == null) {
			if (other.dataEsecuzioneControlliDichiarati != null)
				return false;
		} else if (!dataEsecuzioneControlliDichiarati.equals(other.dataEsecuzioneControlliDichiarati))
			return false;
		if (foglio == null) {
			if (other.foglio != null)
				return false;
		} else if (!foglio.equals(other.foglio))
			return false;
		if (idPianoRiferimento == null) {
			if (other.idPianoRiferimento != null)
				return false;
		} else if (!idPianoRiferimento.equals(other.idPianoRiferimento))
			return false;
		if (idTitoloPossesso == null) {
			if (other.idTitoloPossesso != null)
				return false;
		} else if (!idTitoloPossesso.equals(other.idTitoloPossesso))
			return false;
		if (idUte == null) {
			if (other.idUte != null)
				return false;
		} else if (!idUte.equals(other.idUte))
			return false;
		if (idUtilizzo == null) {
			if (other.idUtilizzo != null)
				return false;
		} else if (!idUtilizzo.equals(other.idUtilizzo))
			return false;
		if (istatComune == null) {
			if (other.istatComune != null)
				return false;
		} else if (!istatComune.equals(other.istatComune))
			return false;
		if (orderBy == null) {
			if (other.orderBy != null)
				return false;
		} else if (!orderBy.equals(other.orderBy))
			return false;
		if (particella == null) {
			if (other.particella != null)
				return false;
		} else if (!particella.equals(other.particella))
			return false;
		if (sezione == null) {
			if (other.sezione != null)
				return false;
		} else if (!sezione.equals(other.sezione))
			return false;
		if (subalterno == null) {
			if (other.subalterno != null)
				return false;
		} else if (!subalterno.equals(other.subalterno))
			return false;
		if (tipoSegnalazioneBloccante == null) {
			if (other.tipoSegnalazioneBloccante != null)
				return false;
		} else if (!tipoSegnalazioneBloccante.equals(other.tipoSegnalazioneBloccante))
			return false;
		if (tipoSegnalazioneOk == null) {
			if (other.tipoSegnalazioneOk != null)
				return false;
		} else if (!tipoSegnalazioneOk.equals(other.tipoSegnalazioneOk))
			return false;
		if (tipoSegnalazioneWarning == null) {
			if (other.tipoSegnalazioneWarning != null)
				return false;
		} else if (!tipoSegnalazioneWarning.equals(other.tipoSegnalazioneWarning))
			return false;
		if (idControllo == null) {
			if (other.idControllo != null)
				return false;
		} else if (!idControllo.equals(other.idControllo))
			return false;
		if (checkEscludiAsservimento == null) {
			if (other.checkEscludiAsservimento != null)
				return false;
		} else if (!checkEscludiAsservimento.equals(other.checkEscludiAsservimento))
			return false;
		if (checkSoloAsservite == null) {
			if (other.checkSoloAsservite != null)
				return false;
		} else if (!checkSoloAsservite.equals(other.checkSoloAsservite))
			return false;
		if (dataInserimentoDichiarazione == null) {
			if (other.dataInserimentoDichiarazione != null)
				return false;
		} else if (!dataInserimentoDichiarazione.equals(other.dataInserimentoDichiarazione))
			return false;
		if (idAreaE == null) {
			if (other.idAreaE != null)
				return false;
		} else if (!idAreaE.equals(other.idAreaE))
			return false;
		if (idMacroUso == null) {
			if (other.idMacroUso != null)
				return false;
		} else if (!idMacroUso.equals(other.idMacroUso))
			return false;
		if (idAreaA == null) {
			if (other.idAreaA != null)
				return false;
		} else if (!idAreaA.equals(other.idAreaA))
			return false;
		if (idAreaB == null) {
			if (other.idAreaB != null)
				return false;
		} else if (!idAreaB.equals(other.idAreaB))
			return false;
		if (idAreaC == null) {
			if (other.idAreaC != null)
				return false;
		} else if (!idAreaC.equals(other.idAreaC))
			return false;
		if (idAreaD == null) {
			if (other.idAreaD != null)
				return false;
		} else if (!idAreaD.equals(other.idAreaD))
			return false;
		if (idAreaM == null) {
      if (other.idAreaM != null)
        return false;
    } else if (!idAreaM.equals(other.idAreaM))
      return false;
		if (idAreaF == null) {
			if (other.idAreaF != null)
				return false;
		} else if (!idAreaF.equals(other.idAreaF))
			return false;
		if (idAreaPSN == null) {
      if (other.idAreaPSN != null)
        return false;
    } else if (!idAreaPSN.equals(other.idAreaPSN))
      return false;
		if (idFasciaFluviale == null) {
      if (other.idFasciaFluviale != null)
        return false;
    } else if (!idFasciaFluviale.equals(other.idFasciaFluviale))
      return false;
		if (idAreaEFasciaFluviale == null) {
      if (other.idAreaEFasciaFluviale != null)
        return false;
    } else if (!idAreaEFasciaFluviale.equals(other.idAreaEFasciaFluviale))
      return false;
		if (idAreaG == null) {
      if (other.idAreaG != null)
        return false;
    } else if (!idAreaG.equals(other.idAreaG))
      return false;
		if (idAreaH == null) {
      if (other.idAreaH != null)
        return false;
    } else if (!idAreaH.equals(other.idAreaH))
      return false;
		if (idZonaAltimetrica == null) {
      if (other.idZonaAltimetrica != null)
        return false;
    } else if (!idZonaAltimetrica.equals(other.idZonaAltimetrica))
      return false;
		if (idCasoParticolare == null) {
      if (other.idCasoParticolare != null)
        return false;
    } else if (!idCasoParticolare.equals(other.idCasoParticolare))
      return false;
		if (idTipoDocumento == null) {
      if (other.idTipoDocumento != null)
        return false;
    } else if (!idTipoDocumento.equals(other.idTipoDocumento))
      return false;
		if (idDocumento == null) {
      if (other.idDocumento != null)
        return false;
    } else if (!idDocumento.equals(other.idDocumento))
      return false;
		if (idProtocolloDocumento == null) {
      if (other.idProtocolloDocumento != null)
        return false;
    } else if (!idProtocolloDocumento.equals(other.idProtocolloDocumento))
      return false;
		if (checkSoloConferite == null) 
		{
			if (other.checkSoloConferite != null)
				return false;
		} 
		else if (!checkSoloConferite.equals(other.checkSoloConferite))
			return false;
		if (checkEscludiConferimento == null) 
		{
			if (other.checkEscludiConferimento != null)
				return false;
		} 
		else if (!checkEscludiConferimento.equals(other.checkEscludiConferimento))
			return false;
		return true;
	}*/
	
	/**
	 * Metodo che si occupa di settare i parametri all'interno del VO e di
	 * controllarne la validità
	 * @param request
	 * @return it.csi.solmr.util.ValidationErrors
	 */
	public ValidationErrors setAndValidateParameter(HttpServletRequest request) {
		ValidationErrors errors = new ValidationErrors();
		// Piano di riferimento
		this.idPianoRiferimento = Long.decode(request.getParameter("idDichiarazioneConsistenza"));
		// Unità produttiva
		if(Validator.isNotEmpty(request.getParameter("idUnitaProduttiva"))) {
			this.idUte = Long.decode(request.getParameter("idUnitaProduttiva"));
		}
		// Comune particella
		if(Validator.isNotEmpty(request.getParameter("istatComuniConduzioniParticelle"))) {
			this.istatComune = request.getParameter("istatComuniConduzioniParticelle");
		}
		// Sezione
		if(Validator.isNotEmpty(request.getParameter("sezione"))) {
			this.sezione = request.getParameter("sezione");
		}
		// Foglio
		if(Validator.isNotEmpty(request.getParameter("foglio"))) {
			this.foglio = request.getParameter("foglio");
		}
		// Particella
		if(Validator.isNotEmpty(request.getParameter("particella"))) {
			this.particella = request.getParameter("particella");
		}
		// Subalterno
		if(Validator.isNotEmpty(request.getParameter("subalterno"))) {
			this.subalterno = request.getParameter("subalterno");
		}
		// USO DEL SUOLO
		this.idUtilizzo = Long.decode(request.getParameter("idTipoUtilizzo"));
		// Check uso primario
		this.checkUsoPrimario = request.getParameter("primario");
		// Check uso secondario
		this.checkUsoSecondario = request.getParameter("secondario");
		if(!Validator.isNotEmpty(this.checkUsoPrimario) && !Validator.isNotEmpty(this.checkUsoSecondario) && this.idUtilizzo.intValue() > 0) {
			this.checkUsoPrimario = (String)SolmrConstants.FLAG_SI;
		}
		// Macro uso
		if(Validator.isNotEmpty(request.getParameter("idMacroUso"))) {
			this.idMacroUso = Long.decode(request.getParameter("idMacroUso"));
		}
		// Titolo possesso
		if(Validator.isNotEmpty(request.getParameter("idTitoloPossesso"))) {
			this.idTitoloPossesso = Long.decode(request.getParameter("idTitoloPossesso"));
		}
		// Segnalazioni
		this.tipoSegnalazioneBloccante = request.getParameter("segnalazioneBloccante");
		this.tipoSegnalazioneWarning = request.getParameter("segnalazioneWarning");
		this.tipoSegnalazioneOk = request.getParameter("segnalazioneOk");
		// Controllo
		if(Validator.isNotEmpty(request.getParameter("idControllo"))) {
			this.idControllo = Long.decode(request.getParameter("idControllo"));
		}
		
		// Escludi asservimento
		this.checkEscludiAsservimento = request.getParameter("escludiAsservimento");
		if(!Validator.isNotEmpty(request.getParameter("escludiAsservimento"))) {
			this.checkEscludiAsservimento = SolmrConstants.FLAG_N;
		}
		// Solo asservite
		this.checkSoloAsservite = request.getParameter("soloAsservite");
		if(!Validator.isNotEmpty(request.getParameter("soloAsservite"))) {
			this.checkSoloAsservite = SolmrConstants.FLAG_N;
		}
		//tipi area
		if(Validator.isNotEmpty(request.getParameter("idTipoValoraArea"))) 
		{ 
		  String idTipoValoraAreaStr = request.getParameter("idTipoValoraArea");
		  StringTokenizer strToken = new StringTokenizer(idTipoValoraAreaStr, "_");
		  this.idTipoValoreArea = new Long(strToken.nextToken());
      this.flagFoglio = strToken.nextToken();
    }
		
    // Zona Altimetrica
    if(Validator.isNotEmpty(request.getParameter("idZonaAltimetrica"))) {
      this.idZonaAltimetrica = Long.decode(request.getParameter("idZonaAltimetrica"));
    }
    // Caso Particolare
    if(Validator.isNotEmpty(request.getParameter("idCasoParticolare"))) {
      this.idCasoParticolare = Long.decode(request.getParameter("idCasoParticolare"));
    }
    // Tipo EFA
    if(Validator.isNotEmpty(request.getParameter("idTipoEfa"))) {
      this.idTipoEfa = Long.decode(request.getParameter("idTipoEfa"));
    }
    
    //Tipologia notifica
    if(Validator.isNotEmpty(request.getParameter("idTipologiaNotifica")))
    {
      this.idTipologiaNotifica = Long.decode(request.getParameter("idTipologiaNotifica"));
    }
    
    //Categoria notifica
    if(Validator.isNotEmpty(request.getParameter("idCategoriaNotifica")))
    {
      this.idCategoriaNotifica = Long.decode(request.getParameter("idCategoriaNotifica"));
    }
    
    // Notifica Chiusa
    this.flagNotificheChiuse = request.getParameter("notificaChiusa");
    
    // Tipo Documento
    if(Validator.isNotEmpty(request.getParameter("idTipoDocumento"))) {
      this.idTipoDocumento = Long.decode(request.getParameter("idTipoDocumento"));
    }
    
    // Documento
    if(Validator.isNotEmpty(request.getParameter("idDocumento"))) {
      this.idDocumento = Long.decode(request.getParameter("idDocumento"));
    }
    
    // Prootocollo Documento
    if(Validator.isNotEmpty(request.getParameter("idProtocolloDocumento"))) {
      this.idProtocolloDocumento = Long.decode(request.getParameter("idProtocolloDocumento"));
    }
    
    // Solo conferite 
		this.checkSoloConferite = request.getParameter("soloConferite");
		if(!Validator.isNotEmpty(request.getParameter("soloConferite")))
			this.checkSoloConferite = SolmrConstants.FLAG_N;
		
		// Escludi conferimento
		this.checkEscludiConferimento = request.getParameter("escludiConferimento");
		if(!Validator.isNotEmpty(request.getParameter("escludiConferimento")))
			this.checkEscludiConferimento = SolmrConstants.FLAG_N;
		
    
		return errors;
	}

  public Long getIdAreaH()
  {
    return idAreaH;
  }

  public void setIdAreaH(Long idAreaH)
  {
    this.idAreaH = idAreaH;
  }

	public int getPaginaCorrente()
	{
		return paginaCorrente;
	}

	public void setPaginaCorrente(int paginaCorrente)
	{
		this.paginaCorrente = paginaCorrente;
	}

  public Long getIdTipoDocumento()
  {
    return idTipoDocumento;
  }

  public void setIdTipoDocumento(Long idTipoDocumento)
  {
    this.idTipoDocumento = idTipoDocumento;
  }

  public Long getIdDocumento()
  {
    return idDocumento;
  }

  public void setIdDocumento(Long idDocumento)
  {
    this.idDocumento = idDocumento;
  }

  public Long getIdProtocolloDocumento()
  {
    return idProtocolloDocumento;
  }

  public void setIdProtocolloDocumento(Long idProtocolloDocumento)
  {
    this.idProtocolloDocumento = idProtocolloDocumento;
  }

  public Long getIdTipologiaNotifica()
  {
    return idTipologiaNotifica;
  }

  public void setIdTipologiaNotifica(Long idTipologiaNotifica)
  {
    this.idTipologiaNotifica = idTipologiaNotifica;
  }

  public Long getIdCategoriaNotifica()
  {
    return idCategoriaNotifica;
  }

  public void setIdCategoriaNotifica(Long idCategoriaNotifica)
  {
    this.idCategoriaNotifica = idCategoriaNotifica;
  }

  public String getFlagNotificheChiuse()
  {
    return flagNotificheChiuse;
  }

  public void setFlagNotificheChiuse(String flagNotificheChiuse)
  {
    this.flagNotificheChiuse = flagNotificheChiuse;
  }

  public Long getIdTipoEfa()
  {
    return idTipoEfa;
  }

  public void setIdTipoEfa(Long idTipoEfa)
  {
    this.idTipoEfa = idTipoEfa;
  }

  public Long getIdTipoValoreArea()
  {
    return idTipoValoreArea;
  }

  public void setIdTipoValoreArea(Long idTipoValoreArea)
  {
    this.idTipoValoreArea = idTipoValoreArea;
  }

  public String getFlagFoglio()
  {
    return flagFoglio;
  }

  public void setFlagFoglio(String flagFoglio)
  {
    this.flagFoglio = flagFoglio;
  }

  public Vector<TipoAreaVO> getvTipoArea()
  {
    return vTipoArea;
  }

  public void setvTipoArea(Vector<TipoAreaVO> vTipoArea)
  {
    this.vTipoArea = vTipoArea;
  }
  
  

}